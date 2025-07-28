package org.example.employeemanagement.services.entities;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationPersonRole;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.entities.responses.OrganisationReponse;
import org.example.employeemanagement.dto.entities.requests.OrganisationRequest;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.repositories.entities.OrganisationRepository;
import org.example.employeemanagement.repositories.people.PersonRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.example.employeemanagement.repositories.permission.PermissionRepository;
import org.example.employeemanagement.repositories.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ScopeFactory  scopeFactory;
    private final OrganisationRepository organisationRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
    private Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
    }


    private Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found: " + companyId));
    }

    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName) // Assuming findByPermissionName
                .orElseThrow(() -> new UsernameNotFoundException("Permission not found: " + permissionName));
    }


    @Transactional
    public GenericResponse createOrganisation(OrganisationRequest request, String creatorEmail) {
        try {
            User creatorUser = getUserByEmail(creatorEmail);
            Person creatorPerson = creatorUser.getPerson();
            Scope scope = scopeFactory.createScope("company", request.getCompanyId());

            if(!scope.hasScopedPermission(creatorUser, getPermissionByName("create-sub")))
                return new GenericResponse(401, "You dont have permission to create organization within this company");

            Company company = getCompanyById(request.getCompanyId());
            Organisation organisation = new Organisation();
            organisation.setOrganisationDescription(request.getDescription());
            organisation.setOrganisationName(request.getName());
            organisation.setCompany(company);
            organisationRepository.save(organisation);
            init_organisation_roles(organisation, creatorUser, getRoleByName("admin"));
            return new GenericResponse(200, "Organisation created successfully");

        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            throw e; // Re-throw specific exceptions for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("Failed to create company: " + e.getMessage(), e);
        }
    }

    private void init_organisation_roles(Organisation organisation, User user, Role role) {
        OrganisationPersonRole opr = new OrganisationPersonRole(organisation, user.getPerson(), role);
        user.getPerson().getOrganizationRoles().add(opr);
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public List<OrganisationReponse> getAllOrganisations(String email) {
        System.out.println("getAllOrganisations");
        User user = getUserByEmail(email);
        List<Company> companies;
        List<Organisation> organisations = new ArrayList<>();
        companies = user.getPerson().getCompanyRoles().stream().map(CompanyPersonRole::getCompany).toList();
        System.out.println(companies);
        Permission permission = getPermissionByName("view-company");
        for(Company company : companies) {
            System.out.println(company);
            for(Organisation organisation : company.getOrganisations()) {
                Scope scope = scopeFactory.createScope("organisation", organisation.getOrganisationId());
                System.out.println(scope);
                if(scope.hasScopedPermission(user, getPermissionByName("view-company")))
                    organisations.add(organisation);
            }
        }

        return organisations.stream().map(OrganisationReponse::new).collect(Collectors.toList());
    }

    public OrganisationReponse editOrganisation(Long organisationId, OrganisationRequest request, String creatorEmail) {
        Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + organisationId));
        Scope scope  = scopeFactory.createScope("organisation", organisationId);
        Role role = getRoleByName("admin");
        if(!(scope.getScopedRole(getUserByEmail(creatorEmail)) == role))
            return null;
        if(request.getDescription() != null)
            organisation.setOrganisationDescription(request.getDescription());
        if(request.getName() != null)
            organisation.setOrganisationName(request.getName());

        organisationRepository.save(organisation);
        return new OrganisationReponse(organisation);
    }

    @Transactional
    public GenericResponse deleteOrganisation(Long organisationId, String email) {
        Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + organisationId));
        Scope scope  = scopeFactory.createScope("organisation", organisationId);
        Role role = getRoleByName("admin");
        if(!(scope.getScopedRole(getUserByEmail(email)) == role))
            return new GenericResponse(401, "only admin can delete company");
        organisationRepository.delete(organisation);
        return new GenericResponse(200, "Organization '" + organisation.getOrganisationName() + "' deleted successfully.");
    }

}
