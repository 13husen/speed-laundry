package com.speedlaundryapp.userapp.activity.profile;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityChangePasswordBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.UserParam;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.session.LocalPreferences;
import com.speedlaundryapp.userapp.utils.GeneralUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChangePasswordBinding mBinding;
    User user;
    Gson gson = new Gson();
    ApiRepository repository = ApiRepository.getInstance();
    LocalPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        initView();

        preferences = new LocalPreferences(this);
        mBinding.back.setOnClickListener(this);
        mBinding.buttonConfirm.setOnClickListener(this);
    }

    private void initView() {
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.back) {
            finish();
        } else if (id == R.id.buttonConfirm) {
            try {
                updatePassword();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    private void updatePassword() throws JSONException {
        JSONObject jsonObject = UserParam.getJSONObject();
        jsonObject.put("old_password", mBinding.etCurrentPass.getText().toString());
        jsonObject.put("password", mBinding.editPassword.getText().toString());
        jsonObject.put("c_password", mBinding.editPassword.getText().toString());
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), ParseObject.requestParse);
        repository.setApiListener(MainApplication.api.updatePassword(requestBody),
                (object, codeResponse, rEnum) -> {
                    if (rEnum == RepositoryEnum.SUCCESS) {
                        preferences.getLogout();
                    } else {
                        //preferences.getLogout();
                        GeneralUtil.errorToast(getApplicationContext(), object);
                    }
                });
    }
}