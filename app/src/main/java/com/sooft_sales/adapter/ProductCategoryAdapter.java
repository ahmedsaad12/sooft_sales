package com.sooft_sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sooft_sales.R;
import com.sooft_sales.databinding.CategoryItemRowBinding;
import com.sooft_sales.model.DepartmentModel;
import com.sooft_sales.model.ProductModel;
import com.sooft_sales.uis.activity_product.ProductActivity;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DepartmentModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ProductCategoryAdapter( Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.category_item_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        ((MyHolder) holder).binding.setModel(list.get(position));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ProductActivity productActivity=(ProductActivity) context;
        productActivity.show(list.get(holder.getLayoutPosition()).getId());
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
    public void updateList(List<DepartmentModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryItemRowBinding binding;

        public MyHolder(CategoryItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
