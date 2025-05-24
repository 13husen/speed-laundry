package com.speedlaundryapp.userapp.laundry_ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.CategorizeClotheAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityCategorizeClotheBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorizeClotheActivity extends AppCompatActivity {
    ActivityCategorizeClotheBinding mBinding;
    ApiRepository repo = ApiRepository.getInstance();
    ArrayList<CategorizeClotheItem> kategoriLaundry = new ArrayList<>();
    String fltrName = null;
    int page = 1, last_page, fltrStatus = -1;
    boolean reloading;
    Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_categorize_clothe);
        initView();
        getData();
    }
    public void initView(){
        mBinding.list.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!v.canScrollVertically(1)) {
                if (!reloading && page < last_page){
                    page++;
                    mBinding.refreshing.setVisibility(View.VISIBLE);
                    getData();
                    // Save state
                    recyclerViewState = mBinding.list.getLayoutManager().onSaveInstanceState();
                }
            }
        });
//        mBinding.btnFilter.setOnClickListener(v -> {
//            DialogFilterListLaundry dailyFilter = new DialogFilterListLaundry();
//            dailyFilter.show(getSupportFragmentManager(), null);
//            dailyFilter.setSubmitListener((vDialog, name, status) -> {
//                fltrName = name;
//                fltrStatus = status;
//                page = 1;
//                kategoriLaundry.clear();
//                getData();
//            });
//        });
        mBinding.Backto.setOnClickListener(view -> {
            finish();
        });
        mBinding.refresh.setOnRefreshListener(() -> {
            resetRefresh();
        });
        mBinding.btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(CategorizeClotheActivity.this,
                    CategorizeClotheDetailActivity.class);
            i.putExtra("action","add");
            startActivity(i);
        });
    }
    public void getData(){
        Map<String, Object> map = new HashMap<>();
        if (fltrStatus != -1){
            map.put("status", fltrStatus);
        }
        if (fltrName != null){
            map.put("clothe", fltrName);
        }
        map.put("page",page);
        map.put("sort","id,desc");
        reloading = true;
        repo.setDialog(this);
        repo.setModelListener(MainApplication.api.getCategorizeClotheLaundry(map),
                new PojoListener<WarpingResponse<CategorizeClotheResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<CategorizeClotheResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<CategorizeClotheItem> temp  = response.getData().getData();
                        last_page = response.getData().getLastPage();
                        kategoriLaundry.addAll(temp);
                        reloading = false;
                        mBinding.refreshing.setVisibility(View.INVISIBLE);
                        CategorizeClotheAdapter categoryAdapter = new CategorizeClotheAdapter(kategoriLaundry, CategorizeClotheActivity.this);
                        mBinding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mBinding.list.setAdapter(categoryAdapter);

                        categoryAdapter.setListener(categoryItem -> {
                            Intent i = new Intent(CategorizeClotheActivity.this,
                                    CategorizeClotheDetailActivity.class);
                            i.putExtra("action","detail");
                            i.putExtra("id",categoryItem.getId());
                            startActivity(i);
                        }, () -> {
                            resetRefresh();
                        });
                        // Restore state
                        mBinding.list.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                    }

                    @Override
                    public void onErrorListener(ResponseError error, int code) {

                    }
                });
    }
    public void resetRefresh(){
        kategoriLaundry.clear();
        page = 1;
        getData();
    }
}