package org.example.employeemanagement.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "organisations")
@Data
@NoArgsConstructor
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="organisation_id")
    private Long organisationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;

    @Column(name="organisation_name", nullable = false)
    private String organisationName;

    @Column(name="organisation_description")
    private String organisationDescription;

    @ToString.Exclude
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    private List<OrganisationalUnit> units;
}
