package com.sooft_sales.uis.activity_return_invoice;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.sooft_sales.R;
import com.sooft_sales.databinding.ActivityReturnInvoiceBinding;
import com.sooft_sales.local_database.AccessDatabase;
import com.sooft_sales.local_database.DataBaseInterfaces;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.uis.activity_base.BaseActivity;


public class ReturnInvoiceActivity extends BaseActivity implements DataBaseInterfaces.SearchInterface, DataBaseInterfaces.OrderupdateInterface {
    private ActivityReturnInvoiceBinding binding;
    private AccessDatabase accessDatabase;
private Preferences preferences;
    private CreateOrderModel createOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_return_invoice);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();

    }


    private void initView() {
        preferences=Preferences.getInstance();
        accessDatabase = new AccessDatabase(this);
        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    accessDatabase.search(ReturnInvoiceActivity.this, binding.edtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double id = Double.parseDouble(binding.tvInvoice.getText().toString().replaceAll(getResources().getString(R.string.invoice), ""));
                accessDatabase.udateOrder(id + "", ReturnInvoiceActivity.this);
            }
        });
    }


    @Override
    public void onSearchDataSuccess(CreateOrderModel createOrderModel) {

        if (createOrderModel != null && !createOrderModel.isIs_back()) {
            this.createOrderModel=createOrderModel;
            binding.tvInvoice.setText(getResources().getString(R.string.invoice) + " " + createOrderModel.getId());
            binding.nextBtn.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, getResources().getString(R.string.before), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOrderUpdateDataSuccess() {
        binding.tvInvoice.setText("");
        binding.nextBtn.setVisibility(View.GONE);
        try {


            if (!createOrderModel.getLocal()) {
                preferences.create_ordernum(this, preferences.getOrdernum(this) + 1);
            }
        }catch (Exception e){
            preferences.create_ordernum(this, preferences.getOrdernum(this) + 1);

        }
    }
}