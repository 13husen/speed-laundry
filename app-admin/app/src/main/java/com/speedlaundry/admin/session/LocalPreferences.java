package com.speedlaundry.admin.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.speedlaundry.admin.activity.LoginActivity;
import com.speedlaundry.admin.base_app.MainApplication;

public class LocalPreferences {

    public static final String SPCore = "core";
    public static final String SPToken = "token";

    private final Context context;
    private final SharedPreferences sp;

    public static LocalPreferences getInstance(){
        return new LocalPreferences(MainApplication.getInstance().getContext());
    }
    public LocalPreferences(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(SPCore,Context.MODE_PRIVATE);
    }
    public void saveString(String key, String value){
        sp.edit().putString(key,value).apply();
    }
    public boolean hasToken(){
        return !TextUtils.isEmpty(sp.getString(SPToken, null));
    }
    public String getToken(){
        return sp.getString(SPToken,"");
    }
    public void getLogout(){
        sp.edit().clear().apply();
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
