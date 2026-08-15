package com.harsh.visionx_signbridge;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.harsh.visionx_signbridge.Common.Urls;

public class TranslateActivity extends AppCompatActivity {

    ImageButton btnBack;
    ImageView imgPreview;
    Button btnSelectImage, btnTranslate;
    EditText etExtractedText;
    TextView tvTranslatedText;
    Spinner spinnerLanguage;

    Uri imageUri;

    String API_URL = Urls.CommonURL + "translate.php";

    ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            imageUri = uri;
                            imgPreview.setImageURI(uri);
                            extractText(uri);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        btnBack = findViewById(R.id.btnBack);
        imgPreview = findViewById(R.id.imgPreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnTranslate = findViewById(R.id.btnTranslate);
        etExtractedText = findViewById(R.id.etExtractedText);
        tvTranslatedText = findViewById(R.id.tvTranslatedText);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        String[] languages = {
                "English",
                "Hindi",
                "Marathi",
                "Gujarati",
                "Tamil",
                "Telugu",
                "Bengali"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                languages
        );

        spinnerLanguage.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnSelectImage.setOnClickListener(v ->
                imagePicker.launch("image/*")
        );

        btnTranslate.setOnClickListener(v ->
                translateText()
        );
    }

    private void extractText(Uri uri) {

        try {

            InputImage image = InputImage.fromFilePath(this, uri);

            TextRecognition
                    .getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(image)
                    .addOnSuccessListener(result -> {

                        String text = result.getText();

                        if (!text.isEmpty()) {
                            etExtractedText.setText(text);
                        } else {
                            etExtractedText.setText(
                                    "No text found in image"
                            );
                        }

                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    this,
                                    "Text extraction failed",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to read image",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void translateText() {

        String text = etExtractedText
                .getText()
                .toString()
                .trim();

        String language = spinnerLanguage
                .getSelectedItem()
                .toString();

        if (text.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please select an image first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                API_URL,

                response -> {

                    try {

                        JSONObject jsonObject =
                                new JSONObject(response);

                        boolean status =
                                jsonObject.getBoolean("status");

                        if (status) {

                            String translatedText =
                                    jsonObject.getString(
                                            "translated_text"
                                    );

                            tvTranslatedText.setText(
                                    translatedText
                            );

                        } else {

                            tvTranslatedText.setText(
                                    jsonObject.getString("message")
                            );
                        }

                    } catch (Exception e) {

                        tvTranslatedText.setText(
                                "Invalid server response"
                        );
                    }
                },

                error -> tvTranslatedText.setText(
                        "Unable to connect to server"
                )
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params =
                        new HashMap<>();

                params.put("text", text);
                params.put("language", language);

                return params;
            }
        };

        Volley
                .newRequestQueue(this)
                .add(request);
    }
}