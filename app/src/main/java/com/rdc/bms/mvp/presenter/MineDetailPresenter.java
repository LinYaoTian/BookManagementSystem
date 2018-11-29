package com.rdc.bms.mvp.presenter;

import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.contract.IMineDetail;
import com.rdc.bms.mvp.model.MineDetailModel;

public class MineDetailPresenter extends BasePresenter<IMineDetail.View>
        implements IMineDetail.Presenter {

    private IMineDetail.Model mModel;

    public MineDetailPresenter(){
        mModel = new MineDetailModel(this);
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
}
