package com.rdc.bms.mvp.model;

import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.SearchBookDTO;
import com.rdc.bms.mvp.contract.ISearchContract;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;

import okhttp3.Call;

public class SearchModel implements ISearchContract.Model {

    private ISearchContract.Presenter mPresenter;
    private int mPage;
    private String previousKey;
    private boolean misNoneData;
    private static final int OFFSET = 15;//每一页的item数

    public SearchModel(ISearchContract.Presenter presenter){
        mPresenter = presenter;
        mPage = 0;
        misNoneData = false;
    }

    @Override
    public void getResult(String key) {
        if (key.equals(previousKey)){
            //相同关键字，因此是由上拉加载引发的请求数据
            if (misNoneData){
                //没有更多数据，直接返回
                mPresenter.getResultError("没有更多数据了！");
                return;
            }
            mPage++;
        }else {
            //新关键字
            previousKey = key;
            mPage=0;
            misNoneData = false;
        }
        String url = Constants.BASE_URL + "books/search?key="+key+"&page="+mPage;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mPresenter.getResultError(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchBookDTO searchBookDTO = GsonUtil.gsonToBean(s,SearchBookDTO.class);
                    if (searchBookDTO.transform().size() < OFFSET){
                        //少于每一页约定的item数，则说明没有更多数据了
                        misNoneData = true;
                        if (mPage != 0){
                            //数据不能填满第一页，不提醒
                            mPresenter.getResultError("没有更多数据了！");
                        }else {
                            mPresenter.getResultError("无数据！");
                        }
                    }
                    mPresenter.getResultSuccess(searchBookDTO.transform(),!misNoneData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mPresenter.getResultError(e.getMessage());
                }
            }
        });
    }
}
