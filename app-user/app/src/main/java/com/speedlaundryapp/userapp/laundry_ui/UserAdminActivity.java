package com.speedlaundryapp.userapp.laundry_ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.UserAdapter;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.ActivityUserLaundryBinding;
import com.speedlaundryapp.userapp.dialog.DialogFilterListUserLaundry;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.PojoListener;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.model.user.data.UserResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdminActivity extends AppCompatActivity {
    ActivityUserLaundryBinding mBinding;
    ApiRepository repo = ApiRepository.getInstance();
    ArrayList<User> userList = new ArrayList<>();
    String fltrName, fltrType = null;
    int page = 1, last_page, fltrStatus = -1;
    boolean reloading;
    Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_laundry);
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
            Bundle args = new Bundle();
            args.putString("tipe", "user");
            dailyFilter.setArguments(args);
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
        map.put("type","user");
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
                        UserAdapter categoryAdapter = new UserAdapter(userList, UserAdminActivity.this);
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