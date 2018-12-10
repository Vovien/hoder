package com.holderzone.intelligencepos.mvp.snack.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.snack.contract.SnackAllDishesRemarkChoiceContract;
import com.holderzone.intelligencepos.mvp.snack.presenter.SnackAllDishesRemarkChoicePresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.EditInputStringFilter;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 批量备注 Activity
 *
 * @author www
 * Created by www on 2018/7/31.
 */
public class SnackAllDishesRemarkChoiceActivity extends BaseActivity<SnackAllDishesRemarkChoiceContract.Presenter> implements SnackAllDishesRemarkChoiceContract.View {
    private static final String KEY_ORDER_DISHES_LIST = "KEY_ORDER_DISHES_LIST";
    private static final String KEY_USE_MEMBER_PRICE = "KEY_USE_MEMBER_PRICE";
    @BindView(R.id.snack_detail_dishes_count)
    TextView snackDetailDishesCount;
    @BindView(R.id.snack_detail_rv)
    RecyclerView snackDetailRv;
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.add_note)
    Title addNote;
    @BindView(R.id.select_all_left)
    Button btnSelectAll;
    @BindView(R.id.batch_note_right)
    Button btnBatchNote;
    @BindView(R.id.batch_note_clear)
    Button btnBatchClear;
    @BindView(R.id.batch_note_sure)
    Button btnBatchSure;
    @BindView(R.id.tv_remark_number)
    TextView tvRemarkNumber;
    @BindView(R.id.batch_note_list)
    View batchNoteList;
    @BindView(R.id.batch_note_context)
    View batchNoteContext;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    /**
     * 获取当前页面（未下单）订单信息列表
     */
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesEList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter<SalesOrderBatchDishesE> mAdapter;
    /**
     * 获取常用备注信息
     */
    private CommonAdapter<DishesRemarkE> mRemarkECommonAdapter;
    private List<DishesRemarkE> mDishesRemarkES = new ArrayList<>();
    /**
     * 是否使用了会员价
     */
    private boolean isUseMemberPrice = false;
    /**
     * 全选状态
     * true = 全部选择
     * false = 部分选择或者未选
     */
    boolean isSelectedAll = true;
    /**
     * 表示当前是哪个页面
     * false = 列表页
     * true = 备注页
     */
    boolean isNoteContext = false;

    public static Intent newIntent(Context context, List<SalesOrderBatchDishesE> mOrderDishesList, boolean isUseMemberPrice) {
        Intent intent = new Intent(context, SnackAllDishesRemarkChoiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_USE_MEMBER_PRICE, isUseMemberPrice);
        bundle.putParcelableArrayList(KEY_ORDER_DISHES_LIST, (ArrayList<? extends Parcelable>) mOrderDishesList);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        isUseMemberPrice = extras.getBoolean(KEY_USE_MEMBER_PRICE);
        mSalesOrderBatchDishesEList = extras.getParcelableArrayList(KEY_ORDER_DISHES_LIST);
        if (mSalesOrderBatchDishesEList != null) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEList) {
                salesOrderBatchDishesE.setCheckedInAll(true);//默认全选
            }
        }
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_all_dishes_remark_choices;
    }

    @Nullable
    @Override
    protected SnackAllDishesRemarkChoiceContract.Presenter initPresenter() {
        return new SnackAllDishesRemarkChoicePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        tvRemarkNumber.setText("0/50字");
        initAdapter();
        initClickEvent();
        checkSelect();
    }

    @SuppressLint("CheckResult")
    private void initClickEvent() {
        RxView.clicks(btnSelectAll).throttleFirst(0, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (isSelectedAll) {
                        for (SalesOrderBatchDishesE dishesE : mSalesOrderBatchDishesEList) {
                            dishesE.setCheckedInAll(false);
                        }
                        isSelectedAll = false;
                        btnSelectAll.setText("全选");
                        btnBatchNote.setEnabled(false);
                    } else {
                        for (SalesOrderBatchDishesE dishesE : mSalesOrderBatchDishesEList) {
                            dishesE.setCheckedInAll(true);
                        }
                        isSelectedAll = true;
                        btnSelectAll.setText("取消全选");
                        btnBatchNote.setEnabled(true);
                    }
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                });
        RxView.clicks(btnBatchNote).throttleFirst(0, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    isNoteContext = true;
                    isBatchNoteContext(isNoteContext);
                });
        RxView.clicks(btnBatchClear).throttleFirst(0, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    etRemarks.setText("");
                });
        RxView.clicks(btnBatchSure).throttleFirst(0, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    String inputRemark = etRemarks.getText().toString();
                    if (StringUtils.isEmpty(inputRemark)) {
                        finishActivityForResult(true);
                        return;
                    }
                    DishesRemarkE dishesRemarkE = new DishesRemarkE();
                    dishesRemarkE.setName(inputRemark);
                    dishesRemarkE.setType(0);
                    mPresenter.addDishesRemark(dishesRemarkE);
                });
        //过滤备注信息
        etRemarks.setFilters(new InputFilter[]{new EditInputStringFilter(50)});
        etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    tvRemarkNumber.setText(s.length() + "/50字");
                } else {
                    tvRemarkNumber.setText("0/50字");
                }
            }
        });
    }

    /**
     * 返回点菜页面
     *
     * @param isClear 选中菜品是否是清空操作
     */
    private void finishActivityForResult(boolean isClear) {
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEList) {
            List<DishesRemarkE> remarkEList = salesOrderBatchDishesE.getArrayOfDishesRemarkE();
            if (isClear && salesOrderBatchDishesE.isCheckedInAll() && remarkEList != null) {
                remarkEList.clear();
            }
        }
        bundle.putParcelableArrayList("allDishesE", (ArrayList<? extends Parcelable>) mSalesOrderBatchDishesEList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    /**
     * 判断是否是填写备注
     */
    private void isBatchNoteContext(boolean isNoteContext) {
        if (isNoteContext) {
            batchNoteList.setVisibility(View.GONE);
            batchNoteContext.setVisibility(View.VISIBLE);
        } else {
            batchNoteList.setVisibility(View.VISIBLE);
            batchNoteContext.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否全部选中
     */
    private void checkSelect() {
        boolean haveSelect = false;
        Set<Boolean> booleanSet = new HashSet<>();
        for (SalesOrderBatchDishesE s : mSalesOrderBatchDishesEList) {
            booleanSet.add(s.isCheckedInAll());
            if (s.isCheckedInAll()) {
                haveSelect = true;
            }
        }
        isSelectedAll = booleanSet.size() == 1 && booleanSet.contains(true);
        btnSelectAll.setText(isSelectedAll ? "取消全选" : "全选");
        btnBatchNote.setEnabled(haveSelect);
    }

    @SuppressLint("SetTextI18n")
    private void addDishesRemark(DishesRemarkE dishesRemarkE) {
        String checkRemarks = etRemarks.getText().toString();
        String newRemark = dishesRemarkE.getName();
        if (checkRemarks.length() + 1 + newRemark.length() > 50) {
            showMessage("最多填写50字");
            return;
        }

        if (StringUtils.isEmpty(checkRemarks)) {
            etRemarks.setText(newRemark);
        } else {
            etRemarks.setText(checkRemarks + "，" + newRemark);
        }
        etRemarks.setSelection(etRemarks.getText().length());
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestDishesRemark();
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<SalesOrderBatchDishesE>(this, R.layout.item_order_detail_batch_note, mSalesOrderBatchDishesEList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                if (salesOrderBatchDishesE.isCheckedInAll()) {
                    holder.setImageResource(R.id.iv_select, R.drawable.user_protocol_selected);
                } else {
                    holder.setImageResource(R.id.iv_select, R.drawable.user_protocol_unselected);
                }
                boolean itemUseMemberPrice = isUseMemberPrice && salesOrderBatchDishesE.getMemberPrice() != null
                        && salesOrderBatchDishesE.getMemberPrice().compareTo(salesOrderBatchDishesE.getPrice()) != 0;
                //隐藏最后一个item的虚线
                if (position == mSalesOrderBatchDishesEList.size() - 1) {
                    holder.setVisible(R.id.itemDivider, false);
                } else {
                    holder.setVisible(R.id.itemDivider, true);
                }
                //菜品名称
                holder.setText(R.id.dishesName, "");
                if (salesOrderBatchDishesE.getCheckStatus() == 0) {
                    //挂起状态
                    holder.setAppendText(R.id.dishesName, getString(R.string.dishes_suspend), R.color.layout_bg_orange_f4a902);
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
                SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesEList.get(position);
                salesOrderBatchDishesE.setCheckedInAll(!salesOrderBatchDishesE.isCheckedInAll());
                checkSelect();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        snackDetailRv.setLayoutManager(new LinearLayoutManager(this));
        snackDetailRv.setAdapter(mAdapter);
        mRemarkECommonAdapter = new CommonAdapter<DishesRemarkE>(getApplicationContext(), R.layout.choice_dishes_remark_item, mDishesRemarkES) {
            @Override
            protected void convert(ViewHolder holder, DishesRemarkE dishesRemarkE, int position) {
                holder.setText(R.id.tv_remark, dishesRemarkE.getName());
                holder.setOnClickListener(R.id.tv_remark, view -> {
                    addDishesRemark(dishesRemarkE);
                    mRemarkECommonAdapter.notifyDataSetChanged();
                });
            }
        };
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setAdapter(mRemarkECommonAdapter);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onAddDishesRemarkSucceed(String msg, DishesRemarkE dishesRemarkE) {
        if (dishesRemarkE.getType() == 0) {//确认备注
            for (SalesOrderBatchDishesE s : mSalesOrderBatchDishesEList) {
                if (s.isCheckedInAll()) {
                    List<DishesRemarkE> dishesRemarkEList = new ArrayList<>();
                    dishesRemarkEList.add(dishesRemarkE);
                    s.setArrayOfDishesRemarkE(dishesRemarkEList);
                }
            }
            finishActivityForResult(false);
        }
    }

    @Override
    public void getDishesRemarkSuccess(List<DishesRemarkE> arrayOfDishesRemarkE) {
        mDishesRemarkES.clear();
        mDishesRemarkES.addAll(arrayOfDishesRemarkE);
        if (mRemarkECommonAdapter != null) {
            mRemarkECommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAddDishesRemarkFailed(boolean IsEnabled) {

    }

    private void initTitle() {
        mTitle.setOnReturnClickListener(() -> {
            finishActivity();
        });
        addNote.setOnReturnClickListener(() -> {
            isNoteContext = false;
            isBatchNoteContext(isNoteContext);
        });
        mTitle.setTitleText("批量备注");
        addNote.setTitleText("填写备注");
        snackDetailDishesCount.setText("下单菜品");
    }
}
