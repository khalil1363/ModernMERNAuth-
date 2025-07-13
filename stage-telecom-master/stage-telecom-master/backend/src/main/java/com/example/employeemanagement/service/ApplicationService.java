package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Application;
import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Application submitApplication(Long employeeId);
    Optional<Application> findById(Long id);
    List<Application> findByEmployeeId(Long employeeId);
    List<Application> findAllApplications();
    List<Application> findByStatus(Application.ApplicationStatus status);
    Application approveApplication(Long applicationId, String reviewedBy);
    Application rejectApplication(Long applicationId, String rejectionReason, String reviewedBy);
    void deleteApplication(Long id);
}

