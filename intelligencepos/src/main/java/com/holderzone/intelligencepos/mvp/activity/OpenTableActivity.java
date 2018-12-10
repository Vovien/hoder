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
import com.holderzone.intelligencepos.mvp.fragment.OpenTableFragment;
import com.holderzone.intelligencepos.mvp.fragment.PredictedTableFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.widget.Title;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 开台界面
 * Created by tcw on 2017/9/4.
 */
public class OpenTableActivity extends BaseActivity {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_TITLE_NEW = "新开台";

    private static final String KEY_TITLE_PREDICT = "已预订";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tl_func_type)
    TabLayout mTlFuncType;
    @BindView(R.id.vp_func)
    ViewPager mVpFunc;

    // 当前桌台实体
    private DiningTableE mDiningTableE;

    // tabLayout && viewPager
    private OpenTableAdapter mOpenTableAdapter;
    private List<String> mArrayOfTbTitles = new ArrayList<>();

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, OpenTableActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_open_table;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化 title
        initTitle();

        // 初始化 tabLayout
        initTabLayout();

        // 构造TabLayout数据
        generateFakeTabTitle();

        // 初始化 viewPager
        initViewPager();

        // 设置 tabLayout && viewPager 联动
        linkTabLayoutWithViewPager();
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


    /*************************view callback begin*************************/
    /**************************view callback end**************************/

    /*************************child callback begin*************************/

    /**************************child callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化 title
     */
    private void initTitle() {
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setTitleText(mDiningTableE.getName() + " 开台");
    }

    /**
     * 初始化 tabLayout
     */
    private void initTabLayout() {
        mTlFuncType.setTabMode(TabLayout.MODE_FIXED);
        mTlFuncType.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    /**
     * 初始化 viewPager
     */
    private void initViewPager() {
        mOpenTableAdapter = new OpenTableAdapter(getSupportFragmentManager());
        mVpFunc.setAdapter(mOpenTableAdapter);
    }

    /**
     * 设置 tabLayout && viewPager 联动
     */
    private void linkTabLayoutWithViewPager() {
        mTlFuncType.setupWithViewPager(mVpFunc);
    }

    /**
     * 构造TabLayout数据
     */
    private void generateFakeTabTitle() {
        mArrayOfTbTitles.add(KEY_TITLE_NEW);
        mArrayOfTbTitles.add(KEY_TITLE_PREDICT);
    }

    /**************************private method end**************************/

    /**************************inner class begin**************************/

    private class OpenTableAdapter extends FragmentStatePagerAdapter {

        public OpenTableAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            String title = mArrayOfTbTitles.get(position);
            switch (title) {
                case KEY_TITLE_NEW:
                    fragment = OpenTableFragment.newInstance(mDiningTableE);
                    break;
                case KEY_TITLE_PREDICT:
                    fragment = PredictedTableFragment.newInstance(mDiningTableE);
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
    }

    /***************************inner class end***************************/
}