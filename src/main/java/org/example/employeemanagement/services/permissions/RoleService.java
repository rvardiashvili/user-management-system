package org.example.employeemanagement.services.permissions;

import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationalUnitPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.CompanyPersonRoleId;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationPersonRoleId;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationalUnitPersonRoleId;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.example.employeemanagement.dto.GenericResponse; // Assuming GenericResponse is suitable
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.repositories.entities.OrganisationRepository;
import org.example.employeemanagement.repositories.entities.OrganisationalUnitRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.example.employeemanagement.repositories.role.CompanyPersonRoleRepository;
import org.example.employeemanagement.repositories.role.OrganisationPersonRoleRepository;
import org.example.employeemanagement.repositories.role.RoleRepository;
import org.example.employeemanagement.repositories.role.UnitPersonRoleRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final OrganisationRepository organisationRepository;
    private final OrganisationalUnitRepository organisationalUnitRepository;

    private final CompanyPersonRoleRepository companyPersonRoleRepository;
    private final OrganisationPersonRoleRepository organisationPersonRoleRepository;
    private final UnitPersonRoleRepository unitPersonRoleRepository;

    private final ScopeFactory scopeFactory;

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName) // Assuming findByRoleName
                .orElseThrow(() -> new UsernameNotFoundException("Role not found: " + roleName));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    private User saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
        return user;
    }

    @Transactional
    public GenericResponse changeRole(Long userId, String roleName, String scopeType, Long scopeId, String currentAuthenticatedUserEmail) {
        User userToModify = getUserById(userId);
        Person personToModify = userToModify.getPerson();

        if (personToModify == null) {
            return new GenericResponse(400, "User has no associated person record.");
        }

        Role newRole = getRoleByName(roleName);

        User currentUser = userRepository.findByEmail(currentAuthenticatedUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Current authenticated user not found: " + currentAuthenticatedUserEmail));
        Person currentPerson = currentUser.getPerson();

        if (currentPerson == null) {
            return new GenericResponse(400, "Current authenticated user has no associated person record.");
        }

        try {
            Object scopeEntity;
            String currentScopeAdminRoleName;
            Optional<? extends ScopedPersonRole> currentPersonScopedRoleOptional;
            Optional<? extends ScopedPersonRole> personToModifyCurrentScopedRoleOptional;

            switch (scopeType.toUpperCase()) {
                case "COMPANY":
                    Company company = companyRepository.findById(scopeId)
                            .orElseThrow(() -> new UsernameNotFoundException("Company not found: " + scopeId));
                    scopeEntity = company;
                    currentScopeAdminRoleName = "admin";
                    currentPersonScopedRoleOptional = companyPersonRoleRepository.findByCompanyAndPerson(company, currentPerson);
                    personToModifyCurrentScopedRoleOptional = companyPersonRoleRepository.findByCompanyAndPerson(company, personToModify);
                    break;
                case "ORGANISATION":
                    Organisation organisation = organisationRepository.findById(scopeId)
                            .orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + scopeId));
                    scopeEntity = organisation;
                    currentScopeAdminRoleName = "Org Admin"; // Global role name for admin in this scope
                    currentPersonScopedRoleOptional = organisationPersonRoleRepository.findByOrganisationAndPerson(organisation, currentPerson);
                    personToModifyCurrentScopedRoleOptional = organisationPersonRoleRepository.findByOrganisationAndPerson(organisation, personToModify);
                    break;
                case "UNIT":
                    OrganisationalUnit unit = organisationalUnitRepository.findById(scopeId)
                            .orElseThrow(() -> new UsernameNotFoundException("Organisational Unit not found: " + scopeId));
                    scopeEntity = unit;
                    currentScopeAdminRoleName = "Unit Admin"; // Global role name for admin in this scope
                    currentPersonScopedRoleOptional = unitPersonRoleRepository.findByUnitAndPerson(unit, currentPerson);
                    personToModifyCurrentScopedRoleOptional = unitPersonRoleRepository.findByUnitAndPerson(unit, personToModify);
                    break;
                default:
                    return new GenericResponse(400, "Invalid scope type: " + scopeType);
            }

            boolean currentUserIsScopeAdmin = currentPersonScopedRoleOptional
                    .map(r -> r.getRole().getName().equals(currentScopeAdminRoleName))
                    .orElse(false);

            boolean newRoleIsAdminOrModerator = newRole.getName().equals(currentScopeAdminRoleName) ||
                    newRole.getName().equals("moderator");

            boolean targetUserCurrentRoleIsAdminOrModerator = personToModifyCurrentScopedRoleOptional
                    .map(r -> r.getRole().getName().equals(currentScopeAdminRoleName) || r.getRole().getName().equals("moderator"))
                    .orElse(false);


            if (newRoleIsAdminOrModerator && !currentUserIsScopeAdmin) {
                return new GenericResponse(403, "Only " + currentScopeAdminRoleName + " can assign " + newRole.getName() + " role within this scope.");
            }

            if (targetUserCurrentRoleIsAdminOrModerator && !currentUserIsScopeAdmin) {
                String targetRoleName = personToModifyCurrentScopedRoleOptional.get().getRole().getName();
                return new GenericResponse(403, "Only " + currentScopeAdminRoleName + " can change roles for " + targetRoleName + " users within this scope.");
            }

            switch (scopeType.toUpperCase()) {
                case "COMPANY":
                    Company company = (Company) scopeEntity;
                    personToModifyCurrentScopedRoleOptional.ifPresent(companyPersonRoleRepository::delete);
                    companyPersonRoleRepository.save(new CompanyPersonRole(company, personToModify, newRole));
                    break;
                case "ORGANISATION":
                    Organisation organisation = (Organisation) scopeEntity;
                    personToModifyCurrentScopedRoleOptional.ifPresent(organisationPersonRoleRepository::delete);
                    organisationPersonRoleRepository.save(new OrganisationPersonRole(organisation, personToModify, newRole));
                    Company company1 = organisation.getCompany();
                    companyPersonRoleRepository.save(new CompanyPersonRole(company1, personToModify, getRoleByName("user")));
                    break;
                case "UNIT":
                    OrganisationalUnit unit = (OrganisationalUnit) scopeEntity;
                    personToModifyCurrentScopedRoleOptional.ifPresent(unitPersonRoleRepository::delete);

                    init_unit_role(unit, newRole, personToModify);
                    break;
                default:
                    // This case should ideally not be reached due to earlier switch
                    return new GenericResponse(400, "Invalid scope type: " + scopeType);
            }

            return new GenericResponse(200, "User role changed to '" + roleName + "' in " + scopeType.toLowerCase() + " " + scopeEntity.getClass().getSimpleName() + " " + scopeId);

        } catch (UsernameNotFoundException e) {
            return new GenericResponse(404, e.getMessage());
        } catch (Exception e) {
            return new GenericResponse(500, "Failed to change role: " + e.getMessage());
        }
    }

    private void init_unit_role(OrganisationalUnit unit, Role role, Person personToModify) {
        Organisation org =  unit.getOrganisation();
        Company comp = org.getCompany();
        Role defaultRole = getRoleByName("user");
        if(personToModify.getOrganizationRoles().stream().map(OrganisationPersonRole::getOrganisation).noneMatch(organisation -> organisation.equals(org))){
            organisationPersonRoleRepository.save(new OrganisationPersonRole(org, personToModify, defaultRole));
        }
        if(personToModify.getCompanyRoles().stream().map(CompanyPersonRole::getCompany).noneMatch(company -> company.equals(comp))){
            companyPersonRoleRepository.save(new CompanyPersonRole(comp, personToModify, defaultRole));
        }

        apply_unit_role(unit, role, personToModify);

    }
    private void apply_unit_role(OrganisationalUnit unit, Role role, Person personToModify){
        Role defaultRole = getRoleByName("user");
        if(unit.getParent() != null){
            if(personToModify.getUnitRoles().stream().map(OrganisationalUnitPersonRole::getUnit).noneMatch(orgUnit -> orgUnit.equals(unit.getParent()))){
                init_unit_role(unit.getParent(), defaultRole, personToModify);
            }
        }
        unitPersonRoleRepository.save(new OrganisationalUnitPersonRole(unit, personToModify, role));
    }
    public GenericResponse removeRole(Long userId, String roleName, String scopeType, Long scopeId, String currentAuthenticatedUserEmail) {
        User userToModify = getUserById(userId);
        Person personToModify = userToModify.getPerson();

        if (personToModify == null) {
            return new GenericResponse(400, "User has no associated person record.");
        }

        Role newRole = getRoleByName(roleName);

        User currentUser = userRepository.findByEmail(currentAuthenticatedUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Current authenticated user not found: " + currentAuthenticatedUserEmail));
        Person currentPerson = currentUser.getPerson();

        if (currentPerson == null) {
            return new GenericResponse(400, "Current authenticated user has no associated person record.");
        }

        try {
            Object scopeEntity;
            String currentScopeAdminRoleName;
            Optional<? extends ScopedPersonRole> currentPersonScopedRoleOptional;
            Optional<? extends ScopedPersonRole> personToModifyCurrentScopedRoleOptional;

            switch (scopeType.toUpperCase()) {
                case "COMPANY":
                    Company company = companyRepository.findById(scopeId)
                            .orElseThrow(() -> new UsernameNotFoundException("Company not found: " + scopeId));
                    scopeEntity = company;
                    currentScopeAdminRoleName = "admin";
                    currentPersonScopedRoleOptional = companyPersonRoleRepository.findByCompanyAndPerson(company, currentPerson);
                    personToModifyCurrentScopedRoleOptional = companyPersonRoleRepository.findByCompanyAndPerson(company, personToModify);
                    break;
                case "ORGANISATION":
                    Organisation organisation = organisationRepository.findById(scopeId)
                            .orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + scopeId));
                    scopeEntity = organisation;
                    currentScopeAdminRoleName = "Org Admin"; // Global role name for admin in this scope
                    currentPersonScopedRoleOptional = organisationPersonRoleRepository.findByOrganisationAndPerson(organisation, currentPerson);
                    personToModifyCurrentScopedRoleOptional = organisationPersonRoleRepository.findByOrganisationAndPerson(organisation, personToModify);
                    break;
                case "UNIT":
                    OrganisationalUnit unit = organisationalUnitRepository.findById(scopeId)
                            .orElseThrow(() -> new UsernameNotFoundException("Organisational Unit not found: " + scopeId));
                    scopeEntity = unit;
                    currentScopeAdminRoleName = "Unit Admin"; // Global role name for admin in this scope
                    currentPersonScopedRoleOptional = unitPersonRoleRepository.findByUnitAndPerson(unit, currentPerson);
                    personToModifyCurrentScopedRoleOptional = unitPersonRoleRepository.findByUnitAndPerson(unit, personToModify);
                    break;
                default:
                    return new GenericResponse(400, "Invalid scope type: " + scopeType);
            }

            boolean currentUserIsScopeAdmin = currentPersonScopedRoleOptional
                    .map(r -> r.getRole().getName().equals(currentScopeAdminRoleName))
                    .orElse(false);

            boolean newRoleIsAdminOrModerator = newRole.getName().equals(currentScopeAdminRoleName) ||
                    newRole.getName().equals("moderator");

            boolean targetUserCurrentRoleIsAdminOrModerator = personToModifyCurrentScopedRoleOptional
                    .map(r -> r.getRole().getName().equals(currentScopeAdminRoleName) || r.getRole().getName().equals("moderator"))
                    .orElse(false);


            if (newRoleIsAdminOrModerator && !currentUserIsScopeAdmin) {
                return new GenericResponse(403, "Only " + currentScopeAdminRoleName + " can assign " + newRole.getName() + " role within this scope.");
            }

            if (targetUserCurrentRoleIsAdminOrModerator && !currentUserIsScopeAdmin) {
                String targetRoleName = personToModifyCurrentScopedRoleOptional.get().getRole().getName();
                return new GenericResponse(403, "Only " + currentScopeAdminRoleName + " can change roles for " + targetRoleName + " users within this scope.");
            }

            switch (scopeType.toUpperCase()) {
                case "COMPANY":

                    companyPersonRoleRepository.deleteById(new CompanyPersonRoleId(scopeId, personToModify.getPersonId(), newRole.getId()));
                    break;
                case "ORGANISATION":
                    organisationPersonRoleRepository.deleteById(new OrganisationPersonRoleId(scopeId, personToModify.getPersonId(), newRole.getId()));
                    break;
                case "UNIT":
                    unitPersonRoleRepository.deleteById(new OrganisationalUnitPersonRoleId(scopeId, personToModify.getPersonId(), newRole.getId()));
                    break;
                default:
                    // This case should ideally not be reached due to earlier switch
                    return new GenericResponse(400, "Invalid scope type: " + scopeType);
            }

            return new GenericResponse(200, "User role changed to '" + roleName + "' in " + scopeType.toLowerCase() + " " + scopeEntity.getClass().getSimpleName() + " " + scopeId);

        } catch (UsernameNotFoundException e) {
            return new GenericResponse(404, e.getMessage());
        } catch (Exception e) {
            return new GenericResponse(500, "Failed to change role: " + e.getMessage());
        }
    }
}
