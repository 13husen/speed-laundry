<?php

namespace App\Http\Controllers;

use Exception;
use App\Enums\TransactionList;
use App\Enums\PaymentList;
use App\Models\Transaction;
use App\Models\Payment;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;
use App\Traits\ReferenceTrait;
use App\Traits\FileTrait;
use App\Rules\Base64Image;

class PaymentController extends Controller
{
    use ReferenceTrait,FileTrait;
  
    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function topup(Request $request)
    {
        try {
            DB::beginTransaction();
            $input = $request->all();

            $rules = [
                'type' => ["required","regex:(".PaymentList::ACTION_TOPUP.")"],
                'nominal' => 'required|integer|min:1|gt:500',
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $transactionData = [
                'type' => $input['type'],
                'user_id' => auth()->user()->id,
                'note' => $input['note'] ?? "",
                'status' => TransactionList::STATUS_PENDING_PAYMENT,
                'invoice' => $this->generateReferralCode(10, 4),
            ];

            $adminFee = config('laundry.fee.admin') ?? 0 ;

            $payment = Payment::create([
                'nominal' => $input['nominal'],
                "admin_fee" => $adminFee,
                "total_fee" => $input['nominal'] + $adminFee,
                "payment_type" => 2,
                "status" => 1,
            ]);

            // // re-insert paymentId to transaction
            $transactionData['payment_id'] = $payment->id;
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
    public function confirm(Request $request)
    {
        try {
            DB::beginTransaction();
            $input = $request->all();
            $rules = [
                'transaction_id' => 'required|exists:transactions,id',
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $transaction = Transaction::find($input['transaction_id']);
            $payment = Payment::find($transaction->payment_id);

            if (!$payment) {
                return $this->responseFailed([], 'Data Pembayaran tidak dapat ditemukan !');
            }

            if ($payment->confirm_count > 2) {
                return $this->responseFailed([], 'Anda sudah tidak bisa mengirim konfirmasi kembali !');
            }            
            // if (isset($input['confirm_image'])) {
            //     $imageFileName = $this->uploadImage($input['confirm_image']);
            // }
            // updating data
            // $payment->confirm_image = $imageFileName ?? "";
            // $payment->confirm_bank = $input['confirm_bank'] ?? "";
            // $payment->confirm_bank_number = $input['confirm_bank_number'] ?? "";
            $payment->confirm_count = $payment->confirm_count + 1;
            $payment->status = PaymentList::STATUS_CONFIRM_CHECKING ?? "";
            // turn status
            $payment->save();
            $transaction->status = TransactionList::STATUS_PENDING_PAYMENT;
            $transaction->save();
            DB::commit();
            $transaction->refresh();
            return $this->responseSuccess($transaction->toArray());
        } catch (Exception $exception) {
            DB::rollBack();
            return $this->responseError($exception->getMessage());
        }
    }


    /**
     * @param int $id
     * @return JsonResponse
     */
    public function getPaymentInfo()
    {
        $fee = [
            "admin_fee" => config('laundry.fee.admin') ?? 0,
            "method" => PaymentList::ALL_METHOD_DETAIL
        ];
        return $this->responseSuccess(array($fee));
    }
}
