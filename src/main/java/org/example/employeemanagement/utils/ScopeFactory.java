package org.example.employeemanagement.utils; // Or a more appropriate package like .factories

import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.repositories.entities.OrganisationRepository;
import org.example.employeemanagement.repositories.entities.OrganisationalUnitRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.example.employeemanagement.repositories.group.CompanyGroupPersonRepository;
import org.example.employeemanagement.repositories.group.OrganisationGroupPersonRepository;
import org.example.employeemanagement.repositories.group.UnitGroupPersonRepository;
import org.example.employeemanagement.repositories.permission.CompanyPersonPermissionRepository;
import org.example.employeemanagement.repositories.permission.OrganisationPersonPermissionRepository;
import org.example.employeemanagement.repositories.permission.UnitPersonPermissionRepository;
import org.example.employeemanagement.repositories.role.CompanyPersonRoleRepository;
import org.example.employeemanagement.repositories.role.OrganisationPersonRoleRepository;
import org.example.employeemanagement.repositories.role.UnitPersonRoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // Lombok will generate a constructor with all final fields
public class ScopeFactory {

    // Inject all repositories that the Scope class needs
    private final CompanyPersonPermissionRepository companyPersonPermissionRepository;
    private final CompanyPersonRoleRepository companyPersonRoleRepository;
    private final CompanyRepository companyRepository;
    private final CompanyGroupPersonRepository companyGroupPersonRepository;

    private final OrganisationRepository organisationRepository;
    private final OrganisationPersonPermissionRepository organisationPersonPermissionRepository;
    private final OrganisationPersonRoleRepository organisationPersonRoleRepository;
    private final OrganisationGroupPersonRepository organisationGroupPersonRepository;

    private final OrganisationalUnitRepository organisationalUnitRepository;
    private final UnitPersonPermissionRepository unitPersonPermissionRepository;
    private final UnitPersonRoleRepository unitPersonRoleRepository;
    private final UnitGroupPersonRepository unitGroupPersonRepository;

    private final UserRepository userRepository;

    // Add Group repositories here if needed
    // private final CompanyGroupPersonRepository companyGroupPersonRepository;
    // private final OrganisationGroupPersonRepository organisationGroupPersonRepository;
    // private final UnitGroupPersonRepository unitGroupPersonRepository;

    /**
     * Creates and initializes a new Scope instance with the provided context and injected repositories.
     * This method should be called when you need a new Scope object.
     *
     * @param scopeType The type of scope ("COMPANY", "ORGANISATION", "UNIT").
     * @param scopeId The ID of the specific scope instance.
     * @return A new, initialized Scope instance.
     */
    public Scope createScope(String scopeType, Long scopeId) {
        return new Scope(
                scopeType,
                scopeId,
                companyPersonPermissionRepository,
                companyPersonRoleRepository,
                companyRepository,
                organisationRepository,
                organisationPersonPermissionRepository,
                organisationPersonRoleRepository,
                organisationalUnitRepository,
                unitPersonPermissionRepository,
                unitPersonRoleRepository,
                userRepository,
                companyGroupPersonRepository,
                organisationGroupPersonRepository,
                unitGroupPersonRepository
        );
    }
}
