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
public class OrganisationPersonRoleId implements Serializable {
    @Column(name = "organisation_id")
    private Long organisationId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "role_id")
    private Long roleId;

    public OrganisationPersonRoleId(Long organisationId, Long personId, Long roleId) {
        this.organisationId = organisationId;
        this.personId = personId;
        this.roleId = roleId;
    }
}
