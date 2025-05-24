<?php

namespace App\Http\Controllers;

use App\Enums\GeneralList;
use App\Models\TypeLaundry;
use Exception;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;

class TypeLaundryController extends Controller
{
    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function search(Request $request)
    {
        try {
            $types = TypeLaundry::search($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($types);
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
            $types = TypeLaundry::match($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($types);
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
                'id' => 'required|exists:type_laundries,id',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $type = TypeLaundry::where('id', $id)->first()->toArray();

            return $this->responseSuccess($type);
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
                'nama' => 'required|string|max:255|unique:type_laundries,nama,NULL,id',
                'status' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_STATUS),
                'count_type' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_COUNT),
                'count_time' => 'required|numeric',
                'fee' => 'required|numeric',
                'deskripsi' => 'nullable|required|string|max:255',
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $data = [
                'nama' => $input['nama'],
                'status' => $input['status'] ?? 0,
                'fee' => $input['fee'],
                'count_type' => $input['count_type'],
                'count_type' => $input['count_type'],
                'deskripsi' => $input['deskripsi'],
            ];

            $type = TypeLaundry::create($data);
            DB::commit();
            return $this->responseSuccess(array($type));
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
                'nama' => 'sometimes|required|string|max:255',
                'status' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_STATUS),
                'deskripsi' => 'nullable|string',
                'count_type' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_COUNT),
                'count_time' => 'nullable|numeric',
                'fee' => 'nullable|numeric'

            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $patchingData = [];

            if (array_key_exists('nama', $input)) {
                $patchingData['nama'] = $input['nama'];
            }

            if (array_key_exists('fee', $input)) {
                $patchingData['fee'] = $input['fee'];
            }
            if (array_key_exists('count_type', $input)) {
                $patchingData['count_type'] = $input['count_type'];
            }            

            if (array_key_exists('count_time', $input)) {
                $patchingData['count_time'] = $input['count_time'];
            }            

            if (array_key_exists('deskripsi', $input)) {
                $patchingData['deskripsi'] = $input['deskripsi'];
            }

            if (array_key_exists('status', $input)) {
                $patchingData['status'] = $input['status'];
            }            

            $type = TypeLaundry::updateOrCreate(['id' => $id], $patchingData);

            return $this->responseSuccess(array($type));
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
                'id' => 'required|exists:type_laundries,id',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $type = TypeLaundry::find($id);
            $type->delete();
            return $this->responseSuccess(array($type));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }
}
