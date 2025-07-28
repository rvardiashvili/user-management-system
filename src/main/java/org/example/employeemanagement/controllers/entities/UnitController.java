package org.example.employeemanagement.controllers.entities;


import jakarta.validation.Valid;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.dto.entities.requests.UnitRequest;
import org.example.employeemanagement.dto.entities.responses.UnitResponse;
import org.example.employeemanagement.services.entities.CompanyService;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.services.entities.UnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/unit/")
@RequiredArgsConstructor
public class UnitController {

    private final CompanyService companyService;
    private final UnitService unitService;


    @GetMapping("/")
    public ResponseEntity<List<UnitResponse>> getAllUnits(Authentication authentication) {
        return ResponseEntity.ok(unitService.getAllUnits(authentication.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createUnit(@Valid @RequestBody UnitRequest request, Authentication authentication) {
        return ResponseEntity.ok(unitService.createUnit(request, authentication.getName()));
    }

    @DeleteMapping("/{unitId}")
    public ResponseEntity<GenericResponse> deleteUnit(
            @PathVariable Long unitId,
            Authentication authentication) {
        return ResponseEntity.ok(unitService.deleteUnit(unitId, authentication.getName()));
    }

    @PutMapping("{unitId}")
    public ResponseEntity<UnitResponse> updateUnit(
            @PathVariable Long unitId,
            @Valid @RequestBody UnitRequest request, Authentication authentication
    ){
        return ResponseEntity.ok(unitService.editUnit(unitId, request, authentication.getName()));
    }
}
