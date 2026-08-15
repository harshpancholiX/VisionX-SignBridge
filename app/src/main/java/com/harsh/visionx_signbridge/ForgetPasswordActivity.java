package com.harsh.visionx_signbridge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.harsh.visionx_signbridge.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText etusername,etNewPassword,etConfirmNewPassword;
    Button btnForgetPassword;
    ProgressDialog progressDialog;
    CheckBox cbForgetShowHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        etusername=findViewById(R.id.etForgetPasswordUsername);
        etNewPassword=findViewById(R.id.etForgetPasswordNewPassword);
        etConfirmNewPassword=findViewById(R.id.etForgetPasswordConfirmNewPassword);
        btnForgetPassword=findViewById(R.id.btnForgetPaswordForgetPasswordBtn);
        cbForgetShowHidePassword = findViewById(R.id.cbForgetShowHidePassword);


        btnForgetPassword.setOnClickListener(v -> {
            if (etusername.getText().toString().isEmpty())
            {
                etusername.setError("Please Enter Username");
            } else if (etusername.getText().toString().length() < 8)
            {
                etusername.setError("Username Must be Greater Than 8");
            } else if (etNewPassword.getText().toString().isEmpty())
            {
                etNewPassword.setError("Please Enter New Password");
            } else if (etNewPassword.getText().toString().length() < 8)
            {
                etNewPassword.setError("New Password Must be Greater Than 8");
            } else if (etConfirmNewPassword.getText().toString().isEmpty())
            {
                etConfirmNewPassword.setError("Please Enter Confirm Password");
            } else if (etConfirmNewPassword.getText().toString().length() <8 )
            {
                etConfirmNewPassword.setError("Confirm Password Must Be Greater Than 8");
            } else if (!etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString()))
            {
                etConfirmNewPassword.setError("Password and ConfirmPassword Does Not Match");
            }
            else
            {
                progressDialog=new ProgressDialog(ForgetPasswordActivity.this);
                progressDialog.setTitle("Forget Password");
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                forgetPassword();
            }
        });
        cbForgetShowHidePassword.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {
                        // Show Password
                        etNewPassword.setTransformationMethod(
                                HideReturnsTransformationMethod.getInstance());

                        etConfirmNewPassword.setTransformationMethod(
                                HideReturnsTransformationMethod.getInstance());
                    }
                    else {
                        // Hide Password
                        etNewPassword.setTransformationMethod(
                                PasswordTransformationMethod.getInstance());

                        etConfirmNewPassword.setTransformationMethod(
                                PasswordTransformationMethod.getInstance());
                    }

                    // Move cursor to the end
                    etNewPassword.setSelection(
                            etNewPassword.getText().length());

                    etConfirmNewPassword.setSelection(
                            etConfirmNewPassword.getText().length());
                });

    }

    private void forgetPassword()
    {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("username",etusername.getText().toString());
        params.put("newpassword",etNewPassword.getText().toString());


        client.post(Urls.forgetPasswordAPI,params,new JsonHttpResponseHandler()

                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            String status=response.getString("success");
                            String message=response.getString("message");

                            if (status.equals("1"))
                            {
                                Toast.makeText(ForgetPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }
                            else
                            {
                                Toast.makeText(ForgetPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        }

                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)

                    {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        progressDialog.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this,"Server Error",Toast.LENGTH_SHORT).show();

                    }
                }

        );
    }
}