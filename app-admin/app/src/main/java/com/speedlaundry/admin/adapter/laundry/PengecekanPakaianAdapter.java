package com.speedlaundry.admin.adapter.laundry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.databinding.LaundryPengecekanPakaianAdapterLayoutBinding;
import com.speedlaundry.admin.model.laundry.categorize_clothes.CategorizeClotheItem;

import java.util.ArrayList;

public class PengecekanPakaianAdapter extends RecyclerView.Adapter<PengecekanPakaianAdapter.Holder> {
    ArrayList<CategorizeClotheItem> categories;
    CategoryAdapterClickListener listener;
    RefreshDataListener refreshDataListener;
    Activity activity;

    public PengecekanPakaianAdapter(ArrayList<CategorizeClotheItem> categories, Activity activity
    ) {
        this.categories = categories;
        this.activity = activity;
    }

    public void setListener(CategoryAdapterClickListener listener, RefreshDataListener refreshDataListener) {
        this.listener = listener;
        this.refreshDataListener = refreshDataListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.laundry_pengecekan_pakaian_adapter_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryPengecekanPakaianAdapterLayoutBinding mBinding = holder.mBinding;
        CategorizeClotheItem category = categories.get(position);
        mBinding.setItemCategory(category);
        mBinding.btnDelete.setOnClickListener(v -> {
            listener.onDeleteClickListener(category,position);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LaundryPengecekanPakaianAdapterLayoutBinding mBinding;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }


    public interface CategoryAdapterClickListener{
        void onDeleteClickListener(CategorizeClotheItem categorizeClotheItem, int position);
    }
    public interface RefreshDataListener{
        void refreshListener();
    }
}
