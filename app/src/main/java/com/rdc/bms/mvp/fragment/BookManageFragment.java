package com.rdc.bms.mvp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.rdc.bms.RvCell.BookCell;
import com.rdc.bms.RvCell.ReaderCell;
import com.rdc.bms.app.App;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.rdc.bms.easy_rv_adapter.fragment.AbsBaseFragment;
import com.rdc.bms.entity.Book;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.activity.BookManageActivity;
import com.rdc.bms.mvp.activity.InfoDetailActivity;
import com.rdc.bms.mvp.activity.ReaderManageActivity;
import com.rdc.bms.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BookManageFragment extends AbsBaseFragment {

    private List<BaseRvCell> mData;

    private BookManageActivity mBookManageActivity;

    private CustomPopWindow mCustomPopWindow;

    private int mCurrentPosition;//正在操作的ITEM位置

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBookManageActivity = (BookManageActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mData = new ArrayList<>();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static BookManageFragment newInstance() {
        BookManageFragment fragment = new BookManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        mBookManageActivity.searchAllBook();
    }

    @Override
    public void onPullRefresh() {
        mBaseAdapter.clear();
        mData.clear();
        mBookManageActivity.clearFlag();
        mBookManageActivity.searchAllBook();
    }

    @Override
    public void onLoadMore() {
        mBookManageActivity.searchAllBook();
    }



    public void hideList(){
        mSwipeRefreshLayout.setEnabled(false);
        mData.clear();
        mData.addAll(mBaseAdapter.getData());
        mBaseAdapter.clear();
    }

    public void showList(){
        mSwipeRefreshLayout.setEnabled(true);
        mBaseAdapter.clear();
        mBaseAdapter.addAll(mData);
        mData.clear();
    }

    public void setSearchResult(List<Book> list){
        mBaseAdapter.clear();
        List<BaseRvCell> readerCellList = new ArrayList<>();
        for (final Book book : list) {
            BookCell cell = new BookCell(book);
            cell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onClickItem(BaseRvViewHolder holder, int position) {
                    showMenu(holder.itemView,book,position);
                }
            });
            readerCellList.add(cell);
        }
        mBaseAdapter.addAll(readerCellList);
    }

    public void appendResult(List<Book> list){
        mSwipeRefreshLayout.setRefreshing(false);
        List<BaseRvCell> readerCellList = new ArrayList<>();
        for (final Book book : list) {
            BookCell cell = new BookCell(book);
            cell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onClickItem(BaseRvViewHolder holder, int position) {
                    showMenu(holder.itemView,book,position);
                }
            });
            readerCellList.add(cell);
        }
        mBaseAdapter.addAll(readerCellList);
    }

    private void showMenu(View view, final Book book, final int position){
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_menu,null);
        TextView tvDelete = contentView.findViewById(R.id.btn_delete_layout_menu);
        TextView tv_Update = contentView.findViewById(R.id.btn_update_layout_menu);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBookManageActivity.deleteBook(book.getBookId());
                mCustomPopWindow.dissmiss();
            }
        });
        tv_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = position;
                mBookManageActivity.showAddBookDialog(BookManageActivity.UPDATE_BOOK_OPTION,book);
                mCustomPopWindow.dissmiss();
            }
        });
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(mActivity)
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAsDropDown(view,view.getWidth()/2,-view.getHeight());
    }

    public void updateCurrentOptionalItem(final Book book){
        BookCell cell = new BookCell(book);
        cell.setListener(new OnClickViewRvListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onClickItem(BaseRvViewHolder holder, int position) {
                showMenu(holder.itemView,book,position);
            }
        });
        mBaseAdapter.update(mCurrentPosition,cell);

    }

    public void removeCurrentOptionalItem(){
        mBaseAdapter.remove(mCurrentPosition);
    }
}
