package com.example.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status = ApplicationStatus.EN_ATTENTE;
    
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;
    
    @Column(name = "review_date")
    private LocalDateTime reviewDate;
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "reviewed_by")
    private String reviewedBy;
    
    // Constructors
    public Application() {
        this.submissionDate = LocalDateTime.now();
    }
    
    public Application(Employee employee) {
        this.employee = employee;
        this.submissionDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public LocalDateTime getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public String getReviewedBy() {
        return reviewedBy;
    }
    
    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public enum ApplicationStatus {
        EN_ATTENTE, ACCEPTED, REJECTED
    }
}

