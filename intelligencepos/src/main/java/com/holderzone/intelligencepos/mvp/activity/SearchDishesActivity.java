package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.impl.AddPracticeDishesDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ChangeOrderDishesCountDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ReducePracticeDishesDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.SearchDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroupDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.PackageItemE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.SearchDishesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CloneUtils;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.holderzone.intelligencepos.mvp.activity.SnackDishesActivity.REQUEST_CODE_PACKAGE_GROUP;

/**
 * 点菜时查询菜品页面
 */
public class SearchDishesActivity extends BaseActivity<SearchDishesContract.Presenter> implements SearchDishesContract.View,
        ReducePracticeDishesDialogFragment.CallBackReduceDialog,
        AddPracticeDishesDialogFragment.CallBackPracticeOK,
        ChangeOrderDishesCountDialogFragment.ChangeOrderDishesCountListener {

    private static final String KEY_MEMBER_INFO = "KEY_MEMBER_INFO";
    private static final String KEY_USE_MEMBER_PRICE = "KEY_USE_MEMBER_PRICE";
    private static final String TYPE_COMMON = "TYPE_COMMON";
    private static final String SHOW_TYPE_COMMON = "SHOW_TYPE_COMMON";
    private static final String SHOW_TYPE_PRACTICE = "SHOW_TYPE_PRACTICE";
    private static String KEY_ORDER_GROUP = "KEY_ORDER_GROUP";

    private static final String INTENT_DISHES_GUID = "INTENT_DISHES_GUID";
    private static final String INTENT_ORDER_COUNT = "INTENT_ORDER_COUNT";
    private static final String INTENT_RISE_GUID = "INTENT_RISE_GUID";
    private static final String INTENT_PACKAGE_GROUP = "INTENT_PACKAGE_GROUP";

    public static final String CALL_BACK_ORDER_GROUP = "CALL_BACK_ORDER_GROUP";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_close)
    ImageView ivSearchClose;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rv_context)
    RecyclerView rvContext;
    @BindView(R.id.msv_context)
    MultiStateView msvContext;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_no_search)
    LinearLayout llNoSearch;
    @BindView(R.id.btn_retry)
    Button btnRetry;

    // 估清信息
    private Map<String, DishesEstimateRecordDishes> mEstimateMap = new HashMap<>();
    // 菜品集合
    private List<DishesE> dishesEList = new ArrayList<>();
    // 已选择的菜品
    public OrderDishesGroup orderDishesGroup = new OrderDishesGroup();

    private List<DishesE> dishesESearchList = new ArrayList<>();
    /**
     * 各菜品列表的数量
     */
    private Map<String, Double> dishesOrderCount = new ArrayMap<>();

    private CommonAdapter<DishesE> commonAdapter;
    private boolean useMemberPrice;
    private boolean loginMember;

    /**
     * 选择做法弹出框
     */
    private AddPracticeDishesDialogFragment mAddPracticeDishesDialogFragment;

    DishesEstimateRecordDishes estimate = null;
    /**
     * 停售信息
     */
    private Map<String, Boolean> stopSalse = new ArrayMap<>();

    private Map<String, List<DishesPracticeE>> dishesPracticeMap;

    /**
     * 标记是否有菜品
     */
    private boolean isHaveDishes = true;

    public static Intent newIntent(Context context, OrderDishesGroup orderDishesGroup, boolean useMemberPrice, boolean loginMember) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ORDER_GROUP, orderDishesGroup);
        bundle.putBoolean(KEY_MEMBER_INFO, loginMember);
        bundle.putBoolean(KEY_USE_MEMBER_PRICE, useMemberPrice);

        Intent intent = new Intent(context, SearchDishesActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override

    protected void handleBundleExtras(@NonNull Bundle extras) {
        orderDishesGroup = extras.getParcelable(KEY_ORDER_GROUP);
        useMemberPrice = extras.getBoolean(KEY_USE_MEMBER_PRICE);
        loginMember = extras.getBoolean(KEY_MEMBER_INFO);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_dishes_search;
    }

    @Nullable
    @Override
    protected SearchDishesContract.Presenter initPresenter() {
        return new SearchDishesPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        msvContext.setViewState(MultiStateView.VIEW_STATE_LOADING);
        ivSearchClose.setVisibility(View.GONE);
        initClickEvent();
    }

    @SuppressLint("CheckResult")
    private void initClickEvent() {
        final Drawable drawable = getResources().getDrawable(R.drawable.search_gray);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        etSearch.setCompoundDrawables(drawable, null, null, null);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.length() == 0) {
                    etSearch.setCompoundDrawables(drawable, null, null, null);
                    ivSearchClose.setVisibility(View.GONE);
                    setSearchDishes("");
                } else {
                    etSearch.setCompoundDrawables(null, null, null, null);
                    ivSearchClose.setVisibility(View.VISIBLE);
                    setSearchDishes(editable.toString().trim());
                }
            }
        });

        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            mPresenter.getDishesAndPracticeList();
        });
    }

    public void setOrderDishesGroup() {
        if (orderDishesGroup == null || orderDishesGroup.getDishesList() == null) {
            return;
        }
        dishesOrderCount.clear();
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = orderDishesGroup.getDishesList();
        for (int i = 0; i < salesOrderBatchDishesEList.size(); i++) {
            SalesOrderBatchDishesE salesOrderBatchDishesE = salesOrderBatchDishesEList.get(i);
            double temp = salesOrderBatchDishesE.getOrderCount();
            String dishesGUID = salesOrderBatchDishesE.getDishesGUID().toLowerCase();
            Double tempCount = dishesOrderCount.get(dishesGUID);
            double count = tempCount != null ? ArithUtil.add(tempCount, temp) : temp;
            dishesOrderCount.put(dishesGUID, count);
        }
        if (commonAdapter != null) {
            commonAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerAdapter() {
        commonAdapter = new CommonAdapter<DishesE>(this, R.layout.item_order_dishes, dishesESearchList) {
            @SuppressLint("CheckResult")
            @Override
            protected void convert(ViewHolder holder, DishesE dishesE, int position) {
                boolean isWightEnable = dishesE.getWeighEnable() != null && dishesE.getWeighEnable() == 1;
                //是否显示分割线
                if (position + 1 < dishesESearchList.size()) {
                    holder.setVisible(R.id.layout_top_divider, true);
                } else {
                    holder.setVisible(R.id.layout_top_divider, false);
                }

                holder.setText(R.id.tv_operation, dishesE.getCode());
                holder.setText(R.id.tv_order_dishes_name, dishesE.getSimpleName());
                //2018-04-02  添加会员价
                if (useMemberPrice && dishesE.getMemberPrice() != null && dishesE.getMemberPrice().compareTo(dishesE.getCheckPrice()) != 0) {
                    holder.setText(R.id.tv_order_dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesE.getMemberPrice())));
                    holder.setVisible(R.id.tv_original_price, true);
                    SpanUtils spanUtils = new SpanUtils();
                    spanUtils.append(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesE.getCheckPrice())))
                            .setStrikethrough()
                            .append("/" + dishesE.getCheckUnit()).setFontSize(getResources().getDimensionPixelSize(R.dimen.tv_text_size_10)).setStrikethrough();
                    holder.setText(R.id.tv_original_price, spanUtils.create());
                } else {
                    holder.setText(R.id.tv_order_dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesE.getCheckPrice())));
                    holder.setVisible(R.id.tv_original_price, false);
                }
                //设置菜品单位
                String unit = dishesE.getCheckUnit();
                if (unit == null) {
                    holder.setText(R.id.tv_order_dishes_price_unit, "");
                } else {
                    unit = "/" + unit;
                    holder.setText(R.id.tv_order_dishes_price_unit, unit);
                }

                if (dishesE.getArrayOfDishesPracticeE() != null) {
                    //斤数
                    holder.setVisible(R.id.ll_catty, false);
                    //菜品有做法
                    holder.setVisible(R.id.layout_order_dishes_operation, false);
                    holder.setVisible(R.id.practice_order_dishes_ll, true);
                    holder.setEnable(R.id.tv_order_dishes_operation, true);
                    holder.setText(R.id.tv_order_dishes_operation, "选做法");
                    if (isWightEnable) {
                        holder.setTextColorRes(R.id.tv_order_dishes_operation, R.color.common_text_color_2495ee);
                        holder.setBackgroundRes(R.id.tv_order_dishes_operation, R.drawable.shape_edit_choose_blue_text_bg);
                    } else {
                        holder.setTextColorRes(R.id.tv_order_dishes_operation, R.color.tv_text_white_ffffff);
                        holder.setBackgroundRes(R.id.tv_order_dishes_operation, R.drawable.shape_edit_choose_bg);
                    }

                    double disheCount = getDishesOrderCount(dishesE.getDishesGUID());
                    //设置已经选择的有做法的菜品数量
                    if (disheCount == 0) {//没有点菜
                        holder.setVisible(R.id.practice_order_dishes_count, false);
                    } else {
                        holder.setVisible(R.id.practice_order_dishes_count, true);
                        holder.setText(R.id.practice_order_dishes_count, "×" + ArithUtil.stripTrailingZeros(disheCount));
                    }
                } else if (dishesE.getIsPackageDishes() == 1 && dishesE.getPackageType() == 1) {
                    //斤数
                    holder.setVisible(R.id.ll_catty, false);
                    //自选套餐
                    holder.setVisible(R.id.layout_order_dishes_operation, false);
                    holder.setVisible(R.id.practice_order_dishes_ll, true);

                    holder.setEnable(R.id.tv_order_dishes_operation, true);
                    holder.setText(R.id.tv_order_dishes_operation, "选套餐");
                    holder.setBackgroundRes(R.id.tv_order_dishes_operation, R.drawable.shape_edit_choose_bg);
                    double disheCount = getDishesOrderCount(dishesE.getDishesGUID());
                    //设置已经点的套餐数量
                    if (disheCount == 0) {//没有点菜
                        holder.setVisible(R.id.practice_order_dishes_count, false);
                    } else {
                        holder.setVisible(R.id.practice_order_dishes_count, true);
                        holder.setText(R.id.practice_order_dishes_count, "×" + ArithUtil.stripTrailingZeros(disheCount));
                    }
                } else {
                    if (isWightEnable) {
                        holder.setVisible(R.id.ll_catty, true);
                        holder.setVisible(R.id.layout_order_dishes_operation, false);
                        holder.setVisible(R.id.practice_order_dishes_ll, false);
                    } else {
                        holder.setVisible(R.id.ll_catty, false);
                        holder.setVisible(R.id.layout_order_dishes_operation, true);
                        holder.setVisible(R.id.practice_order_dishes_ll, false);
                    }

                }
                if (dishesE.isStopSale()) {//菜品已停售
                    setIsStopSale(holder);
                } else {
                    if (isWightEnable) {
                        SpanUtils spanUtils = new SpanUtils();
                        if (getDishesOrderCount(dishesE.getDishesGUID()) == 0) {
                            spanUtils.append("输入重量")
                                    .setFontSize(13, true)
                                    .setForegroundColor(ContextCompat.getColor(getApplicationContext(), R.color.common_text_color_2495ee));
                            holder.setBackgroundRes(R.id.et_input_catty, R.drawable.shape_edit_choose_blue_text_bg);
                            holder.setText(R.id.et_input_catty, spanUtils.create());
                        } else {
                            spanUtils.append(ArithUtil.stripTrailingZeros(getDishesOrderCount(dishesE.getDishesGUID())))
                                    .setFontSize(16, true)
                                    .setForegroundColor(ContextCompat.getColor(getApplicationContext(), R.color.common_text_color_000000))
                                    .setUnderline();
                            holder.setBackgroundRes(R.id.et_input_catty, R.color.btn_text_white_ffffff);
                            holder.setText(R.id.et_input_catty, spanUtils.create());
                        }
                    } else {
                        if (mEstimateMap != null) {
                            estimate = mEstimateMap.get(dishesE.getDishesGUID().toLowerCase());
                            if (estimate != null) {//菜品有估清
                                if (estimate.getResidueCount() <= 0) {//菜品剩余数量不足
                                    setIsStopSale(holder);
                                } else if (ArithUtil.sub(estimate.getResidueCount(), getDishesOrderCount(dishesE.getDishesGUID())) <= 0) {//点菜数量大于剩余数量
                                    double disheCount = getDishesOrderCount(dishesE.getDishesGUID());
                                    if (estimate.getResidueCount() < disheCount) {
                                        holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_bg_light_red_normal));
                                        holder.setTextColor(R.id.practice_order_dishes_count, getResources().getColor(R.color.btn_bg_light_red_normal));
                                    } else {
                                        holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                                        holder.setTextColor(R.id.practice_order_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                                    }
                                    holder.setImageResource(R.id.iv_add, R.drawable.add_gray);
                                } else {
                                    holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                                    holder.setTextColor(R.id.practice_order_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                                    holder.setImageResource(R.id.iv_add, R.drawable.add_blue_max);
                                }
                            } else {
                                holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                                holder.setTextColor(R.id.practice_order_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                                holder.setImageResource(R.id.iv_add, R.drawable.add_blue_max);
                            }
                        } else if (stopSalse.get(dishesE.getDishesGUID()) == null ? false : stopSalse.get(dishesE.getDishesGUID())) {
                            setIsStopSale(holder);
                        } else {
                            holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                            holder.setTextColor(R.id.practice_order_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                            holder.setImageResource(R.id.iv_add, R.drawable.add_blue_max);
                        }
                    }
                }
                //判断菜品是否有添加
                isSelected(getDishesOrderCount(dishesE.getDishesGUID()), holder);
                //加菜按钮点击事件
                RxView.clicks(holder.getView(R.id.iv_add)).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
                    SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
                    boolean isLittle = false;
                    //菜品估清信息
                    estimate = mEstimateMap.get(dishesE.getDishesGUID().toLowerCase());
                    if (estimate != null) {
                        if (ArithUtil.sub(estimate.getResidueCount(), getDishesOrderCount(dishesE.getDishesGUID())) <= 0) {
                            BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate));
                            return;
                        } else if (ArithUtil.sub(estimate.getResidueCount(), getDishesOrderCount(dishesE.getDishesGUID())) < 1) {
                            isLittle = true;
                            salesOrderBatchDishesE.setOrderCount(ArithUtil.sub(estimate.getResidueCount(), getDishesOrderCount(dishesE.getDishesGUID())));
                            if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
                                for (SalesOrderBatchDishesE sub : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                                    sub.setOrderCount(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), sub.getPackageDishesUnitCount()));
                                }
                            }
                        }
                    }
                    //判断是否有做法
                    if (dishesE.getArrayOfDishesPracticeE() != null) {
                        List<DishesPracticeE> dishesPracticeEs = dishesPracticeMap.get(dishesE.getDishesGUID());
                        salesOrderBatchDishesE.setOrderCount(0d);
                        addDishesEPractice(dishesPracticeEs, salesOrderBatchDishesE, dishesOrderCount);
                        return;
                    }
                    if (isLittle) {
                        String salesUnit = salesOrderBatchDishesE.getDishesUnit();
                        BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate_small, salesUnit == null ? "" : salesUnit));
                        dishesOrderCount.put(dishesE.getDishesGUID(), estimate.getResidueCount());
                    } else {
                        dishesOrderCount.put(dishesE.getDishesGUID(), ArithUtil.add(getDishesOrderCount(dishesE.getDishesGUID()), 1));
                    }
                    isSelected(getDishesOrderCount(dishesE.getDishesGUID()), holder);
                    addDishesE(salesOrderBatchDishesE);
                });
                //减菜按钮点击事件
                RxView.clicks(holder.getView(R.id.iv_reduce)).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
                    SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
                    if (dishesE.getArrayOfDishesPracticeE() != null) {
                        reduceDishesEDialog(salesOrderBatchDishesE.getDishesGUID());
                        return;
                    }
                    if (getDishesOrderCount(dishesE.getDishesGUID()) > 0 && getDishesOrderCount(dishesE.getDishesGUID()) < 1) {
                        dishesOrderCount.put(dishesE.getDishesGUID(), 0d);
                    } else {
                        dishesOrderCount.put(dishesE.getDishesGUID(), ArithUtil.sub(getDishesOrderCount(dishesE.getDishesGUID()), 1));
                    }
                    isSelected(getDishesOrderCount(dishesE.getDishesGUID()), holder);
                    reduceDishesE(salesOrderBatchDishesE);
                });
                //添加做法或者选择套餐按钮
                RxView.clicks(holder.getView(R.id.tv_order_dishes_operation)).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
                    SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
                    salesOrderBatchDishesE.setOrderCount(0d);

                    if (dishesE.getIsPackageDishes() == 1 && dishesE.getPackageType() == 1) {//自选套餐
                        onAddPackageGroup(salesOrderBatchDishesE.getDishesGUID(), dishesOrderCount);
                    } else {//选做法
                        List<DishesPracticeE> dishesPracticeEs = dishesPracticeMap.get(dishesE.getDishesGUID());
                        addDishesEPractice(dishesPracticeEs, salesOrderBatchDishesE, dishesOrderCount);
                    }
                });
                //修改数量
                RxView.clicks(holder.getView(R.id.tv_dishes_count)).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
                    SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
                    //设置已经点的菜品数量
                    salesOrderBatchDishesE.setOrderCount(getDishesOrderCount(dishesE.getDishesGUID()));
                    onChangeCount(salesOrderBatchDishesE);
                });

                RxView.clicks(holder.getView(R.id.et_input_catty)).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
                    SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
                    //设置已经点的菜品数量
                    salesOrderBatchDishesE.setOrderCount(getDishesOrderCount(dishesE.getDishesGUID()));
                    setCattyDishes(salesOrderBatchDishesE);
                });
            }

            private void setIsStopSale(ViewHolder holder) {
                holder.setVisible(R.id.layout_order_dishes_operation, false);
                holder.setVisible(R.id.practice_order_dishes_ll, true);
                holder.setVisible(R.id.practice_order_dishes_count, false);
                holder.setVisible(R.id.ll_catty, false);

                holder.setText(R.id.tv_order_dishes_operation, "已售罄");
                holder.setEnable(R.id.tv_order_dishes_operation, false);
                holder.setBackgroundRes(R.id.tv_order_dishes_operation, R.drawable.shape_edit_no_choose_bg);
            }

            /**
             * 判断是否某菜品有添加到
             *
             * @param i  条目下标
             * @param holder ViewHolder
             */
            private void isSelected(double i, ViewHolder holder) {
                SpanUtils dishesCount = new SpanUtils();
                if (i == 0) {
                    holder.setVisible(R.id.tv_dishes_count, false);
                    holder.setVisible(R.id.iv_reduce, false);
                } else {
                    holder.setVisible(R.id.tv_dishes_count, true);
                    if (i == (int) i) {
                        dishesCount.append((int) i + "");
                    } else {
                        dishesCount.append(getString(R.string.two_decimal_str, ArithUtil.stripTrailingZeros(i)));
                    }
                    dishesCount.setUnderline();
                    holder.setText(R.id.tv_dishes_count, dishesCount.create());
                    holder.setVisible(R.id.iv_reduce, true);
                }
            }
        };

        rvContext.setLayoutManager(new LinearLayoutManager(this));
        rvContext.setAdapter(commonAdapter);

    }

    /**
     * 根据菜品guid返回已点数量
     *
     * @param dishesGUID
     * @return
     */
    private double getDishesOrderCount(String dishesGUID) {
        if (dishesOrderCount == null) {
            return 0;
        }
        return dishesOrderCount.get(dishesGUID) == null ? 0 : dishesOrderCount.get(dishesGUID);
    }

    /**
     * 克隆 SalesOrderBatchDishesE
     *
     * @param salesOrderBatchDishesE
     * @return
     */
    private SalesOrderBatchDishesE cloneSalesOrderBatchDishesE(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        SalesOrderBatchDishesE newSalesOrderBatchDishesE = new SalesOrderBatchDishesE();
        newSalesOrderBatchDishesE.setDishesE(salesOrderBatchDishesE.getDishesE());
        newSalesOrderBatchDishesE.setDishesGUID(salesOrderBatchDishesE.getDishesGUID());
        newSalesOrderBatchDishesE.setCheckStatus(salesOrderBatchDishesE.getCheckStatus());
        newSalesOrderBatchDishesE.setKitchenPrintStatus(salesOrderBatchDishesE.getKitchenPrintStatus());
//        newSalesOrderBatchDishesE.setNew(salesOrderBatchDishesE.getNew());
        newSalesOrderBatchDishesE.setDishesUnit(salesOrderBatchDishesE.getDishesUnit());
        newSalesOrderBatchDishesE.setGift(salesOrderBatchDishesE.getGift());
        newSalesOrderBatchDishesE.setIsPackageDishes(salesOrderBatchDishesE.getIsPackageDishes());
        newSalesOrderBatchDishesE.setOrderCount(salesOrderBatchDishesE.getOrderCount());
        newSalesOrderBatchDishesE.setArrayOfDishesPracticeE(salesOrderBatchDishesE.getArrayOfDishesPracticeE());
        if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
            List<SalesOrderBatchDishesE> subList = new ArrayList();
            for (SalesOrderBatchDishesE subDishes : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
//                subDishes.setDishesGUID(packageItemE.getSubDishesGUID());
//                subDishes.setOrderCount(ArithUtil.mul(newSalesOrderBatchDishesE.getOrderCount(), packageItemE.getDishesCount()));
//                subDishes.setGift(newSalesOrderBatchDishesE.getGift());
//                subDishes.setIsPackageDishes(2);
//                subDishes.setPackageDishesUnitCount(packageItemE.getDishesCount());
//                subDishes.setCheckStatus(newSalesOrderBatchDishesE.getCheckStatus());
//                subDishes.setKitchenPrintStatus(newSalesOrderBatchDishesE.getKitchenPrintStatus());
                subList.add(subDishes);
            }
            newSalesOrderBatchDishesE.setPackageDishesUnitCount(Double.parseDouble(subList.size() + ""));
            newSalesOrderBatchDishesE.setArrayOfSalesOrderBatchDishesE(subList);
        }
        newSalesOrderBatchDishesE.setPrice(salesOrderBatchDishesE.getPrice());
        newSalesOrderBatchDishesE.setDishesGUID(salesOrderBatchDishesE.getDishesGUID());
        newSalesOrderBatchDishesE.setDishesName(salesOrderBatchDishesE.getDishesName());
        return newSalesOrderBatchDishesE;
    }


    private SalesOrderBatchDishesE getSalesOrderBatchDishesE(int position) {
        DishesE dishesE = dishesESearchList.get(position);
        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
        salesOrderBatchDishesE.setSalesOrderBatchGUID(UUID.randomUUID().toString());
        salesOrderBatchDishesE.setDishesE(dishesE);
        salesOrderBatchDishesE.setWeighEnable(dishesE.getWeighEnable() != null && dishesE.getWeighEnable() == 1);
        salesOrderBatchDishesE.setCheckStatus(1);
        salesOrderBatchDishesE.setKitchenPrintStatus(1);
//        salesOrderBatchDishesE.setNew(true);
        salesOrderBatchDishesE.setDishesUnit(dishesE.getCheckUnit());
        salesOrderBatchDishesE.setGift(0);
        salesOrderBatchDishesE.setIsPackageDishes(dishesE.getIsPackageDishes());
        salesOrderBatchDishesE.setPackageType(dishesE.getPackageType());
        salesOrderBatchDishesE.setOrderCount(1.0);
        if (dishesE.getIsPackageDishes() == 1 && dishesE.getPackageType() == 0) {//固定套餐
            List<SalesOrderBatchDishesE> subList = new ArrayList();
            for (PackageItemE packageItemE : dishesE.getArrayOfPackageItemE()) {
                SalesOrderBatchDishesE subDishes = new SalesOrderBatchDishesE();
                subDishes.setDishesGUID(packageItemE.getSubDishesGUID());
                subDishes.setOrderCount(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), packageItemE.getDishesCount()));
                subDishes.setGift(salesOrderBatchDishesE.getGift());
                subDishes.setIsPackageDishes(2);
                subDishes.setPackageDishesUnitCount(packageItemE.getDishesCount());
                subDishes.setCheckStatus(salesOrderBatchDishesE.getCheckStatus());
                subDishes.setKitchenPrintStatus(salesOrderBatchDishesE.getKitchenPrintStatus());
                subList.add(subDishes);
            }
            salesOrderBatchDishesE.setPackageDishesUnitCount(Double.parseDouble(subList.size() + ""));
            salesOrderBatchDishesE.setArrayOfSalesOrderBatchDishesE(subList);
        }
        salesOrderBatchDishesE.setPrice(dishesE.getCheckPrice());
        salesOrderBatchDishesE.setMemberPrice(dishesE.getMemberPrice());
        salesOrderBatchDishesE.setDishesGUID(dishesE.getDishesGUID());
        salesOrderBatchDishesE.setDishesName(dishesE.getSimpleName());
        return salesOrderBatchDishesE;
    }


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getDishesAndPracticeList();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    /**
     * 输入搜索信息，选取菜品
     *
     * @param str
     */
    private void setSearchDishes(String str) {
        if (dishesEList == null || dishesEList.size() == 0) {
            llEmpty.setVisibility(View.VISIBLE);
            llNoSearch.setVisibility(View.GONE);
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }
        if (StringUtils.isEmpty(str)) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_LOADING);
            return;
        }
        dishesESearchList.clear();
        for (DishesE d : dishesEList) {
            str = str.toLowerCase();
            String pyIndex = d.getPYIndex().toLowerCase();
            String simpleName = d.getSimpleName().toLowerCase();
            String code = d.getCode().toLowerCase();
            if (pyIndex.contains(str) || simpleName.contains(str) || code.contains(str)) {
                dishesESearchList.add(d);
            }
        }

        commonAdapter.notifyDataSetChanged();
        if (dishesESearchList.size() == 0) {
            llEmpty.setVisibility(View.GONE);
            llNoSearch.setVisibility(View.VISIBLE);
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    public void reduceDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        boolean isRemove = false;
        if (orderDishesGroup != null && orderDishesGroup.getDishesList() != null && orderDishesGroup.getDishesList().size() != 0) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
                if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                    if (salesOrderBatchDishesE.getOrderCount() > 1) {
                        isRemove = false;
                        salesOrderBatchDishesE.setOrderCount(ArithUtil.sub(salesOrderBatchDishesE.getOrderCount(), 1));
                    } else {
                        isRemove = true;
                    }
                }
            }
        }
        if (isRemove) {
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                if (iterator.next().getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                    iterator.remove();
                }
            }
        }
        setOrderDishesGroup();
    }

    public void addDishesEPractice(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE, Map<String, Double> dishesOrderCount) {
        DishesEstimateRecordDishes temp = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        Double tempCount = dishesOrderCount.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        Double tempEstimate = temp != null ? (temp.getResidueCount()) : Double.MAX_VALUE;
        if (mAddPracticeDishesDialogFragment != null) {
            mAddPracticeDishesDialogFragment = null;
        }
        mAddPracticeDishesDialogFragment = AddPracticeDishesDialogFragment.newInstance(dishesPracticeEs, salesOrderBatchDishesE, ArithUtil.sub(tempEstimate
                , tempCount != null ? tempCount : 0));
        mAddPracticeDishesDialogFragment.setCallBack(this);
        mAddPracticeDishesDialogFragment.show(getSupportFragmentManager(), "AddPracticeDishesDialogFragment");
    }

    private void addDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        if (orderDishesGroup == null) {
            orderDishesGroup = new OrderDishesGroup();
            orderDishesGroup.setDishesList(new ArrayList<>());
        } else if (orderDishesGroup.getDishesList() == null) {
            orderDishesGroup.setDishesList(new ArrayList<>());
        }
        boolean isContain = false;
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
            if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                if (newSalesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || newSalesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                    if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                        salesOrderBatchDishesE.setOrderCount(ArithUtil.add(newSalesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getOrderCount()));
                        isContain = true;
                    } else {
                        continue;
                    }
                } else {
                    isContain = false;
                }
            }
        }
        if (!isContain) {
            orderDishesGroup.getDishesList().add(newSalesOrderBatchDishesE);
        }
        setOrderDishesGroup();
    }

    public void setCattyDishes(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        if (orderDishesGroup == null) {
            orderDishesGroup = new OrderDishesGroup();
            orderDishesGroup.setDishesList(new ArrayList<>());
        } else if (orderDishesGroup.getDishesList() == null) {
            orderDishesGroup.setDishesList(new ArrayList<>());
        }
        boolean isContain = false;
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
            if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                if (newSalesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || newSalesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                    if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                        salesOrderBatchDishesE.setOrderCount(newSalesOrderBatchDishesE.getOrderCount());
                        isContain = true;
                    } else {
                        continue;
                    }
                } else {
                    isContain = false;
                }
            }
        }
        if (!isContain) {
            orderDishesGroup.getDishesList().add(newSalesOrderBatchDishesE);
        }
        setOrderDishesGroup();
        onChangeCount(newSalesOrderBatchDishesE);
    }

    public void onChangeCount(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        //估清数据
        DishesEstimateRecordDishes recordDishesCount = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        //菜品剩余数量
        double tempEstimate = recordDishesCount != null ? recordDishesCount.getResidueCount() : Double.MAX_VALUE;
        //已点菜品数量
        double orderDishesCount = salesOrderBatchDishesE.getOrderCount();
        //修改点菜数量弹出框
        getDialogFactory().showChangeOrderDishesCountDialog(salesOrderBatchDishesE, orderDishesCount, tempEstimate, TYPE_COMMON
                , SHOW_TYPE_COMMON, this);
    }

    public void onAddPackageGroup(String dishesGUID, Map<String, Double> dishesOrderCount) {
        //估清数据
        DishesEstimateRecordDishes recordDishesCount = mEstimateMap.get(dishesGUID.toLowerCase());
        Double tempCount = dishesOrderCount.get(dishesGUID.toLowerCase());
        //菜品剩余数量
        double tempEstimate = recordDishesCount != null ? recordDishesCount.getResidueCount() : Double.MAX_VALUE;
        //判断是否还可以点菜
        if (ArithUtil.sub(tempEstimate, tempCount != null ? tempCount : 0) < 1) {
            showMessage(getString(R.string.add_dishes_failed_by_estimate));
            return;
        }
        for (DishesE dishesE : dishesEList) {
            if (dishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                List<PackageGroup> list = dishesE.getArrayOfPackageGroup();
                startActivityForResult(PackGroupOrderDishesActivity.newIntent(dishesE.getSimpleName(), dishesE.getDishesGUID()
                        , useMemberPrice && loginMember ? dishesE.getMemberPrice() : dishesE.getCheckPrice()
                        , ArithUtil.sub(tempEstimate, tempCount != null ? tempCount : 0), list, this), REQUEST_CODE_PACKAGE_GROUP);
            }
        }
    }

    public void reduceDishesEDialog(String dishesGUID) {
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEs = new ArrayList<>();
        if (orderDishesGroup != null && orderDishesGroup.getDishesList() != null) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
                if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                    SalesOrderBatchDishesE tempSalesOrder = null;
                    try {
                        tempSalesOrder = (SalesOrderBatchDishesE) CloneUtils.deepClone(salesOrderBatchDishesE);
                    } catch (Exception e) {
                        tempSalesOrder = cloneSalesOrderBatchDishesE(salesOrderBatchDishesE);
                        e.printStackTrace();
                    }
                    salesOrderBatchDishesEs.add(tempSalesOrder);
                }
            }
        }
        DishesEstimateRecordDishes temp = mEstimateMap.get(dishesGUID.toLowerCase());
        Double tempEstimate = temp != null ? (temp.getResidueCount()) : Double.MAX_VALUE;
        ReducePracticeDishesDialogFragment dialogFragment = ReducePracticeDishesDialogFragment.newInstance(salesOrderBatchDishesEs, tempEstimate);
        dialogFragment.setCallBack(this);
        dialogFragment.show(getSupportFragmentManager(), "ReducePracticeDishesDialogFragment");
    }

    @OnClick({R.id.iv_back, R.id.iv_search_close, R.id.msv_context})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                backBySaveOrder();
                break;
            case R.id.iv_search_close:
                etSearch.setText("");
                setSearchDishes("");
                break;
            case R.id.msv_context:
                keyBoardHide(etSearch);
                break;
            default:
        }
    }

    /**
     * 按返回键或者返回按钮时 将创建的
     */
    private void backBySaveOrder() {
        keyBoardHide(etSearch);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CALL_BACK_ORDER_GROUP, orderDishesGroup);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }


    @Override
    public void callBackShowMessage(String message) {
        showMessage(message);
    }

    @Override
    public void callBackChange(List<SalesOrderBatchDishesE> newSalesOrderBatchDishesEList) {
        String dishesGUID = newSalesOrderBatchDishesEList.get(0).getDishesGUID();
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = new ArrayList<>();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
            if (!salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                salesOrderBatchDishesEList.add(salesOrderBatchDishesE);
            }
        }
        for (Iterator<SalesOrderBatchDishesE> iterator = newSalesOrderBatchDishesEList.iterator(); iterator.hasNext(); ) {
            Double temp = iterator.next().getOrderCount();
            if (temp == null || temp == 0) {
                iterator.remove();
            }
        }
        salesOrderBatchDishesEList.addAll(newSalesOrderBatchDishesEList);
        orderDishesGroup.getDishesList().clear();
        orderDishesGroup.setDishesList((ArrayList<SalesOrderBatchDishesE>) salesOrderBatchDishesEList);
