package com.example.a4200finalproj.UI.Activities.Doctor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Models.Doctor;
import com.example.a4200finalproj.R;
import com.example.a4200finalproj.UI.Activities.Adapters.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoctorListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView rvDoctors;
    private Button btnAddDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        databaseHelper = new DatabaseHelper(this);

        rvDoctors = findViewById(R.id.rvDoctors);
        btnAddDoctor = findViewById(R.id.btnAddDoctor);

        rvDoctors.setLayoutManager(new LinearLayoutManager(this));

        btnAddDoctor.setOnClickListener(v ->
                startActivity(new Intent(DoctorListActivity.this, DoctorFormActivity.class)));

        loadDoctors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctors();
    }

    private void loadDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllDoctors();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Doctor doctor = new Doctor();
                doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_ID)));
                doctor.setDepartmentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_DEPARTMENT_ID)));
                doctor.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
                doctor.setSpecialization(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_SPECIALIZATION)));
                doctor.setLicenseNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_LICENSE_NUMBER)));
                doctor.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_PHONE)));
                doctor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_EMAIL)));
                doctor.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_ADDRESS)));
                doctors.add(doctor);
            }
            cursor.close();
        }

        DoctorAdapter adapter = new DoctorAdapter(doctors, doctor -> {
            Intent intent = new Intent(DoctorListActivity.this, DoctorFormActivity.class);
            intent.putExtra("doctor_id", doctor.getId());
            startActivity(intent);
        });

        rvDoctors.setAdapter(adapter);
    }
}