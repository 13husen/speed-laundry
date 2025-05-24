package com.speedlaundry.admin.adapter.laundry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.LaundryTransactionAdapterLayoutBinding;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.http.retrofit.ParseObject;
import com.speedlaundry.admin.laundry_ui.PengecekanLaundryActivity;
import com.speedlaundry.admin.model.RequestParam;
import com.speedlaundry.admin.model.laundry.transaction.TransactionItem;
import com.speedlaundry.admin.model.user.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.RequestBody;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.Holder> {
    ArrayList<TransactionItem> transactions;
    TransactionAdapterClickListener listener;
    RefreshDataListener refreshDataListener;
    Activity activity;
    String keterangan;

    public TransactionAdapter(ArrayList<TransactionItem> transactions, Activity activity,String keterangan
    ) {
        this.transactions = transactions;
        this.activity = activity;
        this.keterangan = keterangan;
    }

    public void setListener(TransactionAdapterClickListener listener, RefreshDataListener refreshDataListener) {
        this.listener = listener;
        this.refreshDataListener = refreshDataListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.laundry_transaction_adapter_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryTransactionAdapterLayoutBinding mBinding = holder.mBinding;
        TransactionItem trx = transactions.get(position);
        mBinding.setItemTransaction(trx);
        PopupMenu popup = new PopupMenu(holder.itemView.getContext(), mBinding.btnMore);
        popup.inflate(R.menu.transaction_menu_option);
        User user = MainApplication.user;
        Menu popupMenu = popup.getMenu();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = inputFormat.parse(trx.getCreatedAt());
            String formattedDate = outputFormat.format(date);
            mBinding.txtDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (keterangan.equals("list")){
            switch (trx.getStatus()){
                case 1:
                    mBinding.txtStatus.setText("Di Proses");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_blue_600));
                    break;
                case 2:
                    mBinding.txtStatus.setText("Di Antar");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_orange_600));
                    break;
                case 3:
                    mBinding.txtStatus.setText("Pengecekan");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_indigo_600));
                    break;
                case 4:
                    mBinding.txtStatus.setText("Pending Pembayaran");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_teal_700));
                    break;
                case 6:
                    mBinding.txtStatus.setText("Di Cuci");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_yellow_900));
                    break;
                case 7:
                    mBinding.txtStatus.setText("Antar Pulang");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_purple_600));
                    break;
                case 8:
                    mBinding.txtStatus.setText("Selesai");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_light_green_800));
                    break;
                case 9:
                    mBinding.txtStatus.setText("Di Cancel");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_red_700));
                    break;
                case 11:
                    mBinding.txtStatus.setText("Kedaluwarsa");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_700));
                    break;
                default:
                    break;
            }
            if (user.getType().equals("admin")){
                mBinding.btnMore.setVisibility(View.GONE);
            }else{
                popupMenu.findItem(R.id.handle).setVisible(true);
            }
        }else{
            switch (trx.getStatus()){
                case 1:
                    popupMenu.findItem(R.id.jemput).setVisible(true);
                    mBinding.txtStatus.setText("Di Proses");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_blue_600));
                    break;
                case 2:
                    popupMenu.findItem(R.id.selesaijemput).setVisible(true);
                    mBinding.txtStatus.setText("Di Antar");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_orange_600));
                    break;
                case 3:
                    popupMenu.findItem(R.id.ceklaundry).setVisible(true);
                    mBinding.txtStatus.setText("Pengecekan");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_indigo_600));
                    break;
                case 4:
                    popupMenu.findItem(R.id.terima).setVisible(true);
                    popupMenu.findItem(R.id.tolak).setVisible(true);
                    if (trx.getType() == 1){
                        switch (trx.getPayment().getStatus()){
                            case 0:
                                mBinding.txtStatus.setText("Pending Bayar");
                                mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_600));
                                break;
                            case 2:
                                mBinding.txtStatus.setText("Konfirmasi");
                                mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_blue_700));
                                break;
                            case 3:
                                mBinding.txtStatus.setText("Gagal");
                                mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_red_600));
                                break;
                            case 4:
                                mBinding.txtStatus.setText("Sukses");
                                mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_green_600));
                                break;
                        }

                    }else {
                        mBinding.txtStatus.setText("Pending Pembayaran");
                        mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_teal_700));
                    }
                    break;
                case 6:
                    popupMenu.findItem(R.id.cuciselesainantar).setVisible(true);
                    mBinding.txtStatus.setText("Di Cuci");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_yellow_900));
                    break;
                case 7:
                    popupMenu.findItem(R.id.selesaiantar).setVisible(true);
                    mBinding.txtStatus.setText("Antar Pulang");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_purple_600));
                    break;
                case 8:
                    mBinding.btnMore.setVisibility(View.INVISIBLE);
                    mBinding.txtStatus.setText("Selesai");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_light_green_800));
                    break;
                case 9:
                    mBinding.btnMore.setVisibility(View.INVISIBLE);
                    mBinding.txtStatus.setText("Di Cancel");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_red_700));
                    break;
                case 11:
                    mBinding.btnMore.setVisibility(View.INVISIBLE);
                    mBinding.txtStatus.setText("Kedaluwarsa");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_700));
                    break;
                default:
                    break;
            }
        }

        mBinding.btnMore.setOnClickListener(v -> {
            popup.show();
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.handle) {
                    setHandle(v.getContext(), trx.getId());
                } else if (id == R.id.terima) {
                    updateStatus(v.getContext(), trx.getId(), item.getTitle().toString(), 2);
                } else if (id == R.id.tolak) {
                    updateStatus(v.getContext(), trx.getId(), item.getTitle().toString(), 1);
                } else if (id == R.id.ceklaundry) {
                    Intent i = new Intent(v.getContext(), PengecekanLaundryActivity.class);
                    i.putExtra("trx_id", trx.getId());
                    v.getContext().startActivity(i);
                } else if (id == R.id.jemput || id == R.id.jemputtoko || id == R.id.selesaijemput
                        || id == R.id.cuciselesainantar || id == R.id.selesaiantar) {
                    updateStatus(v.getContext(), trx.getId(), item.getTitle().toString(), -1);
                } else {
                    // default case - tidak melakukan apa-apa
                }
                return false;
            });
        });
        mBinding.getRoot().setOnClickListener(v->{
            listener.onTransactionClickListener(trx);
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LaundryTransactionAdapterLayoutBinding mBinding;
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

    public void setHandle(Context context, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Handle Transaksi ?");
        builder.setMessage("Transaksi ini akan diubah ke status dihandle oleh anda!");
        builder.setNegativeButton("Kembali", (dialog, which) -> {});
        builder.setPositiveButton("Ubah",
                (dialog, which) -> {
                    ApiRepository repository = ApiRepository.getInstance();
                    repository.setDialog(activity);
                    repository.setApiListener(MainApplication.api.updateHandle(id),
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

    public void updateStatus(Context context, int id,String title, int doAction){
        JSONObject jsonObjectAdd = null;
        try {
            jsonObjectAdd = RequestParam.getJSONObject();
            if (doAction != -1){
                jsonObjectAdd.put("status", doAction);
            }
            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle(title +" ?");
            if (doAction == 1 || doAction == 2){
                String action = doAction == 1 ? "Tolak" : "Terima";
                builder.setMessage("Apakah anda yakin ingin " + action + " Pembayaran ini ?");
            }else{
                builder.setMessage("Transaksi ini akan diubah ke status selanjutnya!");
            }
            builder.setNegativeButton("Kembali", (dialog, which) -> {});
            builder.setPositiveButton("Ubah",
                    (dialog, which) -> {
                        ApiRepository repository = ApiRepository.getInstance();
                        repository.setDialog(activity);
                        repository.setApiListener(MainApplication.api.updateStatus(id, requestBody),
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
        } catch (JSONException e) {
            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public interface TransactionAdapterClickListener{
        void onTransactionClickListener(TransactionItem transactionItem);
    }
    public interface RefreshDataListener{
        void refreshListener();
    }
}