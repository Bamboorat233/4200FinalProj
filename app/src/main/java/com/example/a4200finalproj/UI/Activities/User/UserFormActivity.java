package com.example.a4200finalproj.UI.Activities.User;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.R;

public class UserFormActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etFullName, etEmail, etPhone, etAddress;
    private Spinner spinnerRole;
    private Button btnSave;
    private DatabaseHelper db;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etUsername  = findViewById(R.id.etUsername);
        etPassword  = findViewById(R.id.etPassword);
        etFullName  = findViewById(R.id.etFullName);
        etEmail     = findViewById(R.id.etEmail);
        etPhone     = findViewById(R.id.etPhone);
        etAddress   = findViewById(R.id.etAddress);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave     = findViewById(R.id.btnSave);

        String[] roles = {"Admin", "Doctor", "Nurse", "Receptionist"};
        spinnerRole.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles));

        userId = getIntent().getIntExtra("user_id", -1);
        boolean isEdit = userId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit User" : "New User");
        }

        if (isEdit) {
            loadUserData();
            etUsername.setEnabled(false);
        }

        btnSave.setOnClickListener(v -> saveUser());
    }

    private void loadUserData() {
        Cursor cursor = db.getUserById(userId);
        if (cursor != null && cursor.moveToFirst()) {
            etUsername.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_USERNAME)));
            etFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_FULL_NAME)));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_EMAIL)));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_PHONE)));
            etAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ADDRESS)));

            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ROLE));
            String[] roles = {"Admin", "Doctor", "Nurse", "Receptionist"};
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].equals(role)) { spinnerRole.setSelection(i); break; }
            }
            cursor.close();
        }
    }

    private void saveUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String address  = etAddress.getText().toString().trim();
        String role     = spinnerRole.getSelectedItem().toString();

        if (TextUtils.isEmpty(username)) { etUsername.setError("Required"); return; }
        if (TextUtils.isEmpty(fullName)) { etFullName.setError("Required"); return; }
        if (userId == -1 && TextUtils.isEmpty(password)) { etPassword.setError("Required for new users"); return; }

        if (userId == -1 && db.isUsernameExists(username)) {
            etUsername.setError("Username already taken");
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableUser.COLUMN_FULL_NAME, fullName);
        cv.put(DatabaseHelper.TableUser.COLUMN_EMAIL, email);
        cv.put(DatabaseHelper.TableUser.COLUMN_PHONE, phone);
        cv.put(DatabaseHelper.TableUser.COLUMN_ADDRESS, address);
        cv.put(DatabaseHelper.TableUser.COLUMN_ROLE, role);
        cv.put(DatabaseHelper.TableUser.COLUMN_IS_ACTIVE, 1);

        if (userId == -1) {
            cv.put(DatabaseHelper.TableUser.COLUMN_USERNAME, username);
            cv.put(DatabaseHelper.TableUser.COLUMN_PASSWORD, password);
            long result = db.insertUser(cv);
            if (result > 0) { Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(password)) cv.put(DatabaseHelper.TableUser.COLUMN_PASSWORD, password);
            int result = db.updateUser(userId, cv);
            if (result > 0) { Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}