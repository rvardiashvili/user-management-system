package org.example.employeemanagement.utils;

import lombok.Data;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.bridge.Permission.CompanyPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.OrganisationPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.OrganisationUnitPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.id.CompanyPersonPermissionId;
import org.example.employeemanagement.domain.bridge.Permission.id.OrganisationPersonPermissionId;
import org.example.employeemanagement.domain.bridge.Permission.id.OrganisationUnitPersonPermissionId;
import org.example.employeemanagement.domain.bridge.group.CompanyGroupPerson;
import org.example.employeemanagement.domain.bridge.group.OrganisationGroupPerson;
import org.example.employeemanagement.domain.bridge.group.OrganisationalUnitGroupPerson;
import org.example.employeemanagement.domain.bridge.group.id.CompanyGroupPersonId;
import org.example.employeemanagement.domain.bridge.group.id.OrganisationGroupPersonId;
import org.example.employeemanagement.domain.bridge.group.id.OrganisationalUnitGroupPersonId;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.example.employeemanagement.dto.GenericResponse;
import org.example.employeemanagement.dto.people.UserResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Data
public class Scope {
    boolean isCompany;
    boolean isOrganisation;
    boolean isUnit;
    Long scopeId;

    private final CompanyPersonPermissionRepository companyPersonPermissionRepository;
    private final CompanyPersonRoleRepository companyPersonRoleRepository;
    private final CompanyGroupPersonRepository companyGroupPersonRepository;
    private final CompanyRepository companyRepository;

    private final OrganisationRepository organisationRepository;
    private final OrganisationPersonPermissionRepository organisationPersonPermissionRepository;
    private final OrganisationPersonRoleRepository organisationPersonRoleRepository;
    private final OrganisationGroupPersonRepository organisationGroupPersonRepository;

    private final OrganisationalUnitRepository organisationalUnitRepository;
    private final UnitPersonPermissionRepository unitPersonPermissionRepository;
    private final UnitPersonRoleRepository unitPersonRoleRepository;
    private final UnitGroupPersonRepository unitGroupPersonRepository;

    private final UserRepository userRepository;



    @Autowired
    public Scope(CompanyPersonPermissionRepository companyPersonPermissionRepository, CompanyPersonRoleRepository companyPersonRoleRepository, CompanyRepository companyRepository, UserRepository userRepository,
                 OrganisationRepository organisationRepository, OrganisationPersonPermissionRepository organisationPersonPermissionRepository, OrganisationPersonRoleRepository organisationPersonRoleRepository,
                 OrganisationalUnitRepository organisationalUnitRepository, UnitPersonPermissionRepository unitPersonPermissionRepository, UnitPersonRoleRepository unitPersonRoleRepository,
                 OrganisationGroupPersonRepository organisationGroupPersonRepository, UnitGroupPersonRepository unitGroupPersonRepository, CompanyGroupPersonRepository companyGroupPersonRepository
    ) {
        this.companyPersonPermissionRepository = companyPersonPermissionRepository;
        this.companyPersonRoleRepository = companyPersonRoleRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.organisationPersonPermissionRepository = organisationPersonPermissionRepository;
        this.organisationPersonRoleRepository = organisationPersonRoleRepository;
        this.organisationalUnitRepository = organisationalUnitRepository;
        this.unitPersonPermissionRepository = unitPersonPermissionRepository;
        this.unitPersonRoleRepository = unitPersonRoleRepository;
        this.organisationGroupPersonRepository = organisationGroupPersonRepository;
        this.companyGroupPersonRepository = companyGroupPersonRepository;
        this.unitGroupPersonRepository = unitGroupPersonRepository;

    }

