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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class ForgetPasswordActivity extends AppCompatActivity {

    EditText etusername, etNewPassword, etConfirmNewPassword;
    Button btnForgetPassword;
    ProgressDialog progressDialog;
    CheckBox cbForgetShowHidePassword;
    android.content.SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);

        etusername = findViewById(R.id.etForgetPasswordUsername);
        etNewPassword = findViewById(R.id.etForgetPasswordNewPassword);
        etConfirmNewPassword = findViewById(R.id.etForgetPasswordConfirmNewPassword);
        btnForgetPassword = findViewById(R.id.btnForgetPaswordForgetPasswordBtn);
        cbForgetShowHidePassword = findViewById(R.id.cbForgetShowHidePassword);


        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etusername.getText().toString().isEmpty()) {
                    etusername.setError("Please Enter Username");
                } else if (etusername.getText().toString().length() < 8) {
                    etusername.setError("Username Must be Greater Than 8");
                } else if (etNewPassword.getText().toString().isEmpty()) {
                    etNewPassword.setError("Please Enter New Password");
                } else if (etNewPassword.getText().toString().length() < 8) {
                    etNewPassword.setError("New Password Must be Greater Than 8");
                } else if (etConfirmNewPassword.getText().toString().isEmpty()) {
                    etConfirmNewPassword.setError("Please Enter Confirm Password");
                } else if (etConfirmNewPassword.getText().toString().length() < 8) {
                    etConfirmNewPassword.setError("Confirm Password Must Be Greater Than 8");
                } else if (!etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString())) {
                    etConfirmNewPassword.setError("Password and ConfirmPassword Does Not Match");
                } else {
                    String username = etusername.getText().toString();
                    String savedUsername = preferences.getString("username", "");

                    if (!username.equals(savedUsername)) {
                        etusername.setError("Username not found");
                    } else {
                        android.content.SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("password", etNewPassword.getText().toString());
                        editor.apply();

                        Toast.makeText(ForgetPasswordActivity.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
        cbForgetShowHidePassword.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            // Show Password
                            etNewPassword.setTransformationMethod(
                                    HideReturnsTransformationMethod.getInstance());

                            etConfirmNewPassword.setTransformationMethod(
                                    HideReturnsTransformationMethod.getInstance());
                        } else {
                            // Hide Password
                            etNewPassword.setTransformationMethod(
                                    PasswordTransformationMethod.getInstance());

                            etConfirmNewPassword.setTransformationMethod(
                                    PasswordTransformationMethod.getInstance());
                        }


                        etNewPassword.setSelection(
                                etNewPassword.getText().length());

                        etConfirmNewPassword.setSelection(
                                etConfirmNewPassword.getText().length());
                    }
                });


    }
}