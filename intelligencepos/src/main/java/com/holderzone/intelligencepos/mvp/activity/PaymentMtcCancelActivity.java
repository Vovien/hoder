package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.LinearSpacingItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcCancelContract;
import com.holderzone.intelligencepos.mvp.model.bean.MeituanCoupon;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentMtcCancelPresenter;
import com.holderzone.intelligencepos.utils.DensityUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by terry on 17-11-6.
 */

public class PaymentMtcCancelActivity extends BaseActivity<PaymentMtcCancelContract.Presenter> implements PaymentMtcCancelContract.View {

    private static final String KEY_SALES_ORDER_GUID = "SALES_ORDER_GUID";

    private static final String KEY_MEITUAN_COUPON_LIST = "MEITUAN_COUPON_LIST";

    private static final int REQUEST_CODE_CHECK_OUT = 0;

    public static final String RESULT_CODE_PAYMENT_HAPPENED_INNER_CANCEL = "PAYMENT_HAPPENED_INNER_CANCEL";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_num_consume_succeed)
    TextView mTvNumConsumeSucceed;
    @BindView(R.id.rv_meituan_coupon)
    RecyclerView mRvMeituanCoupon;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    // 订单guid
    private String mSalesOrderGUID;
    // 美团券列表
    private List<MeituanCoupon> mArrayOfMeituanCoupon;
    // 适配器
    private CommonAdapter<MeituanCoupon> mCommonAdapter;
    // 当前撤销的位置
    private int mCurPosToCancel = -1;

    public static Intent newIntent(Context context, String salesOrderGUID, List<MeituanCoupon> arrayOfMeituanCoupon) {
        Intent intent = new Intent(context, PaymentMtcCancelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SALES_ORDER_GUID, salesOrderGUID);
        bundle.putParcelableArrayList(KEY_MEITUAN_COUPON_LIST, (ArrayList<? extends Parcelable>) arrayOfMeituanCoupon);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(KEY_SALES_ORDER_GUID);
        mArrayOfMeituanCoupon = extras.getParcelableArrayList(KEY_MEITUAN_COUPON_LIST);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_payment_mtc_cancel;
    }

    @Nullable
    @Override
    protected PaymentMtcCancelContract.Presenter initPresenter() {
        return new PaymentMtcCancelPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTitle.setOnReturnClickListener(() -> mPresenter.requestSalesOrder(mSalesOrderGUID));

        mTvNumConsumeSucceed.setText(new SpanUtils()
                .append(meituanCouponUsedCount() + " ").setFontSize(14, true).setForegroundColor(Color.parseColor("#2495ee"))
                .append("张美团券消费成功").setFontSize(14, true).setForegroundColor(Color.parseColor("#555555"))
                .create()
        );

        mCommonAdapter = new CommonAdapter<MeituanCoupon>(this, R.layout.item_mt_coupon, mArrayOfMeituanCoupon) {
            @Override
            protected void convert(ViewHolder holder, MeituanCoupon meituanCoupon, int position) {
                holder.setText(R.id.tv_sort_number, "" + (position + 1));
                String couponCode = meituanCoupon.getCouponCode();
                holder.setText(R.id.tv_transaction_number, couponCode.substring(0, 4) + " " + couponCode.substring(4, 8) + " " + couponCode.substring(8));
                if (meituanCouponUsed(meituanCoupon)) {
                    holder.setText(R.id.tv_used_or_not, "撤销");
                    holder.setTextColor(R.id.tv_used_or_not, Color.parseColor("#01b6ad"));
                    holder.setOnClickListener(R.id.tv_used_or_not,
                            view1 -> {
                                mCurPosToCancel = position;
                                mPresenter.MeituanRefund(mSalesOrderGUID, meituanCoupon.getSalesOrderPaymentGUID(), meituanCoupon.getCouponCode());
                            });
                } else {
                    holder.setText(R.id.tv_used_or_not, "已撤销");
                    holder.setTextColor(R.id.tv_used_or_not, Color.parseColor("#b2b6b8"));
                    holder.setOnClickListener(R.id.tv_used_or_not, null);
                }
            }
        };
        mRvMeituanCoupon.setAdapter(mCommonAdapter);
        mRvMeituanCoupon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvMeituanCoupon.addItemDecoration(new LinearSpacingItemDecoration(DensityUtils.dp2px(this, 8f), false));

        RxView.clicks(mBtnPositive).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.requestSalesOrder(mSalesOrderGUID));
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

    @Override
    public void MeituanRefundSuccess() {
        if (mCurPosToCancel >= 0) {
            MeituanCoupon meituanCoupon = mArrayOfMeituanCoupon.get(mCurPosToCancel);
            meituanCoupon.setCancelCount(meituanCoupon.getCancelCount() + 1);
            mTvNumConsumeSucceed.setText(new SpanUtils()
                    .append(meituanCouponUsedCount() + " ").setFontSize(14, true).setForegroundColor(Color.parseColor("#2495ee"))
                    .append("张美团券消费成功").setFontSize(14, true).setForegroundColor(Color.parseColor("#555555"))
                    .create()
            );
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSalesOrderObtainSucceed(SalesOrderE salesOrderE) {
        Double unpaidTotal = salesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            if (unpaidTotal < 0.01) {// 跳转到CheckOutActivity
                startActivityForResult(PaymentCheckoutActivity.newIntent(this, mSalesOrderGUID, salesOrderE.getDiningTableE().getName(), salesOrderE.getCheckTotal(), salesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {
                setResultCancelAndFinish(payHappened());
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onSalesOrderObtainFailed() {
        // no-op by default
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHECK_OUT) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOkAndFinish();
            } else {
                setResultCancelAndFinish(true);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            mPresenter.requestSalesOrder(mSalesOrderGUID);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取用券数量
     *
     * @return
     */
    private int meituanCouponUsedCount() {
        int count = 0;
        for (MeituanCoupon meituanCoupon : mArrayOfMeituanCoupon) {
            if (meituanCouponUsed(meituanCoupon)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 该美团券是否使用
     *
     * @param meituanCoupon
     * @return
     */
    private boolean meituanCouponUsed(MeituanCoupon meituanCoupon) {
        return meituanCoupon.getUseCount() - meituanCoupon.getCancelCount() > 0;
    }

    /**
     * 是否有付款行为
     *
     * @return
     */
    private boolean payHappened() {
        return meituanCouponUsedCount() > 0;
    }

    private void setResultOkAndFinish() {
        setResult(Activity.RESULT_OK);
        finishActivity();
    }

    private void setResultCancelAndFinish(boolean payHappened) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(RESULT_CODE_PAYMENT_HAPPENED_INNER_CANCEL, payHappened);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_CANCELED, intent);
        finishActivity();
    }
}
