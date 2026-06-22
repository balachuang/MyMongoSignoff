package com.example.approval.service;

import com.example.approval.entity.ApprovalLog;
import com.example.approval.entity.ApprovalRequest;
import com.example.approval.repository.ApprovalLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LogService {
    private final ApprovalLogRepository logRepository;

    public void logChanges(ApprovalRequest oldReq, ApprovalRequest newReq, Long operatorId) {
        if (oldReq == null) return;

        logField(oldReq.getId(), operatorId, "title", oldReq.getTitle(), newReq.getTitle());
        logField(oldReq.getId(), operatorId, "signoffPersons", oldReq.getSignoffPersons(), newReq.getSignoffPersons());
        logField(oldReq.getId(), operatorId, "mainCategory", oldReq.getMainCategory(), newReq.getMainCategory());
        logField(oldReq.getId(), operatorId, "subCategory", oldReq.getSubCategory(), newReq.getSubCategory());
        logField(oldReq.getId(), operatorId, "content", oldReq.getContent(), newReq.getContent());
        logField(oldReq.getId(), operatorId, "currentStep", oldReq.getCurrentStep(), newReq.getCurrentStep());
        logField(oldReq.getId(), operatorId, "assigneeId", String.valueOf(oldReq.getAssigneeId()), String.valueOf(newReq.getAssigneeId()));
        logField(oldReq.getId(), operatorId, "comment", oldReq.getComment(), newReq.getComment());
    }

    private void logField(Long requestId, Long operatorId, String fieldName, String oldVal, String newVal) {
        if (!Objects.equals(oldVal, newVal)) {
            ApprovalLog log = ApprovalLog.builder()
                .requestId(requestId)
                .operatorId(operatorId)
                .changeDate(LocalDateTime.now())
                .fieldName(fieldName)
                .oldValue(oldVal)
                .newValue(newVal)
                .build();
            logRepository.save(log);
        }
    }
}
