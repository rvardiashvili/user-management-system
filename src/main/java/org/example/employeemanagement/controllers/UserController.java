package org.example.employeemanagement.controllers;


import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyDetails(Authentication authentication) {
        return ResponseEntity.ok(userService.getMyDetails(authentication.getName()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('view-users')")
    public ResponseEntity<List<UserResponse>> getAllUsers(Authentication authentication) {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('view-users')")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('system-permissions')")
    public ResponseEntity<UserDetails> updateUserDetails(@PathVariable Long userId, @Valid @RequestBody AdminUserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUserDetails(userId, request));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyDetails(Authentication authentication, @Valid @RequestBody UpdatePersonRequest request) {
        return ResponseEntity.ok(userService.updateMyDetails(authentication.getName(), request));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email")
    public ResponseEntity<Void> changeEmail(Authentication authentication, @Valid @RequestBody ChangeEmailRequest request) {
        userService.changeEmail(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

}