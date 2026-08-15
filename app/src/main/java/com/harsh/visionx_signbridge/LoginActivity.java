package com.harsh.visionx_signbridge;

// Import required classes
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.harsh.visionx_signbridge.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    // Declare variables
    LottieAnimationView lavLoginlottie;
    EditText etLoginUsername, etLoginPassword;
    CheckBox cbLoginShowHidePassword;
    Button btnLoginLogin,btnLoginForget;
    TextView tvLoginNewUser;
    CardView cvLoginCard;
    //temp database store
    SharedPreferences preferences;
    //temp database for put or edit
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Called when activity is created
        super.onCreate(savedInstanceState);

        // Connect Java file with XML layout
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("SignBridgePrefs", MODE_PRIVATE);
        editor = preferences.edit();

        // Check persistent login status
        if (preferences.getBoolean("isLoggedIn", false)) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.putExtra("name", preferences.getString("name", ""));
            i.putExtra("mobileno", preferences.getString("mobileno", ""));
            i.putExtra("emailid", preferences.getString("emailid", ""));
            i.putExtra("username", preferences.getString("username", ""));
            i.putExtra("password", preferences.getString("password", ""));
            startActivity(i);
            finish();
        }

        // Find Lottie animation from XML
        lavLoginlottie = findViewById(R.id.lavLoginlottie);

        // Make animation repeat continuously
        lavLoginlottie.setRepeatCount(LottieDrawable.INFINITE);

        // Start animation
        lavLoginlottie.playAnimation();

        // Connect EditTexts, CheckBox, Button and TextView with XML IDs
        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        cbLoginShowHidePassword = findViewById(R.id.cbLoginShowHidePassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        tvLoginNewUser = findViewById(R.id.tvLoginNewUser);
        cvLoginCard = findViewById(R.id.cvLoginCard);
        btnLoginForget = findViewById(R.id.btnLoginForget);
        // Load animations
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Apply animations
        lavLoginlottie.startAnimation(zoomIn);
        cvLoginCard.startAnimation(bounce);

        // Perform action when CheckBox is checked or unchecked
        cbLoginShowHidePassword.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    // If CheckBox is checked, show password
                    if (isChecked) {
                        etLoginPassword.setTransformationMethod(
                                HideReturnsTransformationMethod.getInstance());
                    }
                    // Otherwise hide password
                    else {
                        etLoginPassword.setTransformationMethod(
                                PasswordTransformationMethod.getInstance());
                    }
                });

        // Perform action when Login button is clicked
        btnLoginLogin.setOnClickListener(v -> {
            // Check whether username field is empty
            if (etLoginUsername.getText().toString().isEmpty()) {
                etLoginUsername.setError("Please Enter Your Username");
            }

            // Check username length
            else if (etLoginUsername.getText().toString().length() < 8) {
                etLoginUsername.setError("UserName Must be More Than 8");
            }

            // Validate username using Regular Expression
            else if (!etLoginUsername.getText().toString()
                    .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")) {
                etLoginUsername.setError(
                        "Username must contain one uppercase, one lowercase, one number and one special symbol");
            }

            // Check whether password field is empty
            else if (etLoginPassword.getText().toString().isEmpty()) {
                etLoginPassword.setError("Please Enter Your Password");
            }

            // Check password length
            else if (etLoginPassword.getText().toString().length() < 8) {
                etLoginPassword.setError("Password Must be More Than 8");
            }

            // Validate password using Regular Expression
            else if (!etLoginPassword.getText().toString()
                    .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")) {
                etLoginPassword.setError(
                        "Password must contain one uppercase, one lowercase, one number and one special symbol");
            }

            // If all conditions are satisfied
            else {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                loginUser();
            }
        });

        tvLoginNewUser.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
            finishAffinity();
        });
        btnLoginForget.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(i);
            finish();
        });

    }
    private void loginUser()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",etLoginUsername.getText().toString());
        params.put("password",etLoginPassword.getText().toString());

        client.post(Urls.loginUserURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();

                try {

                    String status = response.getString("success");
                    String message = response.getString("message");


                    if(status.equals("1"))
                    {
                        // Save login state and user details to solve the re-login problem
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", etLoginUsername.getText().toString());
                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            try {
                                editor.putString(key, response.getString(key));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        editor.apply();

                        // Sync with MyProfileActivity's SharedPreferences
                        SharedPreferences profilePrefs = getSharedPreferences("ProfileData", MODE_PRIVATE);
                        SharedPreferences.Editor profileEditor = profilePrefs.edit();
                        try {
                            profileEditor.putString("name", response.getString("name"));
                            profileEditor.putString("email", response.getString("emailid"));
                            profileEditor.putString("mobile", response.getString("mobileno"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        profileEditor.apply();

                        String name = response.getString("name");
                        String mobileno = response.getString("mobileno");
                        String emailid = response.getString("emailid");
                        String username = response.getString("username");
                        String password = response.getString("password");

                        String displayData = "Message: " + message +
                                "\nName: " + name +
                                "\nMobile: " + mobileno +
                                "\nEmail: " + emailid +
                                "\nUsername: " + username +
                                "\nPassword: " + password;

                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Login Success")
                                .setMessage(displayData)
                                .setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("mobileno", mobileno);
                                    intent.putExtra("emailid", emailid);
                                    intent.putExtra("username", username);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    finish();
                                })
                                .setCancelable(false)
                                .show();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,
                                message,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,
                        "Server Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SignBridgePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent i = new Intent(context, LoginActivity.class);
        // Clear activity stack
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}