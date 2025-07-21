package org.example.employeemanagement.controllers;

import org.example.employeemanagement.dto.SignUpRequest;
import org.example.employeemanagement.dto.UserResponse;
import org.example.employeemanagement.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import org.example.employeemanagement.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserResponse registeredUser = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void login(LoginRequest loginRequest) {
        // This method is a placeholder for Swagger documentation.
        // Spring Security's formLogin handles the actual authentication.
        throw new IllegalStateException("This method should not be called. It's a documentation placeholder.");
    }

}