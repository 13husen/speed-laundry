package com.speedlaundry.admin.http.retrofit;

import android.content.Context;
import android.text.TextUtils;

import com.speedlaundry.admin.session.LocalPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    Context context;
    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        LocalPreferences preferences = new LocalPreferences(context);

        String token = preferences.getToken();
        if(!TextUtils.isEmpty(token)){
            builder.addHeader("Authorization", "Bearer "+token)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("accept","application/json")
                    .addHeader("Connection","close")
                    .build();
        }else {
            builder.addHeader("Content-Type", "application/json")
                    .addHeader("accept","application/json")
                    .addHeader("Connection","close")
                    .build();
        }
        return chain.proceed(builder.build());
    }
}
