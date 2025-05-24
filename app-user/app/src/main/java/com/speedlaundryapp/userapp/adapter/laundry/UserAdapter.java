package com.speedlaundryapp.userapp.adapter.laundry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.LaundryUserAdapterLayoutBinding;
import com.speedlaundryapp.userapp.dialog.DialogStatusLaundry;
import com.speedlaundryapp.userapp.http.repository.ApiRepository;
import com.speedlaundryapp.userapp.http.repository.RepositoryEnum;
import com.speedlaundryapp.userapp.http.retrofit.ParseObject;
import com.speedlaundryapp.userapp.model.RequestParam;
import com.speedlaundryapp.userapp.model.user.data.User;
import com.speedlaundryapp.userapp.utils.GeneralUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    ArrayList<User> users;
    UserAdapterClickListener listener;
    RefreshDataListener refreshDataListener;
    Activity activity;

    public UserAdapter(ArrayList<User> users, Activity activity
    ) {
        this.users = users;
        this.activity = activity;
    }

    public void setListener(UserAdapterClickListener listener, RefreshDataListener refreshDataListener) {
        this.listener = listener;
        this.refreshDataListener = refreshDataListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.laundry_user_adapter_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryUserAdapterLayoutBinding mBinding = holder.mBinding;
        User user = users.get(position);
        mBinding.setItemCategory(user);
        mBinding.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), mBinding.btnMore);
            popup.inflate(R.menu.basic_option_menu);
            popup.getMenu().findItem(R.id.delete).setVisible(false);
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.edit) {
                    DialogStatusLaundry dialogStatus = new DialogStatusLaundry();
                    dialogStatus.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), null);
                    dialogStatus.setSubmitListener((vDialog, status) -> {
                        editStatus(v.getContext(), user.getId(), status);
                    });
                    return true;
                } else {
                    return false;
                }
            });
            popup.show();
        });
        mBinding.getRoot().setOnClickListener(v->{
            listener.onUserClickListener(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LaundryUserAdapterLayoutBinding mBinding;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }


    public interface UserAdapterClickListener{
        void onUserClickListener(User categorizeClotheItem);
    }
    public interface RefreshDataListener{
        void refreshListener();
    }

    public void editStatus(Context context, int id, int status){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Edit Status ?");
        builder.setMessage("Data yang diubah tidak bisa dikembalikan");
        builder.setNegativeButton("Kembali", (dialog, which) -> {});
        builder.setPositiveButton("Submit",
                (dialog, which) -> {
                    try {
                        ApiRepository repository = ApiRepository.getInstance();
                        JSONObject jsonObjectAdd = null;
                        jsonObjectAdd = RequestParam.getJSONObject();
                        jsonObjectAdd.put("status", status );
                        RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
                        repository.setDialog(activity);
                        repository.setApiListener(MainApplication.api.editUser(id,requestBody),
                                (object, codeResponse, rEnum) -> {
                                    if (rEnum == RepositoryEnum.SUCCESS) {
                                        String data = object.getString("message");
                                        ToastUtils.showLong(data);
                                        refreshDataListener.refreshListener();

                                    } else {
                                        GeneralUtil.errorToast(context, object);
                                    }
                                });
                    } catch (JSONException e) {
                        Toast.makeText(context, "USRDAD: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
