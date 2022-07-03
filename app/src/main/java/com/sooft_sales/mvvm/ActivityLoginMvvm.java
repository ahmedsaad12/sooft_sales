package com.sooft_sales.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sooft_sales.R;
import com.sooft_sales.model.LoginModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.remote.Api;
import com.sooft_sales.share.Common;
import com.sooft_sales.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityLoginMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityLoginMvvm";
    private MutableLiveData<UserModel> onLoginSuccess = new MutableLiveData<>();
    private MutableLiveData<SettingModel> onSettingSuccess = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressDialog dialog;

    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);


    }

    public MutableLiveData<UserModel> onLoginSuccess() {
        if (onLoginSuccess == null) {
            onLoginSuccess = new MutableLiveData<>();
        }
        return onLoginSuccess;
    }

    public MutableLiveData<SettingModel> onSettingSuccess() {
        if (onSettingSuccess == null) {
            onSettingSuccess = new MutableLiveData<>();
        }
        return onSettingSuccess;
    }

    public void login(Context context, LoginModel model) {
        if (dialog == null) {
            dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        }
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login(model.getUsername(), model.getLang(), model.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<UserModel> userModelResponse) {
                        dialog.dismiss();
                        // Log.e("jjjj", userModelResponse.code() + "");
                        if (userModelResponse.isSuccessful()) {
                            if (userModelResponse.body() != null && userModelResponse.body().getCode() == 200 && userModelResponse.body().getData() != null) {

                                onLoginSuccess().setValue(userModelResponse.body());
                            } else if (userModelResponse.body().getCode() == 410) {
                                Toast.makeText(context, context.getResources().getString(R.string.cant_login), Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(context, context.getResources().getString(R.string.incorrect), Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        dialog.dismiss();
                        //  Log.e("jjjj", e.toString() + "");

                    }
                });
    }

    public void getSetting(UserModel userModel) {

        dialog.show();

        Api.getService(Tags.base_url)
                .getSetting(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SettingDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SettingDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getCode() == 200) {
                                onSettingSuccess().setValue(response.body().getData());

                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                        onLoginSuccess().setValue(userModel);

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
