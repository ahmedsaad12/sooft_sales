package com.sooft_sales.uis.activity_base;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sooft_sales.language.Language;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.model.UserSettingsModel;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.printer_utils.BluetoothUtil;
import com.sooft_sales.printer_utils.ESCUtil;
import com.sooft_sales.printer_utils.SunmiPrintHelper;
import com.sooft_sales.tags.Tags;

import io.paperdb.Paper;


public class BaseActivity extends AppCompatActivity {

    public static final String READ_REQ = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_REQ = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAM_REQ = Manifest.permission.CAMERA;
    public static final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        Tags.setcontext(this);
        initPrinterStyle();


    }
public void setcontext(){        Tags.setcontext(this);
}
    private void initPrinterStyle() {
        BluetoothUtil.isBlueToothPrinter =false;
        /*if(BluetoothUtil.isBlueToothPrinter){
            BluetoothUtil.sendData(ESCUtil.init_printer());
        }else{
            SunmiPrintHelper.getInstance().initPrinter();
        }*/

        SunmiPrintHelper.getInstance().initPrinter();

    }
    protected String getLang() {
        Paper.init(this);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(this);
    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(this, userModel);
    }


    public void setUserSettings(UserSettingsModel userSettingsModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_settings(this, userSettingsModel);
    }

    public UserSettingsModel getUserSettings() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettings(this);
    }

    public void setUserAdditionalSettings(SettingModel settings) {
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_additional_date(this, settings);
    }

    public SettingModel getUserAdditionalSettings() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserAdditionalSettings(this);
    }



    protected void clearUserModel(Context context) {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(context);

    }


}