package org.example.employeemanagement.controllers;


import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PutMapping("/{userId}/{roleName}")
    @PreAuthorize("hasAuthority('role')")
    public ResponseEntity<GenericResponse> changeRole(@PathVariable Long userId, @PathVariable String roleName, Authentication authentication) {
        return ResponseEntity.ok(roleService.changeRole(userId, roleName, authentication.getAuthorities()));
    }

}