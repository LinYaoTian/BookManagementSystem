package com.rdc.bms.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.BorrowRecordDTO;
import com.rdc.bms.dto.SearchUserDTO;
import com.rdc.bms.dto.SimpleDTO;
import com.rdc.bms.mvp.fragment.BorrowManageFragment;
import com.rdc.bms.mvp.fragment.ReaderManageFragment;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import okhttp3.Call;

public class BorrowManageActivity extends BaseActivity {

    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.iv_option2_layout_top)
    ImageView mIvBorrow;
    @BindView(R.id.iv_option1_layout_top)
    ImageView mIvReturn;
    @BindView(R.id.tv_cancel_layout_search)
    TextView mTvCancel;
    @BindView(R.id.et_search_layout_search)
    EditText mEtSearch;
    @BindView(R.id.iv_delete_layout_search)
    ImageView mIvDelete;

    private BorrowManageFragment mBorrowManageFragment;
    private boolean misNoneData = false;
    private int mAllBorrowPage = -1;//显示读者的Page
    private AlertDialog mBorrowBookDialog;
    private AlertDialog mReturnBookDialog;
    private int mType = 0;//0按读者ID搜索,1为书本ID搜索

    public static void actionStart(Context context){
        context.startActivity(new Intent(context,BorrowManageActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_borrow_manage;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBorrowManageFragment = BorrowManageFragment.newInstance();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        mTvCancel.setText("读者ID");
        mTvTitle.setText("借阅管理");
        mIvBorrow.setVisibility(View.VISIBLE);
        mIvBorrow.setImageResource(R.drawable.iv_borrow);
        mIvReturn.setVisibility(View.VISIBLE);
        mIvReturn.setImageResource(R.drawable.iv_return);
        mIvBack.setVisibility(View.VISIBLE);
        mEtSearch.setHint("书本ID或用户ID搜索");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_act_borrow_manage, mBorrowManageFragment);
        transaction.commitNow();
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        mIvBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBorrowDialog();
            }
        });
        mIvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReturnDialog();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {

            boolean isHidedList = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    mBorrowManageFragment.showList();
                    isHidedList = false;
                    mIvDelete.setVisibility(View.INVISIBLE);
                }else {
                    if (!isHidedList){
                        mBorrowManageFragment.hideList();
                        isHidedList = true;
                    }
                    mIvDelete.setVisibility(View.VISIBLE);
                }
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null
                        && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (!TextUtils.isEmpty(getString(mEtSearch))){
                        searchRecord(getString(mEtSearch),mType);
                    }
                }
                return true;
            }
        });

    }

    /**
     * 搜索借阅记录
     * @param key 关键字
     * @param type  0 按读者ID搜索，1 按书本ID搜索
     */
    private void searchRecord(String key,int type){
        String url = Constants.BASE_URL + "records/search?key="+key+"&type="+type;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes)  {
                try {
                    String s = new String(bytes,"UTF-8");
                    BorrowRecordDTO dto = GsonUtil.gsonToBean(s,BorrowRecordDTO.class);
                    if (dto.isSuccess()){
                        mBorrowManageFragment.setSearchResult(dto.getData());
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }


    public void clearFlag(){
        misNoneData = false;
        mAllBorrowPage = -1;
    }

    /**
     * 选择搜索关键字的类型的Dialog
     */
    private void showSelectDialog(){
        String[] items = {"读者ID","书籍ID"};
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        //读者ID
                                        mTvCancel.setText("读者ID");
                                        mType = 0;
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        //书籍ID
                                        mTvCancel.setText("书本ID");
                                        mType = 1;
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }).setTitle("选择");
        builder.show();
    }


    /**
     * 搜索全部的借阅记录
     */
    public void searchAllRecord(){
        if (misNoneData){
            return;
        }
        mAllBorrowPage++;
        String url = Constants.BASE_URL + "records/allRecord?page="+ mAllBorrowPage;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {

            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
                if (mAllBorrowPage != 0){
                    mAllBorrowPage--;
                }
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    BorrowRecordDTO dto = GsonUtil.gsonToBean(s,BorrowRecordDTO.class);
                    if (dto.isSuccess()){
                        mBorrowManageFragment.hideLoadMore();
                        if (dto.transform().size() < 15){
                            //少于每一页约定的item数，则说明没有更多数据了
                            misNoneData = true;
                            mBorrowManageFragment.setCanShowLoadMore(false);
                            showToast(mAllBorrowPage ==0 ? "无数据！":"没有更多数据了！");
                        }
                        mBorrowManageFragment.appendResult(dto.transform());
                    }else {
                        showToast(dto.getMsg());
                        if (mAllBorrowPage != 0){
                            mAllBorrowPage--;
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                    if (mAllBorrowPage != 0){
                        mAllBorrowPage--;
                    }
                }
            }
        });
    }

    /**
     * 发起借书请求
     * @param userId
     * @param bookId
     */
    private void borrowBookRequest(String userId, String bookId){
        String url  = Constants.BASE_URL + "records/borrow?userId="+userId+"&bookId="+bookId;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SimpleDTO dto = GsonUtil.gsonToBean(s,SimpleDTO.class);
                    if (dto.isSuccess()){
                        showToast("借阅成功！");
                        mBorrowBookDialog.dismiss();
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }

    /**
     * 发起还书请求
     * @param bookId
     */
    private void returnBookRequest(String bookId,String userId){
        String url  = Constants.BASE_URL + "records/return?bookId="+bookId+"&userId="+userId;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SimpleDTO dto = GsonUtil.gsonToBean(s,SimpleDTO.class);
                    if (dto.isSuccess()){
                        showToast("还书成功！");
                        mReturnBookDialog.dismiss();
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }

    /**
     * 显示还书 Dialog
     */
    private void showReturnDialog(){
        mReturnBookDialog = new AlertDialog.Builder(this).show();
        //设置背景色为透明，解决设置圆角后有白色直角的问题
        Window window=mReturnBookDialog.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View container = LayoutInflater.from(this).inflate(R.layout.dialog_borrow,null);
        TextView tvTitle = container.findViewById(R.id.tv_title_dialog_borrow);
        final EditText mEtUserId = container.findViewById(R.id.et_reader_id_dialog_borrow);
        final EditText mEtBookId = container.findViewById(R.id.et_book_id_dialog_borrow);
        tvTitle.setText("还书");
        Button btnOk = container.findViewById(R.id.btn_ok_dialog_borrow);
        Button btnCancel = container.findViewById(R.id.btn_cancel_dialog_borrow);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReturnBookDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBookRequest(mEtBookId.getText().toString(),mEtUserId.getText().toString());
            }
        });
        mReturnBookDialog.setContentView(container);
    }

    /**
     * 显示借书Dialog
     */
    private void showBorrowDialog(){
        mBorrowBookDialog = new AlertDialog.Builder(this).show();
        //设置背景色为透明，解决设置圆角后有白色直角的问题
        Window window=mBorrowBookDialog.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View container = LayoutInflater.from(this).inflate(R.layout.dialog_borrow,null);
        final EditText mEtUserId = container.findViewById(R.id.et_reader_id_dialog_borrow);
        final EditText mEtBookId = container.findViewById(R.id.et_book_id_dialog_borrow);
        TextView tvTitle = container.findViewById(R.id.tv_title_dialog_borrow);
        tvTitle.setText("借书");
        Button btnOk = container.findViewById(R.id.btn_ok_dialog_borrow);
        Button btnCancel = container.findViewById(R.id.btn_cancel_dialog_borrow);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBorrowBookDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowBookRequest(mEtUserId.getText().toString(),mEtBookId.getText().toString());
            }
        });
        mBorrowBookDialog.setContentView(container);
    }


}
