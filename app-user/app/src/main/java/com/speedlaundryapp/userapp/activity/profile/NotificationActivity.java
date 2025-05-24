package com.speedlaundryapp.userapp.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.NotificationAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityNotificationBinding;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.laundry_ui.TopupDetailActivity;
import com.speedlaundryapp.userapp.laundry_ui.TransactionDetailActivity;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.laundry.notification.NotificationItem;
import com.speedlaundryapp.userapp.model.laundry.notification.NotificationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    ActivityNotificationBinding mBinding;
    ApiRepository repo = ApiRepository.getInstance();
    ArrayList<NotificationItem> notifLaundry = new ArrayList<>();
    int page = 1, last_page= -1;
    boolean reloading;
    Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();
        getData();
    }
    public void initView(){
        mBinding.list.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!v.canScrollVertically(1)) {
                if (!reloading && page < last_page){
                    page++;
                    mBinding.refreshing.setVisibility(View.VISIBLE);
                    getData();
                    // Save state
                    recyclerViewState = mBinding.list.getLayoutManager().onSaveInstanceState();
                }
            }
        });
        mBinding.Backto.setOnClickListener(view -> {
            finish();
        });
        mBinding.refresh.setOnRefreshListener(() -> {
            resetRefresh();
        });
    }
    public void getData(){
        Map<String, Object> map = new HashMap<>();
        map.put("page",page);
        map.put("sort","id,desc");
        reloading = true;
        repo.setDialog(this);
        repo.setModelListener(MainApplication.api.getNotificationList(map),
                new PojoListener<WarpingResponse<NotificationResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<NotificationResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<NotificationItem> temp  = response.getData().getData();
                        last_page = response.getData().getLastPage();
                        notifLaundry.addAll(temp);
                        reloading = false;
                        mBinding.refreshing.setVisibility(View.INVISIBLE);
                        NotificationAdapter notificationAdapter = new NotificationAdapter(notifLaundry, NotificationActivity.this);
                        mBinding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mBinding.list.setAdapter(notificationAdapter);
                        notificationAdapter.setListener(notificationItem -> {
                            Intent i = new Intent(NotificationActivity.this,
                                    notificationItem.getType() == 1 ? TopupDetailActivity.class : TransactionDetailActivity.class);
                            i.putExtra("id",notificationItem.getTransactionId());
                            startActivity(i);
                        });
                        // Restore state
                        mBinding.list.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    }
                    @Override
                    public void onErrorListener(ResponseError error, int code) {
                    }
                });
    }
    public void resetRefresh(){
        notifLaundry.clear();
        page = 1;
        getData();
    }
}