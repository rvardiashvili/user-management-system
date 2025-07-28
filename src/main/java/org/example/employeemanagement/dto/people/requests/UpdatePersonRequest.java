package org.example.employeemanagement.dto.people.requests;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdatePersonRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
