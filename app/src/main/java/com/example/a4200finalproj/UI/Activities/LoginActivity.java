package com.example.a4200finalproj.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Helpers.SessionManager;
import com.example.a4200finalproj.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView tvError;
    private Button btnLogin;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = DatabaseHelper.getInstance(this);
        databaseHelper.seedDefaultUsersIfNeeded();
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> attemptLogin());

        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            boolean isEnter =
                    event != null
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && event.getAction() == KeyEvent.ACTION_DOWN;

            if (isEnter) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    private void attemptLogin() {
        tvError.setVisibility(View.GONE);

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showError("Please enter username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showError("Please enter password");
            etPassword.requestFocus();
            return;
        }

        boolean isAuthenticated = databaseHelper.authenticateUser(username, password);

        if (isAuthenticated) {
            String role = databaseHelper.getUserRole(username, password);
            int userId = databaseHelper.getUserIdByUsername(username);

            sessionManager.createLoginSession(userId, username, role);

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            showError("Invalid username or password");
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }


}