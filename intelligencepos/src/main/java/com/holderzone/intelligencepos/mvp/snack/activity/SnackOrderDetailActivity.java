package com.holderzone.intelligencepos.mvp.snack.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.DishesOperationDialogFragment;
import com.holderzone.intelligencepos.mvp.activity.BalanceAccountsActivity;
import com.holderzone.intelligencepos.mvp.activity.ChangeNumberActivity;
import com.holderzone.intelligencepos.mvp.activity.ChoosePlateNumberActivity;
import com.holderzone.intelligencepos.mvp.activity.RemarkActivity;
import com.holderzone.intelligencepos.mvp.activity.SnackDishesActivity;
import com.holderzone.intelligencepos.mvp.activity.SnackOpenTableActivity;
import com.holderzone.intelligencepos.mvp.contract.SnackOrderDetailContract;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.SnackOrderDetailPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.SnackOrderDetailPopup;
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
import butterknife.OnClick;

import static com.holderzone.intelligencepos.mvp.activity.SnackOpenTableActivity.KEY_CALLBACK;

/**
 * Created by LiTao on 2017-8-3.
 * 账单详情页面
 */

public class SnackOrderDetailActivity extends BaseActivity<SnackOrderDetailContract.Presenter> implements SnackOrderDetailContract.View, DishesOperationDialogFragment.SetOnOperationClickListener, SnackOrderDetailPopup.OnItemClickListener {

