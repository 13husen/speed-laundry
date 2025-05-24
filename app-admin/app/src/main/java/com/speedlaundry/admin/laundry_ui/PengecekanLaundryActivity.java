package com.speedlaundry.admin.laundry_ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.adapter.laundry.PengecekanAdapter;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.ActivityPengecekanLaundryBinding;
import com.speedlaundry.admin.dialog.DialogPengecekanFormLaundry;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.PojoListener;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.http.retrofit.ParseObject;
import com.speedlaundry.admin.model.RequestParam;
import com.speedlaundry.admin.model.ResponseError;
import com.speedlaundry.admin.model.WarpingResponse;
import com.speedlaundry.admin.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundry.admin.model.laundry.pengecekan.PengecekanData;
import com.speedlaundry.admin.model.laundry.type_laundry.TypeLaundryItem;
import com.speedlaundry.admin.model.laundry.type_laundry.TypeLaundryResponse;
import com.speedlaundry.admin.utils.GeneralUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class PengecekanLaundryActivity extends AppCompatActivity {
    ActivityPengecekanLaundryBinding mBinding;
    PengecekanData datacek;
    private int trxId = -1;
    Gson gson = new Gson();
    TypeLaundryItem tipeSelect;
    List<TypeLaundryItem> typeLaundry = new ArrayList<>();
    ArrayList<PengecekanData> listPengecekan = new ArrayList<>();
    ApiRepository repository = ApiRepository.getInstance();
    PengecekanAdapter pengecekanAdapter;
    Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pengecekan_laundry);
        mBinding.refresh.setEnabled(false);
        if(getIntent().hasExtra("trx_id")){
            trxId = getIntent().getIntExtra("trx_id", 0);
        }
        initData();
        initView();
    }
    public void initData(){
        getType();
    }
    public void initView(){
        mBinding.back.setOnClickListener(v -> finish());
        pengecekanAdapter = new PengecekanAdapter(listPengecekan, PengecekanLaundryActivity.this);
        mBinding.listChek.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mBinding.listChek.setAdapter(pengecekanAdapter);
        mBinding.btnSubmit.setOnClickListener(v -> {
            if (listPengecekan.size() > 0 && tipeSelect.getId() != null){
                try {
                    JSONArray orderParent = new JSONArray();
                    for (PengecekanData dataPesanan: listPengecekan) {
                        JSONObject innerOrder = new JSONObject();
                        innerOrder.put("item_type",dataPesanan.getItemType());
                        innerOrder.put("category",dataPesanan.getCategory().getId());
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
                Toast.makeText(PengecekanLaundryActivity.this, "Mohon isi Tipe Waktu Pencucian Atau Tambahkan Pencucian Terlebih Dahulu !", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.btnAdd.setOnClickListener(v -> {
            DialogPengecekanFormLaundry dialogCek = new DialogPengecekanFormLaundry();
            dialogCek.show(getSupportFragmentManager(), null);
            dialogCek.setSubmitListener((vDialog, pengecekanData) -> {
                listPengecekan.add(pengecekanData);
                if (listPengecekan.size() > 0){
                    mBinding.lytListPencucian.setVisibility(View.VISIBLE);
                }
                pengecekanAdapter.notifyDataSetChanged();
            });
        });
        pengecekanAdapter.setListener((categoryItem,position) -> {
            listPengecekan.remove(position);
            pengecekanAdapter.notifyDataSetChanged();
            if (listPengecekan.size() == 0){
                mBinding.lytListPencucian.setVisibility(View.GONE);
            }
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
            jsonObjectAdd.put("type_laundry",tipeSelect.getId());
            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
            repository.setDialog(this);
            repository.setApiListener(MainApplication.api.updateDetails(trxId,requestBody),
                    (object, codeResponse, rEnum) -> {
                        if (rEnum == RepositoryEnum.SUCCESS) {
                            String data = object.getString("message");
                            ToastUtils.showLong(data);
                            finish();
                        } else {
                            GeneralUtil.errorToast(getApplicationContext(), object);
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Data Tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }
    public void getType(){
        ApiRepository repo = ApiRepository.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("status","1");
        repo.setModelListener(MainApplication.api.getTypeLaundry(map),
                new PojoListener<WarpingResponse<TypeLaundryResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<TypeLaundryResponse> response) {
                        List<TypeLaundryItem> temp  = response.getData().getData();
                        typeLaundry.addAll(temp);
                        List<String> tipeStrng = new ArrayList<>();
                        for (TypeLaundryItem itemtype : temp){
                            tipeStrng.add(itemtype.getNama());
                        }
                        ArrayAdapter<String> typeadapter = new ArrayAdapter<>(PengecekanLaundryActivity.this,R.layout.custom_spinner_laundry,R.id.name,tipeStrng);
                        typeadapter.setDropDownViewResource(R.layout.custom_spinner_laundry);
                        mBinding.edtTypeLaundry.setAdapter(typeadapter);
                        mBinding.edtTypeLaundry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tipeSelect = typeLaundry.get(position);
                                mBinding.txtOngkosTipe.setVisibility(View.VISIBLE);
                                mBinding.txtOngkosTipe.setText("Ongkos "+tipeSelect.getNama()+": " + GeneralUtil.convertRupiah(Integer.parseInt(tipeSelect.getFee())));
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                    @Override
                    public void onErrorListener(ResponseError error, int code) {
                    }
                });
    }

}