package com.rdc.bms.dto;

import com.rdc.bms.base.BaseDTO;
import com.rdc.bms.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SearchUserDTO extends BaseDTO<List<User>> {

    private List<User> data;

    public List<User> getData() {
        return data == null ? new ArrayList<User>():data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    @Override
    public List<User> transform() {
        if (isSuccess()){
            return getData();
        }
        return null;
    }
}
