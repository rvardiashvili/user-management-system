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
public class CompanyPersonRoleId implements Serializable {
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "role_id")
    private Long roleId;

    public CompanyPersonRoleId(Long companyId, Long personId, Long roleId) {
        this.companyId = companyId;
        this.personId = personId;
        this.roleId = roleId;
    }
}
