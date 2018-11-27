package com.rdc.bms.listener;


public interface OnClickRecyclerViewListener {
    void onItemClick(int position);

    boolean onItemLongClick(int position);

    abstract void onFooterViewClick();
}
