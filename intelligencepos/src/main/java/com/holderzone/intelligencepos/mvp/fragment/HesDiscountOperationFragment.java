package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.activity.BalanceAccountsDiscountChangePaymentActivity;
import com.holderzone.intelligencepos.mvp.activity.BalanceAccountsDiscountWholeOrderActivity;
import com.holderzone.intelligencepos.mvp.activity.SingleDishesDiscountActivity;
import com.holderzone.intelligencepos.mvp.balancehes.activity.HesMemberCardActivity;
import com.holderzone.intelligencepos.mvp.balancehes.activity.HesMemberCouponActivity;
import com.holderzone.intelligencepos.mvp.balancehes.activity.HesMemberRedPackageActivity;
import com.holderzone.intelligencepos.mvp.contract.DiscountOperationContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDiscountE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.DiscountOperationPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.holderzone.intelligencepos.mvp.activity.BalanceAccountsActivity.RED_PACKAGE_REQUEST_CODE;

/**
 * 何师专用折扣操作fragment
 * Created by www on 2018/8/21.
 */

public class HesDiscountOperationFragment extends BaseFragment<DiscountOperationContract.Presenter> implements DiscountOperationContract.View {
    private static final int ORDER_DISCOUNT_REQUEST_CODE = 0x4;
    private static final int CHANGE_ORDER_PRICE_REQUEST_CODE = 0x5;
    private static final int COUPON_REQUEST_CODE = 0x6;
    private static final int CARD_REQUEST_CODE = 0x7;
    private static final int AWAY_DISHES_CODE = 0x9;
    private static final int SINGLE_DISHES_DISCOUNT_REQUEST_CODE = 0x10;


    public static final String KEY_DISCOUNT_ARRAY_LIST = "KEY_DISCOUNT_ARRAY_LIST";
    public static final String KEY_SALES_ORDER = "KEY_SALES_ORDER";
    public static final String KEY_SALES_ORDER_GUID = "KEY_SALES_ORDER_GUID";
    public static final String KEY_ORDER_DISHES = "KEY_ORDER_DISHES";
    public static final String IS_AUTO_CHECK = "IS_AUTO_CHECK";

    @BindView(R.id.vipCardDiscountText)
    TextView mVipCardDiscountText;
    @BindView(R.id.vipCardDiscountImage)
    ImageView mVipCardDiscountImage;
    @BindView(R.id.couponDiscountText)
    TextView mCouponDiscountText;
    @BindView(R.id.couponDiscountImage)
    ImageView mCouponDiscountImage;
    @BindView(R.id.orderDiscountText)
    TextView orderDiscountText;
    @BindView(R.id.orderDiscountImage)
    ImageView orderDiscountImage;
    @BindView(R.id.changePriceTextView)
    TextView mChangePriceTextView;
    @BindView(R.id.changePriceImage)
    ImageView mChangePriceImage;
    @BindView(R.id.dishesDiscountText)
    TextView dishesDiscountText;
    @BindView(R.id.dishesDiscountImage)
    ImageView dishesDiscountImage;
    @BindView(R.id.iv_red_envelopes)
    ImageView iv_red_envelopes;
    @BindView(R.id.tv_red_envelopes)
    TextView tv_red_envelopes;


    private List<SalesOrderDiscountE> salesOrderDiscountES;
    private SalesOrderE mOrder;
    private String mSalesOrderGuid;
    private boolean isAutoCheck = false;

    public static HesDiscountOperationFragment getInstance() {
        HesDiscountOperationFragment fragment = new HesDiscountOperationFragment();
        return fragment;
    }

