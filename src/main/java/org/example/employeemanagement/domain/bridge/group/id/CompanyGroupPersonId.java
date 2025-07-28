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
public class CompanyGroupPersonId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "company_id")
    private Long companyId;

    public CompanyGroupPersonId(Long groupId, Long personId, Long companyId) {
        this.groupId = groupId;
        this.personId = personId;
        this.companyId = companyId;
    }
}
