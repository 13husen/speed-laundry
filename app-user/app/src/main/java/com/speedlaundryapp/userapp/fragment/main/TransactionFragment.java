package com.speedlaundryapp.userapp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.TransactionAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.FragmentTransactionBinding;
import com.speedlaundryapp.userapp.dialog.DialogFilterListTransactionLaundry;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.laundry_ui.TopupDetailActivity;
import com.speedlaundryapp.userapp.laundry_ui.TransactionDetailActivity;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionResponse;
import com.speedlaundryapp.userapp.utils.ClearCacheUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionFragment extends Fragment {
    ApiRepository repo = ApiRepository.getInstance();
    ArrayList<TransactionItem> transactionLaundry = new ArrayList<>();
    String fltrInvoice = null;
    String type = "2";
    int page = 1, last_page, fltrStatus = -1;
    boolean reloading;
    Parcelable recyclerViewState;
    FragmentTransactionBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction,
                container,false);
        ClearCacheUtils.deleteCache(getActivity());
        initView();
        getData(type);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initView(){
        mBinding.list.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!v.canScrollVertically(1)) {
                if (!reloading && page < last_page){
                    page++;
                    mBinding.refreshing.setVisibility(View.VISIBLE);
                    getData(type);
                    // Save state
                    recyclerViewState = mBinding.list.getLayoutManager().onSaveInstanceState();
                }
            }
        });
        mBinding.TabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        type = "2";
                        break;
                    case 1:
                        type = "1";
                        break;
                    default:
                        type = null;
                }
                transactionLaundry.clear();
                getData(type);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mBinding.btnFilter.setOnClickListener(v -> {
            DialogFilterListTransactionLaundry dailyFilter = new DialogFilterListTransactionLaundry();
            dailyFilter.show(getFragmentManager(), null);
            dailyFilter.setSubmitListener((vDialog, invoice, status) -> {
                fltrInvoice = invoice;
                fltrStatus = status;
                page = 1;
                transactionLaundry.clear();
                getData(type);
            });
        });
        mBinding.refresh.setOnRefreshListener(() -> {
            resetRefresh();
        });

    }
    public void getData(String type){
        Map<String, Object> map = new HashMap<>();
        if (fltrStatus != -1){
            map.put("status", fltrStatus);
        }
        if (fltrInvoice != null){
            map.put("invoice", fltrInvoice);
        }
        if (type != null){
            map.put("type", type);
        }
        map.put("page",page);
        map.put("sort","id,desc");
        reloading = true;
        repo.setDialog(getActivity());
        repo.setModelListener(MainApplication.api.getTransactions(map),
                new PojoListener<WarpingResponse<TransactionResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<TransactionResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<TransactionItem> temp  = response.getData().getData();
                        last_page = response.getData().getLastPage();
                        transactionLaundry.addAll(temp);
                        if (transactionLaundry.size() > 0){
                            mBinding.txtNoTrx.setVisibility(View.GONE);
                        }else{
                            mBinding.txtNoTrx.setVisibility(View.VISIBLE);
                        }
                        reloading = false;
                        mBinding.refreshing.setVisibility(View.INVISIBLE);
                        TransactionAdapter categoryAdapter = new TransactionAdapter(transactionLaundry, getActivity(),"list");
                        mBinding.list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        mBinding.list.setAdapter(categoryAdapter);

                        categoryAdapter.setListener(transactionItem -> {
                            Intent i = new Intent(getActivity().getApplicationContext(),
                                    transactionItem.getType() == 2 ? TransactionDetailActivity.class : TopupDetailActivity.class);
                            i.putExtra("id",transactionItem.getId());
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
        transactionLaundry.clear();
        page = 1;
        getData(type);
    }
}