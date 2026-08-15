package com.harsh.visionx_signbridge.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.harsh.visionx_signbridge.R;
import com.harsh.visionx_signbridge.TranslateActivity;
import com.harsh.visionx_signbridge.ProfileActivity;

public class HomeFragment extends Fragment {

    TextView txtGreeting;
    TextView txtUsername;
    TextView txtProgress;
    TextView txtSignsLearned;
    TextView txtRecentOne;
    TextView txtRecentTwo;

    Button btnStartTranslate;

    ImageView imgProfile;

    View cardTranslate;
    View cardSignToText;
    View cardTextToSign;
    View cardLearn;
    View cardPractice;

    ProgressBar progressBar;

    SharedPreferences preferences;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );

        preferences = requireActivity().getSharedPreferences(
                "SignBridgePrefs",
                Context.MODE_PRIVATE
        );

        initializeViews(view);

        loadHomeData();

        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {

        txtGreeting =
                view.findViewById(R.id.txtGreeting);




        txtRecentOne =
                view.findViewById(R.id.txtRecentOne);

        txtRecentTwo =
                view.findViewById(R.id.txtRecentTwo);

        btnStartTranslate =
                view.findViewById(R.id.btnStartTranslate);

        imgProfile =
                view.findViewById(R.id.imgProfile);

        cardTranslate =
                view.findViewById(R.id.cardTranslate);

        cardSignToText =
                view.findViewById(R.id.cardSignToText);

        cardTextToSign =
                view.findViewById(R.id.cardTextToSign);

        cardLearn =
                view.findViewById(R.id.cardLearn);

        cardPractice =
                view.findViewById(R.id.cardPractice);


      
    }

    private void loadHomeData() {

        String username =
                preferences.getString(
                        "username",
                        "User"
                );

        int progress =
                preferences.getInt(
                        "progress",
                        68
                );

        int signsLearned =
                preferences.getInt(
                        "signsLearned",
                        12
                );

        String recentOne =
                preferences.getString(
                        "recentOne",
                        "Hello"
                );

        String recentTwo =
                preferences.getString(
                        "recentTwo",
                        "Thank You"
                );

        if (txtUsername != null) txtUsername.setText(username);

        if (progressBar != null) progressBar.setProgress(progress);

        if (txtProgress != null) {
            txtProgress.setText(
                    progress + "%"
            );
        }

        if (txtSignsLearned != null) {
            txtSignsLearned.setText(
                    signsLearned + " signs learned"
            );
        }

        if (txtRecentOne != null) {
            txtRecentOne.setText(
                    "🤟   " + recentOne
            );
        }

        if (txtRecentTwo != null) {
            txtRecentTwo.setText(
                    "🤟   " + recentTwo
            );
        }
    }

    private void setupClickListeners() {

        if (btnStartTranslate != null) {
            btnStartTranslate.setOnClickListener(v -> {
                openTranslate("sign_to_text");
            });
        }

        if (cardTranslate != null) {
            cardTranslate.setOnClickListener(v -> {
                openTranslate("sign_to_text");
            });
        }

        if (cardSignToText != null) {
            cardSignToText.setOnClickListener(v -> {
                openTranslate("sign_to_text");
            });
        }

        if (cardTextToSign != null) {
            cardTextToSign.setOnClickListener(v -> {
                openTranslate("text_to_sign");
            });
        }

        if (cardLearn != null) {
            cardLearn.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Learning mode coming soon", Toast.LENGTH_SHORT).show();
            });
        }

        if (cardPractice != null) {
            cardPractice.setOnClickListener(v -> {
                Toast.makeText(
                        requireContext(),
                        "Practice mode coming soon",
                        Toast.LENGTH_SHORT
                ).show();
            });
        }

        if (imgProfile != null) {
            imgProfile.setOnClickListener(v -> {
                openProfile();
            });
        }
    }

    private void openTranslate(String mode) {

        Intent intent = new Intent(
                requireActivity(),
                TranslateActivity.class
        );

        intent.putExtra(
                "mode",
                mode
        );

        startActivity(intent);
    }


    private void openProfile() {

        Intent intent = new Intent(
                requireActivity(),
                ProfileActivity.class
        );

        startActivity(intent);
    }

    public void saveUsername(String username) {

        preferences.edit()
                .putString(
                        "username",
                        username
                )
                .apply();

        loadHomeData();
    }

    public void saveProgress(int progress) {

        int validatedProgress = progress;
        if (validatedProgress < 0) {
            validatedProgress = 0;
        }

        if (validatedProgress > 100) {
            validatedProgress = 100;
        }

        preferences.edit()
                .putInt(
                        "progress",
                        validatedProgress
                )
                .apply();

        loadHomeData();
    }

    public void saveSignsLearned(int count) {

        preferences.edit()
                .putInt(
                        "signsLearned",
                        count
                )
                .apply();

        loadHomeData();
    }

    public void addLearnedSign() {

        int currentSignsLearned =
                preferences.getInt(
                        "signsLearned",
                        12
                );

        currentSignsLearned++;

        int progress =
                preferences.getInt(
                        "progress",
                        68
                );

        int newProgress = progress + 2;

        if (newProgress > 100) {
            newProgress = 100;
        }

        preferences.edit()
                .putInt(
                        "signsLearned",
                        currentSignsLearned
                )
                .putInt(
                        "progress",
                        newProgress
                )
                .apply();

        loadHomeData();
    }

    public void saveTranslation(
            String translatedText) {

        String recentOne =
                preferences.getString(
                        "recentOne",
                        "Hello"
                );

        preferences.edit()
                .putString(
                        "recentTwo",
                        recentOne
                )
                .putString(
                        "recentOne",
                        translatedText
                )
                .apply();

        loadHomeData();
    }

    public String getUsername() {

        return preferences.getString(
                "username",
                "User"
        );
    }

    public void clearLocalData() {

        preferences.edit()
                .clear()
                .apply();

        loadHomeData();
    }

    @Override
    public void onResume() {

        super.onResume();

        if (preferences != null) {
            loadHomeData();
        }
    }
}
