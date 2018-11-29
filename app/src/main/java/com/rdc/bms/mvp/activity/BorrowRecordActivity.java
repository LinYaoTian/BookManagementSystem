package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rdc.bms.app.App;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.entity.BorrowRecord;
import com.rdc.bms.mvp.contract.IBorrowRecord;
import com.rdc.bms.mvp.fragment.BorrowRecordFragment;
import com.rdc.bms.mvp.presenter.BorrowRecordPresenter;

import java.util.List;

import butterknife.BindView;

public class BorrowRecordActivity extends BaseActivity<BorrowRecordPresenter> implements IBorrowRecord.View {

    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.borrowRecordFragment_act_borrow_record)
    BorrowRecordFragment mBorrowRecordFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_borrow_record;
    }

    @Override
    protected BorrowRecordPresenter getInstance() {
        return new BorrowRecordPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        mIvBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public static void actionStart(Context context){
        context.startActivity(new Intent(context,BorrowRecordActivity.class));
    }

    public void getData(){
        presenter.getResult(App.getUser().getUserId());
    }

    @Override
    public void getResultSuccess(List<BorrowRecord> list, boolean canLoadMore) {

    }

    @Override
    public void getResultError(String msg) {

    }
}
