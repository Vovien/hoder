package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsContract;
import com.holderzone.intelligencepos.mvp.fragment.DiscountDetailsFragment;
import com.holderzone.intelligencepos.mvp.fragment.DiscountOperationFragment;
import com.holderzone.intelligencepos.mvp.fragment.HesDiscountOperationFragment;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.TableAdditionalFree;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算页面
 * Created by zhaoping on 2017/5/27.
 */

public class BalanceAccountsActivity extends BaseActivity<BalanceAccountsContract.Presenter> implements BalanceAccountsContract.View {

    public static String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";
    public static String EXTRAS_SALES_ORDER_SHOW_TAG = "EXTRAS_SALES_ORDER_SHOW_TAG";
    private static final String KEY_HES_USE_MEMBER_CARD = "KEY_HES_USE_MEMBER_CARD";
    private static final String KEY_HAS_SUCCESS_USE_ALL_CARDS = "KEY_HAS_SUCCESS_USE_ALL_CARDS";
    private static final String KEY_HES_COUPON = "KEY_HES_COUPON";
    private static final String KEY_HAS_SUCCESS_USE_ALL_COUPON = "KEY_HAS_SUCCESS_USE_ALL_COUPON";
    public static final String KEY_USE_RED_PACKAGE_NUMBER = "KEY_USE_RED_PACKAGE_NUMBER";
    public static final String KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE = "KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE";

    private static final int PAYMENT_REQUEST_CODE = 0x0;
    private static final int MEMBER_LOGIN_REQUEST_CODE = 0x1;
    private static final int ADDITIONAL_FREES_DISCOUNT_REQUEST_CODE = 0x2;
    private static final int DISCOUNT_REQUEST_CODE = 0x3;

    private static final int ORDER_DISCOUNT_REQUEST_CODE = 0x4;
    private static final int CHANGE_ORDER_PRICE_REQUEST_CODE = 0x5;
    private static final int COUPON_REQUEST_CODE = 0x6;
    private static final int CARD_REQUEST_CODE = 0x7;
    private static final int MEMBER_SHIP_INTEGRAL = 0x8;
    private static final int AWAY_DISHES_CODE = 0x9;
    private static final int SINGLE_DISHES_DISCOUNT_REQUEST_CODE = 0x10;
    public static final int RED_PACKAGE_REQUEST_CODE = 0x11;

