package org.example.employeemanagement.services.entities;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.dto.entities.requests.CompanyRequest;
import org.example.employeemanagement.dto.entities.responses.CompanyResponse;
import org.example.employeemanagement.dto.GenericResponse;
import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.repositories.people.PersonRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.example.employeemanagement.repositories.permission.PermissionRepository;
import org.example.employeemanagement.repositories.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ScopeFactory  scopeFactory;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
    private Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
    }


    private Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found: " + companyId));
    }

    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName) // Assuming findByPermissionName
                .orElseThrow(() -> new UsernameNotFoundException("Permission not found: " + permissionName));
    }


    @Transactional
    public GenericResponse createCompany(CompanyRequest request, String creatorEmail) {
        String companyName = request.getCompany_name();
        String companyDescription = request.getCompany_description();
        try {
            User creatorUser = getUserByEmail(creatorEmail);
            Person creatorPerson = creatorUser.getPerson();

            if (companyRepository.findByCompanyName(companyName).isPresent()) {
                throw new IllegalArgumentException("Company with name '" + companyName + "' already exists.");
            }

            Company newCompany = new Company();
            newCompany.setCompanyName(companyName);
            newCompany.setCompanyDescription(companyDescription);
            newCompany.setCreator(creatorPerson); // Set the Person as creator

            Company savedCompany = companyRepository.save(newCompany);
            init_company_roles(savedCompany, creatorUser, getRoleByName("admin"));

            return new GenericResponse(200, "Company created successfully");

        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            throw e; // Re-throw specific exceptions for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("Failed to create company: " + e.getMessage(), e);
        }
    }

    private void init_company_roles(Company company, User creatorUser, Role role) {
        CompanyPersonRole cpr = new CompanyPersonRole(company, creatorUser.getPerson(), role);
        creatorUser.getPerson().getCompanyRoles().add(cpr);
        userRepository.save(creatorUser);
    }





    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies(String email) {
        User user = getUserByEmail(email);
        List<Company> companies;
        companies = user.getPerson().getCompanyRoles().stream().map(CompanyPersonRole::getCompany).toList();
        return companies.stream().map(CompanyResponse::new).collect(Collectors.toList());
    }


    @Transactional
    public CompanyResponse editCompany(Long companyId, CompanyRequest request,String email) {
        Company company = getCompanyById(companyId);
        Scope scope = scopeFactory.createScope("company", companyId);
        Role role = getRoleByName("admin");
        if(!(scope.getScopedRole(getUserByEmail(email)) == role))
            return null;
        if(request.getCompany_name() != null)
            company.setCompanyName(request.getCompany_name());
        if(request.getCompany_description() != null)
            company.setCompanyDescription(request.getCompany_description());

        Company updatedCompany = companyRepository.save(company);
        return new CompanyResponse(updatedCompany);
    }


    @Transactional
    public GenericResponse deleteCompany(Long companyId, String email) {
        User user = getUserByEmail(email);
        Company company = getCompanyById(companyId);
        if(!company.getCreator().getPersonId().equals(user.getPerson().getPersonId()))
            return new GenericResponse(401, "only creator can delete company");
        companyRepository.delete(company);
        return new GenericResponse(200, "Company '" + company.getCompanyName() + "' deleted successfully.");
    }




}
