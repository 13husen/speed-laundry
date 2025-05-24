package com.speedlaundryapp.userapp.data;

import com.speedlaundryapp.userapp.model.user.auth.AccountData;

public class Callback implements Data.CompletedDataProcess{
    @Override
    public void completedLoginSuccess(AccountData model, String status) {

    }

    @Override
    public void completedLoginFail(String msg) {

    }
}
