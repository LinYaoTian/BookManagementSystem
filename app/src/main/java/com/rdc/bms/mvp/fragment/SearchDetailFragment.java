package com.rdc.bms.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rdc.bms.RvCell.BookCell;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.rdc.bms.easy_rv_adapter.fragment.AbsBaseFragment;
import com.rdc.bms.entity.Book;
import com.rdc.bms.mvp.activity.BookDetailActivity;
import com.rdc.bms.mvp.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchDetailFragment extends AbsBaseFragment {

    public static final String TAG = "SearchDetailFragment";

    private ImageView mIvDelete;
    private EditText mEtSearch;
    private TextView mTvCancel;

    private SearchActivity mSearchActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSearchActivity = (SearchActivity) context;
    }

    public SearchDetailFragment() {
        // Required empty public constructor
    }

    public static SearchDetailFragment newInstance(String key) {
        SearchDetailFragment fragment = new SearchDetailFragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onRecyclerViewInitialized() {
        mSwipeRefreshLayout.setEnabled(false);
        if (getArguments() != null){
            mEtSearch.setText(getArguments().getString("key",""));
        }

    }

    private void loadData() {
        List<BaseRvCell> cellList = new ArrayList<>();
        Book book = new Book();
        book.setAuthor("林耀填");
        book.setIntro("简介:没什么哈哈哈哈哈");
        book.setCoverUrl("http://192.168.1.103:8000/static/book_imge_1.jpg");
        book.setName("浮士德");
        for (int i = 0; i < 5; i++) {
            BookCell bookCell = new BookCell(book);
            cellList.add(bookCell);
        }
        mBaseAdapter.addAll(cellList);
    }

    @Override
    public void onPullRefresh() {
        //下拉刷新回调
    }

    @Override
    public void onLoadMore() {
        //上拉加载回调
        mSearchActivity.search(mEtSearch.getText().toString());
    }

    @Override
    public View addTopLayout() {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.layout_search,null);
        mIvDelete = layout.findViewById(R.id.iv_delete_layout_search);
        mEtSearch = layout.findViewById(R.id.et_search_layout_search);
        mTvCancel = layout.findViewById(R.id.tv_cancel_layout_search);

        mIvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
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
                mBaseAdapter.clear();
                if (!TextUtils.isEmpty(s)){
                    mIvDelete.setVisibility(View.VISIBLE);
                }else {
                    mIvDelete.setVisibility(View.INVISIBLE);
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
                    mSearchActivity.search(mEtSearch.getText().toString());
                }
                return true;
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        return layout;
    }



    /**
     * 更新数据
     * @param list 数据
     */
    public void setData(List<Book> list){
        hideLoadMore();
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final Book book : list) {
            BookCell bookCell = new BookCell(book);
            bookCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onClickItem(BaseRvViewHolder holder, int position) {
                    BookDetailActivity.actionStart(mSearchActivity,book);
                }
            });
            cellList.add(bookCell);
        }
        mBaseAdapter.addAll(cellList);
    }

    public void setCanLoadMore(boolean canLoadMore){
        setCanShowLoadMore(canLoadMore);
    }

    public void setKey(String ket){
        mEtSearch.setText(ket);
    }
}
