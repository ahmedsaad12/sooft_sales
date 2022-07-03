package com.sooft_sales.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.model.UserSettingsModel;
import com.sooft_sales.uis.activity_home.fragments_home_navigaion.FragmentCart;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }
    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void createUpdateUserData(Context context,UserModel userModel) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",user_data);
        editor.apply();

    }

    public void clearUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }
    public void create_update_user_settings(Context context, UserSettingsModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("settings", data);
        editor.apply();


    }

    public UserSettingsModel getUserSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        UserSettingsModel model = new Gson().fromJson(preferences.getString("settings",""),UserSettingsModel.class);
        return model;

    }

    public void create_update_user_additional_date(Context context, SettingModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("additional_settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("additional_settings", data);
        editor.apply();


    }

    public SettingModel getUserAdditionalSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("additional_settings_pref", Context.MODE_PRIVATE);
        SettingModel model = new Gson().fromJson(preferences.getString("additional_settings",""),SettingModel.class);
        return model;

    }



    public void create_baseurl(Context context, String basurl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("basurl", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("basurl", basurl);
        editor.apply();


    }

    public String getbasurl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("basurl", Context.MODE_PRIVATE);
        String basurl = preferences.getString("basurl","");
        return basurl;
    }


    public void createUpdateAppSetting(Context context, UserSettingsModel settings) {
        SharedPreferences preferences = context.getSharedPreferences("settingsEbsar", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String data = gson.toJson(settings);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("settings", data);
        editor.apply();
    }
    public UserSettingsModel getAppSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settingsEbsar", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString("settings",""), UserSettingsModel.class);
    }
    public void create_update_cart_soft(Context context , CreateOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_soft", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_soft_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_soft_data", cart_soft_data);
        editor.apply();

    }

    public CreateOrderModel getcart_softData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_soft", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_soft_data","");
        Gson gson = new Gson();
        CreateOrderModel model = gson.fromJson(json_data, CreateOrderModel.class);
        return model;
    }

    public void clearcart_soft(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cart_soft", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }

    public void createUpdateUserDataSetting(Context context,SettingDataModel settingDataModel) {
        SharedPreferences preferences = context.getSharedPreferences("user_set", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(settingDataModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data_set",user_data);
        editor.apply();

    }
    public void create_ordernum(Context context, int num) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ordernum", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ordernum", num);
        editor.apply();


    }

    public int getOrdernum(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ordernum", Context.MODE_PRIVATE);
        int ordernum = preferences.getInt("ordernum",0);
        return ordernum;
    }



}