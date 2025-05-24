<?php

namespace App\Http\Controllers;

use App\Models\Category;
use App\Enums\GeneralList;
use Illuminate\Http\Request;
use DB;
use Validator;
use App\Traits\FileTrait;

class CategoryController extends Controller
{
    use FileTrait;
    
    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function search(Request $request)
    {
        try {
            $categories = Category::search($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($categories);
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
            $categories = Category::match($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($categories);
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
                'id' => 'required|exists:categories,id,deleted_at,NULL',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $category = Category::where('id', $id)->first()->toArray();

            return $this->responseSuccess($category);
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
                'name' => 'required|string|max:255|unique:categories,name,NULL,id,deleted_at,NULL',
                'fee' => 'required|numeric',
                'detail' => 'nullable|string',
                'description' => 'nullable|string',
                'status' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_STATUS),
            ];

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }


            $data = [
                'name' => $input['name'],
                'detail' => $input['detail'],
                'description' => $input['description'],
                'fee' => $input['fee'],
                'status' => $input['status'],
            ];

            $category = Category::create($data);

            DB::commit();
            return $this->responseSuccess(array($category));
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
                'id' => 'required|exists:categories,id,deleted_at,NULL',
                'name' => 'sometimes|required|string|max:255',
                'status' => 'sometimes|required|in:' . implode(',', GeneralList::ALL_STATUS),
                'detail' => 'nullable|string|max:20',
                'fee' => 'nullable|numeric',
                'description' => 'nullable|string|max:20',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $patchingData = [];

            if (array_key_exists('name', $input)) {
                $patchingData['name'] = $input['name'];
            }

            if (array_key_exists('detail', $input)) {
                $patchingData['detail'] = $input['detail'];
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


            $category = Category::updateOrCreate(['id' => $id], $patchingData);

            return $this->responseSuccess(array($category));
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
                'id' => 'required|exists:categories,id,deleted_at,NULL',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $category = Category::find($id);
            $category->delete();
            return $this->responseSuccess(array($category));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }
}
