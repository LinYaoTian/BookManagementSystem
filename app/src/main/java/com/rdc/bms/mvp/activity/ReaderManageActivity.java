package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.SearchUserDTO;
import com.rdc.bms.dto.SimpleDTO;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.fragment.ReaderManageFragment;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class ReaderManageActivity extends BaseActivity {

    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.iv_option2_layout_top)
    ImageView mIvAddReader;
    @BindView(R.id.tv_cancel_layout_search)
    TextView mTvCancel;
    @BindView(R.id.et_search_layout_search)
    EditText mEtSearch;
    @BindView(R.id.iv_delete_layout_search)
    ImageView mIvDelete;

    private ReaderManageFragment mReaderManageFragment;
    private boolean misNoneData = false;
    private int mAllReaderPage = -1;//显示读者的Page



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_reader_manage;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mReaderManageFragment = ReaderManageFragment.newInstance();
    }

    @Override
    protected void initView() {
        mTvCancel.setVisibility(View.INVISIBLE);
        mTvTitle.setText("读者管理");
        mIvAddReader.setVisibility(View.VISIBLE);
        mIvAddReader.setImageResource(R.drawable.iv_plus);
        mIvBack.setVisibility(View.VISIBLE);
        mEtSearch.setHint("按用户ID搜索读者");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_act_reader_manage,mReaderManageFragment);
        transaction.commitNow();
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {
        mIvAddReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDetailActivity.actionStartForAddReader(ReaderManageActivity.this);
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
                    mReaderManageFragment.showList();
                    isHidedList = false;
                    mIvDelete.setVisibility(View.INVISIBLE);
                }else {
                    if (!isHidedList){
                        mReaderManageFragment.hideList();
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
                        searchReader(getString(mEtSearch));
                    }
                }
                return true;
            }
        });

    }

    public void deleteReader(String userId){
        String url  = Constants.BASE_URL + "users/delete?userId="+userId;
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
                        showToast("删除读者成功！");
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

    public void updateReader(User user){
        String url  = Constants.BASE_URL + "users/update";
        Map<String,String> map = new HashMap<>();
        map.put("userId",user.getUserId());
        map.put("password",user.getPassword());
        map.put("sex",user.getSex());
        map.put("major",user.getMajor());
        map.put("name",user.getName());
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
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
                        showToast("更新读者信息成功！");
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        },map);
    }

    private void searchReader(String userId){
        String url = Constants.BASE_URL + "users/search?userId="+userId;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes)  {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchUserDTO dto = GsonUtil.gsonToBean(s,SearchUserDTO.class);
                    if (dto.isSuccess()){
                        mReaderManageFragment.setSearchResult(dto.getData());
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

    public void searchAllReader(){
        if (misNoneData){
            return;
        }
        mAllReaderPage++;
        String url = Constants.BASE_URL + "users/searchAll?page="+ mAllReaderPage;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {

            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
                if (mAllReaderPage != 0){
                    mAllReaderPage--;
                }
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchUserDTO dto = GsonUtil.gsonToBean(s,SearchUserDTO.class);
                    if (dto.isSuccess()){
                        mReaderManageFragment.hideLoadMore();
                        if (dto.transform().size() < 15){
                            //少于每一页约定的item数，则说明没有更多数据了
                            misNoneData = true;
                            mReaderManageFragment.setCanShowLoadMore(false);
                            showToast(mAllReaderPage ==0 ? "无数据！":"没有更多数据了！");
                        }
                        mReaderManageFragment.appendResult(dto.transform());
                    }else {
                        showToast(dto.getMsg());
                        if (mAllReaderPage != 0){
                            mAllReaderPage--;
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                    if (mAllReaderPage != 0){
                        mAllReaderPage--;
                    }
                }
            }
        });
    }

    public static void actionStart(Context context){
        context.startActivity(new Intent(context,ReaderManageActivity.class));
    }


}
