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

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.databinding.DialogFilterListLaundryBinding;

public class DialogFilterListLaundry extends DialogFragment {
    DialogFilterListLaundryBinding mBinding;
    SubmitListener submitListener;
    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter_list_laundry,container,false);
        mBinding.close.setOnClickListener(view -> {
            getDialog().dismiss();
        });
        mBinding.submit.setOnClickListener(v->{
            if(submitListener != null){
                submitListener.onSubmitListener(v,
                        !mBinding.edtNama.getText().toString().isEmpty() ? mBinding.edtNama.getText().toString() : null ,
                        mBinding.edtStatus.isChecked() ? 1 :2);
            }
            dismiss();
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBinding.getRoot();
    }

    public interface SubmitListener{
        void onSubmitListener(View v, String name, int status);
    }
}
