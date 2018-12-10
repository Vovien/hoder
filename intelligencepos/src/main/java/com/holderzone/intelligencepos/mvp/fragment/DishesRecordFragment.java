package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesRecordViewBean;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 菜品操作记录界面
 * Created by tcw on 2017/9/4.
 */

public class DishesRecordFragment extends BaseFragment {

    private static final String KEY_ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH = "ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH";

    @BindView(R.id.rv_dishes_record)
    RecyclerView mRvDishesRecord;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    // rv && adapter
    private CommonAdapter<OrderedDishesRecordViewBean> mSalesOrderBatchDishesAdapter;

    private List<OrderedDishesRecordViewBean> mSalesOrderBatchDishesEs = new ArrayList<>();


    public static DishesRecordFragment newInstance(List<OrderedDishesRecordViewBean> recordList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH, (ArrayList<? extends Parcelable>) recordList);
        DishesRecordFragment fragment = new DishesRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        // 所有批次数据构造
        mSalesOrderBatchDishesEs = extras.getParcelableArrayList(KEY_ARGUMENT_ARRAY_OF_SALES_ORDER_BATCH);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_dishes_record;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        if (mSalesOrderBatchDishesEs.size() == 0) {
            // 切换到空布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            // 切换到内容布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            // adapter && rv
            mSalesOrderBatchDishesAdapter = new CommonAdapter<OrderedDishesRecordViewBean>(mBaseActivity, R.layout.item_dishes_ordered_, mSalesOrderBatchDishesEs) {
                @Override
                protected void convert(ViewHolder holder, final OrderedDishesRecordViewBean bean, int position) {

                    holder.setVisible(R.id.table_right_fragment_center_detailed_item_view_bottom, bean.isShowTopSpace());
                    holder.setVisible(R.id.include_title, bean.isShowTopSpace());
                    if (bean.isShowTopSpace()) {
                        holder.setImageResource(R.id.iv_title_image, R.drawable.dishes_batch_time_record_title);
                        holder.setTextColor(R.id.tv_title_text, 0xffffffff);
                        holder.setText(R.id.tv_title_text, bean.getOperateTime());
                        holder.setText(R.id.tv_title_price, bean.getOperatePrice());
                        holder.setBackgroundRes(R.id.include_title, bean.getTitleColorResId());
                    }
                    holder.setVisible(R.id.tv_return, bean.isReturnDishes());
                    holder.setText(R.id.tv_dishes_name, bean.getDishesName());

                    holder.setText(R.id.tv_dishes_number, bean.getCount());
                    holder.setVisible(R.id.tv_gift, false);
                    holder.setVisible(R.id.dashLine, bean.isShowDivider());
                    SpanUtils totalPriceBuilder = new SpanUtils();
                    if (EmptyUtils.isNotEmpty(bean.getDishesTagName())) {
                        totalPriceBuilder.append(bean.getDishesTagName()).setForegroundColor(getResources().getColor(bean.getDishesTagColorResId()))
                                .append(bean.getPrice()).setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    } else {
                        totalPriceBuilder.append(bean.getPrice()).setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    }
                    holder.setText(R.id.tv_dishes_total_price, totalPriceBuilder.create());

                    holder.setVisible(R.id.ll_cook_method, EmptyUtils.isNotEmpty(bean.getCookMethod()) || EmptyUtils.isNotEmpty(bean.getSubDishesString()));
                    if (EmptyUtils.isNotEmpty(bean.getCookMethod())) {
                        holder.setText(R.id.tv_cook_method, bean.getCookMethod());
                        holder.setVisible(R.id.tv_cook_method_money_total, true);
                        holder.setText(R.id.tv_cook_method_money_total, bean.getCookMethodPrice());
                    }
                    if (EmptyUtils.isNotEmpty(bean.getSubDishesString())) {
                        holder.setText(R.id.tv_cook_method, bean.getSubDishesString());
                        holder.setVisible(R.id.tv_cook_method_money_total, false);
                    }
                    holder.setVisible(R.id.tv_retreat_reason, EmptyUtils.isNotEmpty(bean.getReturnReason()));
                    if (EmptyUtils.isNotEmpty(bean.getReturnReason())) {
                        holder.setText(R.id.tv_retreat_reason, bean.getReturnReason());
                    }
                }
            };
            mRvDishesRecord.setLayoutManager(new LinearLayoutManager(mBaseActivity));
            mRvDishesRecord.setAdapter(mSalesOrderBatchDishesAdapter);
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
