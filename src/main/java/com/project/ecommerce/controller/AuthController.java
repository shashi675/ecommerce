package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.dto.JwtResponse;
import com.project.ecommerce.dto.LoginRequest;
import com.project.ecommerce.dto.RegisterRequest;
import com.project.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request){

        log.info("Register request for email: {}", request.getEmail());
        String response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(true, "registeration successful", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest request){

        log.info("Login request for email: {}", request.getEmail());
        JwtResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(true, "login successful", response)
        );
    }
}