package org.example.employeemanagement.controllers.permissions;


import org.example.employeemanagement.dto.GenericResponse;
import org.example.employeemanagement.services.permissions.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PutMapping("/{userId}/{scopeType}/{scopeId}/{permissionName}")
    public ResponseEntity<GenericResponse> givePermission(
            @PathVariable Long userId,
            @PathVariable String permissionName,
            @PathVariable String scopeType,
            @PathVariable Long scopeId,
            Authentication authentication) {
        return ResponseEntity.ok(permissionService.givePermission(userId, permissionName, scopeType, scopeId, authentication.getName()));
    }

    @DeleteMapping("/{userId}/{scopeType}/{scopeId}/{permissionName}")
    public ResponseEntity<GenericResponse> deletePermission(
            @PathVariable Long userId,
            @PathVariable String permissionName,
            @PathVariable String scopeType,
            @PathVariable Long scopeId,
            Authentication authentication) {
        return ResponseEntity.ok(permissionService.deletePermission(userId, permissionName, scopeType, scopeId, authentication.getName()));
    }
}
