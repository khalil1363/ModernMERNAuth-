import { Routes } from '@angular/router';
import { EmployeeFormComponent } from './components/employee-form/employee-form';
import { UserDashboardComponent } from './components/user-dashboard/user-dashboard';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard';

export const routes: Routes = [
  { path: '', redirectTo: '/employee-form', pathMatch: 'full' },
  { path: 'employee-form', component: EmployeeFormComponent },
  { path: 'user-dashboard/:matricule', component: UserDashboardComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent }
];

