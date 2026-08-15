package com.harsh.visionx_signbridge;

// Import required classes
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.Objects;

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
            String username = etLoginUsername.getText().toString();
            String password = etLoginPassword.getText().toString();

            if (username.isEmpty()) {
                etLoginUsername.setError("Please Enter Your Username");
            } else if (password.isEmpty()) {
                etLoginPassword.setError("Please Enter Your Password");
            } else {
                // Check against registration data saved in SharedPreferences
                String savedUsername = preferences.getString("username", "");
                String savedPassword = preferences.getString("password", "");

                if (Objects.equals(username, savedUsername) && Objects.equals(password, savedPassword)) {
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", username);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to HomeActivity which opens HomeFragment
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else if (savedUsername.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "No user found. Please register first.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLoginNewUser.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
        });
        btnLoginForget.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(i);
            finish();
        });

    }

    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SignBridgePrefs", MODE_PRIVATE);
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