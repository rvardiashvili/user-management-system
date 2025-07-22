package org.example.employeemanagement.services;

import org.example.employeemanagement.domain.Permission;
import org.example.employeemanagement.domain.Position;
import org.example.employeemanagement.domain.Role;
import org.example.employeemanagement.domain.User;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.PermissionRepository;
import org.example.employeemanagement.repositories.PositionRepository;
import org.example.employeemanagement.repositories.RoleRepository;
import org.example.employeemanagement.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PositionService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PositionRepository positionRepository;

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
    public Position createPosition(PositionRequest request) {
        if (request.getName() == null || request.getName().equals("")) {
            return  null;
        }
        if (request.getDescription() == null || request.getDescription().equals("")) {
            return  null;
        }
        Position position = new Position();
        position.setName(request.getName());
        position.setDescription(request.getDescription());
        positionRepository.save(position);
        return position;
    }

    @Transactional
    public List<PositionRequest> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(PositionRequest::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public GenericResponse assignPosition(Long id, String name) {
        User user = getUserById(id);
        Position position = positionRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + name));
        user.getPerson().setPosition(position);
        userRepository.save(user);
        return new GenericResponse(200, "Successfully assigned position");
    }

    @Transactional
    public GenericResponse removePositionFromUser(Long userId) {
        User user = getUserById(userId);
        user.getPerson().setPosition(null);
        userRepository.save(user);
        return new GenericResponse(200, "Successfully removed position from user");
    }

    @Transactional
    public GenericResponse deletePosition(String position_name) {
        Position position = positionRepository.findByName(position_name)
                .orElseThrow(() -> new UsernameNotFoundException("Position not found: " + position_name));
        positionRepository.delete(position);
        return new GenericResponse(200, "Successfully deleted position");
    }
}
