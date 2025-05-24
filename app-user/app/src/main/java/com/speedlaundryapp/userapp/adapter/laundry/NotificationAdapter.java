package com.speedlaundryapp.userapp.adapter.laundry;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.databinding.NotificationAdapterBinding;
import com.speedlaundryapp.userapp.model.laundry.notification.NotificationItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {
    List<NotificationItem> notificationModels;
    Context c;
    NotificationAdapterClickListener listener;

    public NotificationAdapter(List<NotificationItem> notificationModels, Context c) {
        this.notificationModels = notificationModels;
        this.c = c;
    }
    public void setListener(NotificationAdapterClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_adapter, parent, false);
        return new Holder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NotificationItem notificationModel = notificationModels.get(position);

        //2020-10-20T10:04:20.000000Z
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(notificationModel.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        holder.mBinding.notifTime.setText(formattedDate);

        holder.mBinding.setNotification(notificationModel);
        holder.mBinding.getRoot().setOnClickListener(v->{
            listener.onNotificationClickListener(notificationModel);
        });
    }
    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        NotificationAdapterBinding mBinding;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
    public interface NotificationAdapterClickListener{
        void onNotificationClickListener(NotificationItem notificationItem);
    }
}
