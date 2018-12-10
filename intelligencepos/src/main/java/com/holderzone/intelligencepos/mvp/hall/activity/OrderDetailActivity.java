package com.holderzone.intelligencepos.mvp.hall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.google.gson.Gson;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.activity.ChangeNumberActivity;
import com.holderzone.intelligencepos.mvp.activity.DineChangeNumberActivity;
import com.holderzone.intelligencepos.mvp.activity.OrderDishesActivity;
import com.holderzone.intelligencepos.mvp.activity.RemarkActivity;
import com.holderzone.intelligencepos.mvp.activity.TableActivity;
import com.holderzone.intelligencepos.mvp.hall.contract.OrderDetailContract;
import com.holderzone.intelligencepos.mvp.hall.dialog.DineDishesOperationDialogFragment;
import com.holderzone.intelligencepos.mvp.hall.presenter.OrderDetailPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.OrderDetailPopup;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by LiTao on 2017-9-5.
 * 点餐详情页面
 */

public class OrderDetailActivity extends BaseActivity<OrderDetailContract.Presenter> implements OrderDetailContract.View, DineDishesOperationDialogFragment.SetOnOperationClickListener, OrderDetailPopup.OnItemClickListener {
    @BindView(R.id.snack_detail_dishes_count)
    TextView snackDetailDishesCount;
    @BindView(R.id.snack_detail_rv)
    RecyclerView snackDetailRv;
    @BindView(R.id.snack_detail_order)
    Button snackDetailOrder;
    @BindView(R.id.bill_detail_multistateview)
    MultiStateView content;
    public static final String SALES_ORDER_GUID_KEY = "SALES_ORDER_GUID_KEY";
    public static final String SALES_ORDER_DISHES_GROUP_KEY = "SALES_ODER_DISHES_GROUP_KEY";
    public static final String SALES_RECORDDIShES_KEY = "SALES_RECORDDIShES_KEY";
    public static final String DINING_TABLE_GUID_KEY = "DINING_TABLE_GUID_KEY";
    public static final String EXTRAS_USE_MEMBER_PRICE = "EXTRAS_USE_MEMBER_PRICE";
    public static final String BACK_STATE = "BACK_STATE";
    public static final String DINING_TABLE_NAME_KEY = "DINING_TABLE_NAME_KEY";
    private static final int REMARK_REQUEST = 101;
    private static final int FINISH_ACTIVITY_REQUEST = 202;
    public static final int BACK_STATE_0 = 0;
    private static int KEY_INTENT_ALL_DISHES_REMARK = 111;
    @BindView(R.id.title)
    Title mTitle;
    /**
     * 已点菜品
     */
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesEList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter<SalesOrderBatchDishesE> mAdapter;
    /**
     * 订单菜品实体
     */
    private OrderDishesGroup mOrderDishesGroup;
    /**
     * 估清信息
     */
    private Map<String, DishesEstimateRecordDishes> mEstimateMap = new HashMap<>();
    private String mSalesOrderBatchGuid = UUID.randomUUID().toString();
    ArrayMap<String, Double> currentOrderDishCountMap = new ArrayMap<>();

    /**
     * 是否是赠送全部
     */
    private boolean isGiftAll;
    /**
     * 是否是全部挂起
     */
    private boolean isHangAll;
    /**
     * menu文字 挂起全部或者全部不挂起
     */
    private String mHangText;
    /**
     * menu文字 赠送全部或者全不赠送
     */
    private String mGiftText;
    /**
     * 订单GUID
     */
    private String mSalesOrderGuid;
    /**
     * 餐桌GUID
     */
    private String mDiningTableGuid;
    /**
     * 餐桌名称
     */
    private String mDiningTableName;

    private boolean useMemberPrice;