    private static final int REQUEST_INTENT = 111;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_more_image)
    ImageView titleMoreImage;
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
    public static final String EXTRAS_USE_MEMBER_PRICE = "EXTRAS_USE_MEMBER_PRICE";
    public static final String EXTRAS_MEMBER_GUID = "EXTRAS_MEMBER_GUID";
    private static final int REMARK_REQUEST = 101;
    private static final int SELECTED_ORG_REQUEST = 201;
    private static final int FINISH_ACTIVITY_REQUEST = 202;
    private static int KEY_INTENT_ALL_DISHES_REMARK = 203;
    @BindView(R.id.title_refresh)
    TextView titleRefresh;
    /**
     * 账单GUID
     */
    private String mSalesOrderGUID;
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
     * 号牌
     */
    private String mNumberPlate;
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
     * menu文字 赠送全部或者全不赠送
     */
    private String mGiftText;
    /**
     * 是否选择号牌
     */
    private boolean isChickLicensePlate = false;
    /**
     * 就餐人数
     */
    private int mGuestCount = 1;
    private boolean useMemberPrice;
    private String memberGuid;

    public static Intent newIntent(Context context, OrderDishesGroup dishesGroup, Map<String, DishesEstimateRecordDishes> estimateMap, boolean useMemberPrice, String memberGuid) {
        Intent intent = new Intent(context, SnackOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SALES_ORDER_DISHES_GROUP_KEY, dishesGroup);
        bundle.putBoolean(EXTRAS_USE_MEMBER_PRICE, useMemberPrice);
        bundle.putString(EXTRAS_MEMBER_GUID, memberGuid);
        bundle.putSerializable(SALES_RECORDDIShES_KEY, (Serializable) estimateMap);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mOrderDishesGroup = extras.getParcelable(SALES_ORDER_DISHES_GROUP_KEY);
        mEstimateMap = (Map<String, DishesEstimateRecordDishes>) extras.getSerializable(SALES_RECORDDIShES_KEY);
        useMemberPrice = extras.getBoolean(EXTRAS_USE_MEMBER_PRICE);
        memberGuid = extras.getString(EXTRAS_MEMBER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_sanck_order_detail;
    }

    @Override
    protected SnackOrderDetailContract.Presenter initPresenter() {
        return new SnackOrderDetailPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //重新加载点击事件
        titleText.setText(getString(R.string.order_detail_title));
        //设置刷新不可见
        titleRefresh.setVisibility(View.INVISIBLE);
        //初始化adapter
        initAdapter();
        //防止多次点击
        RxView.clicks(snackDetailOrder).throttleFirst(1L, TimeUnit.SECONDS)
                .flatMap(o -> RepositoryImpl.getInstance().getIsStartFlapper())
                .subscribe(aBoolean -> {
                    isChickLicensePlate = aBoolean;
                    mPresenter.getFastSalesGuestCount();
                });
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<SalesOrderBatchDishesE>(this, R.layout.item_snack_order_detail, mSalesOrderBatchDishesEList) {
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
                //是否达到最大估清数量
                if (salesOrderBatchDishesE.getMaxValue() != null && salesOrderBatchDishesE.getOrderCount() > salesOrderBatchDishesE.getMaxValue()) {
                    holder.setTextColorRes(R.id.count, R.color.common_text_color_f56766);
                    //设置菜品名称
                    holder.setAppendText(R.id.dishesName, salesOrderBatchDishesE.getDishesName(), R.color.common_text_color_f56766);
                } else {
                    holder.setTextColorRes(R.id.count, R.color.common_text_color_000000);
                    //设置菜品名称
                    holder.setAppendText(R.id.dishesName, salesOrderBatchDishesE.getDishesName(), R.color.card_item_type_text_color);
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
                List<DishesPracticeE> arrayOfDishesPracticeE = salesOrderBatchDishesE.getArrayOfDishesPracticeE();
                boolean isPackage = salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1;
                if ((arrayOfDishesPracticeE == null || arrayOfDishesPracticeE.size() == 0) && !isPackage) {
                    holder.setVisible(R.id.dishesCookMethodPanel, false);
                } else {
                    holder.setVisible(R.id.dishesCookMethodPanel, true);
                    if (isPackage) {
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
                        holder.setText(R.id.cuisineMethodInfo, packGroup);
                        holder.setVisible(R.id.cuisineMethodPrice, false);
                    } else {
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
                    }
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
                mDialogFactory.showDishesOperationDialog(mSalesOrderBatchDishesEList.get(position).getDishesName()
                        , mSalesOrderBatchDishesEList.get(position).getGift(), mSalesOrderBatchDishesEList.get(position).getKitchenPrintStatus(), position, SnackOrderDetailActivity.this);
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
    protected void initData(@Nullable Bundle savedInstanceState) {
        //已点菜品数据
        mSalesOrderBatchDishesEList.clear();
        mSalesOrderBatchDishesEList.addAll(mOrderDishesGroup.getOrderDishesRecordList());
        snackDetailDishesCount.setText("已点菜品 " + "(" + mSalesOrderBatchDishesEList.size() + ")");
        //制空DishesE
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEList) {
            salesOrderBatchDishesE.setDishesE(null);
        }
        //计算总计
        initAmount();
        notifyOrderDishesDataSetChanged();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @OnClick({R.id.back_image, R.id.title_text, R.id.title_more_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
            case R.id.title_text:
                backToLastActivity();
                break;
            case R.id.title_more_image:
                //menu菜单
                titleMoreClick();
                break;
            default:
                break;
        }
    }

    //menu菜单
    private void titleMoreClick() {
        //检查菜品是否全部赠送
        checkIsGiftAll();
        // 弹出菜单窗口
        SnackOrderDetailPopup snackOrderDetailPopup = new SnackOrderDetailPopup(this);
        snackOrderDetailPopup.setGiftAllText(mGiftText);
        snackOrderDetailPopup.showOnAnchor(titleMoreImage,
                RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
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

    //清空全部
    private void clearAll() {
        mSalesOrderBatchDishesEList.clear();
        mOrderDishesGroup.getOrderDishesRecordList().clear();
        //已点菜品为0
        snackDetailDishesCount.setText(getString(R.string.order_0));
        mAdapter.notifyDataSetChanged();
        //返回点菜页面
        backToLastActivity();
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

    @Override
    public void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map) {
        mEstimateMap.clear();
        mEstimateMap.putAll(map);
        notifyOrderDishesDataSetChanged();
    }

    @Override
    public void onCreateOrderSuccess(SalesOrderE salesOrderE) {
        mOrderDishesGroup.getOrderDishesRecordList().clear();
        mSalesOrderBatchDishesEList.clear();
        mNumberPlate = null;
        startActivityForResult(BalanceAccountsActivity.newIntent(this, salesOrderE.getSalesOrderGUID(), 1), FINISH_ACTIVITY_REQUEST);
    }

    @Override
    public void onCreateOrderFiled(String msg) {
        mPresenter.getDishesEstimate();
        mDialogFactory.showConfirmDialog(getString(R.string.content_estimate), false, "", R.color.additional_frees_count_select, true, "确认修改", R.drawable.selector_button_blue, null);
    }

    @Override
    public void responseFastSalesGuestCountSuccess(boolean isFastSalesGuestCount) {
        if (isFastSalesGuestCount) {
            startActivityForResult(SnackOpenTableActivity.newIntent(this, mGuestCount + ""), REQUEST_INTENT);
        } else {
            showLicensePlate();
        }
    }

    private void showLicensePlate() {
        if (isChickLicensePlate) {// 选择号牌
            startActivityForResult(ChoosePlateNumberActivity.newIntent(this), SELECTED_ORG_REQUEST);
        } else {// 无号牌
            SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
            salesOrderBatchE.setSalesOrderBatchGUID(mSalesOrderBatchGuid);
            salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(mSalesOrderBatchDishesEList);
            mPresenter.createOrder(salesOrderBatchE, mNumberPlate, mGuestCount, memberGuid);
        }
    }

    @Override
    public void responseFastSalesGuestCountFail() {
    }

    @Override
    public void onChangeNumberClick(int position) {
        Double orderCount = mSalesOrderBatchDishesEList.get(position).getOrderCount();
        double maxValue = -1;
        DishesEstimateRecordDishes estimate = mEstimateMap.get(mSalesOrderBatchDishesEList.get(position).getDishesGUID().toLowerCase());
        if (estimate != null) {
            maxValue = mSalesOrderBatchDishesEList.get(position).getMaxValue();
        }
        startActivityForResult(ChangeNumberActivity.newIntent(this, mSalesOrderBatchDishesEList.get(position).getDishesName()
                , orderCount, maxValue, mSalesOrderBatchDishesEList.get(position).getGift(), position
                , mSalesOrderBatchDishesEList.get(position).getDishesUnit()), REMARK_REQUEST);
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
                            backToLastActivity();
                        }
                    }
                });
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
        } else if (requestCode == SELECTED_ORG_REQUEST) {
            if (resultCode == RESULT_OK) {
                //选择号牌回调
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    mNumberPlate = bundle.getString(ChoosePlateNumberActivity.ORG_NUMBER);
                    //下单请求
                    SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
                    salesOrderBatchE.setSalesOrderBatchGUID(mSalesOrderBatchGuid);
                    salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(mSalesOrderBatchDishesEList);
                    mPresenter.createOrder(salesOrderBatchE, mNumberPlate, mGuestCount, memberGuid);
                }
            }
        } else if (requestCode == FINISH_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                backToLastActivity();
            }
        } else if (requestCode == REQUEST_INTENT) {
            if (resultCode == RESULT_OK) {
                try {
                    String number = data.getStringExtra(KEY_CALLBACK);
                    mGuestCount = Integer.valueOf(number);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showLicensePlate();
            }
        }else if (requestCode == KEY_INTENT_ALL_DISHES_REMARK ) {
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if (bundle != null && bundle.containsKey("allDishesE")) {
                    List<SalesOrderBatchDishesE> allDishesE = bundle.getParcelableArrayList("allDishesE");
                    if(allDishesE!=null){
                        for (int i=0;i<allDishesE.size();i++){
                            List<DishesRemarkE> remarkEList = allDishesE.get(i).getArrayOfDishesRemarkE();
                            mSalesOrderBatchDishesEList.get(i).setArrayOfDishesRemarkE(remarkEList);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
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
        snackDetailOrder.setText(getString(R.string.sure_order_str, ArithUtil.stripTrailingZeros(amount)));
    }

    /**
     * 返回上一个页面
     */
    private void backToLastActivity() {
        mOrderDishesGroup.getOrderDishesRecordList().clear();
        mOrderDishesGroup.setOrderDishesRecordList(mSalesOrderBatchDishesEList);
        Intent intent = new Intent(this, SnackDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SALES_ORDER_DISHES_GROUP_KEY, mOrderDishesGroup);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            backToLastActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public void onDispose() {
    }

    @Override
    public void onItemClick(int itemType) {
        switch (itemType) {
            case SnackOrderDetailPopup.ITEM_TYPE_BATCH_NOTE://批量备注
                startActivityForResult(SnackAllDishesRemarkChoiceActivity.newIntent(SnackOrderDetailActivity.this, mSalesOrderBatchDishesEList, useMemberPrice ), KEY_INTENT_ALL_DISHES_REMARK);
                break;
            case SnackOrderDetailPopup.ITEM_TYPE_GIFT_ALL_OR_NOT:
                //赠送全部
                PermissionManager.checkPermission(PermissionManager.PERMISSION_GIFT_DISHES, this::giftAll);
                break;
            case SnackOrderDetailPopup.ITEM_TYPE_CLEAR_ALL_OR_NOT:
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
}