    @BindView(R.id.memberLogin)
    TextView memberLogin;
    @BindView(R.id.tableNameTextView)
    TextView tableNameTextView;
    @BindView(R.id.memberTelTextView)
    TextView memberTelTextView;
    @BindView(R.id.goodsPriceTotalTextView)
    TextView goodsPriceTotalTextView;
    @BindView(R.id.discountTotalTextView)
    TextView discountTotalTextView;
    @BindView(R.id.additionalFeesPriceTextView)
    TextView additionalFeesPriceTextView;
    @BindView(R.id.memberNameTextView)
    TextView memberNameTextView;
    @BindView(R.id.payButton)
    Button payButton;
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.memberPanel)
    View memberPanel;
    @BindView(R.id.content)
    MultiStateView content;
    @BindView(R.id.payButton_left)
    Button payButtonLeft;
    @BindView(R.id.discountContextPanel)
    LinearLayout discountContextPanel;
    @BindView(R.id.discountDetailContextPanel)
    LinearLayout discountDetailContextPanel;
    @BindView(R.id.discountDetailTextView)
    TextView discountDetailTextView;
    @BindView(R.id.discountDetailPanel)
    RelativeLayout discountDetailPanel;
    @BindView(R.id.additionalDiscountPanel)
    RelativeLayout additionalDiscountPanel;
    @BindView(R.id.hes_memberGrade)
    TextView hes_memberGrade;
    @BindView(R.id.hes_memberIntegral)
    TextView hes_memberIntegral;

    SalesOrderE mOrder;
    String mSalesOrderGuid;
    private ArrayList<OrderDishesGroup> mDishesList = new ArrayList<>();

    /**
     * 上个页面出来的标记 标记底部按钮的显示方式
     * 0:桌台点餐页面
     * 1：快销页面（新点单）
     * 2：快销页面（旧账单）
     */
    private int mTag;
    /**
     * 按桌台分类附加费
     */
    ArrayList<TableAdditionalFree> mAdditionalFreeList = new ArrayList<>();
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager mFragmentManager;
    /**
     * 是否是何师登录
     */
    private boolean isHesMember = false;
    /**
     * 是否是自动核算
     */
    private boolean isAutoCheck = false;
    FragmentTransaction operationTra;
    FragmentTransaction detailTra;
    //折扣操作
    DiscountOperationFragment discountOperationFragment;
    //折扣操作Hes版本
    HesDiscountOperationFragment hesDiscountOperationFragment;
    //折扣明细
    DiscountDetailsFragment discountDetailsFragment;
    //默认展开折扣
    private boolean isOpenDiscount = true;
    //默认关闭折扣明细
    private boolean isOpenDiscountDetial = true;

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID);
        mTag = extras.getInt(EXTRAS_SALES_ORDER_SHOW_TAG);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts;
    }

    @Override
    protected BalanceAccountsContract.Presenter initPresenter() {
        return new BalanceAccountsPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        Drawable drawableRight = getResources().getDrawable(
                R.drawable.more_arrow_down);
        discountTotalTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                null, drawableRight, null);
        discountTotalTextView.setCompoundDrawablePadding(16);
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                if (1 == mTag) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finishActivityByVersion();
            }
        });
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getOrderInfo(mSalesOrderGuid);
            }
        });
        //底部按钮的显示控制
        switch (mTag) {
            case 0:
                //桌台点餐页面
                payButtonLeft.setVisibility(View.GONE);
                break;
            case 1:
                //快销页面（新点单）
                payButtonLeft.setVisibility(View.VISIBLE);
                payButtonLeft.setText("暂不结账");
                break;
            case 2:
                //快销页面（旧账单）
                payButtonLeft.setVisibility(View.VISIBLE);
                payButtonLeft.setText("作废账单");
                break;
            case 3:
                //堂食打印结账单
                payButtonLeft.setVisibility(View.VISIBLE);
                payButtonLeft.setText("预结单");
                break;
            default:
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //获取是否是何师版
        mPresenter.getHesMember();
        mPresenter.getOrderInfo(mSalesOrderGuid);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onGetOrderDisheSuccess(boolean isAutoCalc, List<OrderDishesGroup> list, List<SalesOrderE> salesOrderEList) {
        this.isAutoCheck = isAutoCalc;
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        SalesOrderE mainOrder = null;
        mAdditionalFreeList.clear();
        if (salesOrderEList.size() == 1) {
            mainOrder = salesOrderEList.get(0);
        } else {
            for (SalesOrderE salesOrderE : salesOrderEList) {
                TableAdditionalFree free = new TableAdditionalFree();
                free.setTableName(salesOrderE.getDiningTableE().getName());
                free.setTotal(salesOrderE.getDtAdditionalFeesTotal());
                free.setSalesOrderGuid(salesOrderE.getSalesOrderGUID());
                if (salesOrderE.getUpperState() == 1) {
                    mainOrder = salesOrderE;
                }
                mAdditionalFreeList.add(free);
            }
        }
        title.setTitleText("结算：" + mainOrder.getSerialNumber());
        mOrder = mainOrder;
        mDishesList.clear();
        mDishesList.addAll(list);
        if (mainOrder.getDiscountTotal() == 0) {
            discountDetailContextPanel.setVisibility(View.GONE);
            if (mainOrder.getDiscountTotal() == null || mainOrder.getDiscountTotal() == 0) {
                discountTotalTextView.setText("");
            } else {
                discountTotalTextView.setText(getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(mainOrder.getDiscountTotal())));
            }
            Drawable drawableRight = getResources().getDrawable(
                    R.drawable.more_arrow);
            discountDetailTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
            discountDetailTextView.setCompoundDrawablePadding(16);
            isOpenDiscountDetial = false;
        } else {
            discountDetailContextPanel.setVisibility(View.VISIBLE);
            if (mainOrder.getDiscountTotal() == null || mainOrder.getDiscountTotal() == 0) {
                discountTotalTextView.setText("");
            } else {
                discountTotalTextView.setText(getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(mainOrder.getDiscountTotal())));
            }
            Drawable drawableRight = getResources().getDrawable(
                    R.drawable.more_arrow_down);
            discountDetailTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
            discountDetailTextView.setCompoundDrawablePadding(16);
            isOpenDiscountDetial = true;
        }
        if (0 == mainOrder.getAdditionalFeesTotal()) {
            additionalFeesPriceTextView.setText("");
        } else {
            additionalFeesPriceTextView.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mainOrder.getAdditionalFeesTotal())));
        }
        goodsPriceTotalTextView.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mainOrder.getDishesConsumeTotal())));
        int count = 0;
        for (OrderDishesGroup group : list) {
            count += group.getSalesOrderDishesEList().size();
        }
        CharSequence charSequence = new SpanUtils()
                .append("商品共")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_000000))
                .append(String.valueOf(count))
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_01b6ad))
                .append("项")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_000000))
                .create();
        tableNameTextView.setText(charSequence);
