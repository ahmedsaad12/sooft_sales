package com.sooft_sales.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.gson.Gson;
import com.sooft_sales.R;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.DepartmentDataModel;
import com.sooft_sales.model.DepartmentModel;
import com.sooft_sales.model.OrderDataModel;
import com.sooft_sales.model.ProductDataModel;
import com.sooft_sales.model.ProductModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SingleProductDataModel;
import com.sooft_sales.model.StatusResponse;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.remote.Api;
import com.sooft_sales.share.Common;
import com.sooft_sales.tags.Tags;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private Context context;

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<DepartmentModel>> departmentLivData;
    private MutableLiveData<List<ProductModel>> listMutableLiveData;
    private MutableLiveData<List<CreateOrderModel>> mutableLiveData;
    private MutableLiveData<SettingDataModel> settingDataModelMutableLiveData;
    private MutableLiveData<Boolean> onLogoutSuccess;

    private MutableLiveData<ProductModel> productMutableLiveData;
    private MutableLiveData<Boolean> send;
    private MutableLiveData<Boolean> back;


    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Boolean> getSend() {
        if (send == null) {
            send = new MutableLiveData<>();
        }

        return send;
    }

    public LiveData<SettingDataModel> getSetting() {
        if (settingDataModelMutableLiveData == null) {
            settingDataModelMutableLiveData = new MutableLiveData<>();
        }

        return settingDataModelMutableLiveData;
    }

    public LiveData<Boolean> getBack() {
        if (back == null) {
            back = new MutableLiveData<>();
        }

        return back;
    }

    public MutableLiveData<List<ProductModel>> getProductList() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    public MutableLiveData<List<CreateOrderModel>> getOrderList() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    public MutableLiveData<ProductModel> getProduct() {
        if (productMutableLiveData == null) {
            productMutableLiveData = new MutableLiveData<>();
        }
        return productMutableLiveData;
    }


    public LiveData<List<DepartmentModel>> getCategoryData() {
        if (departmentLivData == null) {
            departmentLivData = new MutableLiveData<>();

        }
        return departmentLivData;
    }


    public void getDepartment(UserModel userModel) {
        Api.getService(Tags.base_url)
                .getDepartments(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<DepartmentDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<DepartmentDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("jjj",response.body().getCode()+"");
                            if (response.body().getCode() == 200) {
                                List<DepartmentModel> list = response.body().getData();
                                if (list.size() > 0) {
                                    departmentLivData.setValue(list);
                                } else {
                                    departmentLivData.setValue(new ArrayList<>());

                                }

                            } else {
                                departmentLivData.setValue(new ArrayList<>());

                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // Log.e(TAG, "onError: ", e);
                    }
                });

    }

    public void getProducts(UserModel userModel) {


        Api.getService(Tags.base_url)
                .getProducts(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ProductDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    public void onSuccess(@NonNull Response<ProductDataModel> response) {
                        //    Log.e("dddd", response.code() + "");

                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("dddd", response.body().getCode() + "");
                            if (response.body().getCode() == 200) {
                                // List<ProductModel> list = response.body().getData();
                                listMutableLiveData.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //  Log.e("kkkk", e.toString());
                    }
                });
    }

    public void getOrders(UserModel userModel) {


        Api.getService(Tags.base_url)
                .getOrders(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<OrderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    public void onSuccess(@NonNull Response<OrderDataModel> response) {
                        //  Log.e("dddd", response.code() + "");

                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("dddd", response.body().getCode() + "");
                            if (response.body().getCode() == 200) {
                                // List<ProductModel> list = response.body().getData();
                                mutableLiveData.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //Log.e("kkkk", e.toString());
                    }
                });
    }

    public void sendOrder(CreateOrderModel cartDataModel, UserModel userModel) {
        cartDataModel.setLocal(false);
        cartDataModel.setLocal1(null);
        Gson gson = new Gson();
        String user_data = gson.toJson(cartDataModel);
        Log.e("user", user_data);
      //  Toast.makeText(context, user_data, Toast.LENGTH_SHORT).show();
        Api.getService(Tags.base_url)
                .
                        sendOrder(userModel.getData().getAccess_token(), cartDataModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        //Log.e("order",response.code()+"");
//                        Log.e("slkdkdkkdk", response.code() + ""+cartDataModel.getLatitude()+" "+cartDataModel.getLongitude()+" "+response.body().getStatus());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == 200) {

                                send.postValue(true);

                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //  Log.e(TAG, "onError: ", e);
                    }
                });

    }

    public void backOrder(CreateOrderModel cartDataModel, UserModel userModel) {

        Api.getService(Tags.base_url)
                .backOrder(userModel.getData().getAccess_token(), cartDataModel.getId() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
//                        Log.e("slkdkdkkdk", response.code() + ""+cartDataModel.getLatitude()+" "+cartDataModel.getLongitude()+" "+response.body().getStatus());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == 200) {

                                back.postValue(true);

                            } else {
                                back.postValue(true);

                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //   Log.e(TAG, "onError: ", e);
                    }
                });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void addproduct(Context context, ProductModel model, UserModel userModel) {
        RequestBody ar_part, en_part;
        if (userModel.getData().getUser().getLang().equals("ar")) {
            ar_part = Common.getRequestBodyText(model.getTitle());
            en_part = Common.getRequestBodyText(model.getTitle2());
        } else {
            en_part = Common.getRequestBodyText(model.getTitle());
            ar_part = Common.getRequestBodyText(model.getTitle2());
        }
        RequestBody price = Common.getRequestBodyText(model.getPrice() + "");
        RequestBody cat_part = Common.getRequestBodyText(model.getCategory_id() + "");


        MultipartBody.Part image = Common.getMultiPart(context, getUriFromBitmap(model.getImageBitmap(), context), "photo");


        Api.getService(Tags.base_url).addPeoduct(userModel.getData().getAccess_token(), ar_part, en_part, cat_part, price, image).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io()).subscribe(new SingleObserver<Response<SingleProductDataModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<SingleProductDataModel> singleProductDataModelResponse) {
                if (singleProductDataModelResponse.isSuccessful()) {
                    // Log.e("kkkk", singleProductDataModelResponse.body().getCode() + "");
                    if (singleProductDataModelResponse.body().getCode() == 200) {

                        productMutableLiveData.postValue(singleProductDataModelResponse.body().getData());
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //   Log.e("hhhh", e.toString());
            }
        });

    }

    private Uri getUriFromBitmap(byte[] endPoint, Context context) {
        //  Log.e(";;;;",endPoint.length+"");
        Bitmap bitmap = BitmapFactory.decodeByteArray(endPoint, 0, endPoint.length);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "filename", ""));
    }

    public void getSetting(UserModel userModel) {


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
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == 200) {

                                settingDataModelMutableLiveData.setValue(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public LiveData<Boolean> onLogoutSuccess() {
        if (onLogoutSuccess == null) {
            onLogoutSuccess = new MutableLiveData<>();
        }

        return onLogoutSuccess;
    }

    public void logout(Context context, UserModel userModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).logout(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                        dialog.dismiss();
                        if (statusResponseResponse.isSuccessful()) {
                            //  Log.e("code", statusResponseResponse.body().getStatus()+"");

                            if (statusResponseResponse.body().getCode() == 200) {
                                onLogoutSuccess.setValue(true);
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }
                });
    }

}
