package com.speedlaundry.admin.dialog;

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

import com.bumptech.glide.Glide;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.databinding.DialogLoadingBinding;

public class DialogMainLoading extends DialogFragment {
    DialogLoadingBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_loading,container,false);
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Glide.with(getContext())
                .load(R.drawable.splash_screen)
                .into(mBinding.ivSplash);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        return mBinding.getRoot();
    }
}
