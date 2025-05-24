<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\EmailVerificationController;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::get('transactions/export/{id}/{tipe}/{userId}', 'TransactionController@export');

Route::middleware('auth:sanctum','verified')->get('/user', function (Request $request) {
    return $request->user();
});
Route::post('/auth/forgot-password', 'UserController@forgotPassword');
Route::post('reset-password-web', 'UserController@resetPasswordWeb')->name('user.reset-password-web');
Route::post('/auth/check-user', 'UserController@checkUsername');
Route::post('reset-password', 'UserController@resetPassword');
Route::post('/auth/register', [AuthController::class, 'register']);
Route::post('/auth/login', [AuthController::class, 'login']);


Route::get('/email/resend', 'EmailVerificationController@resend')->name('verification.resend');
Route::get('/email/verify/{id}/{hash}', 'EmailVerificationController@verify')->name('verification.verify');

Route::post('/password/email', 'ForgotPasswordController@sendResetLinkEmail');
Route::post('/password/reset', 'ResetPasswordController@reset');

Route::group(['middleware' => ['auth:sanctum']], function () {
	Route::get('/me', [AuthController::class, 'me']);
	Route::group(['prefix' => 'users'], function () {
        Route::patch('password', 'UserController@updatePassword');
	    Route::patch('detail/{id}', 'UserDetailController@update');
        Route::get('search', 'UserController@search'); //all
        Route::get('match', 'UserController@match'); //all
        Route::post('store', 'UserController@store');
        Route::patch('{id}', 'UserController@update');
        Route::get('{id}', 'UserController@get'); //all        
	});

    Route::group(['prefix' => 'categories'], function () {
        Route::get('search', 'CategoryController@search'); //all
        Route::get('match', 'CategoryController@match'); //all
        Route::post('store', 'CategoryController@store');
        Route::patch('{id}', 'CategoryController@update');
        Route::delete('{id}', 'CategoryController@delete');
        Route::get('{id}', 'CategoryController@get'); //all
    });


    Route::group(['prefix' => 'categorize-clothe'], function () {
        Route::get('search', 'CategorizeClotheController@search'); //all
        Route::get('match', 'CategorizeClotheController@match'); //all
        Route::post('store', 'CategorizeClotheController@store');
        Route::patch('{id}', 'CategorizeClotheController@update');
        Route::delete('{id}', 'CategorizeClotheController@delete');
        Route::get('{id}', 'CategorizeClotheController@get'); //all
    });

    Route::group(['prefix' => 'banks'], function () {
        Route::get('search', 'BankController@search'); //all
        Route::get('match', 'BankController@match'); //all
        Route::post('store', 'BankController@store');
        Route::patch('{id}', 'BankController@update');
        Route::delete('{id}', 'BankController@delete');
        Route::get('{id}', 'BankController@get'); //all
    });

    Route::group(['prefix' => 'notifications'], function () {
        Route::get('search', 'NotificationController@search'); //all
        Route::get('match', 'NotificationController@match'); //all
    });

    Route::group(['prefix' => 'type-laundries'], function () {
        Route::get('search', 'TypeLaundryController@search'); //all
        Route::get('match', 'TypeLaundryController@match'); //all
        Route::post('store', 'TypeLaundryController@store');
        Route::patch('{id}', 'TypeLaundryController@update');
        Route::delete('{id}', 'TypeLaundryController@delete');
        Route::get('{id}', 'TypeLaundryController@get'); //all
    });    



    Route::group(['prefix' => 'transactions'], function () {
        Route::get('search', 'TransactionController@search'); //all
        Route::get('match', 'TransactionController@match'); //all
        Route::post('store', 'TransactionController@store');
        Route::get('fee/{id}', 'TransactionController@getFee'); //all
        Route::post('update-status/{id}', 'TransactionController@updateStatus');
        Route::post('update-payment/{id}', 'TransactionController@updatePayment');
        Route::post('update-details/{id}', 'TransactionController@updateDetails');
        Route::get('update-handle/{id}', 'TransactionController@updateHandle');
        Route::get('{id}', 'TransactionController@get'); //all
        Route::get('push/{id}', 'TransactionController@tesPush'); //all
        Route::get('check/{id}', 'TransactionController@exportCheck'); //all
    });


    Route::group(['prefix' => 'payments'], function () {
        Route::post('topup', 'PaymentController@topup');
        Route::post('confirm', 'PaymentController@confirm'); //all
        Route::get('get-info', 'PaymentController@getPaymentInfo'); //all
    });

    Route::post('/auth/logout', [AuthController::class, 'logout']);
});