package com.speedlaundryapp.userapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.DialogLogoutAppBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.session.LocalPreferences;


public class DialogLogout extends DialogFragment {
    DialogLogoutAppBinding mBinding;
    ApiRepository repository = ApiRepository.getInstance();

    LocalPreferences preferences;
    FragmentActivity activity;

    public DialogLogout(FragmentActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_logout_app, container, false);

        preferences = new LocalPreferences(activity);
        mBinding.confirm.setOnClickListener(view -> {
            logout();
        });
        mBinding.cancelDialog.setOnClickListener(view -> {
            getDialog().dismiss();
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBinding.getRoot();
    }

    private void logout() {
        repository.setDialog(getActivity());
        repository.setApiListener(MainApplication.api.logout(),
                (object, codeResponse, rEnum) -> {
                    if (rEnum == RepositoryEnum.SUCCESS) {
//                        logoutUserAccount();
                        getDialog().dismiss();
                        preferences.getLogout();
                    } else {
                        Toast.makeText(getContext(), object.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}

