package org.example.employeemanagement.domain.people;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;

@Entity
@Table(name = "positions")
@Data
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrganisationalUnit unit_id;

    @Column(name="position_name")
    private String name;

    @Column(name="description")
    private String description;

}







