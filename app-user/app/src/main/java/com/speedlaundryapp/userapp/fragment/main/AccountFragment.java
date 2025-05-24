package com.speedlaundryapp.userapp.fragment.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.HomeActivity;
import com.speedlaundryapp.userapp.activity.laundry_user.UserDataActivity;
import com.speedlaundryapp.userapp.activity.profile.ChangePasswordActivity;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.FragmentAccountBinding;
import com.speedlaundryapp.userapp.dialog.DialogLogout;
import com.speedlaundryapp.userapp.model.user.data.User;

public class AccountFragment extends Fragment implements View.OnClickListener {

    FragmentAccountBinding mBinding;
    User user;
    //Dialog
    DialogLogout logoutDialog;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mBinding = DataBindingUtil.bind(view);
        user = MainApplication.user;
        updateUI();
        logoutDialog = new DialogLogout(getActivity());


        assert mBinding != null;

        return mBinding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        mBinding.setting.changePassword.setOnClickListener(this);
        mBinding.setting.changeUserdata.setOnClickListener(this);
        mBinding.setting.logOut.setOnClickListener(this);
        mBinding.refresh.setOnRefreshListener(() -> {
            HomeActivity.userUpdate();
            user = MainApplication.user;
            updateUI();
            new Handler().postDelayed(() -> mBinding.refresh.setRefreshing(false), 1000);
        });
    }

    private void updateUI() {
        mBinding.toolbarHomex.textUserName1.setText(user.getName());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.change_password) {
            startActivity(new Intent(getContext(), ChangePasswordActivity.class));
        } else if (id == R.id.change_userdata) {
            startActivity(new Intent(getContext(), UserDataActivity.class));
        } else if (id == R.id.log_out) {
            assert getFragmentManager() != null;
            logoutDialog.show(getFragmentManager(), null);
        } else {
            throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }


}