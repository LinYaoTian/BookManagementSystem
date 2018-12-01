package com.rdc.bms.mvp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rdc.bms.app.App;
import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.mvp.activity.MyBorrowRecordActivity;
import com.rdc.bms.mvp.activity.InfoDetailActivity;

import butterknife.BindView;

public class MineFragment extends BaseFragment {

    @BindView(R.id.btn_my_borrow_record_fragment)
    Button mBtnBorrowRecord;
    @BindView(R.id.btn_edit_fragment_mine)
    Button mBtnEdit;
    @BindView(R.id.tv_name_fragment_mine)
    TextView mTvName;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;


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
        mTvTitle.setText("我的");
    }

    @Override
    public void onResume() {
        super.onResume();
        mTvName.setText(App.getUser().getName());
    }

    @Override
    protected void setListener() {
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDetailActivity.actionStart(mBaseActivity,null);
            }
        });
        mBtnBorrowRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBorrowRecordActivity.actionStart(mBaseActivity);
            }
        });
    }
}
