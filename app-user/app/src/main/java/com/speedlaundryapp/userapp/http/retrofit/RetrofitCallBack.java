package com.speedlaundryapp.userapp.http.retrofit;

import com.speedlaundryapp.userapp.model.ConfirmForgetPassword;
import com.speedlaundryapp.userapp.model.WarpingResponse;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheResponse;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryResponse;
import com.speedlaundryapp.userapp.model.laundry.notification.NotificationResponse;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionResponse;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryResponse;
import com.speedlaundryapp.userapp.model.user.auth.AccountData;
import com.speedlaundryapp.userapp.model.user.data.UserResponse;
import com.speedlaundryapp.userapp.model.user.info_user.MyAccount;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitCallBack {
    @POST("auth/login")
    Call<AccountData> login(@Body RequestBody user);

    @POST("auth/register")
    Call<AccountData> send(@Body RequestBody user);

    @POST("auth/register/confirm")
    Call<AccountData> confirm(@Body RequestBody user);

    @POST("auth/register")
    Call<AccountData> register(@Body RequestBody user);

    @POST("auth/update/phone/send")
    Call<ResponseBody> requestOTPPhone();

    @PATCH("users/password")
    Call<ResponseBody> updatePassword(@Body RequestBody user);

    @POST("auth/update/phone/confirm")
    Call<ResponseBody> updatePhoneNumber(@Body RequestBody user);

    @POST("auth/logout")
    Call<ResponseBody> logout();

    @GET("notifications/search")
    Call<WarpingResponse<NotificationResponse>> getNotificationList(@QueryMap Map<String, Object> map);

    @GET("me")
    Call<MyAccount> myAccount();

    @POST("auth/forgot-password/send")
    @FormUrlEncoded
    Call<ResponseBody> forgotPassword(@Field("phone") String phone);

    @POST("auth/forgot-password/confirm")
    Call<ResponseBody> otpForgotPassword(@Body ConfirmForgetPassword user);

    @POST("auth/check-user")
    Call<ResponseBody> check(@Body RequestBody categoryItem);

    @POST("auth/forgot-password")
    Call<ResponseBody> forgotPassword(@Body RequestBody request);

    @GET("me")
    Call<ResponseBody> getProfile();

    /* Categories */
    @GET("categories/search?limit=10")
    Call<WarpingResponse<CategoryResponse>> getCategoriesLaundry(@QueryMap Map<String, Object> map);

    @GET("categories/{id}")
    Call<ResponseBody> getCategory(@Path("id") int id);

    @DELETE("categories/{id}")
    Call<ResponseBody> deleteCategory(@Path("id") int id);

    @POST("categories/store")
    Call<ResponseBody> createCategory(@Body RequestBody categoryItem);

    @PATCH("categories/{id}")
    Call<ResponseBody> editCategory(@Path("id") int id, @Body RequestBody categoryItem);

    /* Categories Clothe*/
    @GET("categorize-clothe/search?limit=10")
    Call<WarpingResponse<CategorizeClotheResponse>> getCategorizeClotheLaundry(@QueryMap Map<String, Object> map);

    @GET("categorize-clothe/{id}")
    Call<ResponseBody> getCategorizeClothe(@Path("id") int id);

    @DELETE("categorize-clothe/{id}")
    Call<ResponseBody> deleteCategorizeClothe(@Path("id") int id);

    @POST("categorize-clothe/store")
    Call<ResponseBody> createCategorizeClothe(@Body RequestBody categoryItem);

    @PATCH("categorize-clothe/{id}")
    Call<ResponseBody> editCategorizeClothe(@Path("id") int id, @Body RequestBody categoryItem);

    /* Type Laundry */
    @GET("type-laundries/search?limit=10")
    Call<WarpingResponse<TypeLaundryResponse>> getTypeLaundry(@QueryMap Map<String, Object> map);

    @GET("type-laundries/{id}")
    Call<ResponseBody> getType(@Path("id") int id);

    @DELETE("type-laundries/{id}")
    Call<ResponseBody> deleteType(@Path("id") int id);

    @POST("type-laundries/store")
    Call<ResponseBody> createType(@Body RequestBody categoryItem);

    @PATCH("type-laundries/{id}")
    Call<ResponseBody> ediType(@Path("id") int id, @Body RequestBody categoryItem);

    // User
    @GET("users/search?limit=10")
    Call<WarpingResponse<UserResponse>> getUsers(@QueryMap Map<String, Object> map);

    @GET("users/{id}")
    Call<ResponseBody> getUser(@Path("id") int id);

    @PATCH("users/{id}")
    Call<ResponseBody> editUser(@Path("id") int id, @Body RequestBody categoryItem);

    //
    @GET("transactions/search?limit=10")
    Call<WarpingResponse<TransactionResponse>> getTransactions(@QueryMap Map<String, Object> map);

    @GET("transactions/{id}")
    Call<ResponseBody> getTransaction(@Path("id") int id);
    @GET("transactions/fee/{id}")
    Call<ResponseBody> getTransactionFee(@Path("id") int id);

    @PATCH("transactions/{id}")
    Call<ResponseBody> editTransaction(@Path("id") int id, @Body RequestBody categoryItem);

    // update handle
    @GET("transactions/update-handle/{id}")
    Call<ResponseBody> updateHandle(@Path("id") int id);
    // update status
    @POST("transactions/update-status/{id}")
    Call<ResponseBody> updateStatus(@Path("id") int id,@Body RequestBody statusData);

    @POST("transactions/update-details/{id}")
    Call<ResponseBody> updateDetails(@Path("id") int id,@Body RequestBody payloadData);

    @POST("transactions/update-payment/{id}")
    Call<ResponseBody> updatePayment(@Path("id") int id,@Body RequestBody payloadData);

    @POST("payments/confirm")
    Call<ResponseBody> updateConfirm(@Body RequestBody payloadData);


    @PATCH("users/detail/{id}")
    Call<ResponseBody> updateUserDetails(@Path("id") int id,@Body RequestBody payloadData);

    @POST("transactions/store")
    Call<ResponseBody> createLaundryRequest(@Body RequestBody payloadData);

    @POST("payments/topup")
    Call<ResponseBody> topup(@Body RequestBody payloadData);

    @PATCH("users/{id}")
    Call<ResponseBody> updateUser(@Path("id") int id,@Body RequestBody payloadData);

}

