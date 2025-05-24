package com.speedlaundryapp.userapp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.HomeActivity;
import com.speedlaundryapp.userapp.activity.laundry_user.UserRequestLaundryActivity;
import com.speedlaundryapp.userapp.activity.laundry_user.UserTopupActivity;
import com.speedlaundryapp.userapp.adapter.laundry.CategoryAdapter;
import com.speedlaundryapp.userapp.adapter.laundry.TransactionAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.FragmentHomeBinding;
import com.speedlaundryapp.userapp.dialog.DialogInfoLaundry;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.laundry_ui.TransactionDetailActivity;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryItem;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryResponse;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionResponse;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryItem;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryResponse;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;
    ArrayList<TransactionItem> transactionLaundry = new ArrayList<>();
    ArrayList<CategoryItem> kategoriLaundry = new ArrayList<>();
    ArrayList<TypeLaundryItem> tipeLaundry = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
                container, false);
        mBinding.refresh.setOnRefreshListener(() -> {
        HomeActivity.userUpdate();
            getTransaction();
            new Handler().postDelayed(() -> { mBinding.refresh.setRefreshing(false); },1000);
        });
        initClickListener();
        initData();
        return mBinding.getRoot();
    }
    public void initData(){
        User userData = MainApplication.user;
        mBinding.txtWallet.setText(GeneralUtil.convertRupiah(Integer.parseInt(userData.getBalance())));
        mBinding.txtNama.setText(userData.getName());
        getTransaction();
        getTypeData();
        getCategoryData();
    }
    public void initClickListener(){
        mBinding.btnTopup.setOnClickListener(v -> {
            Intent intents = new Intent(getContext(), UserTopupActivity.class);
            startActivity(intents);
        });
        mBinding.textRiwayatMore.setOnClickListener(v -> {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new TransactionFragment())
                    .addToBackStack(null)
                    .commit();
        });
        mBinding.btnRequest.setOnClickListener(v -> {
            Intent intents = new Intent(getContext(), UserRequestLaundryActivity.class);
            startActivity(intents);
        });
    }
    public void getTransaction(){
        Map<String, Object> map = new HashMap<>();
        map.put("type", "2");
        map.put("page",0);
        map.put("sort","id,desc");
        ApiRepository repo = ApiRepository.getInstance();
        repo.setDialog(getActivity());
        repo.setModelListener(MainApplication.api.getTransactions(map),
                new PojoListener<WarpingResponse<TransactionResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<TransactionResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<TransactionItem> temp  = response.getData().getData();
                        transactionLaundry.clear();
                        transactionLaundry.addAll(temp);
                        if (transactionLaundry.size() > 0){
                            mBinding.txtNoTrx.setVisibility(View.GONE);
                        }else{
                            mBinding.txtNoTrx.setVisibility(View.VISIBLE);
                        }
                        TransactionAdapter categoryAdapter = new TransactionAdapter(transactionLaundry, getActivity(),"list");
                        mBinding.listRiwayat.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        mBinding.listRiwayat.setAdapter(categoryAdapter);

                        categoryAdapter.setListener(transactionItem -> {
                            Intent i = new Intent(getActivity().getApplicationContext(),
                                    TransactionDetailActivity.class);
                            i.putExtra("id",transactionItem.getId());
                            startActivity(i);
                        }, () -> {});
                    }
                    @Override
                    public void onErrorListener(ResponseError error, int code) {

                    }
                });
    }

    public void getCategoryData(){
        Map<String, Object> map = new HashMap<>();
        ApiRepository repo = ApiRepository.getInstance();
        repo.setDialog(getActivity());
        repo.setModelListener(MainApplication.api.getCategoriesLaundry(map),
                new PojoListener<WarpingResponse<CategoryResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<CategoryResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<CategoryItem> temp  = response.getData().getData();
                        kategoriLaundry.addAll(temp);
                        CategoryAdapter categoryAdapter = new CategoryAdapter(kategoriLaundry, getActivity());
                        mBinding.listKategori.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        mBinding.listKategori.setAdapter(categoryAdapter);

                        categoryAdapter.setListener(categoryItem -> {
                            DialogInfoLaundry dialogInfo = new DialogInfoLaundry(tipeLaundry, categoryItem);
                            dialogInfo.show(getFragmentManager(), null);
                        }, () -> {
                        });
                    }
                    @Override
                    public void onErrorListener(ResponseError error, int code) {

                    }
                });
    }
    public void getTypeData(){
        ApiRepository repo = ApiRepository.getInstance();
        Map<String, Object> map = new HashMap<>();
        repo.setDialog(getActivity());
        repo.setModelListener(MainApplication.api.getTypeLaundry(map),
                new PojoListener<WarpingResponse<TypeLaundryResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<TypeLaundryResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<TypeLaundryItem> temp  = response.getData().getData();
                        tipeLaundry.addAll(temp);
                    }
                    @Override
                    public void onErrorListener(ResponseError error, int code) {}
                });
    }
}