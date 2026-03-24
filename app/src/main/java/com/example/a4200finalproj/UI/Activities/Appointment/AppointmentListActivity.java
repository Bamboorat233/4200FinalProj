package com.example.a4200finalproj.UI.Activities.Appointment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Models.Appointment;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();
    private List<Appointment> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private Spinner spinnerStatus;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;
    private String selectedStatus = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Appointments");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(filteredList, this::onAppointmentClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        String[] statuses = {"All", "Scheduled", "Completed", "Cancelled"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statuses[position];
                applyFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, AppointmentFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }

    private void loadAppointments() {
        appointmentList.clear();
        Cursor cursor = db.getAllAppointments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Appointment a = new Appointment();
                a.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_ID)));
                a.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_PATIENT_ID)));
                a.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_DOCTOR_ID)));
                a.setAppointmentDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_DATE)));
                a.setAppointmentTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_TIME)));
                a.setReason(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_REASON)));
                a.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_STATUS)));
                a.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_NOTES)));

                Cursor patientCursor = db.getPatientById(a.getPatientId());
                if (patientCursor != null && patientCursor.moveToFirst()) {
                    a.setPatientName(patientCursor.getString(patientCursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                    patientCursor.close();
                }
                Cursor doctorCursor = db.getDoctorById(a.getDoctorId());
                if (doctorCursor != null && doctorCursor.moveToFirst()) {
                    a.setDoctorName(doctorCursor.getString(doctorCursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
                    doctorCursor.close();
                }
                appointmentList.add(a);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Appointment a : appointmentList) {
            boolean matchesStatus = selectedStatus.equals("All") || selectedStatus.equals(a.getStatus());
            boolean matchesSearch = query.isEmpty()
                    || (a.getPatientName() != null && a.getPatientName().toLowerCase().contains(query))
                    || (a.getDoctorName() != null && a.getDoctorName().toLowerCase().contains(query))
                    || (a.getReason() != null && a.getReason().toLowerCase().contains(query))
                    || (a.getAppointmentDate() != null && a.getAppointmentDate().contains(query));
            if (matchesStatus && matchesSearch) filteredList.add(a);
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onAppointmentClick(Appointment appointment) {
        Intent intent = new Intent(this, AppointmentFormActivity.class);
        intent.putExtra("appointment_id", appointment.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Appointment appointment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Appointment")
                .setMessage("Delete appointment for " + appointment.getPatientName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteAppointment(appointment.getId());
                    Toast.makeText(this, "Appointment deleted", Toast.LENGTH_SHORT).show();
                    loadAppointments();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}