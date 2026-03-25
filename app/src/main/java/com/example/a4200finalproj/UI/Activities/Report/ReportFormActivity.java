package com.example.a4200finalproj.UI.Activities.Report;

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
import com.example.a4200finalproj.Helpers.SessionManager;
import com.example.a4200finalproj.R;

import java.util.Locale;

public class ReportFormActivity extends AppCompatActivity {

    private EditText etTitle, etGeneratedDate, etPatientId, etData;
    private Spinner spinnerType, spinnerDoctor;
    private Button btnSave;
    private DatabaseHelper db;
    private int reportId = -1;
    private java.util.List<Integer> doctorIds = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etTitle         = findViewById(R.id.etTitle);
        etGeneratedDate = findViewById(R.id.etGeneratedDate);
        etPatientId     = findViewById(R.id.etPatientId);
        etData          = findViewById(R.id.etData);
        spinnerType     = findViewById(R.id.spinnerType);
        spinnerDoctor   = findViewById(R.id.spinnerDoctor);
        btnSave         = findViewById(R.id.btnSave);

        String[] types = {"Patient", "Doctor", "Appointment", "Financial", "General"};
        spinnerType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types));

        reportId = getIntent().getIntExtra("report_id", -1);
        boolean isEdit = reportId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Report" : "New Report");
        }

        populateDoctorSpinner();
        setupDatePicker();
        if (isEdit) loadReportData();

        btnSave.setOnClickListener(v -> saveReport());
    }

    private void populateDoctorSpinner() {
        java.util.List<String> doctorNames = new java.util.ArrayList<>();
        doctorNames.add("None");
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
        etGeneratedDate.setFocusable(false);
        etGeneratedDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                etGeneratedDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day));
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadReportData() {
        Cursor cursor = db.getReportById(reportId);
        if (cursor != null && cursor.moveToFirst()) {
            etTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_TITLE)));
            etGeneratedDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_GENERATED_DATE)));
            etData.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_DATA)));

            int patientId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_PATIENT_ID));
            if (patientId > 0) etPatientId.setText(String.valueOf(patientId));

            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_REPORT_TYPE));
            String[] types = {"Patient", "Doctor", "Appointment", "Financial", "General"};
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(type)) { spinnerType.setSelection(i); break; }
            }

            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_DOCTOR_ID));
            int doctorPos = doctorIds.indexOf(doctorId);
            if (doctorPos >= 0) spinnerDoctor.setSelection(doctorPos);

            cursor.close();
        }
    }

    private void saveReport() {
        String title         = etTitle.getText().toString().trim();
        String generatedDate = etGeneratedDate.getText().toString().trim();
        String patientIdStr  = etPatientId.getText().toString().trim();
        String data          = etData.getText().toString().trim();
        String type          = spinnerType.getSelectedItem().toString();
        int doctorPos        = spinnerDoctor.getSelectedItemPosition();

        if (TextUtils.isEmpty(title)) { etTitle.setError("Title is required"); return; }
        if (TextUtils.isEmpty(generatedDate)) { etGeneratedDate.setError("Date is required"); return; }

        SessionManager session = new SessionManager(this);
        String generatedBy = session.getUsername();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableReport.COLUMN_TITLE, title);
        cv.put(DatabaseHelper.TableReport.COLUMN_REPORT_TYPE, type);
        cv.put(DatabaseHelper.TableReport.COLUMN_GENERATED_DATE, generatedDate);
        cv.put(DatabaseHelper.TableReport.COLUMN_GENERATED_BY, generatedBy);
        cv.put(DatabaseHelper.TableReport.COLUMN_DATA, data);

        if (!TextUtils.isEmpty(patientIdStr)) {
            try { cv.put(DatabaseHelper.TableReport.COLUMN_PATIENT_ID, Integer.parseInt(patientIdStr)); }
            catch (NumberFormatException e) { etPatientId.setError("Enter a valid Patient ID"); return; }
        }

        int doctorId = doctorIds.get(doctorPos);
        if (doctorId > 0) cv.put(DatabaseHelper.TableReport.COLUMN_DOCTOR_ID, doctorId);

        if (reportId == -1) {
            long result = db.insertReport(cv);
            if (result > 0) { Toast.makeText(this, "Report created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create report", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updateReport(reportId, cv);
            if (result > 0) { Toast.makeText(this, "Report updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update report", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}