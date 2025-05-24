package com.speedlaundryapp.userapp.http.repository;

import com.speedlaundryapp.userapp.model.ResponseError;

import org.json.JSONException;

public interface PojoListener<T> {
      void onResponseListener( T response) throws JSONException;
      void onErrorListener(ResponseError error, int code);
}
