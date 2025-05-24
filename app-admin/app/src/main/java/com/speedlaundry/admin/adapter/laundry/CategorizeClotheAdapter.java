package com.speedlaundry.admin.adapter.laundry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.LaundryCategorizeClotheAdapterLayoutBinding;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.laundry_ui.CategorizeClotheDetailActivity;
import com.speedlaundry.admin.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundry.admin.utils.GeneralUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CategorizeClotheAdapter extends RecyclerView.Adapter<CategorizeClotheAdapter.Holder> {
    ArrayList<CategorizeClotheItem> categories;
    CategoryAdapterClickListener listener;
    RefreshDataListener refreshDataListener;
    Activity activity;

    public CategorizeClotheAdapter(ArrayList<CategorizeClotheItem> categories, Activity activity
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
        View view = inflater.inflate(R.layout.laundry_categorize_clothe_adapter_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryCategorizeClotheAdapterLayoutBinding mBinding = holder.mBinding;
        CategorizeClotheItem category = categories.get(position);
        mBinding.setItemCategory(category);
        mBinding.textFee.setText(" : " + GeneralUtil.convertRupiah(Integer.parseInt(category.getFee())));
        mBinding.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), mBinding.btnMore);
            popup.inflate(R.menu.basic_option_menu);
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.edit) {
                    Intent i = new Intent(v.getContext(), CategorizeClotheDetailActivity.class);
                    i.putExtra("action", "edit");
                    i.putExtra("id", category.getId());
                    i.putExtra("item_category", category);
                    v.getContext().startActivity(i);
                    return true;
                } else if (itemId == R.id.delete) {
                    deleteCategory(v.getContext(), category.getId());
                    return true;
                } else {
                    return false;
                }

            });
            popup.show();
        });
        mBinding.getRoot().setOnClickListener(v->{
            listener.onCategoryClickListener(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LaundryCategorizeClotheAdapterLayoutBinding mBinding;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    @BindingAdapter("textTime")
    public static void setTextTime(TextView textTime, String time){
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat textDateFormat = new SimpleDateFormat("hh.mm aa");
        try {
            Date date =  dateFormat.parse(time);
            String dateString = textDateFormat.format(date);
            textTime.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(Context context, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Hapus Data ?");
        builder.setMessage("Data yang terhapus tidak bisa dikembalikan");
        builder.setNegativeButton("Kembali", (dialog, which) -> {});
        builder.setPositiveButton("Hapus",
                (dialog, which) -> {
                    ApiRepository repository = ApiRepository.getInstance();
                    repository.setDialog(activity);
                    repository.setApiListener(MainApplication.api.deleteCategory(id),
                            (object, codeResponse, rEnum) -> {
                                if (rEnum == RepositoryEnum.SUCCESS) {
                                    String data = object.getString("message");
                                    ToastUtils.showLong(data);
                                    refreshDataListener.refreshListener();

                                } else {
                                    Toast.makeText(context, object.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface CategoryAdapterClickListener{
        void onCategoryClickListener(CategorizeClotheItem categorizeClotheItem);
    }
    public interface RefreshDataListener{
        void refreshListener();
    }
}
