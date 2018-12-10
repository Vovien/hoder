package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.base.RecycleHolder;
import com.holderzone.intelligencepos.adapter.base.RecyclerAdapter;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.DishesAwayContract;
import com.holderzone.intelligencepos.mvp.model.bean.MatchSalesOrderDishes;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.DishesAwayPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * pos菜品赠送activity
 * Created by www on 2018/7/26.
 */

public class DishesAwayActivity extends BaseActivity<DishesAwayContract.Presenter> implements DishesAwayContract.View {

    public static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.awayDishesRecyclerView)
    RecyclerView dishesAwayRecyclerView;
    @BindView(R.id.all_dishesAway)
    Button awayButton;
    @BindView(R.id.dishesAway_confirm)
    Button dishesAwayConfirm;
    @BindView(R.id.content)
    MultiStateView content;
    private RecyclerAdapter<MatchSalesOrderDishes> matchSalesOrderDishesRecyclerAdapter;
    private List<MatchSalesOrderDishes> orderDishesList = new ArrayList<>();
    //是否选择全部
    private boolean isAllAway = false;
    private String mSalesOrderGuid;
    public static Intent newIntent(Context context,String salesOrderGuid ) {
        Intent intent = new Intent(context, DishesAwayActivity.class);
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
        return R.layout.activity_away_activity;
    }

    @Nullable
    @Override
    protected DishesAwayContract.Presenter initPresenter() {
        return new DishesAwayPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(v -> mPresenter.getOrderInfo(mSalesOrderGuid));
        matchSalesOrderDishesRecyclerAdapter = new RecyclerAdapter<MatchSalesOrderDishes>(this, orderDishesList, R.layout.dishes_away_item) {
            @Override
            public void convert(RecycleHolder holder, final MatchSalesOrderDishes data, int position) {
                String curSalesOrderBatchGUID = data.getSalesOrderBatchGUID();
                holder.setVisible(R.id.header, data.getTitle() != null);
                holder.setVisible(R.id.tableInfo, data.getTitle() != null);
                if (data.getTitle() != null) {
                    holder.setText(R.id.tableInfo, data.getTitle());
                }
                holder.setVisible(R.id.divider, data.getTitle() != null);
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
                    MatchSalesOrderDishes preSalesOrderBatchDishesE = orderDishesList.get(position - 1);
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
                    for (MatchSalesOrderDishes matchSalesOrderDishes : orderDishesList) {
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
                if (EmptyUtils.isNotEmpty(data.getCookMethodString())) {
                    holder.setText(R.id.cookMethodInfo, data.getCookMethodString());
                }
                //自选套餐明细
                holder.setVisible(R.id.packageDishesInfo, EmptyUtils.isNotEmpty(data.getPackageDishesString()));
                if (EmptyUtils.isNotEmpty(data.getPackageDishesString())) {
                    holder.setText(R.id.packageDishesInfo, data.getPackageDishesString());
                }
                // 菜名
                holder.setText(R.id.tv_dish_name, data.getDishesName());// 结算数量
                SpannableStringBuilder spannableStringBuilder = new SpanUtils()
                        .append(getString(R.string.two_decimals_str, ArithUtil.stripTrailingZeros(data.getConfirmCount())))
                        .setFontSize(21, true)
                        .setForegroundColor(ContextCompat.getColor(DishesAwayActivity.this, R.color.common_text_color_000000))
                        .create();
                holder.setText(R.id.tv_dish_count, spannableStringBuilder);
                //清除监听
                holder.setOnClickListener(R.id.ll_dish_context, null);
                holder.setOnClickListener(R.id.ll_dish_context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SwitchCompat switchCompat = v.findViewById(R.id.dishes_away_switch);
                        if (switchCompat.isChecked()) {
                            switchCompat.setChecked(false);
                        } else {
                            switchCompat.setChecked(true);
                        }
                        confirmChanged();
                    }
                });
                //赠送操作
                holder.clearOnCheckedChangeListener(R.id.dishes_away_switch); //清掉监听器
                holder.setChecked(R.id.dishes_away_switch, data.getGift() == 1 ? true : false);
                holder.setOnCheckedChangeListener(R.id.dishes_away_switch, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            orderDishesList.get(position).setGift(1);
                        } else {
                            orderDishesList.get(position).setGift(0);
                        }
                    }
                });
            }
        };
//        dishesAwayRecyclerView.addItemDecoration(new DashLineItemDecoration(this, DashLineItemDecoration.VERTICAL_LIST));
        dishesAwayRecyclerView.setAdapter(matchSalesOrderDishesRecyclerAdapter);
        dishesAwayRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
    }

    @OnClick({R.id.all_dishesAway, R.id.dishesAway_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_dishesAway:
                List<MatchSalesOrderDishes> list = new ArrayList<>();
                if (list.size() > 0) {
                    list.clear();
                }
                if (!isAllAway) {
                    for (MatchSalesOrderDishes matchSalesOrderDishes : orderDishesList) {
                        matchSalesOrderDishes.setGift(1);
                        list.add(matchSalesOrderDishes);
                    }
                } else {
                    for (MatchSalesOrderDishes matchSalesOrderDishes1 : orderDishesList) {
                        matchSalesOrderDishes1.setGift(0);
                        list.add(matchSalesOrderDishes1);
                    }
                }
                orderDishesList.clear();
                orderDishesList.addAll(list);
                confirmChanged();
                matchSalesOrderDishesRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.dishesAway_confirm:
                List<SalesOrderBatchDishesE> changedList = new java.util.ArrayList<>();
                for (int i = 0; i < orderDishesList.size(); i++) {
                    if (orderDishesList.get(i).getGift() != orderDishesList.get(i).getDefaultGift()) {
                        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
                        salesOrderBatchDishesE.setSalesOrderBatchDishesGUID(orderDishesList.get(i).getSalesOrderBatchDishesGUID());
                        salesOrderBatchDishesE.setGift(orderDishesList.get(i).getGift());
                        changedList.add(salesOrderBatchDishesE);
                    }
                }
                mPresenter.SetDishesGift(changedList);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getOrderInfo(mSalesOrderGuid);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onGetOrderInfoSuccess(List<MatchSalesOrderDishes> list, SalesOrderE salesOrderE, int orderSize) {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        orderDishesList.clear();
        orderDishesList.addAll(list);
        //拿到原始数据
        for (int i = 0; i < list.size(); i++) {
            if (orderDishesList.get(i).getGift() == 1) {
                orderDishesList.get(i).setDefaultGift(1);
            } else if(orderDishesList.get(i).getGift() == 0){
                orderDishesList.get(i).setDefaultGift(0);
            }
        }
        matchSalesOrderDishesRecyclerAdapter.notifyDataSetChanged();
        confirmChanged();
    }

    //这个是表示当前页面是否和初始一样

    private void confirmChanged() {
        isAllAway = true;
        boolean isChange = false;
        for (int i = 0; i < orderDishesList.size(); i++) {
            if (orderDishesList.get(i).getGift() == 0) {
                isAllAway = false;
            }
            if (orderDishesList.get(i).getGift() != orderDishesList.get(i).getDefaultGift()) {
                isChange = true;
            }
        }

        if (isAllAway) {
            awayButton.setText("全部不赠送");
            isAllAway = true;
        } else {
            awayButton.setText("全部赠送");
            isAllAway = false;
        }

        if (isChange) {
            dishesAwayConfirm.setEnabled(true);
        } else {
            dishesAwayConfirm.setEnabled(false);
        }
    }

    @Override
    public void setDishesGiftSuccess() {
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void showNetworkError() {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }
}
