package com.holderzone.intelligencepos.widget.recyclerview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroupDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;

import java.util.List;

/**
 * Created by LT on 2018-04-18.
 */

public class PackageGroupOrderDishesAdapter extends GroupedRecyclerViewAdapter {
    /**
     * 数据源
     */
    private List<PackageGroup> mPackageGroupList;
    private ItemViewClickListener mClickListener;

    public PackageGroupOrderDishesAdapter(Context context, List<PackageGroup> packageGroups) {
        super(context);
        mPackageGroupList = packageGroups;
    }

    @Override
    public int getGroupCount() {
        return mPackageGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mPackageGroupList.get(groupPosition).getArrayOfPackageGroupDishesE().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.item_package_group_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_package_group_content;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        PackageGroup packageGroup = mPackageGroupList.get(groupPosition);
        holder.setText(R.id.packager_group_name, packageGroup.getName());
        double selectedCount = packageGroup.getSelectedCount();
        holder.setText(R.id.max_count, selectedCount > 0 ? ArithUtil.stripTrailingZeros(selectedCount) + "/" + packageGroup.getOptionalCount() + "已选"
                : "0/" + packageGroup.getOptionalCount() + "已选");
        //如果选择数量与可选数量相等  改变字体颜色
        TextView view = holder.get(R.id.max_count);
        if (ArithUtil.sub(packageGroup.getOptionalCount(), selectedCount) > 0) {
            view.setTextColor(mContext.getResources().getColor(R.color.btn_text_gray_555555));
        } else {
            view.setTextColor(mContext.getResources().getColor(R.color.btn_text_blue_2495ee));
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        PackageGroupDishesE packageGroupDishesE = mPackageGroupList.get(groupPosition).getArrayOfPackageGroupDishesE().get(childPosition);
        StringBuilder name = new StringBuilder();
        if (packageGroupDishesE.getSingleCount() != null && ArithUtil.sub(packageGroupDishesE.getSingleCount(), 1.0) == 0) {
            holder.setText(R.id.dishes_name, packageGroupDishesE.getSimpleName());
        } else if (packageGroupDishesE.getSingleCount() != null
                && packageGroupDishesE.getCheckUnit() != null
                && packageGroupDishesE.getSingleCount() != 1) {
            name.append(packageGroupDishesE.getSimpleName())
                    .append(ArithUtil.stripTrailingZeros(packageGroupDishesE.getSingleCount()))
                    .append(packageGroupDishesE.getCheckUnit());
            holder.setText(R.id.dishes_name, name.toString());
        } else {
            holder.setText(R.id.dishes_name, packageGroupDishesE.getSimpleName());
        }
        if (packageGroupDishesE.getRiseAmount() != null && packageGroupDishesE.getRiseAmount() > 0) {
            holder.setVisible(R.id.rise_amount, View.VISIBLE);
            //菜品有浮动价
            holder.setText(R.id.rise_amount, "+" + "￥" + ArithUtil.stripTrailingZeros(packageGroupDishesE.getRiseAmount()));
        } else {
            holder.setVisible(R.id.rise_amount, View.GONE);
        }
        //菜品是否有下单数量
        if (packageGroupDishesE.getPackageDishesOrderCount() > 0) {
            holder.setVisible(R.id.tv_dishes_count, View.VISIBLE);
            holder.setVisible(R.id.iv_reduce, View.VISIBLE);
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append(ArithUtil.stripTrailingZeros(packageGroupDishesE.getPackageDishesOrderCount())).setUnderline();
            TextView view = holder.get(R.id.tv_dishes_count);
            view.setText(spanUtils.create());
        } else {
            holder.setVisible(R.id.iv_reduce, View.INVISIBLE);
            holder.setVisible(R.id.tv_dishes_count, View.INVISIBLE);
        }
        //添加按钮是否可以点击
        if (ArithUtil.sub(mPackageGroupList.get(groupPosition).getOptionalCount(), mPackageGroupList.get(groupPosition).getSelectedCount()) > 0) {
            holder.get(R.id.iv_add).setEnabled(true);
            holder.setImageResource(R.id.iv_add, R.drawable.add_blue_max);
        } else {
            holder.get(R.id.iv_add).setEnabled(false);
            holder.setImageResource(R.id.iv_add, R.drawable.add_gray);
        }
        //控制底部分割线的显示
        if (childPosition == mPackageGroupList.get(groupPosition).getArrayOfPackageGroupDishesE().size() - 1) {
            holder.setVisible(R.id.divider, View.GONE);
        } else {
            holder.setVisible(R.id.divider, View.VISIBLE);
        }
        holder.get(R.id.iv_add).setOnClickListener(v -> {
            if (mClickListener != null) {
                int addCount = packageGroupDishesE.getPackageDishesOrderCount() + 1;
                double groupSelectedCount = ArithUtil.add(mPackageGroupList.get(groupPosition).getSelectedCount(), 1);
                mClickListener.onAddDishesClick(groupPosition, childPosition, addCount, groupSelectedCount);
            }
        });
        holder.get(R.id.iv_reduce).setOnClickListener(v -> {
            if (mClickListener != null) {
                int reduceCount = packageGroupDishesE.getPackageDishesOrderCount() - 1;
                double groupSelectedCount = ArithUtil.sub(mPackageGroupList.get(groupPosition).getSelectedCount(), 1);
                mClickListener.onReduceDishesClick(groupPosition, childPosition, reduceCount, groupSelectedCount);
            }
        });
        holder.get(R.id.tv_dishes_count).setOnClickListener(v -> {
            if (mClickListener != null) {
                //已点数量
                double selectedCount = mPackageGroupList.get(groupPosition).getSelectedCount();
                double canSelectedCount = ArithUtil.sub(mPackageGroupList.get(groupPosition).getOptionalCount(),
                        selectedCount);
                //子菜 已点数量
                int count = packageGroupDishesE.getPackageDishesOrderCount();
                //能修改的数量为可点击数量-已点数量+当前这个菜品已经点的数量
                double canSelectedAll = ArithUtil.add(canSelectedCount, count);
                mClickListener.onChangeCountClick(groupPosition, childPosition, count, canSelectedAll);
            }
        });
    }

    public void setItemClickListener(ItemViewClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ItemViewClickListener {
        void onAddDishesClick(int groupPosition, int childPosition, int count, double groupSelectedCount);

        void onReduceDishesClick(int groupPosition, int childPosition, int count, double groupSelectedCount);

        void onChangeCountClick(int groupPosition, int childPosition, int count, double canSelectedCount);
    }
}
