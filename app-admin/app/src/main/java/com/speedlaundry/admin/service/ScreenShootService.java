package com.speedlaundry.admin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public abstract class ScreenShootService extends BroadcastReceiver {
    View view;
    Context context;
    Intent intent;
    Bitmap bitmap;
    File imageFile;
    public final static String TAG = ScreenShootService.class.getName();

    public ScreenShootService(View view) {
        this.view = view;
    }


    private void takeScreenShot(){
        Date now  = new Date();
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try{
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            View v1 =  view;
            v1.setDrawingCacheEnabled(true);
             bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot();
        }catch (Throwable e){
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private void openScreenshot() {
        intent.setAction(TAG);
        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra("image", uri.getPath());
        context.sendBroadcast(intent);
        getImage(uri, bitmap);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        takeScreenShot();
    }

    public abstract void getImage(Uri uri, Bitmap bitmap);

}
