package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    Optional<Employee> findById(Long id);
    Optional<Employee> findByMatricule(String matricule);
    List<Employee> findAllEmployees();
    Employee updateEmployee(Employee employee);
    void deleteEmployee(Long id);
    boolean existsByMatricule(String matricule);
}

