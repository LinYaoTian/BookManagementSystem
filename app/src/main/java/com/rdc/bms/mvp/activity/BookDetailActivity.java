package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.entity.Book;

import butterknife.BindView;

public class BookDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_act_book_detail)
    Toolbar mToolbar;
    @BindView(R.id.tv_name_act_book_detail)
    TextView mTvName;
    @BindView(R.id.tv_author_act_book_detail)
    TextView mTvAuthor;
    @BindView(R.id.tv_location_act_book_detail)
    TextView mTvLocation;
    @BindView(R.id.tv_isbn_act_book_detail)
    TextView mTvIsbn;
    @BindView(R.id.tv_publishing_house_act_book_detail)
    TextView mTvPublishingHouse;
    @BindView(R.id.tv_intro_act_book_detail)
    TextView mTvIntro;
    @BindView(R.id.tv_bookId_act_book_detail)
    TextView mTvBookId;
    @BindView(R.id.iv_cover_act_book_detail)
    ImageView mIvCover;

    private static Book mBook;

    public static void actionStart(Context context, @NonNull Book book){
        mBook = book;
        context.startActivity(new Intent(context,BookDetailActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        mTvAuthor.setText(mBook.getAuthor());
        mTvIntro.setText(mBook.getIntro());
        mTvIsbn.setText(mBook.getIsbn());
        mTvName.setText(mBook.getName());
        mTvBookId.setText(mBook.getBookId());
        mTvLocation.setText(mBook.getLocation());
        mTvPublishingHouse.setText(mBook.getPublishingHouse());
        Glide.with(this).load(mBook.getCoverUrl()).into(mIvCover);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void initListener() {

    }
}
