package com.example.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "higher_education_children")
public class HigherEducationChild {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Child full name is required")
    @Column(name = "child_full_name")
    private String childFullName;
    
    @NotNull(message = "Child birth date is required")
    @Column(name = "child_birth_date")
    private LocalDate childBirthDate;
    
    @NotBlank(message = "Academic year is required")
    @Column(name = "academic_year")
    private String academicYear;
    
    @Column(name = "pdf_file_path")
    private String pdfFilePath;
    
    @Column(name = "pdf_file_name")
    private String pdfFileName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonBackReference("employee-higherChildren")
    private Employee employee;
    
    // Constructors
    public HigherEducationChild() {}
    
    public HigherEducationChild(String childFullName, LocalDate childBirthDate, String academicYear) {
        this.childFullName = childFullName;
        this.childBirthDate = childBirthDate;
        this.academicYear = academicYear;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getChildFullName() {
        return childFullName;
    }
    
    public void setChildFullName(String childFullName) {
        this.childFullName = childFullName;
    }
    
    public LocalDate getChildBirthDate() {
        return childBirthDate;
    }
    
    public void setChildBirthDate(LocalDate childBirthDate) {
        this.childBirthDate = childBirthDate;
    }
    
    public String getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    public String getPdfFilePath() {
        return pdfFilePath;
    }
    
    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }
    
    public String getPdfFileName() {
        return pdfFileName;
    }
    
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}

