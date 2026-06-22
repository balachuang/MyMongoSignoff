package com.example.approval.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(columnDefinition = "TEXT")
    private String signoffPersons; // Comma separated usernames

    private String mainCategory;
    private String subCategory;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long assigneeId;
    private Long creatorId;

    @Column(nullable = false)
    private String currentStep; // OPEN, APPROVING, ACCEPT, REJECT

    private String comment;
}
