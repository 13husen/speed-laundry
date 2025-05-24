package com.speedlaundryapp.userapp.activity.laundry_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.HomeActivity;
import com.speedlaundryapp.userapp.activity.LoginActivity;
import com.speedlaundryapp.userapp.activity.registration.RegistrationActivity;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityUserPreparationBinding;
import com.speedlaundryapp.userapp.session.LocalPreferences;

public class UserPreparationActivity extends AppCompatActivity {
    LocalPreferences preferences;
    private boolean isExit = false;
    ActivityUserPreparationBinding mBinding;

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_preparation);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isExit) {
            finish();
        } else {
            Toast.makeText(this, "Tekan kembali sekali lagi untuk keluar.", Toast.LENGTH_SHORT).show();
            isExit = true;
        }
    }

    private void initView() {
        preferences = new LocalPreferences(getBaseContext());
        if (preferences.hasToken()) {
            startActivity(new Intent(UserPreparationActivity.this, HomeActivity.class));
            finish();
        }
        mBinding.btnMasuk.setOnClickListener(v -> {
            Intent intents = new Intent(UserPreparationActivity.this, LoginActivity.class);
            startActivity(intents);
        });
        mBinding.btnDaftar.setOnClickListener(v->{
            Intent intents = new Intent(UserPreparationActivity.this, RegistrationActivity.class);
            startActivity(intents);
        });

    }


    @Override
    public void onDestroy() {
        MainApplication.getInstance().dismissProgressDialog();
        super.onDestroy();
    }
}