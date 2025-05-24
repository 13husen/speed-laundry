package com.speedlaundry.admin.data;

import com.speedlaundry.admin.model.user.auth.AccountData;

public class Callback implements Data.CompletedDataProcess{
    @Override
    public void completedLoginSuccess(AccountData model, String status) {

    }

    @Override
    public void completedLoginFail(String msg) {

    }
}
