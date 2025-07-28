package org.example.employeemanagement.domain.authorities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="permission_id")
    private Long id;

    @Column(name="permission_name")
    private String name;

    @Column(name="description")
    private String description;
}







