package com.example.a4200finalproj.UI.Activities.MedicalRecord;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicalRecordFormActivity extends AppCompatActivity {

    private EditText etPatientId, etVisitDate, etDiagnosis, etComplaints, etTreatment, etVitalSigns, etNotes;
    private Spinner spinnerDoctor;
    private Button btnSave;
    private DatabaseHelper db;
    private int recordId = -1;
    private List<Integer> doctorIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etPatientId  = findViewById(R.id.etPatientId);
        etVisitDate  = findViewById(R.id.etVisitDate);
        etDiagnosis  = findViewById(R.id.etDiagnosis);
        etComplaints = findViewById(R.id.etComplaints);
        etTreatment  = findViewById(R.id.etTreatment);
        etVitalSigns = findViewById(R.id.etVitalSigns);
        etNotes      = findViewById(R.id.etNotes);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        btnSave      = findViewById(R.id.btnSave);

        recordId = getIntent().getIntExtra("record_id", -1);
        boolean isEdit = recordId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Medical Record" : "New Medical Record");
        }

        populateDoctorSpinner();
        setupDatePicker();

        if (isEdit) loadRecordData();

        btnSave.setOnClickListener(v -> saveRecord());
    }

    private void populateDoctorSpinner() {
        List<String> doctorNames = new ArrayList<>();
        doctorNames.add("Select Doctor");
        doctorIds.add(-1);
        Cursor cursor = db.getAllDoctors();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                doctorIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_ID)));
                doctorNames.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
            }
            cursor.close();
        }
        spinnerDoctor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorNames));
    }

    private void setupDatePicker() {
        etVisitDate.setFocusable(false);
        etVisitDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                etVisitDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day));
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadRecordData() {
        Cursor cursor = db.getMedicalRecordById(recordId);
        if (cursor != null && cursor.moveToFirst()) {
            etPatientId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_PATIENT_ID))));
            etVisitDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_VISIT_DATE)));
            etDiagnosis.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_DIAGNOSIS)));
            etComplaints.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_COMPLAINTS)));
            etTreatment.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_TREATMENT)));
            etVitalSigns.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_VITAL_SIGNS)));
            etNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_NOTES)));

            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_DOCTOR_ID));
            int doctorPos = doctorIds.indexOf(doctorId);
            if (doctorPos >= 0) spinnerDoctor.setSelection(doctorPos);
            cursor.close();
        }
    }

    private void saveRecord() {
        String patientIdStr = etPatientId.getText().toString().trim();
        String visitDate    = etVisitDate.getText().toString().trim();
        String diagnosis    = etDiagnosis.getText().toString().trim();
        String complaints   = etComplaints.getText().toString().trim();
        String treatment    = etTreatment.getText().toString().trim();
        String vitalSigns   = etVitalSigns.getText().toString().trim();
        String notes        = etNotes.getText().toString().trim();
        int selectedDoctorPos = spinnerDoctor.getSelectedItemPosition();

        if (TextUtils.isEmpty(patientIdStr)) { etPatientId.setError("Required"); return; }
        if (TextUtils.isEmpty(visitDate)) { etVisitDate.setError("Required"); return; }
        if (TextUtils.isEmpty(diagnosis)) { etDiagnosis.setError("Required"); return; }
        if (selectedDoctorPos == 0) { Toast.makeText(this, "Please select a doctor", Toast.LENGTH_SHORT).show(); return; }

        int patientId;
        try { patientId = Integer.parseInt(patientIdStr); }
        catch (NumberFormatException e) { etPatientId.setError("Enter a valid Patient ID"); return; }

        int doctorId = doctorIds.get(selectedDoctorPos);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_PATIENT_ID, patientId);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_DOCTOR_ID, doctorId);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_VISIT_DATE, visitDate);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_DIAGNOSIS, diagnosis);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_COMPLAINTS, complaints);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_TREATMENT, treatment);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_VITAL_SIGNS, vitalSigns);
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_NOTES, notes);

        if (recordId == -1) {
            long result = db.insertMedicalRecord(cv);
            if (result > 0) { Toast.makeText(this, "Record created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create record", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updateMedicalRecord(recordId, cv);
            if (result > 0) { Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update record", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}