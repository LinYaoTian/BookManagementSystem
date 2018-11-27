package com.rdc.bms.dto;

import com.rdc.bms.base.BaseDTO;
import com.rdc.bms.entity.User;

public class LoginDTO extends BaseDTO<User> {

    private User data;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public User transform() {
        return data;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
