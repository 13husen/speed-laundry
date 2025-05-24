package com.speedlaundry.admin.activity.profile;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.ActivityChangePasswordBinding;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.http.retrofit.ParseObject;
import com.speedlaundry.admin.model.UserParam;
import com.speedlaundry.admin.session.LocalPreferences;
import com.speedlaundry.admin.utils.GeneralUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChangePasswordBinding mBinding;
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