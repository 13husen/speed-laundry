package com.speedlaundryapp.userapp.laundry_ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityCategorizeClotheDetailBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundryapp.userapp.utils.GeneralUtil;
import com.speedlaundryapp.userapp.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class CategorizeClotheDetailActivity extends AppCompatActivity {
    ActivityCategorizeClotheDetailBinding mBinding;
    private int id;
    CategorizeClotheItem categoryItem;
    Gson gson = new Gson();
    ApiRepository repository = ApiRepository.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_categorize_clothe_detail);
        if(getIntent().hasExtra("action")){
            String action = getIntent().getStringExtra("action");
            id = getIntent().getIntExtra("id", 0);
            if (getIntent().hasExtra("item_category")){
                categoryItem = getIntent().getParcelableExtra("item_category");
                setFormData();
            }
            initView(action);
        }
    }

    public void initView(String action){
        mBinding.back.setOnClickListener(v -> finish());

        if (action.equals("detail")){
            mBinding.refresh.setOnRefreshListener(() -> {
                getCategoryData();
            });
            mBinding.btnSubmit.setVisibility(View.GONE);
            getCategoryData();
        }else if (action.equals("add")){
            mBinding.refresh.setEnabled(false);
            mBinding.txtAction.setText("Add Kategori Pakaian");
            mBinding.btnSubmit.setOnClickListener(v -> addData());
        }else if (action.equals("edit")){
            mBinding.refresh.setEnabled(false);
            mBinding.txtAction.setText("Edit Kategori Pakaian");
            mBinding.btnSubmit.setOnClickListener(v -> editData());
        }
    }

    private void getCategoryData(){
        repository.setDialog(this);
        repository.setApiListener(MainApplication.api.getCategorizeClothe(id),
                (object, codeResponse, rEnum) -> {
                    mBinding.refresh.setRefreshing(false);
                    if (rEnum == RepositoryEnum.SUCCESS) {
                        JSONObject data = object.optJSONObject("data");
                        categoryItem = gson.fromJson(data.toString(), CategorizeClotheItem.class);
                        setFormData();
                        //disable textfield
                        ViewUtil.disableInput(mBinding.edtNama);
                        ViewUtil.disableInput(mBinding.edtDeskripsi);
                        ViewUtil.disableInput(mBinding.edtFee);
                        mBinding.edtStatus.setClickable(false);
                    } else {
                        Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void setFormData(){
        mBinding.setCategoryDetail(categoryItem);
        mBinding.edtStatus.setChecked(categoryItem.getStatus() == 1);
    }

    public void addData(){
        JSONObject jsonObjectAdd = null;
        try {
            jsonObjectAdd = RequestParam.getJSONObject();
            jsonObjectAdd.put("clothe", mBinding.edtNama.getText());
            jsonObjectAdd.put("fee", mBinding.edtFee.getText());
            jsonObjectAdd.put("description", mBinding.edtDeskripsi.getText());
            jsonObjectAdd.put("status", mBinding.edtStatus.isChecked() ? 1 : 2);
            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
            repository.setDialog(this);
            repository.setApiListener(MainApplication.api.createCategorizeClothe(requestBody),
                    (object, codeResponse, rEnum) -> {
                        if (rEnum == RepositoryEnum.SUCCESS) {
                            String data = object.getString("message");
                            ToastUtils.showLong(data);
                            Intent i = new Intent(this,
                                    CategorizeClotheActivity.class);
                            startActivity(i);
                        } else {
                            GeneralUtil.errorToast(getApplicationContext(), object);
                        }
                    });
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void editData(){
        if (categoryItem.getId() != null){
            JSONObject jsonObjectAdd = null;
            try {
                jsonObjectAdd = RequestParam.getJSONObject();
                jsonObjectAdd.put("clothe", mBinding.edtNama.getText());
                jsonObjectAdd.put("fee", mBinding.edtFee.getText());
                jsonObjectAdd.put("description", mBinding.edtDeskripsi.getText());
                jsonObjectAdd.put("status", mBinding.edtStatus.isChecked() ? 1 : 2);
                RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
                repository.setDialog(this);
                repository.setApiListener(MainApplication.api.editCategorizeClothe(categoryItem.getId(),requestBody),
                        (object, codeResponse, rEnum) -> {
                            if (rEnum == RepositoryEnum.SUCCESS) {
                                String data = object.getString("message");
                                ToastUtils.showLong(data);
                                Intent i = new Intent(this,
                                        CategorizeClotheDetailActivity.class);
                                i.putExtra("action","detail");
                                i.putExtra("id",categoryItem.getId());
                                startActivity(i);
                            } else {
                                GeneralUtil.errorToast(getApplicationContext(), object);
                            }
                        });
            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(e.getMessage());
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

            }
        }else{
            Toast.makeText(getApplicationContext(), "Data Tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }

}