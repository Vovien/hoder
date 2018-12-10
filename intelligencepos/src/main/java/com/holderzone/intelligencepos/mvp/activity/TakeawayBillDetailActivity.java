package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmChangeDishesDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.TakeawayBillDetailContract;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.presenter.TakeawayBillDetailPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2018-04-03.
 * 外卖订单详情页面
 */

public class TakeawayBillDetailActivity extends BaseActivity<TakeawayBillDetailContract.Presenter> implements TakeawayBillDetailContract.View
        , ConfirmChangeDishesDialogFragment.ConfirmChangeDishesListener {
    private static final String KEY_INTENT_UNORDER_RECEIVE_MSG_GUID = "KEY_INTENT_UNORDER_RECEIVE_MSG_GUID";
    private static final String KEY_INTENT_UNORDER_GUID = "INTENT_UNORDER_GUID";
    private static final String KEY_INTENT_UNORDE_TYPE = "KEY_INTENT_UNORDE_TYPE";
    private static final String KEY_INTENT_UNORDE_NAME_TYPE = "KEY_INTENT_UNORDE_NAME_TYPE";
    private static final String KEY_INTENT_UNORDE_SN = "KEY_INTENT_UNORDE_SN";
    private static final String KEY_INTENT_UNORDER_SHOW = "KEY_INTENT_UNORDER_SHOW";
    private static final String KEY_IS_SUBMIT_CONFIRM = "KEY_IS_SUBMIT_CONFIRM";
    private static final int REQUEST_CODE_CHANGE_DISHES = 0x00000000;
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.take_out_detail_Rv)
    RecyclerView mTakeOutDetailRv;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.multi_state_view)
    MultiStateView multiStateView;
    /**
     * 订单GUID
     */
    private String mUnOrderGuid;
    /**
     * 消息GUID
     */
    private String mMsgGuid;
    /**
     * 外卖类型 0:外卖订单 1：微信订单
     */
    private int mUnOrderType;
    /**
     * 如果mUnOrderType = 0：0：美团 1：饿了么
     * 如果mUnOrderType = 1: 0:扫码订单 1：微信预订单
     */
    private int mUnOrderTypeName;
    /**
     * 流水号
     */
    private String mDaySn;
    /**
     * titleName
     */
    private String mName;
    /**
     * 是否显示底部按钮
     */
    private boolean isShowButton;
    /**
     * adapter
     */
    private CommonAdapter<UnOrderDishesE> mAdapter;
    /**
     * 菜品数据
     */
    private List<UnOrderDishesE> mUnOrderDishesEList = new ArrayList<>();
    /**
     * 是否是外卖
     */
    private boolean isTakeaway = false;
    /**
     * 是否可以点餐
     */
    private boolean isCanOrder = true;
    /**
     * 点击的菜品实体
     */
    private UnOrderDishesE mClickUnOrderDishes;
    /**
     * 是否需要先接单
     */
    private boolean isSubmitConfirmOrder;

    public static Intent newIntent(Context context, String orderGuid, String msgGuid, int orderType, int orderTypeName, String daySn, boolean isShow, boolean isConfirm) {
        Intent intent = new Intent(context, TakeawayBillDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_INTENT_UNORDER_GUID, orderGuid);
        bundle.putString(KEY_INTENT_UNORDER_RECEIVE_MSG_GUID, msgGuid);
        bundle.putInt(KEY_INTENT_UNORDE_TYPE, orderType);
        bundle.putInt(KEY_INTENT_UNORDE_NAME_TYPE, orderTypeName);
        bundle.putString(KEY_INTENT_UNORDE_SN, daySn);
        bundle.putBoolean(KEY_INTENT_UNORDER_SHOW, isShow);
        bundle.putBoolean(KEY_IS_SUBMIT_CONFIRM, isConfirm);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mUnOrderGuid = extras.getString(KEY_INTENT_UNORDER_GUID);
        mMsgGuid = extras.getString(KEY_INTENT_UNORDER_RECEIVE_MSG_GUID);
        mDaySn = extras.getString(KEY_INTENT_UNORDE_SN);
        mUnOrderType = extras.getInt(KEY_INTENT_UNORDE_TYPE);
        mUnOrderTypeName = extras.getInt(KEY_INTENT_UNORDE_NAME_TYPE);
        takeOutName(mUnOrderType, mUnOrderTypeName);
        isShowButton = extras.getBoolean(KEY_INTENT_UNORDER_SHOW);
        isSubmitConfirmOrder = extras.getBoolean(KEY_IS_SUBMIT_CONFIRM);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_take_away_bill_detail;
    }

    @Nullable
    @Override
    protected TakeawayBillDetailContract.Presenter initPresenter() {
        return new TakeawayBillDetailPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //initTitle
        initTitle();
        //是否显示底部按钮 催单和退单不显示
        mBtnConfirm.setVisibility(isShowButton ? View.VISIBLE : View.GONE);
        //初始化Adapter
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<UnOrderDishesE>(this, R.layout.item_takeway_dishes, mUnOrderDishesEList) {
            @Override
            protected void convert(ViewHolder holder, UnOrderDishesE unOrderDishesE, int position) {
                holder.setVisible(R.id.changeDishesTextView, isShowButton);
                if (isTakeaway) {//外卖订单
                    //控制口袋显示
                    if ((position == 0 && unOrderDishesE.getCartId() != null) ||
                            (position > 0 && unOrderDishesE.getCartId() != null && !mUnOrderDishesEList.get(position - 1).getCartId().equals(unOrderDishesE.getCartId()))) {
                        holder.setText(R.id.packageNameTextView, getString(R.string.takeaway_package_name, (unOrderDishesE.getCartId() + 1)));
                        holder.setVisible(R.id.packageNameTextView_ll, true);
                        holder.setVisible(R.id.itemTitle, true);
                    } else if (position > 0) {
                        holder.setVisible(R.id.packageNameTextView_ll, false);
                        holder.setVisible(R.id.itemTitle, false);
                    }
                    holder.setText(R.id.platformDishesNameTextView, unOrderDishesE.getDishesName());
                    if (unOrderDishesE.getDishesE() != null) {
                        holder.setText(R.id.dishesNameTextView, unOrderDishesE.getDishesE().getSimpleName());
                        holder.setTextColorRes(R.id.platformDishesNameTextView, R.color.common_text_color_000000);
                        if (unOrderDishesE.getDishesCode().equals(unOrderDishesE.getDishesE().getCode())) {
                            holder.setTextColorRes(R.id.dishesNameTextView, R.color.common_text_color_000000);
                        } else {
                            holder.setTextColorRes(R.id.dishesNameTextView, R.color.common_text_color_01b6ad);
                        }
                        if (unOrderDishesE.getDishesE().getIsEstimate() != null && unOrderDishesE.getDishesE().getIsEstimate() == 1) {
                            holder.setAppendText(R.id.dishesNameTextView, "[售罄]", 14.7f, R.color.common_text_color_f56766);
                        }
                    } else {
                        holder.setText(R.id.dishesNameTextView, "- -");
                        holder.setTextColorRes(R.id.dishesNameTextView, R.color.common_text_color_f56766);
                        holder.setTextColorRes(R.id.platformDishesNameTextView, R.color.common_text_color_f56766);
                    }
                    holder.setText(R.id.dishesAmountTextView, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(unOrderDishesE.getDishesSubTotal())));
                    if ((!TextUtils.isEmpty(unOrderDishesE.getDishesSpecs())) || (!TextUtils.isEmpty(unOrderDishesE.getDishesProperty()))) {
                        String remark = "";
                        if (!TextUtils.isEmpty(unOrderDishesE.getDishesSpecs())) {
                            remark = unOrderDishesE.getDishesSpecs();
                        }
                        if (!TextUtils.isEmpty(unOrderDishesE.getDishesProperty())) {
                            remark = (remark.length() == 0 ? "" : remark + ",") + unOrderDishesE.getDishesProperty();
                        }
                        holder.setVisible(R.id.remarkLinearLayout, true);
                        holder.setText(R.id.remarkTextView, remark);
                    } else {
                        holder.setVisible(R.id.remarkLinearLayout, false);
                    }
                    //外卖不管做法
                    holder.setVisible(R.id.practice_ll, false);
                } else {
                    holder.setVisible(R.id.remarkLinearLayout, false);
                    holder.setVisible(R.id.dishesNameTextView, false);
                    holder.setVisible(R.id.imagePanel, false);
                    holder.setVisible(R.id.itemTitle, false);
                    holder.setTextSize(R.id.platformDishesNameTextView, 21.3f);
                    double practiceTotal1 = ArithUtil.mul(unOrderDishesE.getPracticePrice(), unOrderDishesE.getDishesQuantity());
                    double dishesAmount = ArithUtil.sub(unOrderDishesE.getChangeSubTotal(), practiceTotal1);
                    holder.setText(R.id.dishesAmountTextView, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesAmount)));
                    if (unOrderDishesE.getDishesE() != null) {
                        holder.setText(R.id.platformDishesNameTextView, unOrderDishesE.getDishesE().getSimpleName());
                        if (unOrderDishesE.getDishesE().getIsEstimate() != null && unOrderDishesE.getDishesE().getIsEstimate() == 1) {
                            holder.setAppendText(R.id.platformDishesNameTextView, "[售罄]", 14.7f, R.color.common_text_color_f56766);
                        }
                    }
                    if (!TextUtils.isEmpty(unOrderDishesE.getDishesPracticeContent())) {
                        holder.setText(R.id.practice_name, unOrderDishesE.getDishesPracticeContent());
                        double practiceTotal = ArithUtil.mul(unOrderDishesE.getPracticePrice(), unOrderDishesE.getDishesQuantity());
                        holder.setText(R.id.practice_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(practiceTotal)));
                        holder.setVisible(R.id.practice_ll, true);
                    } else {
                        holder.setVisible(R.id.practice_ll, false);
                    }
                }
                holder.setText(R.id.dishesCountTextView, getString(R.string.dishes_count_str, ArithUtil.stripTrailingZeros(unOrderDishesE.getDishesQuantity())));
                holder.setOnClickListener(R.id.item_content, v -> {
                    //换菜页面
                    if (isShowButton) {
                        mClickUnOrderDishes = unOrderDishesE;
                        getDialogFactory().showConfirmChangeDishesDialog(unOrderDishesE.getDishesName(), TakeawayBillDetailActivity.this);
                    }
                });
                //控制底部分割线的显示
                if (position == mUnOrderDishesEList.size() - 1) {
                    holder.setVisible(R.id.itemDivider, false);
                } else {
                    holder.setVisible(R.id.itemDivider, true);
                }
            }
        };
        //设置adapter
        mTakeOutDetailRv.setAdapter(mAdapter);
        mTakeOutDetailRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initTitle() {
        mTitle.setTitleText(mName);
        mTitle.setOnReturnClickListener(this::finishActivity);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getOrderDetail(mUnOrderGuid);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 外卖类型名称
     */
    private void takeOutName(int type, int typeName) {
        if (type == 0) {//外卖订单
            switch (typeName) {
                case 0:
                    mName = "美团";
                    break;
                case 1:
                    mName = "饿了么";
                    break;
                case 3:
                    mName = "其他";
                    break;
                default:
            }
        } else if (type == 1) {//微信订单
            switch (typeName) {
                case 0:
                    mName = "扫码点餐";
                    break;
                case 1:
                    mName = "微信预订单";
                    break;
                case 3:
                    mName = "其他";
                    break;
                default:
            }
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (!isCanOrder) {
            getDialogFactory().showConfirmDialog(isTakeaway ? "部分菜品门店无货，请更换成其他菜品。" : "部分菜品已售罄，请更换成其他菜品。"
                    , false, "取消", R.drawable.selector_button_green, true, "确定", R.drawable.selector_button_blue, new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {

                        }

                        @Override
                        public void onPosClick() {

                        }
                    });
        } else {
            if (isSubmitConfirmOrder) {
                //接单
                mPresenter.submitConfirmOrder(mUnOrderGuid, mMsgGuid, 0);
            } else {
                //下单
                UnOrderE unOrderE = new UnOrderE();
                unOrderE.setUnOrderGUID(mUnOrderGuid);
                unOrderE.setUnOrderReceiveMsgGUID(mMsgGuid);
                unOrderE.setIsSalesOrder(1);
                mPresenter.confirmOrder(unOrderE);
            }
        }
    }

    @Override
    public void onConfirmOrderSuccess() {
        finishActivity();
    }

    @Override
    public void onGetOrderSuccess(UnOrderE order) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        isCanOrder = true;
        mBtnConfirm.setText(getString(R.string.order_button_text, ArithUtil.stripTrailingZeros(order.getTotal())));
        if (order.getOrderType() == 0) {//外卖订单
            isTakeaway = true;
            for (UnOrderDishesE dishes : order.getArrayOfUnOrderDishesE()) {
                if (dishes.getDishesE() == null) {
                    isCanOrder = false;
                    break;
                }
            }
        } else if (order.getOrderType() == 1) {//微信点餐
            if (order.getOrderSubType() == 0) {//扫码点餐
                for (UnOrderDishesE dishes : order.getArrayOfUnOrderDishesE()) {
                    if (dishes.getDishesE() != null) {
                        if (dishes.getDishesE().getIsEstimate() != null && dishes.getDishesE().getIsEstimate() == 1) {
                            isCanOrder = false;
                            break;
                        }
                    }
                }
            }
        }
        mUnOrderDishesEList.clear();
        mUnOrderDishesEList.addAll(order.getArrayOfUnOrderDishesE());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void onConfirmOrderFailedInnerNoDish() {

    }

    @Override
    public void addDishesEstimateFailed() {

    }

    @Override
    public void onConfirmOrderFailed() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onSubmitConfirmOrderSucceed() {
        //接单成功
        isSubmitConfirmOrder = false;
        UnOrderE unOrderE = new UnOrderE();
        unOrderE.setUnOrderGUID(mUnOrderGuid);
        unOrderE.setUnOrderReceiveMsgGUID(mMsgGuid);
        unOrderE.setIsSalesOrder(1);
        mPresenter.confirmOrder(unOrderE);
    }

    @Override
    public void onSubmitConfirmOrderFailed() {
        //接单失败
        showMessage("接单失败");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHANGE_DISHES && resultCode == RESULT_OK) {
            mPresenter.getOrderDetail(mUnOrderGuid);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConfirmChange() {
        startActivityForResult(ChangeDishesActivity.newIntent(TakeawayBillDetailActivity.this, mUnOrderGuid
                , mClickUnOrderDishes.getUnOrderDishesGUID(), isTakeaway), REQUEST_CODE_CHANGE_DISHES);
    }
}
