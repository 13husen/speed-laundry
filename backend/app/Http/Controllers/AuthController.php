<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use App\Models\UserDetail;
use App\Enums\UserList;
use App\Enums\RoleList;
use Auth;
use Illuminate\Foundation\Auth\EmailVerificationRequest;
use Illuminate\Auth\Events\Registered;
use Illuminate\Support\Facades\DB;


class AuthController extends Controller
{

    public function register(Request $request)
    {
        try {
            DB::beginTransaction();        
            $attr = $request->validate([
                'name' => 'required|string|max:255',
                'phone_number' => 'required|string|max:50',
                'email' => 'required|string|email|unique:users,email',
                'password' => 'required|string|min:6|confirmed',
                'type' => 'required|in:' . implode(',', UserList::ALL_TYPE),
            ]);

            $userDetail = UserDetail::create([
                'phone_number' => $attr['phone_number'],
            ]);
            $user = User::create([
                'name' => $attr['name'],
                'password' => encrypt($attr['password']),
                'email' => $attr['email'],
                'type' => $attr['type'],
                'status' => 1,
                'balance' => 0,
                'user_detail_id' => $userDetail->id
            ]);

            event(new Registered($user));
        // $request->user()->sendEmailVerificationNotification();
            DB::commit();
            return $this->responseSuccess([
                'token' => $user->createToken('API Token')->plainTextToken
            ], "User Registered, Email Verification has been sent!");            
        } catch (Exception $exception) {
            DB::rollBack();
            return $this->responseError($exception->getMessage());
        }        
    }

    public function login(Request $request)
    {
        $input = $request->all();
        $rules = [
            'type' => 'required|in:' . implode(',', RoleList::ALL_ROLES),
            'password' => 'required',
        ];
        $rules['email'] = 'required|email';

        $user = User::where("email", $input["email"])
            ->where('type', $input['type'])->first();

        if (!$user) {
            return $this->responseFailed([
                'password' => ['Kesalahan Krudential username atau password.']
            ]);
        }

        //check status
        if ($user->status == UserList::STATUS_BLOCK || $user->status == UserList::STATUS_SUSPEND) {
            return $this->responseFailed([
                'password' => ['Akun anda di tangguhkan, mohon hubungi admin atau pihak toko.']
            ]);
        }
        // remove old or existing token
        $user->tokens()->delete();

        if (decrypt(($user->password ?? '')) == $input['password']) {
            $success['token'] = $user->createToken('API Token')->plainTextToken;
            $success['user'] = $user;

            return $this->responseSuccess($success);
        }

        return $this->responseFailed([
            'password' => ['Kesalahan Krudential username atau password.']
        ]);        
    }

    public function me()
    {
        return $this->responseSuccess([
            'user' => auth()->user(),
        ]);
    }

    public function logout()
    {
        auth()->user()->tokens()->delete();

        return [
            'message' => 'Tokens Revoked'
        ];
    }
}
