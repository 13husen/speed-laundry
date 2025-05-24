package com.speedlaundryapp.userapp.laundry_ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.PengecekanAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityPengecekanLaundryBinding;
import com.speedlaundryapp.userapp.dialog.DialogPengecekanFormLaundry;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundryapp.userapp.model.laundry.pengecekan.PengecekanData;
import com.speedlaundryapp.userapp.utils.GeneralUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class PengecekanLaundryActivity extends AppCompatActivity {
    ActivityPengecekanLaundryBinding mBinding;
    PengecekanData datacek;
    private int trxId = -1;
    Gson gson = new Gson();
    ArrayList<PengecekanData> listPengecekan = new ArrayList<>();
    ApiRepository repository = ApiRepository.getInstance();
    PengecekanAdapter pengecekanAdapter;
    Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pengecekan_laundry);
        if(getIntent().hasExtra("trx_id")){
            trxId = getIntent().getIntExtra("trx_id", 0);
        }
        initView();
    }

    public void initView(){
        mBinding.back.setOnClickListener(v -> finish());
        pengecekanAdapter = new PengecekanAdapter(listPengecekan, PengecekanLaundryActivity.this);
        mBinding.listChek.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mBinding.listChek.setAdapter(pengecekanAdapter);
        mBinding.btnSubmit.setOnClickListener(v -> {
            if (listPengecekan.size() > 0){
                try {
                    JSONArray orderParent = new JSONArray();
                    for (PengecekanData dataPesanan: listPengecekan) {
                        JSONObject innerOrder = new JSONObject();
                        innerOrder.put("item_type",dataPesanan.getItemType());
                        innerOrder.put("category",dataPesanan.getCategory().getId());
                        innerOrder.put("type_laundry",dataPesanan.getTypeLaundry().getId());
                        if (dataPesanan.getItemType() == 1){
                            innerOrder.put("weight",dataPesanan.getWeight());
                            innerOrder.put("pcs",dataPesanan.getPcs());
                        }else{
                            innerOrder.put("pcs",dataPesanan.getClothe().size());
                            JSONArray clothes = new JSONArray();
                            for (CategorizeClotheItem pakaianPesanan: dataPesanan.getClothe()) {
                                JSONObject innerClothes = new JSONObject();
                                innerClothes.put("clothe_id", pakaianPesanan.getId());
                                clothes.put(innerClothes);
                            }
                            innerOrder.put("clothe",clothes);
                        }
                        orderParent.put(innerOrder);
                    }
                    submitDetails(orderParent);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(PengecekanLaundryActivity.this, "Tambahkan Pencucian Terlebih Dahulu !", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.btnAdd.setOnClickListener(v -> {
            DialogPengecekanFormLaundry dialogCek = new DialogPengecekanFormLaundry();
            dialogCek.show(getSupportFragmentManager(), null);
            dialogCek.setSubmitListener((vDialog, pengecekanData) -> {
                listPengecekan.add(pengecekanData);
                pengecekanAdapter.notifyDataSetChanged();
            });
        });
        pengecekanAdapter.setListener((categoryItem,position) -> {
            listPengecekan.remove(position);
            pengecekanAdapter.notifyDataSetChanged();
        }, () -> {
            Toast.makeText(getApplicationContext(), "Tester", Toast.LENGTH_SHORT).show();
        });
        // Restore state
        mBinding.listChek.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    public void submitDetails(JSONArray order) throws JSONException {
        if (trxId != -1){
            JSONObject jsonObjectAdd = null;
            jsonObjectAdd = RequestParam.getJSONObject();
            jsonObjectAdd.put("order", order);
            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
            repository.setDialog(this);
            repository.setApiListener(MainApplication.api.updateDetails(trxId,requestBody),
                    (object, codeResponse, rEnum) -> {
                        if (rEnum == RepositoryEnum.SUCCESS) {
                            String data = object.getString("data");
                            ToastUtils.showLong(data);
                        } else {
                            GeneralUtil.errorToast(getApplicationContext(), object);
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Data Tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }
}