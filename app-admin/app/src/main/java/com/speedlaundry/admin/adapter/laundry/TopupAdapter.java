package com.speedlaundry.admin.adapter.laundry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.speedlaundry.admin.databinding.LaundryTopupAdapterLayoutBinding;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.http.retrofit.ParseObject;
import com.speedlaundry.admin.model.RequestParam;
import com.speedlaundry.admin.model.laundry.transaction.TransactionItem;
import com.speedlaundry.admin.model.user.data.User;
import com.speedlaundry.admin.utils.GeneralUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.RequestBody;

public class TopupAdapter extends RecyclerView.Adapter<TopupAdapter.Holder> {
    ArrayList<TransactionItem> transactions;
    TransactionAdapterClickListener listener;
    RefreshDataListener refreshDataListener;
    Activity activity;
    String keterangan;

    public TopupAdapter(ArrayList<TransactionItem> transactions, Activity activity, String keterangan
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
        View view = inflater.inflate(R.layout.laundry_topup_adapter_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LaundryTopupAdapterLayoutBinding mBinding = holder.mBinding;
        TransactionItem trx = transactions.get(position);
        mBinding.setItemTransaction(trx);
        PopupMenu popup = new PopupMenu(holder.itemView.getContext(), mBinding.btnMore);
        popup.inflate(R.menu.transaction_menu_option);
        User user = MainApplication.user;
        Menu popupMenu = popup.getMenu();
        double amountFee = trx.getPayment().getTotalFee();
        Double newFee = new Double(amountFee);
        int finalFee = newFee.intValue();
        mBinding.txtAmount.setText("Total :" + GeneralUtil.convertRupiah(finalFee));
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
            switch (trx.getPayment().getStatus()){
                case 1:
                    mBinding.txtStatus.setText("Pending Bayar");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_600));
                    break;
                case 2:
                    mBinding.btnMore.setVisibility(View.VISIBLE);
                    popupMenu.findItem(R.id.terima).setVisible(true);
                    popupMenu.findItem(R.id.tolak).setVisible(true);
                    mBinding.txtStatus.setText("Menunggu Konfirmasi");
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
                default:
                    mBinding.txtStatus.setText("N/A");
                    mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_grey_900));
                    break;
            }
        }else{
            switch (trx.getPayment().getStatus()){
                case 1:
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
                if (id == R.id.handle) {
                    setHandle(v.getContext(), trx.getId());
                } else if (id == R.id.terima) {
                    updateStatus(v.getContext(), trx.getId(), Objects.requireNonNull(item.getTitle()).toString(), 2);
                } else if (id == R.id.tolak) {
                    updateStatus(v.getContext(), trx.getId(), Objects.requireNonNull(item.getTitle()).toString(), 1);
                } else if (id == R.id.jemput || id == R.id.jemputtoko || id == R.id.selesaijemput
                        || id == R.id.cuciselesainantar || id == R.id.selesaiantar) {
                    updateStatus(v.getContext(), trx.getId(), Objects.requireNonNull(item.getTitle()).toString(), -1);
                } else {
                    // default case, tidak perlu lakukan apa-apa
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
        LaundryTopupAdapterLayoutBinding mBinding;
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