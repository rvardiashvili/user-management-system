package org.example.employeemanagement.controllers.people;

import org.example.employeemanagement.dto.people.requests.SignUpRequest;
import org.example.employeemanagement.dto.people.UserResponse;
import org.example.employeemanagement.services.people.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import org.example.employeemanagement.dto.people.requests.LoginRequest;

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
        throw new IllegalStateException("This method should not be called. It's a documentation placeholder.");
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void logout() {
        throw new IllegalStateException("This method should not be called. It's a documentation placeholder.");
    }

}