package com.speedlaundry.admin.http.repository;

import com.speedlaundry.admin.model.ResponseError;

import org.json.JSONException;

public interface PojoListener<T> {
      void onResponseListener( T response) throws JSONException;
      void onErrorListener(ResponseError error, int code);
}
