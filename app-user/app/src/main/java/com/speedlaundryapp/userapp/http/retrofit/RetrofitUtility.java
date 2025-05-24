package com.speedlaundryapp.userapp.http.retrofit;

import android.content.Context;

import com.speedlaundryapp.userapp.http.ApiConstant;

public class RetrofitUtility {
        public static RetrofitCallBack getApiWithHeader(Context context){
        return RetrofitBuilder.getResponderWithHeader(ApiConstant.SERVER,context)
                .create(RetrofitCallBack.class);
    }
    public static RetrofitCallBack getApiWithoutHeader(){
        return RetrofitBuilder.getResponder(ApiConstant.SERVER)
                .create(RetrofitCallBack.class);
    }
}
