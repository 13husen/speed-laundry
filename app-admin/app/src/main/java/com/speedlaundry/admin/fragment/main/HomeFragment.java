package com.speedlaundry.admin.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.activity.HomeActivity;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.FragmentHomeBinding;
import com.speedlaundry.admin.laundry_ui.AdminTopupActivity;
import com.speedlaundry.admin.laundry_ui.AdminTransactionActivity;
import com.speedlaundry.admin.laundry_ui.CategorizeClotheActivity;
import com.speedlaundry.admin.laundry_ui.CategoryActivity;
import com.speedlaundry.admin.laundry_ui.TypeLaundryActivity;
import com.speedlaundry.admin.laundry_ui.UserAdminActivity;
import com.speedlaundry.admin.laundry_ui.UserAdminManageActivity;
import com.speedlaundry.admin.model.user.data.User;

public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;
    User user;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
                container, false);
        user = MainApplication.user;
        mBinding.refresh.setOnRefreshListener(() -> {
        HomeActivity.userUpdate();
            new Handler().postDelayed(() -> { mBinding.refresh.setRefreshing(false); },1000);
        });
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        /* mBinding.cardSubsStat.setOnClickListener(this); */
        initClickListener();
        return mBinding.getRoot();
    }

    public void initClickListener(){
        if (user.getType().equals("admin")){
            mBinding.laundryCategory.setVisibility(View.VISIBLE);
            mBinding.laundryCategory.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), CategoryActivity.class));
            });
            mBinding.laundryCategorizeClothe.setVisibility(View.VISIBLE);
            mBinding.laundryCategorizeClothe.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), CategorizeClotheActivity.class));
            });
            mBinding.laundryType.setVisibility(View.VISIBLE);
            mBinding.laundryType.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), TypeLaundryActivity.class));
            });
            mBinding.laundryUserManage.setVisibility(View.VISIBLE);
            mBinding.laundryUserManage.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), UserAdminManageActivity.class));
            });

            mBinding.laundryUser.setVisibility(View.VISIBLE);
            mBinding.laundryUser.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), UserAdminActivity.class));
            });

            mBinding.laundryTopup.setVisibility(View.VISIBLE);
            mBinding.laundryTopup.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), AdminTopupActivity.class));
            });
        }
        mBinding.laundryTransaksi.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdminTransactionActivity.class));
        });

    }
}