//        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
    }

    @Override
    public void callBackOK(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        addDishesE(salesOrderBatchDishesE);
    }

    @Override
    public void changePracticeCount(double currentCount, double maxCount) {
        //修改做法数量弹框
        getDialogFactory().showChangeOrderDishesCountDialog(new SalesOrderBatchDishesE(), currentCount, maxCount
                , TYPE_COMMON, SHOW_TYPE_PRACTICE, this);
    }

    @Override
    public void onDishesCountChanged(SalesOrderBatchDishesE salesOrderBatchDishesE, double count) {
        //修改点菜数量回掉
        if ((salesOrderBatchDishesE.getArrayOfDishesPracticeE() != null && salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() > 0)
                || (salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1)) {//自选套餐或者有做法
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                SalesOrderBatchDishesE next = iterator.next();
                List<DishesPracticeE> arrayOfDishesPracticeE = next.getArrayOfDishesPracticeE();
                boolean isPackage = next.getIsPackageDishes() == 1 && next.getPackageType() == 1;
                boolean isPractice = arrayOfDishesPracticeE != null && arrayOfDishesPracticeE.size() > 0;
                if (next.getSalesOrderBatchGUID().equalsIgnoreCase(salesOrderBatchDishesE.getSalesOrderBatchGUID())
                        && (isPackage || isPractice)) {
                    if (count <= 0) {
                        iterator.remove();
                    } else {
                        next.setOrderCount(count);
                    }
                }
            }
        } else {//普通菜品
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                SalesOrderBatchDishesE next = iterator.next();
                List<DishesPracticeE> arrayOfDishesPracticeE = next.getArrayOfDishesPracticeE();
                boolean isPackage = next.getIsPackageDishes() == 1 && next.getPackageType() == 1;
                boolean isPractice = arrayOfDishesPracticeE != null && arrayOfDishesPracticeE.size() > 0;
                if ((next.getDishesGUID().equalsIgnoreCase(salesOrderBatchDishesE.getDishesGUID())) &&
                        !isPractice && !isPackage) {
                    if (count == 0) {
                        iterator.remove();
                    } else {
                        next.setOrderCount(count);
                    }
                }
            }
        }
        setOrderDishesGroup();
    }

    @Override
    public void onPracticeCountChanged(double count) {
        //修改做法菜品数量 回掉
        if (mAddPracticeDishesDialogFragment != null) {
            mAddPracticeDishesDialogFragment.setNetPracticeCount(count);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PACKAGE_GROUP) {
            //套餐页面传回来的数据
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String dishesGUID = bundle.getString(INTENT_DISHES_GUID);
                    double count = bundle.getDouble(INTENT_ORDER_COUNT);//套餐数量
                    double rise = bundle.getDouble(INTENT_RISE_GUID);//浮动价
                    List<PackageGroupDishesE> packageGroupList = bundle.getParcelableArrayList(INTENT_PACKAGE_GROUP);
                    //设置订单数据
                    SalesOrderBatchDishesE batchDishesE = new SalesOrderBatchDishesE();
                    DishesE dishesE = null;
                    for (DishesE dishes : dishesEList) {
                        if (dishesGUID.equalsIgnoreCase(dishes.getDishesGUID())) {
                            dishesE = dishes;
                        }
                    }
                    batchDishesE.setSalesOrderBatchGUID(UUID.randomUUID().toString());
                    batchDishesE.setDishesGUID(dishesGUID);
                    batchDishesE.setDishesE(dishesE);
                    batchDishesE.setCheckStatus(1);
                    batchDishesE.setKitchenPrintStatus(1);
                    batchDishesE.setDishesUnit(dishesE.getCheckUnit());
                    batchDishesE.setGift(0);
                    batchDishesE.setIsPackageDishes(1);
                    batchDishesE.setPackageType(1);
                    batchDishesE.setOrderCount(count);
                    batchDishesE.setPrice(ArithUtil.add(dishesE.getCheckPrice(), rise));
                    batchDishesE.setMemberPrice(ArithUtil.add(dishesE.getMemberPrice(), rise));
                    batchDishesE.setSimpleName(dishesE.getSimpleName());
                    batchDishesE.setDishesName(dishesE.getSimpleName());
                    //设置估清数据
                    DishesEstimateRecordDishes recordDishes = mEstimateMap.get(dishesGUID.toLowerCase());
                    batchDishesE.setMaxValue(recordDishes != null ? recordDishes.getResidueCount() : Double.MAX_VALUE);
                    //套餐子项数据
                    List<SalesOrderBatchDishesE> groupList = new ArrayList<>();
                    for (PackageGroupDishesE groupDishesE : packageGroupList) {
                        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
                        salesOrderBatchDishesE.setDishesGUID(groupDishesE.getDishesGUID());
                        salesOrderBatchDishesE.setIsPackageDishes(2);
                        salesOrderBatchDishesE.setCheckUnit(groupDishesE.getCheckUnit());
                        salesOrderBatchDishesE.setPackageDishesUnitCount(groupDishesE.getSingleCount());
                        salesOrderBatchDishesE.setPackageDishesOrderCount(groupDishesE.getPackageDishesOrderCount());
                        salesOrderBatchDishesE.setDishesName(groupDishesE.getSimpleName());
                        salesOrderBatchDishesE.setSingleCount(groupDishesE.getSingleCount());
                        salesOrderBatchDishesE.setSimpleName(groupDishesE.getSimpleName());
                        salesOrderBatchDishesE.setRiseAmount(groupDishesE.getRiseAmount());
                        groupList.add(salesOrderBatchDishesE);
                    }
                    batchDishesE.setArrayOfSalesOrderBatchDishesE(groupList);
                    if (orderDishesGroup == null) {
                        orderDishesGroup = new OrderDishesGroup();
                    }
                    if (orderDishesGroup.getDishesList() == null) {
                        ArrayList<SalesOrderBatchDishesE> list = new ArrayList<>();
                        list.add(batchDishesE);
                        orderDishesGroup.setDishesList(list);
                    } else {
                        orderDishesGroup.getDishesList().add(batchDishesE);
                    }
                    setOrderDishesGroup();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            backBySaveOrder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setDishesData(List<DishesE> dishesData) {
        dishesEList.clear();
        dishesEList.addAll(dishesData);
        if (dishesEList.size() == 0) {
            isHaveDishes = false;
            llEmpty.setVisibility(View.VISIBLE);
            llNoSearch.setVisibility(View.GONE);
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }
        isHaveDishes = true;
        msvContext.setViewState(MultiStateView.VIEW_STATE_LOADING);
        setOrderDishesGroup();
        initRecyclerAdapter();
    }

    @Override
    public void setDishesPracticeData(Map<String, List<DishesPracticeE>> dishesPracticeList) {
        dishesPracticeMap = dishesPracticeList;
        mPresenter.getDishesEstimate();
    }

    @Override
    public void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map) {
        mEstimateMap.clear();
        mEstimateMap.putAll(map);
        //根据估清信息设置菜品停售信息
        mPresenter.updateDishesStopStatus(dishesEList, map);
    }

    @Override
    public void refreshDishesStatus(List<DishesE> list) {
        stopSalse.clear();
        if (list != null && list.size() != 0) {
            for (DishesE d : list) {
                stopSalse.put(d.getDishesGUID(), d.isStopSale());
            }
        }
        if (commonAdapter != null) {
            commonAdapter.notifyDataSetChanged();
        }

        if (isHaveDishes) {//这个判断是为了控制当 当前门店没有配置菜品时不弹出软键盘
            keyBoardShow(etSearch);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        keyBoardHide(etSearch);
    }

    @Override
    public void showNetworkError() {
        //目前不做网络连接失败的处理
//        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    /**
     * 强制显示系统键盘
     */
    private void keyBoardShow(final EditText txtSearchKey) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 300);
    }

    private void keyBoardHide(EditText txtSearchKey) {
        InputMethodManager m = (InputMethodManager)
                txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m != null) {
            m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
        }
    }
}
