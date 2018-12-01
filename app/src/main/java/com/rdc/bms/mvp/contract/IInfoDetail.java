package com.rdc.bms.mvp.contract;

import com.rdc.bms.entity.User;

public interface IInfoDetail {
    interface View{
        void updateSuccess(String msg);
        void updateError(String msg);
        void addSuccess(String msg);
        void addError(String msg);
    }

    interface Presenter{
        void updateSuccess(String msg);
        void updateError(String msg);
        void update(User user);
        void add(User user);
        void addSuccess(String msg);
        void addError(String msg);
    }

    interface Model{
        void update(User user);
        void add(User user);
    }
}
