package com.harsh.visionx_signbridge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactUsActivity extends AppCompatActivity {

    TextView ivtextanim;
    EditText etcontactusmobile,etcontactmessage,etcontactusemail,etcontactusreciepent,etcontactemailenter;

    AppCompatButton btncantactussendsms ,btncantactussendemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        etcontactusmobile = findViewById(R.id.etcontactusmobile);
        etcontactmessage = findViewById(R.id.etcontactmessage);
        etcontactusreciepent = findViewById(R.id.etcontactusreciepent);
        ivtextanim = findViewById(R.id.ivtextanim);
        etcontactusemail = findViewById(R.id.etcontactusemail);
        etcontactemailenter = findViewById(R.id.etcontactemailenter);
        btncantactussendemail = findViewById(R.id.btncantactussendemail);
        btncantactussendsms = findViewById(R.id.btncantactussendsms);

        Animation animation = AnimationUtils.loadAnimation(ContactUsActivity.this,
                R.anim.luxaryanim);

        ivtextanim.startAnimation(animation);

        if (ContextCompat.checkSelfPermission(ContactUsActivity.this,
                Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(ContactUsActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},999);

        }
        btncantactussendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String strMobileNo = etcontactusmobile.getText().toString();
                    String strmessage = etcontactmessage.getText().toString();

                    SmsManager smsManajer = SmsManager.getDefault();
                    smsManajer.sendTextMessage(strMobileNo,null,
                            strmessage,null,null);

                    Toast.makeText(ContactUsActivity.this,
                            "SMS Send Successfully",Toast.LENGTH_SHORT).show();

                    etcontactusmobile.setText("");
                    etcontactmessage.setText("");

                } catch (Exception e) {
                    Toast.makeText(ContactUsActivity.this,"not",Toast.LENGTH_SHORT).show();
                }}
        });
        btncantactussendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strreciepint = etcontactusreciepent.getText().toString();
                String stremail = etcontactusemail.getText().toString();
                String stremailenter = etcontactemailenter.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("Message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{strreciepint});
                intent.putExtra(Intent.EXTRA_SUBJECT,stremail);
                intent.putExtra(Intent.EXTRA_TEXT,stremailenter);


                try {
                    startActivity(Intent.createChooser(intent,"Choose an App"));
                } catch (Exception e) {
                    Toast.makeText(ContactUsActivity.this,""+e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}