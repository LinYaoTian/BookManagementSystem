package com.rdc.bms.mvp.contract;

import com.rdc.bms.entity.Book;

import java.util.List;

public interface ISearchContract {
    interface View{
        void getResultSuccess(List<Book> list,boolean canLoadMore);
        void getResultError(String msg);
    }

    interface Presenter{
        void getResultSuccess(List<Book> list,boolean canLoadMore);
        void getResultError(String msg);
        void getResult(String key);
    }

    interface Model{
        void getResult(String key);
    }
}
