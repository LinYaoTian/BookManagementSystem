package com.rdc.bms.RvCell;

import android.graphics.Color;
import android.text.TextUtils;
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
import com.rdc.bms.entity.BorrowRecord;

public class BorrowRecordCell extends BaseRvCell<BorrowRecord> {

    public BorrowRecordCell(BorrowRecord borrowRecord) {
        super(borrowRecord);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.BORROW_RECORD_TYPE;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_borrow_record,parent,false);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        ImageView ivCover = holder.getImageView(R.id.iv_cover_cell_borrow_record);
        Glide.with(ivCover.getContext()).load(mData.getCoverUrl()).into(ivCover);
        TextView mTvTag = holder.getTextView(R.id.tv_tag_cell_borrow_record);
        if (TextUtils.isEmpty(mData.getReturnTime())){
            mTvTag.setText("未还");
            mTvTag.setTextColor(Color.RED);
        }else {
            mTvTag.setText("已还");
            mTvTag.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
        }
        holder.setText(R.id.tv_bookName_cell_borrow_record,mData.getBookName());
        holder.setText(R.id.tv_bookId_cell_borrow_record,"书本ID："+mData.getBookId());
        holder.setText(R.id.tv_userId_cell_borrow_record,"借书者ID："+mData.getUserId());
        holder.setText(R.id.tv_return_time_cell_borrow_record,"还书时间："+mData.getReturnTime());
        holder.setText(R.id.tv_borrow_time_cell_borrow_record,"借书时间："+mData.getBorrowTime());

    }
}
