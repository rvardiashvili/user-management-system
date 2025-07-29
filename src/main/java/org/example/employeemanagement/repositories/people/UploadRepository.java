package org.example.employeemanagement.repositories.people;

import org.example.employeemanagement.domain.UploadedFile;
import org.example.employeemanagement.domain.people.Position;
import org.example.employeemanagement.domain.people.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findByUser(User user);
}