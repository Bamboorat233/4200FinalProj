package com.example.a4200finalproj.UI.Activities.Report;

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
import com.example.a4200finalproj.Models.Report;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ReportListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<Report> reportList = new ArrayList<>();
    private List<Report> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private Spinner spinnerType;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;
    private String selectedType = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reports");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch     = findViewById(R.id.etSearch);
        spinnerType  = findViewById(R.id.spinnerType);
        tvEmpty      = findViewById(R.id.tvEmpty);
        fabAdd       = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportAdapter(filteredList, this::onReportClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        String[] types = {"All", "Patient", "Doctor", "Appointment", "Financial", "General"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = types[position];
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

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, ReportFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReports();
    }

    private void loadReports() {
        reportList.clear();
        Cursor cursor = db.getAllReports();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Report r = new Report();
                r.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_ID)));
                r.setReportType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_REPORT_TYPE)));
                r.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_TITLE)));
                r.setGeneratedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_GENERATED_DATE)));
                r.setGeneratedBy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_GENERATED_BY)));
                r.setData(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_DATA)));

                int patientId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_PATIENT_ID));
                if (patientId > 0) {
                    r.setPatientId(patientId);
                    Cursor patientCursor = db.getPatientById(patientId);
                    if (patientCursor != null && patientCursor.moveToFirst()) {
                        r.setPatientName(patientCursor.getString(patientCursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                        patientCursor.close();
                    }
                }

                int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_DOCTOR_ID));
                if (doctorId > 0) {
                    r.setDoctorId(doctorId);
                    Cursor doctorCursor = db.getDoctorById(doctorId);
                    if (doctorCursor != null && doctorCursor.moveToFirst()) {
                        r.setDoctorName(doctorCursor.getString(doctorCursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
                        doctorCursor.close();
                    }
                }
                reportList.add(r);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Report r : reportList) {
            boolean matchesType = selectedType.equals("All") || selectedType.equals(r.getReportType());
            boolean matchesSearch = query.isEmpty()
                    || (r.getTitle() != null && r.getTitle().toLowerCase().contains(query))
                    || (r.getGeneratedBy() != null && r.getGeneratedBy().toLowerCase().contains(query))
                    || (r.getReportType() != null && r.getReportType().toLowerCase().contains(query));
            if (matchesType && matchesSearch) filteredList.add(r);
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onReportClick(Report report) {
        Intent intent = new Intent(this, ReportFormActivity.class);
        intent.putExtra("report_id", report.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Report report) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Report")
                .setMessage("Delete report \"" + report.getTitle() + "\"?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteReport(report.getId());
                    Toast.makeText(this, "Report deleted", Toast.LENGTH_SHORT).show();
                    loadReports();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}