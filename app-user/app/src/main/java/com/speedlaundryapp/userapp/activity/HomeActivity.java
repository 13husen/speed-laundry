package com.speedlaundryapp.userapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.SPUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.profile.NotificationActivity;
import com.speedlaundryapp.userapp.base.NotificationConstant;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityHomeBinding;
import com.speedlaundryapp.userapp.dialog.DialogExit;
import com.speedlaundryapp.userapp.fragment.main.AccountFragment;
import com.speedlaundryapp.userapp.fragment.main.HomeFragment;
import com.speedlaundryapp.userapp.fragment.main.TransactionFragment;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitCallBack;
import com.speedlaundryapp.userapp.http.retrofit.RetrofitUtility;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.UserParam;
import com.speedlaundryapp.userapp.model.user.info_user.MyAccount;
import com.speedlaundryapp.userapp.utils.ClearCacheUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding mBinding;
    static RetrofitCallBack api;
    static Activity activity;
    ApiRepository repo = ApiRepository.getInstance();
    static FragmentManager fragmentManager;
    //Dialog
    DialogExit quitDialog;
    private final boolean isExit = false;
    String tokenFCM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        initTokenFcm();
        api = RetrofitUtility.getApiWithHeader(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        activity = this;
        repo.setDialogLoading(getSupportFragmentManager());
        repo.setDialogError(true);

        ClearCacheUtils.deleteCache(this);
        getData();
        getBottomMenu();
        getInitClickListener();
    }

    void initTokenFcm(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Failed", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    tokenFCM = task.getResult();
                });
    }


    private void getBottomMenu() {
        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            if (item.getItemId() == R.id.action_home) {
                fragment = new HomeFragment();
            } else if (item.getItemId() == R.id.action_riwayat) {
                fragment = new TransactionFragment();
            } else if (item.getItemId() == R.id.action_profile) {
                fragment = new AccountFragment();
            } else {
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            return loadFragment(fragment);
        });

    }


    private void getInitClickListener() {
        // disable-first
        mBinding.notificationBell.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
        });
    }

    void getData() {
        repo.setModelListener(api.myAccount(), new PojoListener<MyAccount>() {
            @Override
            public void onResponseListener(MyAccount response) throws JSONException {
                MainApplication.getInstance().setUser(response.getData().getUser());
                MainApplication.user = response.getData().getUser();
                updateTokenFCM(MainApplication.user.getId());
                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
                    loadFragment(new HomeFragment());
                }
            }

            @Override
            public void onErrorListener(ResponseError error, int code) {
                Toast.makeText(getApplicationContext(), "ERRHOME: " +  error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void userUpdate() {
        ApiRepository repository = ApiRepository.getInstance();
        repository.setDialog(activity);
        repository.setModelListener(api.myAccount(), new PojoListener<MyAccount>() {
            @Override
            public void onResponseListener(MyAccount response) {
                MainApplication.user = response.getData().getUser();
            }

            @Override
            public void onErrorListener(ResponseError error, int code) {
            }
        });
    }

    public void updateTokenFCM(int id) throws JSONException {
        JSONObject jsonObject = UserParam.getJSONObject();
        jsonObject.put("id", id);
        jsonObject.put("fcm_token",tokenFCM);
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), ParseObject.requestParse);
        ApiRepository apiRepository = ApiRepository.getInstance();
        apiRepository.setApiListener(MainApplication.api.updateUser(id,requestBody),
                (object, codeResponse, rEnum) -> {
                    if (rEnum == RepositoryEnum.SUCCESS) {
                        String data = object.getString("message");
                    }
                });
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

            fragmentManager.executePendingTransactions();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        int index = manager.getBackStackEntryCount()-1;
        if (index > 0) {
            manager.popBackStack();
        } else {
            quitDialog = new DialogExit(HomeActivity.this);
            quitDialog.show(getSupportFragmentManager(), null);
        }
    }

    private final BroadcastReceiver mMessageReceiveNotificationPage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onNotificationShow(intent.getStringExtra("title"), intent.getStringExtra("message"));
        }
    };

    private void onNotificationShow(String title, String description) {
        SPUtils.getInstance().put(NotificationConstant.UNREAD_NOTIFICATION, NotificationConstant.getUnreadNotification() + 1);
        NotificationConstant.clearNotificationShow();
    }



    @Override
    protected void onResume() {
        super.onResume();
        getData();

        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiveNotificationPage),
                new IntentFilter("NotificationPage")
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!NotificationConstant.getTitleNotification().equals("")) {
            onNotificationShow(NotificationConstant.getTitleNotification(), NotificationConstant.getMessageNotification());
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiveNotificationPage);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



}