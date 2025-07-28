package org.example.employeemanagement.controllers.entities;


import jakarta.validation.Valid;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.dto.entities.responses.OrganisationReponse;
import org.example.employeemanagement.dto.entities.requests.OrganisationRequest;
import org.example.employeemanagement.services.entities.CompanyService;
import org.example.employeemanagement.services.entities.OrganisationService;
import org.example.employeemanagement.services.permissions.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/organization/")
@RequiredArgsConstructor
public class OrganizationController {

    private final PermissionService permissionService;
    private final CompanyService companyService;
    private final OrganisationService organisationService;


    @GetMapping("/")
    public ResponseEntity<List<OrganisationReponse>> getAllOrganisations(Authentication authentication) {
        return ResponseEntity.ok(organisationService.getAllOrganisations(authentication.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createOrganization(@Valid @RequestBody OrganisationRequest request, Authentication authentication) {
        return ResponseEntity.ok(organisationService.createOrganisation(request, authentication.getName()));
    }

    @DeleteMapping("/{organisationId}")
    public ResponseEntity<GenericResponse> deleteCompany(
            @PathVariable Long organisationId,
            Authentication authentication) {
        return ResponseEntity.ok(organisationService.deleteOrganisation(organisationId, authentication.getName()));
    }
    @PutMapping("/{organisationId}")
    public ResponseEntity<OrganisationReponse> updateOrganisation(
            @PathVariable Long organisationId,
            @Valid @RequestBody OrganisationRequest request,
            Authentication authentication
    ){
        return ResponseEntity.ok(organisationService.editOrganisation(organisationId, request, authentication.getName()));
    }
}
