package com.holderzone.intelligencepos.adapter.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.base.RecycleHolder;
import com.holderzone.intelligencepos.adapter.base.SectionedRecyclerViewAdapter;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CollectionUtils;

import java.util.List;

/**
 * 结算菜品适配器
 * Created by zhaoping on 2016/8/23.
 */

public class BalanceAccountsAllDishesAdapter extends SectionedRecyclerViewAdapter<RecycleHolder, RecycleHolder, RecyclerView.ViewHolder> {

    public List<OrderDishesGroup> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    private SparseBooleanArray mBooleanMap;

    public BalanceAccountsAllDishesAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBooleanMap = new SparseBooleanArray();
    }

    public void setOrderDishesData(List<OrderDishesGroup> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return CollectionUtils.isEmpty(mData) ? 0 : mData.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = mData.get(section).getSalesOrderDishesEList().size();
//        if (!mBooleanMap.get(section)) {
//            count = 0;
//        }
        return CollectionUtils.isEmpty(mData.get(section).getSalesOrderDishesEList()) ? 0 : count;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return mData.size() != section + 1;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final RecycleHolder holder, final int section) {
//        holder.openView.setOnClickListener(new View.ConfirmPasswordDialogListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isOpen = mBooleanMap.get(section);
//                String text = isOpen ? "展开" : "关闭";
//                mBooleanMap.put(section, !isOpen);
//                holder.openView.setText(text);
//                notifyDataSetChanged();
//            }
//        });
//
        holder.setVisible(R.id.orderDishesGroupImage, false);
        holder.setVisible(R.id.orderDishesGroupImage, ViewGroup.GONE);
        holder.setText(R.id.orderDishesGroupTitle, mData.get(section).getTitle());
        holder.setTextColorRes(R.id.orderDishesGroupTitle, mData.get(section).getTitleColorRes());
//        holder.openView.setText(mBooleanMap.get(section) ? "关闭" : "展开");
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(RecycleHolder helper, int section, int position) {
        helper.setVisible(R.id.itemDivider, position != 0);
        SalesOrderDishesE item = mData.get(section).getSalesOrderDishesEList().get(position);
        helper.setText(R.id.dishesName, "");
        if (item.getGift() == 1) {//赠送
            helper.setAppendText(R.id.dishesName, R.string.dishes_give, R.color.text_order_dishes_give);
        }
        String dishesName = item.getSimpleName();
        if (dishesName == null) {
            dishesName = item.getSimpleName();
        }
        helper.setAppendText(R.id.dishesName, dishesName, R.color.text_order_dishes_new_name);
        helper.setText(R.id.count, "");
        helper.setAppendText(R.id.count, mContext.getString(R.string.dishes_count_str, ArithUtil.stripTrailingZeros(item.getCheckCount())), R.color.text_order_dishes_new_name);
        //做法以及套餐
        double practiceTotal = ArithUtil.mul(item.getPracticeSubTotal() == null ? 0 : item.getPracticeSubTotal(), item.getCheckCount());
        String subDishes = item.getSubDishes();
        String practiceNames = item.getPracticeNames();
        if (StringUtils.isEmpty(practiceNames) && StringUtils.isEmpty(subDishes)) {
            helper.setVisible(R.id.dishesCookMethodPanel, false);
        } else {
            if (!StringUtils.isEmpty(subDishes)) {
                helper.setText(R.id.cuisineMethodInfo, subDishes);
                helper.setVisible(R.id.cuisineMethodPrice, false);
            } else {
                helper.setText(R.id.cuisineMethodInfo, item.getPracticeNames());
                helper.setText(R.id.cuisineMethodPrice, mContext.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(practiceTotal)));
                helper.setVisible(R.id.cuisineMethodPrice, true);
            }
            helper.setVisible(R.id.dishesCookMethodPanel, true);
        }
        boolean itemUseMemberPrice = item.getIsUseMemberPrice() != null && item.getIsUseMemberPrice() == 1;
        helper.setText(R.id.price, "");
        SpanUtils priceBuilder = new SpanUtils();
        if (itemUseMemberPrice) {
            priceBuilder.append(mContext.getText(R.string.member_mark)).setForegroundColor(mContext.getResources().getColor(R.color.tv_text_orange));
        }
        double dishesPrice = item.getDishesTotal();
        priceBuilder.append(mContext.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesPrice))).setForegroundColor(mContext.getResources().getColor(R.color.common_text_color_707070));
        helper.setText(R.id.price, priceBuilder.create());
    }

    @Override
    protected RecycleHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new RecycleHolder(mInflater.inflate(R.layout.item_order_dishes_group_dishes, parent, false));
    }

    @Override
    protected RecycleHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new RecycleHolder(mInflater.inflate(R.layout.header_order_dishes_group, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return new RecycleHolder(mInflater.inflate(R.layout.footer_order_dishes_group, parent, false));
    }
}
