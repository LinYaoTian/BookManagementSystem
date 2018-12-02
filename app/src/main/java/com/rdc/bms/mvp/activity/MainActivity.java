package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.rdc.bms.app.App;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BaseFragment;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.Constants;
import com.rdc.bms.mvp.fragment.ManageFragment;
import com.rdc.bms.mvp.fragment.MineFragment;
import com.rdc.bms.mvp.fragment.HotKeyFragment;
import com.rdc.bms.mvp.fragment.TipFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.layout_bottom_act_main)
    BottomNavigationView mBnvTab;
    @BindView(R.id.viewpager_act_main)
    ViewPager mVpContainer;

    private HotKeyFragment mHotKeyFragment;
    private MineFragment mMineFragment;
    private ManageFragment mManageFragment;

    private List<BaseFragment> mFragmentList;


    public static void actionStart(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HotKeyFragment.newInstance());
        mFragmentList.add(App.getUser().getPermission() == Constants.ACCOUNT_TYPE_MANAGER ?
                ManageFragment.newInstance() : TipFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());
    }

    @Override
    protected void initView() {
        initViewPager();
    }

    private void initViewPager() {
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        mVpContainer.setAdapter(pagerAdapter);
        mVpContainer.setOffscreenPageLimit(3);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {
        mBnvTab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_search:
                        mVpContainer.setCurrentItem(0);
                        return true;
                    case R.id.tab_manage:
                        mVpContainer.setCurrentItem(1);
                        return true;
                    case R.id.tab_mine:
                        mVpContainer.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBnvTab.setSelectedItemId(R.id.tab_search);
                        break;
                    case 1:
                        mBnvTab.setSelectedItemId(R.id.tab_manage);
                        break;
                    case 2:
                        mBnvTab.setSelectedItemId(R.id.tab_mine);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
