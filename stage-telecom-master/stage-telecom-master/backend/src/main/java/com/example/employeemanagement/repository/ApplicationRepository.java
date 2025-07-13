package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByEmployeeId(Long employeeId);
    List<Application> findByStatus(Application.ApplicationStatus status);
    List<Application> findAllByOrderBySubmissionDateDesc();
}

