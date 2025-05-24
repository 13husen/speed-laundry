package com.speedlaundryapp.userapp.activity.laundry_user;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityUserRequestLaundryBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitCallBack;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitUtility;
import com.speedlaundryapp.userapp.laundry_ui.TransactionDetailActivity;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.model.user.data.UserDetail;
import com.speedlaundryapp.userapp.model.user.info_user.MyAccount;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class UserRequestLaundryActivity extends AppCompatActivity {
    ActivityUserRequestLaundryBinding mBinding;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_request_laundry);
        initView();
    }
    public void initView(){
        mBinding.back.setOnClickListener(v -> finish());
        getUser();
        mBinding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(UserRequestLaundryActivity.this,UserDataActivity.class);
            startActivityForResult(intent,101);
        });
        mBinding.refresh.setOnRefreshListener(() -> getUser());
        mBinding.btnSubmit.setOnClickListener(v -> createRequest());
    }
    public void createRequest(){
        UserDetail userDetail = user.getUserDetail();
        if (userDetail.getPhoneNumber() != null && userDetail.getPhoneNumber() != "" &&
                userDetail.getShipmentName() != null && userDetail.getShipmentName() != "" &&
                userDetail.getShipmentAddress() != null && userDetail.getShipmentAddress() != ""
        ){
            JSONObject jsonObjectAdd = null;
            try {
                jsonObjectAdd = RequestParam.getJSONObject();
                jsonObjectAdd.put("type", 2);
                jsonObjectAdd.put("note", mBinding.editNote.getText());
                jsonObjectAdd.put("shipment_fix_address", userDetail.getShipmentAddress());
                jsonObjectAdd.put("shipment_fix_name", userDetail.getShipmentName());
                jsonObjectAdd.put("shipment_fix_phone", userDetail.getPhoneNumber());
                RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Buat Request Laundry ?");
                builder.setMessage("Setelah ini kurir akan datang untuk melakukan pengecekan!");
                builder.setNegativeButton("Kembali", (dialog, which) -> {});
                builder.setPositiveButton("Buat",
                        (dialog, which) -> {
                            ApiRepository repository = ApiRepository.getInstance();
                            repository.setDialog(UserRequestLaundryActivity.this);
                            repository.setApiListener(MainApplication.api.createLaundryRequest(requestBody),
                                    (object, codeResponse, rEnum) -> {
                                        if (rEnum == RepositoryEnum.SUCCESS) {
                                            String data = object.getString("message");
                                            TransactionItem obj = new Gson().fromJson(object.getString("data"), TransactionItem.class);
                                            ToastUtils.showLong(data);
                                            Intent i = new Intent(UserRequestLaundryActivity.this,
                                                    TransactionDetailActivity.class);
                                            i.putExtra("id",obj.getId());
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "ERRUSREQ: " +  object.getString("message"), Toast.LENGTH_LONG).show();
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
        }else{
            Toast.makeText(this, "Mohon Lengkapi Alamat Anda Terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUser() {
        ApiRepository repo = ApiRepository.getInstance();
        RetrofitCallBack api = RetrofitUtility.getApiWithHeader(getApplicationContext());
        repo.setDialog(this);
        repo.setModelListener(api.myAccount(), new PojoListener<MyAccount>() {
            @Override
            public void onResponseListener(MyAccount response) {
                mBinding.refresh.setRefreshing(false);
                MainApplication.getInstance().setUser(response.getData().getUser());
                MainApplication.user = response.getData().getUser();
                user = MainApplication.user;
                mBinding.txtNama.setText(user.getUserDetail().getShipmentName() != null ? user.getUserDetail().getShipmentName() : "-");
                mBinding.txtPhone.setText(user.getUserDetail().getPhoneNumber()!= null ? user.getUserDetail().getPhoneNumber() : "-");
                mBinding.txtAlamat.setText(user.getUserDetail().getShipmentAddress() != null ? user.getUserDetail().getShipmentAddress() : "-");
                if (user.getUserDetail().getMapAddressUrl() != null){
                    mBinding.txtMapUrl.setVisibility(View.VISIBLE);
                    if (URLUtil.isValidUrl(user.getUserDetail().getMapAddressUrl()))
                        mBinding.txtMapUrl.setOnClickListener(v -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getUserDetail().getMapAddressUrl()));
                            startActivity(browserIntent);
                        });
                }
            }
            @Override
            public void onErrorListener(ResponseError error, int code) {
                Toast.makeText(getApplicationContext(), "ERRUSREQERR: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getUser();
    }
}