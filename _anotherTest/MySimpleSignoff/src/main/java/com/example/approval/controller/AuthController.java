package com.example.approval.controller;

import com.example.approval.config.JwtTokenProvider;
import com.example.approval.dto.LoginRequest;
import com.example.approval.dto.LoginResponse;
import com.example.approval.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
// import org.springframework.security.core.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(ApiResponse.success(new LoginResponse(token, authentication.getName())));
    }
}
