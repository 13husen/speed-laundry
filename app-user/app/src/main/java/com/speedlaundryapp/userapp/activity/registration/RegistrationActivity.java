package com.speedlaundryapp.userapp.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.LoginActivity;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityRegistrationBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.UserParam;
import com.speedlaundryapp.userapp.model.user.auth.AccountData;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mBinding.back.setOnClickListener(v -> finish());
        mBinding.registration.registerButton.setOnClickListener(v -> {
            try {
                Register();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    void Register() throws JSONException {
        JSONObject jsonObject = UserParam.getJSONObject();
        jsonObject.put("name", mBinding.registration.etName.getText().toString());
        jsonObject.put("email", mBinding.registration.etEmail.getText().toString());
        jsonObject.put("phone_number", mBinding.registration.etPhone.getText().toString());
        jsonObject.put("password", mBinding.registration.etPassword.getText().toString());
        jsonObject.put("password_confirmation", mBinding.registration.etPassword2.getText().toString());
        jsonObject.put("type", "user");
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), ParseObject.requestParse);
        ApiRepository apiRepository = ApiRepository.getInstance();
//        ApiRepository.getInstance().setApiListener(MainApplication.api.loginS(requestBody), (object, codeResponse, rEnum) -> {
//            if(rEnum == RepositoryEnum.SUCCESS){
//                JSONObject data = object.optJSONObject("data");
//                String token = data.optJSONObject("token").optString("access_token");
//                preferences.saveString(preferences.SPToken, token);
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            }else {
//                Toast.makeText(getBaseContext(), object.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
        apiRepository.setDialogLoading(getSupportFragmentManager());
        apiRepository.setDialogError(true);
        apiRepository.setModelListener(MainApplication.api.send(requestBody),
                new PojoListener<AccountData>() {
                    @Override
                    public void onResponseListener(AccountData response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(RegistrationActivity.this, "Pendaftaran Berhasil !", Toast.LENGTH_SHORT).show();
                            Intent intents = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intents);
                        } else {
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            Toast.makeText(RegistrationActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorListener(ResponseError error, int code) {
                        Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}