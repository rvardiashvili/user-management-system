package org.example.employeemanagement.services;

import org.example.employeemanagement.domain.Group;
import org.example.employeemanagement.domain.Permission;
import org.example.employeemanagement.domain.Role;
import org.example.employeemanagement.domain.User;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.GroupRepository;
import org.example.employeemanagement.repositories.PermissionRepository;
import org.example.employeemanagement.repositories.RoleRepository;
import org.example.employeemanagement.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GroupService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found: " + roleName));
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
    public Group createGroup( GroupRequest request, Collection<? extends GrantedAuthority> authorities){
        if(request.getName() == null || request.getName().isEmpty()){
            return  null;
        }
        Group group = new Group();
        group.setName(request.getName());
        Set<String> auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        Set<Permission> permissions = new HashSet<>();
        for(String permissionName : request.getPermissions()){
            if(auth.contains(permissionName))
                permissions.add(getPermissionByName(permissionName));
        }
        group.setPermissions(permissions);
        groupRepository.save(group);
        return group;
    }

    @Transactional
    public Group addGroupPermissions(Long id, Set<String> request, Collection<? extends GrantedAuthority> authorities){
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
        Set<String> auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        if(request == null || request.isEmpty()){
            return group;
        }
        Set<Permission> permissions = group.getPermissions();
        for(String permissionName : request){
            if(auth.contains(permissionName))
                permissions.add(getPermissionByName(permissionName));
        }
        group.setPermissions(permissions);
        groupRepository.save(group);
        return group;
    }

    @Transactional
    public Group updateGroupName(Long id, String name, Collection<? extends GrantedAuthority> authorities){
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
        if(name == null || name.isEmpty())
            return group;

        group.setName(name);
        groupRepository.save(group);
        return group;
    }

    @Transactional
    public Group viewGroupById(Long id){
        return groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
    }
    public List<Group> viewAllGroups(){
        return groupRepository.findAll();
    }

    @Transactional
    public Group deleteGroup(Long id){
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + id));
        groupRepository.delete(group);
        return group;
    }

    @Transactional
    public Group deletePermissions(Long group_id, Set<String> permissions, Collection<? extends GrantedAuthority> authorities){
        Group group = groupRepository.findById(group_id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + group_id));
        Set<String> auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        for(String permissionName : permissions){
            group = deletePermission(group, permissionName, auth);
        }
        groupRepository.save(group);
        return group;
    }

    private Group deletePermission(Group group, String permission_name, Set<String> auth){
        if(auth.contains(permission_name))
            group.getPermissions().remove(getPermissionByName(permission_name));
        return group;
    }

    @Transactional
    public UserResponse addUserToGroup(Long group_id, Long user_id){
        User user =  getUserById(user_id);
        Group group = groupRepository.findById(group_id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + group_id));
        user.getPerson().getGroups().add(group);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse deleteUserFromGroup(Long group_id, Long user_id){
        User user = getUserById(user_id);
        Group group = groupRepository.findById(group_id)
                .orElseThrow(() -> new UsernameNotFoundException("Group not found: " + group_id));
        user.getPerson().getGroups().remove(group);
        userRepository.save(user);
        return new UserResponse(user);
    }

}
