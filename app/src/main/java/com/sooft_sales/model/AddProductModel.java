package com.sooft_sales.model;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.sooft_sales.BR;
import com.sooft_sales.R;


public class AddProductModel extends BaseObservable {

    private int id;
    private String english_title;
    private String arabic_title;
    private String price;

    private Uri image;
    private int category_id;

    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();



    public boolean isDataValid(Context context)
    {
        if (!english_title.trim().isEmpty()&&
                !arabic_title.trim().isEmpty()
                &&category_id!=0&&image!=null
        ){
            error_english_title.set(null);
            error_arabic_title.set(null);

            return true;
        }else
            {
                if (english_title.trim().isEmpty())
                {
                    error_english_title.set(context.getString(R.string.field_required));

                }else
                    {
                        error_english_title.set(null);

                    }


                if (arabic_title.trim().isEmpty())
                {
                    error_arabic_title.set(context.getString(R.string.field_required));

                }else
                {
                    error_arabic_title.set(null);

                }
                if(image==null){
                    Toast.makeText(context,context.getResources().getString(R.string.ch_imge),Toast.LENGTH_LONG).show();
                }
if(category_id==0){
    Toast.makeText(context,context.getResources().getString(R.string.ch_cat),Toast.LENGTH_LONG).show();
}

                return false;
            }
    }
    public AddProductModel() {
        setEnglish_title("");
        setArabic_title("");
        setPrice("");
        setImage(null);

    }



    @Bindable
    public String getEnglish_title() {
        return english_title;
    }

    public void setEnglish_title(String english_title) {
        this.english_title = english_title;
        notifyPropertyChanged(BR.english_title);

    }


    @Bindable
    public String getArabic_title() {
        return arabic_title;
    }

    public void setArabic_title(String arabic_title) {
        this.arabic_title = arabic_title;
        notifyPropertyChanged(BR.arabic_title);
    }

    @Bindable

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}

