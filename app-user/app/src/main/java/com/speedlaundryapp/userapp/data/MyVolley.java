package com.speedlaundryapp.userapp.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyVolley {
    private static RequestQueue mRequestQueue;
    private static Context mContext;

    private MyVolley() {
        // no instances
    }

    static void init(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context, new OkHttp3Stack());
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mContext == null)
            mContext = context;

        if (mRequestQueue == null) {
            init(mContext);
            return mRequestQueue;
        }

        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }
}
