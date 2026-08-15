package com.harsh.visionx_signbridge;

import android.content.DialogInterface;
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

import com.harsh.visionx_signbridge.Fragment.HomeFragment;
import com.harsh.visionx_signbridge.Fragment.SignFragment;
import com.harsh.visionx_signbridge.Fragment.favroiteFragment;
import com.harsh.visionx_signbridge.Fragment.LanguagesFragment;


public class HomeActivity extends AppCompatActivity
        implements NavigationBarView.OnItemSelectedListener {


    // Shared Preferences
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    // Main Views
    FrameLayout homeFrameLayout;
    BottomNavigationView homeBottomNavigationView;


    // Fragments
    HomeFragment homeFragment = new HomeFragment();
    SignFragment signFragment = new SignFragment();
    favroiteFragment favoriteFragment = new favroiteFragment();
    LanguagesFragment languagesFragment = new LanguagesFragment();


    // Double Back
    boolean doubleTap = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        // Find Views
        homeFrameLayout =
                findViewById(R.id.homeFrameLayout);

        homeBottomNavigationView =
                findViewById(R.id.homeBottomNavigationView);


        // Bottom Navigation Listener
        homeBottomNavigationView
                .setOnItemSelectedListener(this);


        // Default Fragment
        homeBottomNavigationView
                .setSelectedItemId(R.id.MenuBottomHome);


        // Shared Preferences
        preferences =
                PreferenceManager
                        .getDefaultSharedPreferences(
                                HomeActivity.this
                        );

        editor = preferences.edit();


        // First Time Welcome
        boolean isFirstTime =
                preferences.getBoolean(
                        "isFirstTime",
                        true
                );


        if (isFirstTime) {

            welcome();

            editor.putBoolean(
                    "isFirstTime",
                    false
            );

            editor.commit();
        }
    }




    private void welcome() {

        AlertDialog.Builder ad =
                new AlertDialog.Builder(
                        HomeActivity.this
                );


        ad.setTitle(
                "Welcome to SignBridge"
        );


        ad.setMessage(
                "Breaking communication barriers, " +
                        "one sign at a time."
        );


        ad.setPositiveButton(
                "Let's Start",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        dialog.dismiss();
                    }
                }
        );


        ad.show();
    }


    // ==========================================
    // BOTTOM NAVIGATION
    // ==========================================

    @Override
    public boolean onNavigationItemSelected(
            @NonNull MenuItem item) {


        // HOME
        if (item.getItemId()
                == R.id.MenuBottomHome) {


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.homeFrameLayout,
                            homeFragment
                    )
                    .commit();


            return true;
        }


        // SIGNS
        else if (item.getItemId()
                == R.id.communication) {


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.homeFrameLayout,
                            signFragment
                    )
                    .commit();


            return true;
        }


        // FAVORITES
        else if (item.getItemId()
                == R.id.MenuBottomFavorites) {


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.homeFrameLayout,
                            favoriteFragment
                    )
                    .commit();


            return true;
        }



        else if (item.getItemId()
                == R.id.MenuBottomLanguages) {


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.homeFrameLayout,
                            languagesFragment
                    )
                    .commit();


            return true;
        }


        return false;
    }


    // ==========================================
    // TOP MENU
    // ==========================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater =
                getMenuInflater();


        inflater.inflate(
                R.menu.menus,
                menu
        );


        return true;
    }


    // ==========================================
    // TOP MENU ACTIONS
    // ==========================================

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {


        // PROFILE






        // SETTINGS
         if (item.getItemId()
                == R.id.MenuSetting) {


            Intent intent =
                    new Intent(
                            HomeActivity.this,
                           SettingActivity.class
                    );


            startActivity(intent);
        }


        // ABOUT
        else if (item.getItemId()
                == R.id.MenuAboutUs) {


            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            AboutUsActivity.class
                    );


            startActivity(intent);
        }


        // LOGOUT
        else if (item.getItemId()
                == R.id.MenuLogout) {


            logout();
        }


        return true;
    }


    // ==========================================
    // LOGOUT
    // ==========================================

    private void logout() {


        AlertDialog.Builder ad =
                new AlertDialog.Builder(
                        HomeActivity.this
                );


        ad.setTitle(
                "Logout"
        );


        ad.setMessage(
                "Are you sure you want to logout?"
        );


        // CANCEL
        ad.setPositiveButton(
                "Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        dialog.dismiss();
                    }
                }
        );


        // LOGOUT
        ad.setNegativeButton(
                "Logout",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {


                        editor.putBoolean(
                                "isLogin",
                                false
                        );


                        editor.commit();


                        Intent intent =
                                new Intent(
                                        HomeActivity.this,
                                        LoginActivity.class
                                );


                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );


                        startActivity(intent);

                        finishAffinity();
                    }
                }
        );


        ad.show();
    }


    // ==========================================
    // DOUBLE TAP TO EXIT
    // ==========================================

    @Override
    public void onBackPressed() {


        if (doubleTap) {

            finishAffinity();

        } else {


            Toast.makeText(
                    HomeActivity.this,
                    "Double Tap To Exit",
                    Toast.LENGTH_SHORT
            ).show();


            doubleTap = true;


            Handler handler =
                    new Handler();


            handler.postDelayed(
                    new Runnable() {

                        @Override
                        public void run() {

                            doubleTap = false;
                        }

                    },
                    3000
            );
        }
    }
}