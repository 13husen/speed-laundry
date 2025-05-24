package com.speedlaundryapp.userapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.speedlaundryapp.userapp.R;

public class MainActivity extends AppCompatActivity{
    private TextView text_desc, text_email,text_birthday;
    ImageView logo_image;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        setContentView(R.layout.activity_main);
        logo_image = findViewById(R.id.logo_image);
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        text_desc = findViewById(R.id.text_desc);

        text_email = findViewById(R.id.text_email);
        text_birthday = findViewById(R.id.text_birthday);

       /* Intent in = getIntent();
        String nama = in.getStringExtra("Name");
        String picture = in.getStringExtra("Picture");

        text_desc.setText(nama);*/

    }
}