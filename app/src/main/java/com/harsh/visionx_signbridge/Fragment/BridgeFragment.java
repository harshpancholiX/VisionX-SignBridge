package com.harsh.visionx_signbridge.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import android.graphics.drawable.Drawable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.harsh.visionx_signbridge.BridgePhrase;
import com.harsh.visionx_signbridge.R;

import org.json.JSONObject;

import java.util.Locale;

public class BridgeFragment extends Fragment {

    private TextInputEditText etMessage;
    private TextView tvCharacterCount;
    private TextView tvPhrase;
    private TextView tvTranslation;
    private TextView tvOffline;

    private ImageView ivSign;
    private ImageButton btnFavorite;

    private MaterialButton btnGenerate;
    private MaterialButton btnSpeak;
    private MaterialButton btnShare;

    private MaterialCardView cardResult;
    private ProgressBar progressMedia;

    private Spinner spinnerLanguage;

    private RequestQueue requestQueue;

    private TextToSpeech textToSpeech;

    private BridgePhrase currentPhrase;

    private boolean isFavorite = false;

    // CHANGE THIS TO YOUR COMPUTER'S IP
    private static final String BASE_URL =
            "http://10.176.197.132/signbridgeAPI/";

    public BridgeFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_bridge,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        requestQueue = Volley.newRequestQueue(requireContext());

        setupLanguageSpinner();

        setupTextCounter();

        setupTextToSpeech();

