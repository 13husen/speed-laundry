package com.speedlaundryapp.userapp.adapter.laundry;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.activity.laundry_user.UserTransactionPayActivity;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.databinding.LaundryTransactionAdapterLayoutBinding;
import com.speedlaundryapp.userapp.laundry_ui.PengecekanLaundryActivity;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;
import com.speedlaundryapp.userapp.model.user.data.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        mBinding.btnMore.setVisibility(View.GONE);
        if (trx.getType() == 2){
            switch (trx.getStatus()){
                case 1:
                    mBinding.txtStatus.setText(R.string.di_proses);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_blue_600));
                    break;
                case 2:
                    mBinding.txtStatus.setText(R.string.di_antar);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_orange_600));
                    break;
                case 3:
                    mBinding.txtStatus.setText(R.string.pengecekan);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_indigo_600));
                    break;
                case 4:
                    mBinding.btnMore.setVisibility(View.VISIBLE);
                    popupMenu.findItem(R.id.bayar).setVisible(true);
                    mBinding.txtStatus.setText(R.string.pending_pembayaran);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_teal_700));
                    break;
                case 6:
                    mBinding.txtStatus.setText(R.string.di_cuci);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_yellow_900));
                    break;
                case 7:
                    mBinding.txtStatus.setText(R.string.di_antar_pulang);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_purple_600));
                    break;
                case 8:
                    mBinding.txtStatus.setText(R.string.selesai);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_light_green_800));
                    break;
                case 9:
                    mBinding.txtStatus.setText(R.string.di_cancel);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_red_700));
                    break;
                case 11:
                    mBinding.txtStatus.setText(R.string.kedaluwarsa);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_700));
                    break;
                default:
                    break;
            }
        }else if (trx.getType() == 1){
            switch (trx.getPayment().getStatus()){
                case 1:
                    mBinding.btnMore.setVisibility(View.VISIBLE);
                    popupMenu.findItem(R.id.konfirmasi).setVisible(true);
                    mBinding.txtStatus.setText(R.string.pending_pembayaran);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_600));
                    break;
                case 2:

                    mBinding.txtStatus.setText(R.string.menunggu_konfirmasi);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_blue_700));
                    break;
                case 3:
                    mBinding.txtStatus.setText(R.string.gagal);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_red_600));
                    break;
                case 4:
                    mBinding.txtStatus.setText(R.string.sukses);
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_green_600));
                    break;
                default:
                    mBinding.txtStatus.setText("N/A");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_900));
                    break;
            }

        }

        mBinding.btnMore.setOnClickListener(v -> {
            popup.show();
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.bayar) {
                    Intent ints = new Intent(v.getContext(), UserTransactionPayActivity.class);
                    ints.putExtra("trx_id", trx.getId());
                    v.getContext().startActivity(ints);
                } else if (id == R.id.konfirmasi) {
                    Intent i = new Intent(v.getContext(), PengecekanLaundryActivity.class);
                    i.putExtra("trx_id", trx.getId());
                    v.getContext().startActivity(i);
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


//    public void updateStatus(Context context, int id,String title, int doAction){
//        JSONObject jsonObjectAdd = null;
//        try {
//            jsonObjectAdd = RequestParam.getJSONObject();
//            if (doAction != -1){
//                jsonObjectAdd.put("status", doAction);
//            }
//            RequestBody requestBody = RequestBody.create(jsonObjectAdd.toString(), ParseObject.requestParse);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(true);
//            builder.setTitle(title +" ?");
//            if (doAction == 1 || doAction == 2){
//                String action = doAction == 1 ? "Tolak" : "Terima";
//                builder.setMessage("Apakah anda yakin ingin " + action + " Pembayaran ini ?");
//            }else{
//                builder.setMessage("Transaksi ini akan diubah ke status selanjutnya!");
//            }
//            builder.setNegativeButton("Kembali", (dialog, which) -> {});
//            builder.setPositiveButton("Ubah",
//                    (dialog, which) -> {
//                        ApiRepository repository = ApiRepository.getInstance();
//                        repository.setDialog(activity);
//                        repository.setApiListener(MainApplication.api.updateStatus(id, requestBody),
//                                (object, codeResponse, rEnum) -> {
//                                    if (rEnum == RepositoryEnum.SUCCESS) {
//                                        String data = object.getString("message");
//                                        ToastUtils.showLong(data);
//                                        refreshDataListener.refreshListener();
//                                    } else {
//                                        Toast.makeText(context, object.toString(), Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                    });
//            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        } catch (JSONException e) {
//            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }

    public interface TransactionAdapterClickListener{
        void onTransactionClickListener(TransactionItem transactionItem);
    }
    public interface RefreshDataListener{
        void refreshListener();
    }
}