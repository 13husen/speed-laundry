package com.speedlaundry.admin.laundry_ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.speedlaundry.admin.R;
import com.speedlaundry.admin.base_app.MainApplication;
import com.speedlaundry.admin.databinding.ActivityTopupDetailBinding;
import com.speedlaundry.admin.http.repository.ApiRepository;
import com.speedlaundry.admin.http.repository.RepositoryEnum;
import com.speedlaundry.admin.model.laundry.transaction.TransactionClotheItem;
import com.speedlaundry.admin.model.laundry.transaction.TransactionDetail;
import com.speedlaundry.admin.model.laundry.transaction.TransactionItem;
import com.speedlaundry.admin.utils.GeneralUtil;

import org.json.JSONObject;

public class TopupDetailActivity extends AppCompatActivity {
    ActivityTopupDetailBinding mBinding;
    int id;
    TransactionItem transactionItem;
    Gson gson = new Gson();
    ApiRepository repository = ApiRepository.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_topup_detail);
        if(getIntent().hasExtra("id")){
            id = getIntent().getIntExtra("id", 0);
            initView();
        }else{
            Toast.makeText(TopupDetailActivity.this, "Data Tidak Ditemukan!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void initView(){
        mBinding.back.setOnClickListener(v -> {
            finish();
        });
        getTransactionData();
        mBinding.refresh.setOnRefreshListener(() -> {
            getTransactionData();
        });
    }
    private void getTransactionData(){
        repository.setDialog(this);
        repository.setApiListener(MainApplication.api.getTransaction(id),
                (object, codeResponse, rEnum) -> {
                    mBinding.refresh.setRefreshing(false);
                    if (rEnum == RepositoryEnum.SUCCESS) {
                        JSONObject data = object.optJSONObject("data");
                        transactionItem = gson.fromJson(data.toString(), TransactionItem.class);
                        setPageData();
                    } else {
                        Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    public void setPageData(){
        mBinding.setTransactionDetail(transactionItem);
        // status transaksi
        switch (transactionItem.getPayment().getStatus()){
                    case 1:
                        mBinding.txtStatusTrx.setText("Pending Bayar");
                        mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(TopupDetailActivity.this, R.color.md_blue_600));
                        break;
                    case 2:
                        mBinding.txtStatusTrx.setText("Menunggu Konfirmasi");
                        mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(TopupDetailActivity.this, R.color.md_orange_600));
                        break;
                    case 3:
                        mBinding.txtStatusTrx.setText("Gagal");
                        mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(TopupDetailActivity.this, R.color.md_indigo_600));
                        break;
                    case 4:
                        mBinding.txtStatusTrx.setText("Sukses");
                        mBinding.lytStatus.setCardBackgroundColor(ContextCompat.getColor(TopupDetailActivity.this, R.color.md_teal_700));
                        break;
                    default:
                        break;
        }
        // cek payment
        if (transactionItem.getPayment().getPaymentType() != null){
            mBinding.cardPayment.setVisibility(View.VISIBLE);
            if (transactionItem.getPayment().getPaymentType() == 2){
                mBinding.cardKonfirmasi.setVisibility(View.VISIBLE);
            }
            mBinding.txtAdmin.setText(": "+ GeneralUtil.convertRupiah(transactionItem.getPayment().getAdminFee().intValue()));
            mBinding.txtNominal.setText(": "+GeneralUtil.convertRupiah(transactionItem.getPayment().getNominal().intValue()));
            mBinding.txtTotalBiaya.setText(": "+GeneralUtil.convertRupiah(transactionItem.getPayment().getTotalFee().intValue()));
        }
        if (transactionItem.getTransactionDetail().size() > 0){
            mBinding.tblLaundry.removeAllViews();
            mBinding.cardLaundryDetails.setVisibility(View.VISIBLE);
            int index = 1;
            for (TransactionDetail transactionDetail : transactionItem.getTransactionDetail())
            {
                    /* Find Tablelayout defined in main.xml */
                    TableRow tr = new TableRow(this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    tr.setGravity(Gravity.CENTER);
                    tr.setWeightSum(1);
                    /* Create a Button to be the row-content. */
                    // Teks Informasi Laundry
                    TextView keterangan = new TextView(this);
                    keterangan.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    if (transactionDetail.getItemType() == 1) {
                        keterangan.setText(index + ".) Cucian "+ transactionDetail.getWeight()+ "("+ transactionDetail.getKategori().getUnit() +"), Tipe " + transactionItem.getTipe().getNama()+ ", Kategori "
                                + transactionDetail.getKategori().getName()+", Harga Cuci : "+GeneralUtil.convertRupiah(Integer.parseInt(transactionDetail.getAmountFee())));
                    }else if (transactionDetail.getItemType() == 2){
                        String ket = index + ".) Cucian Per-pakaian, Tipe " + transactionItem.getTipe().getNama()+ ", Kategori " + transactionDetail.getKategori().getName() + ", Harga Cuci: "+
                                GeneralUtil.convertRupiah(Integer.parseInt(transactionDetail.getAmountFee())) +"\n";
                        for (TransactionClotheItem trxClothe : transactionDetail.getPakaian())
                        {
                            ket += "\u2023 " + trxClothe.getCategorizeClothe().getClothe() +
                                    " (1 Pcs), Harga Cuci: "+ GeneralUtil.convertRupiah(Integer.parseInt(trxClothe.getCategorizeClothe().getFee()))+ "\n";
                        }
                        keterangan.setText(ket);
                    }

                    tr.addView(keterangan);

                    /* Add row to TableLayout. */
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
                    mBinding.tblLaundry.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    index++;


            }
        }

    }


}