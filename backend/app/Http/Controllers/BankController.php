<?php

namespace App\Http\Controllers;

use App\Enums\BankList;
use App\Models\Bank;
use Exception;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;

class BankController extends Controller
{
    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function search(Request $request)
    {
        try {
            $banks = Bank::search($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($banks);
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
            $banks = Bank::match($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($banks);
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
                'id' => 'required|exists:banks,id',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $bank = Bank::where('id', $id)->first();

            return $this->responseSuccess(array($bank ?? []));
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
                'name' => 'required|string|max:255|unique:banks,name,NULL,id',
                'code' => 'required|string|max:10|unique:banks,code,NULL,id',
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $data = [
                'name' => $input['name'],
                'code' => $input['code'],
            ];

            $bank = Bank::create($data);
            DB::commit();
            return $this->responseSuccess(array($bank));
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
    public function update(Request $request, int $id)
    {
        try {
            $input = $request->all();
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'id' => 'required|exists:banks,id',
                'name' => 'sometimes|required|string|max:255',
                'code' => 'sometimes|required|string|max:255',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $patchingData = [];

            if (array_key_exists('name', $input)) {
                $patchingData['name'] = $input['name'];
            }

            if (array_key_exists('code', $input)) {
                $patchingData['code'] = $input['code'];
            }

            $bank = Bank::updateOrCreate(['id' => $id], $patchingData);

            return $this->responseSuccess(array($bank));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param Request $request
     * @param int $id
     * @return JsonResponse
     */
    public function delete(Request $request, int $id)
    {
        try {
            $input['id'] = $id;
            $validator = Validator::make($input, [
                'id' => 'required|exists:banks,id',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $bank = Bank::find($id);
            $bank->delete();
            return $this->responseSuccess(array($bank));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }
}
