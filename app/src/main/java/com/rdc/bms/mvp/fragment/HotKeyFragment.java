package com.rdc.bms.mvp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.mvp.activity.SearchActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class HotKeyFragment extends BaseFragment {

    @BindView(R.id.iv_delete_fragment_search)
    ImageView mIvDelete;
    @BindView(R.id.et_search_fragment_search)
    EditText mEtSearch;
    @BindView(R.id.tagFlowLayout_fragment_search)
    TagFlowLayout mFlowLayout;

    private List<String> list = new ArrayList<>();

    public static HotKeyFragment newInstance() {
        HotKeyFragment fragment = new HotKeyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initData(Bundle bundle) {
        String[] strings = {"Python","科技",
                "科学","数学",
                "计算机","心理",
                "旅行","的","人","哲学","中国","西方"};
        list.addAll(Arrays.asList(strings));
        Log.d("SF2", "initData: "+list.size());
    }

    @Override
    protected void initView() {
        mFlowLayout.setAdapter(new TagAdapter<String>(list) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mRootView.getContext()).inflate(R.layout.cell_key,mFlowLayout,false);
                tv.setText(s);
                return tv;
            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListener() {
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                SearchActivity.actionStart(mBaseActivity,list.get(position));
                return false;
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });
        mEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.actionStart(mBaseActivity,null);
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    mIvDelete.setVisibility(View.VISIBLE);
                }else {
                    mIvDelete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
