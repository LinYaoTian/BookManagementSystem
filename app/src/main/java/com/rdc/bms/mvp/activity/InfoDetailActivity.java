package com.rdc.bms.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.app.App;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.contract.IInfoDetail;
import com.rdc.bms.mvp.presenter.InfoDetailPresenter;
import com.rdc.bms.util.GsonUtil;

import butterknife.BindView;

public class InfoDetailActivity extends BaseActivity<InfoDetailPresenter> implements IInfoDetail.View {

    @BindView(R.id.iv_back_layout_top)
    ImageView mIvFalse;
    @BindView(R.id.iv_option2_layout_top)
    ImageView mIvTrue;
    @BindView(R.id.et_name_act_info_detail)
    EditText mEtName;
    @BindView(R.id.et_major_act_info_detail)
    EditText mEtMajor;
    @BindView(R.id.et_sex_act_info_detail)
    EditText mEtSex;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.et_password_act_info_detail)
    EditText mEtPassword;
    @BindView(R.id.et_userId_act_info_detail)
    EditText mEtUserId;
    @BindView(R.id.til_userId_act_mine_detail)
    TextInputLayout  mTilUserId;

    private static User mInComingUser;//传入的User

    private User mResultUser;//修改过数据的User

    private static  boolean isForAddReader = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void actionStart(Context context, User user){
        isForAddReader = false;
        mInComingUser = user;
        context.startActivity(new Intent(context,InfoDetailActivity.class));
    }

    public static void actionStartForResult(Fragment fragment, User user){
        isForAddReader = false;
        mInComingUser = user;
        fragment.startActivityForResult(new Intent(fragment.getActivity(),InfoDetailActivity.class),0);
    }

    public static void actionStartForAddReader(Context context){
        isForAddReader = true;
        mInComingUser = null;
        context.startActivity(new Intent(context,InfoDetailActivity.class));
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_mine_detail;
    }

    @Override
    protected InfoDetailPresenter getInstance() {
        return new InfoDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        if (mInComingUser != null){
            //查看别人的个人信息
            mTvTitle.setText("读者ID:"+ mInComingUser.getName());
            mEtPassword.setText(mInComingUser.getPassword());
            mEtName.setText(mInComingUser.getName());
            mEtMajor.setText(mInComingUser.getMajor());
            mEtSex.setText(mInComingUser.getSex());
        }else if (isForAddReader){
            // 添加读者
            mTvTitle.setText("添加读者");
            mTilUserId.setVisibility(View.VISIBLE);
        }else {
            mTvTitle.setText("我的个人信息");
            mEtName.setText(App.getUser().getName());
            mEtMajor.setText(App.getUser().getMajor());
            mEtSex.setText(App.getUser().getSex());
            mEtPassword.setText(App.getUser().getPassword());
        }
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
                mResultUser = new User();
                mResultUser.setName(getString(mEtName));
                mResultUser.setSex(getString(mEtSex));
                mResultUser.setMajor(getString(mEtMajor));
                if (TextUtils.isEmpty(getString(mEtPassword))){
                    showToast("密码不能为空！");
                    return;
                }else {
                    mResultUser.setPassword(getString(mEtPassword));
                }

                if (mInComingUser == null){
                    //自己
                    mResultUser.setUserId(App.getUser().getUserId());
                    presenter.update(mResultUser);
                }else {
                    //他人
                    mResultUser.setUserId(mInComingUser.getUserId());
                    presenter.update(mResultUser);
                }
                if (isForAddReader){
                    //添加读者
                    if (TextUtils.isEmpty(getString(mEtUserId))){
                        showToast("帐号不能为空！");
                        return;
                    }else {
                        mResultUser.setUserId(getString(mEtUserId));
                    }
                    presenter.add(mResultUser);
                }

            }
        });
    }

    @Override
    public void updateSuccess(String msg) {
        showToast(msg);
        Intent intent = new Intent();
        intent.putExtra("user",GsonUtil.gsonToJson(mResultUser));
        setResult(RESULT_OK,intent);
        onBackPressed();
    }

    @Override
    public void updateError(String msg) {
        showToast(msg);
    }

    @Override
    public void addSuccess(String msg) {
        showToast(msg);
        onBackPressed();
    }

    @Override
    public void addError(String msg) {
        showToast(msg);
    }
}
