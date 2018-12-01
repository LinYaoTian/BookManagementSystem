package com.rdc.bms.RvCell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.ItemType;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.rdc.bms.entity.User;

public class ReaderCell extends BaseRvCell<User> {
    public ReaderCell(User user) {
        super(user);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.READER_TYPE;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_reader,parent,false);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        holder.setText(R.id.tv_reader_name_cell_reader,"姓名："+mData.getName());
        holder.setText(R.id.tv_reader_id_cell_reader,"读者ID："+mData.getUserId());
        holder.setText(R.id.tv_reader_major_cell_reader,"专业："+mData.getMajor());
        holder.setText(R.id.tv_reader_sex_cell_reader,"性别："+mData.getSex());
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(holder, position);
                }
            });
        }
    }
}
