package com.sooft_sales.model;

import java.io.Serializable;

public class UserSettingsModel implements Serializable {
  private boolean is_first;

    public boolean isIs_first() {
        return is_first;
    }

    public void setIs_first(boolean is_first) {
        this.is_first = is_first;
    }
}
