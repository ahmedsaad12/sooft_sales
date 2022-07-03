package com.sooft_sales.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import com.sooft_sales.tags.Tags;

import java.io.Serializable;

@Entity(tableName = Tags.table_order_products,
        foreignKeys = {
                @ForeignKey(entity = CreateOrderModel.class, parentColumns = "id", childColumns = "order_id", onDelete = CASCADE, onUpdate = CASCADE),
                @ForeignKey(entity = ProductModel.class, parentColumns = "id", childColumns = "product_id", onDelete = CASCADE, onUpdate = CASCADE),



        }

)
public class ItemCartModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int localid;
    private double order_id;
    private double product_id;
    private double price;
    private int qty;
    private double total;
    private String name;

    public int getLocalid() {
        return localid;
    }

    public void setLocalid(int localid) {
        this.localid = localid;
    }

    public double getOrder_id() {
        return order_id;
    }

    public void setOrder_id(double order_id) {
        this.order_id = order_id;
    }

    public double getProduct_id() {
        return product_id;
    }

    public void setProduct_id(double product_id) {
        this.product_id = product_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
