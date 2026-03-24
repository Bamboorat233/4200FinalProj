package com.example.a4200finalproj.UI.Activities.Prescription;

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
import com.example.a4200finalproj.Models.Prescription;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PrescriptionAdapter adapter;
    private List<Prescription> prescriptionList = new ArrayList<>();
    private List<Prescription> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Prescriptions");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch     = findViewById(R.id.etSearch);
        tvEmpty      = findViewById(R.id.tvEmpty);
        fabAdd       = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrescriptionAdapter(filteredList, this::onPrescriptionClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, PrescriptionFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrescriptions();
    }

    private void loadPrescriptions() {
        prescriptionList.clear();
        Cursor cursor = db.getAllPrescriptions();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Prescription p = new Prescription();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_ID)));
                p.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_PATIENT_ID)));
                p.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DOCTOR_ID)));
                p.setMedicationId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_MEDICATION_ID)));
                p.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DOSAGE)));
                p.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DURATION)));
                p.setFrequency(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_FREQUENCY)));
                p.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_INSTRUCTIONS)));

                Cursor patientCursor = db.getPatientById(p.getPatientId());
                if (patientCursor != null && patientCursor.moveToFirst()) {
                    p.setPatientName(patientCursor.getString(patientCursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                    patientCursor.close();
                }

                Cursor doctorCursor = db.getDoctorById(p.getDoctorId());
                if (doctorCursor != null && doctorCursor.moveToFirst()) {
                    p.setDoctorName(doctorCursor.getString(doctorCursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
                    doctorCursor.close();
                }

                Cursor medCursor = db.getMedicationById(p.getMedicationId());
                if (medCursor != null && medCursor.moveToFirst()) {
                    p.setMedicationName(medCursor.getString(medCursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_NAME)));
                    medCursor.close();
                }

                prescriptionList.add(p);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Prescription p : prescriptionList) {
            if (query.isEmpty()
                    || (p.getPatientName() != null && p.getPatientName().toLowerCase().contains(query))
                    || (p.getMedicationName() != null && p.getMedicationName().toLowerCase().contains(query))
                    || (p.getDoctorName() != null && p.getDoctorName().toLowerCase().contains(query))) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onPrescriptionClick(Prescription prescription) {
        Intent intent = new Intent(this, PrescriptionFormActivity.class);
        intent.putExtra("prescription_id", prescription.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Prescription prescription) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Prescription")
                .setMessage("Delete prescription for " + prescription.getPatientName() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deletePrescription(prescription.getId());
                    Toast.makeText(this, "Prescription deleted", Toast.LENGTH_SHORT).show();
                    loadPrescriptions();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}