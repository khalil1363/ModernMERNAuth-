package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Application;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.ApplicationRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Application submitApplication(Long employeeId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isPresent()) {
            Application application = new Application(employeeOpt.get());
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Employee not found with id: " + employeeId);
    }

    @Override
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public List<Application> findByEmployeeId(Long employeeId) {
        return applicationRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Application> findAllApplications() {
        return applicationRepository.findAllByOrderBySubmissionDateDesc();
    }

    @Override
    public List<Application> findByStatus(Application.ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }

    @Override
    public Application approveApplication(Long applicationId, String reviewedBy) {
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            application.setStatus(Application.ApplicationStatus.ACCEPTED);
            application.setReviewDate(LocalDateTime.now());
            application.setReviewedBy(reviewedBy);
            application.setRejectionReason(null); // Clear any previous rejection reason
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Application not found with id: " + applicationId);
    }

    @Override
    public Application rejectApplication(Long applicationId, String rejectionReason, String reviewedBy) {
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            application.setStatus(Application.ApplicationStatus.REJECTED);
            application.setReviewDate(LocalDateTime.now());
            application.setRejectionReason(rejectionReason);
            application.setReviewedBy(reviewedBy);
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Application not found with id: " + applicationId);
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}

