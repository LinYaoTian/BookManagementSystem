package com.rdc.bms.mvp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
//    @BindView(R.id.iv_change_account_type_act_login)
//    ImageView mIvChangeAccountType;
    @BindView(R.id.et_username_act_login)
    EditText mEtUsername;
    @BindView(R.id.et_password_act_login)
    EditText mEtPassword;
    @BindView(R.id.btn_login_act_login)
    Button mBtnLogin;
    @BindView(R.id.tv_account_type_act_login)
    TextView mTvAccountType;
    @BindView(R.id.tv_setting_act_login)
    TextView mTvSetting;

    private int mAccountType = Constants.ACCOUNT_TYPE_READER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingIp();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA}, 1);
        }
    }

    private void settingIp() {
        final AlertDialog dialog = new AlertDialog.Builder(this).show();
        //设置背景色为透明，解决设置圆角后有白色直角的问题
        Window window=dialog.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View container = LayoutInflater.from(this).inflate(R.layout.dialog_setting_ip,null);
        final EditText input = container.findViewById(R.id.et_setting_ip);
        Button btnOk = container.findViewById(R.id.btn_ok_dialog_setting_ip);
        Button btnCancel = container.findViewById(R.id.btn_cancel_dialog_setting_ip);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.BASE_URL = getString(input);
            }
        });
        dialog.setContentView(container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED){
                        showToast("请打开相应权限！否则无法使用本应用");
                        finish();
                    }
                }
                break;
        }

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
        mTvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingIp();
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
