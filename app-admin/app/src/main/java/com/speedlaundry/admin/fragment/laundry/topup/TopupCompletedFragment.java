package com.speedlaundry.admin.fragment.laundry.topup;

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

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.adapter.laundry.TopupAdapter;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.FragmentTransactionBinding;
import com.speedlaundry.admin.dialog.DialogFilterListTransactionLaundry;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.PojoListener;
import com.speedlaundry.admin.laundry_ui.TopupDetailActivity;
import com.speedlaundry.admin.model.ResponseError;
import com.speedlaundry.admin.model.WarpingResponse;
import com.speedlaundry.admin.model.laundry.transaction.TransactionItem;
import com.speedlaundry.admin.model.laundry.transaction.TransactionResponse;
import com.speedlaundry.admin.model.user.data.User;
import com.speedlaundry.admin.utils.ClearCacheUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopupCompletedFragment extends Fragment {
    ApiRepository repo = ApiRepository.getInstance();
    ArrayList<TransactionItem> transactionLaundry = new ArrayList<>();
    String fltrInvoice = null;
    int page = 1, last_page= -1;
    boolean reloading;
    Parcelable recyclerViewState;
    FragmentTransactionBinding mBinding;
    int selectedMonth = -1;
    User userData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction,
                container,false);
        ClearCacheUtils.deleteCache(getActivity());
        initView();
        getData();
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userData = MainApplication.user;
    }

    public void initView(){
        if (userData.getType().equals("admin")){
            mBinding.lytExport.setVisibility(View.VISIBLE);
        }
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
        mBinding.btnFilter.setOnClickListener(v -> {
            DialogFilterListTransactionLaundry dailyFilter = new DialogFilterListTransactionLaundry();
            Bundle args = new Bundle();
            args.putString("tipe","topup_selesai");
            dailyFilter.setArguments(args);
            dailyFilter.show(getFragmentManager(), null);
            dailyFilter.setSubmitListener((vDialog, name, status) -> {
                fltrInvoice = name;
                page = 1;
                transactionLaundry.clear();
                getData();
            });
        });
        mBinding.refresh.setOnRefreshListener(() -> {
            resetRefresh();
        });

    }
    public void getData(){
        Map<String, Object> map = new HashMap<>();
        if (fltrInvoice != null){
            map.put("invoice", fltrInvoice);
        }
        User userData = MainApplication.user;
        map.put("status","8");
        map.put("type", "1");
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
                        reloading = false;
                        mBinding.refreshing.setVisibility(View.INVISIBLE);
                        TopupAdapter categoryAdapter = new TopupAdapter(transactionLaundry, getActivity(),"action");
                        mBinding.list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        mBinding.list.setAdapter(categoryAdapter);

                        categoryAdapter.setListener(transactionItem -> {
                            Intent i = new Intent(getActivity().getApplicationContext(),
                                    TopupDetailActivity.class);
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
        getData();
    }
}