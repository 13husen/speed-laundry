package com.speedlaundryapp.userapp.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationInstanceService{
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Log.d("Tidak Berhasil","Failed To Get The token");
            }
            String token = task.getResult();
           //if task is failed then
        });
    }

}
