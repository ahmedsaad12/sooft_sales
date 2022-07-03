package com.sooft_sales.model;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel extends StatusResponse implements Serializable {
    private List<CreateOrderModel> data;

    public List<CreateOrderModel> getData() {
        return data;
    }

}
