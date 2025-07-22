package org.example.employeemanagement.controllers;


import jakarta.validation.Valid;
import org.example.employeemanagement.domain.Position;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.services.PositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/position")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('position')")
    public ResponseEntity<Position> createPosition(@Valid @RequestBody PositionRequest request, Authentication authentication) {
        return ResponseEntity.ok(positionService.createPosition(request));
    }

    @GetMapping("/")
    public ResponseEntity<List<PositionRequest>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @PutMapping("/{user_id}/{position_name}")
    @PreAuthorize("hasAuthority('position')")
    public ResponseEntity<GenericResponse> assignPosition(@PathVariable Long user_id, @PathVariable String position_name) {
        return ResponseEntity.ok(positionService.assignPosition(user_id, position_name));
    }

    @DeleteMapping("demote/{user_id}")
    @PreAuthorize("hasAuthority('position')")
    public ResponseEntity<GenericResponse> removePositionFromUser(@PathVariable Long user_id) {
        return ResponseEntity.ok(positionService.removePositionFromUser(user_id));
    }

    @DeleteMapping("/{position_name}")
    @PreAuthorize("hasAuthority('position')")
    public ResponseEntity<GenericResponse> deletePosition(@PathVariable String position_name) {
        return ResponseEntity.ok(positionService.deletePosition(position_name));
    }

}