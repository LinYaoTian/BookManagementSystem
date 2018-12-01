package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.app.App;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.entity.BorrowRecord;
import com.rdc.bms.mvp.contract.IBorrowRecord;
import com.rdc.bms.mvp.fragment.MyBorrowRecordFragment;
import com.rdc.bms.mvp.presenter.BorrowRecordPresenter;

import java.util.List;

import butterknife.BindView;

public class MyBorrowRecordActivity extends BaseActivity<BorrowRecordPresenter> implements IBorrowRecord.View {

    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    MyBorrowRecordFragment mMyBorrowRecordFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_my_borrow_record;
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
        mMyBorrowRecordFragment = MyBorrowRecordFragment.newInstance();
        replaceFragment(mMyBorrowRecordFragment);
        mIvBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("我的订阅");
    }

    @Override
    protected void initToolbar() {

    }

    /**
     * 切换主界面Fragment
     * @param f
     */
    private void replaceFragment(Fragment f){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_act_borrow_record,f);
        transaction.commitNow();
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
        context.startActivity(new Intent(context,MyBorrowRecordActivity.class));
    }

    public void getData(){
        presenter.getMyBorrowRecord(App.getUser().getUserId());
    }

    @Override
    public void getMyBorrowRecordSuccess(List<BorrowRecord> list, boolean canLoadMore) {
        mMyBorrowRecordFragment.setData(list);
        mMyBorrowRecordFragment.setCanLoadMore(canLoadMore);
    }

    @Override
    public void getMyBorrowRecordError(String msg) {
        showToast(msg);
    }
}
