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
import com.rdc.bms.RvCell.ReaderCell;
import com.rdc.bms.app.App;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.rdc.bms.easy_rv_adapter.fragment.AbsBaseFragment;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.activity.InfoDetailActivity;
import com.rdc.bms.mvp.activity.ReaderManageActivity;
import com.rdc.bms.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ReaderManageFragment extends AbsBaseFragment {

    private List<BaseRvCell> mData;

    private ReaderManageActivity mReaderManageActivity;

    private CustomPopWindow mCustomPopWindow;

    private int mCurrentPosition;//正在操作的ITEM位置

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mReaderManageActivity = (ReaderManageActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mData = new ArrayList<>();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static ReaderManageFragment newInstance() {
        ReaderManageFragment fragment = new ReaderManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        mSwipeRefreshLayout.setEnabled(false);
        mReaderManageActivity.searchAllReader();
    }

    @Override
    public void onPullRefresh() {

    }

    @Override
    public void onLoadMore() {
        mReaderManageActivity.searchAllReader();
    }



    public void hideList(){
        mData.clear();
        mData.addAll(mBaseAdapter.getData());
        mBaseAdapter.clear();
    }

    public void showList(){
        mBaseAdapter.clear();
        mBaseAdapter.addAll(mData);
        mData.clear();
    }

    public void setSearchResult(List<User> list){
        mBaseAdapter.clear();
        List<BaseRvCell> readerCellList = new ArrayList<>();
        for (final User user : list) {
            ReaderCell cell = new ReaderCell(user);
            cell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onClickItem(BaseRvViewHolder holder, int position) {
                    showMenu(holder.itemView,user,position);
                }
            });
            readerCellList.add(cell);
        }
        mBaseAdapter.addAll(readerCellList);
    }

    public void appendResult(List<User> list){
        List<BaseRvCell> readerCellList = new ArrayList<>();
        for (final User user : list) {
            ReaderCell cell = new ReaderCell(user);
            cell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onClickItem(BaseRvViewHolder holder, int position) {
                    showMenu(holder.itemView,user,position);
                }
            });
            readerCellList.add(cell);
        }
        mBaseAdapter.addAll(readerCellList);
    }

    private void showMenu(View view, final User user, final int position){
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_menu,null);
        TextView tvDelete = contentView.findViewById(R.id.btn_delete_layout_menu);
        TextView tv_Update = contentView.findViewById(R.id.btn_update_layout_menu);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUserId().equals(App.getUser().getUserId())){
                    Toast.makeText(mReaderManageActivity, "不可以删除自己！", Toast.LENGTH_SHORT).show();
                }else {
                    mReaderManageActivity.deleteReader(user.getUserId());
                    mBaseAdapter.remove(position);
                }
                mCustomPopWindow.dissmiss();
            }
        });
        tv_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = position;
                InfoDetailActivity.actionStartForResult(ReaderManageFragment.this,user);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK){
                    String u = data.getStringExtra("user");
                    User user = GsonUtil.gsonToBean(u,User.class);
                    mBaseAdapter.update(mCurrentPosition,new ReaderCell(user));
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: mdata siz="+mData.size()+",adapter size="+mBaseAdapter.getData().size());
        super.onDestroyView();
    }
}