    public Scope(String scopeType, Long scopeId,
                 CompanyPersonPermissionRepository companyPersonPermissionRepository,
                 CompanyPersonRoleRepository companyPersonRoleRepository,
                 CompanyRepository companyRepository,
                 OrganisationRepository organisationRepository,
                 OrganisationPersonPermissionRepository organisationPersonPermissionRepository,
                 OrganisationPersonRoleRepository organisationPersonRoleRepository,
                 OrganisationalUnitRepository organisationalUnitRepository,
                 UnitPersonPermissionRepository unitPersonPermissionRepository,
                 UnitPersonRoleRepository unitPersonRoleRepository,
                 UserRepository userRepository,
                 CompanyGroupPersonRepository companyGroupPersonRepository,
                 OrganisationGroupPersonRepository  organisationGroupPersonRepository,
                 UnitGroupPersonRepository unitGroupPersonRepository
    ) {
        // Initialize scope context
        scopeType = scopeType.toUpperCase();
        this.isCompany = false;
        this.isOrganisation = false;
        this.isUnit = false;

        if ("COMPANY".equals(scopeType)) {
            this.isCompany = true;

        } else if ("ORGANISATION".equals(scopeType)) {
            this.isOrganisation = true;
        } else if ("UNIT".equals(scopeType)) {
            this.isUnit = true;
        } else {
            throw new IllegalArgumentException("Invalid scope type: " + scopeType);
        }
        this.scopeId = scopeId;

        // Assign injected repositories
        this.companyPersonPermissionRepository = companyPersonPermissionRepository;
        this.companyPersonRoleRepository = companyPersonRoleRepository;
        this.companyRepository = companyRepository;
        this.organisationRepository = organisationRepository;
        this.organisationPersonPermissionRepository = organisationPersonPermissionRepository;
        this.organisationPersonRoleRepository = organisationPersonRoleRepository;
        this.organisationalUnitRepository = organisationalUnitRepository;
        this.unitPersonPermissionRepository = unitPersonPermissionRepository;
        this.unitPersonRoleRepository = unitPersonRoleRepository;
        this.userRepository = userRepository;
        this.companyGroupPersonRepository = companyGroupPersonRepository;
        this.organisationGroupPersonRepository = organisationGroupPersonRepository;
        this.unitGroupPersonRepository = unitGroupPersonRepository;
    }


        public List<UserResponse> findAllUsers() {
        if(isCompany) {
            return userRepository.findAllWithCompany(this.scopeId).stream().map(UserResponse::new).collect(Collectors.toList());
        }
        else if(isOrganisation) {
            return userRepository.findAllWithOrganisation(this.scopeId).stream().map(UserResponse::new).collect(Collectors.toList());
        }
        else if(isUnit) {
            return userRepository.findAllWithUnit(this.scopeId).stream().map(UserResponse::new).collect(Collectors.toList());
        }
        return null;
    }

    public Role getScopedRole(User user) {
        Role role = null;
        if(isCompany) {
            Company company = companyRepository.findById(scopeId).orElseThrow(()-> new IllegalStateException("Company Not Found"));
            role = companyPersonRoleRepository.findByCompanyAndPerson(company, user.getPerson()).orElseThrow().getRole();
        }
        else if(isOrganisation) {
            Organisation organisation = organisationRepository.findById(scopeId).orElseThrow(()-> new IllegalStateException("Organisation Not Found"));
            role =  organisationPersonRoleRepository.findByOrganisationAndPerson(organisation, user.getPerson()).orElseThrow().getRole();
        }
        else if(isUnit) {
            OrganisationalUnit unit = organisationalUnitRepository.findById(scopeId).orElseThrow(() -> new IllegalStateException("Unit Not Found"));
            role = unitPersonRoleRepository.findByUnitAndPerson(unit, user.getPerson()).orElseThrow().getRole();
        }
        return role;
    }

    private Scope scopeFactory(String scopeType, Long scopeId){
        return new Scope(scopeType, scopeId, companyPersonPermissionRepository, companyPersonRoleRepository, companyRepository,
                organisationRepository, organisationPersonPermissionRepository, organisationPersonRoleRepository,
                organisationalUnitRepository, unitPersonPermissionRepository, unitPersonRoleRepository, userRepository, companyGroupPersonRepository, organisationGroupPersonRepository, unitGroupPersonRepository);
    }

