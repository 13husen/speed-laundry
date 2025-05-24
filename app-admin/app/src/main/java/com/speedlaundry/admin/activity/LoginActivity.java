package com.speedlaundry.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.activity.registration.RegistrationActivity;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.ActivityLoginBinding;
import com.speedlaundry.admin.dialog.DialogForgetPassword;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.PojoListener;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.http.retrofit.ParseObject;
import com.speedlaundry.admin.model.RequestParam;
import com.speedlaundry.admin.model.ResponseError;
import com.speedlaundry.admin.model.UserParam;
import com.speedlaundry.admin.model.user.auth.AccountData;
import com.speedlaundry.admin.session.LocalPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView registerButton;
    private Button cirLoginButton;
    private boolean isExit = false;
    ApiRepository repository = ApiRepository.getInstance();
    ActivityLoginBinding mBinding;
    LocalPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        preferences = new LocalPreferences(getBaseContext());
        if (preferences.hasToken()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        mBinding.login.forgotPassword.setOnClickListener(v -> {
            DialogForgetPassword dialogForgetPassword = new DialogForgetPassword();
            dialogForgetPassword.setSubmitNoHandPhone((s,pass, dialog) -> {
                JSONObject jsonObjectAdd = null;
                try {
                    jsonObjectAdd = RequestParam.getJSONObject();
                    jsonObjectAdd.put("email", s);
                    jsonObjectAdd.put("password", pass);
                    ApiRepository repository = ApiRepository.getInstance();
                    RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
                    repository.setDialogLoading(getSupportFragmentManager());
                    repository.setDialogError(true);
                    repository.setApiListener(MainApplication.api.forgotPassword(requestBody),
                            (object, codeResponse, rEnum) -> {
                                String data = object.getString("message");
                                if (rEnum == RepositoryEnum.SUCCESS) {
                                    ToastUtils.showLong(data);
                                    dialogForgetPassword.dismiss();
                                }else {
                                    ToastUtils.showLong(data);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
            dialogForgetPassword.show(getSupportFragmentManager(), null);
        });
        initView();
    }

    void getLogin() throws JSONException {
        JSONObject jsonObject = UserParam.getJSONObject();
        jsonObject.put("email", mBinding.login.editTextEmail.getText().toString());
        jsonObject.put("type", mBinding.login.edtUserType.getSelectedItem().toString().toLowerCase());
        jsonObject.put("password", mBinding.login.editTextPassword.getText().toString());
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), ParseObject.requestParse);
        ApiRepository apiRepository = ApiRepository.getInstance();

        apiRepository.setDialogLoading(getSupportFragmentManager());
        apiRepository.setDialogError(true);
        apiRepository.setModelListener(MainApplication.api.login(requestBody),
                new PojoListener<AccountData>() {
            @Override
            public void onResponseListener(AccountData response) {
                String token = response.getData().getToken();
                Log.d("FB TOKEN ", token);
                preferences.saveString(LocalPreferences.SPToken, token);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onErrorListener(ResponseError error, int code) {
            }
        });
    }


    private void initView() {
        registerButton = findViewById(R.id.registerButton);
        mBinding.login.registerButton.setOnClickListener(v -> {
            Intent intents = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intents);
        });
        cirLoginButton = findViewById(R.id.cirLoginButton);
        cirLoginButton.setOnClickListener(v->{
            try {
                getLogin();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cirLoginButton) {
            try {
                getLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void onDestroy() {
        MainApplication.getInstance().dismissProgressDialog();
        super.onDestroy();
    }

}