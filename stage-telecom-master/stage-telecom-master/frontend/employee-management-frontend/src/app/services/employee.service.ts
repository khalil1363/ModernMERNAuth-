import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee, Application } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = 'http://localhost:8080/api/employee';

  constructor(private http: HttpClient) { }

  registerEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/register`, employee);
  }

  getEmployee(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  getEmployeeByMatricule(matricule: string): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/matricule/${matricule}`);
  }

  addPrimaryEducationChild(employeeId: number, formData: FormData): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/${employeeId}/primary-education-child`, formData);
  }

  addSecondaryEducationChild(employeeId: number, formData: FormData): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/${employeeId}/secondary-education-child`, formData);
  }

  addHigherEducationChild(employeeId: number, formData: FormData): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/${employeeId}/higher-education-child`, formData);
  }

  submitApplication(employeeId: number): Observable<Application> {
    return this.http.post<Application>(`${this.apiUrl}/${employeeId}/submit-application`, {});
  }

  getEmployeeApplications(employeeId: number): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/${employeeId}/applications`);
  }
}

