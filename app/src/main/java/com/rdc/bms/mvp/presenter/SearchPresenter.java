package com.rdc.bms.mvp.presenter;


import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.entity.Book;
import com.rdc.bms.mvp.contract.ISearchContract;
import com.rdc.bms.mvp.model.SearchModel;

import java.util.List;

public class SearchPresenter extends BasePresenter<ISearchContract.View>
        implements ISearchContract.Presenter {

    private ISearchContract.Model mModel;

    public SearchPresenter(){
        mModel = new SearchModel(this);
    }

    @Override
    public void getResultSuccess(List<Book> list,boolean canLoadMore) {
        if (isAttachView()){
            getView().getResultSuccess(list,canLoadMore);
        }
    }

    @Override
    public void getResultError(String msg) {
        if (isAttachView()){
            getView().getResultError(msg);
        }
    }

    @Override
    public void getResult(String key) {
        mModel.getResult(key);
    }
}
