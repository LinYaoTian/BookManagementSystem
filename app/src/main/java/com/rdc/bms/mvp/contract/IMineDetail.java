package com.rdc.bms.mvp.contract;

import com.rdc.bms.entity.User;

public interface IMineDetail {
    interface View{
        void updateSuccess(String msg);
        void updateError(String msg);
    }

    interface Presenter{
        void updateSuccess(String msg);
        void updateError(String msg);
        void update(User user);
    }

    interface Model{
        void update(User user);
    }
}
