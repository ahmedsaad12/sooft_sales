package com.sooft_sales.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;


import com.sooft_sales.R;

import com.sooft_sales.adapter.CartAdapter;
import com.sooft_sales.adapter.ProductAdapter;
import com.sooft_sales.databinding.FragmentCartBinding;
import com.sooft_sales.local_database.AccessDatabase;
import com.sooft_sales.local_database.DataBaseInterfaces;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.ItemCartModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.mvvm.FragmentCartMvvm;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.share.Common;
import com.sooft_sales.uis.activity_base.BaseFragment;
import com.sooft_sales.uis.activity_home.HomeActivity;
import com.sooft_sales.uis.activity_invoice.InvoiceActivity;
import com.sooft_sales.uis.activity_return_invoice.ReturnInvoiceActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentCart extends BaseFragment implements DataBaseInterfaces.OrderInsertInterface, DataBaseInterfaces.ProductOrderInsertInterface, DataBaseInterfaces.SearchInterface {
    private FragmentCartBinding binding;
    private HomeActivity activity;
    private FragmentCartMvvm fragmentCartMvvm;
    private CartAdapter cartadpter;
    private List<ItemCartModel> list;
    private CreateOrderModel createOrderModel;
    private double total, tax, discount;
    private boolean isDataChanged = false;
    private Preferences preferences;
    private UserModel userModel;
    private AccessDatabase accessDatabase;
    private ProgressDialog dialog;
    private int pos;
    private double id;
    private SettingModel settingDataModel;
    private int payment_method;

    public static FragmentCart newInstance() {
        FragmentCart fragment = new FragmentCart();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        list = new ArrayList<>();
        fragmentCartMvvm = ViewModelProviders.of(this).get(FragmentCartMvvm.class);
        preferences = Preferences.getInstance();
        settingDataModel = preferences.getUserAdditionalSettings(activity);
        accessDatabase = new AccessDatabase(activity);
        dialog = Common.createProgressDialog(activity, activity.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        userModel = preferences.getUserData(activity);
        createOrderModel = preferences.getcart_softData(activity);
        cartadpter = new CartAdapter(activity, this);
        binding.recviewcart.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.recviewcart.setAdapter(cartadpter);
        updateUi();
        binding.rb1.setOnClickListener(view ->
        {
            payment_method = 1;


        });
        binding.rb2.setOnClickListener(view ->
        {
            payment_method = 2;


        });
        binding.edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    discount = ((total) * (Double.parseDouble(binding.edDiscount.getText().toString()))) / 100;


                } catch (Exception e) {
                    discount = 0;

                }
                if (createOrderModel != null) {
                    calculateTotal();
                }
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createOrderModel != null) {
                    if (payment_method == 0) {
                        Toast.makeText(activity, getResources().getString(R.string.ch_payment), Toast.LENGTH_SHORT).show();
                    } else {
                        createOrderModel.setPay_type(payment_method);
                        createOrderModel.setOrder_date_time(System.currentTimeMillis());
                        createOrderModel.setIs_back(false);
                        createOrderModel.setLocal(true);
                        double min = 1000;
                        double max = 100000;

                        //Generate random int value from 50 to 100
                        double random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
                        id = random_int;
                        createOrderModel.setId(id);
                        dialog.show();

                        try {
                            accessDatabase.insertOrder(createOrderModel, FragmentCart.this);

                        } catch (Exception e) {

                        }
                    }
                }
            }
        });
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("ordersStore")) {
            binding.nested.setVisibility(View.GONE);
        }
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("productsIndex")) {
            binding.nested.setVisibility(View.GONE);
        }
    }


    private void updateUi() {
        list.clear();
        if (createOrderModel != null) {
            list.addAll(createOrderModel.getDetails());
            cartadpter.updateList(list);
            //cartadpter.notifyDataSetChanged();
            //   Log.e(",kkkk", list.size() + "");
//            binding.llEmptyCart.setVisibility(View.GONE);
            calculateTotal();

        } else {
            cartadpter.updateList(new ArrayList<>());
//            binding.llEmptyCart.setVisibility(View.VISIBLE);
//            binding.fltotal.setVisibility(View.GONE);
            total = 0;
            tax = 0;
            discount = 0;
            binding.tvDiscount.setText(discount + "");
            binding.tvTax.setText(tax + "");
            binding.tvTotal.setText(total + "");
            binding.tvTotal2.setText((total + tax - discount) + "");
            binding.tvtotaltax.setText((total + tax) + "");

        }
    }

    private void calculateTotal() {
        total = 0;
        for (ItemCartModel model : list) {

            total += model.getTotal();
        }
        if (settingDataModel.getTax_method().equals("inclusive")) {
            total = total / (1 + (settingDataModel.getTax_val() / 100));
        }
        tax = (((total - discount) * settingDataModel.getTax_val())) / 100;


        binding.tvDiscount.setText(String.format(Locale.ENGLISH, "%.2f", discount) + "");
        binding.tvTax.setText(String.format(Locale.ENGLISH, "%.2f", tax) + "");
        binding.tvTotal.setText(String.format(Locale.ENGLISH, "%.2f", total) + "");
        binding.tvtotaltax.setText(String.format(Locale.ENGLISH, "%.2f", (total - discount)) + "");
        binding.tvTotal2.setText(String.format(Locale.ENGLISH, "%.2f", (total + tax - discount)) + "");
        createOrderModel.setTotal(total);
        createOrderModel.setTax(tax);
        createOrderModel.setDiscount(discount);

    }


    @Override
    public void onOrderDataInsertedSuccess(boolean bol) {
         Log.e("id", bol + ""+id);
        if (bol ) {
            for (int i = 0; i < list.size(); i++) {
                ItemCartModel itemCartModel = list.get(i);
                itemCartModel.setOrder_id(id);
                list.set(i, itemCartModel);
            }
            createOrderModel.setDetails(list);
            try {
                accessDatabase.insertOrderProduct(createOrderModel.getDetails(), this);

            } catch (Exception e) {

            }

        }
        else{
            accessDatabase.insertOrder(createOrderModel,this);
        }
    }

    @Override
    public void onProductORderDataInsertedSuccess(Boolean bol) {
        Log.e("id", bol + ""+id);

        if (bol) {

            accessDatabase.search(this,id+"");


        } else {
            accessDatabase.insertOrderProduct(createOrderModel.getDetails(), this);

        }
    }


    public void deleteItem(int adapterPosition) {
        list.remove(adapterPosition);
        cartadpter.notifyItemRemoved(adapterPosition);
        createOrderModel.setDetails(list);
        preferences.create_update_cart_soft(activity, createOrderModel);
        isDataChanged = true;
        calculateTotal();
        if (list.size() == 0) {


            preferences.clearcart_soft(activity);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences != null) {
            createOrderModel = preferences.getcart_softData(activity);
            updateUi();
        }
    }

    @Override
    public void onSearchDataSuccess(CreateOrderModel createOrderModel1) {
        if(createOrderModel1!=null){
            dialog.dismiss();

            preferences.clearcart_soft(activity);
            preferences.create_ordernum(activity, preferences.getOrdernum(activity)+1);
            Intent intent = new Intent(activity, InvoiceActivity.class);
            intent.putExtra("data", createOrderModel);
            startActivity(intent);
        }
        else{
            createOrderModel.setIs_back(false);
            createOrderModel.setLocal(true);
            double min = 1000;
            double max = 100000;

            //Generate random int value from 50 to 100
            double random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
            id = random_int;
            createOrderModel.setId(id);
            try {
                accessDatabase.insertOrder(createOrderModel, this);
            }
            catch (Exception e){

            }
        }
    }
}