package com.speedlaundryapp.userapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.databinding.DialogLogoutAppBinding;

public class DialogExit extends DialogFragment {
    DialogLogoutAppBinding mBinding;
    FragmentActivity activity;

    public DialogExit(FragmentActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_logout_app, container, false);
        mBinding.titleDialog.setText(activity.getResources().getString(R.string.are_you_sure_want_to_exit));
        mBinding.confirm.setText(activity.getResources().getString(R.string.OK));
        mBinding.confirm.setOnClickListener(view -> {
            activity.finish();
        });
        mBinding.cancelDialog.setOnClickListener(view -> {
            getDialog().dismiss();
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBinding.getRoot();
    }

}