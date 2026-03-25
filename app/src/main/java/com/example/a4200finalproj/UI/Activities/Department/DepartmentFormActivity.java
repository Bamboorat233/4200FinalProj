package com.example.a4200finalproj.UI.Activities.Department;

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

public class DepartmentFormActivity extends AppCompatActivity {

    private EditText etName, etDescription, etLocation, etPhone;
    private Button btnSave;
    private DatabaseHelper db;
    private int departmentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etName        = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etLocation    = findViewById(R.id.etLocation);
        etPhone       = findViewById(R.id.etPhone);
        btnSave       = findViewById(R.id.btnSave);

        departmentId = getIntent().getIntExtra("department_id", -1);
        boolean isEdit = departmentId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Department" : "New Department");
        }

        if (isEdit) loadDepartmentData();

        btnSave.setOnClickListener(v -> saveDepartment());
    }

    private void loadDepartmentData() {
        Cursor cursor = db.getDepartmentById(departmentId);
        if (cursor != null && cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_NAME)));
            etDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_DESCRIPTION)));
            etLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_LOCATION)));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_PHONE)));
            cursor.close();
        }
    }

    private void saveDepartment() {
        String name        = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String location    = etLocation.getText().toString().trim();
        String phone       = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Department name is required");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            etLocation.setError("Location is required");
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableDepartment.COLUMN_NAME, name);
        cv.put(DatabaseHelper.TableDepartment.COLUMN_DESCRIPTION, description);
        cv.put(DatabaseHelper.TableDepartment.COLUMN_LOCATION, location);
        cv.put(DatabaseHelper.TableDepartment.COLUMN_PHONE, phone);
        cv.put(DatabaseHelper.TableDepartment.COLUMN_IS_ACTIVE, 1);

        if (departmentId == -1) {
            long result = db.insertDepartment(cv);
            if (result > 0) {
                Toast.makeText(this, "Department created", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to create department", Toast.LENGTH_SHORT).show();
            }
        } else {
            int result = db.updateDepartment(departmentId, cv);
            if (result > 0) {
                Toast.makeText(this, "Department updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update department", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}