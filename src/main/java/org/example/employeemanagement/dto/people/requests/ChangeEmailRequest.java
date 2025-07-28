package org.example.employeemanagement.dto.people.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailRequest {
    @NotBlank
    @Email
    private String newEmail;
    @NotBlank
    private String password; // For security, require current password to change email
}