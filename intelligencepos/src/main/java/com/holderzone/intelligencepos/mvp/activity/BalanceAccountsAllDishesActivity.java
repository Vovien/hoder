package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.BalanceAccountsAllDishesAdapter;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsAllDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsAllDishesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.TextUtils;
import com.holderzone.intelligencepos.widget.Title;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算-商品合计（消费商品） 界面
 * Created by zhaoping on 2017/5/31.
 */

public class BalanceAccountsAllDishesActivity extends BaseActivity<BalanceAccountsAllDishesContract.Presenter> implements BalanceAccountsAllDishesContract.View {

    public static String EXTRAS_ORDER_DISHES = "EXTRAS_ORDER_DISHES";
    public static String EXTRAS_ORDER_GUID = "EXTRAS_ORDER_GUID";
    public static String EXTRAS_ORDER_DISHES_PRICE = "EXTRAS_ORDER_DISHES_PRICE";
    public static String EXTRAS_ORDER_DISHES_TAG = "EXTRAS_ORDER_DISHES_TAG";
    @BindView(R.id.orderDishesRecyclerView)
    RecyclerView orderDishesRecyclerView;
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.totalTextView)
    TextView totalTextView;
    @BindView(R.id.dishesCountTextView)
    TextView dishesCountTextView;
    @BindView(R.id.printAccountButton)
    Button mPrintAccountButton;
    private ArrayList<OrderDishesGroup> mDatas = new ArrayList<>();
    private double mAmount = 0;
    private String mSalesOrderGuid;
    BalanceAccountsAllDishesAdapter orderDishesAdapter;
    /**
     * 上个页面出来的标记 标记底部按钮的显示方式
     * 0:桌台点餐页面
     * 1：快销页面（新点单）
     * 2：快销页面（旧账单）
     */
    private int mTag;


    public static Intent newIntent(Context context, ArrayList<OrderDishesGroup> list, double amount, String salesOrderGuid, int tag) {
        Intent intent = new Intent(context, BalanceAccountsAllDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRAS_ORDER_DISHES, list);
        bundle.putString(EXTRAS_ORDER_GUID, salesOrderGuid);
        bundle.putDouble(EXTRAS_ORDER_DISHES_PRICE, amount);
        bundle.putInt(EXTRAS_ORDER_DISHES_TAG, tag);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDatas.addAll(extras.getParcelableArrayList(EXTRAS_ORDER_DISHES));
        mAmount = extras.getDouble(EXTRAS_ORDER_DISHES_PRICE);
        mSalesOrderGuid = extras.getString(EXTRAS_ORDER_GUID);
        mTag = extras.getInt(EXTRAS_ORDER_DISHES_TAG);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts_all_dishes;
    }

    @Override
    protected BalanceAccountsAllDishesContract.Presenter initPresenter() {
        return new BalanceAccountsAllDishesPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter.getHasOpenMemberPrice();
        totalTextView.setText(getString(R.string.account_balance_dishes_price, ArithUtil.stripTrailingZeros(mAmount)));
        int count = 0;
        for (OrderDishesGroup group : mDatas) {
            count += group.getSalesOrderDishesEList().size();
        }
        if (mTag == 0) {
            //桌台点餐页面
            mPrintAccountButton.setVisibility(View.VISIBLE);
        } else {
            //快销页面
            mPrintAccountButton.setVisibility(View.GONE);
        }
        dishesCountTextView.setText(getString(R.string.account_balance_dishes_count, count));
        TextUtils.decorateTextViewText(dishesCountTextView, 1, dishesCountTextView.getText().length() - 1, R.color.tv_text_green, R.dimen.confirm_text_size);
        TextUtils.decorateTextViewText(totalTextView, 3, totalTextView.getText().length(), R.color.tv_text_red, R.dimen.confirm_text_size);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        orderDishesRecyclerView.setLayoutManager(linearLayoutManager);
        orderDishesAdapter = new BalanceAccountsAllDishesAdapter(this);
        orderDishesAdapter.setOrderDishesData(mDatas);
        orderDishesRecyclerView.setAdapter(orderDishesAdapter);
        orderDishesAdapter.notifyDataSetChanged();
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @OnClick(R.id.printAccountButton)
    public void onClick() {
        mPresenter.checkPrint(mSalesOrderGuid);
    }

    @Override
    public void onCheckPrintSuccess() {
    }

    @Override
    public void onGetHasOpenMemberPrice(Boolean hasOpen) {

    }

    @Override
    public void onDispose() {
    }
}
