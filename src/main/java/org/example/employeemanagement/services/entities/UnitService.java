package org.example.employeemanagement.services.entities;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.bridge.role.OrganisationalUnitPersonRole;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.entities.requests.OrganisationRequest;
import org.example.employeemanagement.dto.entities.requests.UnitRequest;
import org.example.employeemanagement.dto.entities.responses.OrganisationReponse;
import org.example.employeemanagement.dto.entities.responses.UnitResponse;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.repositories.entities.OrganisationRepository;
import org.example.employeemanagement.repositories.entities.OrganisationalUnitRepository;
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
public class UnitService {

    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ScopeFactory  scopeFactory;
    private final OrganisationRepository organisationRepository;
    private final OrganisationalUnitRepository organisationalUnitRepository;

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
    public GenericResponse createUnit(UnitRequest request, String creatorEmail) {
        try {
            User creatorUser = getUserByEmail(creatorEmail);
            Scope scope;

            if(request.getParentId() != null)
                scope = scopeFactory.createScope("unit", request.getParentId());
            else
                scope = scopeFactory.createScope("organisation", request.getOrganisationId());
            if(!scope.hasScopedPermission(creatorUser, getPermissionByName("create-sub")))
                return new GenericResponse(401, "You dont have permission to create unit within this organization");

            System.out.println(scope);
            Organisation org = organisationRepository.findById(request.getOrganisationId()).orElseThrow(() -> new UsernameNotFoundException("Organisation not found"));
            System.out.println(org);

            OrganisationalUnit unit = new OrganisationalUnit();
            unit.setUnitDescription(request.getDescription());
            unit.setUnitName(request.getName());
            unit.setOrganisation(org);
            if(request.getParentId() != null){
                OrganisationalUnit parent = organisationalUnitRepository.findById(request.getParentId()).orElseThrow(() -> new UsernameNotFoundException("Parent Unit not found"));
                unit.setParent(parent);
                unit.setUnitLevel(parent.getUnitLevel()+1);
            }
            else
                unit.setUnitLevel(1);
            organisationalUnitRepository.save(unit);
            init_unit_roles(unit, creatorUser, getRoleByName("Admin"));
            return new GenericResponse(200, "Unit created successfully");

        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            throw e; // Re-throw specific exceptions for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Unit: " + e.getMessage(), e);
        }
    }

    private void init_unit_roles(OrganisationalUnit unit, User user, Role role) {
        OrganisationalUnitPersonRole upr = new OrganisationalUnitPersonRole(unit, user.getPerson(), role);
        user.getPerson().getUnitRoles().add(upr);
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public List<UnitResponse> getAllUnits(String email) {
        List<OrganisationalUnit> unitReturnList = new ArrayList<>();
        User user = getUserByEmail(email);
        List<OrganisationalUnit> units = organisationalUnitRepository.findAllByCompanyMembership(email);
        Permission permission = getPermissionByName("view-company");
        if(units == null || units.isEmpty())
            throw new UsernameNotFoundException("no units found");

        for (OrganisationalUnit unit : units) {
            Scope scope = scopeFactory.createScope("unit", unit.getUnitId());
            if (scope.hasScopedPermission(user, permission))
                unitReturnList.add(unit);
        }
        return unitReturnList.stream().map(UnitResponse::new).collect(Collectors.toList());
    }

    public UnitResponse editUnit(Long unitId, UnitRequest request, String creatorEmail) {
        OrganisationalUnit unit = organisationalUnitRepository.findById(unitId).orElseThrow(() -> new UsernameNotFoundException("unit not found: " + unitId));
        Scope scope  = scopeFactory.createScope("unit", unit.getUnitId());
        Role role = getRoleByName("admin");
        if(!(scope.getScopedRole(getUserByEmail(creatorEmail)) == role))
            return null;
        if(request.getDescription() != null)
            unit.setUnitDescription(request.getDescription());
        if(request.getName() != null)
            unit.setUnitName(request.getName());

        organisationalUnitRepository.save(unit);
        return new UnitResponse(unit);
    }

    @Transactional
    public GenericResponse deleteUnit(Long unitId, String email) {
        OrganisationalUnit unit = organisationalUnitRepository.findById(unitId).orElseThrow(() -> new UsernameNotFoundException("unit not found: " + unitId));
        Scope scope  = scopeFactory.createScope("unit", unit.getUnitId());
        Role role = getRoleByName("admin");
        if(!(scope.getScopedRole(getUserByEmail(email)) == role))
            return new GenericResponse(401, "only admin can delete company");
        organisationalUnitRepository.delete(unit);
        return new GenericResponse(200, "Unit '" + unit.getUnitName() + "' deleted successfully.");
    }

}
