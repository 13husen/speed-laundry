package com.speedlaundry.admin.laundry_ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.adapter.laundry.UserAdapter;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.ActivityUserManageLaundryBinding;
import com.speedlaundry.admin.dialog.DialogFilterListUserLaundry;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.PojoListener;
import com.speedlaundry.admin.model.ResponseError;
import com.speedlaundry.admin.model.WarpingResponse;
import com.speedlaundry.admin.model.user.data.User;
import com.speedlaundry.admin.model.user.data.UserResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdminManageActivity extends AppCompatActivity {
    ActivityUserManageLaundryBinding mBinding;
    ApiRepository repo = ApiRepository.getInstance();
    ArrayList<User> userList = new ArrayList<>();
    String fltrName, fltrType = null;
    int page = 1, last_page, fltrStatus = -1;
    boolean reloading;
    Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_manage_laundry);
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
        mBinding.btnFilter.setOnClickListener(v -> {
            DialogFilterListUserLaundry dailyFilter = new DialogFilterListUserLaundry();
            dailyFilter.show(getSupportFragmentManager(), null);
            dailyFilter.setSubmitListener((vDialog, name, tipe, status) -> {
                fltrName = name;
                fltrStatus = status;
                fltrType = tipe;
                page = 1;
                userList.clear();
                getData();
            });
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

        if (fltrStatus != -1){
            map.put("status", fltrStatus);
        }
        if (fltrName != null && fltrName != ""){
            map.put("name", fltrName);
        }
        if (fltrType != null){
            map.put("type", fltrType.toLowerCase());
        }else{
            map.put("type", "admin|karyawan|driver");
        }

        map.put("page",page);
        map.put("sort","id,desc");
        reloading = true;
        repo.setDialog(this);
        repo.setModelListener(MainApplication.api.getUsers(map),
                new PojoListener<WarpingResponse<UserResponse>>() {
                    @Override
                    public void onResponseListener(WarpingResponse<UserResponse> response) {
                        mBinding.refresh.setRefreshing(false);
                        List<User> temp  = response.getData().getData();
                        last_page = response.getData().getLastPage();
                        userList.addAll(temp);
                        reloading = false;
                        mBinding.refreshing.setVisibility(View.INVISIBLE);
                        UserAdapter categoryAdapter = new UserAdapter(userList, UserAdminManageActivity.this);
                        mBinding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mBinding.list.setAdapter(categoryAdapter);

                        categoryAdapter.setListener(categoryItem -> {
//                            Intent i = new Intent(UserAdminManageActivity.this,
//                                    CategoryDetailActivity.class);
//                            i.putExtra("action","detail");
//                            i.putExtra("id",categoryItem.getId());
//                            startActivity(i);
                        }, () -> resetRefresh());
                        // Restore state
                        mBinding.list.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                    }

                    @Override
                    public void onErrorListener(ResponseError error, int code) {

                    }
                });
    }
    public void resetRefresh(){
        userList.clear();
        page = 1;
        getData();
    }
}