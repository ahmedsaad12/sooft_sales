package com.sooft_sales.model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.sooft_sales.R;

import java.io.Serializable;

public class UserSignUpModel extends BaseObservable implements Serializable {
    private String image;
    private String name;
    private String phone_code;
    private String phone;


    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.isEmpty()
        ) {
            error_name.set(null);

            return true;
        } else {

            error_name.set(context.getString(R.string.field_required));

            return false;
        }


    }


    public UserSignUpModel() {
        image = "";
        name = "";
        phone = "";
        phone_code = "20";


    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }


    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
//        notifyPropertyChanged(BR.phone);
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }
}
