package org.example.employeemanagement.controllers.permissions;

import org.example.employeemanagement.dto.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.services.permissions.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PutMapping("/{userId}/{scopeType}/{scopeId}/{roleName}")
    public ResponseEntity<GenericResponse> changeRole(
            @PathVariable Long userId,
            @PathVariable String roleName,
            @PathVariable String scopeType,
            @PathVariable Long scopeId,
            Authentication authentication) {
        return ResponseEntity.ok(roleService.changeRole(userId, roleName, scopeType, scopeId, authentication.getName()));
    }

    @DeleteMapping("/{userId}/{scopeType}/{scopeId}/{roleName}")
    public ResponseEntity<GenericResponse> removeRole(
            @PathVariable Long userId,
            @PathVariable String roleName,
            @PathVariable String scopeType,
            @PathVariable Long scopeId,
            Authentication authentication) {
        return ResponseEntity.ok(roleService.removeRole(userId, roleName, scopeType, scopeId, authentication.getName()));
    }
}
