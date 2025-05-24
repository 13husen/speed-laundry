package com.speedlaundry.admin.data;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.http.ApiConstant;
import com.speedlaundry.admin.model.RegisterModel;
import com.speedlaundry.admin.model.user.auth.AccountData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private final Gson gson = new Gson();
    private static Data mData;
    public static RequestQueue queue;

    public interface CompletedDataProcess {
        void completedLoginSuccess(AccountData model, String status);

        void completedLoginFail(String msg);
    }

    public Data() {
        queue = MyVolley.getRequestQueue(MainApplication.getInstance());
    }

    public static Data getInstance() {
        if (mData == null) {
            mData = new Data();
        }
        return mData;
    }

    /* LOGIN REQUEST */
    public void Login(final RegisterModel inputModel, CompletedDataProcess dataProcess, Response.ErrorListener errorListener) {
        try {

            MainApplication.getInstance().quizCalendar = null;
            JSONObject params = new JSONObject(gson.toJson(inputModel));
            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                    ApiConstant.SERVER + ApiConstant.SOCIAL_LOGIN, params,
                    SuccessListener(dataProcess), errorListener){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String>  headers  = new HashMap<String, String>();
                    headers .put("Content-Type", "application/json");
                    headers .put("accept", "application/json");

                    return headers;
                }
            };

            myReq.setShouldCache(false);
            myReq.setRetryPolicy(new DefaultRetryPolicy(
                    ApiConstant.SERVER_TIMEOUT_MS,
                    ApiConstant.SERVER_RETRY,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            myReq.setTag(ApiConstant.TAG);
            queue.add(myReq);
        } catch (JSONException e) {
            e.printStackTrace();
            MainApplication.getInstance().dismissProgressDialog();
            Toast.makeText(MainApplication.getInstance(), "Network timeout", Toast.LENGTH_SHORT).show();
        }
    }



    private Response.Listener<JSONObject> SuccessListener(final CompletedDataProcess dataProcess) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                     String status = response.optString("status");
                    String message = response.optString("message");
                    JSONObject jObj = new JSONObject(response.toString());

                    AccountData model = gson.fromJson(jObj.toString(), AccountData.class);

                    if (status.equals("fail")) {
                        Log.d("Response FAIL: ", response.toString());

                        dataProcess.completedLoginFail(message);

                    } else {
                        Log.d("Response SUCCESS: ", response.toString());

                        dataProcess.completedLoginSuccess(model, status);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "Login:What");
                    dataProcess.completedLoginFail(e.getMessage());
                }
            }
        };
    }



}
