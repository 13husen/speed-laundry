package com.speedlaundryapp.userapp.laundry_ui;


import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityAdminTransactionBinding;
import com.speedlaundryapp.userapp.fragment.laundry.transaction.TransactionHandledFragment;
import com.speedlaundryapp.userapp.fragment.main.TransactionFragment;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitCallBack;
import com.speedlaundryapp.userapp.model.user.data.User;

import java.util.ArrayList;
import java.util.List;


public class AdminTransactionActivity extends AppCompatActivity {
    ActivityAdminTransactionBinding mBinding;
    static RetrofitCallBack api;
    User user;
    static Activity activity;
    ApiRepository repo = ApiRepository.getInstance();
    Gson gson = new Gson();
    static EditText token;
    static FragmentManager fragmentManager;
    private final boolean isExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_admin_transaction);
        mBinding.backtoWeb.setOnClickListener(v -> {
            finish();
        });
        user = MainApplication.user;
        mBinding.TabLayoutHome.setupWithViewPager(mBinding.PagerHome);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(new TransactionFragment(),"Semua");
        viewPagerAdapter.addFragment(new TransactionHandledFragment(),user.getType().equals("admin") ? "Selesai" : "Aktif" );
        mBinding.PagerHome.setAdapter(viewPagerAdapter);

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}