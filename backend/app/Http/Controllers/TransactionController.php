<?php

namespace App\Http\Controllers;

use App\Enums\TransactionList;
use App\Enums\PaymentList;
use App\Enums\UserList;
use App\Enums\GeneralList;
use App\Enums\LaundryList;
use App\Models\Transaction;
use App\Models\Payment;
use App\Models\User;
use App\Models\Category;
use App\Models\CategorizeClothe;
use App\Models\TypeLaundry;
use App\Models\TransactionDetail;
use App\Models\TransactionClothe;
use Exception;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use App\Traits\ReferenceTrait;
use App\Traits\HelperTrait;
use App\Traits\WalletTrait;
use Illuminate\Validation\Rule;
use Carbon\Carbon;
use \PDF;

class TransactionController extends Controller
{
    use HelperTrait,ReferenceTrait,WalletTrait;

    private function filter($query, Request $request)
    {
        $currentUser = auth()->user();
        if ($currentUser->type == "user") {
            $query->where(function ($subQuery) use ($currentUser) {
                $subQuery->where('user_id', $currentUser->id);
                return $subQuery;
            });
        }
        return $query;
    }    
    

    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function search(Request $request)
    {
        try {

            $query = Transaction::search($request->all());
            $query = $this->filter($query,$request);
            $transactions = $query->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($transactions);
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function match(Request $request)
    {
        try {
            $query = Transaction::match($request->all());
            $query = $this->filter($query,$request);
            $transactions = $query->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($transactions);
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param int $id
     * @return JsonResponse
     */
    public function get(int $id)
    {
        try {
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'id' => 'required|exists:transactions,id',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $transaction = Transaction::with('user','transactionDetail')->where('id', $id)->first()->toArray();

            return $this->responseSuccess($transaction);
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function store(Request $request)
    {
        try {
            DB::beginTransaction();
            $input = $request->all();

            $rules = [
                'type' => ["required","regex:(".PaymentList::ACTION_LAUNDRY.")"],  
                'note' => 'sometimes|required|string',                
                'shipment_fix_address' => 'required|string',                
                'shipment_fix_phone' => 'required|string',                
                'shipment_fix_name' => 'required|string',         
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $transactionData = [
                'type' => $input['type'],                
                'user_id' => auth()->user()->id,
                'note' => $input['note'] ?? "",
                'shipment_fix_address' => $input['shipment_fix_address'] ?? "",
                'shipment_fix_name' => $input['shipment_fix_name'] ?? "",
                'shipment_fix_phone' => $input['shipment_fix_phone'] ?? "",
                'note' => $input['note'] ?? "",
                'status' => TransactionList::STATUS_PROSES,
                'invoice' => $this->generateReferralCode(10, 4),
            ];

            $courier = User::where("type", UserList::TYPE_DRIVER)->first();
            if (!$courier) {
                return $this->responseFailed([], "Pengemudi Tidak ditemukan!");
            }
            $payment = Payment::create([]); // create empty payment

            // // re-insert paymentId to transaction
            $transactionData['payment_id'] = $payment->id;
            $transactionData['courier_id'] = $courier->id;
            $transaction = Transaction::create($transactionData);


            DB::commit();
            return $this->responseSuccess($transaction->toArray());
        } catch (Exception $exception) {
            DB::rollBack();
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param Request $request
     * @param int $id
     * @return JsonResponse
     */
    public function updateStatus(Request $request, int $id)
    {
        try {
            DB::beginTransaction(); 
            $input = $request->all();
            $input['id'] = $id;
            $patchingData = [];
            $validator = Validator::make($input, [
                'status' => Rule::requiredIf($this->isAdmin()),
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $transaction = Transaction::find($id);
            if (!$transaction) {
                return $this->responseFailed([], 'Data tidak dapat ditemukan !');
            }

            $message = "";
            $statusUpdate = $transaction->status;

            if ($transaction->status != TransactionList::STATUS_CANCEL && $transaction->status != TransactionList::STATUS_EXPIRED) {
                switch (auth()->user()->type) {
                    case UserList::TYPE_DRIVER:
                        if ($transaction->status == TransactionList::STATUS_PROSES) {
                            $statusUpdate = TransactionList::STATUS_PENGECEKAN;
                            $message = "sedang dalam pengecekan";
                        }
                        // elseif ($transaction->status == TransactionList::STATUS_PENGECEKAN) {
                        //     $statusUpdate = TransactionList::STATUS_ANTAR;
                        // }
                        elseif ($transaction->status == TransactionList::STATUS_ANTAR) {
                            $statusUpdate = TransactionList::STATUS_PENDING_PAYMENT;
                            $transaction->is_handled = false;
                            $transaction->handled_by = null;
                            $message = "telah di antar, mohon melakukan pembayaran untuk transaksi diproses";
                        }elseif ($transaction->status == TransactionList::STATUS_ANTARPULANG) {
                            $statusUpdate = TransactionList::STATUS_SELESAI;
                            $message = "telah selesai, Terima kasih menggunakan jasa kami";
                        }
                        break;
                    case UserList::TYPE_KARYAWAN:
                        if ($transaction->status == TransactionList::STATUS_CUCI) {
                            $statusUpdate = TransactionList::STATUS_ANTARPULANG;
                            $transaction->is_handled = false;
                            $transaction->handled_by = null;
                            $message = "cucian sedang diantar, mohon di tunggu!";
                        }
                        break;
                    case UserList::TYPE_ADMIN:
                        if ($transaction->status == TransactionList::STATUS_PENDING_PAYMENT) {
                            $payment = Payment::find($transaction->payment_id);
                            if (!$payment) {
                                return $this->responseFailed([], 'Data Pembayaran tidak dapat ditemukan !');
                            }                            

                            if ($payment->payment_type == null || !in_array($payment->payment_type, PaymentList::ALL_METHOD)) {
                                return $this->responseFailed([], 'Pembayaran Belum diupdate oleh user !');
                            }
                            if ($input['status'] == TransactionList::DO_TERIMA) {
                                if ($transaction->type == PaymentList::ACTION_LAUNDRY) {
                                    $statusUpdate = TransactionList::STATUS_CUCI;
                                    $message = "diterima, pakaian anda sedang di cuci";                                    
                                }elseif($transaction->type == PaymentList::ACTION_TOPUP){
                                    $statusUpdate = TransactionList::STATUS_SELESAI;
                                    $user = User::find($transaction->user_id);
                                    $this->debit($payment->nominal, $user);
                                    $message = "topup berhasil, mohon cek saldo anda";                                    
                                }
                                $payment->status = PaymentList::STATUS_SUCCESS;
                                $payment->save();               
                            }else{
                                $statusUpdate = TransactionList::STATUS_CANCEL;
                                $payment->status = PaymentList::STATUS_FAILED;
                                $payment->save();
                                $message = "pembayaran gagal, silahkan coba kembali";            

                            }
                        }
                        break;                                        
                    default:
                        break;
                }    
            }
            $transaction->status = $statusUpdate;
            $transaction->save();
            DB::commit();
            $transaction->refresh();
            $user = User::find($transaction->user_id);
            $this->sendPushNotif($message,$user,$transaction);
            return $this->responseSuccess(array($transaction));
        } catch (Exception $exception) {
            DB::rollBack();
            return $this->responseError($exception->getMessage());
        }
    }    


    /**
     * @param int $id
     * @return JsonResponse
     */
    public function getFee(int $id)
    {
        try {
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'id' => 'required|exists:transactions,id',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $transaction = Transaction::where('id', $id)->first();

            if (!$transaction) {
                return $this->responseFailed([], 'Data tidak dapat ditemukan !');
            }

            $transactionDetails = TransactionDetail::where("transaction_id",$id)->get()->toArray();

            if (count($transactionDetails) <= 0) {
                return $this->responseFailed([], 'Detail Transaksi belum di update oleh driver !');
            }            

            if (auth()->user()->type != UserList::TYPE_USER || auth()->user()->id != $transaction->user_id) {
                return $this->responseFailed([], 'Anda tidak dapat melakukan aksi ini !');
            }              


            $totalAmount = 0;
            $adminFee = config('laundry.fee.admin') ?? 0 ;
            $ongkirFee = config('laundry.fee.shipping') ?? 0;

            foreach ($transactionDetails as $detail) {
                $totalAmount += $detail["amount_fee"];
            }

            $paymentData = [
                "nominal" => $totalAmount,
                "admin_fee" => $adminFee,
                "shipping_fee" => $ongkirFee,
                "discount_fee" => 0,
                "total_fee" => $totalAmount + $adminFee + $ongkirFee,
            ];

            $payment = Payment::updateOrCreate(['id' => $transaction->payment_id], $paymentData);
            
            $transaction->refresh();
            return $this->responseSuccess($transaction->toArray());
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }



    /**
     * @param Request $request
     * @param int $id
     * @return JsonResponse
     */
    public function updatePayment(Request $request, int $id)
    {
        try {
            $message = "";
            DB::beginTransaction(); 
            $input = $request->all();
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'method' => 'required|in:' . implode(',', PaymentList::ALL_METHOD),
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $transaction = Transaction::find($id);
            if (!$transaction) {
                return $this->responseFailed([], 'Data tidak dapat ditemukan !');
            }
            $payment = Payment::find($transaction->payment_id);
            if (!$payment) {
                return $this->responseFailed([], 'Data Pembayaran tidak dapat ditemukan !');
            }

            if (auth()->user()->type != UserList::TYPE_USER || auth()->user()->id != $transaction->user_id) {
                return $this->responseFailed([], 'Anda tidak dapat melakukan aksi ini !');
            }              

            $method = $input['method'];

            if ($method == PaymentList::METHOD_BANK) {
                $transaction->status = TransactionList::STATUS_PENDING_PAYMENT;
                $transaction->save();
                $payment->payment_type = PaymentList::METHOD_BANK;
                $payment->status = PaymentList::STATUS_CONFIRM;
                $payment->save();                
            }elseif ($method == PaymentList::METHOD_CASH) {
                $transaction->status = TransactionList::STATUS_CUCI;
                $transaction->save();
                $payment->payment_type = PaymentList::METHOD_CASH;
                $payment->save();                
            }elseif ($method == PaymentList::METHOD_WALLET) {
                // check if total_fee still 0
                if ($payment->total_fee == null) {
                    return $this->responseFailed([], 'Total Pembayaran belum ditentukan!');
                }              

                if ((int)auth()->user()->balance < $payment->total_fee) {
                    return $this->responseFailed([], 'Saldo kurang untuk pembayaran metode wallet!');
                }

                $this->credit($payment->total_fee, auth()->user());
                $transaction->status = TransactionList::STATUS_CUCI;
                $transaction->save();
                $payment->payment_type = PaymentList::METHOD_WALLET;
                $payment->status = PaymentList::STATUS_SUCCESS;
                $payment->save();
                $message = "Telah dibayar, silahkan cek detail transaksi";  
            }

            $transaction->refresh();
            DB::commit();
            $user = User::find($transaction->user_id);
            $this->sendPushNotif($message,$user,$transaction,"admin");
            return $this->responseSuccess($transaction->toArray());
        } catch (Exception $exception) {
            DB::rollBack();
            return $this->responseError($exception->getMessage());
        }
    }    



    /**
     * @param Request $request
     * @param int $id
     * @return JsonResponse
     */
    public function updateDetails(Request $request, int $id)
    {
        try {
            DB::beginTransaction();
            $input = $request->all();
            $input['id'] = $id;
            $patchingData = [];
            $validator = Validator::make($input, [
                "order"    => "required|array|min:1",
                'type_laundry' => 'required|exists:type_laundries,id',
                'order.*.item_type' => 'required|in:' . implode(',', LaundryList::ALL_BARANG),
                'order.*.category' => 'required_if:order.*.item_type,'.LaundryList::BARANG_ITEMS.'|exists:categories,id',            
                "order.*.clothe" => 'required_if:order.*.item_type,'.LaundryList::BARANG_CLOTHES.'|array',
                "order.*.weight" => "required_if:order.*.item_type,==,".LaundryList::BARANG_ITEMS,
                'order.*.pcs' => 'required_if:order.*.item_type,'.LaundryList::BARANG_CLOTHES.'|numeric',
                'order.*.clothe.*.clothe_id' => 'required|exists:categorize_clothes,id',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            if (auth()->user()->type != UserList::TYPE_DRIVER) {
                return $this->responseFailed([], 'Hanya driver yang dapat melakukan aksi ini !');
            }            
            $transaction = Transaction::find($id);
            if (!$transaction) {
                return $this->responseFailed([], 'Data tidak dapat ditemukan !');
            }

            if ($transaction->status != TransactionList::STATUS_PENGECEKAN) {
                return $this->responseFailed([], "Transaksi belum dalam pengecekan!");
            }            
            $typeLaundry = TypeLaundry::find($input['type_laundry']);
            foreach ($input['order'] as $purchase) {
                $amountFee = 0;
                $category = Category::find($purchase['category']);
                if ($purchase['item_type'] == LaundryList::BARANG_ITEMS) {
                    $amountFee = (($category->fee + $typeLaundry->fee) * $purchase['weight']);
                }elseif ($purchase['item_type'] == LaundryList::BARANG_CLOTHES) {
                    foreach ($purchase['clothe'] as $clothe) {
                        $categorizeClothe = CategorizeClothe::find($clothe['clothe_id']);
                        $amountFee = $amountFee + ($typeLaundry->fee + $categorizeClothe->fee);
                    }                    
                }

                $transactionDetailData = [
                    "transaction_id" => $id,
                    "item_type" => $purchase['item_type'],
                    "category_id" => $purchase['category'] ?? null,
                    "weight" => $purchase["weight"] ?? 0,
                    "pcs" => $purchase['pcs'] ?? 0,
                    "amount_fee" => $amountFee
                ];            
                $transactionDetail = TransactionDetail::create($transactionDetailData);
                if ($purchase['item_type'] == LaundryList::BARANG_CLOTHES) {
                    foreach ($purchase['clothe'] as $transactionClotheData) {
                        TransactionClothe::create([
                            "transaction_detail_id" => $transactionDetail->id,
                            "clothe_id" => $transactionClotheData['clothe_id'],
                        ]);
                    }
                }
            }
            // count jobe done at
            $transaction->type_id = $typeLaundry->id;
            $date = Carbon::now();
            if ($typeLaundry->count_type == GeneralList::COUNT_HARI) {
                $transaction->job_done_at = $date->addDays($typeLaundry->count_time)->toDateTime();
            }elseif($typeLaundry->count_type == GeneralList::COUNT_JAM){
                $transaction->job_done_at = $date->addHour($typeLaundry->count_time)->toDateTime();
            }

            $transaction->status = TransactionList::STATUS_ANTAR;
            $transaction->save();
            $transaction->refresh();            
            DB::commit();
            $user = User::find($transaction->user_id);
            $message = "Pengecekan berhasil, pakaian akan diantar oleh kurir kami!";
            $this->sendPushNotif($message,$user,$transaction);
            return $this->responseSuccess(array($transaction));              
        } catch (Exception $exception) {
            DB::rollBack();
            dd($exception);
            return $this->responseError($exception->getMessage());
        }
    }        

    /**
     * @param Request $request
     * @param int $id
     * @return JsonResponse
     */
    public function updateHandle(int $id)
    {
        try {
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'id' => 'required|exists:transactions,id',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $transaction = Transaction::where('id', $id)->first();

            if (!$transaction) {
                return $this->responseFailed([], 'Data tidak dapat ditemukan !');
            }
     

            if (auth()->user()->type == UserList::TYPE_ADMIN || auth()->user()->type == UserList::TYPE_USER) {
                return $this->responseFailed([], 'Anda tidak dapat melakukan aksi ini !');
            }              

            $transaction->is_handled = true;
            $transaction->handled_by = auth()->user()->id;
            $transaction->save();
            
            $transaction->refresh();
            return $this->responseSuccess(array($transaction ?? []));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }        
    }
    /**
     * @param int $id
     * @return JsonResponse
     */
    public function export(int $id, int $tipe, $userid)
    {
        try {
            $year = date("Y");
            $bulan = ['Januari','Februari','Maret','April','Mei','Juni','Juli','Agustus','September','Oktober','November','Desember'];

            $monthNum = "01";
            if ($id < 10) {
                $monthNum = "0".$id;
            }else{
                $monthNum = $id;
            }

            $user = User::find($userid);

            $query_date = $year.'-'.$monthNum.'-01';
            $from = date('Y-m-01', strtotime($query_date));
            $to = date('Y-m-t', strtotime($query_date));
            $transaction = Transaction::with('user')->whereBetween('created_at', [$from." 00:00:00", $to." 23:59:59"])->where('status', TransactionList::STATUS_SELESAI)->where('type', $tipe == 1 ? TransactionList::ACTION_TOPUP : TransactionList::ACTION_LAUNDRY)->orderBy('id', 'ASC')->get()->toArray();
            $data = [
                "transaction" => $transaction,
                "admin" => $user->name,
                "tanggalexport" => Carbon::now(),
                "date" => $bulan[$id-1]." ".$year 
            ];
            view()->share($data);
            $pdf = PDF::loadView('transaction-info')->setPaper('a4', 'landscape');

            return $pdf->download('Laporan_'.$bulan[$id-1].'_'.$year.'pdf'); 
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }
    /**
     * @param int $id
     * @return JsonResponse
     */
    public function exportCheck(int $id)
    {
        try {
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'id' => 'required|numeric',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $year = date("Y");
            $query_date = $year.'-'.$id.'-01';
            $from = date('Y-m-01', strtotime($query_date));
            $to = date('Y-m-t', strtotime($query_date));
            $transaction = Transaction::with('user')->whereBetween('created_at', [$from, $to])->where('status', TransactionList::STATUS_SELESAI)->get()->toArray();
            if (count($transaction) <= 0) {
                return $this->responseFailed([], 'Data Transaksi Kosong!');
            }
            return $this->responseSuccess($transaction);
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }


}
