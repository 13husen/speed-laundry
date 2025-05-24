package com.speedlaundry.admin.base_app;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.firebase.messaging.FirebaseMessaging;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.http.retrofit.RetrofitCallBack;
import com.speedlaundry.admin.http.retrofit.RetrofitUtility;
import com.speedlaundry.admin.model.user.data.User;

import java.util.Calendar;
import java.util.function.Consumer;


/**
 * Created by Jovial on 5/16/2016.
 */
    public class MainApplication extends Application implements LifecycleObserver {



    public static boolean isActivityVisible() {
        return activityVisible;
    }

    private static boolean activityVisible;

    private static MainApplication mApplication;
    private static ProgressDialog progressDialog;
    public static RetrofitCallBack api;
    public Calendar quizCalendar;
    public static User user;
    public static Context context;
    public static FragmentManager fragmentManager;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if(MainApplication.user != null){
            MainApplication.user = user;
        }
    }

    public boolean checkUser(){
        return user == null;
    }

    public interface CompletedDialog {
        void dialogPressOK();
    }

    public static MainApplication getInstance() {
        return mApplication;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public float getScale(int size) {
        return size * getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        context = getApplicationContext();
        api = RetrofitUtility.getApiWithHeader(getApplicationContext());
        }



    public void showProgressDialog(Activity context) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                return;
            }
            progressDialog = new ProgressDialog(context);
            if (!context.isFinishing()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_layout);
            }
        } catch (IllegalStateException | IllegalArgumentException | NullPointerException ex) {
            Log.d("Progress Dialog", "State Error on Progress Dialog");
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void getTokenFcm(Consumer<String> callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        callback.accept(token);
                    } else {
                        callback.accept(null); // or handle error
                    }
                });
    }
    public void showDialog(Context context, String title, String msg, final CompletedDialog dialogProcess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setTitle(title)
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        MainApplication.fragmentManager = fragmentManager;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("MyApp", "App in background");
        activityVisible = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Log.d("MyApp", "App in foreground");
        activityVisible = true;
    }

/*public void LogoutProcess(Activity activity) {
        LocalData.getInstance().clearUserPreferences();
        LocalData.subscriber = null;
        Intent intent = new Intent(activity, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        activity.finish();
    }*/
}
