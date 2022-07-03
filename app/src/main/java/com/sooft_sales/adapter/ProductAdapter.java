package com.sooft_sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sooft_sales.R;
import com.sooft_sales.databinding.ProductItemRowBinding;
import com.sooft_sales.model.ProductModel;
import com.sooft_sales.uis.activity_product.ProductActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ProductAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.product_item_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        ((MyHolder) holder).binding.setModel(list.get(position));
        myHolder.binding.llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductActivity productActivity=(ProductActivity)context;
                productActivity.CreateDialogAlertProfile(context,list.get(holder.getLayoutPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void updateList(List<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ProductItemRowBinding binding;

        public MyHolder(ProductItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
