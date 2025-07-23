package org.example.employeemanagement.dto;

import org.example.employeemanagement.domain.Person;
import org.example.employeemanagement.domain.Position;
import org.example.employeemanagement.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDetails {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Position position;

    public UserDetails(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        if (user.getPerson() != null) {
            Person person = user.getPerson();
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.role = person.getRole().getName();
            this.position = person.getPosition();
        }
    }
}