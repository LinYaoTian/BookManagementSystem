package com.rdc.bms.mvp.model;

import android.util.Log;

import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.BorrowRecordDTO;
import com.rdc.bms.mvp.contract.IBorrowRecord;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;

import okhttp3.Call;

public class BorrowRecordModel implements IBorrowRecord.Model {

    private IBorrowRecord.Presenter mPresenter;

    private int mPage;
    private boolean misNoneData;
    private static final int OFFSET = 15;//每一页的item数

    public BorrowRecordModel(IBorrowRecord.Presenter presenter){
        mPresenter = presenter;
        mPage = 0;
        misNoneData = false;
    }

    @Override
    public void getMyBorrowRecord(String userId) {
        if (misNoneData){
            //没有更多数据，直接返回
            mPresenter.getMyBorrowRecordError("没有更多数据了！");
            return;
        }else {
            mPage = 0;
        }
        String url = Constants.BASE_URL + "records/myRecord?page="+mPage;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mPresenter.getMyBorrowRecordError(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    BorrowRecordDTO dto = GsonUtil.gsonToBean(s,BorrowRecordDTO.class);
                    if (dto.isSuccess()){
                        if (dto.transform().size() < OFFSET){
                            //少于每一页约定的item数，则说明没有更多数据了
                            misNoneData = true;
                            mPresenter.getMyBorrowRecordError(mPage==0 ? "无数据！":"没有更多数据了！");
                        }
                        mPresenter.getMyBorrowRecordSuccess(dto.transform(),!misNoneData);
                    }else {
                        mPresenter.getMyBorrowRecordError(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mPresenter.getMyBorrowRecordError(e.getMessage());
                }
            }
        });
    }
}
