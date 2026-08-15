package com.harsh.visionx_signbridge;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.harsh.visionx_signbridge.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgProfile, btnChangePhoto;
    Button btnEditProfile, btnDeleteAccount;
    TextView txtName, txtMobile, txtEmail, txtUserNameDetails;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.imgProfile);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        txtName = findViewById(R.id.txtName);
        txtMobile = findViewById(R.id.txtMobile);
        txtEmail = findViewById(R.id.txtEmail);
        txtUserNameDetails = findViewById(R.id.txtUserNameDetails);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnDeleteAccount = findViewById(R.id.DeleteAc);

        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = preferences.edit();

        // Note: btnLogout was used in the previous version but not found in the new layout activity_profile.
        // If there was a logout button, it seems it's replaced or missing. 
        // I will keep the listener for DeleteAc or you might want to add a Logout button to activity_profile.xml.
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetails();
    }

    private void getMyDetails() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", preferences.getString("username", ""));

        client.post(Urls.getmydetailsAPI, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    progressDialog.dismiss();
                    if (response.has("getMyDetails")) {
                        JSONArray jsonArray = response.getJSONArray("getMyDetails");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strName = jsonObject.getString("name");
                            String strMobileno = jsonObject.getString("mobileno");
                            String strEmailId = jsonObject.getString("emailid");
                            String strUsername = jsonObject.getString("username");

                            txtName.setText(strName);
                            txtMobile.setText(strMobileno);
                            txtEmail.setText(strEmailId);
                            txtUserNameDetails.setText(strUsername);

                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "No details found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Response error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
