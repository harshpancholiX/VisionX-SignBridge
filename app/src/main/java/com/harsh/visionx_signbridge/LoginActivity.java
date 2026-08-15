package com.harsh.visionx_signbridge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // =========================
        // LOGIN
        // =========================

        btnLogin.setOnClickListener(v -> {

            if (validateLogin()) {

                // API will be added later

                android.widget.Toast.makeText(
                        LoginActivity.this,
                        "Login details are valid",
                        android.widget.Toast.LENGTH_SHORT
                ).show();
            }
        });


        // =========================
        // FORGOT PASSWORD
        // =========================

        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    Forgot_Password_Activity.class
            );

            startActivity(intent);
        });


        // =========================
        // CREATE ACCOUNT
        // =========================

        btnCreateAccount.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }


    // =====================================================
    // LOGIN VALIDATION
    // =====================================================

    private boolean validateLogin() {

        String email = etEmail.getText() != null
                ? etEmail.getText().toString().trim()
                : "";

        String password = etPassword.getText() != null
                ? etPassword.getText().toString()
                : "";


        // Email required
        if (email.isEmpty()) {

            etEmail.setError("Email is required");
            etEmail.requestFocus();

            return false;
        }


        // Email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();

            return false;
        }


        // Password required
        if (password.isEmpty()) {

            etPassword.setError("Password is required");
            etPassword.requestFocus();

            return false;
        }


        // Minimum 8 characters
        if (password.length() < 8) {

            etPassword.setError(
                    "Password must contain at least 8 characters"
            );

            etPassword.requestFocus();

            return false;
        }


        // Uppercase
        if (!password.matches(".*[A-Z].*")) {

            etPassword.setError(
                    "Password must contain at least 1 uppercase letter"
            );

            etPassword.requestFocus();

            return false;
        }


        // Lowercase
        if (!password.matches(".*[a-z].*")) {

            etPassword.setError(
                    "Password must contain at least 1 lowercase letter"
            );

            etPassword.requestFocus();

            return false;
        }


        // Number
        if (!password.matches(".*[0-9].*")) {

            etPassword.setError(
                    "Password must contain at least 1 number"
            );

            etPassword.requestFocus();

            return false;
        }


        // Special character
        if (!password.matches(".*[^a-zA-Z0-9].*")) {

            etPassword.setError(
                    "Password must contain at least 1 special symbol"
            );

            etPassword.requestFocus();

            return false;
        }


        return true;
    }
}