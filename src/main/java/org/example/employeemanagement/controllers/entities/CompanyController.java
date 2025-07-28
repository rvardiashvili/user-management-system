package org.example.employeemanagement.controllers.entities;


import jakarta.validation.Valid;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.dto.entities.requests.CompanyRequest;
import org.example.employeemanagement.dto.entities.responses.CompanyResponse;
import org.example.employeemanagement.services.entities.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;


    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(Authentication authentication) {
        return ResponseEntity.ok(companyService.getAllCompanies(authentication.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createCompany(@Valid @RequestBody CompanyRequest request, Authentication authentication) {
        return ResponseEntity.ok(companyService.createCompany(request, authentication.getName()));
    }

    @PutMapping("/{companyId}/edit")
    public ResponseEntity<CompanyResponse> updateCompanyDescription(@PathVariable Long companyId, @Valid @RequestBody CompanyRequest request, Authentication authentication) {
        return ResponseEntity.ok(companyService.editCompany(companyId ,request, authentication.getName()));
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<GenericResponse> deleteCompanyn(
            @PathVariable Long companyId,
            Authentication authentication) {
        return ResponseEntity.ok(companyService.deleteCompany(companyId, authentication.getName()));
    }
}
