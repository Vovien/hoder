package com.holderzone.intelligencepos.mvp.balancehes.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.balancehes.contract.HesMemberCardContract;
import com.holderzone.intelligencepos.mvp.balancehes.presenter.HesMemberCardPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.VipCardModel;
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

/**
 * 何师结算页面 会员卡
 */
public class HesMemberCardActivity extends BaseActivity<HesMemberCardContract.Presenter> implements HesMemberCardContract.View {
    private static final String KEY_MEMBERINFO = "KEY_MEMBERINFO";
    private static final String KEY_SALESORDERGUID = "KEY_SALESORDERGUID";
    /**
     * 成功使用了多少张会员卡
     */
    public static final String KEY_HES_USE_MEMBER_CARD = "KEY_HES_USE_MEMBER_CARD";
    /**
     * 是否成功使用所有会员卡
     */
    public static final String KEY_HAS_SUCCESS_USE_ALL_CARDS = "KEY_HAS_SUCCESS_USE_ALL_CARDS";

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

    private MemberInfoE mMemberInfoE;

    private CommonAdapter<VipCardModel> mCommonAdapter;
    /**
     * 所有的数据信息
     */
    private List<VipCardModel> mVipCardModels = new ArrayList<>();

    private String salesOrderGUID;

    private List<String> cords = new ArrayList<>();

