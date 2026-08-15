package com.harsh.visionx_signbridge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.harsh.visionx_signbridge.Fragment.BridgeFragment;
import com.harsh.visionx_signbridge.Fragment.HomeFragment;
import com.harsh.visionx_signbridge.Fragment.SignFragment;
import com.harsh.visionx_signbridge.Fragment.favroiteFragment;
import com.harsh.visionx_signbridge.Fragment.LanguagesFragment;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    boolean doubleTap = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    FrameLayout homeFrameLayout;
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    SignFragment signFragment = new SignFragment();

    BridgeFragment bridgeFragment = new BridgeFragment();
    favroiteFragment favoriteFragment = new favroiteFragment();
    LanguagesFragment languagesFragment = new LanguagesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor = preferences.edit();

        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            welcome();
        }

        homeFrameLayout = findViewById(R.id.homeFrameLayout);
        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
        
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.MenuBottomHome);
    }

    private void welcome() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Sign Bridge");
        ad.setMessage("Welcome to Sign Bridge App");
        ad.setPositiveButton("Let's Start", (dialog, which) -> dialog.dismiss());
        ad.show();
        editor.putBoolean("isFirstTime", false).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.MenuSetting) {
            Intent i = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.MenuAboutUs) {
            Intent i = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.MenuProfile)
        {
            Intent i = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.MenuContactUs)
        {
         Intent i = new Intent(HomeActivity.this,ContactUsActivity.class);
         startActivity(i);
        }
        else if (item.getItemId() == R.id.MenuLogout) {
            logout();
        }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Logout");
        ad.setMessage("Are you sure you want to Logout?");
        ad.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
        ad.setNegativeButton("Logout", (dialog, which) -> {
            LoginActivity.logout(HomeActivity.this);
        }).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.MenuBottomHome) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFrameLayout, homeFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.communication) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFrameLayout, signFragment)
                    .commit();
            return true;
        }
        else if (item.getItemId() == R.id.bridge) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFrameLayout, bridgeFragment)
                    .commit();
            return true;
        }
        else if (item.getItemId() == R.id.MenuBottomFavorites) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFrameLayout, favoriteFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.MenuBottomLanguages) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFrameLayout, languagesFragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleTap) {
            finishAffinity();
        } else {
            Toast.makeText(HomeActivity.this, "Double tap to exit app", Toast.LENGTH_SHORT).show();
            doubleTap = true;
            new Handler().postDelayed(() -> doubleTap = false, 2000);
        }
    }
}
