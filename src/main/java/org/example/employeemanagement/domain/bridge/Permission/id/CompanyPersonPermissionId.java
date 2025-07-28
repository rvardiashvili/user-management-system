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
public class CompanyPersonPermissionId implements Serializable {
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "permission_id")
    private Long permissionId;

    public CompanyPersonPermissionId(Long companyId, Long personId, Long permissionId) {
        this.companyId = companyId;
        this.personId = personId;
        this.permissionId = permissionId;
    }
}
