package com.example.approval.service;

import com.example.approval.entity.ApprovalRequest;
import com.example.approval.entity.User;
import com.example.approval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssigneeResolver {
    private final UserRepository userRepository;

    public Long resolveAssignee(ConfigurationService.StepConfig stepConfig, ApprovalRequest request, String action) {
        String assigneeType = stepConfig.getAssignee();

        if ("creator".equals(assigneeType)) {
            return request.getCreatorId();
        }

        if ("signoff_person".equals(assigneeType)) {
            return resolveSignoffPerson(request, action);
        }

        throw new RuntimeException("Unknown assignee type: " + assigneeType);
    }

    private Long resolveSignoffPerson(ApprovalRequest request, String action) {
        String persons = request.getSignoffPersons();
        if (persons == null || persons.isEmpty()) {
            throw new RuntimeException("No signoff persons configured for this request");
        }

        String[] split = persons.split(",");

        // Case 1: Starting the approval process
        if ("SEND_FOR_APPROVAL".equals(action)) {
            return findUserId(split[0].trim());
        }

        // Case 2: Moving to the next person in the list
        if ("ACCEPT".equals(action)) {
            String currentAssigneeName = userRepository.findById(request.getAssigneeId())
                .map(User::getUsername).orElse("");

            int currentIndex = -1;
            for (int i = 0; i < split.length; i++) {
                if (split[i].trim().equalsIgnoreCase(currentAssigneeName)) {
                    currentIndex = i;
                    break;
                }
            }

            if (currentIndex == -1) {
                // Current assignee not found in list, default to the first person
                return findUserId(split[0].trim());
            } else if (currentIndex < split.length - 1) {
                return findUserId(split[currentIndex + 1].trim());
            }

            // If we reach here, it means we've run out of signoff persons.
            // The transition logic in ApprovalService will handle the step change to "ACCEPT".
            // But we return null here to indicate that no MORE signoff persons are available.
            return null;
        }

        throw new RuntimeException("Action " + action + " is not valid for resolving a signoff person");
    }

    private Long findUserId(String username) {
        return userRepository.findByUsername(username)
            .map(User::getId)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
