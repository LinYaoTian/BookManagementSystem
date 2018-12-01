package com.rdc.bms.mvp.contract;

import com.rdc.bms.entity.BorrowRecord;

import java.util.List;

public interface IBorrowRecord {
    interface View{
        void getMyBorrowRecordSuccess(List<BorrowRecord> list, boolean canLoadMore);
        void getMyBorrowRecordError(String msg);
    }

    interface Presenter{
        void getMyBorrowRecordSuccess(List<BorrowRecord> list, boolean canLoadMore);
        void getMyBorrowRecordError(String msg);
        void getMyBorrowRecord(String key);
    }

    interface Model{
        void getMyBorrowRecord(String key);
    }
}
