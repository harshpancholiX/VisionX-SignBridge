package com.harsh.visionx_signbridge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Forgot_Password_Activity extends AppCompatActivity {

    private TextInputEditText etForgotPasswordUsername;
    private TextInputEditText etForgotPasswordNewPassword;
    private TextInputEditText etForgotPasswordConfirmNewPassword;
    private MaterialButton btnForgotPassword;
    private TextView tvBackToLogin;
    private AsyncHttpClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etForgotPasswordUsername = findViewById(R.id.etForgotPasswordUsername);
        etForgotPasswordNewPassword = findViewById(R.id.etForgotPasswordNewPassword);
        etForgotPasswordConfirmNewPassword = findViewById(R.id.etForgotPasswordConfirmNewPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        client = new AsyncHttpClient();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating password...");
        progressDialog.setCancelable(false);

        btnForgotPassword.setOnClickListener(v -> resetPassword());

        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Forgot_Password_Activity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void resetPassword() {
        if (etForgotPasswordUsername.getText() == null || etForgotPasswordNewPassword.getText() == null || etForgotPasswordConfirmNewPassword.getText() == null) {
            return;
        }

        String username = etForgotPasswordUsername.getText().toString().trim();
        String newPassword = etForgotPasswordNewPassword.getText().toString();
        String confirmPassword = etForgotPasswordConfirmNewPassword.getText().toString();

        if (username.isEmpty()) {
            etForgotPasswordUsername.setError("Please enter username or email");
            etForgotPasswordUsername.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            etForgotPasswordNewPassword.setError("Please enter new password");
            etForgotPasswordNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 8) {
            etForgotPasswordNewPassword.setError("Password must be at least 8 characters");
            etForgotPasswordNewPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etForgotPasswordConfirmNewPassword.setError("Please confirm your password");
            etForgotPasswordConfirmNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etForgotPasswordConfirmNewPassword.setError("Passwords do not match");
            etForgotPasswordConfirmNewPassword.requestFocus();
            return;
        }

        setLoading(true);

        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", newPassword);

        client.post(ApiConfig.RESET_PASSWORD_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                setLoading(false);
                boolean success = response.optBoolean("success", false);
                String message = response.optString("message", "Something went wrong");

                Toast.makeText(Forgot_Password_Activity.this, message, Toast.LENGTH_SHORT).show();

                if (success) {
                    Intent intent = new Intent(Forgot_Password_Activity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                setLoading(false);
                String message = "Connection error";
                if (errorResponse != null) {
                    message = errorResponse.optString("message", message);
                }
                Toast.makeText(Forgot_Password_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
        btnForgotPassword.setEnabled(!loading);
    }
}
