package com.holderzone.intelligencepos.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.LinearSpacingItemDecoration;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.CardTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 会员卡支付 卡列表
 * Created by tcw on 2017/5/31.
 */

public class PaymentVipCardDisplayCardsFragment extends BaseFragment {

    private static final String EXTRA_ARGUMENT_CARDS_KEY = "EXTRA_ARGUMENT_CARDS_KEY";

    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.rv_cards)
    RecyclerView mRvCards;

    /**
     * 会员卡列表
     */
    private List<CardsE> mCardsEList = new ArrayList<>();

    /**
     * 会员卡列表adapter
     */
    private CommonAdapter<CardsE> mCommonAdapter;

    /**
     * 当前选中会员卡在adapter中的position
     */
    private int mSelectedPosition = -1;

    /**
     * 回调
     */
    private OnClickListener mOnClickListener;

    /**
     * 回调接口定义
     */
    public interface OnClickListener {
        /**
         * 确认使用该卡
         */
        void onConfirmClick(CardsE cardsE);
    }

    public static PaymentVipCardDisplayCardsFragment newInstance(List<CardsE> arrayOfCardsE) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_ARGUMENT_CARDS_KEY, (ArrayList<? extends Parcelable>) arrayOfCardsE);
        PaymentVipCardDisplayCardsFragment fragment = new PaymentVipCardDisplayCardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnClickListener) {
            mOnClickListener = (OnClickListener) parentFragment;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnClickListener != null) {
            mOnClickListener = null;
        }
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        List<CardsE> cardsES = extras.getParcelableArrayList(EXTRA_ARGUMENT_CARDS_KEY);
        for (CardsE cardsE : cardsES) {
            if (cardsE.getCanUseStatus() != null && cardsE.getCanUseStatus() == 1) {
                mCardsEList.add(cardsE);
            }
        }
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_vip_card_display_cards;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 切换布局
        if (mCardsEList.size() == 0) {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        // 初始化适配器相关
        mCommonAdapter = new CommonAdapter<CardsE>(mBaseActivity, R.layout.item_card, mCardsEList) {
            @Override
            protected void convert(ViewHolder holder, CardsE cardsE, int position) {
                holder.setText(R.id.tv_card_name, cardsE.getCardTypeName());
                holder.setText(R.id.tv_card_number, "卡号：" + cardsE.getCardsNumber());
                CardTypeE cardTypeE = cardsE.getCardTypeE();
                holder.setText(R.id.tv_card_discount, "支付折扣：" + (cardTypeE.getIsPaymentCiscount() == 0 ? "无折扣" : ArithUtil.mul(cardTypeE.getPaymentCiscount(), 10) + "折"));
                holder.setText(R.id.tv_card_valid_period_or_balance, "余额：" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(cardsE.getBalance())));

                Integer canUseStatus = cardsE.getCanUseStatus();
                if (canUseStatus != null) {
                    if (position == mSelectedPosition) {// 选中
                        holder.setSelected(R.id.ll_card, true);
                        holder.setEnable(R.id.ll_card, true);
                    } else if (cardsE.getCanUseStatus() == 1) {// 可用
                        holder.setSelected(R.id.ll_card, false);
                        holder.setEnable(R.id.ll_card, true);
                    } else {// 不可用
                        holder.setEnable(R.id.ll_card, false);
                    }

                    holder.setEnable(R.id.tv_card_name, true);
                    holder.setEnable(R.id.tv_card_number, true);
                    holder.setEnable(R.id.tv_card_discount, true);
                    holder.setEnable(R.id.tv_card_valid_period_or_balance, true);
                    if (cardsE.getCanUseStatus() == 0 || position == mSelectedPosition) {
                        holder.setSelected(R.id.tv_card_name, true);
                        holder.setSelected(R.id.tv_card_number, true);
                        holder.setSelected(R.id.tv_card_discount, true);
                        holder.setSelected(R.id.tv_card_valid_period_or_balance, true);
                    } else {
                        holder.setSelected(R.id.tv_card_name, false);
                        holder.setSelected(R.id.tv_card_number, false);
                        holder.setSelected(R.id.tv_card_discount, false);
                        holder.setSelected(R.id.tv_card_valid_period_or_balance, false);
                    }
                } else {
                    holder.setEnable(R.id.ll_card, false);
                    holder.setEnable(R.id.tv_card_name, false);
                    holder.setEnable(R.id.tv_card_number, false);
                    holder.setEnable(R.id.tv_card_discount, false);
                    holder.setEnable(R.id.tv_card_valid_period_or_balance, false);
                }
            }
        };
        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CardsE cardsE = mCardsEList.get(position);
                if (position == mSelectedPosition) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onConfirmClick(cardsE);
                    }
                    return;
                }
                Integer canUseStatus = cardsE.getCanUseStatus();
                if (canUseStatus != null && canUseStatus == 1) {
                    mSelectedPosition = position;
                    mCommonAdapter.notifyDataSetChanged();
                    if (mOnClickListener != null) {
                        mOnClickListener.onConfirmClick(cardsE);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvCards.setAdapter(mCommonAdapter);
        mRvCards.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        mRvCards.addItemDecoration(new LinearSpacingItemDecoration(24, true));
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

    public List<CardsE> getCardList() {
        return mCardsEList;
    }
}
