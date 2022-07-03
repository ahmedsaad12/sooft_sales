package com.sooft_sales.model;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private SettingModel data;

    public SettingModel getData() {
        return data;
    }

    public void setData(SettingModel data) {
        this.data = data;
    }
}
