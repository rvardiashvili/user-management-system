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
public class OrganisationPersonPermissionId implements Serializable {
    @Column(name = "organisation_id")
    private Long organisationId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "permission_id")
    private Long permissionId;

    public OrganisationPersonPermissionId(Long organisationId, Long personId, Long permissionId) {
        this.organisationId = organisationId;
        this.personId = personId;
        this.permissionId = permissionId;
    }
}
