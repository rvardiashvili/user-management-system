package org.example.employeemanagement.services.permissions;

import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.people.UserResponse;
import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.group.GroupRepository;
import org.example.employeemanagement.repositories.permission.PermissionRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GroupService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ScopeFactory scopeFactory;
    private final CompanyRepository companyRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }
    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + permissionName));
    }

    @Transactional
    public GroupResponse createGroup(Long company_id, GroupRequest request, String email) {
        Group group = new Group();
        if(request.getName() == null || request.getName().isEmpty()){
            return  null;
        }
        try{
            User user = getUserByEmail(email);
            Permission permission = getPermissionByName("group");
            Scope scope = scopeFactory.createScope("company", company_id);
            if(!scope.hasScopedPermission(user, permission))
                throw new AccessDeniedException("You do not have permission to create group");
            group.setName(request.getName());
            group.setCompany(companyRepository.findById(company_id).orElseThrow(() -> new UsernameNotFoundException("Company not found: " + company_id)));
            Set<Permission> permissions = new HashSet<>();
            for(String permissionName : request.getPermissions()){
                if(scope.hasScopedPermission(user, getPermissionByName(permissionName)))
                    permissions.add(getPermissionByName(permissionName));
            }
            group.setPermissions(permissions);
            groupRepository.save(group);

        }catch (DataAccessException e){
            throw e;
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        return new GroupResponse(group);
        
    }

    @Transactional
    public GroupResponse addGroupPermissions(Long id, Set<String> request, String email) {
        User user = getUserByEmail(email);
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));

        if(request == null || request.isEmpty()){
            return new GroupResponse(group);
        }
        Scope scope = scopeFactory.createScope("company", group.getCompany().getCompanyId());
        Set<Permission> permissions = group.getPermissions();
        for(String permissionName : request){
            if(scope.hasScopedPermission(user, getPermissionByName(permissionName)))
                permissions.add(getPermissionByName(permissionName));
        }
        group.setPermissions(permissions);
        try {
            groupRepository.save(group);
        }catch (DataAccessException e){
            throw e;
        }
        return new GroupResponse(group);
    }

    @Transactional
    public GroupResponse updateGroupName(Long id, String name, String email) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
        if(name == null || name.isEmpty())
            return new GroupResponse(group);;
        Scope scope = scopeFactory.createScope("company", group.getCompany().getCompanyId());
        Permission permission = getPermissionByName("group");
        if(!scope.hasScopedPermission(getUserByEmail(email), permission))
            throw new RuntimeException("You do not have permission to update group");

        group.setName(name);
        try {
            groupRepository.save(group);
        }catch (DataAccessException e){
            throw e;
        }
        return new GroupResponse(group);
    }

    @Transactional
    public GroupResponse viewGroupById(Long id, String email) {
        User user = getUserByEmail(email);
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
        Scope scope = scopeFactory.createScope("company", group.getCompany().getCompanyId());
        if(scope.hasScopedPermission(user, getPermissionByName("group")))
            return new GroupResponse(group);;
        return null;
    }
    public List<GroupResponse> viewAllGroups(Long company_id, String email) {
        User user = getUserByEmail(email);
        Scope scope = scopeFactory.createScope("company", company_id);
        Company company = companyRepository.findById(company_id).orElseThrow();
        List<Group> groups = groupRepository.findAllByCompany(company);
        List<GroupResponse> groupResponses = groups.stream().map(GroupResponse::new).toList();
        if(scope.hasScopedPermission(user, getPermissionByName("group")))
            return groupResponses;
        return null;
    }

    @Transactional
    public GroupResponse deleteGroup(Long id,  String email) {
        User user = getUserByEmail(email);
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
        Scope scope = scopeFactory.createScope("company", group.getCompany().getCompanyId());
        if(scope.hasScopedPermission(user, getPermissionByName("group"))){
        groupRepository.delete(group);
        return new GroupResponse(group);
        }
        return null;
    }

    @Transactional
    public GroupResponse deletePermissions(Long group_id, Set<String> permissions, String email) {
        User user = getUserByEmail(email);
        Group group = groupRepository.findById(group_id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + group_id));
        Scope scope = scopeFactory.createScope("company", group.getCompany().getCompanyId());
        if(!scope.hasScopedPermission(user, getPermissionByName("group")))
            throw new RuntimeException("You do not have permission to delete group");
        for(String permissionName : permissions){
            group.getPermissions().remove(getPermissionByName(permissionName));
        }
        try {
            groupRepository.save(group);
        }catch (DataAccessException e){
            throw e;
        }
        return new GroupResponse(group);
    }

    @Transactional
    public UserResponse addUserToGroup(Long group_id, Long user_id, String scope_type, Long scope_id, String email) {
        User currentUser = getUserByEmail(email);
        User user =  getUserById(user_id);
        Group group = groupRepository.findById(group_id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + group_id));
        Company company = group.getCompany();
        if(user.getPerson().getCompanyRoles().stream().map(CompanyPersonRole::getCompany).noneMatch(company::equals)){
            throw new UsernameNotFoundException("Group does not exist within this company");
        }
        Scope scope = scopeFactory.createScope(scope_type, scope_id);

        for(Permission permission : group.getPermissions()) {
            if(!scope.hasScopedPermission(currentUser, getPermissionByName(permission.getName()))) {
                throw new RuntimeException("You do not have permission to assing this group in this context");
            }
        }

        try {
            scope.giveGroup(user, group);
        }catch (DataAccessException e){
            throw e;
        }
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse deleteUserFromGroup(Long group_id, Long user_id, String scope_type, Long scope_id, String email) {
        User currentUser = getUserByEmail(email);
        User user =  getUserById(user_id);
        Scope scope = scopeFactory.createScope(scope_type, scope_id);
        Group group = groupRepository.findById(group_id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + group_id));
        Company company = group.getCompany();
        if(user.getPerson().getCompanyRoles().stream().map(CompanyPersonRole::getCompany).noneMatch(company::equals)){
            throw new UsernameNotFoundException("Group does not exist within this company");
        }
        for(Permission permission : group.getPermissions()) {
            if(!scope.hasScopedPermission(currentUser, getPermissionByName(permission.getName()))) {
                throw new RuntimeException("You do not have permission to remove from this group in this context");
            }
        }

        try {
            scope.removeFromGroup(user, group);
        }catch (DataAccessException e){
            throw e;
        }
        return new UserResponse(user);
    }

}

