package com.example.a4200finalproj.UI.Activities.Patient;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.R;

public class PatientFormActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText etPatientFullName, etPatientDob, etPatientGender, etPatientPhone, etPatientEmail,
            etPatientAddress, etEmergencyContact, etEmergencyPhone, etBloodType, etAllergies;
    private Button btnSavePatient, btnDeletePatient;

    private int patientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        databaseHelper = new DatabaseHelper(this);

        etPatientFullName = findViewById(R.id.etPatientFullName);
        etPatientDob = findViewById(R.id.etPatientDob);
        etPatientGender = findViewById(R.id.etPatientGender);
        etPatientPhone = findViewById(R.id.etPatientPhone);
        etPatientEmail = findViewById(R.id.etPatientEmail);
        etPatientAddress = findViewById(R.id.etPatientAddress);
        etEmergencyContact = findViewById(R.id.etEmergencyContact);
        etEmergencyPhone = findViewById(R.id.etEmergencyPhone);
        etBloodType = findViewById(R.id.etBloodType);
        etAllergies = findViewById(R.id.etAllergies);
        btnSavePatient = findViewById(R.id.btnSavePatient);
        btnDeletePatient = findViewById(R.id.btnDeletePatient);

        patientId = getIntent().getIntExtra("patient_id", -1);

        if (patientId != -1) {
            loadPatient();
        } else {
            btnDeletePatient.setVisibility(View.GONE);
        }

        btnSavePatient.setOnClickListener(v -> savePatient());
        btnDeletePatient.setOnClickListener(v -> deletePatient());
    }

    private void loadPatient() {
        Cursor cursor = databaseHelper.getPatientById(patientId);
        if (cursor != null && cursor.moveToFirst()) {
            etPatientFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
            etPatientDob.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_DATE_OF_BIRTH)));
            etPatientGender.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_GENDER)));
            etPatientPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_PHONE)));
            etPatientEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMAIL)));
            etPatientAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ADDRESS)));
            etEmergencyContact.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_CONTACT)));
            etEmergencyPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_PHONE)));
            etBloodType.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_BLOOD_TYPE)));
            etAllergies.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ALLERGIES)));
            cursor.close();
        }
    }

    private void savePatient() {
        String fullName = etPatientFullName.getText().toString().trim();
        String dob = etPatientDob.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etPatientFullName.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(dob)) {
            etPatientDob.setError("Required");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TablePatient.COLUMN_FULL_NAME, fullName);
        values.put(DatabaseHelper.TablePatient.COLUMN_DATE_OF_BIRTH, dob);
        values.put(DatabaseHelper.TablePatient.COLUMN_GENDER, etPatientGender.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_PHONE, etPatientPhone.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_EMAIL, etPatientEmail.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_ADDRESS, etPatientAddress.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_CONTACT, etEmergencyContact.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_PHONE, etEmergencyPhone.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_BLOOD_TYPE, etBloodType.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_ALLERGIES, etAllergies.getText().toString().trim());
        values.put(DatabaseHelper.TablePatient.COLUMN_IS_ACTIVE, 1);

        if (patientId == -1) {
            long result = databaseHelper.insertPatient(values);
            if (result != -1) {
                Toast.makeText(this, "Patient added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add patient", Toast.LENGTH_SHORT).show();
            }
        } else {
            int result = databaseHelper.updatePatient(patientId, values);
            if (result > 0) {
                Toast.makeText(this, "Patient updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update patient", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deletePatient() {
        if (patientId != -1) {
            int result = databaseHelper.deletePatient(patientId);
            if (result > 0) {
                Toast.makeText(this, "Patient deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete patient", Toast.LENGTH_SHORT).show();
            }
        }
    }
}