import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { AdminService } from '../../services/admin.service';
import { Application, ApplicationStatus, Employee } from '../../models/employee.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css']
})
export class AdminDashboardComponent implements OnInit {
  applications: Application[] = [];
  filteredApplications: Application[] = [];
  selectedStatus: string = 'all';
  loading = true;
  error = '';

  // For tracking expanded applications
  expandedApplications: Set<number> = new Set();

  // For rejection modal
  showRejectModal = false;
  selectedApplicationId: number | null = null;
  rejectionReason = '';
  reviewedBy = 'Admin';

  // For PDF viewer modal
  showPDFModal = false;
  currentPDFUrl: SafeResourceUrl | null = null;
  currentPDFName = '';

  constructor(
    private adminService: AdminService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    this.loadApplications();
  }

  loadApplications() {
    this.loading = true;
    this.error = '';

    this.adminService.getAllApplications().subscribe({
      next: (applications) => {
        console.log('Loaded applications:', applications);

        // Debug: Check if employee data is present
        applications.forEach((app, index) => {
          console.log(`Application ${index}:`, {
            id: app.id,
            status: app.status,
            hasEmployee: !!app.employee,
            employee: app.employee,
            employeeChildren: app.employee ? {
              primary: app.employee.primaryEducationChildren?.length || 0,
              secondary: app.employee.secondaryEducationChildren?.length || 0,
              higher: app.employee.higherEducationChildren?.length || 0
            } : null
          });
        });

        this.applications = applications;
        this.filterApplications();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading applications:', error);
        this.error = 'Error loading applications. Please try again.';
        this.loading = false;
      }
    });
  }

  filterApplications() {
    if (this.selectedStatus === 'all') {
      this.filteredApplications = this.applications;
    } else {
      this.filteredApplications = this.applications.filter(
        app => app.status === this.selectedStatus
      );
    }
  }

  onStatusFilterChange() {
    this.filterApplications();
  }

  // Application expansion methods
  toggleApplicationDetails(applicationId: number) {
    if (this.expandedApplications.has(applicationId)) {
      this.expandedApplications.delete(applicationId);
    } else {
      this.expandedApplications.add(applicationId);
    }
  }

  isApplicationExpanded(applicationId: number): boolean {
    return this.expandedApplications.has(applicationId);
  }

  // Application actions
  approveApplication(applicationId: number) {
    if (confirm('Are you sure you want to approve this application?')) {
      this.adminService.approveApplication(applicationId, this.reviewedBy).subscribe({
        next: (updatedApplication) => {
          this.updateApplicationInList(updatedApplication);
          this.showSuccessMessage('Application approved successfully!');
        },
        error: (error) => {
          this.showErrorMessage('Error approving application: ' + error.message);
        }
      });
    }
  }

  openRejectModal(applicationId: number) {
    this.selectedApplicationId = applicationId;
    this.rejectionReason = '';
    this.showRejectModal = true;
  }

  closeRejectModal() {
    this.showRejectModal = false;
    this.selectedApplicationId = null;
    this.rejectionReason = '';
  }

  confirmReject() {
    if (this.selectedApplicationId && this.rejectionReason.trim()) {
      this.adminService.rejectApplication(
        this.selectedApplicationId,
        this.rejectionReason,
        this.reviewedBy
      ).subscribe({
        next: (updatedApplication) => {
          this.updateApplicationInList(updatedApplication);
          this.closeRejectModal();
          this.showSuccessMessage('Application rejected successfully!');
        },
        error: (error) => {
          this.showErrorMessage('Error rejecting application: ' + error.message);
        }
      });
    } else {
      this.showErrorMessage('Please provide a rejection reason.');
    }
  }

  deleteApplication(applicationId: number) {
    if (confirm('Are you sure you want to delete this application? This action cannot be undone.')) {
      this.adminService.deleteApplication(applicationId).subscribe({
        next: () => {
          this.applications = this.applications.filter(app => app.id !== applicationId);
          this.filterApplications();
          this.expandedApplications.delete(applicationId);
          this.showSuccessMessage('Application deleted successfully!');
        },
        error: (error) => {
          this.showErrorMessage('Error deleting application: ' + error.message);
        }
      });
    }
  }

