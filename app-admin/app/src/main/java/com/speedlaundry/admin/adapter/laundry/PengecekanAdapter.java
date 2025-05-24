package com.speedlaundry.admin.adapter.laundry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.speedlaundry.admin.R;
import com.speedlaundry.admin.databinding.LaundryPengecekanAdapterBinding;
import com.speedlaundry.admin.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundry.admin.model.laundry.pengecekan.PengecekanData;
import com.speedlaundry.admin.utils.GeneralUtil;

import java.util.ArrayList;

public class PengecekanAdapter extends RecyclerView.Adapter<PengecekanAdapter.Holder> {
    ArrayList<PengecekanData> pengecekan;
    PengecekanAdapterClickListener listener;
    RefreshDataListener refreshDataListener;
    Activity activity;

    public PengecekanAdapter(ArrayList<PengecekanData> pengecekan, Activity activity
    ) {
        this.pengecekan = pengecekan;
        this.activity = activity;
    }

    public void setListener(PengecekanAdapterClickListener listener, RefreshDataListener refreshDataListener) {
        this.listener = listener;
        this.refreshDataListener = refreshDataListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.laundry_pengecekan_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryPengecekanAdapterBinding mBinding = holder.mBinding;
        PengecekanData orderData = pengecekan.get(position);
        mBinding.InfoKategori.setText("Kategori : "+orderData.getCategory().getName()+" ("+
                GeneralUtil.convertRupiah(Integer.parseInt(orderData.getCategory().getFee())) +")");
        mBinding.InfoBobot.setText("Jumlah Pakaian : "+ orderData.getPcs());
        if (orderData.getItemType() == 1){
            mBinding.InfoItemType.setText("Kiloan");
            mBinding.lytKiloan.setVisibility(View.VISIBLE);
            mBinding.InfoBobot.setText("Bobot : "+ orderData.getWeight()+" KG");
            mBinding.InfoPcs.setText("Jumlah Pakaian : "+ orderData.getPcs());
        }else if (orderData.getItemType() == 2){
            mBinding.InfoItemType.setText("Per-Pakaian");
            mBinding.lytPakaian.setVisibility(View.VISIBLE);
            if (orderData.getClothe().size() > 0){
                String pakaianText = "";
                for (CategorizeClotheItem pakaian: orderData.getClothe()) {
                    pakaianText += "- "+pakaian.getClothe()+" ("+
                            GeneralUtil.convertRupiah(Integer.parseInt(pakaian.getFee()))+") \n";
                }
                mBinding.InfoPakaian.setText(pakaianText);
            }
        }
        mBinding.btnDelete.setOnClickListener(v->{
            listener.onDeleteClickListener(orderData,position);
        });
    }

    @Override
    public int getItemCount() {
        return pengecekan.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LaundryPengecekanAdapterBinding mBinding;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }


    public interface PengecekanAdapterClickListener{
        void onDeleteClickListener(PengecekanData pengecekanData,int position);
    }
    public interface RefreshDataListener{
        void refreshListener();
    }
}
