package com.rdc.bms.mvp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.Constants;
import com.rdc.bms.mvp.contract.ILoginContract;
import com.rdc.bms.mvp.presenter.LoginPresenter;

import butterknife.BindView;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.View {

    @BindView(R.id.toolbar_act_login)
    Toolbar mToolbar;
    @BindView(R.id.iv_change_account_type_act_login)
    ImageView mIvChangeAccountType;
    @BindView(R.id.et_username_act_login)
    EditText mEtUsername;
    @BindView(R.id.et_password_act_login)
    EditText mEtPassword;
    @BindView(R.id.btn_login_act_login)
    Button mBtnLogin;
    @BindView(R.id.tv_account_type_act_login)
    TextView mTvAccountType;

    private int mAccountType = Constants.ACCOUNT_TYPE_READER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getInstance() {
        return new LoginPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        mEtUsername.setText("1");
        mEtPassword.setText("123456");
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setTitle("");
    }

    @Override
    protected void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(getString(mEtUsername))){
                    showToast("帐号不能为空！");
                }else if(TextUtils.isEmpty(getString(mEtPassword))){
                    showToast("密码不能为空！");
                }else {
                    presenter.login(getString(mEtUsername),getString(mEtPassword),mAccountType);
                }
            }
        });
        mIvChangeAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAccountType == Constants.ACCOUNT_TYPE_READER){
                    mAccountType = Constants.ACCOUNT_TYPE_MANAGER;
                    mTvAccountType.setText(R.string.manager);
                }else {
                    mAccountType = Constants.ACCOUNT_TYPE_READER;
                    mTvAccountType.setText(R.string.reader);
                }
                showToast("登录类型->"+mTvAccountType.getText().toString());
            }
        });
    }

    @Override
    public void loginSuccess() {
        MainActivity.actionStart(this);
    }

    @Override
    public void loginError(String msg) {
        showToast(msg);
    }
}
