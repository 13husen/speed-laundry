package com.speedlaundryapp.userapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.databinding.DialogLoadingAppBinding;

public class DialogLoading extends Dialog {
    DialogLoadingAppBinding mBinding;
    Context context;

    public DialogLoading(@NonNull Context context) {
        super(context);
    }

    public DialogLoading(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogLoading(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
//        getContext().setStyle(DialogFragment.STYLE_NO_FRAME,0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_loading_app, null, false);
        setContentView(mBinding.getRoot());
        Glide.with(getContext())
                .load(R.drawable.splash_screen)
                .into(mBinding.ivSplash);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public interface SubmitListener{
        void onSubmitListener(View v);
    }
}

