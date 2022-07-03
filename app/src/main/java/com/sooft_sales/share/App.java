package com.sooft_sales.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.sooft_sales.language.Language;
import com.sooft_sales.printer_utils.SunmiPrintHelper;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
        com.sooft_sales.share.TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/ar_font.ttf");
        com.sooft_sales.share.TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/ar_font.ttf");
        com.sooft_sales.share.TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/ar_font.ttf");
        com.sooft_sales.share.TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/ar_font.ttf");

    }
}