    public boolean hasScopedPermission(User user, Permission permission) {
        boolean hasPermission = false;
        hasPermission = checkInPermissions(user, permission) | checkInRoles(user, permission) | checkInGroups(user, permission);
        if(!hasPermission) {
            if(this.isCompany) {
                return false;
            }
            else if(this.isOrganisation) {
                Organisation organisation = organisationRepository.findById(this.scopeId).orElse(null);
                Scope scope = scopeFactory("company", organisation.getCompany().getCompanyId());
                return scope.hasScopedPermission(user, permission);
            }
            else if(this.isUnit) {
                OrganisationalUnit organisationalUnit = organisationalUnitRepository.findById(this.scopeId).orElse(null);
                if(organisationalUnit.getUnitLevel() == 1){
                    Scope scope = scopeFactory("organisation", organisationalUnit.getOrganisation().getOrganisationId());
                    return scope.hasScopedPermission(user, permission);
                }
                Scope scope = scopeFactory("unit", organisationalUnit.getParent().getUnitId());
                return scope.hasScopedPermission(user, permission);
            }
        }
        return hasPermission;
    }


    private boolean checkInPermissions(User user, Permission permission) {
        boolean result = false;
        if(this.isCompany){

            CompanyPersonPermissionId id = new CompanyPersonPermissionId(this.scopeId, user.getPerson().getPersonId(), permission.getId());

            result =  companyPersonPermissionRepository.findById(id).isPresent();
        }
        else if(this.isOrganisation){
            OrganisationPersonPermissionId id = new OrganisationPersonPermissionId(this.scopeId, user.getPerson().getPersonId(), permission.getId());

            result = organisationPersonPermissionRepository.findById(id).isPresent();

        }
        else if(this.isUnit){

            OrganisationUnitPersonPermissionId id = new OrganisationUnitPersonPermissionId(this.scopeId, user.getPerson().getPersonId(), permission.getId());

            result = unitPersonPermissionRepository.findById(id).isPresent();
        }
        return result;
    }

