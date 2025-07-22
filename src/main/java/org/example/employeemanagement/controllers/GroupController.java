package org.example.employeemanagement.controllers;


import jakarta.validation.Valid;
import org.example.employeemanagement.domain.Group;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.services.GroupService;
import org.example.employeemanagement.services.PositionService;
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

    private final PositionService positionService;
    private final GroupService groupService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('group')")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody GroupRequest request, Authentication authentication) {
        return ResponseEntity.ok(groupService.createGroup(request, authentication.getAuthorities()));
    }

    @PutMapping("/{group_id}")
    @PreAuthorize("hasAuthority('group') && hasAuthority('permission')")
    public ResponseEntity<Group> addGroupPermissions(@PathVariable Long group_id, @Valid @RequestBody Set<String> request, Authentication authentication) {
        return ResponseEntity.ok(groupService.addGroupPermissions(group_id, request, authentication.getAuthorities()));
    }

    @GetMapping("/")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupService.viewAllGroups());
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long group_id) {
        return ResponseEntity.ok(groupService.viewGroupById(group_id));
    }

    @DeleteMapping("/permission/{group_id}")
    @PreAuthorize("hasAuthority('group') && hasAuthority('permission')")
    public ResponseEntity<Group> deleteGroupPermissions(@PathVariable Long group_id, @Valid @RequestBody Set<String> permissions, Authentication authentication) {
        return ResponseEntity.ok(groupService.deletePermissions(group_id, permissions, authentication.getAuthorities()));
    }

    @DeleteMapping("/{group_id}")
    @PreAuthorize("hasAuthority('group')")
    public ResponseEntity<Group> deleteGroup(@PathVariable Long group_id) {
        return ResponseEntity.ok(groupService.deleteGroup(group_id));
    }

    @PutMapping("/adduser/{group_id}/{user_id}")
    @PreAuthorize("hasAuthority('group') && hasAuthority('permission')")
    public ResponseEntity<UserResponse> addUser(@PathVariable Long user_id, @PathVariable Long group_id){
        return ResponseEntity.ok(groupService.addUserToGroup(group_id, user_id));
    }

    @DeleteMapping("/removeuser/{group_id}/{user_id}")
    @PreAuthorize("hasAuthority('group') && hasAuthority('permission')")
    public ResponseEntity<UserResponse> removeUser(@PathVariable Long user_id, @PathVariable Long group_id){
        return ResponseEntity.ok(groupService.deleteUserFromGroup(group_id, user_id));
    }
    @PutMapping("/name/{group_id}/{name}")
    @PreAuthorize("hasAuthority('group')")
    public ResponseEntity<Group> changeName(@PathVariable Long group_id, @PathVariable String name, Authentication authentication) {
        return ResponseEntity.ok(groupService.updateGroupName(group_id, name, authentication.getAuthorities()));
    }
}