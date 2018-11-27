package com.rdc.bms.RvCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.ItemType;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.rdc.bms.entity.Book;

import butterknife.ButterKnife;

public class BookCell extends BaseRvCell<Book> {

    private TextView mTvName;
    private TextView mTvAuthor;
    private TextView mTvIntro;
    private ImageView mIvCover;

    private Context mContext;

    public BookCell(Book book) {
        super(book);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.BOOK_TYPE;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_book,parent,false);
        BaseRvViewHolder holder = new BaseRvViewHolder(view);
        ButterKnife.bind(holder,view);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {

        mTvName = holder.getTextView(R.id.tv_name_cell_book);
        mIvCover = holder.getImageView(R.id.iv_cover_cell_book);
        mTvIntro = holder.getTextView(R.id.tv_intro_cell_book);
        mTvAuthor = holder.getTextView(R.id.tv_author_cell_book);

        mTvName.setText(mData.getName());
        mTvAuthor.setText(mData.getAuthor());
        mTvIntro.setText("简介：".concat(mData.getIntro()));
        Glide.with(mIvCover.getContext())
                .load(mData.getCoverUrl())
                .into(mIvCover);

        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(holder, position);
                }
            });
        }

    }
}
