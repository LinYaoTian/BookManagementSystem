package com.rdc.bms.mvp.presenter;

import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.contract.IInfoDetail;
import com.rdc.bms.mvp.model.InfoModel;

public class InfoDetailPresenter extends BasePresenter<IInfoDetail.View>
        implements IInfoDetail.Presenter {

    private IInfoDetail.Model mModel;

    public InfoDetailPresenter(){
        mModel = new InfoModel(this);
    }

    @Override
    public void updateSuccess(String msg) {
        if (isAttachView()){
            getView().updateSuccess(msg);
        }
    }

    @Override
    public void updateError(String msg) {
        if (isAttachView()){
            getView().updateError(msg);
        }
    }

    @Override
    public void update(User user) {
        mModel.update(user);
    }

    @Override
    public void add(User user) {
        mModel.add(user);
    }

    @Override
    public void addSuccess(String msg) {
        if (isAttachView()){
            getView().addSuccess(msg);
        }
    }

    @Override
    public void addError(String msg) {
        if (isAttachView()){
            getView().addError(msg);
        }
    }
}
