package com.sooft_sales.model;

import java.io.Serializable;
import java.util.List;

public class SingleProductDataModel extends StatusResponse implements Serializable {
    private ProductModel data;

    public ProductModel getData() {
        return data;
    }

}
