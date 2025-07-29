package org.example.employeemanagement.domain.people;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;

    @Column(name="street", nullable=false)
    private String street;

    @Column(name="city", nullable=false)
    private String city;

    @Column(name="state", nullable=false)
    private String state;

    @Column(name="postal_code", nullable=false)
    private String postalCode;

    @Column(name="country", nullable=false)
    private String country;
}
