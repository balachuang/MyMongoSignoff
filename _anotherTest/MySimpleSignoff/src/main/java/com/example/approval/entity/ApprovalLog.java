package com.example.approval.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestId;
    private Long operatorId;
    private LocalDateTime changeDate;
    private String fieldName;
    private String oldValue;
    private String newValue;
}
