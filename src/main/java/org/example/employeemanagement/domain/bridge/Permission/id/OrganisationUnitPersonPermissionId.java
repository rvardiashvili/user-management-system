package org.example.employeemanagement.domain.bridge.Permission.id;

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
public class OrganisationUnitPersonPermissionId implements Serializable {
    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "permission_id")
    private Long permissionId;

    public OrganisationUnitPersonPermissionId(Long unit_id, Long personId, Long permissionId) {
        this.unitId = unit_id;
        this.personId = personId;
        this.permissionId = permissionId;
    }
}
