export interface Employee {
  id?: number;
  firstName: string;
  lastName: string;
  matricule: string;
  dateBirth: string;
  workplace: string;
  wifeFirstName?: string;
  wifeLastName?: string;
  wifeWorkplace?: string;
  wifeMatricule?: string;
  numChildren: number;
  primaryEducationChildren?: PrimaryEducationChild[];
  secondaryEducationChildren?: SecondaryEducationChild[];
  higherEducationChildren?: HigherEducationChild[];
  applications?: Application[];
}

export interface PrimaryEducationChild {
  id?: number;
  childFullName: string;
  childBirthDate: string;
  academicYear: string;
  pdfFilePath?: string;
  pdfFileName?: string;
}

export interface SecondaryEducationChild {
  id?: number;
  childFullName: string;
  childBirthDate: string;
  academicYear: string;
  pdfFilePath?: string;
  pdfFileName?: string;
}

export interface HigherEducationChild {
  id?: number;
  childFullName: string;
  childBirthDate: string;
  academicYear: string;
  pdfFilePath?: string;
  pdfFileName?: string;
}

export interface Application {
  id?: number;
  employee?: Employee;
  status: ApplicationStatus;
  submissionDate?: string;
  reviewDate?: string;
  rejectionReason?: string;
  reviewedBy?: string;
}

export enum ApplicationStatus {
  EN_ATTENTE = 'EN_ATTENTE',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED'
}

