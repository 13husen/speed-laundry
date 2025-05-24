package com.speedlaundry.admin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.databinding.DialogErrorBinding;
import com.speedlaundry.admin.model.ResponseError;

public class DialogError extends Dialog {
    ResponseError error ;
    DialogErrorBinding mBinding;
    SubmitListener submitListener;
    public DialogError(Context context, ResponseError responseError){
        super(context);
        this.error = responseError;
    }

    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
//        getContext().setStyle(DialogFragment.STYLE_NO_FRAME,0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_error, null, false);
        mBinding.setError(error);
        mBinding.close.setOnClickListener(v->{
            dismiss();
        });
        mBinding.submit.setOnClickListener(v->{
            if(submitListener != null){
                submitListener.onSubmitListener(v);
            }
            dismiss();
        });
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public interface SubmitListener{
        void onSubmitListener(View v);
    }
}
