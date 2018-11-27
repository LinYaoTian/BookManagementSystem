package com.rdc.bms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rdc.bms.util.ActivityCollectorUtil;

import butterknife.ButterKnife;


public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity{
    private static final String TAG = "BaseActivity";
    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorUtil.addActivity(this);
        setContentView(setLayoutResID());
        ButterKnife.bind(this);
        presenter = getInstance();
        if (presenter!=null){
            presenter.attachView(this);
        }
        initData(savedInstanceState);
        initToolbar();
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
        }
        ActivityCollectorUtil.removeActivity(this);
        super.onDestroy();
    }

    protected abstract int setLayoutResID();

    protected abstract T getInstance();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initView();

    protected abstract void initToolbar();

    protected abstract void initListener();

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    protected static String getString(EditText et) {
        return et.getText().toString();
    }



}
