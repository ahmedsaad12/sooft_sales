package com.sooft_sales.uis.activity_product;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.sooft_sales.R;
import com.sooft_sales.adapter.ProductAdapter;
import com.sooft_sales.adapter.ProductCategoryAdapter;
import com.sooft_sales.databinding.ActivityProductBinding;
import com.sooft_sales.databinding.DialogAlertBinding;
import com.sooft_sales.local_database.AccessDatabase;
import com.sooft_sales.local_database.DataBaseInterfaces;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.DepartmentModel;
import com.sooft_sales.model.ItemCartModel;
import com.sooft_sales.model.ProductModel;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.uis.activity_add_product.AddProductActivity;
import com.sooft_sales.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends BaseActivity implements DataBaseInterfaces.ProductInterface, DataBaseInterfaces.CategoryInterface {
    private ActivityProductBinding binding;
    private ProductCategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private AccessDatabase accessDatabase;
    private ActivityResultLauncher<Intent> launcher;
    private Preferences preferences;
    private SettingModel settingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        settingModel = preferences.getUserAdditionalSettings(this);
        accessDatabase = new AccessDatabase(this);

        categoryAdapter = new ProductCategoryAdapter(this);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerCategory.setAdapter(categoryAdapter);

        productAdapter = new ProductAdapter(this);
        binding.recyclerProduct.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerProduct.setAdapter(productAdapter);
        try {
            accessDatabase.getCategory(this);

        } catch (Exception e) {

        }
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                accessDatabase.getCategory(this);

            } catch (Exception e) {

            }
        });
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                launcher.launch(intent);
            }
        });
        binding.bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateOrderModel add_order_model = preferences.getcart_softData(ProductActivity.this);
                if (add_order_model != null) {
                    add_order_model.setCustomer_name(binding.edCustomer.getText().toString());
                    binding.edCustomer.getText().toString();
                    if (binding.edCustomer.getText().toString().isEmpty()) {
                        add_order_model.setCustomer_name("عميل افتراضى");
                    }
                    preferences.create_update_cart_soft(ProductActivity.this, add_order_model);


                }
                setResult(RESULT_OK);
                finish();
            }
        });
        checkpermisions();
    }

    private void checkpermisions() {
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("productCreate")) {
            binding.llMap.setVisibility(View.GONE);
        }
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("ordersStore")) {
            binding.bill.setVisibility(View.GONE);
        }
    }

    public void CreateDialogAlertProfile(Context context, ProductModel productModel) {
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("ordersStore")) {
            Toast.makeText(this, R.string.per, Toast.LENGTH_LONG).show();
        } else {
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .create();
            String price1;

                price1 = productModel.getPrice() + "";

            DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_alert, null, false);
            if (productModel.getPrice() > 0) {
                binding.edtprice.setText(price1);
            }

            binding.btnAdd.setOnClickListener(v -> {
                        String amount = binding.edtAmount.getText().toString();
                        String price = binding.edtprice.getText().toString();
                        if (!amount.isEmpty() && !price.isEmpty()) {
                            binding.edtAmount.setError(null);
                            binding.edtprice.setError(null);
                            addtocart(productModel, Integer.parseInt(amount), Double.parseDouble(price));
                            dialog.dismiss();

                        } else {
                            if (amount.isEmpty()) {
                                binding.edtAmount.setError(context.getResources().getString(R.string.field_required));
                            } else {
                                binding.edtAmount.setError(null);
                            }
                            if (price.isEmpty()) {
                                binding.edtprice.setError(context.getResources().getString(R.string.field_required));
                            } else {
                                binding.edtprice.setError(null);
                            }
                        }
                    }

            );
            dialog.setCanceledOnTouchOutside(false);
            dialog.setView(binding.getRoot());
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        CreateOrderModel add_order_model = preferences.getcart_softData(ProductActivity.this);
        if (add_order_model != null) {
            add_order_model.setCustomer_name(binding.edCustomer.getText().toString());
            binding.edCustomer.getText().toString();
            if (binding.edCustomer.getText().toString().isEmpty()) {
                add_order_model.setCustomer_name("عميل افتراضى");
            }
            preferences.create_update_cart_soft(ProductActivity.this, add_order_model);


        }
        finish();

    }

    public void addtocart(ProductModel productModel, int amount, double price) {
        List<ItemCartModel> productDetailsList;
        CreateOrderModel add_order_model = preferences.getcart_softData(ProductActivity.this);
        if (add_order_model != null) {
            productDetailsList = add_order_model.getDetails();
            if (productDetailsList == null) {
                productDetailsList = new ArrayList<>();
            }
        } else {
            add_order_model = new CreateOrderModel();
            productDetailsList = new ArrayList<>();
        }
        add_order_model.setCustomer_name(binding.edCustomer.getText().toString());
        ItemCartModel productDetails = new ItemCartModel();
        productDetails.setQty(amount);
        productDetails.setProduct_id(productModel.getId());
        productDetails.setPrice(price);
        productDetails.setTotal(amount * price);
        productDetails.setName(productModel.getTitle());
        productDetailsList.add(productDetails);
        add_order_model.setDetails(productDetailsList);
        Toast.makeText(this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
        preferences.create_update_cart_soft(ProductActivity.this, add_order_model);

    }

    @Override
    public void onCategoryDataSuccess(List<DepartmentModel> categoryModelList) {
        categoryAdapter.updateList(categoryModelList);
        if (categoryModelList.size() > 0) {
            try {
                accessDatabase.getProduct(this, categoryModelList.get(0).getId() + "");

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onProductDataSuccess(List<ProductModel> productModelList) {
        if (productModelList.size() > 0) {
            productAdapter.updateList(productModelList);
            binding.frame.setVisibility(View.VISIBLE);
        } else {
            binding.frame.setVisibility(View.GONE);
        }
    }

    public void show(int id) {
        try {
            accessDatabase.getProduct(this, id + "");

        } catch (Exception e) {

        }
    }
}
