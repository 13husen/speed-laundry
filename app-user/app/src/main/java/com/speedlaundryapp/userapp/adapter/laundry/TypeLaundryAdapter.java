package com.speedlaundryapp.userapp.adapter.laundry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.databinding.LaundryTypeAdapterLayoutBinding;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryItem;
import com.speedlaundryapp.userapp.utils.GeneralUtil;

import java.util.ArrayList;

public class TypeLaundryAdapter extends RecyclerView.Adapter<TypeLaundryAdapter.Holder> {
    ArrayList<TypeLaundryItem> categories;
    CategoryAdapterClickListener listener;
    Activity activity;

    public TypeLaundryAdapter(ArrayList<TypeLaundryItem> categories, Activity activity
    ) {
        this.categories = categories;
        this.activity = activity;
    }

    public void setListener(CategoryAdapterClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.laundry_type_adapter_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryTypeAdapterLayoutBinding mBinding = holder.mBinding;
        TypeLaundryItem category = categories.get(position);
        mBinding.setItemCategory(category);
        mBinding.textFee.setText(GeneralUtil.convertRupiah(Integer.parseInt(category.getFee())));
        mBinding.getRoot().setOnClickListener(v->{
            listener.onCategoryClickListener(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LaundryTypeAdapterLayoutBinding mBinding;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
    public interface CategoryAdapterClickListener{
        void onCategoryClickListener(TypeLaundryItem categorizeClotheItem);
    }

}
