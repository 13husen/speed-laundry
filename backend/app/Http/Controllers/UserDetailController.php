<?php

namespace App\Http\Controllers;

use App\Models\UserDetail;
use App\Enums\UserList;
use Exception;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;


class UserDetailController extends Controller
{
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
                'user_id' => 'required|exists:users,id,deleted_at,NULL',
                'phone_number' => 'required|string|max:255',
                'address' => 'sometimes|string',
                'shipment_address' => 'required|string',
                'map_address_url' => 'nullable|string',
                "plate_number" => Rule::requiredIf(auth()->user()->type == UserList::TYPE_DRIVER),
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $data = [
                'user_id' => $input['user_id'],
                'phone_number' => $input['phone_number'],
                'address' => $input['address'] ?? "",
                'shipment_address' => $input['shipment_address'],
                'saved_coordinate_long' => $input['saved_coordinate_long'] ?? "",
                'saved_coordinate_lat' => $input['saved_coordinate_lat'] ?? "",
                'map_address_url' => $input['map_address_url'] ?? "",
            ];

            $bank = UserDetail::create($data);
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
                'phone_number' => 'sometimes|string|max:255',
                'address' => 'sometimes|string',
                'shipment_address' => 'sometimes|string',
                'shipment_name' => 'sometimes|string',
                'map_address_url' => 'nullable|string',
                'plate_number' => 'sometimes|string',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $patchingData = [];

            if (array_key_exists('phone_number', $input)) {
                $patchingData['phone_number'] = $input['phone_number'];
            }

            if (array_key_exists('address', $input)) {
                $patchingData['address'] = $input['address'];
            }

            if (array_key_exists('shipment_address', $input)) {
                $patchingData['shipment_address'] = $input['shipment_address'];
            }

            if (array_key_exists('shipment_name', $input)) {
                $patchingData['shipment_name'] = $input['shipment_name'];
            }            

            if (array_key_exists('saved_coordinate_long', $input)) {
                $patchingData['saved_coordinate_long'] = $input['saved_coordinate_long'];
            }                                    

            if (array_key_exists('saved_coordinate_lat', $input)) {
                $patchingData['saved_coordinate_lat'] = $input['saved_coordinate_lat'];
            }

            if (array_key_exists('map_address_url', $input)) {
                $patchingData['map_address_url'] = $input['map_address_url'];
            }            

            $bank = UserDetail::updateOrCreate(['id' => $id], $patchingData);

            return $this->responseSuccess(array($bank));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

}
