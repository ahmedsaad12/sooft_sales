package com.sooft_sales.uis.activity_base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.model.UserSettingsModel;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.tags.Tags;

import io.paperdb.Paper;

public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Tags.setcontext(context);

    }

    protected String getLang() {
        Paper.init(context);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(context);

    }

    protected void clearUserModel(Context context) {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(context);

    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(context, userModel);
    }


    public void setUserSettings(UserSettingsModel userSettingsModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_settings(context, userSettingsModel);
    }

    public UserSettingsModel getUserSettings() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettings(context);
    }

    public void setUserAdditionalSettings(SettingModel settings) {
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_additional_date(context, settings);
    }

    public SettingModel getUserAdditionalSettings() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserAdditionalSettings(context);
    }

}
