package com.example.a4200finalproj.UI.Activities.Appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class AppointmentFormActivity extends AppCompatActivity {

    private EditText etPatientId, etReason, etNotes, etDate, etTime;
    private Spinner spinnerDoctor, spinnerDepartment, spinnerStatus;
    private Button btnSave;
    private DatabaseHelper db;
    private int appointmentId = -1;
    private List<Integer> doctorIds = new ArrayList<>();
    private List<Integer> departmentIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etPatientId       = findViewById(R.id.etPatientId);
        etReason          = findViewById(R.id.etReason);
        etNotes           = findViewById(R.id.etNotes);
        etDate            = findViewById(R.id.etDate);
        etTime            = findViewById(R.id.etTime);
        spinnerDoctor     = findViewById(R.id.spinnerDoctor);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        spinnerStatus     = findViewById(R.id.spinnerStatus);
        btnSave           = findViewById(R.id.btnSave);

        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        boolean isEdit = appointmentId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Appointment" : "New Appointment");
        }

        populateDoctorSpinner();
        populateDepartmentSpinner();
        populateStatusSpinner();
        setupDatePicker();
        setupTimePicker();

        if (isEdit) loadAppointmentData();

        btnSave.setOnClickListener(v -> saveAppointment());
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

    private void populateDepartmentSpinner() {
        List<String> deptNames = new ArrayList<>();
        deptNames.add("Select Department");
        departmentIds.add(-1);
        Cursor cursor = db.getAllDepartments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                departmentIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_ID)));
                deptNames.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_NAME)));
            }
            cursor.close();
        }
        spinnerDepartment.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deptNames));
    }

    private void populateStatusSpinner() {
        String[] statuses = {"Scheduled", "Completed", "Cancelled"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses));
    }

    private void setupDatePicker() {
        etDate.setFocusable(false);
        etDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
                etDate.setText(date);
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupTimePicker() {
        etTime.setFocusable(false);
        etTime.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                etTime.setText(time);
            }, cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), true).show();
        });
    }

    private void loadAppointmentData() {
        Cursor cursor = db.getAppointmentById(appointmentId);
        if (cursor != null && cursor.moveToFirst()) {
            etPatientId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_PATIENT_ID))));
            etDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_DATE)));
            etTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_TIME)));
            etReason.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_REASON)));
            etNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_NOTES)));

            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_DOCTOR_ID));
            int deptId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_DEPARTMENT_ID));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_STATUS));

            int doctorPos = doctorIds.indexOf(doctorId);
            if (doctorPos >= 0) spinnerDoctor.setSelection(doctorPos);

            int deptPos = departmentIds.indexOf(deptId);
            if (deptPos >= 0) spinnerDepartment.setSelection(deptPos);

            String[] statuses = {"Scheduled", "Completed", "Cancelled"};
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(status)) { spinnerStatus.setSelection(i); break; }
            }
            cursor.close();
        }
    }

    private void saveAppointment() {
        String patientIdStr = etPatientId.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String reason = etReason.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        int selectedDoctorPos = spinnerDoctor.getSelectedItemPosition();
        String status = spinnerStatus.getSelectedItem().toString();

        if (TextUtils.isEmpty(patientIdStr)) { etPatientId.setError("Required"); return; }
        if (TextUtils.isEmpty(date)) { etDate.setError("Required"); return; }
        if (TextUtils.isEmpty(time)) { etTime.setError("Required"); return; }
        if (selectedDoctorPos == 0) { Toast.makeText(this, "Please select a doctor", Toast.LENGTH_SHORT).show(); return; }

        int patientId;
        try { patientId = Integer.parseInt(patientIdStr); }
        catch (NumberFormatException e) { etPatientId.setError("Enter a valid Patient ID"); return; }

        int doctorId = doctorIds.get(selectedDoctorPos);
        int deptId = departmentIds.get(spinnerDepartment.getSelectedItemPosition());

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableAppointment.COLUMN_PATIENT_ID, patientId);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_DOCTOR_ID, doctorId);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_DEPARTMENT_ID, deptId > 0 ? deptId : null);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_DATE, date);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_TIME, time);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_REASON, reason);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_STATUS, status);
        cv.put(DatabaseHelper.TableAppointment.COLUMN_NOTES, notes);

        if (appointmentId == -1) {
            long result = db.insertAppointment(cv);
            if (result > 0) { Toast.makeText(this, "Appointment created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create appointment", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updateAppointment(appointmentId, cv);
            if (result > 0) { Toast.makeText(this, "Appointment updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update appointment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}