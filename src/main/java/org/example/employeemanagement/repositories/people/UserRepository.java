package org.example.employeemanagement.repositories.people;

import org.example.employeemanagement.domain.people.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.person p LEFT JOIN FETCH p.addresses WHERE u.email = :email")
    Optional<User> findByEmailWithDetails(String email);



    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u JOIN FETCH u.person p JOIN FETCH p.companyRoles JOIN FETCH p.organizationRoles JOIN FETCH p.unitRoles LEFT JOIN FETCH p.companyPermissions LEFT JOIN FETCH p.organizationPermissions LEFT JOIN FETCH p.unitPermissions LEFT JOIN FETCH p.addresses")
    List<User> findAllWithPerson();

    @Query("SELECT u FROM User u JOIN  FETCH u.person p JOIN FETCH p.companyRoles r JOIN FETCH r.company c WHERE c.companyId = :companyId")
    List<User> findAllWithCompany(Long companyId);

    @Query("SELECT u FROM User u JOIN  FETCH u.person p JOIN FETCH p.organizationRoles r JOIN FETCH r.organisation c WHERE c.organisationId = :organisationId")
    List<User> findAllWithOrganisation(Long organisationId);

    @Query("SELECT u FROM User u JOIN  FETCH u.person p JOIN FETCH p.unitRoles r JOIN FETCH r.unit c WHERE c.unitId = :unitId")
    List<User> findAllWithUnit(Long unitId);

}