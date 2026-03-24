package com.example.a4200finalproj.UI.Activities.Prescription;

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

public class PrescriptionFormActivity extends AppCompatActivity {

    private EditText etPatientId, etMedicalRecordId, etDosage, etDuration, etFrequency, etInstructions;
    private Spinner spinnerDoctor, spinnerMedication;
    private Button btnSave;
    private DatabaseHelper db;
    private int prescriptionId = -1;
    private List<Integer> doctorIds = new ArrayList<>();
    private List<Integer> medicationIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etPatientId       = findViewById(R.id.etPatientId);
        etMedicalRecordId = findViewById(R.id.etMedicalRecordId);
        etDosage          = findViewById(R.id.etDosage);
        etDuration        = findViewById(R.id.etDuration);
        etFrequency       = findViewById(R.id.etFrequency);
        etInstructions    = findViewById(R.id.etInstructions);
        spinnerDoctor     = findViewById(R.id.spinnerDoctor);
        spinnerMedication = findViewById(R.id.spinnerMedication);
        btnSave           = findViewById(R.id.btnSave);

        prescriptionId = getIntent().getIntExtra("prescription_id", -1);
        boolean isEdit = prescriptionId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Prescription" : "New Prescription");
        }

        populateDoctorSpinner();
        populateMedicationSpinner();
        if (isEdit) loadPrescriptionData();

        btnSave.setOnClickListener(v -> savePrescription());
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

    private void populateMedicationSpinner() {
        List<String> medicationNames = new ArrayList<>();
        medicationNames.add("Select Medication");
        medicationIds.add(-1);
        Cursor cursor = db.getAllMedications();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                medicationIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_ID)));
                medicationNames.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_NAME)));
            }
            cursor.close();
        }
        spinnerMedication.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicationNames));
    }

    private void loadPrescriptionData() {
        Cursor cursor = db.getPrescriptionById(prescriptionId);
        if (cursor != null && cursor.moveToFirst()) {
            etPatientId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_PATIENT_ID))));
            etMedicalRecordId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_MEDICAL_RECORD_ID))));
            etDosage.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DOSAGE)));
            etDuration.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DURATION)));
            etFrequency.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_FREQUENCY)));
            etInstructions.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_INSTRUCTIONS)));

            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DOCTOR_ID));
            int doctorPos = doctorIds.indexOf(doctorId);
            if (doctorPos >= 0) spinnerDoctor.setSelection(doctorPos);

            int medicationId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_MEDICATION_ID));
            int medPos = medicationIds.indexOf(medicationId);
            if (medPos >= 0) spinnerMedication.setSelection(medPos);

            cursor.close();
        }
    }

    private void savePrescription() {
        String patientIdStr       = etPatientId.getText().toString().trim();
        String medicalRecordIdStr = etMedicalRecordId.getText().toString().trim();
        String dosage             = etDosage.getText().toString().trim();
        String duration           = etDuration.getText().toString().trim();
        String frequency          = etFrequency.getText().toString().trim();
        String instructions       = etInstructions.getText().toString().trim();
        int doctorPos             = spinnerDoctor.getSelectedItemPosition();
        int medPos                = spinnerMedication.getSelectedItemPosition();

        if (TextUtils.isEmpty(patientIdStr)) { etPatientId.setError("Required"); return; }
        if (TextUtils.isEmpty(dosage)) { etDosage.setError("Required"); return; }
        if (doctorPos == 0) { Toast.makeText(this, "Please select a doctor", Toast.LENGTH_SHORT).show(); return; }
        if (medPos == 0) { Toast.makeText(this, "Please select a medication", Toast.LENGTH_SHORT).show(); return; }

        int patientId;
        try { patientId = Integer.parseInt(patientIdStr); }
        catch (NumberFormatException e) { etPatientId.setError("Enter a valid Patient ID"); return; }

        int medicalRecordId = 0;
        if (!TextUtils.isEmpty(medicalRecordIdStr)) {
            try { medicalRecordId = Integer.parseInt(medicalRecordIdStr); }
            catch (NumberFormatException e) { etMedicalRecordId.setError("Enter a valid Record ID"); return; }
        }

        int doctorId     = doctorIds.get(doctorPos);
        int medicationId = medicationIds.get(medPos);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePrescription.COLUMN_PATIENT_ID, patientId);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DOCTOR_ID, doctorId);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_MEDICATION_ID, medicationId);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_MEDICAL_RECORD_ID, medicalRecordId);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DOSAGE, dosage);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DURATION, duration);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_FREQUENCY, frequency);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_INSTRUCTIONS, instructions);
        cv.put(DatabaseHelper.TablePrescription.COLUMN_IS_ACTIVE, 1);

        if (prescriptionId == -1) {
            long result = db.insertPrescription(cv);
            if (result > 0) { Toast.makeText(this, "Prescription created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create prescription", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updatePrescription(prescriptionId, cv);
            if (result > 0) { Toast.makeText(this, "Prescription updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update prescription", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}