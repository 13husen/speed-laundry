<?php

namespace App\Http\Controllers;

use App\Models\CategorizeClothe;
use App\Enums\GeneralList;
use Illuminate\Http\Request;
use DB;
use Validator;

class CategorizeClotheController extends Controller
{
    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function search(Request $request)
    {
        try {
            $categoriesClothes = CategorizeClothe::search($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($categoriesClothes);
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
            $categoriesClothes = CategorizeClothe::match($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($categoriesClothes);
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
                'id' => 'required|exists:categorize_clothes,id,deleted_at,NULL',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $categorizeCloth = CategorizeClothe::where('id', $id)->first()->toArray();

            return $this->responseSuccess($categorizeCloth);
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
                'clothe' => 'required|string|max:255|unique:categorize_clothes,clothe,NULL,id,deleted_at,NULL',
                'fee' => 'required|numeric',
                'description' => 'nullable|string',
                'status' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_STATUS),
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            
            $data = [
                'clothe' => $input['clothe'],
                'description' => $input['description'],
                'fee' => $input['fee'],
                'status' => $input['status'],
            ];

            $categorizeClothe = CategorizeClothe::create($data);

            DB::commit();
            return $this->responseSuccess(array($categorizeClothe));
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
                'id' => 'required|exists:categorize_clothes,id,deleted_at,NULL',
                'clothe' => 'sometimes|required|string|max:255',
                'status' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_STATUS),
                'fee' => 'nullable|numeric',
                'description' => 'nullable|string|max:20',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $patchingData = [];

            if (array_key_exists('clothe', $input)) {
                $patchingData['clothe'] = $input['clothe'];
            }

            if (array_key_exists('description', $input)) {
                $patchingData['description'] = $input['description'];
            }            

            if (array_key_exists('status', $input)) {
                $patchingData['status'] = $input['status'];
            }
            if (array_key_exists('fee', $input)) {
                $patchingData['fee'] = $input['fee'];
            }

            $categorizeClothe = CategorizeClothe::updateOrCreate(['id' => $id], $patchingData);
  
            return $this->responseSuccess(array($categorizeClothe));
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
                'id' => 'required|exists:categorize_clothes,id,deleted_at,NULL',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $categorizeClothe = CategorizeClothe::find($id);
            $categorizeClothe->delete();
            return $this->responseSuccess(array($categorizeClothe));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }
}
