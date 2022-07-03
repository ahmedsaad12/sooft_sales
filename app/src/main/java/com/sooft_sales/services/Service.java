package com.sooft_sales.services;

import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.DepartmentDataModel;
import com.sooft_sales.model.OrderDataModel;
import com.sooft_sales.model.ProductDataModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SingleProductDataModel;
import com.sooft_sales.model.StatusResponse;
import com.sooft_sales.model.UserModel;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {
    @FormUrlEncoded
    @POST("api/auth/login")
    Single<Response<UserModel>> login(@Field("user_name") String user_name,
                                      @Field("lang") String lang,
                                      @Field("password") String password);

    @GET("api/home/categories")
    Single<Response<DepartmentDataModel>> getDepartments(@Header("Authorization") String authorization);

    @GET("api/home/products")
    Single<Response<ProductDataModel>> getProducts(@Header("Authorization") String authorization);

    @Multipart
    @POST("api/home/addProduct")
    Single<Response<SingleProductDataModel>> addPeoduct(@Header("Authorization") String authorization,
                                                        @Part("title_ar") RequestBody title_ar,
                                                        @Part("title_en") RequestBody title_en,
                                                        @Part("category_id") RequestBody category_id,
                                                        @Part("price") RequestBody price,
                                                        @Part MultipartBody.Part photo);

    @GET("api/order/orders")
    Single<Response<OrderDataModel>> getOrders(@Header("Authorization") String authorization);

    @POST("api/order/storeOrder")
    Single<Response<StatusResponse>> sendOrder(@Header("Authorization") String authorization,
                                               @Body CreateOrderModel cartDataModel
    );
    @FormUrlEncoded
    @POST("api/order/backOrder")
    Single<Response<StatusResponse>> backOrder(@Header("Authorization") String authorization,
                                               @Field("id") String id
    );

    @GET("api/home/setting")
    Single<Response<SettingDataModel>> getSetting(@Header("Authorization") String authorization);
    @POST("api/auth/logout")
    Single<Response<StatusResponse>> logout(@Header("Authorization") String authorization


    );
}