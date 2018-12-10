package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.SnackDishesContract;
import com.holderzone.intelligencepos.mvp.fragment.SnackNewOrderFragment;
import com.holderzone.intelligencepos.mvp.fragment.SnackOrderFragment;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroupDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;
import com.holderzone.intelligencepos.mvp.snack.activity.SnackOrderDetailActivity;
import com.holderzone.intelligencepos.mvp.presenter.SnackDishesPresenter;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.AddDishesPopup;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

import static com.holderzone.intelligencepos.base.Constants.EXTRAS_MEMBER_INFO;
import static com.holderzone.intelligencepos.mvp.activity.SearchDishesActivity.CALL_BACK_ORDER_GROUP;

/**
 * 快销点菜页面
 * Created by chencao on 2017/8/2.
 */

public class SnackDishesActivity extends BaseActivity<SnackDishesContract.Presenter> implements SnackDishesContract.View, AddDishesPopup.AddDishesPopupListener {

    private static final int KEY_SERACH_CODE = 222;
    private static int REQUEST_CODE_MEMBER_LOGIN = 01;
    public static final int REQUEST_CODE_PACKAGE_GROUP = 110;
    private static final String INTENT_ORDER_COUNT = "INTENT_ORDER_COUNT";
    private static final String INTENT_PACKAGE_GROUP = "INTENT_PACKAGE_GROUP";
    private static final String INTENT_DISHES_GUID = "INTENT_DISHES_GUID";
    private static final String INTENT_RISE_GUID = "INTENT_RISE_GUID";
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_refresh)
    TextView titleRefresh;
    @BindView(R.id.title_more_image)
    ImageView titleMoreImage;
    @BindView(R.id.snack_dishes_tab_layout)
    TabLayout snackDishesTabLayout;
    @BindView(R.id.snack_dishes_viewpager)
    ViewPager snackDishesViewpager;
    MemberInfoE mMemberInfo;
    SnackNewOrderFragment snackNewOrderFragment;
    SnackOrderFragment snackOrderFragment;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    private List<String> titleList;
    private MyViewPagerAdapter pagerAdapter;
    private boolean mHasOpenMemberPrice;
    private boolean isHesMember;

    public static Intent newIntent(Context context) {
        return new Intent(context, SnackDishesActivity.class);
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void onGetSystemConfigSuccess(ParametersConfig config) {
        this.mHasOpenMemberPrice = config.isMemberPrice();
        this.isHesMember = config.isHesMember();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_snack_dishes;
    }

    @Override
    protected SnackDishesContract.Presenter initPresenter() {
        return new SnackDishesPresenter(this);
    }

    /**
     * 记录待结账数量
     */
    private int orderDishesNumber = 0;

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter.getSystemConfig();
        Observable.merge(RxView.clicks(backImage), RxView.clicks(titleText))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(aBoolean -> clickBack());

        RxView.clicks(titleMoreImage).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            AddDishesPopup dishesOrderedPopup = new AddDishesPopup(SnackDishesActivity.this, mMemberInfo, isHesMember);
            dishesOrderedPopup.showOnAnchor(titleMoreImage,
                    RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
        });
        ivSearch.setVisibility(View.VISIBLE);
        RxView.clicks(ivSearch).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            orderDishesGroup = snackNewOrderFragment.getOrderDishesGroup();
            startActivityForResult(SearchDishesActivity.newIntent(this, orderDishesGroup, mHasOpenMemberPrice, mMemberInfo != null), KEY_SERACH_CODE);
        });
    }

    @Override
    public void onDispose() {
    }

    @Override
    public void onRefreshClick() {
        if (mPresenter != null) {
            mPresenter.getListNotCheckOut();
        }
        if (snackNewOrderFragment != null) {
            snackNewOrderFragment.setReFresh();
        }
        if (snackOrderFragment != null) {
            snackOrderFragment.setOnDataRefreshed();
        }
    }

    private void refreshPrice() {
        if (snackNewOrderFragment != null) {
            snackNewOrderFragment.resetUseMemberPrice(mMemberInfo != null);
        }
    }

    @Override
    public void onMemeberLoginExitClick(boolean isLogin) {
        if (isLogin) {
            getDialogFactory().showConfirmDialog(getString(R.string.member_exit), getString(R.string.cancel), getString(R.string.confirm_exit), new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {
                }

                @Override
                public void onPosClick() {
                    mMemberInfo = null;
                    refreshPrice();
                }
            });
        } else {
            startActivityForResult(BalanceAccountsMemberLoginActivity.newIntent(getApplicationContext(), null), REQUEST_CODE_MEMBER_LOGIN);
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        titleText.setText("快餐 选择菜品");
        titleRefresh.setVisibility(View.GONE);
        titleMoreImage.setVisibility(View.VISIBLE);
        titleList = new ArrayList<>();
        titleList.add("新点单");
        titleList.add("待结账(" + orderDishesNumber + ")");
        pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), titleList);
        snackDishesViewpager.setAdapter(pagerAdapter);
        snackDishesTabLayout.setupWithViewPager(snackDishesViewpager);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    private void clickBack() {
        OrderDishesGroup newOrderDishesGroup = null;
        if (snackNewOrderFragment != null) {
            newOrderDishesGroup = snackNewOrderFragment.getOrderDishesGroup();
        }
        if (newOrderDishesGroup == null || newOrderDishesGroup.getOrderDishesRecordList() == null || newOrderDishesGroup.getOrderDishesRecordList().size() == 0) {
            finish();
        } else {
            mDialogFactory.showConfirmDialog("返回将不再保留已选菜品。确认返回吗？", "取消", "确认返回", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {
                }

                @Override
                public void onPosClick() {
                    finish();
                }
            });
        }
    }

    @Override
    public void onGetListNotCheckOutSuccess(int count) {
        orderDishesNumber = count;
        if (titleList == null) {
            titleList = new ArrayList<>();
        }
        titleList.clear();
        titleList.add("新点单");
        titleList.add("待结账(" + orderDishesNumber + ")");
        if (pagerAdapter == null) {
            pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), titleList);
        }
        pagerAdapter.setTitleList(titleList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getListNotCheckOut();
    }


    class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> titleList;

        public MyViewPagerAdapter(FragmentManager fm, List<String> titleList) {
            super(fm);
            this.titleList = titleList;
        }

        public void setTitleList(List<String> titleList) {
            this.titleList = titleList;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            if ("新点单".equals(titleList.get(position))) {/**新点菜品fragment*/
                snackNewOrderFragment = SnackNewOrderFragment.getInstanse(orderDishesGroup);
                return snackNewOrderFragment;
            } else {/**待结账fragment**/
                snackOrderFragment = SnackOrderFragment.getInstance();
                return snackOrderFragment;
            }
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        /**
         * FragmentPager的标题,如果不重写这个方法就显示不出tab的标题内容
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    public static final int REQESTCOdE = 100;
    public static final int REQESTCOdE_OPERATION = 200;
    OrderDishesGroup orderDishesGroup = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQESTCOdE && resultCode == RESULT_OK) {
//            mPresenter.getListNotCheckOut();
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    orderDishesGroup = bundle.getParcelable(SnackOrderDetailActivity.SALES_ORDER_DISHES_GROUP_KEY);
                    if (snackNewOrderFragment != null) {
                        snackNewOrderFragment.setOrderDishesGroup(orderDishesGroup);
                    } else {
                        snackNewOrderFragment = SnackNewOrderFragment.getInstanse(orderDishesGroup);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_MEMBER_LOGIN && resultCode == RESULT_OK) {
            mMemberInfo = data.getParcelableExtra(EXTRAS_MEMBER_INFO);
            refreshPrice();
        } else if (requestCode == REQUEST_CODE_PACKAGE_GROUP && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String dishesGUID = bundle.getString(INTENT_DISHES_GUID);
                double count = bundle.getDouble(INTENT_ORDER_COUNT);//套餐数量
                double rise = bundle.getDouble(INTENT_RISE_GUID);//浮动价
                List<PackageGroupDishesE> packageGroupList = bundle.getParcelableArrayList(INTENT_PACKAGE_GROUP);
                if (snackNewOrderFragment != null) {
                    snackNewOrderFragment.setNewDate(dishesGUID, count, rise, packageGroupList);
                } else {
                    snackNewOrderFragment = SnackNewOrderFragment.getInstanse(orderDishesGroup);
                    snackNewOrderFragment.setNewDate(dishesGUID, count, rise, packageGroupList);
                }
            }
        } else if (requestCode == KEY_SERACH_CODE && resultCode == RESULT_OK) {
            //搜索页面返回的数据
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                orderDishesGroup = bundle.getParcelable(CALL_BACK_ORDER_GROUP);
                if (orderDishesGroup != null) {
                    if (snackNewOrderFragment != null) {
                        snackNewOrderFragment.setOrderDishesGroup(orderDishesGroup);
                    } else {
                        snackNewOrderFragment = SnackNewOrderFragment.getInstanse(orderDishesGroup);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            clickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean getUseMemberPrice() {
        return mMemberInfo != null && mHasOpenMemberPrice;
    }

    public String getMemberGuid() {
        return mMemberInfo == null ? null : mMemberInfo.getMemberInfoGUID();
    }
}
