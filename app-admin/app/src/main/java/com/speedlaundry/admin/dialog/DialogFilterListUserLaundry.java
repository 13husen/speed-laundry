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
import com.speedlaundry.admin.databinding.DialogFilterListUserLaundryBinding;

public class DialogFilterListUserLaundry extends DialogFragment {
    DialogFilterListUserLaundryBinding mBinding;
    SubmitListener submitListener;
    String tipe = "admin";
    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        if (mArgs != null){
            if (mArgs.containsKey("tipe") && mArgs.getString("tipe") != null)
            tipe = "user";
        }
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter_list_user_laundry,container,false);
        mBinding.edtStatus.setSelection(4); // will select all option
        mBinding.edtUserType.setSelection(3); // will select all option
        mBinding.close.setOnClickListener(view -> {
            getDialog().dismiss();
        });

        if (tipe.equals("user")){
            mBinding.lytTipeUser.setVisibility(View.GONE);
        }
        mBinding.submit.setOnClickListener(v->{
            if(submitListener != null){
                String type = null;
                int status = -1;
                String name = !mBinding.edtNama.getText().toString().isEmpty() ? mBinding.edtNama.getText().toString() : null;
                if (!mBinding.edtUserType.getSelectedItem().toString().equalsIgnoreCase("all")){
                    type = mBinding.edtUserType.getSelectedItem().toString();
                }
                if (mBinding.edtStatus.getSelectedItemPosition() != 4){
                    status = mBinding.edtStatus.getSelectedItemPosition() + 1;
                }

                submitListener.onSubmitListener(v, name, type , status );
            }
            dismiss();
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBinding.getRoot();
    }

    public interface SubmitListener{
        void onSubmitListener(View v, String name, String tipe, int status);
    }
}
