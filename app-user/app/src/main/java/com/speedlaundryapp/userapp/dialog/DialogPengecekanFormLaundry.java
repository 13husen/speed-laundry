package com.speedlaundryapp.userapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.PengecekanPakaianAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.DialogPengecekanFormLaundryBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheResponse;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryItem;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryResponse;
import com.speedlaundryapp.userapp.model.laundry.pengecekan.PengecekanData;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryItem;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DialogPengecekanFormLaundry extends DialogFragment {
    DialogPengecekanFormLaundryBinding mBinding;
    SubmitListener submitListener;
    // store order
    PengecekanData dataOrder;
    PengecekanPakaianAdapter cekanAdapter;
    TypeLaundryItem tipeSelect;
    CategoryItem kategoriSelect;
    List<TypeLaundryItem> typeLaundry = new ArrayList<>();
    List<CategoryItem> kategoriLaundry = new ArrayList<>();
    List<CategorizeClotheItem> pakaianLaundry = new ArrayList<>();
    ArrayList<CategorizeClotheItem> pakaianSelect = new ArrayList<>();
    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_pengecekan_form_laundry,container,false);
        dataOrder = new PengecekanData();
        mBinding.close.setOnClickListener(view -> getDialog().dismiss());
        mBinding.submit.setOnClickListener(v->{
            if(submitListener != null){
                int tipeCuci = mBinding.edtTipe.getSelectedItemPosition();
                int weight = Integer.parseInt(!mBinding.etWeight.getText().toString().isEmpty() ? mBinding.etWeight.getText().toString() : "0" );
                int pcs = Integer.parseInt(!mBinding.etPcs.getText().toString().isEmpty() ? mBinding.etPcs.getText().toString() : "0");
                if (tipeCuci == 1 && weight != 0 && pcs != 0 && tipeSelect.getId() != null && kategoriSelect.getId() != null){
                    dataOrder.setItemType(tipeCuci);
                    dataOrder.setWeight(weight);
                    dataOrder.setPcs(pcs);
                    dataOrder.setCategory(kategoriSelect);
                    dataOrder.setTypeLaundry(tipeSelect);
                    submitListener.onSubmitListener(v, dataOrder);
                    dismiss();
                }else if(tipeCuci == 2 && pakaianSelect.size() > 0 && tipeSelect.getId() != null && kategoriSelect.getId() != null){
                    dataOrder.setItemType(tipeCuci);
                    dataOrder.setCategory(kategoriSelect);
                    dataOrder.setTypeLaundry(tipeSelect);
                    dataOrder.setClothe(pakaianSelect);
                    submitListener.onSubmitListener(v, dataOrder);
                    dismiss();
                }
                else{
                    Toast.makeText(getContext(), "Mohon lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getData();
        cekanAdapter = new PengecekanPakaianAdapter(pakaianSelect, getActivity());
        mBinding.listPakaian.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listPakaian.setAdapter(cekanAdapter);
        mBinding.edtTipe.setSelection(0,false);
        mBinding.edtTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1){
                    mBinding.lytTipeKiloan.setVisibility(View.VISIBLE);
                    mBinding.lytTipePakaian.setVisibility(View.GONE);
                    pakaianSelect.clear();
                    mBinding.lytPakaian.setVisibility(View.GONE);
                }else{
                    mBinding.lytTipePakaian.setVisibility(View.VISIBLE);
                    mBinding.lytTipeKiloan.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cekanAdapter.setListener((categoryItem, position) -> {
            pakaianSelect.remove(position);
            if (pakaianSelect.size() > 0){
                mBinding.lytPakaian.setVisibility(View.VISIBLE);
            }else{
                mBinding.lytPakaian.setVisibility(View.GONE);
            }
            cekanAdapter.notifyDataSetChanged();
        }, () -> {
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBinding.getRoot();
    }
    public void getData(){
            getType();
            getKategori();
            getPakaian();
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
                        ArrayAdapter<String> typeadapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_laundry,R.id.name,tipeStrng);
                        typeadapter.setDropDownViewResource(R.layout.custom_spinner_laundry);
                        mBinding.edtTypeLaundry.setAdapter(typeadapter);
                        mBinding.edtTypeLaundry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tipeSelect = typeLaundry.get(position);
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
    public void getKategori(){
        ApiRepository repo = ApiRepository.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("status","1");
        repo.setModelListener(MainApplication.api.getCategoriesLaundry(map),
                new PojoListener<WarpingResponse<CategoryResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<CategoryResponse> response) {
                        List<CategoryItem> temp  = response.getData().getData();
                        kategoriLaundry.addAll(temp);
                        List<String> katStrng = new ArrayList<>();
                        for (CategoryItem itemkat : temp){
                            katStrng.add(itemkat.getName());
                        }
                        ArrayAdapter<String> katadapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_laundry,R.id.name,katStrng);
                        katadapter.setDropDownViewResource(R.layout.custom_spinner_laundry);
                        mBinding.edtKategori.setAdapter(katadapter);
                        mBinding.edtKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                kategoriSelect = kategoriLaundry.get(position);
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
    public void getPakaian(){
        ApiRepository repo = ApiRepository.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("status","1");
        repo.setModelListener(MainApplication.api.getCategorizeClotheLaundry(map),
                new PojoListener<WarpingResponse<CategorizeClotheResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<CategorizeClotheResponse> response) {
                        List<CategorizeClotheItem> temp  = response.getData().getData();
                        pakaianLaundry.addAll(temp);
                        List<String> pakaianStrng = new ArrayList<>();
                        for (CategorizeClotheItem itemkat : temp){
                            pakaianStrng.add(itemkat.getClothe());
                        }
                        ArrayAdapter<String> pakaianadapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_laundry,R.id.name,pakaianStrng);
                        pakaianadapter.setDropDownViewResource(R.layout.custom_spinner_laundry);
                        mBinding.edtClothe.setAdapter(pakaianadapter);
                        mBinding.edtClothe.setSelection(0,false);
                        mBinding.edtClothe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                pakaianSelect.add(pakaianLaundry.get(position));
                                if (pakaianSelect.size() > 0){
                                    mBinding.lytPakaian.setVisibility(View.VISIBLE);
                                }else{
                                    mBinding.lytPakaian.setVisibility(View.GONE);
                                }
                                cekanAdapter.notifyDataSetChanged();
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
    public interface SubmitListener{
        void onSubmitListener(View v, PengecekanData pengecekanData);
    }
}