package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.*;
import com.example.employeemanagement.service.ApplicationService;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Backend is working!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
        try {
            // Log the received employee data for debugging
            System.out.println("Received employee data:");
            System.out.println("First Name: " + employee.getFirstName());
            System.out.println("Last Name: " + employee.getLastName());
            System.out.println("Matricule: " + employee.getMatricule());
            System.out.println("Date Birth: " + employee.getDateBirth());
            System.out.println("Workplace: " + employee.getWorkplace());

            // Check for validation errors
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ")
                );
                System.out.println("Validation errors: " + errorMessage.toString());
                return ResponseEntity.badRequest().body(errorMessage.toString());
            }

            // Check for duplicate matricule
            if (employeeService.existsByMatricule(employee.getMatricule())) {
                return ResponseEntity.badRequest().body("Employee with this matricule already exists");
            }

            Employee savedEmployee = employeeService.saveEmployee(employee);
            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering employee: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<?> getEmployeeByMatricule(@PathVariable String matricule) {
        Optional<Employee> employee = employeeService.findByMatricule(matricule);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{employeeId}/primary-education-child")
    public ResponseEntity<?> addPrimaryEducationChild(
            @PathVariable Long employeeId,
            @RequestParam("childFullName") String childFullName,
            @RequestParam("childBirthDate") String childBirthDate,
            @RequestParam("academicYear") String academicYear,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {
        
        try {
            Optional<Employee> employeeOpt = employeeService.findById(employeeId);
            if (!employeeOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Employee employee = employeeOpt.get();
            PrimaryEducationChild child = new PrimaryEducationChild();
            child.setChildFullName(childFullName);
            child.setChildBirthDate(LocalDate.parse(childBirthDate));
            child.setAcademicYear(academicYear);
            child.setEmployee(employee);

            // Store PDF file
            if (pdfFile != null && !pdfFile.isEmpty()) {
                String storedFileName = fileStorageService.storeFile(pdfFile, "primary-education");
                child.setPdfFilePath(storedFileName); // Store the UUID filename for access
                child.setPdfFileName(pdfFile.getOriginalFilename()); // Store original name for display

                System.out.println("Stored PDF file:");
                System.out.println("Original filename: " + pdfFile.getOriginalFilename());
                System.out.println("Stored filename: " + storedFileName);
            }

            employee.getPrimaryEducationChildren().add(child);
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding primary education child: " + e.getMessage());
        }
    }

    @PostMapping("/{employeeId}/secondary-education-child")
    public ResponseEntity<?> addSecondaryEducationChild(
            @PathVariable Long employeeId,
            @RequestParam("childFullName") String childFullName,
            @RequestParam("childBirthDate") String childBirthDate,
            @RequestParam("academicYear") String academicYear,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {
        
        try {
            Optional<Employee> employeeOpt = employeeService.findById(employeeId);
            if (!employeeOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Employee employee = employeeOpt.get();
            SecondaryEducationChild child = new SecondaryEducationChild();
            child.setChildFullName(childFullName);
            child.setChildBirthDate(LocalDate.parse(childBirthDate));
            child.setAcademicYear(academicYear);
            child.setEmployee(employee);

            // Store PDF file
            if (pdfFile != null && !pdfFile.isEmpty()) {
                String storedFileName = fileStorageService.storeFile(pdfFile, "secondary-education");
                child.setPdfFilePath(storedFileName); // Store the UUID filename for access
                child.setPdfFileName(pdfFile.getOriginalFilename()); // Store original name for display

                System.out.println("Stored Secondary PDF file:");
                System.out.println("Original filename: " + pdfFile.getOriginalFilename());
                System.out.println("Stored filename: " + storedFileName);
            }

            employee.getSecondaryEducationChildren().add(child);
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding secondary education child: " + e.getMessage());
        }
    }

    @PostMapping("/{employeeId}/higher-education-child")
    public ResponseEntity<?> addHigherEducationChild(
            @PathVariable Long employeeId,
            @RequestParam("childFullName") String childFullName,
            @RequestParam("childBirthDate") String childBirthDate,
            @RequestParam("academicYear") String academicYear,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {
        
        try {
            Optional<Employee> employeeOpt = employeeService.findById(employeeId);
            if (!employeeOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Employee employee = employeeOpt.get();
            HigherEducationChild child = new HigherEducationChild();
            child.setChildFullName(childFullName);
            child.setChildBirthDate(LocalDate.parse(childBirthDate));
            child.setAcademicYear(academicYear);
            child.setEmployee(employee);

            // Store PDF file
            if (pdfFile != null && !pdfFile.isEmpty()) {
                String storedFileName = fileStorageService.storeFile(pdfFile, "higher-education");
                child.setPdfFilePath(storedFileName); // Store the UUID filename for access
                child.setPdfFileName(pdfFile.getOriginalFilename()); // Store original name for display

                System.out.println("Stored Higher Education PDF file:");
                System.out.println("Original filename: " + pdfFile.getOriginalFilename());
                System.out.println("Stored filename: " + storedFileName);
            }

            employee.getHigherEducationChildren().add(child);
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding higher education child: " + e.getMessage());
        }
    }

    @PostMapping("/{employeeId}/submit-application")
    public ResponseEntity<?> submitApplication(@PathVariable Long employeeId) {
        try {
            Application application = applicationService.submitApplication(employeeId);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error submitting application: " + e.getMessage());
        }
    }

    @GetMapping("/{employeeId}/applications")
    public ResponseEntity<List<Application>> getEmployeeApplications(@PathVariable Long employeeId) {
        List<Application> applications = applicationService.findByEmployeeId(employeeId);
        return ResponseEntity.ok(applications);
    }
}

