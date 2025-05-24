package com.speedlaundryapp.userapp.service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.speedlaundryapp.userapp.activity.HomeActivity;
import com.speedlaundryapp.userapp.laundry_ui.TopupDetailActivity;
import com.speedlaundryapp.userapp.laundry_ui.TransactionDetailActivity;
public class FirebaseBackgroundService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        String transactionId = intent.getStringExtra("transaction_id");
        Intent serviceIntent;
        if (type != ""){
            int tipe = Integer.parseInt(type);
            if (tipe == 1){
                serviceIntent = new Intent(context,TopupDetailActivity.class);
            }else{
                serviceIntent = new Intent(context,TransactionDetailActivity.class);
            }
        }else{
            serviceIntent = new Intent(context, HomeActivity.class);
        }
        serviceIntent.putExtra("id",Integer.parseInt(transactionId));
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(serviceIntent);
    }
}