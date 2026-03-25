package com.example.a4200finalproj.UI.Activities.Medication;

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
import com.example.a4200finalproj.Models.Medication;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MedicationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicationAdapter adapter;
    private List<Medication> medicationList = new ArrayList<>();
    private List<Medication> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Medications");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicationAdapter(filteredList, this::onMedicationClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, MedicationFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedications();
    }

    private void loadMedications() {
        medicationList.clear();
        Cursor cursor = db.getAllMedications();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Medication m = new Medication();
                m.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_ID)));
                m.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_NAME)));
                m.setGenericName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_GENERIC_NAME)));
                m.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_DOSAGE)));
                m.setForm(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_FORM)));
                m.setManufacturer(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_MANUFACTURER)));
                m.setSideEffects(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_SIDE_EFFECTS)));
                m.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_IS_ACTIVE)));
                medicationList.add(m);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Medication m : medicationList) {
            if (query.isEmpty()
                    || m.getName().toLowerCase().contains(query)
                    || (m.getGenericName() != null && m.getGenericName().toLowerCase().contains(query))
                    || (m.getForm() != null && m.getForm().toLowerCase().contains(query))) {
                filteredList.add(m);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onMedicationClick(Medication medication) {
        Intent intent = new Intent(this, MedicationFormActivity.class);
        intent.putExtra("medication_id", medication.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Medication medication) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medication")
                .setMessage("Delete medication \"" + medication.getName() + "\"?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteMedication(medication.getId());
                    Toast.makeText(this, "Medication deleted", Toast.LENGTH_SHORT).show();
                    loadMedications();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}