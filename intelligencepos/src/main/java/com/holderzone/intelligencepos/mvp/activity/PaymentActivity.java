package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.PaymentContract;
import com.holderzone.intelligencepos.mvp.fragment.PaymentAliPayFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentBankCardFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentCashFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentHesMemberCardFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentJhPayFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentMtcCheckFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentSunmiL3Fragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentVipCardFragment;
import com.holderzone.intelligencepos.mvp.fragment.PaymentWxPayFragment;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;
import com.holderzone.intelligencepos.mvp.presenter.PaymentPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 支付界面
 * Created by tcw on 2017/5/31.
 */

public class PaymentActivity extends BaseActivity<PaymentContract.Presenter> implements
        PaymentContract.View,
        PaymentVipCardFragment.OnPaymentListener,
        PaymentCashFragment.OnPaymentListener,
        PaymentBankCardFragment.OnPaymentListener,
        PaymentWxPayFragment.OnPaymentListener,
        PaymentAliPayFragment.OnPaymentListener,
        PaymentJhPayFragment.OnPaymentListener,
        PaymentSunmiL3Fragment.OnL3PayListener,
        PaymentMtcCheckFragment.OnPaymentListener,
        PaymentHesMemberCardFragment.CallBackHesMemberPayment {

    public static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";

    public static final String RESULT_PAY_OR_REFUND_HAPPEN = "RESULT_PAY_OR_REFUND_HAPPEN";
    public static final String RESULT_LOGIN_MEMBER = "RESULT_LOGIN_MEMBER";

    private static final int REQUEST_CODE_PAYMENT_MANAGE = 0x00000000;

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000016;

    public static final String PaymentItemCode_WX01 = "WX01";// 微信
    public static final String PaymentItemCode_ZFB01 = "ZFB01";// 支付宝
    public static final String PaymentItemCode_JHZF01 = "JHZF01";// 聚合支付
    public static final String PaymentItemCode_VC01 = "VC01";// 会员卡
    public static final String PaymentItemCode_CH01 = "CH01";// 人民币
    public static final String PaymentItemCode_BC01 = "BC01";// 银行卡
    public static final String PaymentItemCode_MTC01 = "MTC01";// 美团券
    public static final String PaymentItemCode_SYB01 = "SYB01";//L3支付(通联支付)
    public static final String PaymentItemCode_HES01 = "Hes01";//He`s会员支付

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.tl_payment_item_name)
    TabLayout mTlPaymentItemName;
    @BindView(R.id.vp_payment_item)
    ViewPager mVpPaymentItem;

    /**
     * 待支付订单GUID
     */
    private String mSaleOrderGUID;

    /**
     * 初始化标志位
     */
    private boolean mInitialDone = false;

    /**
     * 订单上的会员实体
     */
    private MemberInfoE mMemberInfoE;

    /**
     * 应收金额
     */
    private double mCheckTotalMoney;

    /**
     * 已收金额
     */
    private double mPayTotalMoney;

    /**
     * 待收金额
     */
    private double mUnpaidTotal;

    /**
     * 是否发生过付款、退款
     */
    private boolean mPayOrRefundHappened;

    /**
     * viewPager adapter
     */
    private PaymentItemAdapter mPaymentItemAdapter;

    private boolean isHesMember = false;

    private boolean needRefreshMember;

    /**
     * 静态方法，对外暴露该activity需要的参数
     *
     * @param context
     * @param salesOrderGUID
     * @return
     */
    public static Intent newIntent(Context context, String salesOrderGUID) {
        Intent intent = new Intent(context, PaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSaleOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    protected PaymentContract.Presenter initPresenter() {
        return new PaymentPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // title
        mTitle.setOnReturnClickListener(
                () -> mDialogFactory.showConfirmDialog("当前收款未完成，确认退出吗？", "取消", "确认退出", new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {

                    }

                    @Override
                    public void onPosClick() {
                        setResultCancelAndFinish();
                    }
                }));
        mTitle.setOnMenuClickListener(() -> startActivityForResult(PaymentManageActivity.newIntent(PaymentActivity.this, mSaleOrderGUID), REQUEST_CODE_PAYMENT_MANAGE));
        // MultiStateView retry when error
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> mPresenter.requestSalesOrder(mSaleOrderGUID));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestIsHesMember();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        mDialogFactory.showConfirmDialog("当前收款未完成，确认退出吗？", "取消", "确认退出", new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {

            }

            @Override
            public void onPosClick() {
                setResultCancelAndFinish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PAYMENT_MANAGE:
                if (Activity.RESULT_OK == resultCode) {
                    setResultOkAndFinish();
                } else if (Activity.RESULT_CANCELED == resultCode) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            boolean refundHappened = extras.getBoolean(PaymentManageActivity.RESULT_REFUND_HAPPENED);
                            if (refundHappened) {
                                // 更新付款、退款操作flag
                                updatePayOrRefundFlag();
                                // 请求新的订单数据
                                mPresenter.requestSalesOrder(mSaleOrderGUID);
                            }
                        }
                    }
                }
                break;
            case REQUEST_CODE_CHECK_OUT:
                if (Activity.RESULT_OK == resultCode) {
                    setResultOkAndFinish();
                } else if (Activity.RESULT_CANCELED == resultCode) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            boolean needCorrect = extras.getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                            if (needCorrect) {
                                updatePayOrRefundFlag();
                                mPresenter.requestSalesOrder(mSaleOrderGUID);
                            }
                        }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private List<PaymentItem> mArrayOfPaymentItem;

    @Override
    public void onPaymentItemObtainSucceed(List<PaymentItem> arrayOfPaymentItem) {
        mArrayOfPaymentItem = arrayOfPaymentItem;
        if (mArrayOfPaymentItem == null || mArrayOfPaymentItem.size() == 0) {
            showMessage("请配置支付方式");
        } else {
            if (isHesMember) {
                //移除会员卡支付
                Iterator<PaymentItem> iterator = arrayOfPaymentItem.iterator();
                while (iterator.hasNext()) {
                    PaymentItem next = iterator.next();
                    if (next.getPaymentItemCode().equalsIgnoreCase(PaymentItemCode_VC01)) {
                        iterator.remove();
                    }
                }
            } else {
                //移除会员支付
                Iterator<PaymentItem> iterator = arrayOfPaymentItem.iterator();
                while (iterator.hasNext()) {
                    PaymentItem next = iterator.next();
                    if (next.getPaymentItemCode().equalsIgnoreCase(PaymentItemCode_HES01)) {
                        iterator.remove();
                    }
                }
            }
            Iterator<PaymentItem> iterator = mArrayOfPaymentItem.iterator();
            while (iterator.hasNext()) {
                PaymentItem next = iterator.next();
                if (next.getPaymentItemCode().equalsIgnoreCase(PaymentItemCode_CH01)) {
                    iterator.remove();
                }
            }
            mPresenter.requestSalesOrder(mSaleOrderGUID);
        }
    }

    /*******************view callback start*******************/

    @Override
    public void onSalesOrderObtainSucceed(SalesOrderE salesOrderE) {
        // 切换到content布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        if (!mInitialDone) {
            mInitialDone = true;
            mPaymentItemAdapter = new PaymentItemAdapter(getSupportFragmentManager(), mArrayOfPaymentItem);
            mVpPaymentItem.setAdapter(mPaymentItemAdapter);
            // tabLayout
            mTlPaymentItemName.setupWithViewPager(mVpPaymentItem);
            if (mMemberInfoE != null) {
                for (int i = 0; i < mArrayOfPaymentItem.size(); i++) {
                    PaymentItem paymentItem = mArrayOfPaymentItem.get(i);
                    if (PaymentItemCode_VC01.equalsIgnoreCase(paymentItem.getPaymentItemCode())) {
                        mTlPaymentItemName.getTabAt(i).select();
                    }
                }
            }
        } else {
            mPaymentItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSalesOrderObtainFailed() {
        // 切换到error布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void responseIsHesMember(boolean isHesMember) {
        this.isHesMember = isHesMember;
        mPresenter.requestPaymentItem();
    }

    /*******************view callback end*******************/

    /*******************childfragment callback start*******************/

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerVipCard() {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 请求新的订单数据以更新界面
        mPresenter.requestSalesOrder(mSaleOrderGUID);
    }

    @Override
    public void onCheckoutFinishedInnerVipCard() {
        setResultOkAndFinish();
    }

    @Override
    public void setPage() {
        mVpPaymentItem.setCurrentItem(0, true);//参数一是ViewPager的position,参数二为是否有滑动效果
    }


    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerCash(SalesOrderE salesOrderE) {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        mPaymentItemAdapter.notifyDataSetChanged();
        // 修复
        tabLayoutBugFixImperfect();
    }

    @Override
    public void onCheckoutFinishedInnerCash() {
        setResultOkAndFinish();
    }

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerBankCard(SalesOrderE salesOrderE) {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        mPaymentItemAdapter.notifyDataSetChanged();
        // 修复
        tabLayoutBugFixImperfect();
    }

    @Override
    public void onCheckoutFinishedInnerBankCard() {
        setResultOkAndFinish();
    }

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerAliPay(SalesOrderE salesOrderE) {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        mPaymentItemAdapter.notifyDataSetChanged();
        // 修复
        tabLayoutBugFixImperfect();
    }

    @Override
    public void onCheckoutFinishedInnerAliPay() {
        setResultOkAndFinish();
    }

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerWxPay(SalesOrderE salesOrderE) {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        mPaymentItemAdapter.notifyDataSetChanged();
        // 修复
        tabLayoutBugFixImperfect();
    }

    @Override
    public void onCheckoutFinishedInnerWxPay() {
        setResultOkAndFinish();
    }

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerJhPay(SalesOrderE salesOrderE) {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        mPaymentItemAdapter.notifyDataSetChanged();
        // 修复
        tabLayoutBugFixImperfect();
    }

    @Override
    public void onCheckoutFinishedInnerJhPay() {
        setResultOkAndFinish();
    }

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerMtc() {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 请求新的订单数据以更新界面
        mPresenter.requestSalesOrder(mSaleOrderGUID);
    }

    @Override
    public void onCheckoutFinishedInnerMtc() {
        setResultOkAndFinish();
    }

    @Override
    public void onPaymentHappenedAndCheckoutUnfinishedInnerL3(SalesOrderE salesOrderE) {
        // 更新发生付款退款操作flag
        updatePayOrRefundFlag();
        // 刷新订单基本信息
        refreshSalesOrderBasicInfo(salesOrderE);
        // 刷新各支付方式fragment
        mPaymentItemAdapter.notifyDataSetChanged();
        // 修复
        tabLayoutBugFixImperfect();
    }

    @Override
    public void onCheckoutFinishedInnerL3() {
        setResultOkAndFinish();
    }

    @Override
    public void onDispose() {

    }

    /*******************childfragment callback end*******************/

    /*******************private method start*******************/

    /**
     * 刷新订单基本信息
     *
     * @param salesOrderE 订单实体
     */
    private void refreshSalesOrderBasicInfo(SalesOrderE salesOrderE) {
        // 显示应收金额、已收金额
        mMemberInfoE = salesOrderE.getMemberInfoE();
        Double checkTotal = salesOrderE.getCheckTotal();
        if (checkTotal != null) {
            mCheckTotalMoney = checkTotal;
        } else {
            showMessage("数据非法，CheckTotal不得为null");
        }
        Double payTotal = salesOrderE.getPayTotal();
        if (payTotal != null) {
            mPayTotalMoney = payTotal;
        } else {
            showMessage("数据非法，PayTotal不得为null");
        }
        Double unpaidTotal = salesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal;
        } else {
            showMessage("数据非法，UnpaidTotal不得为null");
        }

        if (mUnpaidTotal < 0.01) {
            if (mPayTotalMoney == mCheckTotalMoney) {
                mTitle.setMenuText("待收：" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
                mDialogFactory.showConfirmDialog("实收" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCheckTotalMoney)) + "，确认结账吗？",
                        "取消", "确认结账", new ConfirmDialogFragment.ConfirmDialogListener() {
                            @Override
                            public void onNegClick() {

                            }

                            @Override
                            public void onPosClick() {
                                startActivityForResult(PaymentCheckoutActivity.newIntent(PaymentActivity.this
                                        , mSaleOrderGUID, salesOrderE.getDiningTableE().getName(), mCheckTotalMoney
                                        , salesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
                            }
                        }
                );
            } else if (mPayTotalMoney > mCheckTotalMoney) {
                mTitle.setMenuText("溢收：" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(mPayTotalMoney, mCheckTotalMoney))));
                mDialogFactory.showConfirmDialog("价格已调整为应收" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCheckTotalMoney)) +
                                "，超额实收" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(payTotal)) +
                                "。请退还超额款项后结账。", false, null, 0
                        , true, "确认", 0, new ConfirmDialogFragment.ConfirmDialogListener() {
                            @Override
                            public void onNegClick() {

                            }

                            @Override
                            public void onPosClick() {
                                startActivityForResult(PaymentManageActivity.newIntent(PaymentActivity.this
                                        , mSaleOrderGUID, true), REQUEST_CODE_PAYMENT_MANAGE);
                            }
                        });
            }
        } else {
            mTitle.setMenuText("待收：" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
        }
    }

    /**
     * 更新发生付款退款操作flag
     */
    private void updatePayOrRefundFlag() {
        if (!mPayOrRefundHappened) {
            mPayOrRefundHappened = true;
        }
    }

    /**
     * 设置result_ok并且退出当前activity
     */
    private void setResultOkAndFinish() {
        setResult(Activity.RESULT_OK);
        finishActivity();
    }

    /**
     * 设置result_cancel并且退出当前activity
     */
    private void setResultCancelAndFinish() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putBoolean(RESULT_PAY_OR_REFUND_HAPPEN, mPayOrRefundHappened);
        extras.putBoolean(RESULT_LOGIN_MEMBER, needRefreshMember);
        intent.putExtras(extras);
        setResult(Activity.RESULT_CANCELED, intent);
        finishActivity();
    }

    /**
     * 僵硬地修复tablayout标题错位
     */
    @SuppressLint("CheckResult")
    private void tabLayoutBugFixImperfect() {
        Observable.timer(30, TimeUnit.MILLISECONDS)
                .compose(RxTransformer.bindToLifecycle(this))
                .subscribe(aLong -> mTlPaymentItemName.setScrollPosition(mVpPaymentItem.getCurrentItem(), 0, false));
    }

    public void setLoginMemberSuccess() {
        needRefreshMember = true;
    }

    /*******************private method end*******************/

    /**
     * viewPagerAdapter 定义
     */
    private class PaymentItemAdapter extends FragmentStatePagerAdapter {
        private List<PaymentItem> mPaymentItemList;

        public PaymentItemAdapter(FragmentManager fm, List<PaymentItem> paymentItemList) {
            super(fm);
            if (mPaymentItemList == null) {
                mPaymentItemList = new ArrayList<>();
            }
            mPaymentItemList.clear();
            mPaymentItemList.addAll(paymentItemList);
        }

        public void setData(List<PaymentItem> paymentItemList) {
            if (mPaymentItemList == null) {
                mPaymentItemList = new ArrayList<>();
            }
            mPaymentItemList.clear();
            mPaymentItemList.addAll(paymentItemList);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            PaymentItem paymentItem = mPaymentItemList.get(position);
            String paymentItemCode = paymentItem.getPaymentItemCode();
            Fragment fragment;
            switch (paymentItemCode) {
                case PaymentItemCode_VC01:// 会员卡
                    fragment = PaymentVipCardFragment.newInstance(mSaleOrderGUID, mUnpaidTotal, mMemberInfoE);
                    break;
                case PaymentItemCode_BC01:// 银行卡
                    fragment = PaymentBankCardFragment.newInstance(mSaleOrderGUID, mUnpaidTotal);
                    break;
                case PaymentItemCode_CH01:// 人民币
                    fragment = PaymentCashFragment.newInstance(mSaleOrderGUID, mUnpaidTotal);
                    break;
                case PaymentItemCode_WX01:// 微信
                    fragment = PaymentWxPayFragment.newInstance(mSaleOrderGUID, mUnpaidTotal);
                    break;
                case PaymentItemCode_ZFB01:// 支付宝
                    fragment = PaymentAliPayFragment.newInstance(mSaleOrderGUID, mUnpaidTotal);
                    break;
                case PaymentItemCode_JHZF01:// 聚合支付
                    fragment = PaymentJhPayFragment.newInstance(mSaleOrderGUID, mUnpaidTotal);
                    break;
                case PaymentItemCode_MTC01:// 美团验券
                    fragment = PaymentMtcCheckFragment.newInstance(mSaleOrderGUID);
                    break;
                case PaymentItemCode_SYB01://L3支付
                    fragment = PaymentSunmiL3Fragment.newInstence(mSaleOrderGUID, mUnpaidTotal);
                    break;
                case PaymentItemCode_HES01://何师支付
                    fragment = PaymentHesMemberCardFragment.newInstence(mSaleOrderGUID, isHesMember, mUnpaidTotal, mMemberInfoE);
                    break;
                default:// 其余 同银行卡
                    fragment = PaymentBankCardFragment.newInstance(mSaleOrderGUID, mUnpaidTotal, paymentItemCode);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mPaymentItemList != null ? mPaymentItemList.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPaymentItemList.get(position).getPaymentItemName();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
