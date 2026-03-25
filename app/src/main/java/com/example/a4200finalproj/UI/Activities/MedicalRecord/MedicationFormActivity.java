package com.example.a4200finalproj.UI.Activities.Medication;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.R;

public class MedicationFormActivity extends AppCompatActivity {

    private EditText etName, etGenericName, etDosage, etForm, etManufacturer, etSideEffects;
    private Button btnSave;
    private DatabaseHelper db;
    private int medicationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etName        = findViewById(R.id.etName);
        etGenericName = findViewById(R.id.etGenericName);
        etDosage      = findViewById(R.id.etDosage);
        etForm        = findViewById(R.id.etForm);
        etManufacturer = findViewById(R.id.etManufacturer);
        etSideEffects = findViewById(R.id.etSideEffects);
        btnSave       = findViewById(R.id.btnSave);

        medicationId = getIntent().getIntExtra("medication_id", -1);
        boolean isEdit = medicationId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Medication" : "New Medication");
        }

        if (isEdit) loadMedicationData();

        btnSave.setOnClickListener(v -> saveMedication());
    }

    private void loadMedicationData() {
        Cursor cursor = db.getMedicationById(medicationId);
        if (cursor != null && cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_NAME)));
            etGenericName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_GENERIC_NAME)));
            etDosage.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_DOSAGE)));
            etForm.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_FORM)));
            etManufacturer.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_MANUFACTURER)));
            etSideEffects.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_SIDE_EFFECTS)));
            cursor.close();
        }
    }

    private void saveMedication() {
        String name        = etName.getText().toString().trim();
        String genericName = etGenericName.getText().toString().trim();
        String dosage      = etDosage.getText().toString().trim();
        String form        = etForm.getText().toString().trim();
        String manufacturer = etManufacturer.getText().toString().trim();
        String sideEffects = etSideEffects.getText().toString().trim();

        if (TextUtils.isEmpty(name)) { etName.setError("Medication name is required"); return; }
        if (TextUtils.isEmpty(dosage)) { etDosage.setError("Dosage is required"); return; }

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableMedication.COLUMN_NAME, name);
        cv.put(DatabaseHelper.TableMedication.COLUMN_GENERIC_NAME, genericName);
        cv.put(DatabaseHelper.TableMedication.COLUMN_DOSAGE, dosage);
        cv.put(DatabaseHelper.TableMedication.COLUMN_FORM, form);
        cv.put(DatabaseHelper.TableMedication.COLUMN_MANUFACTURER, manufacturer);
        cv.put(DatabaseHelper.TableMedication.COLUMN_SIDE_EFFECTS, sideEffects);
        cv.put(DatabaseHelper.TableMedication.COLUMN_IS_ACTIVE, 1);

        if (medicationId == -1) {
            long result = db.insertMedication(cv);
            if (result > 0) { Toast.makeText(this, "Medication created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create medication", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updateMedication(medicationId, cv);
            if (result > 0) { Toast.makeText(this, "Medication updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update medication", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}