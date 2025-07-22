package org.example.employeemanagement.repositories;

import org.example.employeemanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.person p JOIN FETCH p.role r LEFT JOIN FETCH r.permissions LEFT JOIN FETCH p.permissions WHERE u.email = :email")
    Optional<User> findByEmailWithDetails(String email);

    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u JOIN FETCH u.person p JOIN FETCH p.role r LEFT JOIN FETCH r.permissions LEFT JOIN FETCH p.permissions")
    List<User> findAllWithPerson();
}