package com.harsh.visionx_signbridge.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harsh.visionx_signbridge.Adapter.LanguageAdapter;
import com.harsh.visionx_signbridge.Model.LanguageModel;
import com.harsh.visionx_signbridge.R;

import java.util.ArrayList;

public class LanguagesFragment extends Fragment {

    private RecyclerView rvLanguages;

    private EditText etLanguageSearch;

    private TextView tvLanguageCount;

    private LinearLayout emptyState;

    private LanguageAdapter adapter;

    private ArrayList<LanguageModel> languageList;

    private ArrayList<LanguageModel> filteredList;

    private SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_languages,
                container,
                false
        );

        initializeViews(view);

        setupRecyclerView();

        loadLanguages();

        setupSearch();

        animateScreen(view);

        return view;
    }


    private void initializeViews(View view) {

        rvLanguages =
                view.findViewById(R.id.rvLanguages);

        etLanguageSearch =
                view.findViewById(R.id.etLanguageSearch);

        tvLanguageCount =
                view.findViewById(R.id.tvLanguageCount);

        emptyState =
                view.findViewById(R.id.emptyState);

        preferences = requireContext()
                .getSharedPreferences(
                        "SignBridgePreferences",
                        Context.MODE_PRIVATE
                );
    }


    private void setupRecyclerView() {

        rvLanguages.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        rvLanguages.setHasFixedSize(false);

        rvLanguages.setNestedScrollingEnabled(false);
    }


    private void loadLanguages() {

        languageList = new ArrayList<>();

        // Indian Sign Language
        languageList.add(
                new LanguageModel(
                        1,
                        "Indian Sign Language",
                        "भारतीय सांकेतिक भाषा",
                        "ISL",
                        "Sign language used for communication across India.",
                        "https://en.wikipedia.org/wiki/Indian_Sign_Language",
                        R.drawable.language
                )
        );


        // American Sign Language
        languageList.add(
                new LanguageModel(
                        2,
                        "American Sign Language",
                        "American Sign Language",
                        "ASL",
                        "A visual language widely used in the United States.",
                        "https://en.wikipedia.org/wiki/American_Sign_Language",
                        R.drawable.language
                )
        );


        // British Sign Language
        languageList.add(
                new LanguageModel(
                        3,
                        "British Sign Language",
                        "British Sign Language",
                        "BSL",
                        "Sign language commonly used in the United Kingdom.",
                        "https://en.wikipedia.org/wiki/British_Sign_Language",
                        R.drawable.language
                )
        );


        // International Sign
        languageList.add(
                new LanguageModel(
                        4,
                        "International Sign",
                        "International Sign",
                        "IS",
                        "Used for communication between signers from different countries.",
                        "https://en.wikipedia.org/wiki/International_Sign",
                        R.drawable.language
                )
        );


        filteredList =
                new ArrayList<>(languageList);


        restoreSelectedLanguage();

        adapter = new LanguageAdapter(
                requireContext(),
                filteredList
        );

        rvLanguages.setAdapter(adapter);

        updateLanguageCount();
    }


    private void restoreSelectedLanguage() {

        int selectedId = preferences.getInt(
                "selected_language_id",
                -1
        );

        if (selectedId == -1) {
            return;
        }

        for (LanguageModel language : filteredList) {

            if (language.getId() == selectedId) {

                language.setSelected(true);

                break;
            }
        }
    }


    private void setupSearch() {

        etLanguageSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }


                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        filterLanguages(
                                s.toString()
                        );
                    }


                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }


    private void filterLanguages(String query) {

        filteredList.clear();

        String search =
                query.trim().toLowerCase();


        if (search.isEmpty()) {

            filteredList.addAll(languageList);

        } else {

            for (LanguageModel language : languageList) {

                if (
                        language.getName()
                                .toLowerCase()
                                .contains(search)

                                ||

                                language.getNativeName()
                                        .toLowerCase()
                                        .contains(search)

                                ||

                                language.getCode()
                                        .toLowerCase()
                                        .contains(search)
                ) {

                    filteredList.add(language);
                }
            }
        }


        adapter.notifyDataSetChanged();

        updateLanguageCount();

        updateEmptyState();
    }


    private void updateLanguageCount() {

        int count = filteredList.size();

        tvLanguageCount.setText(
                getResources().getQuantityString(
                        R.plurals.language_count,
                        count,
                        count
                )
        );
    }


    private void updateEmptyState() {

        if (filteredList.isEmpty()) {

            rvLanguages.setVisibility(View.GONE);

            emptyState.setVisibility(View.VISIBLE);

            fadeIn(emptyState);

        } else {

            emptyState.setVisibility(View.GONE);

            rvLanguages.setVisibility(View.VISIBLE);
        }
    }


    private void animateScreen(View view) {

        view.setAlpha(0f);

        view.animate()
                .alpha(1f)
                .setDuration(500)
                .start();
    }


    private void fadeIn(View view) {

        AlphaAnimation animation =
                new AlphaAnimation(
                        0f,
                        1f
                );

        animation.setDuration(300);

        view.startAnimation(animation);
    }
}