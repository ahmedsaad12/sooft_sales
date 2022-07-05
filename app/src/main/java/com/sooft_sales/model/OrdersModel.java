package com.sooft_sales.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class OrdersModel {
    @Embedded
   private CreateOrderModel createOrderModel;
    @Relation(entity = ItemCartModel.class,parentColumn = "id",entityColumn = "order_id")
    private List<ItemCartModel> itemCartModelList;

    public CreateOrderModel getCreateOrderModel() {
        return createOrderModel;
    }

    public void setCreateOrderModel(CreateOrderModel createOrderModel) {
        this.createOrderModel = createOrderModel;
    }

    public List<ItemCartModel> getItemCartModelList() {
        return itemCartModelList;
    }

    public void setItemCartModelList(List<ItemCartModel> itemCartModelList) {
        this.itemCartModelList = itemCartModelList;
    }
}
