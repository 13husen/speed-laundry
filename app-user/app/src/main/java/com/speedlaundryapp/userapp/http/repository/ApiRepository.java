package com.speedlaundryapp.userapp.http.repository;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.speedlaundryapp.userapp.base_app.MainApplication;
import com.speedlaundryapp.userapp.dialog.DialogError;
import com.speedlaundryapp.userapp.dialog.DialogErrorException;
import com.speedlaundryapp.userapp.dialog.DialogLoading;
import com.speedlaundryapp.userapp.dialog.DialogMainLoading;
import com.speedlaundryapp.userapp.model.ResponseError;
import com.speedlaundryapp.userapp.session.LocalPreferences;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    public ResponseListener listener;
    private PojoListener pojoListener;
    boolean showDialogFragment = false;
    Activity activity;
    DialogMainLoading dialogMainLoading = new DialogMainLoading();
    DialogLoading dialogLoading = new DialogLoading(MainApplication.getInstance().getContext());
    private  FragmentManager manager;
    boolean showErrorDialog = false;
    private static final String DIALOG_LOADING = "dialog_loading";
    private static final String DIALOG_ERROR = "dialog_error";
    public static ApiRepository apiRepository;
    public static ApiRepository getInstance(){
        if(apiRepository != null){
            apiRepository = null;
        }
        apiRepository = new ApiRepository();
        return apiRepository;
    }
    private ApiRepository (){}

    public void setApiListener(Call<ResponseBody> callBack, ResponseListener listener) {
        this.listener = listener;
        getResponse(callBack);
    }

    @Deprecated
    public void setDialogLoading(@NotNull FragmentManager manager){
        showDialogFragment = true;
        FragmentTransaction ft = manager.beginTransaction();
        Fragment fragmentLoading = manager.findFragmentByTag(DIALOG_LOADING);
        Fragment fragmentError = manager.findFragmentByTag(DIALOG_ERROR);
        if(fragmentLoading != null){
            ft.remove(fragmentLoading);
        }
        if(fragmentError != null){
            ft.remove(fragmentError);
        }
        this.manager = manager;
        dialogMainLoading.show(manager, DIALOG_LOADING);
    }

    @Deprecated
    public void setDialogError(boolean bool){
        if(manager == null){
            showErrorDialog =false;
            return;
        }
        showErrorDialog = bool;
    }

    public void setDialog(@NotNull Activity activity){
        this.activity = activity;
        dialogLoading = new DialogLoading(activity);
        dialogLoading.show();
    }



    private void getResponse(Call<ResponseBody> callBack) {
        callBack.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                dismissDialog();
                try {
                    String sResponse;
                    if(response.isSuccessful()){
                        sResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(sResponse);
                        listener.onResponseListener(jsonObject,response.code(),RepositoryEnum.SUCCESS);
                    }else {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        ResponseError error = new Gson().fromJson(jsonObject.toString(),ResponseError.class);
                        DialogErrorException dialogErrorException = new DialogErrorException();
                        if(showErrorDialog && !dialogErrorException.isAdded()){
                            dialogErrorException.setError(error);
                            dialogErrorException.show(manager, DIALOG_ERROR);
                            if (error.getCode() != null){
                                if(error.getCode() == 100){
                                    LocalPreferences.getInstance().getLogout();
                                }
                            }
                        }
                        else if(activity != null){
                            if(error.getCode() == 401){
                                    LocalPreferences.getInstance().getLogout();
                            }
                        }
                        else {
                            if(error.getCode() == 100){
                                LocalPreferences.getInstance().getLogout();
                            }
                        }
                        listener.onResponseListener(jsonObject,response.code(),RepositoryEnum.FALSE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Context context = MainApplication.getInstance().getContext();
                Toast.makeText(context, t.getMessage() + "LALALALALAL",Toast.LENGTH_LONG).show();
                dismissDialog();
            }
        });
    }

    public <T> void setModelListener(Call<T> callBack, PojoListener<T> pojoListener ) {
        this.pojoListener = pojoListener;
        getResponseData(callBack);
    }

    public <T> void getResponseData(Call<T> callBack){
        Context context = MainApplication.getInstance().getContext();
        callBack.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    dismissDialog();
                    try {
                        if(response.isSuccessful()){
                            pojoListener.onResponseListener(response.body());
                        }else {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            ResponseError error = new Gson().fromJson(jsonObject.toString(),ResponseError.class);
                            pojoListener.onErrorListener(error, response.code());
                            if(showErrorDialog){
                                DialogErrorException dialogErrorException = new DialogErrorException();
                                dialogErrorException.setError(error);
                                dialogErrorException.show(manager, null);
                                if (error.getCode() != null){
                                    if(error.getCode() == 100 || error.getCode() == 401){
                                        dialogErrorException.setSubmitInterFace(v -> {
                                            LocalPreferences.getInstance().getLogout();
                                        });
                                    }
                                }
                            }
                            else if(activity != null){
                                DialogError dialogError = new DialogError(activity, error);
                                dialogError.show();
                                if(error.getCode() == 100){
                                    dialogError.setSubmitListener(v -> {
                                        LocalPreferences.getInstance().getLogout();
                                    });
                                }
                            }
                            else {
                                if(error.getCode() == 100){
                                    LocalPreferences.getInstance().getLogout();
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage()+"Lelelele",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    dismissDialog();
                    Toast.makeText(context, t.getMessage()+"LOLOLO",Toast.LENGTH_LONG).show();

                }
            });
    }

    public void dismissDialog(){
        if(showDialogFragment){
            dialogMainLoading.dismiss();
        }
        if(dialogLoading.isShowing()){
            dialogLoading.dismiss();
        }
    }

}
