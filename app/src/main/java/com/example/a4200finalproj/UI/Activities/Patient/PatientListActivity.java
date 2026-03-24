package com.example.a4200finalproj.UI.Activities.Patient;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Models.Patient;
import com.example.a4200finalproj.R;
import com.example.a4200finalproj.UI.Activities.Adapters.PatientAdapter;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView rvPatients;
    private EditText etSearchPatient;
    private Button btnAddPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        databaseHelper = DatabaseHelper.getInstance(this);

        rvPatients = findViewById(R.id.rvPatients);
        etSearchPatient = findViewById(R.id.etSearchPatient);
        btnAddPatient = findViewById(R.id.btnAddPatient);

        rvPatients.setLayoutManager(new LinearLayoutManager(this));

        btnAddPatient.setOnClickListener(v ->
                startActivity(new Intent(PatientListActivity.this, PatientFormActivity.class)));

        etSearchPatient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadPatients(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        loadPatients("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPatients(etSearchPatient.getText().toString().trim());
    }

    private void loadPatients(String query) {
        List<Patient> patients = new ArrayList<>();
        Cursor cursor = query.isEmpty() ? databaseHelper.getAllPatients() : databaseHelper.searchPatients(query);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Patient patient = new Patient();
                patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ID)));
                patient.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                patient.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_DATE_OF_BIRTH)));
                patient.setGender(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_GENDER)));
                patient.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_PHONE)));
                patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMAIL)));
                patient.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ADDRESS)));
                patient.setEmergencyContact(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_CONTACT)));
                patient.setEmergencyPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_PHONE)));
                patient.setBloodType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_BLOOD_TYPE)));
                patient.setAllergies(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ALLERGIES)));
                patients.add(patient);
            }
            cursor.close();
        }

        PatientAdapter adapter = new PatientAdapter(patients, patient -> {
            Intent intent = new Intent(PatientListActivity.this, PatientFormActivity.class);
            intent.putExtra("patient_id", patient.getId());
            startActivity(intent);
        });

        rvPatients.setAdapter(adapter);
    }
}