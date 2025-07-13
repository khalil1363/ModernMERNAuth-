# Employee Management Application

A full-stack web application built with Spring Boot (backend) and Angular (frontend) for managing employee information and education child records with admin approval system.

## Features

### User Features
- **Employee Registration**: Complete employee information form including personal details and spouse information
- **Children Education Management**: Add children with different education levels:
  - Primary Education Children
  - Secondary Education Children  
  - Higher Education Children
- **Application Status Tracking**: View application status (En Attente, Accepted, Rejected)
- **File Upload**: Upload PDF documents for each child's education records

### Admin Features
- **Application Management**: View all employee applications
- **Status Filtering**: Filter applications by status (All, En Attente, Accepted, Rejected)
- **Application Review**: Approve or reject applications
- **Rejection Reasons**: Provide detailed rejection reasons
- **Application Statistics**: View summary statistics of all applications

## Technology Stack

### Backend (Spring Boot)
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory for development)
- **Maven** for dependency management

### Frontend (Angular)
- **Angular 17**
- **TypeScript**
- **Reactive Forms**
- **HTTP Client** for API communication
- **CSS** for styling

## Project Structure

```
employee-management-app/
├── backend/                          # Spring Boot application
│   ├── src/main/java/com/example/employeemanagement/
│   │   ├── model/                    # Entity classes
│   │   │   ├── Employee.java
│   │   │   ├── Application.java
│   │   │   ├── PrimaryEducationChild.java
│   │   │   ├── SecondaryEducationChild.java
│   │   │   └── HigherEducationChild.java
│   │   ├── repository/               # JPA repositories
│   │   ├── service/                  # Service interfaces
│   │   ├── service/impl/             # Service implementations
│   │   ├── controller/               # REST controllers
│   │   └── config/                   # Configuration classes
│   ├── src/main/resources/
│   │   └── application.properties    # Application configuration
│   └── pom.xml                       # Maven dependencies
├── frontend/                         # Angular application
│   └── employee-management-frontend/
│       ├── src/app/
│       │   ├── components/           # Angular components
│       │   │   ├── employee-form/
│       │   │   ├── user-dashboard/
│       │   │   └── admin-dashboard/
│       │   ├── services/             # Angular services
│       │   ├── models/               # TypeScript interfaces
│       │   └── app.routes.ts         # Routing configuration
│       └── package.json              # NPM dependencies
└── README.md                         # This file
```

## Database Schema

### Employee Table
- id (Primary Key)
- firstName, lastName, matricule
- dateBirth, workplace
- wifeFirstName, wifeLastName, wifeWorkplace, wifeMatricule
- numChildren

### Application Table
- id (Primary Key)
- employee_id (Foreign Key)
- status (EN_ATTENTE, ACCEPTED, REJECTED)
- submissionDate, reviewDate
- reviewedBy, rejectionReason

### Education Children Tables
- primary_education_children
- secondary_education_children  
- higher_education_children

Each with: id, employee_id, childFullName, childBirthDate, academicYear, pdfFileName, pdfFilePath

## Setup Instructions

### Prerequisites
- **Java 17** or higher
- **Node.js 18** or higher
- **Maven 3.6** or higher
- **Angular CLI** (`npm install -g @angular/cli`)

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend/employee-management-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   ng serve
   ```

4. The frontend will start on `http://localhost:4200`

## API Endpoints

### Employee Endpoints
- `POST /api/employee/register` - Register new employee with application
- `GET /api/employee/{matricule}/applications` - Get employee applications

### Admin Endpoints  
- `GET /api/admin/applications` - Get all applications
- `PUT /api/admin/applications/{id}/approve` - Approve application
- `PUT /api/admin/applications/{id}/reject` - Reject application with reason
- `DELETE /api/admin/applications/{id}` - Delete application

### File Upload Endpoints
- `POST /api/files/upload` - Upload PDF files
- `GET /api/files/{filename}` - Download files

## Usage Guide

### For Users (Employees)

1. **Access the Application**: Open `http://localhost:4200` in your browser
2. **Fill Employee Form**: Complete all required fields:
   - Personal information (First Name, Last Name, Matricule, Date of Birth, Workplace)
   - Spouse information (optional)
   - Number of children
3. **Add Children**: Click "Add [Education Level] Child" buttons to add children
4. **Upload Documents**: Upload PDF files for each child's education records
5. **Submit Application**: Click "Submit Application" to send for review
6. **Check Status**: Use "User Dashboard" to view application status

### For Admins

1. **Access Admin Dashboard**: Click "Admin Dashboard" button
2. **View Applications**: See all submitted applications with status summary
3. **Filter Applications**: Use status filter to view specific application types
4. **Review Applications**: 
   - Click "Approve" to accept an application
   - Click "Reject" to reject with a reason
5. **Manage Applications**: Delete applications if needed

## Application Status Flow

1. **EN_ATTENTE** (Pending) - Initial status when application is submitted
2. **ACCEPTED** - Application approved by admin
3. **REJECTED** - Application rejected by admin with reason

## File Upload

- Supports PDF files only
- Files are stored in the `uploads/` directory
- Each child can have one PDF document
- File names are automatically generated to prevent conflicts

## Configuration

### Backend Configuration (application.properties)
```properties
# Server configuration
server.port=8080

# Database configuration (H2 in-memory)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop

# File upload configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Frontend Configuration
- API base URL: `http://localhost:8080`
- Default routing to employee form
- CORS enabled for cross-origin requests

## Known Issues

1. **Date Format**: There's a minor date parsing issue in the frontend that needs to be fixed. The date should be sent in YYYY-MM-DD format.

## Future Enhancements

1. **Authentication & Authorization**: Add user login and role-based access
2. **Email Notifications**: Send email notifications for status changes
3. **Advanced Search**: Add search functionality for applications
4. **Audit Trail**: Track all changes to applications
5. **Reporting**: Generate reports for applications
6. **Database Migration**: Move from H2 to production database (PostgreSQL/MySQL)

## Development Notes

- The application uses reactive forms in Angular for better validation
- Spring Boot provides RESTful APIs with proper HTTP status codes
- File uploads are handled with multipart form data
- CORS is configured to allow frontend-backend communication
- H2 database console available at `http://localhost:8080/h2-console`

## Troubleshooting

### Common Issues

1. **Port Already in Use**: 
   - Backend: Change port in `application.properties`
   - Frontend: Use `ng serve --port 4201`

2. **CORS Issues**: Ensure WebConfig.java has proper CORS configuration

3. **File Upload Issues**: Check file size limits and upload directory permissions

4. **Database Issues**: H2 database resets on application restart (by design)

## Support

For issues or questions, please check:
1. Application logs in the console
2. Browser developer tools for frontend errors
3. H2 database console for data verification

---

**Note**: This is a development version using H2 in-memory database. For production use, configure a persistent database and implement proper security measures.

