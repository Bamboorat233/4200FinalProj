package com.example.a4200finalproj.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.a4200finalproj.Helpers.SessionManager;
import com.example.a4200finalproj.R;
import com.example.a4200finalproj.UI.Activities.Appointment.AppointmentListActivity;
import com.example.a4200finalproj.UI.Activities.Department.DepartmentListActivity;
import com.example.a4200finalproj.UI.Activities.Doctor.DoctorListActivity;
import com.example.a4200finalproj.UI.Activities.Invoice.InvoiceListActivity;
import com.example.a4200finalproj.UI.Activities.MedicalRecord.MedicalRecordListActivity;
import com.example.a4200finalproj.UI.Activities.Medication.MedicationListActivity;
import com.example.a4200finalproj.UI.Activities.Patient.PatientListActivity;
import com.example.a4200finalproj.UI.Activities.Payment.PaymentListActivity;
import com.example.a4200finalproj.UI.Activities.Prescription.PrescriptionListActivity;
import com.example.a4200finalproj.UI.Activities.Report.ReportListActivity;
import com.example.a4200finalproj.UI.Activities.User.UserListActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView tvWelcome, tvRole;
    private Button btnLogout, btnPatients, btnDoctors, btnAppointments,
            btnDepartments, btnMedicalRecords, btnMedications, btnPrescriptions,
            btnInvoices, btnPayments, btnReports, btnUsers;
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

        toolbar          = findViewById(R.id.toolbar);
        drawerLayout     = findViewById(R.id.drawerLayout);
        navigationView   = findViewById(R.id.navigationView);
        tvWelcome        = findViewById(R.id.tvWelcome);
        tvRole           = findViewById(R.id.tvRole);
        btnLogout        = findViewById(R.id.btnLogout);
        btnPatients      = findViewById(R.id.btnPatients);
        btnDoctors       = findViewById(R.id.btnDoctors);
        btnAppointments  = findViewById(R.id.btnAppointments);
        btnDepartments   = findViewById(R.id.btnDepartments);
        btnMedicalRecords = findViewById(R.id.btnMedicalRecords);
        btnMedications   = findViewById(R.id.btnMedications);
        btnPrescriptions = findViewById(R.id.btnPrescriptions);
        btnInvoices      = findViewById(R.id.btnInvoices);
        btnPayments      = findViewById(R.id.btnPayments);
        btnReports       = findViewById(R.id.btnReports);
        btnUsers         = findViewById(R.id.btnUsers);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                android.R.string.ok, android.R.string.cancel
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        String username = sessionManager.getUsername();
        String role = sessionManager.getRole();

        tvWelcome.setText("Welcome, " + username);
        tvRole.setText("Role: " + role);

        TextView tvDrawerUsername = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerUsername);
        TextView tvDrawerRole = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerRole);
        tvDrawerUsername.setText(username);
        tvDrawerRole.setText(role);

        applyRoleVisibility(role);

        btnPatients.setOnClickListener(v -> startActivity(new Intent(this, PatientListActivity.class)));
        btnDoctors.setOnClickListener(v -> startActivity(new Intent(this, DoctorListActivity.class)));
        btnAppointments.setOnClickListener(v -> startActivity(new Intent(this, AppointmentListActivity.class)));
        btnDepartments.setOnClickListener(v -> startActivity(new Intent(this, DepartmentListActivity.class)));
        btnMedicalRecords.setOnClickListener(v -> startActivity(new Intent(this, MedicalRecordListActivity.class)));
        btnMedications.setOnClickListener(v -> startActivity(new Intent(this, MedicationListActivity.class)));
        btnPrescriptions.setOnClickListener(v -> startActivity(new Intent(this, PrescriptionListActivity.class)));
        btnInvoices.setOnClickListener(v -> startActivity(new Intent(this, InvoiceListActivity.class)));
        btnPayments.setOnClickListener(v -> startActivity(new Intent(this, PaymentListActivity.class)));
        btnReports.setOnClickListener(v -> startActivity(new Intent(this, ReportListActivity.class)));
        btnUsers.setOnClickListener(v -> startActivity(new Intent(this, UserListActivity.class)));
        btnLogout.setOnClickListener(v -> logout());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();

            if (id == R.id.nav_dashboard) return true;
            else if (id == R.id.nav_patients) startActivity(new Intent(this, PatientListActivity.class));
            else if (id == R.id.nav_doctors) startActivity(new Intent(this, DoctorListActivity.class));
            else if (id == R.id.nav_appointments) startActivity(new Intent(this, AppointmentListActivity.class));
            else if (id == R.id.nav_departments) startActivity(new Intent(this, DepartmentListActivity.class));
            else if (id == R.id.nav_medical_records) startActivity(new Intent(this, MedicalRecordListActivity.class));
            else if (id == R.id.nav_medications) startActivity(new Intent(this, MedicationListActivity.class));
            else if (id == R.id.nav_prescriptions) startActivity(new Intent(this, PrescriptionListActivity.class));
            else if (id == R.id.nav_invoices) startActivity(new Intent(this, InvoiceListActivity.class));
            else if (id == R.id.nav_payments) startActivity(new Intent(this, PaymentListActivity.class));
            else if (id == R.id.nav_reports) startActivity(new Intent(this, ReportListActivity.class));
            else if (id == R.id.nav_users) startActivity(new Intent(this, UserListActivity.class));
            else if (id == R.id.nav_logout) logout();

            return true;
        });
    }

    private void applyRoleVisibility(String role) {
        Menu menu = navigationView.getMenu();

        boolean isAdmin        = "Admin".equalsIgnoreCase(role);
        boolean isDoctor       = "Doctor".equalsIgnoreCase(role);
        boolean isNurse        = "Nurse".equalsIgnoreCase(role);
        boolean isReceptionist = "Receptionist".equalsIgnoreCase(role);

        boolean canAccessPatients      = isAdmin || isDoctor || isNurse || isReceptionist;
        boolean canAccessDoctors       = isAdmin || isDoctor;
        boolean canAccessAppointments  = isAdmin || isDoctor || isNurse || isReceptionist;
        boolean canAccessDepartments   = isAdmin;
        boolean canAccessMedical       = isAdmin || isDoctor || isNurse;
        boolean canAccessMedications   = isAdmin || isDoctor || isNurse;
        boolean canAccessPrescriptions = isAdmin || isDoctor;
        boolean canAccessInvoices      = isAdmin || isReceptionist;
        boolean canAccessPayments      = isAdmin || isReceptionist;
        boolean canAccessReports       = isAdmin || isDoctor;
        boolean canAccessUsers         = isAdmin;

        btnPatients.setVisibility(canAccessPatients ? View.VISIBLE : View.GONE);
        btnDoctors.setVisibility(canAccessDoctors ? View.VISIBLE : View.GONE);
        btnAppointments.setVisibility(canAccessAppointments ? View.VISIBLE : View.GONE);
        btnDepartments.setVisibility(canAccessDepartments ? View.VISIBLE : View.GONE);
        btnMedicalRecords.setVisibility(canAccessMedical ? View.VISIBLE : View.GONE);
        btnMedications.setVisibility(canAccessMedications ? View.VISIBLE : View.GONE);
        btnPrescriptions.setVisibility(canAccessPrescriptions ? View.VISIBLE : View.GONE);
        btnInvoices.setVisibility(canAccessInvoices ? View.VISIBLE : View.GONE);
        btnPayments.setVisibility(canAccessPayments ? View.VISIBLE : View.GONE);
        btnReports.setVisibility(canAccessReports ? View.VISIBLE : View.GONE);
        btnUsers.setVisibility(canAccessUsers ? View.VISIBLE : View.GONE);

        menu.findItem(R.id.nav_patients).setVisible(canAccessPatients);
        menu.findItem(R.id.nav_doctors).setVisible(canAccessDoctors);
        menu.findItem(R.id.nav_appointments).setVisible(canAccessAppointments);
        menu.findItem(R.id.nav_departments).setVisible(canAccessDepartments);
        menu.findItem(R.id.nav_medical_records).setVisible(canAccessMedical);
        menu.findItem(R.id.nav_medications).setVisible(canAccessMedications);
        menu.findItem(R.id.nav_prescriptions).setVisible(canAccessPrescriptions);
        menu.findItem(R.id.nav_invoices).setVisible(canAccessInvoices);
        menu.findItem(R.id.nav_payments).setVisible(canAccessPayments);
        menu.findItem(R.id.nav_reports).setVisible(canAccessReports);
        menu.findItem(R.id.nav_users).setVisible(canAccessUsers);
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}