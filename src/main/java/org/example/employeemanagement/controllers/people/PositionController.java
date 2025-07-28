package org.example.employeemanagement.controllers.people;


import jakarta.validation.Valid;
import org.example.employeemanagement.domain.people.Position;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.dto.people.requests.PositionRequest;
import org.example.employeemanagement.services.people.PositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/position")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/")
    public ResponseEntity<Position> createPosition(@Valid @RequestBody PositionRequest request, Authentication authentication) {
        return ResponseEntity.ok(positionService.createPosition(authentication.getName(), request));
    }

    @GetMapping("/")
    public ResponseEntity<List<PositionRequest>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @PutMapping("/{user_id}/{position_id}")
    public ResponseEntity<GenericResponse> assignPosition(@PathVariable Long user_id, @PathVariable Long position_id, Authentication authentication) {
        return ResponseEntity.ok(positionService.assignPosition(user_id, position_id, authentication.getName()));
    }

    @DeleteMapping("demote/{user_id}/{position_id}")
    public ResponseEntity<GenericResponse> removePositionFromUser(@PathVariable Long user_id, @PathVariable Long position_id, Authentication authentication) {
        return ResponseEntity.ok(positionService.removePositionFromUser(user_id, position_id, authentication.getName()));
    }

    @DeleteMapping("/{position_id}")
    public ResponseEntity<GenericResponse> deletePosition(@PathVariable Long position_id, Authentication authentication) {
        return ResponseEntity.ok(positionService.deletePosition(position_id, authentication.getName()));
    }

}