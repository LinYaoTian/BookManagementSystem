package com.rdc.bms.RvCell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.ItemType;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;

public class KeyCell extends BaseRvCell<String> {

    public KeyCell(String s) {
        super(s);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.KEY_TYPE;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_key,parent,false);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        holder.setText(R.id.tv_key_cell_key,mData);
    }
}
