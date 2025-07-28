package org.example.employeemanagement.services.people;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Position;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.people.requests.PositionRequest;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.entities.OrganisationalUnitRepository;
import org.example.employeemanagement.repositories.permission.PermissionRepository;
import org.example.employeemanagement.repositories.people.PositionRepository;
import org.example.employeemanagement.repositories.role.RoleRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.springframework.dao.DataAccessException;
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
    private final OrganisationalUnitRepository unitRepository;
    private final ScopeFactory scopeFactory;

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }


    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName) // Assuming findByPermissionName
                .orElseThrow(() -> new UsernameNotFoundException("Permission not found: " + permissionName));
    }


    @Transactional
    public Position createPosition(String email, PositionRequest request) {
        if (request.getName() == null || request.getName().equals("")) {
            return  null;
        }
        if (request.getDescription() == null || request.getDescription().equals("")) {
            return  null;
        }
        User user = getUserByEmail(email);
        Scope scope = scopeFactory.createScope("unit", request.getUnitId());
        Permission permission = getPermissionByName("position");
        if(!scope.hasScopedPermission(user, permission))
            return null;
        Position position = new Position();
        position.setName(request.getName());
        position.setDescription(request.getDescription());
        position.setUnit_id( unitRepository.findById(request.getUnitId()).orElse(null));
        try {
            positionRepository.save(position);
        }catch (DataAccessException e){
            throw e;
        }
        return position;
    }

    @Transactional
    public List<PositionRequest> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(PositionRequest::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public GenericResponse assignPosition(Long id, Long position_id, String email) {
        User user = getUserById(id);
        User current_user = getUserByEmail(email);
        Position position = positionRepository.findById(position_id)
                .orElseThrow(() -> new UsernameNotFoundException("Position not found"));

        Scope scope = scopeFactory.createScope("unit", position.getUnit_id().getUnitId());
        if(!scope.hasScopedPermission(current_user, getPermissionByName("position")))
            return new GenericResponse(401, "Not Permitted");
        user.getPerson().getPositions().add(position);
        try {
        userRepository.save(user);
        }catch (DataAccessException e){
            throw e;
        }
        return new GenericResponse(200, "Successfully assigned position");
    }

    @Transactional
    public GenericResponse removePositionFromUser(Long id, Long positionId, String email) {
        User user = getUserById(id);
        User current_user = getUserByEmail(email);
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new UsernameNotFoundException("Position not found"));

        Scope scope = scopeFactory.createScope("unit", position.getUnit_id().getUnitId());
        if(!scope.hasScopedPermission(current_user, getPermissionByName("position")))
            return new GenericResponse(401, "Not Permitted");
        user.getPerson().getPositions().remove(position);
        try {
        userRepository.save(user);
        }catch (DataAccessException e){
            throw e;
        }
        return new GenericResponse(200, "Successfully removed position from user");
    }

    @Transactional
    public GenericResponse deletePosition(Long position_id, String email) {
        User current_user = getUserByEmail(email);
        Position position = positionRepository.findById(position_id)
                .orElseThrow(() -> new UsernameNotFoundException("Position not found: "));

        Scope scope = scopeFactory.createScope("unit", position.getUnit_id().getUnitId());
        if(!scope.hasScopedPermission(current_user, getPermissionByName("position")))
            return new GenericResponse(401, "Not Permitted");
        positionRepository.delete(position);
        return new GenericResponse(200, "Successfully deleted position");
    }
}
