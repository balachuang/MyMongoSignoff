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
    private final AssigneeResolver assigneeResolver;

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

        String nextStepId = configService.getNextStep("OPEN", "SEND_FOR_APPROVAL");
        ConfigurationService.StepConfig stepConfig = configService.getStepConfig(nextStepId);

        request.setCurrentStep(nextStepId);
        request.setAssigneeId(assigneeResolver.resolveAssignee(stepConfig, request, "SEND_FOR_APPROVAL"));

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

        String currentStep = request.getCurrentStep();
        String nextStepId = configService.getNextStep(currentStep, action);

        if (nextStepId == null) {
            throw new RuntimeException("Invalid action " + action + " for step " + currentStep);
        }

        ConfigurationService.StepConfig nextStepConfig = configService.getStepConfig(nextStepId);
        Long nextAssigneeId = assigneeResolver.resolveAssignee(nextStepConfig, request, action);

        // Special case: Loop exhaustion for signoff persons
        if (nextAssigneeId == null && "APPROVING".equals(nextStepId)) {
            // The target was APPROVING but no more people left.
            // Move to the completion step (ACCEPT)
            nextStepId = "ACCEPT";
            nextStepConfig = configService.getStepConfig(nextStepId);
            nextAssigneeId = assigneeResolver.resolveAssignee(nextStepConfig, request, action);
        }

        request.setCurrentStep(nextStepId);
        request.setAssigneeId(nextAssigneeId);

        ApprovalRequest saved = requestRepository.save(request);
        logService.logChanges(oldState, saved, operator.getId());
        return saved;
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
