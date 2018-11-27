package com.rdc.bms.mvp.presenter;


import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.mvp.contract.ILoginContract;
import com.rdc.bms.mvp.model.LoginModel;

public class LoginPresenter extends BasePresenter<ILoginContract.View>
        implements ILoginContract.Presenter {

    private ILoginContract.Model mModel;

    public LoginPresenter(){
        this.mModel = new LoginModel(this);
    }
    @Override
    public void login(String account, String password, int type) {
        mModel.login(account, password, type);
    }

    @Override
    public void loginSuccess() {
        if (isAttachView()){
            getView().loginSuccess();
        }
    }

    @Override
    public void loginError(String msg) {
        if (isAttachView()){
            getView().loginError(msg);
        }
    }
}
