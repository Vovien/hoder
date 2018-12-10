package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.LinearSpacingItemDecoration;
import com.holderzone.intelligencepos.adapter.base.RecycleHolder;
import com.holderzone.intelligencepos.adapter.base.RecyclerAdapter;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsMemCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsMemCardPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CollectionUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.kennyc.view.MultiStateView;

import java.util.List;

import butterknife.BindView;

/**
 * 结算-折扣-会员卡折扣 界面
 * Created by zhaoping on 2017/6/2.
 */

public class BalanceAccountsDiscountMemCardActivity extends BaseActivity<BalanceAccountsMemCardContract.Presenter> implements BalanceAccountsMemCardContract.View {

    public static final String EXTRAS_MEMBER_INFO = "EXTRAS_MEMBER_INFO";
    public static final String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";

    @BindView(R.id.cardRecyclerView)
    RecyclerView cardRecyclerView;
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.content)
    MultiStateView content;
    private String mSalesOrderGuid = null;
    private MemberInfoE mMemberInfoE;
    private List<CardsE> mCardsList = new java.util.ArrayList<>();
    RecyclerAdapter<CardsE> recyclerAdapter;
    private String mSelectCardsChipNo;

    public static Intent newIntent(Context context, String salesOrderGuid, MemberInfoE memberInfoE) {
        Intent intent = new Intent(context, BalanceAccountsDiscountMemCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, salesOrderGuid);
        if (memberInfoE != null) {
            bundle.putParcelable(EXTRAS_MEMBER_INFO, memberInfoE);
        }
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMemberInfoE = extras.getParcelable(EXTRAS_MEMBER_INFO);
        mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_account_mem_card;
    }

    @Override
    protected BalanceAccountsMemCardContract.Presenter initPresenter() {
        return new BalanceAccountsMemCardPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        title.setOnReturnClickListener(this::finishActivity);
//        if (mMemberInfoE != null) {
//            emptyText.setVisibility(View.GONE);
//        }
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMemberInfoE != null) {
                    mPresenter.getCardListByMember(mMemberInfoE.getMemberInfoGUID());
                }
            }
        });
        cardRecyclerView.addItemDecoration(new LinearSpacingItemDecoration(SizeUtils.dp2px(16), true));
        recyclerAdapter = new RecyclerAdapter<CardsE>(getApplicationContext(), mCardsList, R.layout.item_card_2) {
            @Override
            public void convert(RecycleHolder holder, CardsE data, int position) {
                if (data.getCardsChipNo().equalsIgnoreCase(mSelectCardsChipNo)) {
                    holder.setBackgroundResource(R.id.cardLinearlayout, R.drawable.shape_card_item_select);
                } else if (data.getIsAvailable() == 0) {
                    holder.setBackgroundResource(R.id.cardLinearlayout, R.drawable.shape_card_item_disable);
                } else {
                    holder.setBackgroundResource(R.id.cardLinearlayout, R.drawable.selector_card_item_normal);
                }
                holder.setText(R.id.cardNameTextView, data.getCardTypeName());
                if (data.getIsAvailable() == 0 || data.getCardsChipNo().equalsIgnoreCase(mSelectCardsChipNo)) {
                    holder.setTextColor(R.id.cardNameTextView, R.color.card_item_selected_text_color);
                    holder.setTextColor(R.id.cardNumberTextView, R.color.card_item_selected_text_color);
                    holder.setTextColor(R.id.cardDiscountTextView, R.color.card_item_selected_text_color);
                    holder.setTextColor(R.id.cardValidityTextView, R.color.card_item_selected_text_color);
                } else {
                    holder.setTextColor(R.id.cardNameTextView, R.color.card_item_type_text_color);
                    holder.setTextColor(R.id.cardNumberTextView, R.color.card_item_text_color);
                    holder.setTextColor(R.id.cardDiscountTextView, R.color.card_item_text_color);
                    holder.setTextColor(R.id.cardValidityTextView, R.color.card_item_text_color);
                }
                holder.setText(R.id.cardNumberTextView, getString(R.string.card_name, data.getCardsNumber()));
                holder.setText(R.id.cardDiscountTextView, "权益折扣：" + (data.getRightsCiscount() == 0 ? "无折扣" : ArithUtil.mul(data.getRightsCiscount(), 10) + "折"));
                holder.setText(R.id.cardValidityTextView, getString(R.string.date_section, data.getEffectTime(), data.getInvalidTime()));
            }
        };
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                if (mCardsList.get(position).getIsAvailable() == 1) {
                    mSelectCardsChipNo = mCardsList.get(position).getCardsChipNo();
                    recyclerAdapter.notifyDataSetChanged();
                    mPresenter.vipCardDiscount(mSalesOrderGuid, mSelectCardsChipNo);
                }
            }
        });
        cardRecyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoE != null) {
            mPresenter.getCardListByMember(mMemberInfoE.getMemberInfoGUID());
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onGetCardListSuccess(List<CardsE> list) {
        content.setVisibility(View.VISIBLE);
        if (CollectionUtils.isEmpty(list)) {
            content.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        mCardsList.clear();
        mCardsList.addAll(list);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVipCardDiscountSuccess() {
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void showNetworkErrorLayout() {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }
}
