package com.example.approval.repository;

import com.example.approval.entity.ApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApprovalLogRepository extends JpaRepository<ApprovalLog, Long> {
    List<ApprovalLog> findByRequestId(Long requestId);
}
