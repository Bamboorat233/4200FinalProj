package com.example.a4200finalproj.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.a4200finalproj.Helpers.SessionManager;
import com.example.a4200finalproj.R;
import com.example.a4200finalproj.UI.Activities.Doctor.DoctorListActivity;
import com.example.a4200finalproj.UI.Activities.Patient.PatientListActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView tvWelcome, tvRole;
    private Button btnLogout, btnPatients, btnDoctors;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                android.R.string.ok, android.R.string.cancel
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnPatients = findViewById(R.id.btnPatients);
        btnDoctors = findViewById(R.id.btnDoctors);

        String username = sessionManager.getUsername();
        String role = sessionManager.getRole();

        tvWelcome.setText("Welcome, " + username);
        tvRole.setText("Role: " + role);

        TextView tvDrawerUsername = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerUsername);
        TextView tvDrawerRole = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerRole);
        tvDrawerUsername.setText(username);
        tvDrawerRole.setText(role);

        applyRoleVisibility(role);

        btnPatients.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PatientListActivity.class)));

        btnDoctors.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DoctorListActivity.class)));

        btnLogout.setOnClickListener(v -> logout());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_patients) {
                startActivity(new Intent(MainActivity.this, PatientListActivity.class));
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_doctors) {
                startActivity(new Intent(MainActivity.this, DoctorListActivity.class));
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_logout) {
                logout();
                return true;
            }

            return false;
        });
    }

    private void applyRoleVisibility(String role) {
        Menu menu = navigationView.getMenu();

        boolean canAccessDoctors = "Admin".equalsIgnoreCase(role) || "Doctor".equalsIgnoreCase(role);

        btnDoctors.setEnabled(canAccessDoctors);
        btnDoctors.setVisibility(canAccessDoctors ? android.view.View.VISIBLE : android.view.View.GONE);

        menu.findItem(R.id.nav_doctors).setVisible(canAccessDoctors);
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}