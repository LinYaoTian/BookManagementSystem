package com.rdc.bms.mvp.contract;

import com.rdc.bms.entity.BorrowRecord;

import java.util.List;

public interface IBorrowRecord {
    interface View{
        void getResultSuccess(List<BorrowRecord> list, boolean canLoadMore);
        void getResultError(String msg);
    }

    interface Presenter{
        void getResultSuccess(List<BorrowRecord> list,boolean canLoadMore);
        void getResultError(String msg);
        void getResult(String key);
    }

    interface Model{
        void getResult(String key);
    }
}
