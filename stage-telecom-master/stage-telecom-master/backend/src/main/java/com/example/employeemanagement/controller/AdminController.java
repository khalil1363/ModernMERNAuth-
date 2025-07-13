package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Application;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.ApplicationService;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.findAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/pending")
    public ResponseEntity<List<Application>> getPendingApplications() {
        List<Application> applications = applicationService.findByStatus(Application.ApplicationStatus.EN_ATTENTE);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/accepted")
    public ResponseEntity<List<Application>> getAcceptedApplications() {
        List<Application> applications = applicationService.findByStatus(Application.ApplicationStatus.ACCEPTED);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/rejected")
    public ResponseEntity<List<Application>> getRejectedApplications() {
        List<Application> applications = applicationService.findByStatus(Application.ApplicationStatus.REJECTED);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long id) {
        Optional<Application> application = applicationService.findById(id);
        if (application.isPresent()) {
            return ResponseEntity.ok(application.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/applications/{id}/approve")
    public ResponseEntity<?> approveApplication(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Admin") String reviewedBy) {
        try {
            Application application = applicationService.approveApplication(id, reviewedBy);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving application: " + e.getMessage());
        }
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<?> rejectApplication(
            @PathVariable Long id,
            @RequestParam String rejectionReason,
            @RequestParam(defaultValue = "Admin") String reviewedBy) {
        try {
            Application application = applicationService.rejectApplication(id, rejectionReason, reviewedBy);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rejecting application: " + e.getMessage());
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting application: " + e.getMessage());
        }
    }
}

