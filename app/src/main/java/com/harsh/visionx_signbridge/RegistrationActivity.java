package com.harsh.visionx_signbridge;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RegistrationActivity extends AppCompatActivity {

    EditText etRegistrationName, etRegistrationMobNumber,
            etRegistrationEmailId, etRegistrationUsername,
            etRegistrationPassword, etRegistrationConformPassword;
    ProgressDialog progressDialog;
    Button btnRegistrationRegister;
    CheckBox cbRegistrationShowHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etRegistrationName = findViewById(R.id.etRegistrationName);
        etRegistrationMobNumber = findViewById(R.id.etRegistrationMobNumber);
        etRegistrationEmailId = findViewById(R.id.etRegistrationEmailId);
        etRegistrationUsername = findViewById(R.id.etRegistrationUsername);
        etRegistrationPassword = findViewById(R.id.etRegistrationPassword);
        etRegistrationConformPassword = findViewById(R.id.etRegistrationConformPassword);
        btnRegistrationRegister = findViewById(R.id.btnRegistrationRegister);
        cbRegistrationShowHidePassword = findViewById(R.id.cbRegistrationShowHidePassword);


        btnRegistrationRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etRegistrationName.getText().toString().isEmpty()) {
                    etRegistrationName.setError("Enter Name");
                }

                else if (etRegistrationMobNumber.getText().toString().isEmpty()) {
                    etRegistrationMobNumber.setError("Enter Mobile Number");
                }

                else if (etRegistrationMobNumber.getText().toString().length() != 10) {
                    etRegistrationMobNumber.setError("Mobile Number Length Must be 10");
                }

                else if (etRegistrationEmailId.getText().toString().isEmpty()) {
                    etRegistrationEmailId.setError("Enter Email Id");
                }

                else if (!etRegistrationEmailId.getText().toString().contains("@")
                        || !etRegistrationEmailId.getText().toString().contains(".com")) {
                    etRegistrationEmailId.setError("Enter Valid Email Id");
                }

                else if (etRegistrationUsername.getText().toString().isEmpty()) {
                    etRegistrationUsername.setError("Enter Username");
                }

                else if (etRegistrationUsername.getText().toString().length() < 8) {
                    etRegistrationUsername.setError("UserName Length Must be more than 8");
                }

                else if (!etRegistrationUsername.getText().toString().matches(".*[A-Z].*")) {
                    etRegistrationUsername.setError("UserName Must contain 1 UpperCase");
                }

                else if (!etRegistrationUsername.getText().toString().matches(".*[a-z].*")) {
                    etRegistrationUsername.setError("UserName Must contain 1 LowerCase");
                }

                else if (!etRegistrationUsername.getText().toString().matches(".*[0-9].*")) {
                    etRegistrationUsername.setError("UserName Must contain 1 Number");
                }

                else if (!etRegistrationUsername.getText().toString().matches(".*[@#$%^&+=!].*")) {
                    etRegistrationUsername.setError("UserName Must contain 1 Special Symbol");
                }

                else if (etRegistrationPassword.getText().toString().isEmpty()) {
                    etRegistrationPassword.setError("Enter Password");
                }

                else if (etRegistrationPassword.getText().toString().length() < 8) {
                    etRegistrationPassword.setError("Password Length Must be more than 8");
                }

                else if (!etRegistrationPassword.getText().toString().matches(".*[A-Z].*")) {
                    etRegistrationPassword.setError("Password Must contain 1 UpperCase");
                }

                else if (!etRegistrationPassword.getText().toString().matches(".*[a-z].*")) {
                    etRegistrationPassword.setError("Password Must contain 1 LowerCase");
                }

                else if (!etRegistrationPassword.getText().toString().matches(".*[0-9].*")) {
                    etRegistrationPassword.setError("Password Must contain 1 Number");
                }

                else if (!etRegistrationPassword.getText().toString().matches(".*[@#$%^&+=!].*")) {
                    etRegistrationPassword.setError("Password Must contain 1 Special Symbol");
                }

                else if (etRegistrationConformPassword.getText().toString().isEmpty()) {
                    etRegistrationConformPassword.setError("Enter Conform Password");
                }

                else if (!etRegistrationPassword.getText().toString().equals(etRegistrationConformPassword.getText().toString()))
                {
                    etRegistrationConformPassword.setError("Password and Confirm Password must be same");
                }

                else {
                    Toast.makeText(RegistrationActivity.this,
                            "Registration Successfull",
                            Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        cbRegistrationShowHidePassword.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            // Show Password
                            etRegistrationPassword.setTransformationMethod(
                                    HideReturnsTransformationMethod.getInstance());

                            etRegistrationConformPassword.setTransformationMethod(
                                    HideReturnsTransformationMethod.getInstance());
                        }
                        else {
                            // Hide Password
                            etRegistrationPassword.setTransformationMethod(
                                    PasswordTransformationMethod.getInstance());

                            etRegistrationConformPassword.setTransformationMethod(
                                    PasswordTransformationMethod.getInstance());
                        }

                        // Move cursor to the end
                        etRegistrationPassword.setSelection(
                                etRegistrationPassword.getText().length());

                        etRegistrationConformPassword.setSelection(
                                etRegistrationConformPassword.getText().length());
                    }
                });

        Toast.makeText(RegistrationActivity.this,
                "Registration Page",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
