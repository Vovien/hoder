package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTable;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 账单点单信息详情界面
 * Created by tcw on 2017/7/12.
 */
public class OrderInformationFragment extends BaseFragment {

    private static final String KEY_ARGUMENT_SALES_ORDER_DISHES = "ARGUMENT_SALES_ORDER_DISHES";
    private static final String KEY_ARGUMENT_USE_MEMBER_PRICE = "KEY_ARGUMENT_USE_MEMBER_PRICE";
    @BindView(R.id.rv_addition_fee)
    RecyclerView mRvAdditionFee;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    private boolean useMemberPrice;

    private CommonAdapter<SalesOrderDishesE> mSalesOrderDishesEAdapter;
    private List<SalesOrderDishesE> mArrayOfSalesOrderDishesE;

    public static OrderInformationFragment newInstance(List<SalesOrderDishesE> arrayOfSalesOrderDishesE, boolean useMemberPrice) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_ARGUMENT_USE_MEMBER_PRICE, useMemberPrice);
        args.putParcelableArrayList(KEY_ARGUMENT_SALES_ORDER_DISHES, (ArrayList<? extends Parcelable>) arrayOfSalesOrderDishesE);
        OrderInformationFragment fragment = new OrderInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mArrayOfSalesOrderDishesE = extras.getParcelableArrayList(KEY_ARGUMENT_SALES_ORDER_DISHES);
        useMemberPrice = extras.getBoolean(KEY_ARGUMENT_USE_MEMBER_PRICE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_order_information;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mArrayOfSalesOrderDishesE == null || mArrayOfSalesOrderDishesE.size() == 0) {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            mSalesOrderDishesEAdapter = new CommonAdapter<SalesOrderDishesE>(mBaseActivity, R.layout.item_sales_order_dishes, mArrayOfSalesOrderDishesE) {
                @Override
                protected void convert(ViewHolder holder, SalesOrderDishesE salesOrderDishesE, int position) {
                    // header
                    holder.setVisible(R.id.divider_space_line_header, position == 0);
                    // title
                    if (position == 0 || !salesOrderDishesE.getSalesOrderGUID().equalsIgnoreCase(mArrayOfSalesOrderDishesE.get(position - 1).getSalesOrderGUID())) {
                        holder.setVisible(R.id.ll_title, true);
                        DiningTable diningTable = salesOrderDishesE.getDiningTable();
                        holder.setText(R.id.tv_title, diningTable != null ? diningTable.getName() : "快餐");
                    } else {
                        holder.setVisible(R.id.ll_title, false);
                    }

                    // footer  放在这里是为了当无菜品时的判断操作显示
                    holder.setVisible(R.id.divider_space_line_footer, position == mArrayOfSalesOrderDishesE.size() - 1);
                    /**
                     * 是否显示菜品详情布局
                     */
                    if (salesOrderDishesE.getDishesE() == null) {
                        holder.setVisible(R.id.ll_context, false);
                        return;
                    } else {
                        holder.setVisible(R.id.ll_context, true);
                    }
                    // 菜品名称
                    holder.setText(R.id.tv_dish_name, "");
                    holder.setAppendText(R.id.tv_dish_name, salesOrderDishesE.getDishesE().getSimpleName(), R.color.tv_text_black_000000);
                    //做法和套餐
                    String subDishes = salesOrderDishesE.getSubDishes();
                    if (salesOrderDishesE.getPracticeSubTotal() <= 0 && StringUtils.isEmpty(subDishes)) {
                        holder.setVisible(R.id.ll_addition_item, false);
                    } else {
                        holder.setVisible(R.id.ll_addition_item, true);
                        if (!StringUtils.isEmpty(subDishes)) {//套餐
                            holder.setText(R.id.tv_addition_fee_identify, subDishes);
                            holder.setVisible(R.id.tv_addition_fee_price, false);
                        } else {
                            holder.setText(R.id.tv_addition_fee_identify, salesOrderDishesE.getPracticeNames());
                            holder.setText(R.id.tv_addition_fee_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.mul(salesOrderDishesE.getPracticeSubTotal()
                                    , salesOrderDishesE.getCheckCount()))));
                            holder.setVisible(R.id.tv_addition_fee_price, true);
                        }
                    }
                    // 结算数量
                    holder.setText(R.id.tv_dish_count, getString(R.string.dishes_count_str, ArithUtil.stripTrailingZeros(salesOrderDishesE.getCheckCount())));
                    holder.setText(R.id.tv_total_money, "");
                    // 小计
                    double practiceTotalPrice = salesOrderDishesE.getPracticeSubTotal() != null && salesOrderDishesE.getPracticeSubTotal() > 0 ?
                            ArithUtil.mul(salesOrderDishesE.getOrderCount(), salesOrderDishesE.getPracticeSubTotal()) : 0;
                    boolean itemUseMemberPrice = useMemberPrice && salesOrderDishesE.getMemberTotal() != null
                            && salesOrderDishesE.getMemberTotal().compareTo(salesOrderDishesE.getSubTotal()) != 0;
                    SpanUtils totalPriceBuilder = new SpanUtils();
                    if (salesOrderDishesE.getGift() == 1) {
                        totalPriceBuilder.append(getString(R.string.dishes_give)).setForegroundColor(getResources().getColor(R.color.btn_text_green_01b6ad))
                                .append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(Math.abs(salesOrderDishesE.getSubTotal()), practiceTotalPrice))))
                                .setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    } else if (itemUseMemberPrice) {
                        totalPriceBuilder.append(getString(R.string.member_mark)).setForegroundColor(getResources().getColor(R.color.btn_text_orange_f4a902))
                                .append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(salesOrderDishesE.getMemberTotal())))
                                .setForegroundColor(getResources().getColor(R.color.tv_text_black_000000));
                    } else {
                        totalPriceBuilder.append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(salesOrderDishesE.getSubTotal(), practiceTotalPrice))))
                                .setForegroundColor(getResources().getColor(R.color.tv_text_black_000000));
                    }
                    holder.setText(R.id.tv_total_money, totalPriceBuilder.create());
                    // 分割线
                    if (position == mArrayOfSalesOrderDishesE.size() - 1) {
                        holder.setVisible(R.id.divider_dash_line, false);
                        holder.setVisible(R.id.divider_solid_line, false);
                    } else if (salesOrderDishesE.getSalesOrderGUID().equalsIgnoreCase(mArrayOfSalesOrderDishesE.get(position + 1).getSalesOrderGUID())) {
                        holder.setVisible(R.id.divider_dash_line, true);
                        holder.setVisible(R.id.divider_solid_line, false);
                    } else {
                        holder.setVisible(R.id.divider_dash_line, false);
                        holder.setVisible(R.id.divider_solid_line, true);
                    }

                }
            };
            mRvAdditionFee.setAdapter(mSalesOrderDishesEAdapter);
            mRvAdditionFee.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    /*************************view callback begin*************************/

    @Override
    public void onDispose() {
    }
    /**************************view callback end**************************/
    /*************************dialog callback begin*************************/
    /**************************dialog callback end**************************/
    /*************************private method begin*************************/
    /**************************private method end**************************/
}