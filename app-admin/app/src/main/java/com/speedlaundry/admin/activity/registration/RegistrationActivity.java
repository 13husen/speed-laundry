package com.speedlaundry.admin.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.activity.LoginActivity;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.ActivityRegistrationBinding;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.PojoListener;
import com.speedlaundry.admin.http.retrofit.ParseObject;
import com.speedlaundry.admin.model.ResponseError;
import com.speedlaundry.admin.model.UserParam;
import com.speedlaundry.admin.model.user.auth.AccountData;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding mBinding;
    String fbToken;
    int first= 34;
    int last = 67;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mBinding.registration.registerButton.setOnClickListener(v -> {
            try {
                Register();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
//        SpannableString spanString = new SpannableString(mBinding.registration.agrre.getText().toString());
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                Intent i = new Intent(getApplicationContext(), TermsConditionsActivity.class);
//                i.putExtra("url", ApiConstant.TERMSCONDITIONS);
//                startActivity(i);
//            }
//        };
//        spanString.setSpan(clickableSpan, first, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mBinding.registration.agrre.setText(spanString);
//        mBinding.registration.agrre.setMovementMethod(LinkMovementMethod.getInstance());

    }

    void Register() throws JSONException {
        JSONObject jsonObject = UserParam.getJSONObject();
        jsonObject.put("name", mBinding.registration.etName.getText().toString());
        jsonObject.put("email", mBinding.registration.etEmail.getText().toString());
        jsonObject.put("phone_number", mBinding.registration.etPhone.getText().toString());
        jsonObject.put("password", mBinding.registration.etPassword.getText().toString());
        jsonObject.put("password_confirmation", mBinding.registration.etPassword2.getText().toString());
        jsonObject.put("type", mBinding.registration.edtUserType.getSelectedItem().toString().toLowerCase());
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