//        dishesCountTextView.setText((count > 1 ? "等" : "") + "共");
//        TextUtils.appentTextViewText(dishesCountTextView, count + "", R.color.tv_text_green, null);
//        TextUtils.appentTextViewText(dishesCountTextView, "项", null, null);
        setMemberInfo();
        if (isHesMember) {
            operationTra = mFragmentManager.beginTransaction();
            hesDiscountOperationFragment = new HesDiscountOperationFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(HesDiscountOperationFragment.KEY_DISCOUNT_ARRAY_LIST, (ArrayList<? extends Parcelable>) mOrder.getArrayOfSalesOrderDiscountE());
            bundle.putParcelable(HesDiscountOperationFragment.KEY_SALES_ORDER, mOrder);
            bundle.putString(HesDiscountOperationFragment.KEY_SALES_ORDER_GUID, mSalesOrderGuid);
            bundle.putParcelableArrayList(HesDiscountOperationFragment.KEY_ORDER_DISHES, mDishesList);
            bundle.putBoolean(HesDiscountOperationFragment.IS_AUTO_CHECK, isAutoCheck);
            hesDiscountOperationFragment.setArguments(bundle);
            operationTra.replace(R.id.discountContextPanel, hesDiscountOperationFragment).show(hesDiscountOperationFragment).commitAllowingStateLoss();
        } else {
            operationTra = mFragmentManager.beginTransaction();
            discountOperationFragment = new DiscountOperationFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(DiscountOperationFragment.KEY_DISCOUNT_ARRAY_LIST, (ArrayList<? extends Parcelable>) mOrder.getArrayOfSalesOrderDiscountE());
            bundle.putParcelable(DiscountOperationFragment.KEY_SALES_ORDER, mOrder);
            bundle.putString(DiscountOperationFragment.KEY_SALES_ORDER_GUID, mSalesOrderGuid);
            bundle.putParcelableArrayList(DiscountOperationFragment.KEY_ORDER_DISHES, mDishesList);
            discountOperationFragment.setArguments(bundle);
            operationTra.replace(R.id.discountContextPanel, discountOperationFragment).show(discountOperationFragment).commitAllowingStateLoss();
        }

        if (mainOrder.getArrayOfSalesOrderDiscountE() == null || mainOrder.getArrayOfSalesOrderDiscountE().size() == 0) {
            discountDetailPanel.setVisibility(View.GONE);
        } else {
            discountDetailPanel.setVisibility(View.VISIBLE);
            detailTra = mFragmentManager.beginTransaction();
            discountDetailsFragment = DiscountDetailsFragment.getInstance(mainOrder.getArrayOfSalesOrderDiscountE());
            detailTra.replace(R.id.discountDetailContextPanel, discountDetailsFragment).show(discountDetailsFragment).commitAllowingStateLoss();
        }

        payButton.setText(getString(R.string.balance_account_pay_text, ArithUtil.stripTrailingZeros(mainOrder.getCheckTotal())));
    }

    public void setMemberInfo() {
        if (mOrder.getMemberInfoE() != null) {
            memberPanel.setVisibility(View.VISIBLE);
            memberNameTextView.setText(mOrder.getMemberInfoE().getNickName());
            memberLogin.setVisibility(View.GONE);
            //会员信息处理
            if (isHesMember) {
                memberTelTextView.setVisibility(View.GONE);
                hes_memberGrade.setVisibility(View.VISIBLE);
                hes_memberIntegral.setVisibility(View.VISIBLE);
                hes_memberGrade.setText(mOrder.getMemberInfoE().getLevel() == null ? "等级：--" : "等级：" + mOrder.getMemberInfoE().getLevel());
                hes_memberIntegral.setText(mOrder.getMemberInfoE().getIntegral() == null ? "积分：--" : "积分：" + mOrder.getMemberInfoE().getIntegral() + "");
            } else {
                memberTelTextView.setText(mOrder.getMemberInfoE().getRegTel());
                memberTelTextView.setVisibility(View.VISIBLE);
                hes_memberGrade.setVisibility(View.GONE);
                hes_memberIntegral.setVisibility(View.GONE);
            }
        } else {
            memberLogin.setVisibility(View.VISIBLE);
            memberPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMemberLoginOutSuccess() {
        mPresenter.getOrderInfo(mSalesOrderGuid);
    }

    @Override
    public void showNetworkErrorLayout() {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void invalidSuccess() {
        //作废账单成功
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void invalidFiled(int code) {
        if (code == 0) {
            //未退款 账单作废失败
            mDialogFactory.showConfirmDialog("该账单已收款，请先进行退款操作。", false, ""
                    , R.color.additional_frees_count_select
                    , true, "确定", R.color.btn_bg_light_blue_normal
                    , new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {
                        }

                        @Override
                        public void onPosClick() {
                        }
                    });
        }
    }

    @Override
    public void onCheckPrintSuccess() {
    }

    @Override
    public void onGetHesMember(Boolean hesMember) {
        isHesMember = hesMember;
//        additionalDiscountPanel.setVisibility(isHesMember ? View.GONE : View.VISIBLE);
    }

    public static Intent newIntent(Context context, String orderGuid, int showTag) {
        Intent intent = new Intent(context, BalanceAccountsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, orderGuid);
        bundle.putInt(EXTRAS_SALES_ORDER_SHOW_TAG, showTag);
        intent.putExtras(bundle);
        return intent;
    }

    @OnClick({R.id.dishesPanel, R.id.memberPanel, R.id.additionalDiscountPanel, R.id.payButton, R.id.discountPanel, R.id.memberLogin, R.id.payButton_left, R.id.discountDetailPanel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discountDetailPanel:
                if (!isOpenDiscountDetial) {
                    discountDetailContextPanel.setVisibility(View.VISIBLE);
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.more_arrow_down);
                    discountDetailTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    discountDetailTextView.setCompoundDrawablePadding(16);
                    isOpenDiscountDetial = true;
                } else {
                    discountDetailContextPanel.setVisibility(View.GONE);
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.more_arrow);
                    discountDetailTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    discountDetailTextView.setCompoundDrawablePadding(16);
                    isOpenDiscountDetial = false;
                }
                break;
            case R.id.discountPanel:
                if (!isOpenDiscount) {
                    discountContextPanel.setVisibility(View.VISIBLE);
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.more_arrow_down);
                    discountTotalTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    discountTotalTextView.setCompoundDrawablePadding(16);
                    isOpenDiscount = true;
                } else {
                    discountContextPanel.setVisibility(View.GONE);
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.more_arrow);
                    discountTotalTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    discountTotalTextView.setCompoundDrawablePadding(16);
                    isOpenDiscount = false;
                }
//                startActivityForResult(BalanceAccountsDiscountActivity.newIntent(getApplicationContext(), mOrder, mDishesList), DISCOUNT_REQUEST_CODE);
                break;
            case R.id.dishesPanel:
                startActivity(BalanceAccountsAllDishesActivity.newIntent(getApplicationContext(), mDishesList, mOrder.getDishesConsumeTotal(), mOrder.getSalesOrderGUID(), mTag));
                break;
            case R.id.memberLogin:
                startActivityForResult(BalanceAccountsMemberLoginActivity.newIntent(getApplicationContext(), mSalesOrderGuid), MEMBER_LOGIN_REQUEST_CODE);
                break;
            case R.id.memberPanel:
                mDialogFactory.showConfirmDialog(getString(R.string.exit_member_confirm_content), getString(R.string.cancel), getString(R.string.confirm_logout), new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {
                    }

                    @Override
                    public void onPosClick() {
                        mPresenter.memberLoginOut(mSalesOrderGuid);
                    }
                });
                break;
            case R.id.additionalDiscountPanel:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_ADDITION_FEE,
                        () -> startActivityForResult(BalanceAccountsAdditionalFreesActivity.newIntent(getApplicationContext(), mSalesOrderGuid, mAdditionalFreeList), ADDITIONAL_FREES_DISCOUNT_REQUEST_CODE));
                break;
            case R.id.payButton:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_JIE_ZHANG,
                        () -> startActivityForResult(PaymentActivity.newIntent(BalanceAccountsActivity.this, mSalesOrderGuid), PAYMENT_REQUEST_CODE));
                break;
            case R.id.payButton_left:
                switch (mTag) {
                    case 1:// 暂不结账
                        setResult(RESULT_OK);
                        finishActivity();
                        break;
                    case 2:// 作废账单
                        PermissionManager.checkPermission(PermissionManager.PERMISSION_DISABLE_SALES_ORDER,
                                () -> mDialogFactory.showConfirmDialog(getString(R.string.invalid_order_content), getString(R.string.cancel), getString(R.string.confirm_invalid), new ConfirmDialogFragment.ConfirmDialogListener() {
                                    @Override
                                    public void onNegClick() {
                                    }

                                    @Override
                                    public void onPosClick() {
                                        mPresenter.requestInvalid(mSalesOrderGuid);
                                    }
                                }));
                        break;
                    case 3:// 打印预结单
                        mPresenter.checkPrint(mSalesOrderGuid);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finishActivity();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null && (data.getExtras().getBoolean(PaymentActivity.RESULT_PAY_OR_REFUND_HAPPEN, false) || data.getExtras().getBoolean(PaymentActivity.RESULT_LOGIN_MEMBER, false))) {
//                    refreshOrderInfo();
                    mPresenter.getOrderInfo(mSalesOrderGuid);
                }
            }
        } else if (requestCode == MEMBER_LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == DISCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == ADDITIONAL_FREES_DISCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == ORDER_DISCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == CHANGE_ORDER_PRICE_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == COUPON_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                boolean isShow = bundle.getBoolean(KEY_HAS_SUCCESS_USE_ALL_COUPON, false);
                if (!isShow && bundle.containsKey(KEY_HAS_SUCCESS_USE_ALL_COUPON)) {
                    String message = bundle.getString(KEY_HES_COUPON, "");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            showCoupon(message);
                        }
                    });
                }
            }
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == CARD_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                boolean isShow = bundle.getBoolean(KEY_HAS_SUCCESS_USE_ALL_CARDS, false);
                if (!isShow && bundle.containsKey(KEY_HAS_SUCCESS_USE_ALL_CARDS)) {
                    String message = bundle.getString(KEY_HES_USE_MEMBER_CARD, "");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            showVipCard(message);
                        }
                    });
                }
            }
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == MEMBER_SHIP_INTEGRAL && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == AWAY_DISHES_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == SINGLE_DISHES_DISCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getOrderInfo(mSalesOrderGuid);
        } else if (requestCode == RED_PACKAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                boolean isShow = bundle.getBoolean(KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE, false);
                if (!isShow && bundle.containsKey(KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE)) {
                    String message = bundle.getString(KEY_USE_RED_PACKAGE_NUMBER, "");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            showRedenvelope(message);
                        }
                    });
                }
            }
            mPresenter.getOrderInfo(mSalesOrderGuid);
        }
    }

    private void showVipCard(String message) {
        showDialog("已选用其中" + message + "张会员卡。");
    }

    private void showCoupon(String message) {
        showDialog("已选用其中" + message + "张优惠券。");
    }

    private void showRedenvelope(String message) {
        showDialog("已选用其中" + message + "个红包。");
    }

    private void showDialog(String message) {
        mDialogFactory.showConfirmDialog(message, false, "", R.drawable.selector_button_red, true, "确定", R.drawable.selector_button_blue, new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
            }

            @Override
            public void onPosClick() {

            }
        });
    }

    //Hes版本判断是否添加弹出框
    private void finishActivityByVersion() {
        if (isHesMember) {
            mDialogFactory.showConfirmDialog(getString(R.string.back_member_confirm_content), getString(R.string.cancel), getString(R.string.confirm_back), new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {
                }

                @Override
                public void onPosClick() {
                    finishActivity();
                }
            });
        } else {
            finishActivity();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (1 == mTag) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
            finishActivityByVersion();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDispose() {
    }
}
