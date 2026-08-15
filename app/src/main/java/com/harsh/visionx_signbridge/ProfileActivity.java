package com.harsh.visionx_signbridge;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.harsh.visionx_signbridge.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivMyProfileMyphoto, ivMyProfileQRCode;
    Button btnMyProfileChangeProfilePhoto, btnMyProfileDeleteAccount;
    TextView tvMyProfileName, tvMyProfileMobileNo, tvMyProfileEmailid, tvMyProfileUsername;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    CardView cardProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        cardProfile = findViewById(R.id.cardProfile);
        ivMyProfileMyphoto = findViewById(R.id.ivMyProfileMyphoto);
        btnMyProfileChangeProfilePhoto = findViewById(R.id.btnMyProfileChangeProfilePhoto);
        btnMyProfileDeleteAccount = findViewById(R.id.btnMyProfileDeleteAccount);
        tvMyProfileName = findViewById(R.id.tvMyProfileName);
        tvMyProfileMobileNo = findViewById(R.id.tvMyProfileMobileNo);
        tvMyProfileEmailid = findViewById(R.id.tvMyProfileEmailid);
        tvMyProfileUsername = findViewById(R.id.tvMyProfileUsername);
        ivMyProfileQRCode = findViewById(R.id.ivMyProfileQRCode);

        // Animations
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        if (cardProfile != null) cardProfile.startAnimation(animation);
        if (btnMyProfileDeleteAccount != null) btnMyProfileDeleteAccount.startAnimation(animation);

        // Using unified SharedPreferences name
        preferences = getSharedPreferences("SignBridgePrefs", MODE_PRIVATE);
        editor = preferences.edit();

        if (btnMyProfileDeleteAccount != null) {
            btnMyProfileDeleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteAccountDialog();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog("My Profile", "Please Wait");
        getMyDetails();
    }

    private void showProgressDialog(String title, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void getMyDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", preferences.getString("username", ""));

        client.post(Urls.getMyDetailsAPI, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dismissProgressDialog();
                try {
                    if (response.has("getMyDetails")) {
                        JSONArray jsonArray = response.getJSONArray("getMyDetails");
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String strName = jsonObject.optString("name", "");
                            String strMobileno = jsonObject.optString("mobileno", "");
                            String strEmailId = jsonObject.optString("emailid", "");
                            String strUsername = jsonObject.optString("username", "");

                            if (tvMyProfileName != null) tvMyProfileName.setText(strName);
                            if (tvMyProfileMobileNo != null) tvMyProfileMobileNo.setText(strMobileno);
                            if (tvMyProfileEmailid != null) tvMyProfileEmailid.setText(strEmailId);
                            if (tvMyProfileUsername != null) tvMyProfileUsername.setText(strUsername);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Data Parse Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dismissProgressDialog();
                Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dismissProgressDialog();
                Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgressDialog("Delete Account", "Please Wait...");
                deleteAccount();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Account deletion cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void deleteAccount() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", preferences.getString("username", ""));

        client.post(Urls.deleteAccount, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dismissProgressDialog();
                try {
                    String status = response.optString("success", "0");
                    String message = response.optString("message", "Request failed");

                    if (status.equals("1")) {
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        logout();
                    } else {
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dismissProgressDialog();
                Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        editor.clear(); // Clear all user data
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
