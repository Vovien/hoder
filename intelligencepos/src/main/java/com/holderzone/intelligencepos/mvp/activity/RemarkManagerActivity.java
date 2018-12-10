package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.fragment.DishesRemarkFragment;
import com.holderzone.intelligencepos.mvp.fragment.RetreatDishesReasonFragment;
import com.holderzone.intelligencepos.widget.Title;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LiTao on 2017-9-6.
 * 备注管理页面 包括：菜品备注、退菜原因
 */

public class RemarkManagerActivity extends BaseActivity {
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.remark_manager_table)
    TabLayout mRemarkManagerTable;
    @BindView(R.id.remark_manager_vp)
    ViewPager mRemarkManagerVp;
    private static final String KEY_TITLE_REMARK_DISHES = "菜品备注";

    private static final String KEY_TITLE_RETURN_REASON = "退菜原因";
    /**
     * TableLayout数据
     */
    private List<String> mArrayOfTbTitles = new ArrayList<>();

    /**
     * ViewPager Adapter
     */
    private RemarkManagerAdapter mRemarkManagerAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RemarkManagerActivity.class);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_remark_manager;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTitle.setTitleText("备注管理");
        mTitle.setOnReturnClickListener(this::finish);
        // 构造TabLayout数据
        mArrayOfTbTitles.add(KEY_TITLE_REMARK_DISHES);
        mArrayOfTbTitles.add(KEY_TITLE_RETURN_REASON);
        //初始化tableLayout
        mRemarkManagerTable.setTabMode(TabLayout.MODE_FIXED);
        mRemarkManagerTable.setTabGravity(TabLayout.GRAVITY_FILL);
        //初始化Adapter
        mRemarkManagerAdapter = new RemarkManagerAdapter(getSupportFragmentManager());
        mRemarkManagerVp.setAdapter(mRemarkManagerAdapter);
        mRemarkManagerTable.setupWithViewPager(mRemarkManagerVp);

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    private class RemarkManagerAdapter extends FragmentStatePagerAdapter {

        public RemarkManagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            String title = mArrayOfTbTitles.get(position);
            switch (title) {
                case KEY_TITLE_REMARK_DISHES:
                    //菜品备注
                    fragment = DishesRemarkFragment.newInstance();
                    break;
                case KEY_TITLE_RETURN_REASON:
                    //退菜原因
                    fragment = RetreatDishesReasonFragment.newInstance();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mArrayOfTbTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mArrayOfTbTitles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
