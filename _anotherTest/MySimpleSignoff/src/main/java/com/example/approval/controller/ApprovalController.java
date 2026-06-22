package com.example.approval.controller;

import com.example.approval.dto.*;
import com.example.approval.entity.*;
import com.example.approval.global.response.ApiResponse;
import com.example.approval.repository.*;
import com.example.approval.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {
    private final ApprovalRequestRepository requestRepository;
    private final ApprovalService approvalService;
    private final ApprovalLogRepository logRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.example.approval.entity.User) {
            return (com.example.approval.entity.User) principal;
        }

        // If it's a Spring Security User, we need to fetch the entity from the database
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found in database: " + username));
        }

        throw new RuntimeException("Unexpected principal type: " + principal.getClass().getName());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApprovalRequestDto>>> list() {
        User user = getCurrentUser();
        List<ApprovalRequest> requests = requestRepository.findAll().stream()
            .filter(r -> r.getAssigneeId().equals(user.getId()) || r.getCreatorId().equals(user.getId()))
            .collect(Collectors.toList());

        List<ApprovalRequestDto> dtos = requests.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApprovalRequestDto>> create(@RequestBody ApprovalRequestDto dto) {
        User user = getCurrentUser();
        ApprovalRequest request = ApprovalRequest.builder()
            .title(dto.getTitle())
            .signoffPersons(dto.getSignoffPersons())
            .mainCategory(dto.getMainCategory())
            .subCategory(dto.getSubCategory())
            .content(dto.getContent())
            .build();

        ApprovalRequest saved = approvalService.createRequest(request, user);
        return ResponseEntity.ok(ApiResponse.success(toDto(saved)));
    }

    @PostMapping("/{id}/action")
    public ResponseEntity<ApiResponse<ApprovalRequestDto>> action(@PathVariable Long id, @RequestBody ActionRequest actionReq) {
        User user = getCurrentUser();

        // For the sake of simplicity in this demo, we need a real User entity
        com.example.approval.entity.User entityUser = userRepository.findByUsername(user.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        ApprovalRequest saved = approvalService.processAction(id, actionReq.getAction(), entityUser);
        return ResponseEntity.ok(ApiResponse.success(toDto(saved)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> detail(@PathVariable Long id) {
        ApprovalRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        User currentUser = getCurrentUser();

        Map<String, Object> result = new HashMap<>();
        result.put("request", toDto(request));
        result.put("logs", logRepository.findByRequestId(id));
        result.put("isCurrentAssignee", Objects.equals(request.getAssigneeId(), currentUser.getId()));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private ApprovalRequestDto toDto(ApprovalRequest r) {
        ApprovalRequestDto d = new ApprovalRequestDto();
        d.setId(r.getId());
        d.setTitle(r.getTitle());
        d.setCreateDate(r.getCreateDate());
        d.setUpdateDate(r.getUpdateDate());
        d.setSignoffPersons(r.getSignoffPersons());
        d.setMainCategory(r.getMainCategory());
        d.setSubCategory(r.getSubCategory());
        d.setContent(r.getContent());
        d.setAssigneeId(r.getAssigneeId());
        d.setCreatorId(r.getCreatorId());
        d.setCurrentStep(r.getCurrentStep());
        d.setComment(r.getComment());

        // Fetch names
        userRepository.findById(r.getAssigneeId())
            .ifPresent(u -> d.setAssigneeName(u.getUsername()));
        userRepository.findById(r.getCreatorId())
            .ifPresent(u -> d.setCreatorName(u.getUsername()));

        return d;
    }
}
