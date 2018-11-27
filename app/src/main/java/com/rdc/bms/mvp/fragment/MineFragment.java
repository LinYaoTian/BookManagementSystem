package com.rdc.bms.mvp.fragment;

import android.os.Bundle;

import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.bookmanagementsystem.R;

public class MineFragment extends BaseFragment {


    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }
}
