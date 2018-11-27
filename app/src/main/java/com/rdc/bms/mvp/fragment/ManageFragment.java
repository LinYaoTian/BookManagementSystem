package com.rdc.bms.mvp.fragment;

import android.os.Bundle;

import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.bookmanagementsystem.R;

public class ManageFragment extends BaseFragment {

    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_manage;
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
