package com.holderzone.intelligencepos.mvp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesViewBean;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 已点餐界面
 * Created by tcw on 2017/9/4.
 */

public class DishesOrderedFragment extends BaseFragment {

    public static final String KEY_ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH = "ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH";
    public static final String KEY_IS_DESIGNATED = "isDesignated";

    @BindView(R.id.rv_dishes_ordered)
    RecyclerView mRvDishesOrdered;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    private List<OrderedDishesViewBean> mArrayOfSalesOrderBatchE;

    // rv && adapter
    private CommonAdapter<OrderedDishesViewBean> mSalesOrderBatchDishesAdapter;


    private boolean isDesignated = false;

    public static DishesOrderedFragment newInstance(List<OrderedDishesViewBean> activeList, boolean isDesignated) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_DESIGNATED, isDesignated);
        args.putParcelableArrayList(KEY_ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH, (ArrayList<? extends Parcelable>) activeList);
        DishesOrderedFragment fragment = new DishesOrderedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mArrayOfSalesOrderBatchE = extras.getParcelableArrayList(KEY_ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH);
        isDesignated = extras.getBoolean(KEY_IS_DESIGNATED, false);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_dishes_ordered;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        if (mArrayOfSalesOrderBatchE == null || mArrayOfSalesOrderBatchE.size() == 0) {
            // 切换到空布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            // 切换到内容布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            // 初始化adapter && rv
            mSalesOrderBatchDishesAdapter = new CommonAdapter<OrderedDishesViewBean>(mBaseActivity, R.layout.item_dishes_ordered_, mArrayOfSalesOrderBatchE) {
                @SuppressLint("RestrictedApi")
                @Override
                protected void convert(ViewHolder holder, final OrderedDishesViewBean bean, int position) {

                    holder.setVisible(R.id.table_right_fragment_center_detailed_item_view_bottom, bean.isShowTopSpace());
                    holder.setVisible(R.id.include_title, bean.isShowTopSpace());
                    holder.setVisible(R.id.tv_return, false);
                    if (bean.isShowTopSpace()) {
                        holder.setImageResource(R.id.iv_title_image, bean.getGroupTitleImgResId());
                        // 设置 tilte 文字
                        holder.setText(R.id.tv_title_text, bean.getGroupTitleName());
                        holder.setTextColorRes(R.id.tv_title_text, bean.getGroupTitleColorResId());
                    }
                    holder.setText(R.id.tv_dishes_name, bean.getDishesName());
                    String checkCountStr = ArithUtil.stripTrailingZeros(bean.getCount());
                    if (isDesignated) {
                        String servingCountStr = ArithUtil.stripTrailingZeros(bean.getServingCount());
                        if (ArithUtil.sub(bean.getCount(), bean.getServingCount()) == 0) {
                            holder.setTextColor(R.id.tv_dishes_number, ContextCompat.getColor(getContext(), R.color.common_text_color_000000));
                            holder.setText(R.id.tv_dishes_number, "×" + servingCountStr + "/" + checkCountStr);
                        } else {
                            Context context = getContext();
                            CharSequence charSequence = new SpanUtils()
                                    .append("×")
                                    .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_000000))
                                    .append(servingCountStr)
                                    .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_f56766))
                                    .append("/" + checkCountStr)
                                    .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_000000))
                                    .create();
                            holder.setText(R.id.tv_dishes_number, charSequence);
                        }
                    } else {
                        holder.setText(R.id.tv_dishes_number, getString(R.string.dishes_count_str, checkCountStr));
                    }
                    holder.setVisible(R.id.tv_gift, false);
                    SpanUtils totalPriceBuilder = new SpanUtils();
                    if (EmptyUtils.isNotEmpty(bean.getDishesTagName())) {
                        totalPriceBuilder.append(bean.getDishesTagName()).setForegroundColor(getResources().getColor(bean.getDishesTagColorResId()))
                                .append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(bean.getPrice())))
                                .setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    } else {
                        totalPriceBuilder.append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(bean.getPrice())))
                                .setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    }
                    holder.setText(R.id.tv_dishes_total_price, totalPriceBuilder.create());
                    holder.setVisible(R.id.ll_cook_method, EmptyUtils.isNotEmpty(bean.getCookMethod()) || EmptyUtils.isNotEmpty(bean.getSubDishesString()));
                    if (EmptyUtils.isNotEmpty(bean.getCookMethod())) {
                        holder.setText(R.id.tv_cook_method, bean.getCookMethod());
                        holder.setVisible(R.id.tv_cook_method_money_total, true);
                        holder.setText(R.id.tv_cook_method_money_total, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(bean.getCookMethodPrice())));
                    }
                    if (EmptyUtils.isNotEmpty(bean.getSubDishesString())) {
                        holder.setText(R.id.tv_cook_method, bean.getSubDishesString());
                        holder.setVisible(R.id.tv_cook_method_money_total, false);
                    }
                    holder.setVisible(R.id.dashLine, !bean.isGoneDivider());
                }
            }

            ;
            mRvDishesOrdered.setLayoutManager(new

                    LinearLayoutManager(mBaseActivity));
            mRvDishesOrdered.setAdapter(mSalesOrderBatchDishesAdapter);
        }
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
}
