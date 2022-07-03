package com.sooft_sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.sooft_sales.R;
import com.sooft_sales.databinding.BillItemRowBinding;
import com.sooft_sales.model.ItemCartModel;

import java.util.List;

public class ProductBillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemCartModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int pos = -1;

    //private Fragment_Main fragment_main;
    public ProductBillAdapter(List<ItemCartModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        BillItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.bill_item_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
//if(position==list.size()-1){
//    myHolder.binding.view.setVisibility(View.GONE);
//}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public BillItemRowBinding binding;

        public MyHolder(@NonNull BillItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
