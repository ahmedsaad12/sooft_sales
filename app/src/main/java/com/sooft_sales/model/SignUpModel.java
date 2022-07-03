package com.sooft_sales.model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.sooft_sales.R;


public class SignUpModel extends BaseObservable {
    private String first_name;
    private String seconed_name;
    private boolean ischeck;
    private String code;


    public ObservableField<String> error_first_name = new ObservableField<>();
    public ObservableField<String> error_seconed_name = new ObservableField<>();
    public ObservableField<String> error_code = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!first_name.trim().isEmpty()
                &&
                !seconed_name.trim().isEmpty()
                && (!ischeck || !code.isEmpty())
            //&&
//               department_id != 0


        ) {
            error_first_name.set(null);
            error_seconed_name.set(null);

            return true;
        } else {

            if (first_name.trim().isEmpty()) {
                error_first_name.set(context.getString(R.string.field_required));

            } else {
                error_first_name.set(null);

            }
            if (seconed_name.trim().isEmpty()) {
                error_seconed_name.set(context.getString(R.string.field_required));

            } else {
                error_seconed_name.set(null);

            }

            if (ischeck) {
                if (code.isEmpty()) {
                    error_code.set(context.getString(R.string.field_required));

                } else {
                    error_code.set(null);

                }

            }

            return false;
        }
    }

    public SignUpModel() {

        setFirst_name("");

        setSeconed_name("");
        setCode("");

    }


    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
//        notifyPropertyChanged(BR.first_name);

    }

    @Bindable
    public String getSeconed_name() {
        return seconed_name;
    }

    public void setSeconed_name(String seconed_name) {
        this.seconed_name = seconed_name;
//        notifyPropertyChanged(BR.seconed_name);

    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}