import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { Employee, PrimaryEducationChild, SecondaryEducationChild, HigherEducationChild } from '../../models/employee.model';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-form.html',
  styleUrls: ['./employee-form.css']
})
export class EmployeeFormComponent {
  employee: Employee = {
    firstName: '',
    lastName: '',
    matricule: '',
    dateBirth: '',
    workplace: '',
    wifeFirstName: '',
    wifeLastName: '',
    wifeWorkplace: '',
    wifeMatricule: '',
    numChildren: 0,
    primaryEducationChildren: [],
    secondaryEducationChildren: [],
    higherEducationChildren: []
  };

  primaryChildren: PrimaryEducationChild[] = [];
  secondaryChildren: SecondaryEducationChild[] = [];
  higherChildren: HigherEducationChild[] = [];

  primaryFiles: File[] = [];
  secondaryFiles: File[] = [];
  higherFiles: File[] = [];

  isSubmitting = false;
  message = '';
  messageType = '';
  showDropdown = false;
  quickAccessMatricule = '';

  constructor(
    private employeeService: EmployeeService,
    private router: Router
  ) {}

  addPrimaryChild() {
    this.primaryChildren.push({
      childFullName: '',
      childBirthDate: '',
      academicYear: ''
    });
    this.primaryFiles.push(new File([], ''));
  }

  addSecondaryChild() {
    this.secondaryChildren.push({
      childFullName: '',
      childBirthDate: '',
      academicYear: ''
    });
    this.secondaryFiles.push(new File([], ''));
  }

  addHigherChild() {
    this.higherChildren.push({
      childFullName: '',
      childBirthDate: '',
      academicYear: ''
    });
    this.higherFiles.push(new File([], ''));
  }

  removePrimaryChild(index: number) {
    this.primaryChildren.splice(index, 1);
    this.primaryFiles.splice(index, 1);
  }

  removeSecondaryChild(index: number) {
    this.secondaryChildren.splice(index, 1);
    this.secondaryFiles.splice(index, 1);
  }

  removeHigherChild(index: number) {
    this.higherChildren.splice(index, 1);
    this.higherFiles.splice(index, 1);
  }

  onPrimaryFileSelected(event: any, index: number) {
    const file = event.target.files[0];
    if (file) {
      this.primaryFiles[index] = file;
    }
  }

  onSecondaryFileSelected(event: any, index: number) {
    const file = event.target.files[0];
    if (file) {
      this.secondaryFiles[index] = file;
    }
  }

  onHigherFileSelected(event: any, index: number) {
    const file = event.target.files[0];
    if (file) {
      this.higherFiles[index] = file;
    }
  }

  async onSubmit() {
    if (this.isSubmitting) return;

    this.isSubmitting = true;
    this.message = '';

    try {
      // Log the employee data being sent
      console.log('Sending employee data:', this.employee);

      // First, register the employee
      const registeredEmployee = await this.employeeService.registerEmployee(this.employee).toPromise();

      if (!registeredEmployee || !registeredEmployee.id) {
        throw new Error('Failed to register employee');
      }

      // Add primary education children
      for (let i = 0; i < this.primaryChildren.length; i++) {
        const child = this.primaryChildren[i];
        const file = this.primaryFiles[i];

        if (child.childFullName && child.childBirthDate && child.academicYear) {
          const formData = new FormData();
          formData.append('childFullName', child.childFullName);
          formData.append('childBirthDate', child.childBirthDate);
          formData.append('academicYear', child.academicYear);
          if (file && file.size > 0) {
            formData.append('pdfFile', file);
          }

          await this.employeeService.addPrimaryEducationChild(registeredEmployee.id, formData).toPromise();
        }
      }

      // Add secondary education children
      for (let i = 0; i < this.secondaryChildren.length; i++) {
        const child = this.secondaryChildren[i];
        const file = this.secondaryFiles[i];

        if (child.childFullName && child.childBirthDate && child.academicYear) {
          const formData = new FormData();
          formData.append('childFullName', child.childFullName);
          formData.append('childBirthDate', child.childBirthDate);
          formData.append('academicYear', child.academicYear);
          if (file && file.size > 0) {
            formData.append('pdfFile', file);
          }

          await this.employeeService.addSecondaryEducationChild(registeredEmployee.id, formData).toPromise();
        }
      }

      // Add higher education children
      for (let i = 0; i < this.higherChildren.length; i++) {
        const child = this.higherChildren[i];
        const file = this.higherFiles[i];

        if (child.childFullName && child.childBirthDate && child.academicYear) {
          const formData = new FormData();
          formData.append('childFullName', child.childFullName);
          formData.append('childBirthDate', child.childBirthDate);
          formData.append('academicYear', child.academicYear);
          if (file && file.size > 0) {
            formData.append('pdfFile', file);
          }

          await this.employeeService.addHigherEducationChild(registeredEmployee.id, formData).toPromise();
        }
      }

      // Submit application
      await this.employeeService.submitApplication(registeredEmployee.id).toPromise();

      this.message = 'Employee registered and application submitted successfully!';
      this.messageType = 'success';

      // Redirect to user dashboard after 2 seconds
      setTimeout(() => {
        this.router.navigate(['/user-dashboard', this.employee.matricule]);
      }, 2000);

    } catch (error: any) {
      console.error('Registration error:', error);

      let errorMessage = 'Unknown error occurred';

      if (error.error) {
        if (typeof error.error === 'string') {
          errorMessage = error.error;
        } else if (error.error.message) {
          errorMessage = error.error.message;
        } else {
          errorMessage = JSON.stringify(error.error);
        }
      } else if (error.message) {
        errorMessage = error.message;
      }

      this.message = 'Error: ' + errorMessage;
      this.messageType = 'error';
    } finally {
      this.isSubmitting = false;
    }
  }

  goToUserDashboard() {
    if (this.employee.matricule && this.employee.matricule.trim()) {
      this.router.navigate(['/user-dashboard', this.employee.matricule.trim()]);
    } else {
      // Show a message to the user that matricule is required
      this.message = 'Please enter a matricule number to access the user dashboard.';
      this.messageType = 'error';

      // Clear the message after 3 seconds
      setTimeout(() => {
        this.message = '';
        this.messageType = '';
      }, 3000);

      // Focus on the matricule input field
      const matriculeInput = document.getElementById('matricule') as HTMLInputElement;
      if (matriculeInput) {
        matriculeInput.focus();
      }
    }
  }

  goToAdminDashboard() {
    this.router.navigate(['/admin-dashboard']);
  }

  // Alternative method to navigate to user dashboard with prompt
  goToUserDashboardWithPrompt() {
    const matricule = prompt('Please enter your matricule number to access your dashboard:');
    if (matricule && matricule.trim()) {
      this.router.navigate(['/user-dashboard', matricule.trim()]);
    }
  }

  // Quick access method using the input field
  goToUserDashboardQuick() {
    if (this.quickAccessMatricule && this.quickAccessMatricule.trim()) {
      this.router.navigate(['/user-dashboard', this.quickAccessMatricule.trim()]);
    }
  }

  // Dropdown management methods
  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  hideDropdown() {
    this.showDropdown = false;
  }

  // Close dropdown when clicking outside
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event) {
    const target = event.target as HTMLElement;
    const dropdown = target.closest('.dropdown');
    if (!dropdown) {
      this.showDropdown = false;
    }
  }
}

