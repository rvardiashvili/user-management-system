package org.example.employeemanagement.controllers;


import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.services.PermissionService;
import org.example.employeemanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PutMapping("/{userId}/{permissionName}")
    @PreAuthorize("hasAuthority('permission')")
    public ResponseEntity<PermissionResponse> givePermission(@PathVariable Long userId, @PathVariable String permissionName, Authentication authentication) {
        return ResponseEntity.ok(permissionService.givePermission(userId, permissionName, authentication.getAuthorities()));
    }
    @DeleteMapping("/{userId}/{permissionName}")
    @PreAuthorize("hasAuthority('permission')")
    public ResponseEntity<PermissionResponse> deletePermission(@PathVariable Long userId, @PathVariable String permissionName, Authentication authentication) {
        return ResponseEntity.ok(permissionService.deletePermission(userId, permissionName, authentication.getName()));
    }


}