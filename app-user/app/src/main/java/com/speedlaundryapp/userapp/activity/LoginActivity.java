package com.speedlaundryapp.userapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.registration.RegistrationActivity;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityLoginBinding;
import com.speedlaundryapp.userapp.dialog.DialogForgetPassword;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.UserParam;
import com.speedlaundryapp.userapp.model.user.auth.AccountData;
import com.speedlaundryapp.userapp.session.LocalPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
        jsonObject.put("type", "user");
        jsonObject.put("password", mBinding.login.editTextPassword.getText().toString());
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), ParseObject.requestParse);
        ApiRepository apiRepository = ApiRepository.getInstance();
        apiRepository.setDialogLoading(getSupportFragmentManager());
        apiRepository.setDialogError(true);
        apiRepository.setModelListener(MainApplication.api.login(requestBody),
                new PojoListener<AccountData>() {
            @Override
            public void onResponseListener(AccountData response) throws JSONException {
                String token = response.getData().getToken();
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
        mBinding.back.setOnClickListener(v -> finish());
        mBinding.login.registerButton.setOnClickListener(v -> {
            Intent intents = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intents);
        });
        mBinding.login.cirLoginButton.setOnClickListener(v->{
            try {
                getLogin();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = R.id.cirLoginButton;
        if (v.getId() == id) {
            try {
                getLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onDestroy() {
        MainApplication.getInstance().dismissProgressDialog();
        super.onDestroy();
    }

}