package com.example.a4200finalproj.DAL;

import android.content.Context;

import com.example.a4200finalproj.Models.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DataSeeder - Seeds the database with test data for development/testing
 * Run this once to populate the database with sample data
 */
public class DataSeeder {

    private final Context context;
    private final SimpleDateFormat dateFormat;

    public DataSeeder(Context context) {
        this.context = context;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    public void seedAllData() {
        // Order matters due to foreign key dependencies
        seedDepartments();
        seedUsers();
        seedDoctors();
        seedPatients();
        seedAppointments();
        seedMedicalRecords();
        seedMedications();
        seedPrescriptions();
        seedInvoices();
        seedPayments();
        seedReports();
    }

    private void seedDepartments() {
        DALDepartment dal = new DALDepartment(context);

        Department d1 = new Department();
        d1.setName("Cardiology");
        d1.setDescription("Heart and cardiovascular system care");
        d1.setLocation("Building A, Floor 2");
        d1.setPhone("555-0101");
        dal.insert(d1);

        Department d2 = new Department();
        d2.setName("Neurology");
        d2.setDescription("Brain and nervous system care");
        d2.setLocation("Building A, Floor 3");
        d2.setPhone("555-0102");
        dal.insert(d2);

        Department d3 = new Department();
        d3.setName("Pediatrics");
        d3.setDescription("Children's healthcare");
        d3.setLocation("Building B, Floor 1");
        d3.setPhone("555-0103");
        dal.insert(d3);

        Department d4 = new Department();
        d4.setName("Orthopedics");
        d4.setDescription("Bone and joint care");
        d4.setLocation("Building B, Floor 2");
        d4.setPhone("555-0104");
        dal.insert(d4);

        Department d5 = new Department();
        d5.setName("General Medicine");
        d5.setDescription("Primary care and general health");
        d5.setLocation("Building A, Floor 1");
        d5.setPhone("555-0105");
        dal.insert(d5);
    }

    private void seedUsers() {
        DALUser dal = new DALUser(context);

        // Admin user
        User u1 = new User();
        u1.setUsername("admin");
        u1.setPassword("admin123");
        u1.setFullName("John Smith");
        u1.setEmail("john.smith@hospital.com");
        u1.setRole("admin");
        u1.setPhone("555-1001");
        u1.setAddress("123 Admin Street");
        u1.setIsActive(1);
        dal.addUser(u1);

        // Doctor users
        User u2 = new User();
        u2.setUsername("dr_wilson");
        u2.setPassword("doctor123");
        u2.setFullName("Dr. Emily Wilson");
        u2.setEmail("emily.wilson@hospital.com");
        u2.setRole("doctor");
        u2.setPhone("555-1002");
        u2.setAddress("456 Doctor Lane");
        u2.setIsActive(1);
        dal.addUser(u2);

        User u3 = new User();
        u3.setUsername("dr_johnson");
        u3.setPassword("doctor123");
        u3.setFullName("Dr. Michael Johnson");
        u3.setEmail("michael.johnson@hospital.com");
        u3.setRole("doctor");
        u3.setPhone("555-1003");
        u3.setAddress("789 Medical Ave");
        u3.setIsActive(1);
        dal.addUser(u3);

        User u4 = new User();
        u4.setUsername("dr_brown");
        u4.setPassword("doctor123");
        u4.setFullName("Dr. Sarah Brown");
        u4.setEmail("sarah.brown@hospital.com");
        u4.setRole("doctor");
        u4.setPhone("555-1004");
        u4.setAddress("321 Health Road");
        u4.setIsActive(1);
        dal.addUser(u4);

        User u5 = new User();
        u5.setUsername("dr_davis");
        u5.setPassword("doctor123");
        u5.setFullName("Dr. Robert Davis");
        u5.setEmail("robert.davis@hospital.com");
        u5.setRole("doctor");
        u5.setPhone("555-1005");
        u5.setAddress("654 Care Street");
        u5.setIsActive(1);
        dal.addUser(u5);

        // Nurse user
        User u6 = new User();
        u6.setUsername("nurse_garcia");
        u6.setPassword("nurse123");
        u6.setFullName("Maria Garcia");
        u6.setEmail("maria.garcia@hospital.com");
        u6.setRole("nurse");
        u6.setPhone("555-1006");
        u6.setAddress("987 Hospital Blvd");
        u6.setIsActive(1);
        dal.addUser(u6);
    }

    private void seedDoctors() {
        DALDoctor dal = new DALDoctor(context);

        Doctor d1 = new Doctor();
        d1.setFullName("Dr. Emily Wilson");
        d1.setSpecialization("Cardiologist");
        d1.setLicenseNumber("MD-12345");
        d1.setPhone("555-2001");
        d1.setEmail("emily.wilson@hospital.com");
        d1.setAddress("456 Doctor Lane");
        d1.setDepartmentId(1);
        dal.addDoctor(d1);

        Doctor d2 = new Doctor();
        d2.setFullName("Dr. Michael Johnson");
        d2.setSpecialization("Neurologist");
        d2.setLicenseNumber("MD-12346");
        d2.setPhone("555-2002");
        d2.setEmail("michael.johnson@hospital.com");
        d2.setAddress("789 Medical Ave");
        d2.setDepartmentId(2);
        dal.addDoctor(d2);

        Doctor d3 = new Doctor();
        d3.setFullName("Dr. Sarah Brown");
        d3.setSpecialization("Pediatrician");
        d3.setLicenseNumber("MD-12347");
        d3.setPhone("555-2003");
        d3.setEmail("sarah.brown@hospital.com");
        d3.setAddress("321 Health Road");
        d3.setDepartmentId(3);
        dal.addDoctor(d3);

        Doctor d4 = new Doctor();
        d4.setFullName("Dr. Robert Davis");
        d4.setSpecialization("Orthopedic Surgeon");
        d4.setLicenseNumber("MD-12348");
        d4.setPhone("555-2004");
        d4.setEmail("robert.davis@hospital.com");
        d4.setAddress("654 Care Street");
        d4.setDepartmentId(4);
        dal.addDoctor(d4);
    }

    private void seedPatients() {
        DALPatient dal = new DALPatient(context);

        Patient p1 = new Patient();
        p1.setFullName("Alice Thompson");
        p1.setDateOfBirth("1985-03-15");
        p1.setGender("Female");
        p1.setPhone("555-3001");
        p1.setEmail("alice.thompson@email.com");
        p1.setAddress("100 Patient Way");
        p1.setEmergencyContact("James Thompson");
        p1.setEmergencyPhone("555-3002");
        p1.setBloodType("A+");
        p1.setAllergies("Penicillin");
        dal.addPatient(p1);

        Patient p2 = new Patient();
        p2.setFullName("Bob Martinez");
        p2.setDateOfBirth("1978-07-22");
        p2.setGender("Male");
        p2.setPhone("555-3003");
        p2.setEmail("bob.martinez@email.com");
        p2.setAddress("200 Health Street");
        p2.setEmergencyContact("Linda Martinez");
        p2.setEmergencyPhone("555-3004");
        p2.setBloodType("O-");
        p2.setAllergies("None");
        dal.addPatient(p2);

        Patient p3 = new Patient();
        p3.setFullName("Carol Williams");
        p3.setDateOfBirth("1990-11-08");
        p3.setGender("Female");
        p3.setPhone("555-3005");
        p3.setEmail("carol.williams@email.com");
        p3.setAddress("300 Medical Lane");
        p3.setEmergencyContact("Tom Williams");
        p3.setEmergencyPhone("555-3006");
        p3.setBloodType("B+");
        p3.setAllergies("Sulfa drugs");
        dal.addPatient(p3);

        Patient p4 = new Patient();
        p4.setFullName("David Lee");
        p4.setDateOfBirth("1995-01-30");
        p4.setGender("Male");
        p4.setPhone("555-3007");
        p4.setEmail("david.lee@email.com");
        p4.setAddress("400 Care Ave");
        p4.setEmergencyContact("Susan Lee");
        p4.setEmergencyPhone("555-3008");
        p4.setBloodType("AB+");
        p4.setAllergies("Aspirin");
        dal.addPatient(p4);

        Patient p5 = new Patient();
        p5.setFullName("Emma Anderson");
        p5.setDateOfBirth("2010-05-12");
        p5.setGender("Female");
        p5.setPhone("555-3009");
        p5.setEmail("sarah.anderson@email.com");
        p5.setAddress("500 Healing Road");
        p5.setEmergencyContact("Sarah Anderson");
        p5.setEmergencyPhone("555-3010");
        p5.setBloodType("O+");
        p5.setAllergies("None");
        dal.addPatient(p5);

        Patient p6 = new Patient();
        p6.setFullName("Frank White");
        p6.setDateOfBirth("1965-09-25");
        p6.setGender("Male");
        p6.setPhone("555-3011");
        p6.setEmail("frank.white@email.com");
        p6.setAddress("600 Wellness Blvd");
        p6.setEmergencyContact("Mary White");
        p6.setEmergencyPhone("555-3012");
        p6.setBloodType("A-");
        p6.setAllergies("Codeine");
        dal.addPatient(p6);
    }

    private void seedAppointments() {
        DALAppointment dal = new DALAppointment(context);

        Appointment a1 = new Appointment();
        a1.setPatientId(1);
        a1.setDoctorId(1);
        a1.setDepartmentId(1);
        a1.setAppointmentDate("2024-03-20");
        a1.setAppointmentTime("09:00");
        a1.setReason("Heart checkup");
        a1.setStatus("Completed");
        a1.setNotes("Regular cardiac examination");
        dal.addAppointment(a1);

        Appointment a2 = new Appointment();
        a2.setPatientId(2);
        a2.setDoctorId(2);
        a2.setDepartmentId(2);
        a2.setAppointmentDate("2024-03-21");
        a2.setAppointmentTime("10:30");
        a2.setReason("Headache consultation");
        a2.setStatus("Completed");
        a2.setNotes("Patient reported persistent headaches");
        dal.addAppointment(a2);

        Appointment a3 = new Appointment();
        a3.setPatientId(3);
        a3.setDoctorId(3);
        a3.setDepartmentId(3);
        a3.setAppointmentDate("2024-03-22");
        a3.setAppointmentTime("14:00");
        a3.setReason("Annual checkup");
        a3.setStatus("Scheduled");
        a3.setNotes("Routine pediatric visit");
        dal.addAppointment(a3);

        Appointment a4 = new Appointment();
        a4.setPatientId(4);
        a4.setDoctorId(4);
        a4.setDepartmentId(4);
        a4.setAppointmentDate("2024-03-23");
        a4.setAppointmentTime("11:00");
        a4.setReason("Knee pain");
        a4.setStatus("Scheduled");
        a4.setNotes("Sports injury follow-up");
        dal.addAppointment(a4);

        Appointment a5 = new Appointment();
        a5.setPatientId(5);
        a5.setDoctorId(3);
        a5.setDepartmentId(3);
        a5.setAppointmentDate("2024-03-24");
        a5.setAppointmentTime("15:30");
        a5.setReason("Vaccination");
        a5.setStatus("Scheduled");
        a5.setNotes("Routine immunization");
        dal.addAppointment(a5);
    }

    private void seedMedicalRecords() {
        DALMedicalRecord dal = new DALMedicalRecord(context);

        MedicalRecord m1 = new MedicalRecord();
        m1.setPatientId(1);
        m1.setDoctorId(1);
        m1.setVisitDate("2024-03-20");
        m1.setDiagnosis("Normal cardiac function");
        m1.setComplaints("Routine checkup, no complaints");
        m1.setTreatment("Annual cardiac screening recommended");
        m1.setVitalSigns("BP: 120/80, HR: 72");
        m1.setNotes("All tests within normal range");
        dal.insert(m1);

        MedicalRecord m2 = new MedicalRecord();
        m2.setPatientId(2);
        m2.setDoctorId(2);
        m2.setVisitDate("2024-03-21");
        m2.setDiagnosis("Tension headache");
        m2.setComplaints("Persistent headache for 2 weeks");
        m2.setTreatment("Prescribed pain relief, stress management");
        m2.setVitalSigns("BP: 118/78, HR: 70");
        m2.setNotes("MRI scheduled if symptoms persist");
        dal.insert(m2);

        MedicalRecord m3 = new MedicalRecord();
        m3.setPatientId(4);
        m3.setDoctorId(4);
        m3.setVisitDate("2024-03-15");
        m3.setDiagnosis("Meniscal tear");
        m3.setComplaints("Knee pain after running");
        m3.setTreatment("Physical therapy, knee brace");
        m3.setVitalSigns("BP: 122/82, HR: 75");
        m3.setNotes("Follow-up in 2 weeks");
        dal.insert(m3);
    }

    private void seedMedications() {
        DALMedication dal = new DALMedication(context);

        Medication m1 = new Medication();
        m1.setName("Amoxicillin");
        m1.setGenericName("Amoxicillin");
        m1.setDosage("500mg");
        m1.setForm("Capsule");
        m1.setManufacturer("PharmaCorp");
        m1.setSideEffects("Nausea, diarrhea, allergic reactions");
        m1.setIsActive(1);
        dal.insert(m1);

        Medication m2 = new Medication();
        m2.setName("Ibuprofen");
        m2.setGenericName("Ibuprofen");
        m2.setDosage("400mg");
        m2.setForm("Tablet");
        m2.setManufacturer("MediLab");
        m2.setSideEffects("Stomach upset, headache, dizziness");
        m2.setIsActive(1);
        dal.insert(m2);

        Medication m3 = new Medication();
        m3.setName("Lisinopril");
        m3.setGenericName("Lisinopril");
        m3.setDosage("10mg");
        m3.setForm("Tablet");
        m3.setManufacturer("CardioPharm");
        m3.setSideEffects("Dry cough, dizziness, headache");
        m3.setIsActive(1);
        dal.insert(m3);

        Medication m4 = new Medication();
        m4.setName("Metformin");
        m4.setGenericName("Metformin Hydrochloride");
        m4.setDosage("500mg");
        m4.setForm("Tablet");
        m4.setManufacturer("DiaCare");
        m4.setSideEffects("Nausea, stomach upset, metallic taste");
        m4.setIsActive(1);
        dal.insert(m4);

        Medication m5 = new Medication();
        m5.setName("Atorvastatin");
        m5.setGenericName("Atorvastatin Calcium");
        m5.setDosage("20mg");
        m5.setForm("Tablet");
        m5.setManufacturer("HeartPharm");
        m5.setSideEffects("Muscle pain, headache, joint pain");
        m5.setIsActive(1);
        dal.insert(m5);
    }

    private void seedPrescriptions() {
        DALPrescription dal = new DALPrescription(context);

        Prescription p1 = new Prescription();
        p1.setMedicalRecordId(1);
        p1.setPatientId(1);
        p1.setDoctorId(1);
        p1.setMedicationId(5);
        p1.setDosage("20mg");
        p1.setDuration("30 days");
        p1.setFrequency("Once daily");
        p1.setInstructions("Take with food at bedtime");
        p1.setIsActive(1);
        dal.addPrescription(p1);

        Prescription p2 = new Prescription();
        p2.setMedicalRecordId(2);
        p2.setPatientId(2);
        p2.setDoctorId(2);
        p2.setMedicationId(2);
        p2.setDosage("400mg");
        p2.setDuration("7 days");
        p2.setFrequency("Three times daily");
        p2.setInstructions("Take with meals");
        p2.setIsActive(1);
        dal.addPrescription(p2);

        Prescription p3 = new Prescription();
        p3.setMedicalRecordId(2);
        p3.setPatientId(2);
        p3.setDoctorId(2);
        p3.setMedicationId(1);
        p3.setDosage("500mg");
        p3.setDuration("10 days");
        p3.setFrequency("Twice daily");
        p3.setInstructions("Complete full course");
        p3.setIsActive(1);
        dal.addPrescription(p3);
    }

    private void seedInvoices() {
        DALInvoice dal = new DALInvoice(context);

        Invoice i1 = new Invoice();
        i1.setPatientId(1);
        i1.setInvoiceNumber("INV-2024-001");
        i1.setInvoiceDate("2024-03-20");
        i1.setDueDate("2024-04-20");
        i1.setSubtotal(150.00);
        i1.setTax(15.00);
        i1.setTotal(165.00);
        i1.setStatus("Paid");
        i1.setNotes("Cardiac checkup");
        dal.insert(i1);

        Invoice i2 = new Invoice();
        i2.setPatientId(2);
        i2.setInvoiceNumber("INV-2024-002");
        i2.setInvoiceDate("2024-03-21");
        i2.setDueDate("2024-04-21");
        i2.setSubtotal(200.00);
        i2.setTax(20.00);
        i2.setTotal(220.00);
        i2.setStatus("Pending");
        i2.setNotes("Neurology consultation");
        dal.insert(i2);

        Invoice i3 = new Invoice();
        i3.setPatientId(3);
        i3.setInvoiceNumber("INV-2024-003");
        i3.setInvoiceDate("2024-03-22");
        i3.setDueDate("2024-04-22");
        i3.setSubtotal(100.00);
        i3.setTax(10.00);
        i3.setTotal(110.00);
        i3.setStatus("Pending");
        i3.setNotes("Pediatric visit");
        dal.insert(i3);

        Invoice i4 = new Invoice();
        i4.setPatientId(4);
        i4.setInvoiceNumber("INV-2024-004");
        i4.setInvoiceDate("2024-03-15");
        i4.setDueDate("2024-04-15");
        i4.setSubtotal(300.00);
        i4.setTax(30.00);
        i4.setTotal(330.00);
        i4.setStatus("Paid");
        i4.setNotes("Orthopedic consultation");
        dal.insert(i4);
    }

    private void seedPayments() {
        DALPayment dal = new DALPayment(context);

        Payment pay1 = new Payment();
        pay1.setInvoiceId(1);
        pay1.setPatientId(1);
        pay1.setAmount(165.00);
        pay1.setPaymentDate("2024-03-20");
        pay1.setPaymentMethod("Credit Card");
        pay1.setTransactionId("TXN-001-2024");
        pay1.setStatus("Completed");
        pay1.setNotes("Full payment received");
        dal.addPayment(pay1);

        Payment pay2 = new Payment();
        pay2.setInvoiceId(4);
        pay2.setPatientId(4);
        pay2.setAmount(330.00);
        pay2.setPaymentDate("2024-03-16");
        pay2.setPaymentMethod("Cash");
        pay2.setTransactionId("TXN-002-2024");
        pay2.setStatus("Completed");
        pay2.setNotes("Full payment received");
        dal.addPayment(pay2);
    }

    private void seedReports() {
        DALReport dal = new DALReport(context);

        Report r1 = new Report();
        r1.setReportType("Medical Summary");
        r1.setTitle("Cardiac Checkup Report");
        r1.setPatientId(1);
        r1.setDoctorId(1);
        r1.setGeneratedDate("2024-03-20");
        r1.setData("Patient shows normal cardiac function. All vitals within normal range.");
        r1.setFilePath("/reports/cardiac_001.pdf");
        r1.setGeneratedBy("Dr. Emily Wilson");
        dal.addReport(r1);

        Report r2 = new Report();
        r2.setReportType("Laboratory");
        r2.setTitle("Blood Test Results");
        r2.setPatientId(2);
        r2.setDoctorId(2);
        r2.setGeneratedDate("2024-03-21");
        r2.setData("All blood panel results within normal range. No abnormalities detected.");
        r2.setFilePath("/reports/lab_002.pdf");
        r2.setGeneratedBy("Dr. Michael Johnson");
        dal.addReport(r2);

        Report r3 = new Report();
        r3.setReportType("Financial");
        r3.setTitle("Payment Summary - March 2024");
        r3.setPatientId(1);
        r3.setDoctorId(1);
        r3.setGeneratedDate("2024-03-31");
        r3.setData("Total charges: $165.00, Paid: $165.00, Balance: $0.00");
        r3.setFilePath("/reports/financial_003.pdf");
        r3.setGeneratedBy("System");
        dal.addReport(r3);
    }
}