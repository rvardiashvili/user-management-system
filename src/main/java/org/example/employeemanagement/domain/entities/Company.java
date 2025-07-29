package org.example.employeemanagement.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.people.Person;

import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="company_id")
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @Column(name="company_name", nullable = false)
    private String companyName;

    @Column(name="company_description")
    private String companyDescription;

    @ToString.Exclude
    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Organisation> organisations;

}
