package com.holderzone.intelligencepos.mvp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.PaymentActivity;
import com.holderzone.intelligencepos.mvp.activity.PaymentVipCardCollectionActivity;
import com.holderzone.intelligencepos.mvp.activity.PaymentVipCardsActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentVipCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentVipCardPresenter;
import com.kennyc.view.MultiStateView;

import java.util.List;

import butterknife.BindView;

import static com.holderzone.intelligencepos.mvp.activity.PaymentVipCardsActivity.RESULT_PAYMENT_BY_OTHER_WAY;

/**
 * 会员卡支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentVipCardFragment extends BaseFragment<PaymentVipCardContract.Presenter> implements
        PaymentVipCardContract.View,
        PaymentVipCardChooseCardFragment.OnClickListener,
        PaymentVipCardDisplayCardsFragment.OnClickListener {

    private static final String EXTRA_SALES_ORDER_GUID = "cn.holdzone.intelligencepos.SalesOrderGUID";
    private static final String EXTRA_UNPAID_TOTAL = "cn.holdzone.intelligencepos.UppaidTotal";
    private static final String EXTRA_MEMBER_INFO = "cn.holdzone.intelligencepos.MemberInfo";

    private static final String SAVED_INSTANCE_REG_TEL = "com.holderzone.intelligencepos.RegTel";

    private static final int REQUEST_CODE_PAYMENT_VC_COLLECTION = 0x00000012;

    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    /**
     * 是否是卡片选择页面
     */
    private boolean isCard = false;
    private PaymentVipCardDisplayCardsFragment paymentVipCardDisplayCardsFragment;

    /**
     * 订单GUID
     */
    private String mSalesOrderGUID;

    /**
     * 待支付金额
     */
    private double mUnpaidTotal;

    /**
     * 会员手机号
     */
    private String mRegTel;

    /**
     * 订单已登录的会员实体
     */
    private MemberInfoE mMemberInfoE;

    /**
     * 选卡 会员标识
     */
    private String mMemberInfoGUID;

    /**
     * 回调
     */
    private OnPaymentListener mOnPaymentListener;

    /**
     * 回调接口定义
     */
    public interface OnPaymentListener {
        void onPaymentHappenedAndCheckoutUnfinishedInnerVipCard();

        void onCheckoutFinishedInnerVipCard();

        void setPage();
    }

    public static PaymentVipCardFragment newInstance(String salesOrderGUID, double updaidTotal, MemberInfoE memberInfoE) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_UNPAID_TOTAL, updaidTotal);
        args.putParcelable(EXTRA_MEMBER_INFO, memberInfoE);
        PaymentVipCardFragment fragment = new PaymentVipCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentListener) {
            mOnPaymentListener = (OnPaymentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnPaymentListener != null) {
            mOnPaymentListener = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRegTel = savedInstanceState.getString(SAVED_INSTANCE_REG_TEL);
        }
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
        mUnpaidTotal = extras.getDouble(EXTRA_UNPAID_TOTAL);
        mMemberInfoE = extras.getParcelable(EXTRA_MEMBER_INFO);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_vip_card;
    }

    @Override
    protected PaymentVipCardContract.Presenter initPresenter() {
        return new PaymentVipCardPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoE != null) {
            mMemberInfoGUID = mMemberInfoE.getMemberInfoGUID();
            mRegTel = mMemberInfoE.getRegTel();
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoGUID != null || mRegTel != null) {
            // "订单上存在会员信息时"或"状态保存的RegTel存在时"：默认登录会员并显示会员卡列表fragment
            mPresenter.requestLoginAndGetCardsByMember(mSalesOrderGUID, mRegTel, mMemberInfoGUID);
        } else {
            getChildFragmentManager().beginTransaction()
                    //暂时保存 以防以后又需要添加登录功能
                    .replace(R.id.fl_fragment_container, PaymentVipCardChooseCardFragment.newInstance())
//                    .replace(R.id.fl_fragment_container, PaymentMemberNoLoginFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_INSTANCE_REG_TEL, mRegTel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PAYMENT_VC_COLLECTION:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    boolean checkOutDone = extras.getBoolean(PaymentVipCardCollectionActivity.RESULT_CHECK_OUT_OR_NOT, false);
                    if (checkOutDone) {
                        if (mOnPaymentListener != null) {
                            mOnPaymentListener.onCheckoutFinishedInnerVipCard();
                        }
                    } else {
                        if (mOnPaymentListener != null) {
                            mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerVipCard();
                        }
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // no need notice
//                    showMessage("已取消收款");
                    if (isCard && paymentVipCardDisplayCardsFragment != null) {
                        if (paymentVipCardDisplayCardsFragment.getCardList().size() < 2) {
                            mOnPaymentListener.setPage();
                        }
                    }
                } else if (resultCode == RESULT_PAYMENT_BY_OTHER_WAY) {
                    if (mOnPaymentListener != null && paymentVipCardDisplayCardsFragment.getCardList().size() < 2) {
                        mOnPaymentListener.setPage();
                    }
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 请求使用卡支付折扣详情失败
     */
    @Override
    public void readDiscountDetailsFailed() {
        showMessage("折扣信息读取失败，请重试");
    }

    /**
     * 请求使用卡支付折扣详情成功
     */
    @Override
    public void readDiscountDetailsSuccess(CardsE cardsE) {
        cardsE.setRegTel(mRegTel);
        cardsE.setCardsChipNo(cardsE.getCardsChipNo());
        startActivityForResult(PaymentVipCardsActivity.newIntent(mBaseActivity, mSalesOrderGUID, cardsE), REQUEST_CODE_PAYMENT_VC_COLLECTION);
    }


    /*********************view callback begin******************************/

    @Override
    public void onLoginAndGetCardsSuccess(List<CardsE> arrayOfCardsE) {
        // 切换到content布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        // 加载PaymentVipCardDisplayCardsFragment
        paymentVipCardDisplayCardsFragment = PaymentVipCardDisplayCardsFragment.newInstance(arrayOfCardsE);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment_container, paymentVipCardDisplayCardsFragment)
                .commit();
        ((PaymentActivity) getActivity()).setLoginMemberSuccess();
    }

    @Override
    public void onLoginAndGetCardsFailed() {
        // 注册号码置空
        mRegTel = null;
        // 默认显示choose card
        Fragment fragmentById = getChildFragmentManager().findFragmentById(R.id.fl_fragment_container);
        if (fragmentById == null) {
            PaymentVipCardChooseCardFragment paymentVipCardChooseCardFragment = PaymentVipCardChooseCardFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fl_fragment_container, paymentVipCardChooseCardFragment)
                    .commit();
        }
    }

    @Override
    public void onNetworkError() {
        // 注册号码置空
        mRegTel = null;
        // 切换到error布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }

    /*********************view callback end******************************/

    /*********************child fragment callback begin******************************/

    @Override
    public void onLoginClick(String regTel) {
        mRegTel = regTel;
        mPresenter.requestLoginAndGetCardsByMember(mSalesOrderGUID, mRegTel, mMemberInfoGUID);
    }

    @Override
    public void onConfirmClick(CardsE cardsE) {
//        startActivityForResult(PaymentVipCardCollectionActivity.newIntent(mBaseActivity, mSalesOrderGUID, mUnpaidTotal, mRegTel, cardsE), REQUEST_CODE_PAYMENT_VC_COLLECTION);

//        //请求会员卡折扣详情
        cardsE.setPayableAmount(mUnpaidTotal);
        mPresenter.requestCardDiscountDetails(cardsE);
    }


    /*********************child fragment callback end******************************/

    /*********************public  method begin*************************/
    /*********************public method end*************************/
}
