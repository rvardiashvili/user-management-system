package org.example.employeemanagement.controllers.people;


import org.example.employeemanagement.dto.people.*;
import org.example.employeemanagement.dto.people.requests.AdminUserUpdateRequest;
import org.example.employeemanagement.dto.people.requests.ChangeEmailRequest;
import org.example.employeemanagement.dto.people.requests.ChangePasswordRequest;
import org.example.employeemanagement.dto.people.requests.UpdatePersonRequest;
import org.example.employeemanagement.services.people.UserService;
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
        return ResponseEntity.ok(userService.getDetailsSelf(authentication.getName()));
    }

    @GetMapping("/users/{scope_type}/{scope_id}/email/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String scope_type, @PathVariable Long scope_id, @PathVariable String email, Authentication authentication) {
        return ResponseEntity.ok(userService.getDetails(email, scope_type, scope_id, authentication.getName()));
    }

    @GetMapping("/all/{scope_type}/{scope_id}")
    public ResponseEntity<List<UserResponse>> getAllUsers(@PathVariable String scope_type, @PathVariable Long scope_id, Authentication authentication) {
        return ResponseEntity.ok(userService.getAllUsers(scope_type, scope_id, authentication.getName()));
    }

    @GetMapping("/users/{scope_type}/{scope_id}/{user_id}")
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable String scope_type, @PathVariable Long scope_id, @PathVariable Long user_id, Authentication authentication) {
        return ResponseEntity.ok(userService.getDetails(user_id, scope_type, scope_id, authentication.getName()));
    }

    @PutMapping("/users/{userId}")
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