    //刷新页面
    @Override
    public void onDispose() {
        refreshImageMessage();
    }


    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        salesOrderDiscountES = extras.getParcelableArrayList(KEY_DISCOUNT_ARRAY_LIST);
        mOrder = extras.getParcelable(KEY_SALES_ORDER);
        mSalesOrderGuid = extras.getString(KEY_SALES_ORDER_GUID);
        isAutoCheck = extras.getBoolean(IS_AUTO_CHECK);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.hes_fragment_discount_operation;
    }

    @Override
    protected DiscountOperationContract.Presenter initPresenter() {
        return new DiscountOperationPresenter(this);
    }


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        refreshImageMessage();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.vipCardDiscountPanel, R.id.couponDiscountPanel, R.id.orderDiscountPanel, R.id.updateOrderPrice, R.id.dishesDiscountPanel, R.id.ll_red_envelopes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.vipCardDiscountPanel:
                if (mOrder != null && mOrder.getMemberInfoE() != null) {
                    if (!isAutoCheck) {
                        if (getActivity() != null) {
                            getActivity().startActivityForResult(HesMemberCardActivity.newInstance(getContext(), mOrder.getMemberInfoE(), mSalesOrderGuid), CARD_REQUEST_CODE);
                        }
                    } else {
                        showAutoCheckDialog();
                    }
                } else {
                    showMessage("请登录会员");
                }
                break;
            case R.id.couponDiscountPanel:
                if (mOrder != null && mOrder.getMemberInfoE() != null) {
                    if (!isAutoCheck) {
                        if (getActivity() != null) {
                            getActivity().startActivityForResult(HesMemberCouponActivity.newInstance(getContext(), mSalesOrderGuid, mOrder.getMemberInfoE()), COUPON_REQUEST_CODE);
                        }
                    } else {
                        showAutoCheckDialog();
                    }
                } else {
                    showMessage("请登录会员");
                }
                break;
            case R.id.ll_red_envelopes:
                if (mOrder != null && mOrder.getMemberInfoE() != null) {
                    if (!isAutoCheck) {
                        if (getActivity() != null) {
                            getActivity().startActivityForResult(HesMemberRedPackageActivity.newInstance(getContext(), mOrder.getMemberInfoE(), mSalesOrderGuid), RED_PACKAGE_REQUEST_CODE);
                        }
                    } else {
                        showAutoCheckDialog();
                    }
                } else {
                    showMessage("请登录会员");
                }
                break;
            case R.id.orderDiscountPanel:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_SALES_ORDER_DISCOUNT,
                        () -> {
                            if (getActivity() != null) {
                                getActivity().startActivityForResult(BalanceAccountsDiscountWholeOrderActivity.newIntent(getContext(), mSalesOrderGuid, mOrder.getDiscountRatio()), ORDER_DISCOUNT_REQUEST_CODE);
                            }
                        });
                break;
            case R.id.updateOrderPrice:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_CHANGE_PRICE,
                        () -> {
                            if (getActivity() != null) {
                                getActivity().startActivityForResult(BalanceAccountsDiscountChangePaymentActivity.newIntent(getContext(), mSalesOrderGuid, mOrder.getCheckTotal()), CHANGE_ORDER_PRICE_REQUEST_CODE);
                            }
                        });
                break;
            case R.id.dishesDiscountPanel:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_GIFT_DISHES,
                        () -> {
                            if (getActivity() != null) {
                                getActivity().startActivityForResult(SingleDishesDiscountActivity.newIntent(getContext(), mSalesOrderGuid), SINGLE_DISHES_DISCOUNT_REQUEST_CODE);
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void showAutoCheckDialog() {
        mDialogFactory.showConfirmDialog("已自动计算折扣！", false, "", R.drawable.selector_button_red, true, "确定", R.drawable.selector_button_blue, new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
            }

            @Override
            public void onPosClick() {

            }
        });
    }

    /**
     * 刷新折扣信息 判断是否显示图片
     */
    public void refreshImageMessage() {
        String countStr = "";
        double couponDiscount = 0;
        for (SalesOrderDiscountE salesOrderDiscountE : salesOrderDiscountES) {
            if (salesOrderDiscountE.getDiscountType() == 10) {//整单折扣
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount()));
                    orderDiscountImage.setVisibility(View.INVISIBLE);
                    orderDiscountText.setVisibility(View.VISIBLE);
                } else {
                    countStr = "";
                    orderDiscountImage.setVisibility(View.VISIBLE);
                    orderDiscountText.setVisibility(View.INVISIBLE);
                }
                orderDiscountText.setText(countStr);
            }
            if (salesOrderDiscountE.getDiscountType() == 11) {//会员卡权益折扣
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount()));
                    mVipCardDiscountImage.setVisibility(View.INVISIBLE);
                    mVipCardDiscountText.setVisibility(View.VISIBLE);
                } else {
                    countStr = "";
                    mVipCardDiscountImage.setVisibility(View.VISIBLE);
                    mVipCardDiscountText.setVisibility(View.INVISIBLE);
                }
                mVipCardDiscountText.setText(countStr);
            }
            if (salesOrderDiscountE.getDiscountType() == 103) {//Hes优惠券
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    couponDiscount = ArithUtil.add(couponDiscount, salesOrderDiscountE.getDiscountAmount());
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(couponDiscount));
                    mCouponDiscountImage.setVisibility(View.INVISIBLE);
                    mCouponDiscountText.setVisibility(View.VISIBLE);
                } else {
                    countStr = "";
                    mCouponDiscountImage.setVisibility(View.VISIBLE);
                    mCouponDiscountText.setVisibility(View.INVISIBLE);
                }
                mCouponDiscountText.setText(countStr);
            }
            if (salesOrderDiscountE.getDiscountType() == 50) {//省零（人工）
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount()));
                    mChangePriceImage.setVisibility(View.INVISIBLE);
                    mChangePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    countStr = "";
                    mChangePriceImage.setVisibility(View.VISIBLE);
                    mChangePriceTextView.setVisibility(View.INVISIBLE);
                }
                mChangePriceTextView.setText(countStr);
            }

            if (salesOrderDiscountE.getDiscountType() == 80) {//单品折扣
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount()));
                    dishesDiscountImage.setVisibility(View.INVISIBLE);
                    dishesDiscountText.setVisibility(View.VISIBLE);
                } else {
                    countStr = "";
                    dishesDiscountImage.setVisibility(View.VISIBLE);
                    dishesDiscountText.setVisibility(View.INVISIBLE);
                }
                dishesDiscountText.setText(countStr);
            }

            if (salesOrderDiscountE.getDiscountType() == 102) {//Hes红包
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount()));
                    iv_red_envelopes.setVisibility(View.INVISIBLE);
                    tv_red_envelopes.setVisibility(View.VISIBLE);
                } else {
                    countStr = "";
                    iv_red_envelopes.setVisibility(View.VISIBLE);
                    tv_red_envelopes.setVisibility(View.INVISIBLE);
                }
                tv_red_envelopes.setText(countStr);
            }
        }
    }
}
