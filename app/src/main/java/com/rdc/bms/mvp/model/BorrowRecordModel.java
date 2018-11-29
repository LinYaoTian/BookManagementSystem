package com.rdc.bms.mvp.model;

import com.rdc.bms.mvp.contract.IBorrowRecord;

public class BorrowRecordModel implements IBorrowRecord.Model {

    private IBorrowRecord.Presenter mPresenter;

    public BorrowRecordModel(IBorrowRecord.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void getResult(String key) {

    }
}
