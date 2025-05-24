package com.speedlaundryapp.userapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.databinding.DialogFilterListTransactionLaundryBinding;
public class DialogFilterListTransactionLaundry extends DialogFragment {
    DialogFilterListTransactionLaundryBinding mBinding;
    SubmitListener submitListener;
    String[] transactionStatus=new String[]{"Proses","Antar","Pengecekan","Pending Pembayaran","Cuci","Antar Pulang","Selesai","Di Cancel","Kadaluwarsa"};
    String[] paymentStatus=new String[]{"Pending Bayar","Menunggu Konfrimasi","Gagal","Sukses"};
    String type = "transaction"; int status = -1;
    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        if (mArgs != null){
            if (mArgs.containsKey("tipe") && mArgs.getString("tipe") != null)
                type = mArgs.getString("tipe");
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter_list_transaction_laundry,container,false);
        mBinding.close.setOnClickListener(view -> {
            getDialog().dismiss();
        });
        if (type.equals("topup_selesai") || type.equals("transaction_selesai")){
            mBinding.lytStatus.setVisibility(View.GONE);
        }

        mBinding.submit.setOnClickListener(v->{
            int position = mBinding.edtStatus.getSelectedItemPosition();
            if (type.equals("transaction")){
                switch (position){
                    case 0:
                        status = 1;
                        break;
                    case 1:
                        status = 2;
                        break;
                    case 2:
                        status = 3;
                        break;
                    case 3:
                        status = 4;
                        break;
                    case 4:
                        status = 6;
                        break;
                    case 5:
                        status = 7;
                        break;
                    case 6:
                        status = 8;
                        break;
                    case 7:
                        status = 9;
                        break;
                    case 8:
                        status = 11;
                        break;
                }
            }else{
                if (position == 4)
                    status = -1;
                else
                    status = position+1;
            }
            if(submitListener != null){
                submitListener.onSubmitListener(v,
                        !mBinding.edtInvoice.getText().toString().isEmpty() ? mBinding.edtInvoice.getText().toString() : null
                        ,status);
            }
            dismiss();
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ArrayAdapter<String> statusList= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,  type.equals("transaction") ? transactionStatus : paymentStatus);
        statusList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.edtStatus.setAdapter(statusList);
        return mBinding.getRoot();
    }
    public interface SubmitListener{
        void onSubmitListener(View v, String invoice, int status);
    }
}
