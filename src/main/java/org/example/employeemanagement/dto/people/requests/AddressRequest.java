package org.example.employeemanagement.dto.people.requests;


import lombok.Data;

@Data
public class AddressRequest {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}