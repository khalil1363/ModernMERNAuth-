package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.HigherEducationChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HigherEducationChildRepository extends JpaRepository<HigherEducationChild, Long> {
    List<HigherEducationChild> findByEmployeeId(Long employeeId);
}