    public static Intent newInstance(Context context, MemberInfoE mMemberInfo, String mSalesOrderGuid) {
        Intent intent = new Intent(context, HesMemberCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MEMBERINFO, mMemberInfo);
        bundle.putString(KEY_SALESORDERGUID, mSalesOrderGuid);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMemberInfoE = extras.getParcelable(KEY_MEMBERINFO);
        salesOrderGUID = extras.getString(KEY_SALESORDERGUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_member_card_hes;
    }

    @Override
    protected HesMemberCardContract.Presenter initPresenter() {
        return new HesMemberCardPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvTitle.setText("会员折扣");
        ivSearch.setVisibility(View.GONE);
        llMenu.setVisibility(View.GONE);
        initClickEvent();
        if (mMemberInfoE != null) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_LOADING);
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            ivEmpty.setVisibility(View.GONE);
            tvEmpty.setText("未登录会员");
            return;
        }
        initRecyclerAdapter();
    }

    @SuppressLint("CheckResult")
    private void initClickEvent() {
        RxView.clicks(btnConfirm)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    mPresenter.requestUseMemberCard(cords, salesOrderGUID, mMemberInfoE.getMemberInfoGUID());
                });

        RxView.clicks(llReturn)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    finishActivity();
                });

        RxView.clicks(btnRetry)
                .subscribe(o -> {
                    if (mMemberInfoE != null) {
                        mPresenter.getMemberCardList(salesOrderGUID, mMemberInfoE.getMemberInfoGUID());
                    }
                });
    }

    private void initRecyclerAdapter() {
        mCommonAdapter = new CommonAdapter<VipCardModel>(getApplicationContext(), R.layout.hes_balance_member_card_item, mVipCardModels) {
            @Override
            protected void convert(ViewHolder holder, VipCardModel vipCardModel, int position) {
                if (position == 0 && vipCardModel.isMemberAccount()) {
                    holder.setText(R.id.iv_card_name, "账户折扣");
                    holder.setImageDrawable(R.id.iv_card_image, ContextCompat.getDrawable(getApplicationContext(), R.drawable.member_account));
                    holder.setVisible(R.id.iv_card_term_validity, false);
                } else {
                    holder.setVisible(R.id.iv_card_term_validity, true);
                    String cardName = vipCardModel.getCardName();
                    holder.setText(R.id.iv_card_name, cardName);
                    if (cardName.length() >5) {
                        holder.setTextSize(R.id.iv_card_name, 16);
                    } else {
                        holder.setTextSize(R.id.iv_card_name, 20);
                    }
                    holder.setImageDrawable(R.id.iv_card_image, ContextCompat.getDrawable(getApplicationContext(), R.drawable.member_card));
                    if (vipCardModel.getPerpetual() != null && vipCardModel.getPerpetual()) {
                        holder.setText(R.id.iv_card_term_validity, "永久有效");
                    } else {
                        holder.setText(R.id.iv_card_term_validity, getString(R.string.hes_coupon_term_validity, vipCardModel.getStartTime(), vipCardModel.getEndTime()));
                    }
                }
                holder.setVisible(R.id.space,position == mVipCardModels.size()-1);

                if (vipCardModel.getSelected()) {
                    holder.setImageDrawable(R.id.iv_card_select, ContextCompat.getDrawable(getApplicationContext(), R.drawable.check_checked));
                } else {
                    holder.setImageDrawable(R.id.iv_card_select, ContextCompat.getDrawable(getApplicationContext(), R.drawable.check_normal));
                }
                holder.setVisible(R.id.iv_share, !vipCardModel.getShare());
                holder.setText(R.id.iv_card_discount, vipCardModel.getDiscount() == null || vipCardModel.getDiscount() == 10 ? "无折扣" : (ArithUtil.stripTrailingZeros(vipCardModel.getDiscount()) + "折"));
                holder.setOnClickListener(R.id.ll_card_item, view -> {
                    String cord;
                    if (position == 0) {
                        cord = "-1";
                    } else {
                        cord = vipCardModel.getCardNum();
                    }

                    boolean selected = vipCardModel.getSelected();
                    vipCardModel.setSelected(!selected);
                    if (selected) {
                        cords.remove(cord);
                    } else {
                        cords.add(cord);
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
     * 修改按钮状态
     */
    private void setButtonState() {
        cords = removeDuplicate(cords);
        if (cords == null || cords.size() == 0) {
            btnConfirm.setText("完成");
        } else {
            btnConfirm.setText("确认使用");
        }
    }

    /**
     * list 去重
     *
     * @param list
     * @return
     */
    private static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoE != null) {
            mPresenter.getMemberCardList(salesOrderGUID, mMemberInfoE.getMemberInfoGUID());
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void getMemberCardListSuccess(boolean memberSelected, List<VipCardModel> vipCardModels) {
        mVipCardModels.clear();
        boolean haveAccount = false;
        if (mMemberInfoE != null && mMemberInfoE.getVipCardModels() != null && mMemberInfoE.getVipCardModels().size() != 0) {
            haveAccount = true;
            VipCardModel vipCardModel = mMemberInfoE.getVipCardModels().get(0);
            if (vipCardModel.getDiscount() != null && vipCardModel.getDiscount() != 10) {
                vipCardModel.setSelected(memberSelected);
                vipCardModel.setMemberAccount(true);
                mVipCardModels.addAll(mMemberInfoE.getVipCardModels());
            }
        }
        if (vipCardModels != null) {
            mVipCardModels.addAll(vipCardModels);
        }
        for (int i = 0; i < mVipCardModels.size(); i++) {
            VipCardModel vipCardModel = mVipCardModels.get(i);
            if (vipCardModel.getSelected()) {
                if (i == 0 && haveAccount) {
                    cords.add("-1");
                } else {
                    cords.add(vipCardModel.getCardNum());
                }
            }
        }
        setButtonState();
        if (mVipCardModels.size() == 0) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            ivEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getString(R.string.hes_not_card));
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getMemberCardListFail() {
        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void responseUseMemberCardFail() {
        cords.clear();
        mPresenter.getMemberCardList(salesOrderGUID, mMemberInfoE.getMemberInfoGUID());
    }

    @Override
    public void responseUseMemberCardNotAll(String number) {
        finishToOperation(false, number);
    }

    private void finishToOperation(boolean isUseAll, String number) {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_HAS_SUCCESS_USE_ALL_CARDS, isUseAll);
        bundle.putString(KEY_HES_USE_MEMBER_CARD, number);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void responseUseMemberCardSuccess() {
        finishToOperation(true, "");
    }
}
