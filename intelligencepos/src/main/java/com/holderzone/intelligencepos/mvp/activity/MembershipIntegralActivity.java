package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.MembershipIntegralContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.MembershipIntegralPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员积分页面
 * Created by chencao on 2018/2/2.
 */

public class MembershipIntegralActivity extends BaseActivity<MembershipIntegralContract.Presenter> implements MembershipIntegralContract.View {
    private static final String EXTRAS_MEMBER_INFO = "EXTRAS_MEMBER_INFO";
    private static final String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";

    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_return)
    LinearLayout mLlReturn;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.tv_menu)
    TextView mTvMenu;
    @BindView(R.id.ll_menu)
    LinearLayout mLlMenu;
    @BindView(R.id.sc_using)
    SwitchCompat mScUsing;
    @BindView(R.id.tv_context)
    TextView mTvContext;
    @BindView(R.id.btn_sure)
    Button mBtnSure;
    @BindView(R.id.msv_context)
    MultiStateView mMsvContext;
    @BindView(R.id.btn_retry)
    Button mBtnRetry;


    private MemberInfoE mMemberInfo;
    private String salesOrderGuid;

    /**
     * 当前账单可用积分
     */
    private int mCanUsePoint;
    /**
     * 积分可抵金额
     */
    private double mCanUseMoney;
    /**
     * 剩余积分
     */
    private int mAllPoints;
    /**
     * 是否使用积分
     */
    private boolean isUsingPoints = false;


    public static Intent newIntent(Context context, MemberInfoE mMemberInfo, String mSalesOrderGuid) {
        Intent intent = new Intent(context, MembershipIntegralActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRAS_MEMBER_INFO, mMemberInfo);
        bundle.putString(EXTRAS_SALES_ORDER_GUID, mSalesOrderGuid);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMemberInfo = extras.getParcelable(EXTRAS_MEMBER_INFO);
        salesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_membership_integral;
    }

    @Nullable
    @Override
    protected MembershipIntegralContract.Presenter initPresenter() {
        return new MembershipIntegralPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTvTitle.setText("会员积分");
        mLlMenu.setVisibility(View.GONE);
        RxView.clicks(mBtnSure)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mScUsing.isChecked()) {
                        mPresenter.requestUsePoints(salesOrderGuid, mCanUsePoint, mCanUseMoney);
                    } else {
                        mPresenter.requestCancelUse(salesOrderGuid);
                    }
                });

        RxView.clicks(mBtnRetry)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mPresenter.requestCanUsePoints(salesOrderGuid, mMemberInfo.getMemberInfoGUID());
                });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestCanUsePoints(salesOrderGuid, mMemberInfo.getMemberInfoGUID());
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.ll_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_return:
                finishActivity();
                break;
            default:
        }
    }

    @Override
    public void responseCanUsePoints(SalesOrderE salesOrderE) {
        mMsvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mCanUsePoint = salesOrderE.getUsePoints();
        mCanUseMoney = salesOrderE.getPointsAmount();
        mAllPoints = salesOrderE.getLeaguerPoint();
        int isUsePoint = 0;
        if (salesOrderE.getIsUsePoint() != null) {
            isUsePoint = salesOrderE.getIsUsePoint();
        }
        isUsingPoints = isUsePoint == 1;
        mScUsing.setChecked(isUsingPoints);

        CharSequence charSequence = new SpanUtils()
                .append("使用")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_7c7c7c))
                .setFontSize(16, true)
                .append(String.valueOf(mCanUsePoint))
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_2495ee))
                .setFontSize(19, true)
                .append("积分抵扣")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_7c7c7c))
                .setFontSize(16, true)
                .append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCanUseMoney)))
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_2495ee))
                .setFontSize(19, true)
                .append("\n当前账户积分：" + mAllPoints + (salesOrderE.getIsEnough() == 1 ? "" : " ( 积分不足 )"))
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_7c7c7c))
                .setFontSize(16, true)
                .create();
        mTvContext.setText(charSequence);
    }

    @Override
    public void resopnseUsePoints() {
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void showNetworkErrorLayout() {
        mMsvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }
}
