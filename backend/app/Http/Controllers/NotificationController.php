<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Enums\GeneralList;
use Illuminate\Http\Request;
use DB;
use Validator;

class NotificationController extends Controller
{
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

            $query = Notification::search($request->all());
            $query = $this->filter($query,$request);
            $notifications = $query->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($notifications);
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
            $query = Notification::match($request->all());
            $query = $this->filter($query,$request);
            $notifications = $query->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($notifications);
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }
}
