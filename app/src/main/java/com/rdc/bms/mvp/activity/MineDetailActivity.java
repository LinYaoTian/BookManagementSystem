package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.app.App;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.contract.IMineDetail;
import com.rdc.bms.mvp.presenter.MineDetailPresenter;

import butterknife.BindView;

public class MineDetailActivity extends BaseActivity<MineDetailPresenter> implements IMineDetail.View {

    @BindView(R.id.iv_back_layout_top)
    ImageView mIvFalse;
    @BindView(R.id.iv_true_layout_top)
    ImageView mIvTrue;
    @BindView(R.id.et_name_act_mine_detail)
    EditText mEtName;
    @BindView(R.id.et_major_act_mine_detail)
    EditText mEtMajor;
    @BindView(R.id.et_sex_act_mine_detail)
    EditText mEtSex;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.et_password_act_mine_detail)
    EditText mEtPassword;

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
    protected MineDetailPresenter getInstance() {
        return new MineDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        mTvTitle.setText("个人信息");
        mEtName.setText(App.getUser().getName());
        mEtMajor.setText(App.getUser().getMajor());
        mEtSex.setText(App.getUser().getSex());
        mIvTrue.setVisibility(View.VISIBLE);
        mIvFalse.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {
        mIvFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUserId(App.getUser().getUserId());
                user.setName(getString(mEtName));
                user.setSex(getString(mEtSex));
                user.setMajor(getString(mEtMajor));
                if (!TextUtils.isEmpty(getString(mEtPassword))
                        &&!getString(mEtPassword).equals(App.getUser().getPassword())){
                    user.setPassword(getString(mEtPassword));
                }else {
                    user.setPassword(App.getUser().getPassword());
                }
                presenter.update(user);
            }
        });
    }

    @Override
    public void updateSuccess(String msg) {
        showToast(msg);
        onBackPressed();
    }

    @Override
    public void updateError(String msg) {
        showToast(msg);
    }
}