  updateApplicationInList(updatedApplication: Application) {
    const index = this.applications.findIndex(app => app.id === updatedApplication.id);
    if (index !== -1) {
      this.applications[index] = updatedApplication;
      this.filterApplications();
    }
  }

  // PDF viewing methods
  viewPDF(pdfPath: string, pdfName: string) {
    // Use the stored file path (UUID filename) instead of the original filename
    // pdfPath contains the actual stored filename (UUID)
    const storedFileName = pdfPath; // This is the UUID filename returned by the backend
    const pdfUrl = `http://localhost:8080/api/files/${storedFileName}`;

    console.log('Admin Dashboard - Attempting to view PDF:', {
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

  // Employee details methods
  viewEmployeeDetails(employee: Employee) {
    // Toggle the application details to show employee info
    if (employee.id) {
      const application = this.applications.find(app => app.employee?.id === employee.id);
      if (application?.id) {
        this.toggleApplicationDetails(application.id);
      }
    }
  }

  viewDocuments(employee: Employee) {
    // Expand application and scroll to documents section
    if (employee.id) {
      const application = this.applications.find(app => app.employee?.id === employee.id);
      if (application?.id) {
        this.expandedApplications.add(application.id);
        // Small delay to ensure DOM is updated before scrolling
        setTimeout(() => {
          const element = document.getElementById(`app-${application.id}-documents`);
          if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
          }
        }, 100);
      }
    }
  }

  // Utility methods
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

  refresh() {
    this.loadApplications();
  }

  getEmployeeName(employee: Employee | undefined): string {
    if (!employee) return 'Unknown Employee';
    return `${employee.firstName} ${employee.lastName}`;
  }

  getEmployeeMatricule(employee: Employee | undefined): string {
    if (!employee) return 'N/A';
    return employee.matricule;
  }

  hasChildren(employee: Employee | undefined): boolean {
    if (!employee) return false;
    return !!(
      employee.primaryEducationChildren?.length ||
      employee.secondaryEducationChildren?.length ||
      employee.higherEducationChildren?.length
    );
  }

  getTotalChildren(employee: Employee | undefined): number {
    if (!employee) return 0;
    const primary = employee.primaryEducationChildren?.length || 0;
    const secondary = employee.secondaryEducationChildren?.length || 0;
    const higher = employee.higherEducationChildren?.length || 0;
    return primary + secondary + higher;
  }

  // Statistics methods
  getPendingCount(): number {
    return this.applications.filter(app => app.status === ApplicationStatus.EN_ATTENTE).length;
  }

  getAcceptedCount(): number {
    return this.applications.filter(app => app.status === ApplicationStatus.ACCEPTED).length;
  }

  getRejectedCount(): number {
    return this.applications.filter(app => app.status === ApplicationStatus.REJECTED).length;
  }

  // Message methods
  private showSuccessMessage(message: string) {
    // You can implement a toast notification system here
    alert(message);
  }

  private showErrorMessage(message: string) {
    // You can implement a toast notification system here
    alert(message);
  }

  // Search and filter methods
  searchApplications(searchTerm: string) {
    if (!searchTerm.trim()) {
      this.filterApplications();
      return;
    }

    const term = searchTerm.toLowerCase();
    this.filteredApplications = this.applications.filter(app => {
      const employee = app.employee;
      if (!employee) return false;

      return (
        employee.firstName.toLowerCase().includes(term) ||
        employee.lastName.toLowerCase().includes(term) ||
        employee.matricule.toLowerCase().includes(term) ||
        app.id?.toString().includes(term)
      );
    });
  }

  // Bulk actions
  selectAllApplications() {
    // Implementation for bulk selection
  }

  bulkApprove() {
    // Implementation for bulk approval
  }

  bulkReject() {
    // Implementation for bulk rejection
  }

  // Export methods
  exportToCSV() {
    // Implementation for CSV export
  }

  exportToPDF() {
    // Implementation for PDF export
  }
}

