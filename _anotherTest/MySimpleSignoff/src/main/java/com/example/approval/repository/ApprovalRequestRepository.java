package com.example.approval.repository;

import com.example.approval.entity.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    List<ApprovalRequest> findByAssigneeId(Long assigneeId);
    List<ApprovalRequest> findByCreatorId(Long creatorId);
}
