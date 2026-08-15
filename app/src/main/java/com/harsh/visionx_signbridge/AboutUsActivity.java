package com.harsh.visionx_signbridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    TextView navHome, navLearn, navTranslate, navFavorites, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        navHome = findViewById(R.id.navHome);
        navLearn = findViewById(R.id.navLearn);
        navTranslate = findViewById(R.id.navTranslate);
        navFavorites = findViewById(R.id.navFavorites);
        navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(AboutUsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        navLearn.setOnClickListener(v -> {
            Intent intent = new Intent(AboutUsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        navTranslate.setOnClickListener(v -> {
            Intent intent = new Intent(AboutUsActivity.this, TranslateActivity.class);
            startActivity(intent);
        });

        navFavorites.setOnClickListener(v -> {
            // favroiteFragment is a Fragment, not an Activity. 
            // Navigation to fragments usually happens via HomeActivity.
            Intent intent = new Intent(AboutUsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AboutUsActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
