import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { EmployeeService } from '../../services/employee.service';
import { Employee, Application, ApplicationStatus } from '../../models/employee.model';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-dashboard.html',
  styleUrls: ['./user-dashboard.css']
})
export class UserDashboardComponent implements OnInit {
  employee: Employee | null = null;
  applications: Application[] = [];
  loading = true;
  error = '';
  matricule = '';

  // For PDF viewer modal
  showPDFModal = false;
  currentPDFUrl: SafeResourceUrl | null = null;
  currentPDFName = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private employeeService: EmployeeService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.matricule = params['matricule'];
      if (this.matricule) {
        this.loadEmployeeData();
      }
    });
  }

  loadEmployeeData() {
    this.loading = true;
    this.error = '';

    this.employeeService.getEmployeeByMatricule(this.matricule).subscribe({
      next: (employee) => {
        this.employee = employee;
        if (employee.id) {
          this.loadApplications(employee.id);
        }
      },
      error: (error) => {
        this.error = 'Employee not found. Please check your matricule number.';
        this.loading = false;
      }
    });
  }

  loadApplications(employeeId: number) {
    this.employeeService.getEmployeeApplications(employeeId).subscribe({
      next: (applications) => {
        this.applications = applications;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error loading applications. Please try again.';
        this.loading = false;
      }
    });
  }

  // PDF viewing methods
  viewPDF(pdfPath: string, pdfName: string) {
    // Use the stored file path (UUID filename) instead of the original filename
    // pdfPath contains the actual stored filename (UUID)
    const storedFileName = pdfPath; // This is the UUID filename returned by the backend
    const pdfUrl = `http://localhost:8080/api/files/${storedFileName}`;

    console.log('Attempting to view PDF:', {
      originalName: pdfName,
      storedFileName: storedFileName,
      url: pdfUrl
    });

    this.currentPDFUrl = this.sanitizer.bypassSecurityTrustResourceUrl(pdfUrl);
    this.currentPDFName = pdfName; // Keep original name for display
    this.showPDFModal = true;
  }

  closePDFModal() {
    this.showPDFModal = false;
    this.currentPDFUrl = null;
    this.currentPDFName = '';
  }

  getStatusClass(status: ApplicationStatus): string {
    switch (status) {
      case ApplicationStatus.EN_ATTENTE:
        return 'status-pending';
      case ApplicationStatus.ACCEPTED:
        return 'status-accepted';
      case ApplicationStatus.REJECTED:
        return 'status-rejected';
      default:
        return '';
    }
  }

  getStatusText(status: ApplicationStatus): string {
    switch (status) {
      case ApplicationStatus.EN_ATTENTE:
        return 'En Attente';
      case ApplicationStatus.ACCEPTED:
        return 'Accepted';
      case ApplicationStatus.REJECTED:
        return 'Rejected';
      default:
        return status;
    }
  }

  getStatusIcon(status: ApplicationStatus): string {
    switch (status) {
      case ApplicationStatus.EN_ATTENTE:
        return '⏳';
      case ApplicationStatus.ACCEPTED:
        return '✅';
      case ApplicationStatus.REJECTED:
        return '❌';
      default:
        return '○';
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch (error) {
      return dateString;
    }
  }

  formatDateTime(dateString: string): string {
    if (!dateString) return '';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return dateString;
    }
  }

  goToEmployeeForm() {
    this.router.navigate(['/employee-form']);
  }

  goToAdminDashboard() {
    this.router.navigate(['/admin-dashboard']);
  }

  refresh() {
    this.loadEmployeeData();
  }

  getTotalChildren(): number {
    if (!this.employee) return 0;

    const primary = this.employee.primaryEducationChildren?.length || 0;
    const secondary = this.employee.secondaryEducationChildren?.length || 0;
    const higher = this.employee.higherEducationChildren?.length || 0;

    return primary + secondary + higher;
  }

  hasSpouseInfo(): boolean {
    return !!(this.employee?.wifeFirstName || this.employee?.wifeLastName || this.employee?.wifeWorkplace || this.employee?.wifeMatricule);
  }

  getApplicationStatusSummary(): string {
    if (this.applications.length === 0) return 'No applications submitted';

    const pending = this.applications.filter(app => app.status === ApplicationStatus.EN_ATTENTE).length;
    const accepted = this.applications.filter(app => app.status === ApplicationStatus.ACCEPTED).length;
    const rejected = this.applications.filter(app => app.status === ApplicationStatus.REJECTED).length;

    if (pending > 0) return `${pending} application(s) pending review`;
    if (accepted > 0 && rejected === 0) return `${accepted} application(s) approved`;
    if (rejected > 0 && accepted === 0) return `${rejected} application(s) rejected`;
    return `${accepted} approved, ${rejected} rejected`;
  }
}

