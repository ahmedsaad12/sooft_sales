package com.sooft_sales.uis.activity_invoice;

import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.KITKAT;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.exoplayer2.util.Log;

import com.sooft_sales.R;
import com.sooft_sales.adapter.ProductBillAdapter;
import com.sooft_sales.databinding.ActivityInvoiceBinding;
import com.sooft_sales.language.Language;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.ItemCartModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.model.ZatcaQRCodeGeneration;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.printer_utils.BluetoothUtil;
import com.sooft_sales.printer_utils.ESCUtil;
import com.sooft_sales.printer_utils.SunmiPrintHelper;
import com.sooft_sales.uis.activity_base.BaseActivity;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.paperdb.Paper;
import pl.allegro.finance.tradukisto.ValueConverters;

public class InvoiceActivity extends BaseActivity {
    private ActivityInvoiceBinding binding;
    private Preferences preferences;
    private String lang;
    private CreateOrderModel createOrderModel;
    private UserModel userModel;
    private List<ItemCartModel> limsProductSaleDataList;
    private ProductBillAdapter productBillAdapter;
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_req = 100;
    private SettingModel settingDataModel;
    private boolean isPermissionGranted = false;
    private Context context;
    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        createOrderModel = (CreateOrderModel) intent.getSerializableExtra("data");
    }



    private void initView() {
        limsProductSaleDataList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        settingDataModel = preferences.getUserAdditionalSettings(this);
        lang = getLang();
        binding.setLang(lang);
        productBillAdapter = new ProductBillAdapter(limsProductSaleDataList, this);
        binding.recView.setNestedScrollingEnabled(false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(productBillAdapter);
        // getlastInvoice();
        binding.setSettingmodel(settingDataModel);
        binding.btnConfirm3.setOnClickListener(view -> printApiBitmap());
        updateData();
    }

    private void updateData() {

        dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
        binding.tvDate.setText(dateFormat.format(new Date(createOrderModel.getOrder_date_time())).split(" ")[0]);

        binding.tvTime.setText(dateFormat.format(new Date(createOrderModel.getOrder_date_time())).split(" ")[1]);
        ZatcaQRCodeGeneration.Builder builder;
        try {
            builder = new ZatcaQRCodeGeneration.Builder();
            builder.sellerName(userModel.getData().getUser().getName()) // Shawrma House
                    .taxNumber(userModel.getData().getSetting().getVat()) // 1234567890
                    .invoiceDate(dateFormat.format(new Date(createOrderModel.getOrder_date_time()))) //..> 22/11/2021 03:00 am
                    .totalAmount((createOrderModel.getTotal() + createOrderModel.getTax() - createOrderModel.getDiscount()) + "") // 100
                    .taxAmount(createOrderModel.getTax() + "");
        } catch (Exception e) {
            builder = new ZatcaQRCodeGeneration.Builder();
            builder.sellerName(userModel.getData().getUser().getName()) // Shawrma House
                    .taxNumber("") // 1234567890
                    .invoiceDate(dateFormat.format(new Date(createOrderModel.getOrder_date_time()))) //..> 22/11/2021 03:00 am
                    .totalAmount((createOrderModel.getTotal() + createOrderModel.getTax() - createOrderModel.getDiscount()) + "") // 100
                    .taxAmount(createOrderModel.getTax() + "");
        }

        binding.setModel(createOrderModel);
        binding.setUsermodel(userModel);

        binding.setImage(builder.getBase64());

        if (createOrderModel.getDetails() != null && createOrderModel.getDetails().size() > 0) {
            limsProductSaleDataList.addAll(createOrderModel.getDetails());
            productBillAdapter.notifyDataSetChanged();
           // Log.e("dkdkdk", limsProductSaleDataList.size() + "");

//      if(limsProductSaleDataList.size()>3){
//          LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
//          lp.weight = 1;
//          binding.fl.setLayoutParams(lp);
//
//      }
        }
    }

//    public void getlastInvoice() {
//        Log.e("kdkkd", salid);
//        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//
//
//        Api.getService(Tags.base_url)
//                .getInv(salid, userModel.getUser().getId() + "")
//                .enqueue(new Callback<InvoiceDataModel>() {
//                    @Override
//                    public void onResponse(Call<InvoiceDataModel> call, Response<InvoiceDataModel> response) {
//                        dialog.dismiss();
//                        if (response.isSuccessful() && response.body() != null) {
//                            if (response.body().getStatus() == 200) {
//                                if (response.body() != null) {
//                                    updateData(response.body());
//
////                                    Intent intent = new Intent(HomeActivity.this, InvoiceActivity.class);
////                                    intent.putExtra("data", response.body().getData());
////                                    startActivity(intent);
//                                } else if (response.body().getStatus() == 400) {
//                                    Toast.makeText(InvoiceActivity.this, getResources().getString(R.string.no_invoice), Toast.LENGTH_SHORT).show();
//
//                                }
//
//                            }
//
//                        } else {
//                            if (response.code() == 500) {
//                                Toast.makeText(InvoiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e("ERROR", response.message() + "");
//
//                                //     Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<InvoiceDataModel> call, Throwable t) {
//                        try {
//                            dialog.dismiss();
//                            if (t.getMessage() != null) {
//                                Log.e("msg_category_error", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    // Toast.makeText(SubscriptionActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    //Toast.makeText(SubscriptionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });
//    }

    private void printApiBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(binding.scrollView.getChildAt(0).getWidth(), binding.scrollView.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        binding.scrollView.draw(canvas);

        if (bitmap != null) {

            Toast.makeText(this, "Printing", Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            double h = bitmap.getWidth() / 384.0;
            double dstH = bitmap.getHeight() / h;


            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 384, (int) dstH, true);


            int width = newBitmap.getWidth();
            int height = newBitmap.getHeight();
            int newWidth = (width/8+1)*8;
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, 1);
            Bitmap b = Bitmap.createBitmap(newBitmap, 0, 0, width, height, matrix, true);
            SunmiPrintHelper.getInstance().printBitmap(b,1);





        }
    }

}
