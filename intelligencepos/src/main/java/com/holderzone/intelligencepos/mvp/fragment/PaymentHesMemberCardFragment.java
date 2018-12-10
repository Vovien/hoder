package com.holderzone.intelligencepos.mvp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.PaymentVipCardCollectionActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentHesMemberCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.VipCardModel;
import com.holderzone.intelligencepos.mvp.presenter.PaymentHesMemberCardPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 何师会员支付
 */
public class PaymentHesMemberCardFragment extends BaseFragment<PaymentHesMemberCardContract.Presenter> implements PaymentHesMemberCardContract.View {
    private static final String EXTRA_SALES_MEMBER_INFO = "EXTRA_SALES_MEMBER_INFO";
    private static final String KEY_SALES_ORDER_GUID = "KEY_SALES_ORDER_GUID";
    private static final String EXTRA_PAY_MONEY = "EXTRA_PAY_MONEY";
    private static final String KEY_IS_HES_MEMBER = "KEY_IS_HES_MEMBER";

    private static final int REQUEST_CODE_PAYMENT_VC_COLLECTION = 0x00000013;
    @BindView(R.id.tv_member_name)
    TextView tvMemberName;
    @BindView(R.id.tv_member_number)
    TextView tvMemberNumber;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.msv_context)
    MultiStateView msvContext;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    /**
     * 登陆会员实体
     */
    private MemberInfoE mMemberInfoE;
    /**
     * 待收金额
     */
    private double mUnpaidTotal;
    private String mSalesOrderGUID;
    private boolean isHesMember = false;

    private CommonAdapter<VipCardModel> mCommonAdapter;
    private List<VipCardModel> mVipCardModelList = new ArrayList<>();

    private CallBackHesMemberPayment mCallBackHesMemberPayment;


    public static PaymentHesMemberCardFragment newInstence(String salesOrderGUID, boolean isHesMember, double updaidTotal, MemberInfoE memberInfoE) {
        Bundle args = new Bundle();
        args.putString(KEY_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_PAY_MONEY, updaidTotal);
        args.putParcelable(EXTRA_SALES_MEMBER_INFO, memberInfoE);
        args.putBoolean(KEY_IS_HES_MEMBER, isHesMember);
        PaymentHesMemberCardFragment fragment = new PaymentHesMemberCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMemberInfoE = extras.getParcelable(EXTRA_SALES_MEMBER_INFO);
        mUnpaidTotal = extras.getDouble(EXTRA_PAY_MONEY);
        mSalesOrderGUID = extras.getString(KEY_SALES_ORDER_GUID);
        isHesMember = extras.getBoolean(KEY_IS_HES_MEMBER, false);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_hes_member;
    }

    @Override
    protected PaymentHesMemberCardContract.Presenter initPresenter() {
        return new PaymentHesMemberCardPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoE != null) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            //会员名称
            tvMemberName.setText(mMemberInfoE.getNickName());
            //会员手机号码
            tvMemberNumber.setText(mMemberInfoE.getRegTel());
            initRecycler();
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            tvEmpty.setText("未登陆会员");
            ivEmpty.setVisibility(View.GONE);
        }

    }

    private void initRecycler() {
        mCommonAdapter = new CommonAdapter<VipCardModel>(getContext(), R.layout.member_payment_item, mVipCardModelList) {
            @Override
            protected void convert(ViewHolder holder, VipCardModel vipCardModel, int position) {
                //名称
                holder.setText(R.id.car_name, vipCardModel.getCardName());
                //余额
                holder.setText(R.id.car_money, "1".equals(vipCardModel.getStatus())
                        ? getString(R.string.hes_card_balance, ArithUtil.stripTrailingZeros(vipCardModel.getBalance())) : "--");
                //图标
                holder.setImageResource(R.id.member_pay_iv, vipCardModel.isMember()
                        ? R.drawable.member_account : R.drawable.member_card);
                //是否可用
                if ("1".equals(vipCardModel.getStatus())) {//可用
                    holder.setText(R.id.use_button, "支付");
//                    holder.setBackgroundRes(R.id.use_button, R.drawable.button_blue_corners_selector);
                    holder.setTextColorRes(R.id.use_button, R.color.common_text_color_ffffff);
                    holder.setEnable(R.id.use_button, true);
                } else {
                    holder.setText(R.id.use_button, "不可用");
                    holder.setBackgroundRes(R.id.use_button, R.color.common_text_color_ffffff);
                    holder.setTextColorRes(R.id.use_button, R.color.btn_bg_disable);
                    holder.setEnable(R.id.use_button, false);
                }
            }
        };

        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                VipCardModel vipCardModel = mVipCardModelList.get(position);
                if (vipCardModel.isMember()) {
                    vipCardModel.setCardNum(mMemberInfoE.getMemberInfoGUID());
                }
                startActivityForResult(PaymentVipCardCollectionActivity.newIntent(mBaseActivity, isHesMember, mMemberInfoE.isSetPassWord(), mSalesOrderGUID, mUnpaidTotal, vipCardModel), REQUEST_CODE_PAYMENT_VC_COLLECTION);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContent.setAdapter(mCommonAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBackHesMemberPayment) {
            mCallBackHesMemberPayment = (CallBackHesMemberPayment) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCallBackHesMemberPayment != null) {
            mCallBackHesMemberPayment = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PAYMENT_VC_COLLECTION:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    boolean checkOutDone = extras.getBoolean(PaymentVipCardCollectionActivity.RESULT_CHECK_OUT_OR_NOT, false);
                    if (checkOutDone) {
                        if (mCallBackHesMemberPayment != null) {
                            mCallBackHesMemberPayment.onCheckoutFinishedInnerVipCard();
                        }
                    } else {
                        if (mCallBackHesMemberPayment != null) {
                            mCallBackHesMemberPayment.onPaymentHappenedAndCheckoutUnfinishedInnerVipCard();
                        }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoE != null) {
            mPresenter.requestMemberCardList(mSalesOrderGUID, mMemberInfoE.getMemberInfoGUID());
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void responseMemberCardListSuccess(List<VipCardModel> vipCardModels) {
        mVipCardModelList.clear();

        mVipCardModelList.addAll(mMemberInfoE.getVipCardModels());
        List<VipCardModel> inflaterVipCardList = inflaterVipCard(vipCardModels);
        mVipCardModelList.addAll(inflaterVipCardList);

        if (mVipCardModelList != null && mVipCardModelList.size() != 0) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            mCommonAdapter.notifyDataSetChanged();
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            tvEmpty.setText("无可用会员折扣");
            ivEmpty.setVisibility(View.VISIBLE);
        }
    }

    private List<VipCardModel> inflaterVipCard(List<VipCardModel> vipCardModels) {
        List<VipCardModel> vipCardModelList = new ArrayList<>();
        if (vipCardModels != null && vipCardModels.size() != 0) {
            for (VipCardModel vipCard : vipCardModels) {
                String cardType = vipCard.getCardType();
                if ("010".equalsIgnoreCase(cardType)
                        || "011".equalsIgnoreCase(cardType)
                        || "110".equalsIgnoreCase(cardType)
                        || "111".equalsIgnoreCase(cardType)) {
                    vipCardModelList.add(vipCard);
                }
            }
        }

        return vipCardModelList;
    }

    @Override
    public void responseMemberCardListFail() {
        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    public interface CallBackHesMemberPayment {
        void onPaymentHappenedAndCheckoutUnfinishedInnerVipCard();

        void onCheckoutFinishedInnerVipCard();
    }
}
