# Hospital Management System - Android Port
## Team Task Assignment (4 Members)

---

## Member 1: Project Setup & Database Layer

### Phase 1: Project Setup & Architecture
- [ ] Create new Android project with Java
- [ ] Set up build.gradle with dependencies (SQLite, Material Design, etc.)
- [ ] Configure AndroidManifest.xml with required permissions
- [ ] Create project folder structure (UI/Activities, Models, DAL, Helpers)

### Phase 2: Database Layer (SQLite)
- [ ] Create DatabaseHelper class extending SQLiteOpenHelper
- [ ] Design SQLite database schema based on existing models:
  - [ ] User table
  - [ ] Patient table
  - [ ] Doctor table
  - [ ] Department table
  - [ ] Appointment table
  - [ ] MedicalRecord table
  - [ ] Prescription table
  - [ ] Medication table
  - [ ] Invoice table
  - [ ] Payment table
  - [ ] Report table
- [ ] Implement CRUD operations for each table
- [ ] Add database version management (onUpgrade)

---

## Member 2: Data Models & Data Access Layer

### Phase 3: Data Models (Java)
- [ ] Convert C# Model classes to Java:
  - [ ] User model
  - [ ] Patient model
  - [ ] Doctor model
  - [ ] Department model
  - [ ] Appointment model
  - [ ] MedicalRecord model
  - [ ] Prescription model
  - [ ] Medication model
  - [ ] Invoice model
  - [ ] Payment model
  - [ ] Report model

### Phase 4: Data Access Layer (DAL)
- [ ] Create DAL classes for each entity:
  - [ ] UserDAL
  - [ ] PatientDAL
  - [ ] DoctorDAL
  - [ ] DepartmentDAL
  - [ ] AppointmentDAL
  - [ ] MedicalRecordDAL
  - [ ] PrescriptionDAL
  - [ ] MedicationDAL
  - [ ] InvoiceDAL
  - [ ] PaymentDAL
  - [ ] ReportDAL

---

## Member 3: UI Layer - Authentication & Main Dashboard

### Phase 5: UI Layer - Authentication
- [ ] Create LoginActivity
- [ ] Implement username/password validation
- [ ] Implement user authentication against SQLite
- [ ] Create session management (SharedPreferences)
- [ ] Implement logout functionality

### Phase 6: UI Layer - Main Dashboard
- [ ] Create MainActivity with navigation drawer
- [ ] Display user info and role
- [ ] Implement role-based menu visibility (Admin, Doctor, Nurse, Receptionist)

### Phase 7: UI Layer - Core Management Modules
- [ ] Patient Management:
  - [ ] PatientListActivity (RecyclerView)
  - [ ] PatientFormActivity (Add/Edit)
  - [ ] Search functionality
- [ ] Doctor Management:
  - [ ] DoctorListActivity
  - [ ] DoctorFormActivity

---

## Member 4: UI Layer - Remaining Modules & Polish

### Phase 7: UI Layer - Additional Management Modules
- [ ] Appointment Management:
  - [ ] AppointmentListActivity
  - [ ] AppointmentFormActivity
  - [ ] Status update (Scheduled/Completed/Cancelled)
- [ ] Department Management:
  - [ ] DepartmentListActivity
  - [ ] DepartmentFormActivity
- [ ] User Management (Admin only):
  - [ ] UserListActivity
  - [ ] UserFormActivity

### Phase 8: Additional Features
- [ ] Medical Records Management
- [ ] Prescriptions Management
- [ ] Medications Management
- [ ] Invoice Generation
- [ ] Payment Processing
- [ ] Reports Generation

### Phase 9: Polish & Testing
- [ ] Add input validation
- [ ] Implement error handling
- [ ] Add loading indicators
- [ ] Test on various Android devices
- [ ] Performance optimization
- [ ] Build debug APK

---

## Notes
- Member 1 handles infrastructure (foundation)
- Member 2 handles data layer (core logic)
- Member 3 handles authentication and main UI
- Member 4 handles remaining modules and final testing
