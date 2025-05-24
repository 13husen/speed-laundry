<?php

namespace App\Http\Controllers;

use App\Enums\PasswordResetList;
use App\Enums\RoleList;
use App\Enums\UserList;
use App\Jobs\SendEmailResetPassword;
use App\Models\PasswordReset;
use App\Models\User;
use App\Rules\InvalidOldPassword;
use App\Rules\GeneralToken;
use App\Traits\ReferenceTrait;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Validator;
use Exception;
use DB;
class UserController extends Controller
{
    use ReferenceTrait;
    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function updatePassword(Request $request)
    {
        try {
            $input = $request->all();
            $input['id'] = Auth::user()->id;
            $validator = Validator::make($input, [
                'id' => 'required|exists:users,id,deleted_at,NULL',
                'old_password' => ['required', 'string', new InvalidOldPassword($input['id'])],
                'password' => 'required|string',
                'c_password' => 'required|same:password',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $currentUser = User::find($input['id']);

            $patchingData['password'] = encrypt($input['password']);
            $currentUser = $currentUser->updateOrCreate(['id' => $currentUser->id], $patchingData);
            return $this->responseSuccess(array($currentUser));
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function forgotPassword(Request $request)
    {
        try {
            $input = $request->all();

            $rules['email'] = 'required|email|exists:users,email';
            $rules['password'] = 'required|string|min:6';
            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }
            $user = User::whereEmail($input['email'])->first();
            $user->password = encrypt($input['password']);
            $user->save();
            $successMessage = "Password berhasil di reset !";

            return $this->responseSuccess([], $successMessage);
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function resetPassword(Request $request)
    {
        try {
            DB::beginTransaction();
            $input = $request->all();

            $rules['email'] = 'required|email';
            $username = 'email';
            $rules['type'] = 'required|in:' . implode(',', RoleList::ALL_ROLES);
            $rules['token'] = ['required', 'string', new GeneralToken(($input['type'] ?? ''), $username,
                ($input[$username] ?? ''), $phonePrefix)];
            $rules['password'] = 'required|string';
            $rules['c_password'] = 'required|same:password';

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $passwordReset = PasswordReset::where('token', $input['token'])
                ->where($username, $input[$username])
                ->latest()->first();

            $user = User::where($username, $input[$username])->first();
            $user->password = encrypt($input['password']);
            $user->save();
            $passwordReset->status = PasswordResetList::STATUS_USED;
            $passwordReset->save();
            DB::commit();
            return $this->responseSuccess(array($user));
        } catch (Exception $exception) {
            DB::rollBack();
            return $this->responseError($exception->getMessage());
        }
    }

    public function resetPasswordWeb(Request $request)
    {
        try {
            DB::beginTransaction();
            $input = $request->all();

            $rules['email'] = 'required|email';
            $username = 'email';
            $rules['token'] = ['required', 'string', new GeneralToken(($input['type'] ?? ''), $username, ($input[$username] ?? ''), null)];
            $rules['password'] = 'required|string';
            $rules['c_password'] = 'required|same:password';

            $validator = Validator::make($input, $rules);
            if ($validator->fails()) {
                $email = $input['email'] ?? null;
                $type = $input['type'] ?? null;
                $token = $input['token'] ?? null;                
                $status = "error";
                $detail = $validator->errors()->toArray();
                return view('auth.passwords.reset', compact('email', 'type', 'token','status','detail'));
            }

            $passwordReset = PasswordReset::where('token', $input['token'])
                ->where($username, $input[$username])
                ->latest()->first();

            $user = User::where($username, $input[$username])->first();
            $user->password = encrypt($input['password']);
            $user->save();
            $passwordReset->status = PasswordResetList::STATUS_USED;
            $passwordReset->save();
            DB::commit();
    
            $stats = '';
            $errorDetails = null;
            $stats = "Action Success !";
            return view('auth.passwords.verified');

        } catch (Exception $exception) {
            DB::rollBack();
            $stats = $exception->getMessage();
            $errorDetails = $exception;
            dd($exception);
            return redirect()->back()->with('errorDetails', $errorDetails)->with('status', $stats);
        }
    }    


    /**
     * Forgot password.
     *
     * @param Request $request
     * @return \Illuminate\Http\Response
     */
    public function resetPasswordView(Request $request)
    {
        $email = $request->email ?? null;
        $type = $request->type ?? null;
        $token = $request->token ?? null;

        return view('auth.passwords.reset', compact('email', 'type', 'token'));
    }    


  /**
     * @param Request $request
     * @return JsonResponse
     */
    public function search(Request $request)
    {
        try {
            $users = User::search($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($users);
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
            $users = User::match($request->all())->paginate($request->input('limit') ?? 50)->toArray();
            return $this->responseSuccess($users);
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
                'id' => 'required|exists:users,id',
            ]);

            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $user = User::where('id', $id)->first();

            return $this->responseSuccess(array($user ?? []));
        } catch (Exception $exception) {
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
                'id' => 'required|exists:users,id',
                'fcm_token' => 'nullable|string',
                'status' => 'sometimes|required|in:' . implode(',', UserList::ALL_STATUS),
            ]);
            if ($validator->fails()) {
                return $this->responseFailed($validator->errors()->toArray());
            }

            $patchingData = [];

            if (array_key_exists('name', $input)) {
                $patchingData['name'] = $input['name'];
            }

            if (array_key_exists('status', $input)) {
                $patchingData['status'] = $input['status'];
            }

            if (array_key_exists('fcm_token', $input)) {
                $patchingData['fcm_token'] = $input['fcm_token'];
            }

            $user = User::updateOrCreate(['id' => $id], $patchingData);

            return $this->responseSuccess($user->toArray());
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }



    /**
     * @param Request $request
     * @return JsonResponse
     */
    public function checkUsername(Request $request)
    {
        try {
            $input = $request->all();
            $validator = Validator::make($input, [
                'email' => 'required|exists:users,email',
            ]);
            if ($validator->fails()) {
                return $this->responseFailed([], 'username email tidak terdaftar di sistem!');
            }
            return $this->responseSuccess([],'username terdaftar!');
        } catch (Exception $exception) {
            return $this->responseError($exception->getMessage());
        }
    }

}
