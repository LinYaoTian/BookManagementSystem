package com.rdc.bms.mvp.contract;

public interface ILoginContract {
    interface View{
        void loginSuccess();
        void loginError(String msg);
    }

    interface Model{
        void login(String account,String password,int type);
    }

    interface Presenter{
        void login(String account,String password,int type);
        void loginSuccess();
        void loginError(String msg);
    }
}
