package com.rdc.bms.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rdc.bms.RvCell.BorrowRecordCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.fragment.AbsBaseFragment;
import com.rdc.bms.entity.BorrowRecord;
import com.rdc.bms.mvp.activity.BorrowManageActivity;

import java.util.ArrayList;
import java.util.List;

public class BorrowManageFragment extends AbsBaseFragment {

    private List<BaseRvCell> mData;

    private BorrowManageActivity mBorrowManageActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBorrowManageActivity = (BorrowManageActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mData = new ArrayList<>();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static BorrowManageFragment newInstance() {
        BorrowManageFragment fragment = new BorrowManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        mBorrowManageActivity.searchAllRecord();
    }

    @Override
    public void onPullRefresh() {
        mBaseAdapter.clear();
        mData.clear();
        mBorrowManageActivity.clearFlag();
        mBorrowManageActivity.searchAllRecord();
    }

    @Override
    public void onLoadMore() {
        mBorrowManageActivity.searchAllRecord();
    }



    public void hideList(){
        mData.clear();
        mData.addAll(mBaseAdapter.getData());
        mBaseAdapter.clear();
    }

    public void showList(){
        mBaseAdapter.clear();
        mBaseAdapter.addAll(mData);
        mData.clear();
    }

    public void setSearchResult(List<BorrowRecord> list){
        mBaseAdapter.clear();
        List<BaseRvCell> readerCellList = new ArrayList<>();
        for (BorrowRecord record : list) {
            BorrowRecordCell cell = new BorrowRecordCell(record);
            readerCellList.add(cell);
        }
        mBaseAdapter.addAll(readerCellList);
    }

    public void appendResult(List<BorrowRecord> list){
        setRefreshing(false);
        List<BaseRvCell> readerCellList = new ArrayList<>();
        for (BorrowRecord record : list) {
            BorrowRecordCell cell = new BorrowRecordCell(record);
            readerCellList.add(cell);
        }
        mBaseAdapter.addAll(readerCellList);
    }
}
