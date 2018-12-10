package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountWholeOrderContract;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsDiscountWholeOrderPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算-折扣-整单折扣 界面
 * Created by zhaoping on 2017/6/1.
 */

public class BalanceAccountsDiscountWholeOrderActivity extends BaseActivity<BalanceAccountsDiscountWholeOrderContract.Presenter> implements BalanceAccountsDiscountWholeOrderContract.View {

    public static String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";
    public static String EXTRAS_SALES_ORDER_DISCOUNT = "EXTRAS_SALES_ORDER_DISCOUNT";

    @BindView(R.id.discountValueText)
    TextView discountValueText;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.title)
    Title title;

    private String mSalesOrderGuid;
    private double mBeforeOrderDiscount;

    public static Intent newIntent(Context context, String salesOrderGuid, Double orderDiscount) {
        Intent intent = new Intent(context, BalanceAccountsDiscountWholeOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, salesOrderGuid);
        if (orderDiscount != null) {
            bundle.putDouble(EXTRAS_SALES_ORDER_DISCOUNT, orderDiscount);
        }
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID, null);
        mBeforeOrderDiscount = extras.getDouble(EXTRAS_SALES_ORDER_DISCOUNT, -1);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts_discount_whole_order;
    }

    @Override
    protected BalanceAccountsDiscountWholeOrderContract.Presenter initPresenter() {
        return new BalanceAccountsDiscountWholeOrderPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
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
    public void onDiscountRatioSuccess() {
        setResult(RESULT_OK);
        finishActivity();
    }

    private void inputChanged(String in) {
        String value = discountValueText.getText().toString();
        if ("-".equals(in) && value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        } else if ("-".equals(in)) {
        } else if (value.length() == 0 && !".".equals(in)) {
            value = in;
        } else if (value.length() == 1 && ".".equals(in)) {
            value += ".";
        } else if ("1".equals(value) && "0".equals(in)) {
            value += in;
        } else if (value.length() == 2 && value.contains(".") && !".".equals(in)) {
            value += in;
        } else if ("10".equals(value) && ".".equals(in)) {
            value += ".";
        } else if ("10.".equals(value) && "0".equals(in)) {
            value += in;
        }
        discountValueText.setText(value);
        if (!".".equalsIgnoreCase(value) && value.length() > 0) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_confirm,
            R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_number_1:
                inputChanged("1");
                break;
            case R.id.btn_number_2:
                inputChanged("2");
                break;
            case R.id.btn_number_3:
                inputChanged("3");
                break;
            case R.id.btn_number_4:
                inputChanged("4");
                break;
            case R.id.btn_number_5:
                inputChanged("5");
                break;
            case R.id.btn_number_6:
                inputChanged("6");
                break;
            case R.id.btn_number_7:
                inputChanged("7");
                break;
            case R.id.btn_number_8:
                inputChanged("8");
                break;
            case R.id.btn_number_9:
                inputChanged("9");
                break;
            case R.id.btn_number_dot:
                inputChanged(".");
                break;
            case R.id.btn_number_0:
                inputChanged("0");
                break;
            case R.id.iv_delete:
                inputChanged("-");
                break;
            case R.id.btn_confirm:
                mPresenter.setDiscountRatio(mSalesOrderGuid, ArithUtil.div(Double.valueOf(discountValueText.getText().toString()), 10));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDispose() {

    }
}
