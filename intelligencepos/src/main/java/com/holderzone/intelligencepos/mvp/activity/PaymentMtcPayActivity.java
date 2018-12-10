package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcPayContract;
import com.holderzone.intelligencepos.mvp.model.bean.MeituanCoupon;
import com.holderzone.intelligencepos.mvp.presenter.PaymentMtcPayPresenter;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by terry on 17-11-6.
 */

public class PaymentMtcPayActivity extends BaseActivity<PaymentMtcPayContract.Presenter> implements PaymentMtcPayContract.View {

    private static final String KEY_SALES_ORDER_GUID = "SALES_ORDER_GUID";

    private static final String KEY_MEITUAN_COUPON = "MEITUAN_COUPON";

    private static final int REQUEST_CODE_MTC_CANCEL = 0;

    public static final String RESULT_CODE_PAYMENT_HAPPENED_INNER_PAY = "PAYMENT_HAPPENED_INNER_PAY";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_transaction_number)
    TextView mTvTransactionNumber;
    @BindView(R.id.tv_max_use_num)
    TextView mTvMaxUseNum;
    @BindView(R.id.iv_minus_num)
    ImageView mIvMinusNum;
    @BindView(R.id.tv_cur_num)
    TextView mTvCurNum;
    @BindView(R.id.iv_add_num)
    ImageView mIvAddNum;
    @BindView(R.id.tv_deal_title)
    TextView mTvDealTitle;
    @BindView(R.id.tv_coupon_buy_price)
    TextView mTvCouponBuyPrice;
    @BindView(R.id.tv_deal_begin_time)
    TextView mTvDealBeginTime;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    private String mSalesOrderGUID;
    private MeituanCoupon mMeituanCoupon;

    public static Intent newIntent(Context context, String salesOrderGUID, MeituanCoupon meituanCoupon) {
        Intent intent = new Intent(context, PaymentMtcPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SALES_ORDER_GUID, salesOrderGUID);
        bundle.putParcelable(KEY_MEITUAN_COUPON, meituanCoupon);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(KEY_SALES_ORDER_GUID);
        mMeituanCoupon = extras.getParcelable(KEY_MEITUAN_COUPON);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_payment_mtc_pay;
    }

    @Nullable
    @Override
    protected PaymentMtcPayContract.Presenter initPresenter() {
        return new PaymentMtcPayPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 返回
        mTitle.setOnReturnClickListener(() -> setResultCancelAndFinish(false));
        // 美团券码
        String couponCode = mMeituanCoupon.getCouponCode();
        mTvTransactionNumber.setText(String.format("%s %s %s", couponCode.substring(0, 4), couponCode.substring(4, 8), couponCode.substring(8)));
        // 可消费数量
        mTvMaxUseNum.setText(new SpanUtils()
                .append("当前有").setFontSize(19, true).setForegroundColor(Color.parseColor("#555555"))
                .append(" " + mMeituanCoupon.getCount() + " ").setFontSize(19, true).setForegroundColor(Color.parseColor("#2495ee"))
                .append("张可消费的券，请选择张数").setFontSize(19, true).setForegroundColor(Color.parseColor("#555555"))
                .create()
        );
        // 数量修改控制
        mTvCurNum.setText("" + mMeituanCoupon.getCount());
        mIvMinusNum.setEnabled(true);
        mIvAddNum.setEnabled(false);
        // 美团券名
        mTvDealTitle.setText(mMeituanCoupon.getDealTitle());
        // 购买价格
        mTvCouponBuyPrice.setText("价格：￥" + mMeituanCoupon.getCouponBuyPrice());
        // 购买时间
        mTvDealBeginTime.setText("购买时间：" + mMeituanCoupon.getDealBeginTime().split(" ")[0]);
        // 确认使用
        RxView.clicks(mBtnPositive).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.MeituanPay(mSalesOrderGUID, mMeituanCoupon.getCouponCode(),
                        Integer.parseInt(mTvCurNum.getText().toString()), PaymentActivity.PaymentItemCode_MTC01));
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
    public void MeituanPaySuccess(List<MeituanCoupon> arrayOfMeituanCoupon) {
        Intent intent = PaymentMtcCancelActivity.newIntent(this, mSalesOrderGUID, arrayOfMeituanCoupon);
        startActivityForResult(intent, REQUEST_CODE_MTC_CANCEL);
    }

    @Override
    public void MeituanPayFailed() {
        mDialogFactory.showMsgDialog(this, "团购券使用未成功，请重新验证。");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_MTC_CANCEL) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOkAndFinish();
            } else {
                boolean payHappened = false;
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        payHappened = extras.getBoolean(PaymentMtcCancelActivity.RESULT_CODE_PAYMENT_HAPPENED_INNER_CANCEL);
                    }
                }
                setResultCancelAndFinish(payHappened);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            setResultCancelAndFinish(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setResultOkAndFinish() {
        setResult(Activity.RESULT_OK);
        finishActivity();
    }

    private void setResultCancelAndFinish(boolean payHappened) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(RESULT_CODE_PAYMENT_HAPPENED_INNER_PAY, payHappened);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_CANCELED, intent);
        finishActivity();
    }

    @OnClick({R.id.iv_minus_num, R.id.iv_add_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_minus_num:
                int minused = Integer.parseInt(mTvCurNum.getText().toString()) - 1;
                mTvCurNum.setText("" + minused);
                mIvMinusNum.setEnabled(minused > mMeituanCoupon.getMinConsume());
                mIvMinusNum.setImageResource(minused > mMeituanCoupon.getMinConsume() ? R.drawable.minus_blue_big : R.drawable.minus_gray_big);
                mIvAddNum.setEnabled(minused < mMeituanCoupon.getCount());
                mIvAddNum.setImageResource(minused < mMeituanCoupon.getCount() ? R.drawable.plus_blue_big : R.drawable.plus_gray_big);
                mBtnPositive.setEnabled(minused > 0);
                break;
            case R.id.iv_add_num:
                int added = Integer.parseInt(mTvCurNum.getText().toString()) + 1;
                mTvCurNum.setText("" + added);
                mIvMinusNum.setEnabled(added > mMeituanCoupon.getMinConsume());
                mIvMinusNum.setImageResource(added > mMeituanCoupon.getMinConsume() ? R.drawable.minus_blue_big : R.drawable.minus_gray_big);
                mIvAddNum.setEnabled(added < mMeituanCoupon.getCount());
                mIvAddNum.setImageResource(added < mMeituanCoupon.getCount() ? R.drawable.plus_blue_big : R.drawable.plus_gray_big);
                mBtnPositive.setEnabled(added > 0);
                break;
            default:
                break;
        }
    }
}
