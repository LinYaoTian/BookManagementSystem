package com.rdc.bms.mvp.presenter;

import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.entity.BorrowRecord;
import com.rdc.bms.mvp.contract.IBorrowRecord;
import com.rdc.bms.mvp.model.BorrowRecordModel;

import java.util.List;

public class BorrowRecordPresenter extends BasePresenter<IBorrowRecord.View> implements IBorrowRecord.Presenter {

    private IBorrowRecord.Model mModel;

    public BorrowRecordPresenter(){
        mModel = new BorrowRecordModel(this);
    }

    @Override
    public void getResultSuccess(List<BorrowRecord> list, boolean canLoadMore) {
        if (isAttachView()){
            getView().getResultSuccess(list, canLoadMore);
        }
    }

    @Override
    public void getResultError(String msg) {
        if (isAttachView()){
            getView().getResultError(msg);
        }
    }

    @Override
    public void getResult(String userId) {
        mModel.getResult(userId);
    }
}
