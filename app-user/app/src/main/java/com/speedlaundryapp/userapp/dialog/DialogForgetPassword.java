package com.speedlaundryapp.userapp.dialog;

import android.annotation.SuppressLint;
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

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.DialogForgetPasswordBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;


public class DialogForgetPassword extends DialogFragment {
    DialogForgetPasswordBinding mBinding;
    SubmitNoHandPhoneListener   submitNoHandPhone;
    boolean passwordVisible = false;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_forget_password, container,
                false);
        Dialog dialog = getDialog();
        Resources res =  getContext().getResources();
        dialog.getWindow().setBackgroundDrawable(res.getDrawable(R.drawable.dialog_inset));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return mBinding.getRoot();
    }

    public void setSubmitNoHandPhone(SubmitNoHandPhoneListener submitNoHandPhone) {
        this.submitNoHandPhone = submitNoHandPhone;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.close.setOnClickListener(v->{
            dismiss();
        });
        mBinding.submit.setOnClickListener(v->{
            if (passwordVisible){
                submitNoHandPhone.onNoHandPhone(mBinding.getEmail(),mBinding.edtPassword.getText().toString(), getDialog());
            }else{
                JSONObject jsonObjectAdd = null;
                try {
                    jsonObjectAdd = RequestParam.getJSONObject();
                    jsonObjectAdd.put("email", mBinding.getEmail());
                    ApiRepository repository = ApiRepository.getInstance();
                    RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
                    repository.setDialogLoading(getFragmentManager());
                    repository.setDialogError(true);
                    repository.setApiListener(MainApplication.api.check(requestBody),
                            (object, codeResponse, rEnum) -> {
                                if (rEnum == RepositoryEnum.SUCCESS) {
                                    mBinding.edtPassword.setVisibility(View.VISIBLE);
                                    passwordVisible = true;
                                }else {
                                    String data = object.getString("message");
                                    ToastUtils.showLong(data);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface SubmitNoHandPhoneListener {
        void onNoHandPhone(String s,String pass, Dialog dialog);
    }
}
