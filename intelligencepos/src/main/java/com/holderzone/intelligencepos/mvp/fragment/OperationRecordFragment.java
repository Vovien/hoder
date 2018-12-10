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
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 账单操作记录详情界面
 * Created by tcw on 2017/7/12.
 */
public class OperationRecordFragment extends BaseFragment {

    private static final String KEY_ARGUMENT_ARRAY_OF_BATCH = "ARGUMENT_ARRAY_OF_BATCH";
    private static final String KEY_ARGUMENT_USE_MEMBER_PRICE = "KEY_ARGUMENT_USE_MEMBER_PRICE";

    @BindView(R.id.rv_addition_fee)
    RecyclerView mRvAdditionFee;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    private List<SalesOrderBatchE> mSalesOrderBatchEs;
    private boolean useMemberPrice;

    private CommonAdapter<SalesOrderBatchDishesE> mSalesOrderBatchDishesEAdapter;
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesEs = new ArrayList<>();

    public static OperationRecordFragment newInstance(List<SalesOrderBatchE> arrayOfSalesOrderBatchE, boolean useMemberPrice) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_ARGUMENT_USE_MEMBER_PRICE, useMemberPrice);
        args.putParcelableArrayList(KEY_ARGUMENT_ARRAY_OF_BATCH, (ArrayList<? extends Parcelable>) arrayOfSalesOrderBatchE);
        OperationRecordFragment fragment = new OperationRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderBatchEs = extras.getParcelableArrayList(KEY_ARGUMENT_ARRAY_OF_BATCH);
        useMemberPrice = extras.getBoolean(KEY_ARGUMENT_USE_MEMBER_PRICE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_operation_record;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 生成菜品数据
        mSalesOrderBatchDishesEs.clear();
        for (SalesOrderBatchE salesOrderBatchE : mSalesOrderBatchEs) {
            List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE = salesOrderBatchE.getArrayOfSalesOrderBatchDishesE();
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : arrayOfSalesOrderBatchDishesE) {
                Integer operationType = salesOrderBatchDishesE.getOperationType();
                if (operationType == null) {
                    salesOrderBatchDishesE.setOperationType(salesOrderBatchE.getOperationType());
                }
            }
            mSalesOrderBatchDishesEs.addAll(arrayOfSalesOrderBatchDishesE);
        }
        // 逻辑控制
        if (mSalesOrderBatchDishesEs.size() == 0) {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            mSalesOrderBatchDishesEAdapter = new CommonAdapter<SalesOrderBatchDishesE>(mBaseActivity, R.layout.item_sales_order_batch_dishes, mSalesOrderBatchDishesEs) {
                @Override
                protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                    boolean isReturnDishes = salesOrderBatchDishesE.getOperationType() == -1;
                    boolean itemUseMemberPrice = useMemberPrice && salesOrderBatchDishesE.getMemberTotal() != null && salesOrderBatchDishesE.getMemberTotal().compareTo(salesOrderBatchDishesE.getTotal()) != 0;
                    //header
                    holder.setVisible(R.id.divider_space_line_header, position == 0);
                    // title
                    if (position == 0 || !salesOrderBatchDishesE.getSalesOrderBatchGUID().equalsIgnoreCase(mSalesOrderBatchDishesEs.get(position - 1).getSalesOrderBatchGUID())) {
                        holder.setVisible(R.id.ll_title, true);
                        for (SalesOrderBatchE salesOrderBatchE : mSalesOrderBatchEs) {
                            if (salesOrderBatchDishesE.getSalesOrderBatchGUID().equalsIgnoreCase(salesOrderBatchE.getSalesOrderBatchGUID())) {
                                DiningTableE diningTableE = salesOrderBatchE.getDiningTableE();
                                String tableName = diningTableE != null ? diningTableE.getName() : "快餐";
                                String batchTime = DateUtils.anewFormat(salesOrderBatchE.getBatchTime(), "HH:mm");
                                String batchPrice = getString(isReturnDishes ? R.string.negative_amount_str : R.string.amount_str, ArithUtil.stripTrailingZeros(itemUseMemberPrice ? salesOrderBatchE.getMemberTotal() : salesOrderBatchE.getTotal()));
                                holder.setText(R.id.tv_title_basic, batchTime + "(" + tableName + ")");
                                holder.setText(R.id.tv_title_total, batchPrice);
                                holder.setBackgroundRes(R.id.ll_title, isReturnDishes ? R.color.layout_bg_red_f56766 : R.color.layout_bg_green_01b6ad);
                                break;
                            }
                        }
                    } else {
                        holder.setVisible(R.id.ll_title, false);
                    }
                    // 菜品名称
                    holder.setText(R.id.tv_dish_name, "");
                    holder.setAppendText(R.id.tv_dish_name, salesOrderBatchDishesE.getDishesE().getSimpleName(), R.color.tv_text_black_000000);
                    //菜品做法和套餐
                    String subDishes = salesOrderBatchDishesE.getSubDishes();
                    if (salesOrderBatchDishesE.getPracticeSubTotal() <= 0 && StringUtils.isEmpty(subDishes)) {
                        holder.setVisible(R.id.ll_addition_item, false);
                    } else {
                        holder.setVisible(R.id.ll_addition_item, true);
                        if (!StringUtils.isEmpty(subDishes)) {
                            holder.setText(R.id.tv_addition_fee_identify, subDishes);
                            holder.setVisible(R.id.tv_addition_fee_price, false);
                        } else {
                            holder.setText(R.id.tv_addition_fee_identify,salesOrderBatchDishesE.getPracticeNames() );
                            double count = isReturnDishes ? salesOrderBatchDishesE.getBackCount() : salesOrderBatchDishesE.getOrderCount();
                            holder.setText(R.id.tv_addition_fee_price, getString(isReturnDishes ? R.string.negative_amount_str : R.string.amount_str
                                    , ArithUtil.stripTrailingZeros(ArithUtil.mul(count, salesOrderBatchDishesE.getPracticeSubTotal()))));
                            holder.setVisible(R.id.tv_addition_fee_price, true);
                        }
                    }
                    double count = isReturnDishes ? salesOrderBatchDishesE.getBackCount() : salesOrderBatchDishesE.getOrderCount();
                    // 点菜、退菜数量
                    holder.setText(R.id.tv_dish_count, getString(R.string.dishes_count_str, ArithUtil.stripTrailingZeros(count)));
                    // 小计
                    holder.setText(R.id.tv_total_money, "");
                    // 小计
                    double practiceTotalPrice = salesOrderBatchDishesE.getPracticeSubTotal() != null && salesOrderBatchDishesE.getPracticeSubTotal() > 0 ?
                            ArithUtil.mul(count, salesOrderBatchDishesE.getPracticeSubTotal()) : 0;
                    SpanUtils totalPriceBuilder = new SpanUtils();
                    if (salesOrderBatchDishesE.getGift() == 1) {
                        totalPriceBuilder.append(getString(R.string.dishes_give)).setForegroundColor(getResources().getColor(R.color.btn_text_green_01b6ad))
                                .append(getString(isReturnDishes ? R.string.negative_amount_str : R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(salesOrderBatchDishesE.getTotal(), practiceTotalPrice))))
                                .setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    } else if (itemUseMemberPrice) {
                        totalPriceBuilder.append(getString(R.string.member_mark)).setForegroundColor(getResources().getColor(R.color.btn_text_orange_f4a902))
                                .append(getString(isReturnDishes ? R.string.negative_amount_str : R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(Math.abs(salesOrderBatchDishesE.getMemberTotal()), practiceTotalPrice))))
                                .setForegroundColor(getResources().getColor(R.color.btn_text_black_000000));
                    } else {
                        totalPriceBuilder.append(getString(isReturnDishes ? R.string.negative_amount_str : R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(Math.abs(salesOrderBatchDishesE.getTotal()), practiceTotalPrice))))
                                .setForegroundColor(getResources().getColor(R.color.tv_text_black_000000));
                    }
                    holder.setText(R.id.tv_total_money, totalPriceBuilder.create());
                    // 分割线
                    if (position == mSalesOrderBatchDishesEs.size() - 1 || !salesOrderBatchDishesE.getSalesOrderBatchGUID().equalsIgnoreCase(mSalesOrderBatchDishesEs.get(position + 1).getSalesOrderBatchGUID())) {
                        holder.setVisible(R.id.divider_dash_line, false);
                    } else {
                        holder.setVisible(R.id.divider_dash_line, true);
                    }
                    // 退菜原因
                    if (isReturnDishes) {
                        if (position == mSalesOrderBatchDishesEs.size() - 1
                                || !salesOrderBatchDishesE.getSalesOrderBatchGUID().equalsIgnoreCase(mSalesOrderBatchDishesEs.get(position + 1).getSalesOrderBatchGUID())) {
                            // 退菜 && (最后一个位置  || 当前guid和下一个不同)
                            boolean findMatchCase = false;
                            for (SalesOrderBatchE salesOrderBatchE : mSalesOrderBatchEs) {
                                if (!findMatchCase && salesOrderBatchDishesE.getSalesOrderBatchGUID().equalsIgnoreCase(salesOrderBatchE.getSalesOrderBatchGUID())) {
                                    findMatchCase = true;
                                    List<DishesReturnReasonE> arrayOfDishesReturnReasonE = salesOrderBatchE.getArrayOfDishesReturnReasonE();
                                    if (arrayOfDishesReturnReasonE != null && arrayOfDishesReturnReasonE.size() > 0) {
                                        StringBuilder sbReason = new StringBuilder();
                                        sbReason.append("退菜原因：");
                                        int size = arrayOfDishesReturnReasonE.size();
                                        for (int i = 0; i < size; i++) {
                                            DishesReturnReasonE dishesReturnReasonE = arrayOfDishesReturnReasonE.get(i);
                                            if (i == size - 1) {
                                                sbReason.append(dishesReturnReasonE.getName());
                                            } else {
                                                sbReason.append(dishesReturnReasonE.getName() + "，");
                                            }
                                        }
                                        holder.setText(R.id.tv_retreat_reason, sbReason.toString());
                                        holder.setVisible(R.id.ll_retreat_reason, true);
                                        holder.setVisible(R.id.divider_dash_line, false);
                                    } else {
                                        holder.setVisible(R.id.ll_retreat_reason, false);
                                    }
                                }
                            }
                            if (!findMatchCase) {
                                holder.setVisible(R.id.ll_retreat_reason, false);
                            }
                        } else {
                            holder.setVisible(R.id.ll_retreat_reason, false);
                        }
                    } else {
                        holder.setVisible(R.id.ll_retreat_reason, false);
                    }
                    // footer
                    holder.setVisible(R.id.divider_space_line_footer, position == mSalesOrderBatchDishesEs.size() - 1);
                }
            };
            mRvAdditionFee.setAdapter(mSalesOrderBatchDishesEAdapter);
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