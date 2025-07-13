package com.example.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Column(name = "first_name")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Column(name = "last_name")
    private String lastName;
    
    @NotBlank(message = "Matricule is required")
    @Column(name = "matricule", unique = true)
    private String matricule;
    
    @NotNull(message = "Date of birth is required")
    @Column(name = "date_birth")
    private LocalDate dateBirth;
    
    @NotBlank(message = "Workplace is required")
    @Column(name = "workplace")
    private String workplace;
    
    @Column(name = "wife_first_name")
    private String wifeFirstName;
    
    @Column(name = "wife_last_name")
    private String wifeLastName;
    
    @Column(name = "wife_workplace")
    private String wifeWorkplace;
    
    @Column(name = "wife_matricule")
    private String wifeMatricule;
    
    @Column(name = "num_children")
    private int numChildren;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("employee-primaryChildren")
    private List<PrimaryEducationChild> primaryEducationChildren = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("employee-secondaryChildren")
    private List<SecondaryEducationChild> secondaryEducationChildren = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("employee-higherChildren")
    private List<HigherEducationChild> higherEducationChildren = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Application> applications = new ArrayList<>();
    
    // Constructors
    public Employee() {}
    
    public Employee(String firstName, String lastName, String matricule, LocalDate dateBirth, String workplace) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.matricule = matricule;
        this.dateBirth = dateBirth;
        this.workplace = workplace;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMatricule() {
        return matricule;
    }
    
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    
    public LocalDate getDateBirth() {
        return dateBirth;
    }
    
    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }
    
    public String getWorkplace() {
        return workplace;
    }
    
    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }
    
    public String getWifeFirstName() {
        return wifeFirstName;
    }
    
    public void setWifeFirstName(String wifeFirstName) {
        this.wifeFirstName = wifeFirstName;
    }
    
    public String getWifeLastName() {
        return wifeLastName;
    }
    
    public void setWifeLastName(String wifeLastName) {
        this.wifeLastName = wifeLastName;
    }
    
    public String getWifeWorkplace() {
        return wifeWorkplace;
    }
    
    public void setWifeWorkplace(String wifeWorkplace) {
        this.wifeWorkplace = wifeWorkplace;
    }
    
    public String getWifeMatricule() {
        return wifeMatricule;
    }
    
    public void setWifeMatricule(String wifeMatricule) {
        this.wifeMatricule = wifeMatricule;
    }
    
    public int getNumChildren() {
        return numChildren;
    }
    
    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }
    
    public List<PrimaryEducationChild> getPrimaryEducationChildren() {
        return primaryEducationChildren;
    }
    
    public void setPrimaryEducationChildren(List<PrimaryEducationChild> primaryEducationChildren) {
        this.primaryEducationChildren = primaryEducationChildren;
    }
    
    public List<SecondaryEducationChild> getSecondaryEducationChildren() {
        return secondaryEducationChildren;
    }
    
    public void setSecondaryEducationChildren(List<SecondaryEducationChild> secondaryEducationChildren) {
        this.secondaryEducationChildren = secondaryEducationChildren;
    }
    
    public List<HigherEducationChild> getHigherEducationChildren() {
        return higherEducationChildren;
    }
    
    public void setHigherEducationChildren(List<HigherEducationChild> higherEducationChildren) {
        this.higherEducationChildren = higherEducationChildren;
    }
    
    public List<Application> getApplications() {
        return applications;
    }
    
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}

