package com.rdc.bms.mvp.fragment;


import android.content.Context;
import android.os.Bundle;

import com.rdc.bms.RvCell.BorrowRecordCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.fragment.AbsBaseFragment;
import com.rdc.bms.entity.BorrowRecord;
import com.rdc.bms.mvp.activity.BorrowRecordActivity;

import java.util.ArrayList;
import java.util.List;


public class BorrowRecordFragment extends AbsBaseFragment {

    private BorrowRecordActivity mBorrowRecordActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBorrowRecordActivity = (BorrowRecordActivity) context;
    }

    @Override
    public void onRecyclerViewInitialized() {
        mSwipeRefreshLayout.setEnabled(false);
        mBorrowRecordActivity.getData();
    }

    @Override
    public void onPullRefresh() {

    }

    @Override
    public void onLoadMore() {
        mBorrowRecordActivity.getData();
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
