package org.example.employeemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionResponse {
    private Long userId;
    private String email;
    private String permissionName; // The permission that was acted upon
    private String scopeType;      // e.g., "COMPANY", "ORGANISATION", "UNIT"
    private Long scopeId;          // The ID of the specific company, organisation, or unit
    private boolean operationSuccess; // true if the operation was successful
    private String message;        // A message about the operation result

    // Constructor for success (optional, for convenience)
    public PermissionResponse(Long userId, String email, String permissionName, String scopeType, Long scopeId, String message) {
        this.userId = userId;
        this.email = email;
        this.permissionName = permissionName;
        this.scopeType = scopeType;
        this.scopeId = scopeId;
        this.operationSuccess = true;
        this.message = message;
    }

    // Constructor for failure (optional, for convenience)
    public PermissionResponse(String message) {
        this.operationSuccess = false;
        this.message = message;
    }
}
