package com.rdc.bms.dto;

import com.rdc.bms.base.BaseDTO;

public class SimpleDTO extends BaseDTO {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public Object transform() {
        return null;
    }
}
