package com.speedlaundryapp.userapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserParam {
    public static JSONObject getJSONObject() throws JSONException {
        return new JSONObject();
//        return new JSONObject(getParam());
    }

//    public static String getParam(){
//        return "{\n" +
//                "    \"facebook_token\": \"\",\n" +
//                "    \"client_id\": \"1\",\n" +
//                "    \"client_secret\": \"VHQXu2YGC2eEBGIC1EOW61tVCmfYJNcaVfCQtJlN\",\n" +
//                "    \"fcm_token\": \"e1-3Of-pyjefAxRngA78Uz:APA91bF2wPGTjkockwiGCehutrskX3oJiZcXrd5XmPV0CL28Hw3bRcIpLDUUUl-8OpktqzEypgFEfSlX9n1Z3hi4HpjyFHtko7q7c3n_MshGWVhqier3fI7I64NeNNXWmhmuscEaO7qs\",\n" +
//                "    \"session_data\": {\n" +
//                "        \"device_id\": \"2819382938989309230\",\n" +
//                "        \"imei\": \"4C2A4EAD-F426-46EA-8611-5A250FB2881C\",\n" +
//                "        \"brand\": \"Apple\",\n" +
//                "        \"model\": \"iPhone 6S\"\n" +
//                "    }\n" +
//                "}";
//    }
}
