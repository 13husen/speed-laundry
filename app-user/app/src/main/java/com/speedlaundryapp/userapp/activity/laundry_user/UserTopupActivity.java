package com.speedlaundryapp.userapp.activity.laundry_user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityUserTopupBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.laundry_ui.TopupDetailActivity;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;

import okhttp3.RequestBody;

public class UserTopupActivity extends AppCompatActivity {
    ActivityUserTopupBinding mBinding;
    DecimalFormat df = new DecimalFormat("#,###");
    DecimalFormat dfnd;
    boolean hasFractionalPart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_topup);
        initView();
    }
    public void initView(){
        mBinding.back.setOnClickListener(v -> finish());
        nominalChange();
        mBinding.btnSubmit.setOnClickListener(v -> {
            if (mBinding.edtNominal.getText().toString().matches("")){
                Toast.makeText(this, "Mohon isi nominal topup terlebih dahulu", Toast.LENGTH_SHORT).show();
            }else{
                topup();
            }
        });
    }
    public void nominalChange(){
        df = new DecimalFormat("#,###");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        hasFractionalPart = false;
        mBinding.edtNominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String c = s.toString();
                c = c.replace(".", "");
                hasFractionalPart = c.contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
            }
            @Override
            public void afterTextChanged(Editable s) {
                mBinding.edtNominal.removeTextChangedListener(this);
                try {
                    int inilen, endlen;
                    inilen = mBinding.edtNominal.getText().length();
                    String c = s.toString();
                    c = c.replace(".", "");
                    String v = c.replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                    Number n = df.parse(v);
                    int cp = mBinding.edtNominal.getSelectionStart();
                    if (hasFractionalPart) {
                        String a = df.format(n);
                        a = a.replace(',', '.');
                        mBinding.edtNominal.setText(a);
                    } else {
                        String b = dfnd.format(n);
                        b = b.replace(',', '.');
                        mBinding.edtNominal.setText(b);
                    }
                    endlen = mBinding.edtNominal.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel <= mBinding.edtNominal.getText().length()) {
                        mBinding.edtNominal.setSelection(sel);
                    } else {
                        mBinding.edtNominal.setSelection(mBinding.edtNominal.getText().length() - 1);
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mBinding.edtNominal.addTextChangedListener(this);
            }
        });
    }

    public void topup(){
        JSONObject jsonObjectAdd = null;
        try {
            String lolos = mBinding.edtNominal.getText().toString().replace(".", "");
            String harga = StringEscapeUtils.escapeCsv(lolos);
            if (Integer.parseInt(harga) >= 10000){
                jsonObjectAdd = RequestParam.getJSONObject();
                jsonObjectAdd.put("type", 1);
                jsonObjectAdd.put("nominal", Integer.parseInt(harga));
                RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Lakukan Topup ?");
                builder.setMessage("Setelah ini anda harus mengkonfirmasikan bukti transfer!");
                builder.setNegativeButton("Kembali", (dialog, which) -> {});
                builder.setPositiveButton("Topup",
                        (dialog, which) -> {
                            ApiRepository repository = ApiRepository.getInstance();
                            repository.setDialog(UserTopupActivity.this);
                            repository.setApiListener(MainApplication.api.topup(requestBody),
                                    (object, codeResponse, rEnum) -> {
                                        if (rEnum == RepositoryEnum.SUCCESS) {
                                            String data = object.getString("message");
                                            TransactionItem obj = new Gson().fromJson(object.getString("data"), TransactionItem.class);
                                            ToastUtils.showLong(data);
                                            Intent i = new Intent(UserTopupActivity.this,
                                                    TopupDetailActivity.class);
                                            i.putExtra("id",obj.getId());
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "ERRTOP: " +  object.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                Toast.makeText(this, "Topup tidak boleh kurang dari 10 Ribu", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}