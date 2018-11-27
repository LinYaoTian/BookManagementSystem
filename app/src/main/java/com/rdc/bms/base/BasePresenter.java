package com.rdc.bms.base;

public class BasePresenter<V> {

    private V view;

    public void attachView(V v){
        view = v;
    }

    public void detachView(){
        view = null;
    }

    public boolean isAttachView(){
        return view != null;
    }

    public V getView(){
        return view;
    }

}
