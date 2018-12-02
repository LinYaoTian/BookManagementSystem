package com.rdc.bms.mvp.fragment;

import android.os.Bundle;

import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.bookmanagementsystem.R;

public class TipFragment extends BaseFragment {

    public static TipFragment newInstance() {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int setLayoutResourceId() {
        return R.layout.layout_tip;
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
