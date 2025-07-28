package org.example.employeemanagement.domain.bridge.group.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class OrganisationalUnitGroupPersonId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "unit_id")
    private Long unitId;

    public OrganisationalUnitGroupPersonId(Long groupId, Long personId, Long unitId) {
        this.groupId = groupId;
        this.personId = personId;
        this.unitId = unitId;
    }
}
