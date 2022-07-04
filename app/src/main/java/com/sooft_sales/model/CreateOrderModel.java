package com.sooft_sales.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;


import com.sooft_sales.tags.Tags;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = Tags.table_order,
        indices = {@Index(value = {"id"}, unique = true)}
)
public class CreateOrderModel implements Serializable {
    @PrimaryKey
    private double id;
    private String customer_name;
    private double total;
    private double discount;
    private double tax;
    private long order_date_time;
    private boolean is_back;
    private int pay_type;
    private boolean local;
    @Ignore
    private List<ItemCartModel> details;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public long getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(long order_date_time) {
        this.order_date_time = order_date_time;
    }

    public List<ItemCartModel> getDetails() {
        return details;
    }

    public void setDetails(List<ItemCartModel> details) {
        this.details = details;
    }

    public boolean isIs_back() {
        return is_back;
    }

    public void setIs_back(boolean is_back) {
        this.is_back = is_back;
    }

    public boolean getLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }
}
