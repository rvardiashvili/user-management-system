package org.example.employeemanagement.controllers.permissions;

import jakarta.validation.Valid;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.dto.people.UserResponse;
import org.example.employeemanagement.services.permissions.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/{company_id}")
    public ResponseEntity<GroupResponse> createGroup(@PathVariable Long company_id, @Valid @RequestBody GroupRequest request, Authentication authentication) {
        return ResponseEntity.ok(groupService.createGroup(company_id, request, authentication.getName()));
    }

    @PutMapping("/{group_id}")
    public ResponseEntity<GroupResponse> addGroupPermissions(@PathVariable Long group_id, @Valid @RequestBody Set<String> request, Authentication authentication) {
        return ResponseEntity.ok(groupService.addGroupPermissions(group_id, request, authentication.getName()));
    }

    @GetMapping("/all/{company_id}")
    public ResponseEntity<List<GroupResponse>> getAllGroups(@PathVariable Long company_id, Authentication authentication) {
        return ResponseEntity.ok(groupService.viewAllGroups(company_id, authentication.getName()));
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long group_id,  Authentication authentication) {
        return ResponseEntity.ok(groupService.viewGroupById(group_id, authentication.getName()));
    }

    @DeleteMapping("/permission/{group_id}")
    public ResponseEntity<GroupResponse> deleteGroupPermissions(@PathVariable Long group_id, @Valid @RequestBody Set<String> permissions, Authentication authentication) {
        return ResponseEntity.ok(groupService.deletePermissions(group_id, permissions, authentication.getName()));
    }

    @DeleteMapping("/{group_id}")
    public ResponseEntity<GroupResponse> deleteGroup(@PathVariable Long group_id, Authentication authentication) {
        return ResponseEntity.ok(groupService.deleteGroup(group_id, authentication.getName()));
    }

    @PutMapping("/adduser/{scope_type}/{scope_id}/{group_id}/{user_id}")
    public ResponseEntity<UserResponse> addUser(@PathVariable String scope_type, @PathVariable Long scope_id, @PathVariable Long user_id, @PathVariable Long group_id, Authentication authentication) {
        return ResponseEntity.ok(groupService.addUserToGroup(group_id, user_id, scope_type, scope_id, authentication.getName()));
    }

    @DeleteMapping("/removeuser/{scope_type}/{scope_id}/{group_id}/{user_id}")
    public ResponseEntity<UserResponse> removeUser(@PathVariable String scope_type, @PathVariable Long scope_id, @PathVariable Long user_id, @PathVariable Long group_id, Authentication authentication) {
        return ResponseEntity.ok(groupService.deleteUserFromGroup(group_id, user_id, scope_type, scope_id, authentication.getName()));
    }
    @PutMapping("/name/{group_id}/{name}")
    public ResponseEntity<GroupResponse> changeName(@PathVariable Long group_id, @PathVariable String name, Authentication authentication) {
        return ResponseEntity.ok(groupService.updateGroupName(group_id, name, authentication.getName()));
    }
}