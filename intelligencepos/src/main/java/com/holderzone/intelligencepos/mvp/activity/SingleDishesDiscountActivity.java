package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.base.RecycleHolder;
import com.holderzone.intelligencepos.adapter.base.RecyclerAdapter;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.SingleDishesDiscountContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SingleDishesDiscount;
import com.holderzone.intelligencepos.mvp.presenter.SingleDishesDiscountPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.ChangePricePopup;
import com.kennyc.view.MultiStateView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 单品折扣
 * Created by www on 2018/8/8.
 */

public class SingleDishesDiscountActivity extends BaseActivity<SingleDishesDiscountContract.Presenter> implements SingleDishesDiscountContract.View, ChangePricePopup.ChangePricePopupListener {

    public static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";
    private String mSalesOrderGuid;
    private RecyclerAdapter<SingleDishesDiscount> orderDishesRecyclerAdapter;
    private List<SingleDishesDiscount> orderDishesList = new ArrayList<>();
    private SingleDishesDiscount mCurrentDishes;

    @BindView(R.id.title)
    Title title;
    @BindView(R.id.singleDishRecyclerView)
    RecyclerView singleDishRecyclerView;
    @BindView(R.id.content)
    MultiStateView content;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    /**
     * 改价View
     */
    ChangePricePopup changePricePopup;
    /**
     * 是否启用会员价
     */
    private boolean mEnableMemberPrice;

    /**
     * 账单是否绑定会员信息
     */
    private boolean mMemberInfoExist;
    /**
     * 是否是并单
     */
    private boolean isMergeOrder = false;

