package com.rdc.bms.easy_rv_adapter.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRvAdapter<C extends BaseRvCell> extends RecyclerView.Adapter<BaseRvViewHolder> {

    public static final String TAG = "BaseRvAdapter";

    protected List<C> mData;

    public BaseRvAdapter(){
        mData = new ArrayList<>();
    }

    public List<C> getData() {
        return mData;
    }

    @NonNull
    @Override
    public BaseRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        C c;
        for (int i = 0; i < getItemCount(); i++) {
            c = mData.get(i);
            if (viewType == c.getItemType()){
                return c.onCreateViewHolder(parent, viewType);
            }
        }
        throw new RuntimeException("Wrong ViewType !");
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRvViewHolder holder, int position) {
        mData.get(position).onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseRvViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.e(TAG, "onViewDetachedFromWindow: invoke...");
        //释放资源
        int position = holder.getAdapterPosition();
        //检查边界
        if (position < 0 || position >= mData.size()){
            return;
        }
        mData.get(position).releaseResource();
    }

    /**
     * set data to adapter(it will clean original data)
     * @param data
     */
    public void setData(List<C> data) {
        mData.clear();
        addAll(data);
        notifyDataSetChanged();
    }


    /**
     * add one cell
     * @param cell
     */
    public void add(C cell){
        mData.add(cell);
        int index = mData.indexOf(cell);
        notifyItemChanged(index);
    }

    public void add(int index,C cell){
        mData.add(index,cell);
        notifyItemChanged(index);
    }

    /**
     * remove a cell
     * @param cell
     */
    public void remove(C cell){
        int indexOfCell = mData.indexOf(cell);
        remove(indexOfCell);
    }

    public void remove(int index){
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     *
     * @param start
     * @param count
     */
    public void remove(int start,int count){
        if((start +count) > mData.size()){
            Log.e(TAG, "remove: 数组越界！");
            return;
        }
        mData.subList(start,start+count).clear();
        notifyItemRangeRemoved(start,count);
    }

    /**
     * add a cell list
     * @param cells
     */
    public void addAll(List<C> cells){
        if(cells == null || cells.size() == 0){
            return;
        }
        Log.e(TAG,"addAll cell size:"+cells.size());
        mData.addAll(cells);
        notifyItemRangeChanged(mData.size()-cells.size(),mData.size());
    }

    public void addAll(int index,List<C> cells){
        if(cells == null || cells.size() == 0){
            return;
        }
        mData.addAll(index,cells);
        notifyItemRangeChanged(index,index+cells.size());
    }

    /**
     * 清除数据和状态
     */
    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }


    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     * @param holder
     * @param position
     */
    protected abstract void onViewHolderBound(BaseRvViewHolder holder, int position);

}
