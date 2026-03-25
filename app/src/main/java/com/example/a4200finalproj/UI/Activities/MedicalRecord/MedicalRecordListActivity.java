package com.example.a4200finalproj.UI.Activities.MedicalRecord;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Models.MedicalRecord;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicalRecordAdapter adapter;
    private List<MedicalRecord> recordList = new ArrayList<>();
    private List<MedicalRecord> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Medical Records");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicalRecordAdapter(filteredList, this::onRecordClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, MedicalRecordFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords();
    }

    private void loadRecords() {
        recordList.clear();
        Cursor cursor = db.getAllMedicalRecords();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MedicalRecord r = new MedicalRecord();
                r.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_ID)));
                r.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_PATIENT_ID)));
                r.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_DOCTOR_ID)));
                r.setVisitDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_VISIT_DATE)));
                r.setDiagnosis(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_DIAGNOSIS)));
                r.setComplaints(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_COMPLAINTS)));
                r.setTreatment(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_TREATMENT)));

                Cursor patientCursor = db.getPatientById(r.getPatientId());
                if (patientCursor != null && patientCursor.moveToFirst()) {
                    r.setPatientName(patientCursor.getString(patientCursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                    patientCursor.close();
                }
                Cursor doctorCursor = db.getDoctorById(r.getDoctorId());
                if (doctorCursor != null && doctorCursor.moveToFirst()) {
                    r.setDoctorName(doctorCursor.getString(doctorCursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
                    doctorCursor.close();
                }
                recordList.add(r);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (MedicalRecord r : recordList) {
            if (query.isEmpty()
                    || (r.getPatientName() != null && r.getPatientName().toLowerCase().contains(query))
                    || (r.getDiagnosis() != null && r.getDiagnosis().toLowerCase().contains(query))
                    || (r.getVisitDate() != null && r.getVisitDate().contains(query))) {
                filteredList.add(r);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onRecordClick(MedicalRecord record) {
        Intent intent = new Intent(this, MedicalRecordFormActivity.class);
        intent.putExtra("record_id", record.getId());
        startActivity(intent);
    }

    private void onDeleteClick(MedicalRecord record) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Delete medical record for " + record.getPatientName() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteMedicalRecord(record.getId());
                    Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                    loadRecords();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}