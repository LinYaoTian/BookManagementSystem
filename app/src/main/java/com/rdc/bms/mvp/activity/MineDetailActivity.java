package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;

public class MineDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void actionStart(Context context){
        context.startActivity(new Intent(context,MineDetailActivity.class));
    }


    @Override
    protected int setLayoutResID() {
        return R.layout.activity_mine_detail;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {

    }
}
