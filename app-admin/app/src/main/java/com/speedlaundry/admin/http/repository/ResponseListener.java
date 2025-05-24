package com.speedlaundry.admin.http.repository;

import org.json.JSONException;
import org.json.JSONObject;

public interface ResponseListener {
    void onResponseListener(JSONObject object, int codeResponse, RepositoryEnum rEnum) throws JSONException;
}