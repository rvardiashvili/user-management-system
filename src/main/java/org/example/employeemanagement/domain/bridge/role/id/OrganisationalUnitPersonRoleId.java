package org.example.employeemanagement.domain.bridge.role.id;

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
public class OrganisationalUnitPersonRoleId implements Serializable {
    @Column(name = "unit_id")
    private Long organisationalUnitId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "role_id")
    private Long roleId;

    public OrganisationalUnitPersonRoleId(Long unitId, Long personId, Long roleId) {
        this.organisationalUnitId = unitId;
        this.personId = personId;
        this.roleId = roleId;
    }
}
