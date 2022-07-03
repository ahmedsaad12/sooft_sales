package com.sooft_sales.tags;

import android.content.Context;

import com.sooft_sales.preferences.Preferences;

public class Tags {
    private static Context context1;

    public static String base_url ;
    public static final int not_id = 3254;
    public static final String not_tag = "wedding_not";
    public static final String DATABASE_NAME = "veg_db";

    public static final String table_products="products";
    public static final String table_category="categorys";
    public static final String table_order="orders";
    public static final String table_order_products="order_products";

    public static void setcontext(Context context){
        context1=context;
        getbaseurl();
    }

    private static void getbaseurl() {
       base_url= Preferences.getInstance().getbasurl(context1);
    }
}
