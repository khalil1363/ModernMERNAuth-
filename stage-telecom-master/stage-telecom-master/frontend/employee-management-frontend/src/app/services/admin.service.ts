import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee, Application, ApplicationStatus } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) { }

  getAllApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/applications`);
  }

  getPendingApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/applications/pending`);
  }

  getAcceptedApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/applications/accepted`);
  }

  getRejectedApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/applications/rejected`);
  }

  getApplicationById(id: number): Observable<Application> {
    return this.http.get<Application>(`${this.apiUrl}/applications/${id}`);
  }

  approveApplication(id: number, reviewedBy: string = 'Admin'): Observable<Application> {
    const params = new HttpParams().set('reviewedBy', reviewedBy);
    return this.http.put<Application>(`${this.apiUrl}/applications/${id}/approve`, {}, { params });
  }

  rejectApplication(id: number, rejectionReason: string, reviewedBy: string = 'Admin'): Observable<Application> {
    const params = new HttpParams()
      .set('rejectionReason', rejectionReason)
      .set('reviewedBy', reviewedBy);
    return this.http.put<Application>(`${this.apiUrl}/applications/${id}/reject`, {}, { params });
  }

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.apiUrl}/employees`);
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/employees/${id}`);
  }

  deleteApplication(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/applications/${id}`);
  }
}

