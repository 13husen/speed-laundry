package com.speedlaundry.admin.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.databinding.DialogErrorBinding;
import com.speedlaundry.admin.model.ResponseError;

import java.util.ArrayList;
import java.util.Map;

public class DialogErrorException extends DialogFragment {
    int i = R.layout.dialog_error;
    ResponseError error;
    DialogErrorBinding mBinding;
    SubmitListener submitInterFace;

    public void setError(ResponseError error) {
        this.error = error;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,i, container, false);
        String desc = "";
        if (error.getData() != null){
            for(Map.Entry<String, ArrayList<String>> entry : error.getData().entrySet()) {
                String key = entry.getKey();
                ArrayList<String> value = entry.getValue();
                desc += "\u2023 " + value.get(0) + "\n";
            }
        }else{
            for(Map.Entry<String, ArrayList<String>> entry : error.getErrors().entrySet()) {
                String key = entry.getKey();
                ArrayList<String> value = entry.getValue();
                desc += "\u2023 " + value.get(0) + "\n";
            }
        }

        mBinding.setError(error);
        mBinding.textDesc.setText(desc);
        Dialog dialog = getDialog();
        Resources res =  getContext().getResources();
        dialog.getWindow().setBackgroundDrawable(res.getDrawable(R.drawable.dialog_inset));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding.close.setOnClickListener(v->{
            dialog.dismiss();
        });
        mBinding.submit.setOnClickListener(v->{
            if(submitInterFace != null){
                submitInterFace.onSubmitListener(v);
            }
            dialog.dismiss();
        });
        return mBinding.getRoot();
    }

    public void setSubmitInterFace(SubmitListener submitInterFace) {
        this.submitInterFace = submitInterFace;
    }

    public interface SubmitListener{
        void onSubmitListener(View v);
    }
}
