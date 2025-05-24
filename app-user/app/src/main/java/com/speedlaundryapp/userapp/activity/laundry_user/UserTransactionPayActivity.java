package com.speedlaundryapp.userapp.activity.laundry_user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityUserTransactionPayBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitCallBack;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitUtility;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.model.user.info_user.MyAccount;
import com.speedlaundryapp.userapp.utils.GeneralUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
public class UserTransactionPayActivity extends AppCompatActivity {
    ActivityUserTransactionPayBinding mBinding;
    Integer trxId;
    Gson gson = new Gson();
    TransactionItem transactionItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("trx_id")){
            trxId = getIntent().getIntExtra("trx_id", 0);
        }else{
            finish();
            Toast.makeText(this, "Data Transaksi Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_transaction_pay);
        initView();
    }
    public void initView(){
        getUser();
        getTransactionFee();
        mBinding.back.setOnClickListener(v -> {
            finish();
        });
        mBinding.btnTopup.setOnClickListener(v -> {
            Intent intents = new Intent(UserTransactionPayActivity.this, UserTopupActivity.class);
            startActivity(intents);
            finish();
        });
        mBinding.btnSubmit.setOnClickListener(v -> {
            if (trxId != null){
                updatePayment();
            }
        });
    }
    private void getTransactionFee(){
        ApiRepository repository = ApiRepository.getInstance();
        repository.setDialog(this);
        repository.setApiListener(MainApplication.api.getTransactionFee(trxId),
                (object, codeResponse, rEnum) -> {
                    if (rEnum == RepositoryEnum.SUCCESS) {
                        JSONObject data = object.optJSONObject("data");
                        transactionItem = gson.fromJson(data.toString(), TransactionItem.class);
                        mBinding.txtAdmin.setText(": " + GeneralUtil.convertRupiah(transactionItem.getPayment().getAdminFee().intValue()));
                        mBinding.txtNominal.setText(": " + GeneralUtil.convertRupiah(transactionItem.getPayment().getNominal().intValue()));
                        mBinding.txtOngkir.setText(": " + GeneralUtil.convertRupiah(transactionItem.getPayment().getShippingFee().intValue()));
                        mBinding.txtTotalBiaya.setText(": "+ GeneralUtil.convertRupiah(transactionItem.getPayment().getTotalFee().intValue()));
                    } else {
                        Toast.makeText(getApplicationContext(), "ERRUSRTAPS1: " +  object.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void updatePayment(){
        JSONObject jsonObjectAdd = null;
        try {
            jsonObjectAdd = RequestParam.getJSONObject();
            jsonObjectAdd.put("method", 3);
            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Lakukan Pembayaran ?");
            builder.setNegativeButton("Kembali", (dialog, which) -> {});
            builder.setPositiveButton("Bayar",
                (dialog, which) -> {
                    ApiRepository repository = ApiRepository.getInstance();
                    repository.setDialog(UserTransactionPayActivity.this);
                    repository.setApiListener(MainApplication.api.updatePayment(transactionItem.getId(), requestBody),
                            (object, codeResponse, rEnum) -> {
                                if (rEnum == RepositoryEnum.SUCCESS) {
                                    String data = object.getString("message");
                                    transactionItem = new Gson().fromJson(object.getString("data"), TransactionItem.class);
                                    ToastUtils.showLong(data);
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK,intent );
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "ERRUSRTOP: " +  object.toString(), Toast.LENGTH_LONG).show();
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

    public void getUser() {
        ApiRepository repo = ApiRepository.getInstance();
        RetrofitCallBack api = RetrofitUtility.getApiWithHeader(getApplicationContext());
        repo.setDialog(this);
        repo.setModelListener(api.myAccount(), new PojoListener<MyAccount>() {
            @Override
            public void onResponseListener(MyAccount response) {
                MainApplication.getInstance().setUser(response.getData().getUser());
                MainApplication.user = response.getData().getUser();
                User user = MainApplication.user;
                mBinding.txtWallet.setText(GeneralUtil.convertRupiah(Integer.parseInt(user.getBalance())));
            }
            @Override
            public void onErrorListener(ResponseError error, int code) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}