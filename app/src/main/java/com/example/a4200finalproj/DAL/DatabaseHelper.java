package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hospital_management.db";
    private static final int DATABASE_VERSION = 1;

    // Singleton instance
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    private void createAllTables(SQLiteDatabase db) {
        // User table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableUser.TABLE_NAME + " (" +
                TableUser.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableUser.COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                TableUser.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                TableUser.COLUMN_EMAIL + " TEXT, " +
                TableUser.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                TableUser.COLUMN_ROLE + " TEXT NOT NULL, " +
                TableUser.COLUMN_PHONE + " TEXT, " +
                TableUser.COLUMN_ADDRESS + " TEXT, " +
                TableUser.COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                TableUser.COLUMN_CREATED_AT + " TEXT, " +
                TableUser.COLUMN_UPDATED_AT + " TEXT)");

        // Department table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableDepartment.TABLE_NAME + " (" +
                TableDepartment.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableDepartment.COLUMN_NAME + " TEXT NOT NULL, " +
                TableDepartment.COLUMN_DESCRIPTION + " TEXT, " +
                TableDepartment.COLUMN_LOCATION + " TEXT, " +
                TableDepartment.COLUMN_PHONE + " TEXT, " +
                TableDepartment.COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                TableDepartment.COLUMN_CREATED_AT + " TEXT, " +
                TableDepartment.COLUMN_UPDATED_AT + " TEXT)");

        // Doctor table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableDoctor.TABLE_NAME + " (" +
                TableDoctor.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableDoctor.COLUMN_USER_ID + " INTEGER, " +
                TableDoctor.COLUMN_DEPARTMENT_ID + " INTEGER, " +
                TableDoctor.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                TableDoctor.COLUMN_SPECIALIZATION + " TEXT, " +
                TableDoctor.COLUMN_LICENSE_NUMBER + " TEXT, " +
                TableDoctor.COLUMN_PHONE + " TEXT, " +
                TableDoctor.COLUMN_EMAIL + " TEXT, " +
                TableDoctor.COLUMN_ADDRESS + " TEXT, " +
                TableDoctor.COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                TableDoctor.COLUMN_CREATED_AT + " TEXT, " +
                TableDoctor.COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TableDoctor.COLUMN_USER_ID + ") REFERENCES " + TableUser.TABLE_NAME + "(" + TableUser.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TableDoctor.COLUMN_DEPARTMENT_ID + ") REFERENCES " + TableDepartment.TABLE_NAME + "(" + TableDepartment.COLUMN_ID + "))");

        // Patient table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TablePatient.TABLE_NAME + " (" +
                TablePatient.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TablePatient.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                TablePatient.COLUMN_DATE_OF_BIRTH + " TEXT, " +
                TablePatient.COLUMN_GENDER + " TEXT, " +
                TablePatient.COLUMN_PHONE + " TEXT, " +
                TablePatient.COLUMN_EMAIL + " TEXT, " +
                TablePatient.COLUMN_ADDRESS + " TEXT, " +
                TablePatient.COLUMN_EMERGENCY_CONTACT + " TEXT, " +
                TablePatient.COLUMN_EMERGENCY_PHONE + " TEXT, " +
                TablePatient.COLUMN_BLOOD_TYPE + " TEXT, " +
                TablePatient.COLUMN_ALLERGIES + " TEXT, " +
                TablePatient.COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                TablePatient.COLUMN_CREATED_AT + " TEXT, " +
                TablePatient.COLUMN_UPDATED_AT + " TEXT)");

        // Appointment table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableAppointment.TABLE_NAME + " (" +
                TableAppointment.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableAppointment.COLUMN_PATIENT_ID + " INTEGER NOT NULL, " +
                TableAppointment.COLUMN_DOCTOR_ID + " INTEGER NOT NULL, " +
                TableAppointment.COLUMN_DEPARTMENT_ID + " INTEGER, " +
                TableAppointment.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
                TableAppointment.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
                TableAppointment.COLUMN_REASON + " TEXT, " +
                TableAppointment.COLUMN_STATUS + " TEXT DEFAULT 'Scheduled', " +
                TableAppointment.COLUMN_NOTES + " TEXT, " +
                TableAppointment.COLUMN_CREATED_AT + " TEXT, " +
                TableAppointment.COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TableAppointment.COLUMN_PATIENT_ID + ") REFERENCES " + TablePatient.TABLE_NAME + "(" + TablePatient.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TableAppointment.COLUMN_DOCTOR_ID + ") REFERENCES " + TableDoctor.TABLE_NAME + "(" + TableDoctor.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TableAppointment.COLUMN_DEPARTMENT_ID + ") REFERENCES " + TableDepartment.TABLE_NAME + "(" + TableDepartment.COLUMN_ID + "))");

        // MedicalRecord table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableMedicalRecord.TABLE_NAME + " (" +
                TableMedicalRecord.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableMedicalRecord.COLUMN_PATIENT_ID + " INTEGER NOT NULL, " +
                TableMedicalRecord.COLUMN_DOCTOR_ID + " INTEGER NOT NULL, " +
                TableMedicalRecord.COLUMN_VISIT_DATE + " TEXT NOT NULL, " +
                TableMedicalRecord.COLUMN_DIAGNOSIS + " TEXT, " +
                TableMedicalRecord.COLUMN_COMPLAINTS + " TEXT, " +
                TableMedicalRecord.COLUMN_TREATMENT + " TEXT, " +
                TableMedicalRecord.COLUMN_VITAL_SIGNS + " TEXT, " +
                TableMedicalRecord.COLUMN_NOTES + " TEXT, " +
                TableMedicalRecord.COLUMN_CREATED_AT + " TEXT, " +
                TableMedicalRecord.COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TableMedicalRecord.COLUMN_PATIENT_ID + ") REFERENCES " + TablePatient.TABLE_NAME + "(" + TablePatient.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TableMedicalRecord.COLUMN_DOCTOR_ID + ") REFERENCES " + TableDoctor.TABLE_NAME + "(" + TableDoctor.COLUMN_ID + "))");

        // Medication table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableMedication.TABLE_NAME + " (" +
                TableMedication.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableMedication.COLUMN_NAME + " TEXT NOT NULL, " +
                TableMedication.COLUMN_GENERIC_NAME + " TEXT, " +
                TableMedication.COLUMN_DOSAGE + " TEXT, " +
                TableMedication.COLUMN_FORM + " TEXT, " +
                TableMedication.COLUMN_MANUFACTURER + " TEXT, " +
                TableMedication.COLUMN_SIDE_EFFECTS + " TEXT, " +
                TableMedication.COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                TableMedication.COLUMN_CREATED_AT + " TEXT, " +
                TableMedication.COLUMN_UPDATED_AT + " TEXT)");

        // Prescription table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TablePrescription.TABLE_NAME + " (" +
                TablePrescription.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TablePrescription.COLUMN_MEDICAL_RECORD_ID + " INTEGER NOT NULL, " +
                TablePrescription.COLUMN_PATIENT_ID + " INTEGER NOT NULL, " +
                TablePrescription.COLUMN_DOCTOR_ID + " INTEGER NOT NULL, " +
                TablePrescription.COLUMN_MEDICATION_ID + " INTEGER NOT NULL, " +
                TablePrescription.COLUMN_DOSAGE + " TEXT NOT NULL, " +
                TablePrescription.COLUMN_DURATION + " TEXT, " +
                TablePrescription.COLUMN_FREQUENCY + " TEXT, " +
                TablePrescription.COLUMN_INSTRUCTIONS + " TEXT, " +
                TablePrescription.COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                TablePrescription.COLUMN_CREATED_AT + " TEXT, " +
                TablePrescription.COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TablePrescription.COLUMN_MEDICAL_RECORD_ID + ") REFERENCES " + TableMedicalRecord.TABLE_NAME + "(" + TableMedicalRecord.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TablePrescription.COLUMN_PATIENT_ID + ") REFERENCES " + TablePatient.TABLE_NAME + "(" + TablePatient.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TablePrescription.COLUMN_DOCTOR_ID + ") REFERENCES " + TableDoctor.TABLE_NAME + "(" + TableDoctor.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TablePrescription.COLUMN_MEDICATION_ID + ") REFERENCES " + TableMedication.TABLE_NAME + "(" + TableMedication.COLUMN_ID + "))");

        // Invoice table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableInvoice.TABLE_NAME + " (" +
                TableInvoice.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableInvoice.COLUMN_PATIENT_ID + " INTEGER NOT NULL, " +
                TableInvoice.COLUMN_INVOICE_NUMBER + " TEXT UNIQUE NOT NULL, " +
                TableInvoice.COLUMN_INVOICE_DATE + " TEXT NOT NULL, " +
                TableInvoice.COLUMN_DUE_DATE + " TEXT, " +
                TableInvoice.COLUMN_SUBTOTAL + " REAL DEFAULT 0, " +
                TableInvoice.COLUMN_TAX + " REAL DEFAULT 0, " +
                TableInvoice.COLUMN_TOTAL + " REAL DEFAULT 0, " +
                TableInvoice.COLUMN_STATUS + " TEXT DEFAULT 'Pending', " +
                TableInvoice.COLUMN_NOTES + " TEXT, " +
                TableInvoice.COLUMN_CREATED_AT + " TEXT, " +
                TableInvoice.COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TableInvoice.COLUMN_PATIENT_ID + ") REFERENCES " + TablePatient.TABLE_NAME + "(" + TablePatient.COLUMN_ID + "))");

        // Payment table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TablePayment.TABLE_NAME + " (" +
                TablePayment.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TablePayment.COLUMN_INVOICE_ID + " INTEGER NOT NULL, " +
                TablePayment.COLUMN_PATIENT_ID + " INTEGER NOT NULL, " +
                TablePayment.COLUMN_AMOUNT + " REAL NOT NULL, " +
                TablePayment.COLUMN_PAYMENT_DATE + " TEXT NOT NULL, " +
                TablePayment.COLUMN_PAYMENT_METHOD + " TEXT, " +
                TablePayment.COLUMN_TRANSACTION_ID + " TEXT, " +
                TablePayment.COLUMN_STATUS + " TEXT DEFAULT 'Completed', " +
                TablePayment.COLUMN_NOTES + " TEXT, " +
                TablePayment.COLUMN_CREATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TablePayment.COLUMN_INVOICE_ID + ") REFERENCES " + TableInvoice.TABLE_NAME + "(" + TableInvoice.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TablePayment.COLUMN_PATIENT_ID + ") REFERENCES " + TablePatient.TABLE_NAME + "(" + TablePatient.COLUMN_ID + "))");

        // Report table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableReport.TABLE_NAME + " (" +
                TableReport.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableReport.COLUMN_REPORT_TYPE + " TEXT NOT NULL, " +
                TableReport.COLUMN_TITLE + " TEXT NOT NULL, " +
                TableReport.COLUMN_PATIENT_ID + " INTEGER, " +
                TableReport.COLUMN_DOCTOR_ID + " INTEGER, " +
                TableReport.COLUMN_GENERATED_DATE + " TEXT NOT NULL, " +
                TableReport.COLUMN_DATA + " TEXT, " +
                TableReport.COLUMN_FILE_PATH + " TEXT, " +
                TableReport.COLUMN_GENERATED_BY + " TEXT, " +
                TableReport.COLUMN_CREATED_AT + " TEXT, " +
                "FOREIGN KEY(" + TableReport.COLUMN_PATIENT_ID + ") REFERENCES " + TablePatient.TABLE_NAME + "(" + TablePatient.COLUMN_ID + "), " +
                "FOREIGN KEY(" + TableReport.COLUMN_DOCTOR_ID + ") REFERENCES " + TableDoctor.TABLE_NAME + "(" + TableDoctor.COLUMN_ID + "))");
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TablePayment.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableInvoice.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePrescription.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableMedication.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableMedicalRecord.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableAppointment.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableDoctor.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePatient.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableDepartment.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableReport.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableUser.TABLE_NAME);
    }

    // ==================== Table Constants ====================

    public static class TableUser {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_IS_ACTIVE = "is_active";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TableDepartment {
        public static final String TABLE_NAME = "departments";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_IS_ACTIVE = "is_active";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TableDoctor {
        public static final String TABLE_NAME = "doctors";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_DEPARTMENT_ID = "department_id";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_SPECIALIZATION = "specialization";
        public static final String COLUMN_LICENSE_NUMBER = "license_number";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_IS_ACTIVE = "is_active";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TablePatient {
        public static final String TABLE_NAME = "patients";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_EMERGENCY_CONTACT = "emergency_contact";
        public static final String COLUMN_EMERGENCY_PHONE = "emergency_phone";
        public static final String COLUMN_BLOOD_TYPE = "blood_type";
        public static final String COLUMN_ALLERGIES = "allergies";
        public static final String COLUMN_IS_ACTIVE = "is_active";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TableAppointment {
        public static final String TABLE_NAME = "appointments";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_DEPARTMENT_ID = "department_id";
        public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";
        public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";
        public static final String COLUMN_REASON = "reason";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TableMedicalRecord {
        public static final String TABLE_NAME = "medical_records";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_VISIT_DATE = "visit_date";
        public static final String COLUMN_DIAGNOSIS = "diagnosis";
        public static final String COLUMN_COMPLAINTS = "complaints";
        public static final String COLUMN_TREATMENT = "treatment";
        public static final String COLUMN_VITAL_SIGNS = "vital_signs";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TableMedication {
        public static final String TABLE_NAME = "medications";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GENERIC_NAME = "generic_name";
        public static final String COLUMN_DOSAGE = "dosage";
        public static final String COLUMN_FORM = "form";
        public static final String COLUMN_MANUFACTURER = "manufacturer";
        public static final String COLUMN_SIDE_EFFECTS = "side_effects";
        public static final String COLUMN_IS_ACTIVE = "is_active";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TablePrescription {
        public static final String TABLE_NAME = "prescriptions";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MEDICAL_RECORD_ID = "medical_record_id";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_MEDICATION_ID = "medication_id";
        public static final String COLUMN_DOSAGE = "dosage";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_FREQUENCY = "frequency";
        public static final String COLUMN_INSTRUCTIONS = "instructions";
        public static final String COLUMN_IS_ACTIVE = "is_active";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TableInvoice {
        public static final String TABLE_NAME = "invoices";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_INVOICE_NUMBER = "invoice_number";
        public static final String COLUMN_INVOICE_DATE = "invoice_date";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_SUBTOTAL = "subtotal";
        public static final String COLUMN_TAX = "tax";
        public static final String COLUMN_TOTAL = "total";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static class TablePayment {
        public static final String TABLE_NAME = "payments";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_INVOICE_ID = "invoice_id";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_PAYMENT_DATE = "payment_date";
        public static final String COLUMN_PAYMENT_METHOD = "payment_method";
        public static final String COLUMN_TRANSACTION_ID = "transaction_id";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_CREATED_AT = "created_at";
    }

    public static class TableReport {
        public static final String TABLE_NAME = "reports";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_REPORT_TYPE = "report_type";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_GENERATED_DATE = "generated_date";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_FILE_PATH = "file_path";
        public static final String COLUMN_GENERATED_BY = "generated_by";
        public static final String COLUMN_CREATED_AT = "created_at";
    }

    // ==================== User CRUD ====================

    public long insertUser(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableUser.TABLE_NAME, null, values);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableUser.TABLE_NAME, null, null, null, null, null, TableUser.COLUMN_FULL_NAME + " ASC");
    }

    public Cursor getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableUser.TABLE_NAME, null, TableUser.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableUser.TABLE_NAME, null, TableUser.COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }

    public int updateUser(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableUser.TABLE_NAME, values, TableUser.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableUser.TABLE_NAME, TableUser.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Department CRUD ====================

    public long insertDepartment(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableDepartment.TABLE_NAME, null, values);
    }

    public Cursor getAllDepartments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableDepartment.TABLE_NAME, null, null, null, null, null, TableDepartment.COLUMN_NAME + " ASC");
    }

    public Cursor getDepartmentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableDepartment.TABLE_NAME, null, TableDepartment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public int updateDepartment(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableDepartment.TABLE_NAME, values, TableDepartment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteDepartment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableDepartment.TABLE_NAME, TableDepartment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Doctor CRUD ====================

    public long insertDoctor(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableDoctor.TABLE_NAME, null, values);
    }

    public Cursor getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableDoctor.TABLE_NAME, null, null, null, null, null, TableDoctor.COLUMN_FULL_NAME + " ASC");
    }

    public Cursor getDoctorById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableDoctor.TABLE_NAME, null, TableDoctor.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getDoctorsByDepartment(int departmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableDoctor.TABLE_NAME, null, TableDoctor.COLUMN_DEPARTMENT_ID + " = ?", new String[]{String.valueOf(departmentId)}, null, null, TableDoctor.COLUMN_FULL_NAME + " ASC");
    }

    public int updateDoctor(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableDoctor.TABLE_NAME, values, TableDoctor.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableDoctor.TABLE_NAME, TableDoctor.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Patient CRUD ====================

    public long insertPatient(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TablePatient.TABLE_NAME, null, values);
    }

    public Cursor getAllPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePatient.TABLE_NAME, null, null, null, null, null, TablePatient.COLUMN_FULL_NAME + " ASC");
    }

    public Cursor getPatientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePatient.TABLE_NAME, null, TablePatient.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor searchPatients(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "%" + query + "%";
        return db.query(TablePatient.TABLE_NAME, null,
                TablePatient.COLUMN_FULL_NAME + " LIKE ? OR " + TablePatient.COLUMN_PHONE + " LIKE ? OR " + TablePatient.COLUMN_EMAIL + " LIKE ?",
                new String[]{searchQuery, searchQuery, searchQuery}, null, null, TablePatient.COLUMN_FULL_NAME + " ASC");
    }

    public int updatePatient(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TablePatient.TABLE_NAME, values, TablePatient.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deletePatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TablePatient.TABLE_NAME, TablePatient.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Appointment CRUD ====================

    public long insertAppointment(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableAppointment.TABLE_NAME, null, values);
    }

    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableAppointment.TABLE_NAME, null, null, null, null, null, TableAppointment.COLUMN_APPOINTMENT_DATE + " DESC, " + TableAppointment.COLUMN_APPOINTMENT_TIME + " ASC");
    }

    public Cursor getAppointmentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableAppointment.TABLE_NAME, null, TableAppointment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getAppointmentsByPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableAppointment.TABLE_NAME, null, TableAppointment.COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)}, null, null, TableAppointment.COLUMN_APPOINTMENT_DATE + " DESC");
    }

    public Cursor getAppointmentsByDoctor(int doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableAppointment.TABLE_NAME, null, TableAppointment.COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(doctorId)}, null, null, TableAppointment.COLUMN_APPOINTMENT_DATE + " DESC");
    }

    public Cursor getAppointmentsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableAppointment.TABLE_NAME, null, TableAppointment.COLUMN_APPOINTMENT_DATE + " = ?", new String[]{date}, null, null, TableAppointment.COLUMN_APPOINTMENT_TIME + " ASC");
    }

    public Cursor getAppointmentsByStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableAppointment.TABLE_NAME, null, TableAppointment.COLUMN_STATUS + " = ?", new String[]{status}, null, null, TableAppointment.COLUMN_APPOINTMENT_DATE + " DESC");
    }

    public int updateAppointment(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableAppointment.TABLE_NAME, values, TableAppointment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableAppointment.TABLE_NAME, TableAppointment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== MedicalRecord CRUD ====================

    public long insertMedicalRecord(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableMedicalRecord.TABLE_NAME, null, values);
    }

    public Cursor getAllMedicalRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableMedicalRecord.TABLE_NAME, null, null, null, null, null, TableMedicalRecord.COLUMN_VISIT_DATE + " DESC");
    }

    public Cursor getMedicalRecordById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableMedicalRecord.TABLE_NAME, null, TableMedicalRecord.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getMedicalRecordsByPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableMedicalRecord.TABLE_NAME, null, TableMedicalRecord.COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)}, null, null, TableMedicalRecord.COLUMN_VISIT_DATE + " DESC");
    }

    public Cursor getMedicalRecordsByDoctor(int doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableMedicalRecord.TABLE_NAME, null, TableMedicalRecord.COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(doctorId)}, null, null, TableMedicalRecord.COLUMN_VISIT_DATE + " DESC");
    }

    public int updateMedicalRecord(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableMedicalRecord.TABLE_NAME, values, TableMedicalRecord.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteMedicalRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableMedicalRecord.TABLE_NAME, TableMedicalRecord.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Medication CRUD ====================

    public long insertMedication(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableMedication.TABLE_NAME, null, values);
    }

    public Cursor getAllMedications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableMedication.TABLE_NAME, null, null, null, null, null, TableMedication.COLUMN_NAME + " ASC");
    }

    public Cursor getMedicationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableMedication.TABLE_NAME, null, TableMedication.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor searchMedications(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "%" + query + "%";
        return db.query(TableMedication.TABLE_NAME, null,
                TableMedication.COLUMN_NAME + " LIKE ? OR " + TableMedication.COLUMN_GENERIC_NAME + " LIKE ?",
                new String[]{searchQuery, searchQuery}, null, null, TableMedication.COLUMN_NAME + " ASC");
    }

    public int updateMedication(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableMedication.TABLE_NAME, values, TableMedication.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteMedication(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableMedication.TABLE_NAME, TableMedication.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Prescription CRUD ====================

    public long insertPrescription(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TablePrescription.TABLE_NAME, null, values);
    }

    public Cursor getAllPrescriptions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePrescription.TABLE_NAME, null, null, null, null, null, TablePrescription.COLUMN_CREATED_AT + " DESC");
    }

    public Cursor getPrescriptionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePrescription.TABLE_NAME, null, TablePrescription.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getPrescriptionsByPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePrescription.TABLE_NAME, null, TablePrescription.COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)}, null, null, TablePrescription.COLUMN_CREATED_AT + " DESC");
    }

    public Cursor getPrescriptionsByDoctor(int doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePrescription.TABLE_NAME, null, TablePrescription.COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(doctorId)}, null, null, TablePrescription.COLUMN_CREATED_AT + " DESC");
    }

    public Cursor getPrescriptionsByMedicalRecord(int medicalRecordId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePrescription.TABLE_NAME, null, TablePrescription.COLUMN_MEDICAL_RECORD_ID + " = ?", new String[]{String.valueOf(medicalRecordId)}, null, null, null);
    }

    public int updatePrescription(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TablePrescription.TABLE_NAME, values, TablePrescription.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deletePrescription(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TablePrescription.TABLE_NAME, TablePrescription.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Invoice CRUD ====================

    public long insertInvoice(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableInvoice.TABLE_NAME, null, values);
    }

    public Cursor getAllInvoices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableInvoice.TABLE_NAME, null, null, null, null, null, TableInvoice.COLUMN_INVOICE_DATE + " DESC");
    }

    public Cursor getInvoiceById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableInvoice.TABLE_NAME, null, TableInvoice.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getInvoiceByNumber(String invoiceNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableInvoice.TABLE_NAME, null, TableInvoice.COLUMN_INVOICE_NUMBER + " = ?", new String[]{invoiceNumber}, null, null, null);
    }

    public Cursor getInvoicesByPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableInvoice.TABLE_NAME, null, TableInvoice.COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)}, null, null, TableInvoice.COLUMN_INVOICE_DATE + " DESC");
    }

    public Cursor getInvoicesByStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableInvoice.TABLE_NAME, null, TableInvoice.COLUMN_STATUS + " = ?", new String[]{status}, null, null, TableInvoice.COLUMN_INVOICE_DATE + " DESC");
    }

    public int updateInvoice(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableInvoice.TABLE_NAME, values, TableInvoice.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteInvoice(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableInvoice.TABLE_NAME, TableInvoice.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Payment CRUD ====================

    public long insertPayment(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TablePayment.TABLE_NAME, null, values);
    }

    public Cursor getAllPayments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePayment.TABLE_NAME, null, null, null, null, null, TablePayment.COLUMN_PAYMENT_DATE + " DESC");
    }

    public Cursor getPaymentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePayment.TABLE_NAME, null, TablePayment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getPaymentsByInvoice(int invoiceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePayment.TABLE_NAME, null, TablePayment.COLUMN_INVOICE_ID + " = ?", new String[]{String.valueOf(invoiceId)}, null, null, TablePayment.COLUMN_PAYMENT_DATE + " DESC");
    }

    public Cursor getPaymentsByPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TablePayment.TABLE_NAME, null, TablePayment.COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)}, null, null, TablePayment.COLUMN_PAYMENT_DATE + " DESC");
    }

    public int updatePayment(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TablePayment.TABLE_NAME, values, TablePayment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deletePayment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TablePayment.TABLE_NAME, TablePayment.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Report CRUD ====================

    public long insertReport(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableReport.TABLE_NAME, null, values);
    }

    public Cursor getAllReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableReport.TABLE_NAME, null, null, null, null, null, TableReport.COLUMN_GENERATED_DATE + " DESC");
    }

    public Cursor getReportById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableReport.TABLE_NAME, null, TableReport.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getReportsByType(String reportType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableReport.TABLE_NAME, null, TableReport.COLUMN_REPORT_TYPE + " = ?", new String[]{reportType}, null, null, TableReport.COLUMN_GENERATED_DATE + " DESC");
    }

    public Cursor getReportsByPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableReport.TABLE_NAME, null, TableReport.COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)}, null, null, TableReport.COLUMN_GENERATED_DATE + " DESC");
    }

    public Cursor getReportsByDoctor(int doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TableReport.TABLE_NAME, null, TableReport.COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(doctorId)}, null, null, TableReport.COLUMN_GENERATED_DATE + " DESC");
    }

    public int updateReport(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TableReport.TABLE_NAME, values, TableReport.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteReport(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableReport.TABLE_NAME, TableReport.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==================== Utility Methods ====================

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableUser.TABLE_NAME, null,
                TableUser.COLUMN_USERNAME + " = ? AND " + TableUser.COLUMN_PASSWORD + " = ? AND " + TableUser.COLUMN_IS_ACTIVE + " = 1",
                new String[]{username, password}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public String getUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableUser.TABLE_NAME, new String[]{TableUser.COLUMN_ROLE},
                TableUser.COLUMN_USERNAME + " = ? AND " + TableUser.COLUMN_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.COLUMN_ROLE));
        }
        cursor.close();
        return role;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableUser.TABLE_NAME, new String[]{TableUser.COLUMN_ID},
                TableUser.COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(TableUser.COLUMN_ID));
        }
        cursor.close();
        return userId;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableUser.TABLE_NAME, new String[]{TableUser.COLUMN_ID},
                TableUser.COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}
