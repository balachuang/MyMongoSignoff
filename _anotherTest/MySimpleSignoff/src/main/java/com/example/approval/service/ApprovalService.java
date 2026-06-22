package com.example.approval.service;

import com.example.approval.entity.ApprovalRequest;
import com.example.approval.entity.User;
import com.example.approval.repository.ApprovalRequestRepository;
import com.example.approval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
// import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApprovalService {
    private final ApprovalRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ConfigurationService configService;
    private final LogService logService;

    public ApprovalRequest createRequest(ApprovalRequest request, User creator) {
        request.setCreatorId(creator.getId());
        request.setAssigneeId(creator.getId());
        request.setCurrentStep("OPEN");
        return requestRepository.save(request);
    }

    public ApprovalRequest sendForApproval(Long requestId, User operator) {
        ApprovalRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"OPEN".equals(request.getCurrentStep())) {
            throw new RuntimeException("Only requests in OPEN state can be sent for approval");
        }

        ApprovalRequest oldState = cloneRequest(request);

        String nextStep = configService.getNextStep("OPEN", "SEND_FOR_APPROVAL");
        request.setCurrentStep(nextStep);

        // Set first signoff person as assignee
        String firstPerson = getFirstSignoffPerson(request.getSignoffPersons());
        User assignee = userRepository.findByUsername(firstPerson)
            .orElseThrow(() -> new RuntimeException("Signoff person not found: " + firstPerson));

        request.setAssigneeId(assignee.getId());

        ApprovalRequest saved = requestRepository.save(request);
        logService.logChanges(oldState, saved, operator.getId());

        return saved;
    }

    public ApprovalRequest processAction(Long requestId, String action, User operator) {
        ApprovalRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!Objects.equals(request.getAssigneeId(), operator.getId())) {
            throw new RuntimeException("You are not the current assignee");
        }

        ApprovalRequest oldState = cloneRequest(request);

        // Use the action specifically to determine the next step
        String nextStep = configService.getNextStep(request.getCurrentStep(), action);

        if (nextStep == null) {
            throw new RuntimeException("Invalid action " + action + " for step " + request.getCurrentStep());
        }

        // Store the old step for later use if needed, then update to next
        String previousStep = request.getCurrentStep();
        request.setCurrentStep(nextStep);

        if ("SEND_FOR_APPROVAL".equals(action)) {
            String firstPerson = getFirstSignoffPerson(request.getSignoffPersons());
            User assignee = userRepository.findByUsername(firstPerson)
                .orElseThrow(() -> new RuntimeException("Signoff person not found: " + firstPerson));
            request.setAssigneeId(assignee.getId());
        } else if ("ACCEPT".equals(action)) {
            handleAccept(request);
        } else if ("REJECT".equals(action)) {
            handleReject(request);
        } else if ("RE_OPEN".equals(action)) {
            handleReOpen(request);
        }

        ApprovalRequest saved = requestRepository.save(request);
        logService.logChanges(oldState, saved, operator.getId());
        return saved;
    }

    private void handleAccept(ApprovalRequest request) {
        String persons = request.getSignoffPersons();
        if (persons == null || persons.isEmpty()) {
            throw new RuntimeException("No signoff persons configured for this request");
        }

        String[] split = persons.split(",");
        String currentAssigneeName = userRepository.findById(request.getAssigneeId())
            .map(User::getUsername).orElse("");

        int currentIndex = -1;
        for (int i = 0; i < split.length; i++) {
            if (split[i].trim().equalsIgnoreCase(currentAssigneeName)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex < split.length - 1 && currentIndex != -1) {
            // Case: Current assignee found in list, and there are more people to approve
            String nextPersonName = split[currentIndex + 1].trim();
            User nextPerson = userRepository.findByUsername(nextPersonName)
                .orElseThrow(() -> new RuntimeException("Next signoff person not found: " + nextPersonName));
            request.setAssigneeId(nextPerson.getId());
            request.setCurrentStep("APPROVING");
        } else if (currentIndex == -1) {
            // Case: Current assignee not found in list (e.g., Creator just approved)
            // Move to the first person in the list
            String firstPersonName = split[0].trim();
            User firstPerson = userRepository.findByUsername(firstPersonName)
                .orElseThrow(() -> new RuntimeException("First signoff person not found: " + firstPersonName));
            request.setAssigneeId(firstPerson.getId());
            request.setCurrentStep("APPROVING");
        } else {
            // Case: Last person in the list approved
            request.setCurrentStep("ACCEPT");
            User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Creator not found"));
            request.setAssigneeId(creator.getId());
        }
    }

    private void handleReject(ApprovalRequest request) {
        request.setCurrentStep("REJECT");
        User creator = userRepository.findById(request.getCreatorId())
            .orElseThrow(() -> new RuntimeException("Creator not found"));
        request.setAssigneeId(creator.getId());
    }

    private void handleReOpen(ApprovalRequest request) {
        request.setCurrentStep("OPEN");
        User creator = userRepository.findById(request.getCreatorId())
            .orElseThrow(() -> new RuntimeException("Creator not found"));
        request.setAssigneeId(creator.getId());
    }

    private String getFirstSignoffPerson(String persons) {
        if (persons == null || persons.isEmpty()) {
            throw new RuntimeException("No signoff persons specified");
        }
        return persons.split(",")[0].trim();
    }

    private ApprovalRequest cloneRequest(ApprovalRequest request) {
        return ApprovalRequest.builder()
            .id(request.getId())
            .title(request.getTitle())
            .signoffPersons(request.getSignoffPersons())
            .mainCategory(request.getMainCategory())
            .subCategory(request.getSubCategory())
            .content(request.getContent())
            .currentStep(request.getCurrentStep())
            .assigneeId(request.getAssigneeId())
            .comment(request.getComment())
            .build();
    }
}
