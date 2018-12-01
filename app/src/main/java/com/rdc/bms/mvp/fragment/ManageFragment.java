package com.rdc.bms.mvp.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.SimpleDTO;
import com.rdc.bms.mvp.activity.BookManageActivity;
import com.rdc.bms.mvp.activity.BorrowManageActivity;
import com.rdc.bms.mvp.activity.ReaderManageActivity;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import okhttp3.Call;

public class ManageFragment extends BaseFragment {

    @BindView(R.id.btn_book_fragment_manage)
    Button mBtnBookManage;
    @BindView(R.id.btn_reader_fragment_manage)
    Button mBtnReaderManage;
    @BindView(R.id.btn_borrow_fragment_manage)
    Button mBtnBorrowManage;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;

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
        mTvTitle.setText("管理");
    }

    @Override
    protected void setListener() {
        mBtnBookManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookManageActivity.actionStart(mBaseActivity);
            }
        });
        mBtnBorrowManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BorrowManageActivity.actionStart(mBaseActivity);
            }
        });
        mBtnReaderManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReaderManageActivity.actionStart(mBaseActivity);
            }
        });
    }





}