    public static Intent newIntent(Context context, OrderDishesGroup dishesGroup, Map<String, DishesEstimateRecordDishes> estimateMap
            , String salesOrderGUID, String diningTableGUID, String tableName, boolean useMemberPrice) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SALES_ORDER_DISHES_GROUP_KEY, dishesGroup);
        bundle.putSerializable(SALES_RECORDDIShES_KEY, (Serializable) estimateMap);
        bundle.putString(SALES_ORDER_GUID_KEY, salesOrderGUID);
        bundle.putString(DINING_TABLE_GUID_KEY, diningTableGUID);
        bundle.putBoolean(EXTRAS_USE_MEMBER_PRICE, useMemberPrice);
        bundle.putString(DINING_TABLE_NAME_KEY, tableName);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mOrderDishesGroup = extras.getParcelable(SALES_ORDER_DISHES_GROUP_KEY);
        mEstimateMap = (Map<String, DishesEstimateRecordDishes>) extras.getSerializable(SALES_RECORDDIShES_KEY);
        mSalesOrderGuid = extras.getString(SALES_ORDER_GUID_KEY);
        mDiningTableGuid = extras.getString(DINING_TABLE_GUID_KEY);
        mDiningTableName = extras.getString(DINING_TABLE_NAME_KEY);
        useMemberPrice = extras.getBoolean(EXTRAS_USE_MEMBER_PRICE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Nullable
    @Override
    protected OrderDetailContract.Presenter initPresenter() {
        return new OrderDetailPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //初始化adapter
        initAdapter();
        //防止多次点击 下单
        RxView.clicks(snackDetailOrder).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
                    salesOrderBatchE.setSalesOrderBatchGUID(mSalesOrderBatchGuid);
                    salesOrderBatchE.setSalesOrderGUID(mSalesOrderGuid);
                    salesOrderBatchE.setDiningTableGUID(mDiningTableGuid);
                    salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(mSalesOrderBatchDishesEList);
                    salesOrderBatchE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    mPresenter.createOrder(salesOrderBatchE);
                });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //已点菜品数据
        mSalesOrderBatchDishesEList.clear();
        mSalesOrderBatchDishesEList.addAll(mOrderDishesGroup.getDishesList());
        snackDetailDishesCount.setText("未下单 " + "(" + mSalesOrderBatchDishesEList.size() + ")");
        //制空DishesE
        for (SalesOrderBatchDishesE batch : mSalesOrderBatchDishesEList) {
            batch.setDishesE(null);
        }
        //计算总计
        initAmount();
        notifyOrderDishesDataSetChanged();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map) {
        mEstimateMap.clear();
        mEstimateMap.putAll(map);
        notifyOrderDishesDataSetChanged();
    }

    @Override
    public void onCreateOrderSuccess(SalesOrderE salesOrderE) {
        mOrderDishesGroup.getDishesList().clear();
        mSalesOrderBatchDishesEList.clear();
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    @Override
    public void onCreateOrderFiled(String msg) {
        mPresenter.getDishesEstimate();
        mDialogFactory.showConfirmDialog(getString(R.string.add_dishes_failed_by_estimate_dialog), false, ""
                , R.color.layout_stroke_focused, true, "确认修改", R.drawable.selector_button_blue, null);
    }

    @Override
    public void onDispose() {
    }

    /**
     * 返回上一个页面
     */
    private void backToLastActivity(int state) {
        mOrderDishesGroup.getDishesList().clear();
        mOrderDishesGroup.setDishesList((ArrayList<SalesOrderBatchDishesE>) mSalesOrderBatchDishesEList);
        Intent intent = new Intent(this, OrderDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SALES_ORDER_DISHES_GROUP_KEY, mOrderDishesGroup);
        bundle.putInt(BACK_STATE, state);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        Log.i("mOrderDishesGroup", new Gson().toJson(mOrderDishesGroup));
        finishActivity();
    }

    //menu菜单
    private void titleMoreClick() {
        // 检查是否是全部挂起
        checkIsHangAll();
        // 检查菜品是否全部赠送
        checkIsGiftAll();
        // 弹出菜单窗口
        OrderDetailPopup orderDetailPopup = new OrderDetailPopup(this);
        orderDetailPopup.setHangAllText(mHangText);
        orderDetailPopup.setGiftAllText(mGiftText);
        orderDetailPopup.showOnAnchor(mTitle.findViewById(R.id.ll_menu),
                RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
    }

    //清空全部
    private void clearAll() {
        mSalesOrderBatchDishesEList.clear();
        mOrderDishesGroup.getDishesList().clear();
        //已点菜品为0
        snackDetailDishesCount.setText(getString(R.string.order_0));
        mAdapter.notifyDataSetChanged();
        //返回点菜页面
        backToLastActivity(BACK_STATE_0);
    }

    //挂起全部
    private void hangAll() {
        if (!isHangAll) {
            for (SalesOrderBatchDishesE batchDishesE : mSalesOrderBatchDishesEList) {
                batchDishesE.setCheckStatus(0);
            }
        } else {
            for (SalesOrderBatchDishesE batchDishesE : mSalesOrderBatchDishesEList) {
                batchDishesE.setCheckStatus(1);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    //赠送全部
    private void giftAll() {
        if (!isGiftAll) {
            for (SalesOrderBatchDishesE batchDishesE : mSalesOrderBatchDishesEList) {
                batchDishesE.setGift(1);
            }
        } else {
            for (SalesOrderBatchDishesE batchDishesE : mSalesOrderBatchDishesEList) {
                batchDishesE.setGift(0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    //检查是否是赠送全部
    private void checkIsGiftAll() {
        isGiftAll = true;
        for (SalesOrderBatchDishesE batchDishesE : mSalesOrderBatchDishesEList) {
            if (batchDishesE.getGift() == 0) {
                isGiftAll = false;
            }
        }
        if (isGiftAll) {
            mGiftText = getString(R.string.order_detail_gift_not);
        } else {
            mGiftText = getString(R.string.order_detail_gift_all);
        }
    }

    //检查是否是挂起全部
    private void checkIsHangAll() {
        isHangAll = true;
        for (SalesOrderBatchDishesE batchDishesE : mSalesOrderBatchDishesEList) {
            if (batchDishesE.getCheckStatus() == 1) {
                isHangAll = false;
            }
        }
        if (isHangAll) {
            mHangText = getString(R.string.order_detail_hang_not);
        } else {
            mHangText = getString(R.string.order_detail_hang_all);
        }
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<SalesOrderBatchDishesE>(this, R.layout.item_order_detail, mSalesOrderBatchDishesEList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                boolean itemUseMemberPrice = useMemberPrice && salesOrderBatchDishesE.getMemberPrice() != null
                        && salesOrderBatchDishesE.getMemberPrice().compareTo(salesOrderBatchDishesE.getPrice()) != 0;
                //隐藏最后一个item的虚线
                if (position == mSalesOrderBatchDishesEList.size() - 1) {
                    holder.setVisible(R.id.itemDivider, false);
                } else {
                    holder.setVisible(R.id.itemDivider, true);
                }
                //菜品名称
                holder.setText(R.id.dishesName, "");
                if (salesOrderBatchDishesE.getCheckStatus() != null) {
                    if (salesOrderBatchDishesE.getCheckStatus() == 0) {
                        //挂起状态
                        holder.setVisible(R.id.dishes_state, true);
                        holder.setBackgroundRes(R.id.dishes_state, R.drawable.hang);
                    } else {
                        holder.setVisible(R.id.dishes_state, false);
                    }
                }
                //是否达到最大估清数量
                if (salesOrderBatchDishesE.getMaxValue() != null && salesOrderBatchDishesE.getOrderCount() > salesOrderBatchDishesE.getMaxValue()) {
                    holder.setTextColorRes(R.id.count, R.color.layout_bg_red_f56766);
                    //设置菜品名称
                    holder.setAppendText(R.id.dishesName, salesOrderBatchDishesE.getDishesName(), R.color.layout_bg_red_f56766);
                } else {
                    holder.setTextColorRes(R.id.count, R.color.layout_bg_black_000000);
                    //设置菜品名称
                    holder.setAppendText(R.id.dishesName, salesOrderBatchDishesE.getDishesName(), R.color.layout_bg_black_000000);
                }
                //是否打印
                if (salesOrderBatchDishesE.getKitchenPrintStatus() != null) {
                    if (salesOrderBatchDishesE.getKitchenPrintStatus() == 1) {
                        //不打印
                        holder.setVisible(R.id.no_print, true);
                    } else {
                        holder.setVisible(R.id.no_print, false);
                    }
                }
                //数量
                holder.setText(R.id.count, getString(R.string.dishes_count_str, ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getOrderCount())));
                //总价
                SpanUtils priceBuilder = new SpanUtils();
                if (salesOrderBatchDishesE.getGift() != null && salesOrderBatchDishesE.getGift() == 1) {
                    //赠送
                    priceBuilder.append(getString(R.string.dishes_give)).setForegroundColor(getResources().getColor(R.color.tv_text_green_01b6ad))
                            .append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getPrice()))))
                            .setForegroundColor(getResources().getColor(R.color.common_text_color_707070));
                } else if (itemUseMemberPrice) {
                    priceBuilder.append(getString(R.string.member_mark)).setForegroundColor(getResources().getColor(R.color.layout_bg_orange_f4a902))
                            .append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getMemberPrice()))))
                            .setForegroundColor(getResources().getColor(R.color.common_text_color_707070));
                } else {
                    priceBuilder.append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getPrice()))))
                            .setForegroundColor(getResources().getColor(R.color.common_text_color_707070));
                }
                holder.setText(R.id.price, priceBuilder.create());
                //是否有备注
                if (salesOrderBatchDishesE.getArrayOfDishesRemarkE() != null && salesOrderBatchDishesE.getArrayOfDishesRemarkE().size() != 0) {
                    //有备注
                    String remark = "";
                    for (DishesRemarkE dishesRemarkE : salesOrderBatchDishesE.getArrayOfDishesRemarkE()) {
                        remark += dishesRemarkE.getName() + ",";
                    }
                    remark = remark.substring(0, remark.length() - 1);
                    holder.setText(R.id.remark, remark);
                    holder.setVisible(R.id.remark, true);
                } else {
                    holder.setVisible(R.id.remark, false);
                }
                //是否有做法
                if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() != null && salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() != 0) {
                    String practiceInfo = "";
                    double cookMethodPrice = 0.0;
                    for (DishesPracticeE dishesPractice : salesOrderBatchDishesE.getArrayOfDishesPracticeE()) {
                        practiceInfo += dishesPractice.getName() + ",";
                        cookMethodPrice = ArithUtil.add(cookMethodPrice, ArithUtil.mul(dishesPractice.getFees(), salesOrderBatchDishesE.getOrderCount()));
                    }
                    practiceInfo = practiceInfo.substring(0, practiceInfo.length() - 1);
                    holder.setText(R.id.cuisineMethodInfo, practiceInfo);
                    holder.setText(R.id.cuisineMethodPrice, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(cookMethodPrice)));
                    holder.setVisible(R.id.cuisineMethodPrice, true);
                    holder.setVisible(R.id.dishesCookMethodPanel, true);
                } else if (salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1) {//自选套餐
                    StringBuilder packGroup = new StringBuilder();
                    for (SalesOrderBatchDishesE batchDishesE : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                        //套餐子项
                        int count = batchDishesE.getPackageDishesOrderCount();
                        double singleCount = batchDishesE.getSingleCount();
                        String unit = batchDishesE.getCheckUnit();
                        if (unit == null) unit = "";
                        if (singleCount == 1) {
                            if (count == 1) {
                                packGroup.append(batchDishesE.getDishesName()).append("+");
                            } else {
                                packGroup.append(batchDishesE.getDishesName()).append("×").append(count).append("+");
                            }
                        } else {
                            if (count == 1) {
                                packGroup.append(batchDishesE.getDishesName()).append(ArithUtil.stripTrailingZeros(singleCount))
                                        .append(unit).append("+");
                            } else {
                                packGroup.append(batchDishesE.getDishesName()).append(ArithUtil.stripTrailingZeros(singleCount))
                                        .append(unit).append("×").append(count).append("+");
                            }
                        }
                    }
                    packGroup.deleteCharAt(packGroup.length() - 1);
                    holder.setVisible(R.id.dishesCookMethodPanel, true);
                    holder.setVisible(R.id.cuisineMethodPrice, false);
                    holder.setText(R.id.cuisineMethodInfo, packGroup);
                } else {
                    holder.setVisible(R.id.dishesCookMethodPanel, false);
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mSalesOrderBatchDishesEList.get(position).getGift() == null) {
                    mSalesOrderBatchDishesEList.get(position).setGift(0);
                }
                if (mSalesOrderBatchDishesEList.get(position).getKitchenPrintStatus() == null) {
                    mSalesOrderBatchDishesEList.get(position).setKitchenPrintStatus(1);
                }
                SalesOrderBatchDishesE batchDishesE = mSalesOrderBatchDishesEList.get(position);
                mDialogFactory.showDineDishesOperationDialog(batchDishesE.getDishesName(), batchDishesE.getCheckStatus()
                        , batchDishesE.getGift(), batchDishesE.getKitchenPrintStatus()
                        , position, OrderDetailActivity.this);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        snackDetailRv.setLayoutManager(new LinearLayoutManager(this));
        snackDetailRv.setAdapter(mAdapter);
    }

    @Override
    public void onChangeNumberClick(int position) {
        Double orderCount = mSalesOrderBatchDishesEList.get(position).getOrderCount();
        double maxValue = -1;
        DishesEstimateRecordDishes estimate = mEstimateMap.get(mSalesOrderBatchDishesEList.get(position).getDishesGUID().toLowerCase());
        if (estimate != null) {
            maxValue = mSalesOrderBatchDishesEList.get(position).getMaxValue();
        }
        SalesOrderBatchDishesE batchDishesE = mSalesOrderBatchDishesEList.get(position);
        startActivityForResult(DineChangeNumberActivity.newIntent(this, batchDishesE.getDishesName()
                , orderCount, maxValue, batchDishesE.getGift(), position
                , batchDishesE.getDishesUnit(), batchDishesE.getCheckStatus()), REMARK_REQUEST);
    }

    @Override
    public void onGiftClick(int position, int giftState) {
        mSalesOrderBatchDishesEList.get(position).setGift(giftState);
        mSalesOrderBatchDishesEList.get(position).setGift(giftState);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoPrintClick(int position, int printState) {
        mSalesOrderBatchDishesEList.get(position).setKitchenPrintStatus(printState);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemarkClick(int position) {
        String inputRemarks;
        if (mSalesOrderBatchDishesEList.get(position).getArrayOfDishesRemarkE() != null && mSalesOrderBatchDishesEList.get(position).getArrayOfDishesRemarkE().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (DishesRemarkE dishesRemarkE : mSalesOrderBatchDishesEList.get(position).getArrayOfDishesRemarkE()) {
                stringBuilder.append(dishesRemarkE.getName()).append(",");
            }
            inputRemarks = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        } else {
            inputRemarks = "";
        }
        startActivityForResult(RemarkActivity.newIntent(this, inputRemarks, position), REMARK_REQUEST);
    }

    @Override
    public void onDeleteClick(int position) {
        mDialogFactory.showConfirmDialog(getString(R.string.confirm_delete) + mSalesOrderBatchDishesEList.get(position).getDishesName()
                , getString(R.string.cancel), getString(R.string.confirm_delete), new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {
                    }

                    @Override
                    public void onPosClick() {
                        mSalesOrderBatchDishesEList.remove(position);
                        //计算总计
                        initAmount();
                        //重置已点菜品数据
                        snackDetailDishesCount.setText("已点菜品 " + "(" + mSalesOrderBatchDishesEList.size() + ")");
                        notifyOrderDishesDataSetChanged();
                        if (mSalesOrderBatchDishesEList.size() == 0) {
                            backToLastActivity(BACK_STATE_0);
                        }
                    }
                });
    }

    @Override
    public void onHang(int position, int hangState) {
        mSalesOrderBatchDishesEList.get(position).setCheckStatus(hangState);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 计算下单总价
     **/
    private void initAmount() {
        double amount = 0;
        for (SalesOrderBatchDishesE orderBatchDishesE : mSalesOrderBatchDishesEList) {
            //菜品总价
            amount = ArithUtil.add(amount, ArithUtil.mul(orderBatchDishesE.getOrderCount(),
                    useMemberPrice ? orderBatchDishesE.getMemberPrice() : orderBatchDishesE.getPrice()));
            //菜品做法总价
            if (orderBatchDishesE.getArrayOfDishesPracticeE() != null) {
                for (DishesPracticeE dishesPracticeE : orderBatchDishesE.getArrayOfDishesPracticeE()) {
                    amount = ArithUtil.add(amount, ArithUtil.mul(dishesPracticeE.getFees(), orderBatchDishesE.getOrderCount()));
                }
            }
        }
        //底部按钮确认下单价格显示
        snackDetailOrder.setText(getString(R.string.confirm_button_text_str, ArithUtil.stripTrailingZeros(amount)));
    }

    /**
     * 估清菜品 刷新adapter
     */
    private void notifyOrderDishesDataSetChanged() {
        currentOrderDishCountMap.clear();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEList) {
            String dishesGUD = salesOrderBatchDishesE.getDishesGUID().toLowerCase();
            Double count = currentOrderDishCountMap.get(dishesGUD);
            if (count == null) {
                count = 0d;
            }
            count = ArithUtil.add(count, salesOrderBatchDishesE.getOrderCount());
            currentOrderDishCountMap.put(dishesGUD, count);
        }
        //估清业务处理
        for (DishesEstimateRecordDishes dishesEstimateRecordDishes : mEstimateMap.values()) {
            dishesEstimateRecordDishes.setResidueCount(ArithUtil.sub(dishesEstimateRecordDishes.getEstimateCount(), dishesEstimateRecordDishes.getSaleCount()));
            dishesEstimateRecordDishes.setCurrentOrderCount(0d);
        }
        for (SalesOrderBatchDishesE record : mSalesOrderBatchDishesEList) {
            DishesEstimateRecordDishes estimate = mEstimateMap.get(record.getDishesGUID().toLowerCase());
            if (estimate != null) {
                double dishesOrderTotalCount = currentOrderDishCountMap.get(record.getDishesGUID().toLowerCase());
                double residueCount = ArithUtil.sub(estimate.getEstimateCount(), estimate.getSaleCount());
                if (residueCount >= dishesOrderTotalCount) {
                    //点菜数量小于估清剩余数量
                    estimate.setResidueCount(ArithUtil.sub(estimate.getResidueCount(), record.getOrderCount()));
                    record.setMaxValue(ArithUtil.add(ArithUtil.sub(residueCount, dishesOrderTotalCount), record.getOrderCount()));
                } else {
                    //点菜数量大于估清数量
                    if (record.getOrderCount() < estimate.getResidueCount()) {
                        //点菜数量小于剩余数量
                        record.setMaxValue(record.getOrderCount());
                        estimate.setCurrentOrderCount(ArithUtil.add(estimate.getCurrentOrderCount(), record.getOrderCount()));
                        estimate.setResidueCount(ArithUtil.sub(estimate.getResidueCount(), record.getOrderCount()));
                    } else {
                        //点菜数量大于等于剩余数量
                        record.setMaxValue(estimate.getResidueCount());
                        estimate.setCurrentOrderCount(ArithUtil.add(estimate.getCurrentOrderCount(), estimate.getResidueCount()));
                        estimate.setResidueCount(0d);
                    }
                }
            }
        }
        //刷新adapter
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            backToLastActivity(BACK_STATE_0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REMARK_REQUEST) {
            if (resultCode == RemarkActivity.DISHES_RESULT_CODE) {
                //备注页面数据回传
                Bundle bundle = data.getExtras();
                DishesRemarkE dishesRemarkE = bundle.getParcelable(RemarkActivity.ARGUMENT_EXTRA_NEW_SELECTED_KEY);
                int position = bundle.getInt(RemarkActivity.DISHES_ITEM_POSITION);
                if ("".equals(dishesRemarkE.getName())) {
                    if (mSalesOrderBatchDishesEList.get(position).getArrayOfDishesRemarkE() != null) {
                        mSalesOrderBatchDishesEList.get(position).getArrayOfDishesRemarkE().clear();
                    }
                } else {
                    List<DishesRemarkE> dishesRemarkES = new ArrayList<>();
                    dishesRemarkES.add(dishesRemarkE);
                    mSalesOrderBatchDishesEList.get(position).setArrayOfDishesRemarkE(dishesRemarkES);
                }
                mAdapter.notifyDataSetChanged();
            } else if (resultCode == ChangeNumberActivity.DISHES_RESULT_CODE) {
                //修改菜品数量页面数据回传
                Bundle bundle = data.getExtras();
                Double number = bundle.getDouble(ChangeNumberActivity.ARGUMENT_EXTRA_DISHES_COUNT_KEY);
                int position = bundle.getInt(ChangeNumberActivity.DISHES_ITEM_POSITION);
                //重置对应菜品数据
                mSalesOrderBatchDishesEList.get(position).setOrderCount(number);
                //计算总计
                initAmount();
                notifyOrderDishesDataSetChanged();
            }
        } else if (requestCode == FINISH_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                backToLastActivity(BACK_STATE_0);
            }
        } else if (requestCode == KEY_INTENT_ALL_DISHES_REMARK && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null && bundle.containsKey("allDishesE")) {
                List<SalesOrderBatchDishesE> allDishesE = bundle.getParcelableArrayList("allDishesE");
                if (allDishesE != null) {
                    for (int i = 0; i < allDishesE.size(); i++) {
                        List<DishesRemarkE> remarkEList = allDishesE.get(i).getArrayOfDishesRemarkE();
                        mSalesOrderBatchDishesEList.get(i).setArrayOfDishesRemarkE(remarkEList);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(int itemType) {
        switch (itemType) {
            case OrderDetailPopup.ITEM_TYPE_BATCH_NOTE://批量备注
                startActivityForResult(AllDishesRemarkChoiceActivity.newIntent(OrderDetailActivity.this, mSalesOrderBatchDishesEList, useMemberPrice), KEY_INTENT_ALL_DISHES_REMARK);
                break;
            case OrderDetailPopup.ITEM_TYPE_HANG_ALL_OR_NOT:// 挂起
                PermissionManager.checkPermission(PermissionManager.PERMISSION_HANG_UP, this::hangAll);
                break;
            case OrderDetailPopup.ITEM_TYPE_GIFT_ALL_OR_NOT:// 赠送
                PermissionManager.checkPermission(PermissionManager.PERMISSION_GIFT_DISHES, this::giftAll);
                break;
            case OrderDetailPopup.ITEM_TYPE_CLEAR_ALL_OR_NOT:
                mDialogFactory.showConfirmDialog(getString(R.string.clear_all_dialog_content), getString(R.string.cancel), getString(R.string.confirm_clear), new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {
                    }

                    @Override
                    public void onPosClick() {
                        //清空全部
                        clearAll();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initTitle() {
        mTitle.setOnReturnClickListener(() -> {
            backToLastActivity(BACK_STATE_0);
        });
        mTitle.setTitleText(getString(R.string.confirm_dishes_title, mDiningTableName));
        mTitle.setOnMenuClickListener(() -> {
            //menu菜单
            titleMoreClick();
        });
    }
}
