package org.example.employeemanagement.dto.people;

import lombok.Data;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;


@Data
public class UserDetails {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;

    public UserDetails(){}

    public UserDetails(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        if (user.getPerson() != null) {
            Person person = user.getPerson();
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
        }
    }
}