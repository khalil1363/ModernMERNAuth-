package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.SecondaryEducationChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SecondaryEducationChildRepository extends JpaRepository<SecondaryEducationChild, Long> {
    List<SecondaryEducationChild> findByEmployeeId(Long employeeId);
}

