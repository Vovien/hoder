package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderAdditionalFeesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDiscountE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;

/**
 * 结算信息
 * Created by zhaoping on 2017/6/2.
 */

public class BalanceAccountInfoActivity extends BaseActivity {

    @BindView(R.id.discountInfoPanel)
    LinearLayout discountInfoPanel;
    @BindView(R.id.additionalChargePanel)
    LinearLayout additionalChargePanel;
    @BindView(R.id.consumeTotalTextView)
    TextView consumeTotalTextView;
    @BindView(R.id.discountRatioTextView)
    TextView discountRatioTextView;
    @BindView(R.id.discountTotalTextView)
    TextView discountTotalTextView;
    @BindView(R.id.additionalFeesTotalText)
    TextView additionalFeesTotalText;
    @BindView(R.id.title)
    Title title;
    public static String EXTRAS_SALES_ORDER = "EXTRAS_SALES_ORDER";
    private SalesOrderE mainOrder;

    public static Intent newIntent(Context context, SalesOrderE salesOrderE) {
        Intent intent = new Intent(context, BalanceAccountInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRAS_SALES_ORDER, salesOrderE);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mainOrder = extras.getParcelable(EXTRAS_SALES_ORDER);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_account_info;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        consumeTotalTextView.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mainOrder.getDishesConsumeTotal())));
        discountTotalTextView.setText(getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(mainOrder.getDiscountTotal())));
        additionalFeesTotalText.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mainOrder.getAdditionalFeesTotal())));
        if (mainOrder.getDiscountRatio() == 1) {
            discountRatioTextView.setText(getString(R.string.no_discount));
            discountRatioTextView.setTextColor(0xff949494);
        } else {
            discountRatioTextView.setText((ArithUtil.mul(mainOrder.getDiscountRatio(), 10)) + "折");
            discountRatioTextView.setTextColor(0xff000000);
        }
        if (mainOrder.getArrayOfSalesOrderDiscountE() != null) {
            discountInfoPanel.setVisibility(View.VISIBLE);
            discountInfoPanel.removeAllViews();
            for (SalesOrderDiscountE salesOrderDiscountE : mainOrder.getArrayOfSalesOrderDiscountE()) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_balance_account_sub_detail, null);
                TextView itemTitle = (TextView) view.findViewById(R.id.itemTitle);
                itemTitle.setText(salesOrderDiscountE.getDiscountItem());
                TextView itemContent = (TextView) view.findViewById(R.id.itemContent);
                itemContent.setText(getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount())));
                discountInfoPanel.addView(view);
            }
        }
        if (mainOrder.getArrayOfSalesOrderAdditionalFeesE() != null) {
            additionalChargePanel.setVisibility(View.VISIBLE);
            additionalChargePanel.removeAllViews();
            for (SalesOrderAdditionalFeesE salesOrderDiscountE : mainOrder.getArrayOfSalesOrderAdditionalFeesE()) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_balance_account_sub_detail, null);
                TextView itemTitle = (TextView) view.findViewById(R.id.itemTitle);
                itemTitle.setText(salesOrderDiscountE.getName());
                TextView itemContent = (TextView) view.findViewById(R.id.itemContent);
                itemContent.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getSubTotal())));
                additionalChargePanel.addView(view);
            }
        }

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

    @Override
    public void onDispose() {

    }
}
