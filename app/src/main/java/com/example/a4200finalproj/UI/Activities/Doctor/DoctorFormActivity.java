package com.example.a4200finalproj.UI.Activities.Doctor;

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

public class DoctorFormActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText etDoctorFullName, etDoctorSpecialization, etDoctorLicense,
            etDoctorPhone, etDoctorEmail, etDoctorAddress, etDoctorDepartmentId;
    private Button btnSaveDoctor, btnDeleteDoctor;

    private int doctorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_form);

        databaseHelper = DatabaseHelper.getInstance(this);

        etDoctorFullName = findViewById(R.id.etDoctorFullName);
        etDoctorSpecialization = findViewById(R.id.etDoctorSpecialization);
        etDoctorLicense = findViewById(R.id.etDoctorLicense);
        etDoctorPhone = findViewById(R.id.etDoctorPhone);
        etDoctorEmail = findViewById(R.id.etDoctorEmail);
        etDoctorAddress = findViewById(R.id.etDoctorAddress);
        etDoctorDepartmentId = findViewById(R.id.etDoctorDepartmentId);
        btnSaveDoctor = findViewById(R.id.btnSaveDoctor);
        btnDeleteDoctor = findViewById(R.id.btnDeleteDoctor);

        doctorId = getIntent().getIntExtra("doctor_id", -1);

        if (doctorId != -1) {
            loadDoctor();
        } else {
            btnDeleteDoctor.setVisibility(View.GONE);
        }

        btnSaveDoctor.setOnClickListener(v -> saveDoctor());
        btnDeleteDoctor.setOnClickListener(v -> deleteDoctor());
    }

    private void loadDoctor() {
        Cursor cursor = databaseHelper.getDoctorById(doctorId);
        if (cursor != null && cursor.moveToFirst()) {
            etDoctorFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
            etDoctorSpecialization.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_SPECIALIZATION)));
            etDoctorLicense.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_LICENSE_NUMBER)));
            etDoctorPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_PHONE)));
            etDoctorEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_EMAIL)));
            etDoctorAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_ADDRESS)));
            etDoctorDepartmentId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_DEPARTMENT_ID))));
            cursor.close();
        }
    }

    private void saveDoctor() {
        String fullName = etDoctorFullName.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etDoctorFullName.setError("Required");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME, fullName);
        values.put(DatabaseHelper.TableDoctor.COLUMN_SPECIALIZATION, etDoctorSpecialization.getText().toString().trim());
        values.put(DatabaseHelper.TableDoctor.COLUMN_LICENSE_NUMBER, etDoctorLicense.getText().toString().trim());
        values.put(DatabaseHelper.TableDoctor.COLUMN_PHONE, etDoctorPhone.getText().toString().trim());
        values.put(DatabaseHelper.TableDoctor.COLUMN_EMAIL, etDoctorEmail.getText().toString().trim());
        values.put(DatabaseHelper.TableDoctor.COLUMN_ADDRESS, etDoctorAddress.getText().toString().trim());

        String departmentIdText = etDoctorDepartmentId.getText().toString().trim();
        int departmentId = departmentIdText.isEmpty() ? 1 : Integer.parseInt(departmentIdText);
        values.put(DatabaseHelper.TableDoctor.COLUMN_DEPARTMENT_ID, departmentId);
        values.put(DatabaseHelper.TableDoctor.COLUMN_IS_ACTIVE, 1);

        if (doctorId == -1) {
            long result = databaseHelper.insertDoctor(values);
            if (result != -1) {
                Toast.makeText(this, "Doctor added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
            }
        } else {
            int result = databaseHelper.updateDoctor(doctorId, values);
            if (result > 0) {
                Toast.makeText(this, "Doctor updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update doctor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteDoctor() {
        if (doctorId != -1) {
            int result = databaseHelper.deleteDoctor(doctorId);
            if (result > 0) {
                Toast.makeText(this, "Doctor deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete doctor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}