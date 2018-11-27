package com.rdc.bms.easy_rv_adapter.base;


import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;

public abstract class BaseRvCell<T> implements Cell {

    public T mData;

    protected OnClickViewRvListener mListener;

    /**
     * 添加监听事件
     * @param mListener
     */
    public void setListener(OnClickViewRvListener mListener) {
        this.mListener = mListener;
    }

    public BaseRvCell(T t){
        mData = t;
    }
}
