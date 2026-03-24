package com.example.a4200finalproj.UI.Activities.Department;

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
import com.example.a4200finalproj.Models.Department;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DepartmentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private List<Department> departmentList = new ArrayList<>();
    private List<Department> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Departments");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DepartmentAdapter(filteredList, this::onDepartmentClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, DepartmentFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDepartments();
    }

    private void loadDepartments() {
        departmentList.clear();
        Cursor cursor = db.getAllDepartments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Department d = new Department();
                d.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_ID)));
                d.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_NAME)));
                d.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_DESCRIPTION)));
                d.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_LOCATION)));
                d.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_PHONE)));
                d.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_IS_ACTIVE)));
                departmentList.add(d);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Department d : departmentList) {
            if (query.isEmpty()
                    || d.getName().toLowerCase().contains(query)
                    || (d.getLocation() != null && d.getLocation().toLowerCase().contains(query))) {
                filteredList.add(d);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onDepartmentClick(Department department) {
        Intent intent = new Intent(this, DepartmentFormActivity.class);
        intent.putExtra("department_id", department.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Department department) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Department")
                .setMessage("Delete department \"" + department.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteDepartment(department.getId());
                    Toast.makeText(this, "Department deleted", Toast.LENGTH_SHORT).show();
                    loadDepartments();
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