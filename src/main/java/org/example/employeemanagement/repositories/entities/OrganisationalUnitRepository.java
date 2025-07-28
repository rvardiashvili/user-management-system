package org.example.employeemanagement.repositories.entities;

import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationalUnitRepository extends JpaRepository<OrganisationalUnit, Long> {
    @Query("SELECT DISTINCT ou FROM User u JOIN u.person p JOIN p.companyRoles cr JOIN cr.company c JOIN  c.organisations o JOIN o.units ou WHERE u.email = :email")
    List<OrganisationalUnit> findAllByCompanyMembership(String email);

}
