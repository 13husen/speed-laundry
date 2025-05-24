<!DOCTYPE html>
<html>
<head>
  <title>Laporan Bulan {{ $date }}</title>
  <style>
    html {
      font-family:"arial"
    }
    th {
      font-size:12px;
      text-align: center;
    }

    td {
      font-size:11px;
    }
    .container {
      display:block;
      margin:0px auto;
      margin-left:50px;
      margin-right:50px;
    }
  </style>
</head>
<body>
 <img src="https://thawing-castle-23484.herokuapp.com/img/speed-logo.png" />
        <h3 align="center" style="font-family: arial">
        Laporan Transaksi
        </h3>
        <div class="container">
          <table style="margin-bottom:20px;">
          <tr>
            <td>Nama Admin</td>
            <td>:</td>
            <td>{{ $admin }}</td>
          </tr>
          <tr>
            <td>Tanggal Export</td>
            <td>:</td>
            <td>{{ $tanggalexport}}</td>
          </tr>
          <tr>
            <td>Periode Laporan</td>
            <td>:</td>
            <td>{{ $date }}</td>
          </tr>        
          
          </table>
          <?php $no = 1; $total = 0; ?>
          <table border="1" style="width:100%;">   
          <tr>
            <th>No</th>
            <th>Invoice</th>
            <th>Tgl Transaksi</th>
            @if($transaction[0]['type'] == 2)   
                <th>Tanggal Selesai</th>
            @endif
            <th>Konsumen</th>
            <th>Nominal</th>
            <th>Biaya Admin</th>
            <th>Biaya Ongkir</th>
            <th>Total Transaksi</th>
          </tr>
      @foreach ($transaction as $trx)
          <tr>
            <td><?php echo $no++; ?></td>
            <td>{{ $trx['invoice']}}</td>
            <td>{{ $trx['created_at'] }}</td>
            @if($trx['type'] == 2)   
                <td>{{ $trx['job_done_at'] != null ? $trx['job_done_at'] : ""}}</td>
            @endif
            <td>{{ substr($trx['user']['name'], 0, 30) ?? "-"}} ({{$trx['user']['user_detail']['phone_number'] ?? "N/A"}})</td>
            <td>Rp {{ number_format($trx['payment']['nominal'], 2, ",", ".") }}</td>
            <td>Rp {{ number_format($trx['payment']['admin_fee'], 2, ",", ".") }}</td>
            <td>Rp {{ number_format($trx['payment']['shipping_fee'], 2, ",", ".") }}</td>
            <td>Rp {{ number_format($trx['payment']['total_fee'], 2, ",", ".") }}</td>
            <?php $total += $trx['payment']['total_fee']; ?>
          </tr>  
      @endforeach      
          <tr>
            <td colspan="<?php echo $transaction[0]['type'] == 2 ? 8 : 7 ?>">Total</td>
            <td>Rp {{ number_format($total, 2, ",", ".") }}</td>
          </tr>
          </table>          
        </div>
</body>
</html>