    public static Intent newIntent(Context context, String salesOrderGuid) {
        Intent intent = new Intent(context, SingleDishesDiscountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SALES_ORDER_GUID, salesOrderGuid);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGuid = extras.getString(EXTRA_SALES_ORDER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_single_dishes_discount;
    }

    @Override
    protected SingleDishesDiscountContract.Presenter initPresenter() {
        return new SingleDishesDiscountPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        changePricePopup = new ChangePricePopup(this);
        orderDishesRecyclerAdapter = new RecyclerAdapter<SingleDishesDiscount>(getApplicationContext(), orderDishesList, R.layout.single_dishes_discount_item) {
            @Override
            public void convert(RecycleHolder holder, final SingleDishesDiscount data, int position) {
                String curSalesOrderBatchGUID = data.getSalesOrderBatchGUID();
                holder.setVisible(R.id.header, data.getTitle() != null && isMergeOrder);
                holder.setVisible(R.id.tableInfo, data.getTitle() != null);
                if (data.getTitle() != null && isMergeOrder) {
                    holder.setText(R.id.tableInfo, data.getTitle());
                }
                holder.setVisible(R.id.divider, data.getTitle() != null && isMergeOrder);
                holder.setVisible(R.id.header_full_divider, data.getTitle() != null);
                holder.setVisible(R.id.header_dash_divider, data.getTitle() == null);
                if (position == 0) {
                    holder.setVisible(R.id.header_full_divider, false);
                    holder.setVisible(R.id.header_dash_divider, false);
                }
                // 设置 tilte是否显示, 包括批次时间，批次涉及总金额，背景
                if (position == 0) {
                    // 位于第一个位置
                    holder.setVisible(R.id.include_title, true);
                } else {
                    SingleDishesDiscount preSalesOrderBatchDishesE = orderDishesList.get(position - 1);
                    if (curSalesOrderBatchGUID.equalsIgnoreCase(preSalesOrderBatchDishesE.getSalesOrderBatchGUID())) {
                        // 当前guid与上一个guid相同，不显示tilte
                        holder.setVisible(R.id.include_title, false);
                    } else {
                        // 当前guid与上一个guid不同，显示tilte
                        holder.setVisible(R.id.include_title, true);
                        holder.setVisible(R.id.header_dash_divider, false);
                    }
                }
                // title 显示的情况下
                if (holder.findView(R.id.include_title).getVisibility() == View.VISIBLE) {
                    // 设置 tilte 图片
                    holder.setImageResource(R.id.iv_title_image, R.drawable.dishes_batch_time_record_title);
                    // 设置 title 文字颜色
                    holder.setTextColor(R.id.tv_title_text, R.color.layout_bg_white);
                    // 设置 title 批次时间、批次金额
                    for (SingleDishesDiscount matchSalesOrderDishes : orderDishesList) {
                        if (curSalesOrderBatchGUID.equalsIgnoreCase(matchSalesOrderDishes.getSalesOrderBatchGUID())) {
                            // 设置 title 批次时间
                            holder.setText(R.id.tv_title_text, matchSalesOrderDishes.getTime().substring(11, 16));
                        }
                    }
                    // 设置 title 背景颜色
                    holder.setBackgroundResource(R.id.include_title, R.color.layout_bg_green_01b6ad);
                }
                //做法
                holder.setVisible(R.id.cookMethodInfo, EmptyUtils.isNotEmpty(data.getCookMethodString()));
//                holder.setVisible(R.id.cookMethodInfoPrice, EmptyUtils.isNotEmpty(data.getCookMethodString()));
                if (EmptyUtils.isNotEmpty(data.getCookMethodString())) {
                    holder.setText(R.id.cookMethodInfo, data.getCookMethodString());
//                    holder.setText(R.id.cookMethodInfoPrice, getString(R.string.amount, data.getPracticeSubTotal()));
                }
                //自选套餐明细
                holder.setVisible(R.id.packageDishesInfo, EmptyUtils.isNotEmpty(data.getPackageDishesString()));
                if (EmptyUtils.isNotEmpty(data.getPackageDishesString())) {
                    holder.setText(R.id.packageDishesInfo, data.getPackageDishesString());
                }
                // 菜品名字
                holder.setText(R.id.tv_dish_name, data.getDishesName());
                // 单价（包括赠、会）
                // TODO: 18-4-4 judge会员价
                boolean displayMember = mEnableMemberPrice && mMemberInfoExist && new BigDecimal(data.getMemberPrice()).compareTo(new BigDecimal(data.getPrice())) < 0;
                double priceOrMemberPrice = displayMember ? data.getMemberPrice() : data.getPrice();
                String indicatorStr = displayMember ? "[会]" : "";
                int indicatorColor = ContextCompat.getColor(SingleDishesDiscountActivity.this, R.color.common_text_color_f4a902);
                String priceOrMemberPriceStr = getString(R.string.amount_str, ArithUtil.stripTrailingZeros(priceOrMemberPrice));
                SpannableStringBuilder spannableStringBuilder = new SpanUtils()
                        .append(indicatorStr)
                        .setForegroundColor(indicatorColor)
                        .setFontSize(13, true)
                        .append(priceOrMemberPriceStr)
                        .setFontSize(21, true)
                        .setForegroundColor(ContextCompat.getColor(SingleDishesDiscountActivity.this, R.color.common_text_color_000000))
                        .create();
                holder.setText(R.id.price, spannableStringBuilder);
                // 数量
                holder.setText(R.id.tv_dish_count, getString(R.string.two_decimals_str, ArithUtil.stripTrailingZeros(data.getCheckCount())));
                // 折后单价
                holder.setText(R.id.newPrice, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(data.getNewPrice())));
                //清除监听
                holder.setOnClickListener(R.id.ll_dish_context, null);
                holder.setOnClickListener(R.id.ll_dish_context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentDishes = orderDishesList.get(position);
                        double priceOrMemberPrice = mEnableMemberPrice && mMemberInfoExist ? mCurrentDishes.getMemberPrice() : mCurrentDishes.getPrice();
                        changePricePopup.setPrice(priceOrMemberPrice);
                        changePricePopup.showOnAnchor(btnConfirm, RelativePopupWindow.VerticalPosition.ALIGN_BOTTOM, RelativePopupWindow.HorizontalPosition.RIGHT, 0, 0);
                    }
                });
                // TODO: 18-4-4 judge会员价
                holder.setTextColor(R.id.newPrice, data.getNewPrice() != priceOrMemberPrice ? R.color.match_order_confirm_text_changed : R.color.singel_dishes_discount_text_gray);
            }
        };
        singleDishRecyclerView.setAdapter(orderDishesRecyclerAdapter);
        singleDishRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(v -> mPresenter.getOrderInfo(mSalesOrderGuid));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getIsMemberPrice();
        mPresenter.getOrderInfo(mSalesOrderGuid);
    }

    //确认
    @Override
    public void onConfirmClick(double newPrice) {
        mCurrentDishes.setNewPrice(newPrice);
        dishesConfirmNumChanged();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    private void dishesConfirmNumChanged() {
        double discountTotal = 0;
        boolean enabled = false;
        for (SingleDishesDiscount bean : orderDishesList) {
            double priceOrMemberPrice = mEnableMemberPrice && mMemberInfoExist ? bean.getMemberPrice() : bean.getPrice();
            if (priceOrMemberPrice != bean.getNewPrice() || (bean.getDiscountPrice() != -1 && bean.getDiscountPrice() != bean.getNewPrice())) {
                enabled = true;
            }
            discountTotal = ArithUtil.add(discountTotal, ArithUtil.mul(ArithUtil.sub(priceOrMemberPrice, bean.getNewPrice()), bean.getCheckCount()));
        }
        btnConfirm.setText((discountTotal > 0 ? "-" : "") + "￥" + ArithUtil.stripTrailingZeros(Math.abs(discountTotal)) + " 单品改价");
        btnConfirm.setEnabled(enabled);
        orderDishesRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetOrderInfoSuccess(List<SingleDishesDiscount> list, SalesOrderE salesOrderE, int orderCount) {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        orderDishesList.clear();
        orderDishesList.addAll(list);
        int count = 0;
        for (SingleDishesDiscount discount : orderDishesList) {
            if (!StringUtils.isEmpty(discount.getTitle())) {
                count++;
            }
        }
        if (count > 1) {
            isMergeOrder = true;
        }else {
            isMergeOrder = false;
        }
        orderDishesRecyclerAdapter.notifyDataSetChanged();
        mMemberInfoExist = salesOrderE.getMemberInfoE() != null;
    }

    @Override
    public void onReviewDishesSuccess() {
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void showNetworkError() {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onGetIsMemberPrice(Boolean hesVersion) {
        mEnableMemberPrice = hesVersion;
    }

    @OnClick({R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                List<SalesOrderBatchDishesE> changedList = new ArrayList<>();
                for (SingleDishesDiscount matchSalesOrderDishes : orderDishesList) {
                    // TODO: 18-4-4 judge会员价
                    double priceOrMemberPrice = mEnableMemberPrice && mMemberInfoExist ? matchSalesOrderDishes.getMemberPrice() : matchSalesOrderDishes.getPrice();
                    if (priceOrMemberPrice != matchSalesOrderDishes.getNewPrice() ||
                            (matchSalesOrderDishes.getDiscountPrice() != -1 && matchSalesOrderDishes.getDiscountPrice() != matchSalesOrderDishes.getNewPrice())) {
                        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
                        salesOrderBatchDishesE.setSalesOrderBatchDishesGUID(matchSalesOrderDishes.getSalesOrderBatchDishesGUID());
                        salesOrderBatchDishesE.setDiscountPrice(matchSalesOrderDishes.getNewPrice());
                        changedList.add(salesOrderBatchDishesE);
                    }
                }
                mPresenter.setPrice(changedList);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDispose() {

    }


}
