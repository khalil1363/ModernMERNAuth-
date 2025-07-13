package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.PrimaryEducationChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrimaryEducationChildRepository extends JpaRepository<PrimaryEducationChild, Long> {
    List<PrimaryEducationChild> findByEmployeeId(Long employeeId);
}

