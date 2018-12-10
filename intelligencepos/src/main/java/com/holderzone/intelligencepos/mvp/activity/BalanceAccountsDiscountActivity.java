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
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountContract;
import com.holderzone.intelligencepos.mvp.fragment.DiscountDetailsFragment;
import com.holderzone.intelligencepos.mvp.fragment.DiscountOperationFragment;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDiscountE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsDiscountPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 结算-折扣 界面
 */
public class BalanceAccountsDiscountActivity extends BaseActivity<BalanceAccountsDiscountContract.Presenter> implements BalanceAccountsDiscountContract.View {

    private static final int ORDER_DISCOUNT_REQUEST_CODE = 0x4;
    private static final int CHANGE_ORDER_PRICE_REQUEST_CODE = 0x3;
    private static final int COUPON_REQUEST_CODE = 0x4;
    private static final int CARD_REQUEST_CODE = 0x6;
    private static final int MEMBER_SHIP_INTEGRAL = 0x7;
    private static final int AWAY_DISHES_CODE = 0x9;



    public static String EXTRAS_ORDER_DISHES = "EXTRAS_ORDER_DISHES";
    public static String EXTRAS_MAIN_SALES_ORDER = "EXTRAS_MAIN_SALES_ORDER";
    public static String EXTRAS_RESULT = "EXTRAS_RESULT";

    private static final String KEY_DISCOUNTED_OPERATION = "折扣操作";
    private static final String KEY_DISCOUNTED_DETAILS = "折扣明细";
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.tl_title)
    TabLayout mTlTitle;
    @BindView(R.id.vp_layout)
    ViewPager mVpLayout;
    @BindView(R.id.tv_total_prince)
    TextView mTVTotalPrince;

    private SalesOrderE mOrder;
    private String mSalesOrderGuid;
    private boolean isDataChanged;
    private List<SalesOrderDiscountE> salesOrderDiscountES = new ArrayList<>();


    private BalanceDiscountAdapter mDishesOrderAdapter;


    private List<String> mArrayOfTbTitles = new ArrayList<>();
    private ArrayList<OrderDishesGroup> mDishesList = new ArrayList<>();
    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mOrder = extras.getParcelable(EXTRAS_MAIN_SALES_ORDER);
        mDishesList = extras.getParcelableArrayList(EXTRAS_ORDER_DISHES);
        mSalesOrderGuid = mOrder.getSalesOrderGUID();
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts_discount;
    }

    @Override
    protected BalanceAccountsDiscountContract.Presenter initPresenter() {
        return new BalanceAccountsDiscountPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化 tabLayout
        mTlTitle.setTabMode(TabLayout.MODE_FIXED);
        mTlTitle.setTabGravity(TabLayout.GRAVITY_FILL);
        mArrayOfTbTitles.add(KEY_DISCOUNTED_OPERATION);
        mArrayOfTbTitles.add(KEY_DISCOUNTED_DETAILS);

        // 初始化 viewPager
        mDishesOrderAdapter = new BalanceDiscountAdapter(getSupportFragmentManager());
        mVpLayout.setAdapter(mDishesOrderAdapter);
        // 设置 tabLayout && viewPager 联动
        mTlTitle.setupWithViewPager(mVpLayout);

        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishDiscountActivity();
            }
        });
    }

    private void finishDiscountActivity() {
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        finishActivity();
    }

    @Override
    public void onBackPressed() {
        finishDiscountActivity();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        refreshOrderData();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    private void refreshOrderData() {
        salesOrderDiscountES = mOrder.getArrayOfSalesOrderDiscountE();
        if (mOrder == null || mOrder.getDiscountTotal() == null) {
            mTVTotalPrince.setText(getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(0)));
        } else {
            mTVTotalPrince.setText(getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(mOrder.getDiscountTotal())));
        }
        mDishesOrderAdapter.notifyDataSetChanged();
    }

    public static Intent newIntent(Context context, SalesOrderE mainOrder, ArrayList<OrderDishesGroup> list) {
        Intent intent = new Intent(context, BalanceAccountsDiscountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRAS_MAIN_SALES_ORDER, mainOrder);
        bundle.putParcelableArrayList(EXTRAS_ORDER_DISHES, list);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ORDER_DISCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            isDataChanged = true;
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == CHANGE_ORDER_PRICE_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putBoolean(EXTRAS_RESULT,true);
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            /**
             * 收款改价返回
             */
            finishActivity();
//            isDataChanged = true;
//            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == COUPON_REQUEST_CODE && resultCode == RESULT_OK) {
            isDataChanged = true;
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == CARD_REQUEST_CODE && resultCode == RESULT_OK) {
            isDataChanged = true;
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == MEMBER_SHIP_INTEGRAL && resultCode == RESULT_OK) {
            isDataChanged = true;
            mPresenter.getOrderInfo(mSalesOrderGuid);
        }else if(requestCode == AWAY_DISHES_CODE && resultCode == RESULT_OK){
            isDataChanged = true;
            mPresenter.getOrderInfo(mSalesOrderGuid);
            finishDiscountActivity();
        }
    }

    @Override
    public void onGetOrderDisheSuccess(List<OrderDishesGroup> list, SalesOrderE mainOrder) {
        mOrder = mainOrder;
        salesOrderDiscountES.clear();
        salesOrderDiscountES.addAll(mOrder.getArrayOfSalesOrderDiscountE());
        refreshOrderData();
    }

    @Override
    public void showNetworkErrorLayout() {
    }

    @Override
    public void onDispose() {
    }

    private class BalanceDiscountAdapter extends FragmentStatePagerAdapter {

        public BalanceDiscountAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            String title = mArrayOfTbTitles.get(position);
            switch (title) {
                case KEY_DISCOUNTED_OPERATION:
//                    fragment = DiscountOperationFragment.getInstance(salesOrderDiscountES, mOrder, mSalesOrderGuid,mDishesList);
                    break;
                case KEY_DISCOUNTED_DETAILS:
                    fragment = DiscountDetailsFragment.getInstance(salesOrderDiscountES);
                    break;
                default:
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
