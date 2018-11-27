package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.entity.Book;
import com.rdc.bms.mvp.contract.ISearchContract;
import com.rdc.bms.mvp.fragment.SearchDetailFragment;
import com.rdc.bms.mvp.presenter.SearchPresenter;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchContract.View {

    private static String mSearchKey;
    private SearchDetailFragment mSearchDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mSearchKey != null){
            presenter.getResult(mSearchKey);
        }
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_search;
    }

    public static void actionStart(Context context,@Nullable String key){
        mSearchKey = key;
        context.startActivity(new Intent(context,SearchActivity.class));
    }

    @Override
    protected SearchPresenter getInstance() {
        return new SearchPresenter();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key",mSearchKey);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mSearchKey = savedInstanceState.getString("key",null);
        }
    }

    @Override
    protected void initView() {
        mSearchDetailFragment = SearchDetailFragment.newInstance(mSearchKey);
        replaceFragment(mSearchDetailFragment);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {

    }

    /**
     * 切换主界面Fragment
     * @param f
     */
    private void replaceFragment(Fragment f){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layout_container_act_search,f);
        transaction.commitNow();
    }

    public void search(String key){
        presenter.getResult(key);
    }

    @Override
    public void getResultSuccess(List<Book> list,boolean canLoadMore) {
        mSearchDetailFragment.setData(list);
        mSearchDetailFragment.setCanShowLoadMore(canLoadMore);
    }

    @Override
    public void getResultError(String msg) {
        showToast(msg);
    }
}
