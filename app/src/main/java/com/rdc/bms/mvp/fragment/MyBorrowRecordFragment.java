package com.rdc.bms.mvp.fragment;


import android.content.Context;
import android.os.Bundle;

import com.rdc.bms.RvCell.BorrowRecordCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.fragment.AbsBaseFragment;
import com.rdc.bms.entity.BorrowRecord;
import com.rdc.bms.mvp.activity.MyBorrowRecordActivity;

import java.util.ArrayList;
import java.util.List;


public class MyBorrowRecordFragment extends AbsBaseFragment {

    private MyBorrowRecordActivity mMyBorrowRecordActivity;


    public static MyBorrowRecordFragment newInstance() {
        MyBorrowRecordFragment fragment = new MyBorrowRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMyBorrowRecordActivity = (MyBorrowRecordActivity) context;
    }

    @Override
    public void onRecyclerViewInitialized() {
        mSwipeRefreshLayout.setEnabled(false);
        mMyBorrowRecordActivity.getData();
    }

    @Override
    public void onPullRefresh() {

    }

    @Override
    public void onLoadMore() {
        mMyBorrowRecordActivity.getData();
    }

    /**
     * 更新数据
     * @param list 数据
     */
    public void setData(List<BorrowRecord> list){
        hideLoadMore();
        List<BaseRvCell> cellList = new ArrayList<>();
        for (BorrowRecord record : list) {
            BorrowRecordCell cell = new BorrowRecordCell(record);
            cellList.add(cell);
        }
        mBaseAdapter.addAll(cellList);
    }

    public void setCanLoadMore(boolean canLoadMore){
        setCanShowLoadMore(canLoadMore);
    }


}
