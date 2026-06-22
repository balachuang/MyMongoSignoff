package com.example.approval.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApprovalRequestDto {
    private Long id;
    private String title;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String signoffPersons;
    private String mainCategory;
    private String subCategory;
    private String content;
    private Long assigneeId;
    private String assigneeName;
    private Long creatorId;
    private String creatorName;
    private String currentStep;
    private String comment;
}
