package com.speedlaundryapp.userapp.activity.laundry_user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityUpdateUserdataBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitCallBack;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitUtility;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.model.user.info_user.MyAccount;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class UserDataActivity extends AppCompatActivity {
    ActivityUpdateUserdataBinding mBinding;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_userdata);
        initView();
    }
    public void initView(){
        getUser();
        mBinding.refresh.setOnRefreshListener(() -> {
            getUser();
        });
        mBinding.back.setOnClickListener(v -> finish());
        mBinding.btnSubmit.setOnClickListener(v -> {
            submitAddress();
        });
    }
    public void getUser() {
        ApiRepository repo = ApiRepository.getInstance();
        RetrofitCallBack api = RetrofitUtility.getApiWithHeader(getApplicationContext());
        repo.setDialog(UserDataActivity.this);
        repo.setModelListener(api.myAccount(), new PojoListener<MyAccount>() {
            @Override
            public void onResponseListener(MyAccount response) {
                mBinding.refresh.setRefreshing(false);
                MainApplication.getInstance().setUser(response.getData().getUser());
                MainApplication.user = response.getData().getUser();
                user = MainApplication.user;
                mBinding.setUserDetail(user);
            }
            @Override
            public void onErrorListener(ResponseError error, int code) {
                Toast.makeText(getApplicationContext(), "ERRUSRD: " +  error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void submitAddress(){
        JSONObject jsonObjectAdd = null;
        try {
            jsonObjectAdd = RequestParam.getJSONObject();
            jsonObjectAdd.put("phone_number", mBinding.edtPhone.getText());
            jsonObjectAdd.put("shipment_address", mBinding.edtAddress.getText());
            jsonObjectAdd.put("map_address_url", mBinding.edtMapUrl.getText());
            jsonObjectAdd.put("shipment_name", mBinding.edtNama.getText());
            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Ubah Data ?");
            builder.setNegativeButton("Kembali", (dialog, which) -> {});
            builder.setPositiveButton("Ubah",
                    (dialog, which) -> {
                        ApiRepository repository = ApiRepository.getInstance();
                        repository.setDialog(UserDataActivity.this);
                        repository.setApiListener(MainApplication.api.updateUserDetails(user.getUserDetailId(),requestBody),
                                (object, codeResponse, rEnum) -> {
                                    if (rEnum == RepositoryEnum.SUCCESS) {
                                        String data = object.getString("message");
                                        ToastUtils.showLong(data);
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK,intent );
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "ERRUSRER: " +  object.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (JSONException e) {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}