    private boolean checkInRoles(User user, Permission permission) {
        Role role;
        Optional<? extends ScopedPersonRole> scopedRole;
        if (this.isCompany) {
            Company company = companyRepository.findById(this.scopeId).orElseThrow(() -> new UsernameNotFoundException("Company not found: " + this.scopeId));

            scopedRole = companyPersonRoleRepository.findByCompanyAndPerson(company, user.getPerson());
        } else if (this.isOrganisation) {

            Organisation org = organisationRepository.findById(this.scopeId).orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + this.scopeId));
            scopedRole = organisationPersonRoleRepository.findByOrganisationAndPerson(org, user.getPerson());

        } else if (this.isUnit) {
            OrganisationalUnit unit = organisationalUnitRepository.findById(this.scopeId).orElseThrow(() -> new UsernameNotFoundException("Unit not found: " + this.scopeId));

            scopedRole = unitPersonRoleRepository.findByUnitAndPerson(unit, user.getPerson());
        }
        else {
            return false;
        }
        role = scopedRole.map(ScopedPersonRole::getRole).orElse(null);
        if(role == null)
            return false;
        for (Permission available_permission : role.getPermissions()) {
            if(permission.getId().equals(available_permission.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkInGroups(User user, Permission permission) {
        if(this.isCompany) {
            Company company = companyRepository.findById(this.scopeId).orElse(null);
            List<CompanyGroupPerson> companyGroupPerson = companyGroupPersonRepository.findByCompanyAndPerson(company, user.getPerson());
            if(companyGroupPerson == null)
                return false;
            List<Group> groups = companyGroupPerson.stream().map(CompanyGroupPerson::getGroup).toList();
            Set<Permission> permissions = groups.stream()
                    .flatMap(group -> group.getPermissions().stream())
                    .collect(Collectors.toSet());
            for(Permission available_permission : permissions) {
                if(permission.getId().equals(available_permission.getId())) {
                    return true;
                }
            }
        }
        else if(this.isOrganisation) {
            Organisation organisation = organisationRepository.findById(this.scopeId).orElse(null);
            List<OrganisationGroupPerson> organisationGroupPeople = organisationGroupPersonRepository.findByOrganisationAndPerson(organisation, user.getPerson());
            if(organisationGroupPeople == null)
                return false;
            List<Group> groups = organisationGroupPeople.stream().map(OrganisationGroupPerson::getGroup).toList();
            Set<Permission> permissions = groups.stream().flatMap(group -> group.getPermissions().stream()).collect(Collectors.toSet());
            for(Permission available_permission : permissions) {
                if(permission.getId().equals(available_permission.getId())) {
                    return true;
                }
            }
        }
        else if(this.isUnit) {
            OrganisationalUnit unit = organisationalUnitRepository.findById(this.scopeId).orElse(null);
            List<OrganisationalUnitGroupPerson> organisationalUnitGroupPeople = unitGroupPersonRepository.findByUnitAndPerson(unit, user.getPerson());
            if(organisationalUnitGroupPeople == null)
                return false;
            List<Group> groups = organisationalUnitGroupPeople.stream().map(OrganisationalUnitGroupPerson::getGroup).toList();
            Set<Permission> permissions = groups.stream().flatMap(group -> group.getPermissions().stream()).collect(Collectors.toSet());
            for(Permission available_permission : permissions) {
                if(permission.getId().equals(available_permission.getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    public GenericResponse givePermission(User user, Permission permission) {
        try {
            if (this.isCompany) {
                CompanyPersonPermission companyPermission = new CompanyPersonPermission();
                companyPermission.setPerson(user.getPerson());
                companyPermission.setPermission(permission);
                companyPermission.setCompany(companyRepository.findById(this.scopeId)
                        .orElseThrow(() -> new UsernameNotFoundException("Company not found: " + this.scopeId)));
                user.getPerson().getCompanyPermissions().add(companyPermission);
            } else if (this.isOrganisation) {
                OrganisationPersonPermission orgPermission = new OrganisationPersonPermission();
                orgPermission.setPerson(user.getPerson());
                orgPermission.setPermission(permission);
                orgPermission.setOrganisation(organisationRepository.findById(this.scopeId)
                        .orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + this.scopeId)));
                user.getPerson().getOrganizationPermissions().add(orgPermission);
            } else if (this.isUnit) {
                OrganisationUnitPersonPermission unitPermission = new OrganisationUnitPersonPermission();
                unitPermission.setPerson(user.getPerson());
                unitPermission.setPermission(permission);
                unitPermission.setUnit(organisationalUnitRepository.findById(this.scopeId)
                        .orElseThrow(() -> new UsernameNotFoundException("Unit not found: " + this.scopeId)));
                user.getPerson().getUnitPermissions().add(unitPermission);
            } else {
                return new GenericResponse(400, "Invalid scope");
            }
            userRepository.save(user);
        }catch (Exception e){
            return new GenericResponse(400, "something went wrong");
        }
        return new GenericResponse(200, "added permission");
    }

    public GenericResponse deletePermission(User user, Permission permission) {
        try{
            if (this.isCompany) {
                Company company = companyRepository.findById(this.scopeId)
                        .orElseThrow(() -> new UsernameNotFoundException("Company not found: " + this.scopeId));

                CompanyPersonPermissionId id = new CompanyPersonPermissionId(this.scopeId, user.getPerson().getPersonId(), permission.getId());

                CompanyPersonPermission companyPermission = companyPersonPermissionRepository.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("Permission not assigned within scope: " + permission.getName()));
                companyPersonPermissionRepository.delete(companyPermission);
            }
            else if (this.isOrganisation) {
                Organisation org = organisationRepository.findById(this.scopeId)
                        .orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + this.scopeId));

                OrganisationPersonPermissionId id = new OrganisationPersonPermissionId(this.scopeId, user.getPerson().getPersonId(), permission.getId());

                OrganisationPersonPermission orgPermission = organisationPersonPermissionRepository.findById(id)
                        .orElseThrow(()-> new UsernameNotFoundException("Permission not assigned within scope: " + permission.getName()));

                organisationPersonPermissionRepository.delete(orgPermission);
            }
            else if (this.isUnit) {
                OrganisationalUnit unit = organisationalUnitRepository.findById(this.scopeId)
                        .orElseThrow(() -> new UsernameNotFoundException("Unit not found: " + this.scopeId));

                OrganisationUnitPersonPermissionId id = new OrganisationUnitPersonPermissionId(this.scopeId, user.getPerson().getPersonId(), permission.getId());
                OrganisationUnitPersonPermission unitPermission = unitPersonPermissionRepository.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("Permission not assigned within scope: " + permission.getName()));

                unitPersonPermissionRepository.delete(unitPermission);

            }

        }catch (Exception e){
            return new GenericResponse(400, "something went wrong");
        }
        return new GenericResponse(200, "deleted permission");

    }

    public void giveGroup(User user, Group group) {
        if(this.isCompany) {
            CompanyGroupPerson cgp = new CompanyGroupPerson();
            Company company = companyRepository.findById(this.scopeId).orElseThrow(() -> new UsernameNotFoundException("Company not found: " + this.scopeId));
            CompanyGroupPersonId id = new CompanyGroupPersonId(this.scopeId, user.getPerson().getPersonId(), group.getId());
            cgp.setPerson(user.getPerson());
            cgp.setGroup(group);
            cgp.setCompany(company);
            cgp.setId(id);
            companyGroupPersonRepository.save(cgp);
        }
        else if(this.isOrganisation) {
            OrganisationGroupPerson ogp = new OrganisationGroupPerson();
            Organisation org = organisationRepository.findById(this.scopeId).orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + this.scopeId));
            OrganisationGroupPersonId id = new OrganisationGroupPersonId(this.scopeId, user.getPerson().getPersonId(), group.getId());
            ogp.setPerson(user.getPerson());
            ogp.setGroup(group);
            ogp.setOrganisation(org);
            ogp.setId(id);
            organisationGroupPersonRepository.save(ogp);
        }
        else if(this.isUnit) {
            OrganisationalUnitGroupPerson ugp = new OrganisationalUnitGroupPerson();
            OrganisationalUnit unit = organisationalUnitRepository.findById(this.scopeId).orElseThrow(() -> new UsernameNotFoundException("Unit not found: " + this.scopeId));
            OrganisationalUnitGroupPersonId id = new OrganisationalUnitGroupPersonId(this.scopeId, user.getPerson().getPersonId(), group.getId());
            ugp.setPerson(user.getPerson());
            ugp.setGroup(group);
            ugp.setUnit(unit);
            ugp.setId(id);
            unitGroupPersonRepository.save(ugp);
        }
    }

    public void removeFromGroup(User user, Group group) {
        if(this.isCompany) {
            CompanyGroupPersonId id = new CompanyGroupPersonId(this.scopeId, user.getPerson().getPersonId(), group.getId());
            companyGroupPersonRepository.deleteById(id);
        }
        else if(this.isOrganisation) {
            OrganisationGroupPersonId id = new OrganisationGroupPersonId(this.scopeId, user.getPerson().getPersonId(),  group.getId());
            organisationGroupPersonRepository.deleteById(id);
        }
        else if(this.isUnit) {
            OrganisationalUnitGroupPersonId id = new OrganisationalUnitGroupPersonId(this.scopeId, user.getPerson().getPersonId(), group.getId());
            unitGroupPersonRepository.deleteById(id);
        }
    }
}
