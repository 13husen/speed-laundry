package com.speedlaundry.admin.http.retrofit;

import android.content.Context;

import com.speedlaundry.admin.http.ApiConstant;

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