        setupListeners();
    }

    private void initializeViews(View view) {

        etMessage = view.findViewById(R.id.etBridgeMessage);

        tvCharacterCount =
                view.findViewById(R.id.tvBridgeCharacterCount);

        tvPhrase =
                view.findViewById(R.id.tvBridgePhrase);

        tvTranslation =
                view.findViewById(R.id.tvBridgeTranslation);

        tvOffline =
                view.findViewById(R.id.tvBridgeOffline);

        ivSign =
                view.findViewById(R.id.ivBridgeSign);

        btnFavorite =
                view.findViewById(R.id.btnBridgeFavorite);

        btnGenerate =
                view.findViewById(R.id.btnBridgeGenerate);

        btnSpeak =
                view.findViewById(R.id.btnBridgeSpeak);

        btnShare =
                view.findViewById(R.id.btnBridgeShare);

        cardResult =
                view.findViewById(R.id.cardBridgeResult);

        progressMedia =
                view.findViewById(R.id.progressBridgeMedia);

        spinnerLanguage =
                view.findViewById(R.id.spinnerBridgeLanguage);
    }

    private void setupLanguageSpinner() {

        String[] languages = {
                "English",
                "Hindi",
                "Marathi"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.spinner_item,
                        languages
                );

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerLanguage.setAdapter(adapter);
    }

    private void setupTextCounter() {

        etMessage.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        tvCharacterCount.setText(
                                s.length() + "/120"
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );
    }

    private void setupTextToSpeech() {

        textToSpeech = new TextToSpeech(
                requireContext(),
                status -> {

                    if (status == TextToSpeech.SUCCESS) {

                        textToSpeech.setLanguage(
                                Locale.ENGLISH
                        );

                        textToSpeech.setSpeechRate(0.9f);
                    }
                }
        );
    }

    private void setupListeners() {

        btnGenerate.setOnClickListener(v -> {

            String message =
                    etMessage.getText()
                            .toString()
                            .trim();

            if (message.isEmpty()) {

                etMessage.setError(
                        "Enter a phrase first"
                );

                return;
            }

            searchPhrase(message);
        });

        btnSpeak.setOnClickListener(v -> speakCurrentPhrase());

        btnFavorite.setOnClickListener(v -> toggleFavorite());

        btnShare.setOnClickListener(v -> sharePhrase());
    }

    private void searchPhrase(String message) {

        hideKeyboard();

        btnGenerate.setEnabled(false);

        progressMedia.setVisibility(View.VISIBLE);

        String language = "English";
        if (spinnerLanguage.getSelectedItem() != null) {
            language = spinnerLanguage.getSelectedItem().toString();
        }

        String url =
                BASE_URL +
                        "get_bridge_phrase.php" +
                        "?query=" +
                        android.net.Uri.encode(message) +
                        "&language=" +
                        android.net.Uri.encode(language);

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            btnGenerate.setEnabled(true);

                            progressMedia.setVisibility(
                                    View.GONE
                            );

                            try {

                                boolean success =
                                        response.getBoolean(
                                                "success"
                                        );

                                if (!success) {

                                    Toast.makeText(
                                            requireContext(),
                                            response.optString(
                                                    "message",
                                                    "No matching sign found"
                                            ),
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                JSONObject data =
                                        response.getJSONObject(
                                                "data"
                                        );

                                currentPhrase =
                                        new BridgePhrase();

                                currentPhrase.setId(
                                        data.getInt("id")
                                );

                                currentPhrase.setPhrase(
                                        data.getString("phrase")
                                );

                                currentPhrase.setEnglish(
                                        data.getString("english")
                                );

                                currentPhrase.setHindi(
                                        data.getString("hindi")
                                );

                                currentPhrase.setMarathi(
                                        data.getString("marathi")
                                );

                                currentPhrase.setCategory(
                                        data.getString("category")
                                );

                                currentPhrase.setImageUrl(
                                        data.getString("image_url")
                                );

                                currentPhrase.setGifUrl(
                                        data.getString("gif_url")
                                );

                                currentPhrase.setFavorite(
                                        data.getInt(
                                                "is_favorite"
                                        ) == 1
                                );

                                showResult();

                            } catch (Exception e) {

                                Toast.makeText(
                                        requireContext(),
                                        "Response error",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        },

                        error -> {

                            btnGenerate.setEnabled(true);

                            progressMedia.setVisibility(
                                    View.GONE
                            );

                            Toast.makeText(
                                    requireContext(),
                                    "Unable to connect to SignBridge server",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );

        requestQueue.add(request);
    }

    private void showResult() {

        if (currentPhrase == null) {
            return;
        }

        cardResult.setVisibility(View.VISIBLE);

        String language = "English";
        if (spinnerLanguage.getSelectedItem() != null) {
            language = spinnerLanguage.getSelectedItem().toString();
        }

        tvPhrase.setText(
                currentPhrase.getEnglish()
        );

        if (language.equals("Hindi")) {

            tvTranslation.setText(
                    currentPhrase.getHindi()
            );

        } else if (language.equals("Marathi")) {

            tvTranslation.setText(
                    currentPhrase.getMarathi()
            );

        } else {

            tvTranslation.setText(
                    currentPhrase.getEnglish()
            );
        }

        isFavorite =
                currentPhrase.isFavorite();

        updateFavoriteIcon();

        progressMedia.setVisibility(
                View.VISIBLE
        );

        String mediaUrl =
                currentPhrase.getGifUrl();

        if (mediaUrl == null ||
                mediaUrl.equals("null") ||
                mediaUrl.trim().isEmpty()) {

            mediaUrl =
                    currentPhrase.getImageUrl();
        }

        Glide.with(this)
                .load(mediaUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressMedia.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressMedia.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivSign);

        tvOffline.setText(
                "● Available Offline"
        );
    }

    private void speakCurrentPhrase() {

        if (currentPhrase == null) {

            Toast.makeText(
                    requireContext(),
                    "Generate a phrase first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String language = "English";
        if (spinnerLanguage.getSelectedItem() != null) {
            language = spinnerLanguage.getSelectedItem().toString();
        }

        String text;

        Locale locale;

        if (language.equals("Hindi")) {

            text = currentPhrase.getHindi();

            locale = new Locale("hi", "IN");

        } else if (language.equals("Marathi")) {

            text = currentPhrase.getMarathi();

            locale = new Locale("mr", "IN");

        } else {

            text = currentPhrase.getEnglish();

            locale = Locale.ENGLISH;
        }

        int result =
                textToSpeech.setLanguage(locale);

        if (result ==
                TextToSpeech.LANG_MISSING_DATA ||
                result ==
                        TextToSpeech.LANG_NOT_SUPPORTED) {

            Toast.makeText(
                    requireContext(),
                    "Language voice data unavailable",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        textToSpeech.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "SIGNBRIDGE_SPEECH"
        );
    }

    private void toggleFavorite() {

        if (currentPhrase == null) {
            return;
        }

        int newStatus =
                isFavorite ? 0 : 1;

        String url =
                BASE_URL +
                        "favorite_phrase.php";

        JSONObject body =
                new JSONObject();

        try {

            body.put(
                    "phrase_id",
                    currentPhrase.getId()
            );

            body.put(
                    "is_favorite",
                    newStatus
            );

        } catch (Exception e) {

            return;
        }

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        body,

                        response -> {

                            if (response.optBoolean(
                                    "success",
                                    false
                            )) {

                                isFavorite =
                                        newStatus == 1;

                                updateFavoriteIcon();

                                Toast.makeText(
                                        requireContext(),
                                        isFavorite
                                                ? "Added to Favorites"
                                                : "Removed from Favorites",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        },

                        error -> Toast.makeText(
                                requireContext(),
                                "Could not update favorite",
                                Toast.LENGTH_SHORT
                        ).show()
                );

        requestQueue.add(request);
    }

    private void updateFavoriteIcon() {

        if (isFavorite) {

            btnFavorite.setImageResource(
                    R.drawable.star
            );

            btnFavorite.setColorFilter(
                    android.graphics.Color
                            .parseColor("#FF4F81")
            );

        } else {

            btnFavorite.setImageResource(
                    R.drawable.star
            );

            btnFavorite.setColorFilter(
                    android.graphics.Color.WHITE
            );
        }
    }

    private void sharePhrase() {

        if (currentPhrase == null) {
            return;
        }

        String shareText =
                "SignBridge 🤟\n\n" +
                        "Phrase: " +
                        currentPhrase.getEnglish() +
                        "\n\n" +
                        "Bridging Silence, Connecting Voices.";

        Intent intent =
                new Intent(
                        Intent.ACTION_SEND
                );

        intent.setType("text/plain");

        intent.putExtra(
                Intent.EXTRA_TEXT,
                shareText
        );

        startActivity(
                Intent.createChooser(
                        intent,
                        "Share Sign"
                )
        );
    }

    private void hideKeyboard() {

        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager)
                        requireContext()
                                .getSystemService(
                                        android.content.Context
                                                .INPUT_METHOD_SERVICE
                                );

        if (imm != null) {

            imm.hideSoftInputFromWindow(
                    etMessage.getWindowToken(),
                    0
            );
        }
    }

    @Override
    public void onDestroyView() {

        if (textToSpeech != null) {

            textToSpeech.stop();

            textToSpeech.shutdown();
        }

        super.onDestroyView();
    }
}