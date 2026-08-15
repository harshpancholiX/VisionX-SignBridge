package com.harsh.visionx_signbridge.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.harsh.visionx_signbridge.R;
import com.harsh.visionx_signbridge.SignAdapter;
import com.harsh.visionx_signbridge.SignModel;

import java.util.ArrayList;
import java.util.List;

public class SignFragment extends Fragment {

    private RecyclerView rvSigns;
    private SignAdapter signAdapter;
    private List<SignModel> signList;
    private List<SignModel> filteredList;
    private EditText etSearchSigns;

    private MaterialButton btnAll, btnGreetings, btnHealthcare, btnEmergency, btnEducation, btnDailyLife;
    private String selectedCategory = "All";
    private String searchQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);

        initializeViews(view);
        loadSigns();

        filteredList = new ArrayList<>(signList);
        signAdapter = new SignAdapter(requireContext(), filteredList);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        rvSigns.setLayoutManager(layoutManager);
        rvSigns.setAdapter(signAdapter);

        setupSearch();
        setupCategoryListeners();
        updateCategoryButtons();

        return view;
    }

    private void initializeViews(View view) {
        rvSigns = view.findViewById(R.id.rvSigns);
        etSearchSigns = view.findViewById(R.id.etSearchSigns);

        btnAll = view.findViewById(R.id.btnAll);
        btnGreetings = view.findViewById(R.id.btnGreetings);
        btnHealthcare = view.findViewById(R.id.btnHealthcare);
        btnEmergency = view.findViewById(R.id.btnEmergency);
        btnEducation = view.findViewById(R.id.btnEducation);
        btnDailyLife = view.findViewById(R.id.btnDailyLife);
    }

    private void setupSearch() {
        etSearchSigns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                filterSigns();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupCategoryListeners() {
        View.OnClickListener categoryClickListener = v -> {
            MaterialButton btn = (MaterialButton) v;
            selectedCategory = btn.getText().toString();
            updateCategoryButtons();
            filterSigns();
        };

        btnAll.setOnClickListener(categoryClickListener);
        btnGreetings.setOnClickListener(categoryClickListener);
        btnHealthcare.setOnClickListener(categoryClickListener);
        btnEmergency.setOnClickListener(categoryClickListener);
        btnEducation.setOnClickListener(categoryClickListener);
        btnDailyLife.setOnClickListener(categoryClickListener);
    }

    private void updateCategoryButtons() {
        MaterialButton[] buttons = {btnAll, btnGreetings, btnHealthcare, btnEmergency, btnEducation, btnDailyLife};
        int blueColor = ContextCompat.getColor(requireContext(), R.color.blue);
        int whiteColor = Color.WHITE;

        for (MaterialButton btn : buttons) {
            if (btn.getText().toString().equalsIgnoreCase(selectedCategory)) {
                btn.setBackgroundTintList(ColorStateList.valueOf(blueColor));
                btn.setTextColor(whiteColor);
                btn.setStrokeWidth(0);
            } else {
                btn.setBackgroundTintList(ColorStateList.valueOf(whiteColor));
                btn.setTextColor(blueColor);
                btn.setStrokeColor(ColorStateList.valueOf(blueColor));
                btn.setStrokeWidth(dpToPx(1));
            }
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    private void filterSigns() {
        filteredList.clear();
        String query = searchQuery.trim().toLowerCase();

        for (SignModel sign : signList) {
            boolean categoryMatches = selectedCategory.equalsIgnoreCase("All")
                    || sign.getCategory().equalsIgnoreCase(selectedCategory);

            boolean searchMatches = query.isEmpty()
                    || sign.getSignName().toLowerCase().contains(query)
                    || sign.getHindiName().toLowerCase().contains(query);

            if (categoryMatches && searchMatches) {
                filteredList.add(sign);
            }
        }
        signAdapter.updateList(filteredList);
    }

    private void loadSigns() {
        signList = new ArrayList<>();

        // Greetings
        signList.add(new SignModel("Hello", "नमस्ते", "Greetings", R.raw.hand_sign_animation));
        signList.add(new SignModel("Thank You", "धन्यवाद", "Greetings", R.drawable.thankyou_image));
        signList.add(new SignModel("Sorry", "माफ़ कीजिए", "Greetings", R.drawable.ic_launcher_foreground));
        signList.add(new SignModel("Goodbye", "अलविदा", "Greetings", R.drawable.ic_launcher_foreground));

        // Healthcare
        signList.add(new SignModel("Doctor", "डॉक्टर", "Healthcare", R.drawable.doctor));
        signList.add(new SignModel("Hospital", "अस्पताल", "Healthcare", R.drawable.ic_launcher_foreground));
        signList.add(new SignModel("Medicine", "दवा", "Healthcare", R.drawable.ic_launcher_foreground));

        // Emergency
        signList.add(new SignModel("Help", "मदद", "Emergency", R.drawable.help));
        signList.add(new SignModel("Police", "पुलिस", "Emergency", R.drawable.ic_launcher_foreground));
        signList.add(new SignModel("Accident", "दुर्घटना", "Emergency", R.drawable.ic_launcher_foreground));

        // Education
        signList.add(new SignModel("Teacher", "शिक्षक", "Education", R.drawable.ic_launcher_foreground));
        signList.add(new SignModel("Student", "छात्र", "Education", R.drawable.ic_launcher_foreground));
        signList.add(new SignModel("Exam", "परीक्षा", "Education", R.drawable.ic_launcher_foreground));

        // Daily Life
        signList.add(new SignModel("Water", "पानी", "Daily Life", R.drawable.water_image));
        signList.add(new SignModel("Food", "खाना", "Daily Life", R.drawable.food));
        signList.add(new SignModel("Home", "घर", "Daily Life", R.drawable.ic_launcher_foreground));
    }

    @Override
    public void onDestroy() {
        if (signAdapter != null) {
            signAdapter.releaseTTS();
        }
        super.onDestroy();
    }
}
