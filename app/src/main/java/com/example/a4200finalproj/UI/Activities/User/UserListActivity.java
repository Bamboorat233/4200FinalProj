package com.example.a4200finalproj.UI.Activities.User;

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
import com.example.a4200finalproj.Helpers.SessionManager;
import com.example.a4200finalproj.Models.User;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private List<User> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager(this);
        if (!"Admin".equalsIgnoreCase(session.getRole())) {
            Toast.makeText(this, "Access denied: Admins only", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("User Management");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(filteredList, this::onUserClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, UserFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        Cursor cursor = db.getAllUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                User u = new User();
                u.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ID)));
                u.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_USERNAME)));
                u.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_FULL_NAME)));
                u.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_EMAIL)));
                u.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ROLE)));
                u.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_PHONE)));
                u.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_IS_ACTIVE)));
                userList.add(u);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (User u : userList) {
            if (query.isEmpty()
                    || u.getFullName().toLowerCase().contains(query)
                    || u.getUsername().toLowerCase().contains(query)
                    || u.getRole().toLowerCase().contains(query)) {
                filteredList.add(u);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onUserClick(User user) {
        Intent intent = new Intent(this, UserFormActivity.class);
        intent.putExtra("user_id", user.getId());
        startActivity(intent);
    }

    private void onDeleteClick(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Delete user \"" + user.getUsername() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteUser(user.getId());
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
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