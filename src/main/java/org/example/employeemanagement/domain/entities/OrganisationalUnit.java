package org.example.employeemanagement.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "organisational_units")
@Data
@NoArgsConstructor
public class OrganisationalUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unit_id")
    private Long unitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "organisation_id", nullable = false, unique = true)
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "parent_id", nullable = false, unique = true)
    private OrganisationalUnit parent;

    @Column(name="unit_level")
    private int unitLevel;

    @Column(name="unit_name")
    private String unitName;

    @Column(name="unit_description")
    private String unitDescription;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrganisationalUnit> children;
}
