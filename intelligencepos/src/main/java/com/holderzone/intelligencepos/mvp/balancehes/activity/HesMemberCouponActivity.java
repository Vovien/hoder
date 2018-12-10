package com.holderzone.intelligencepos.mvp.balancehes.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.balancehes.contract.HesMemberCouponContract;
import com.holderzone.intelligencepos.mvp.balancehes.presenter.HesMemberCouponPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.CashCouponModel;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class HesMemberCouponActivity extends BaseActivity<HesMemberCouponContract.Presenter> implements HesMemberCouponContract.View {
    private static final String KEY_SAALESORDERGUID = "KEY_SAALESORDERGUID";
    private static final String KEY_MEMBER_INFO = "KEY_MEMBER_INFO";
    /**
     * 成功使用了多少张优惠券
     */
    public static final String KEY_HES_COUPON = "KEY_HES_COUPON";
    /**
     * 是否成功使用所有优惠券
     */
    public static final String KEY_HAS_SUCCESS_USE_ALL_COUPON = "KEY_HAS_SUCCESS_USE_ALL_COUPON";

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_return)
    LinearLayout llReturn;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.rl_layout_title)
    RelativeLayout rlLayoutTitle;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.msv_context)
    MultiStateView msvContext;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty_text)
    TextView tvEmpty;
    @BindView(R.id.btn_retry)
    Button btnRetry;

    private CommonAdapter<CashCouponModel> mCommonAdapter;
    private List<CashCouponModel> mCashCouponModelList = new ArrayList<>();
    private List<String> mCheckedCouponCord = new ArrayList<>();

    private String mSalesOrderGuid;
    private MemberInfoE memberInfoE;

    public static Intent newInstance(Context context, String mSalesOrderGuid, MemberInfoE memberInfoE) {
        Intent intent = new Intent(context, HesMemberCouponActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SAALESORDERGUID, mSalesOrderGuid);
        bundle.putParcelable(KEY_MEMBER_INFO, memberInfoE);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGuid = extras.getString(KEY_SAALESORDERGUID);
        memberInfoE = extras.getParcelable(KEY_MEMBER_INFO);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_account_coupon_hes;
    }

    @Override
    protected HesMemberCouponContract.Presenter initPresenter() {
        return new HesMemberCouponPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvTitle.setText("优惠券");
        ivSearch.setVisibility(View.GONE);
        llMenu.setVisibility(View.GONE);
        initClickEvent();
        if (memberInfoE == null) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            ivEmpty.setVisibility(View.GONE);
            tvEmpty.setText(getString(R.string.hes_not_loading));
            return;
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_LOADING);
        }
        setButtonState();
        initRecyclerAdapter();
    }

    private void initRecyclerAdapter() {
        mCommonAdapter = new CommonAdapter<CashCouponModel>(getApplicationContext(), R.layout.hes_balance_account_coupon_item, mCashCouponModelList) {

            @Override
            protected void convert(ViewHolder holder, CashCouponModel couponModel, int position) {
                String couponType = "--";
                String couponAmount = "";
                /**
                 * 1：折扣券
                 * 2：代金券
                 * 3：兑换券
                 */
                if ("1".equalsIgnoreCase(couponModel.getType())) {
                    couponType = "折\n扣\n券";
                    couponAmount = couponModel.getDiscount() != null && couponModel.getDiscount() != 10 ? ArithUtil.stripTrailingZeros(couponModel.getDiscount()) + "折" : "无折扣";
                    holder.setBackgroundRes(R.id.tv_coupon_type, R.drawable.shap_cash_coupon);
                } else if ("2".equalsIgnoreCase(couponModel.getType())) {
                    couponType = "代\n金\n券";
                    couponAmount = couponModel.getVouchers() != null ? "￥" + ArithUtil.stripTrailingZeros(couponModel.getVouchers()) : "--";
                    holder.setBackgroundRes(R.id.tv_coupon_type, R.drawable.shap_discount_coupon);
                } else if ("3".equalsIgnoreCase(couponModel.getType())) {
                    couponType = "兑\n换\n券";
                    holder.setBackgroundRes(R.id.tv_coupon_type, R.drawable.shap_exchange_coupon);
                }
                holder.setVisible(R.id.space,position == mCashCouponModelList.size()-1);


                holder.setVisible(R.id.iv_share, !couponModel.getShare());
                holder.setText(R.id.tv_coupon_type, couponType);
                String couponName = couponModel.getTitle();
                if (couponName.length() >5) {
                    holder.setTextSize(R.id.tv_coupon_name, 16);
                } else {
                    holder.setTextSize(R.id.tv_coupon_name, 20);
                }
                holder.setText(R.id.tv_coupon_amount, couponAmount);
                holder.setText(R.id.tv_coupon_name, couponName);
                holder.setText(R.id.tv_coupon_verification_code, getString(R.string.coupon_verification_code, couponModel.getQrCode()));
                holder.setText(R.id.tv_coupon_term_validity, getString(R.string.hes_coupon_term_validity, couponModel.getStartTime(), couponModel.getEndTime()));

                if (couponModel.getSelected()) {
                    holder.setImageResource(R.id.iv_checked, R.drawable.check_checked);
                } else {
                    holder.setImageResource(R.id.iv_checked, R.drawable.check_normal);
                }

                holder.setOnClickListener(R.id.ll_item_layout, view -> {
                    String code = couponModel.getQrCode();
                    boolean couponSelected = couponModel.getSelected();
                    couponModel.setSelected(!couponSelected);
                    if (couponSelected) {
                        mCheckedCouponCord.remove(code);
                    } else {
                        mCheckedCouponCord.add(code);
                    }
                    setButtonState();
                    notifyDataSetChanged();
                });
            }
        };

        rvContent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvContent.setAdapter(mCommonAdapter);
    }

    /**
     * 设置按钮状态
     */
    private void setButtonState() {
        mCheckedCouponCord = removeDuplicate(mCheckedCouponCord);
        if (mCheckedCouponCord.size() == 0) {
            btnConfirm.setText("完成");
        } else {
            btnConfirm.setText(getString(R.string.hes_coupon, mCheckedCouponCord.size()));
        }
    }

    /**
     * list 去重
     *
     * @param list
     * @return
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }


    @SuppressLint("CheckResult")
    private void initClickEvent() {
        RxView.clicks(llReturn)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    finishActivity();
                });

        RxView.clicks(btnConfirm)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    mPresenter.requestUseMemberCoupon(mCheckedCouponCord, mSalesOrderGuid, memberInfoE.getMemberInfoGUID());
                });


        RxView.clicks(btnRetry)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    mPresenter.getMemberCouponList(mSalesOrderGuid, memberInfoE.getMemberInfoGUID());
                });
    }


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (memberInfoE != null) {
            mPresenter.getMemberCouponList(mSalesOrderGuid, memberInfoE.getMemberInfoGUID());
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void responseUseCouponSuccess() {
        finishToOperation(true,"");
    }

    @Override
    public void responseUseCouponNotAll(String number) {
        finishToOperation(false,number);
    }

    private void finishToOperation(boolean isUseAll, String number) {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_HAS_SUCCESS_USE_ALL_COUPON, isUseAll);
        bundle.putString(KEY_HES_COUPON, number);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void responseUseCouponFail() {
        mCheckedCouponCord.clear();
        mPresenter.getMemberCouponList(mSalesOrderGuid, memberInfoE.getMemberInfoGUID());
    }


    @Override
    public void responseGetCouponListSuccess(List<CashCouponModel> cashCouponModels) {
        if (cashCouponModels == null || cashCouponModels.size() == 0) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            ivEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getString(R.string.hes_not_coupon));
            return;
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        mCashCouponModelList.clear();
        mCashCouponModelList.addAll(cashCouponModels);
        for (CashCouponModel cashCouponModel : cashCouponModels) {
            if (cashCouponModel.getSelected()) {
                mCheckedCouponCord.add(cashCouponModel.getQrCode());
            }
        }
        setButtonState();
        mCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseGetCouponListFail() {
        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }
}
