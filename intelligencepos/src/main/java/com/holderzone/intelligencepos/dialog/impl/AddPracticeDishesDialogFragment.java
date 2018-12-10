package com.holderzone.intelligencepos.dialog.impl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 菜品做法dialog
 * Created by chencao on 2017/8/2.
 */

public class AddPracticeDishesDialogFragment extends BaseDialogFragment {
    @BindView(R.id.dishes_name)
    TextView dishesName;
    @BindView(R.id.dishes_dialog_image)
    ImageView dishesDialogImage;
    @BindView(R.id.practice_recycler)
    RecyclerView practiceRecycler;
    @BindView(R.id.reduce_dishes_image)
    ImageView reduceDishesImage;
    @BindView(R.id.dishes_number)
    TextView dishesNumber;
    @BindView(R.id.add_dishes_image)
    ImageView addDishesImage;
    @BindView(R.id.choice_practice_ok)
    Button choicePracticeOk;
    @BindView(R.id.rl_catty)
    RelativeLayout rlCatty;
    @BindView(R.id.tv_catty)
    TextView tvCatty;
    @BindView(R.id.ll_dishes_number)
    LinearLayout llDishesNumber;
    @BindView(R.id.tv_order_dishes)
    TextView tvOrderDishes;
    @BindView(R.id.rl_order_dishes)
    RelativeLayout rlOrderDishes;
    /**
     * 菜品预定的做法集合
     */
    private List<DishesPracticeE> dishesPracticeEs;
    private SalesOrderBatchDishesE salesOrderBatchDishesE;
    private static final String DISHESPRACTICE = "DishesPracticeE";
    private static final String SALESORDERBATCHDISHESE = "SalesOrderBatchDishesE";
    private static final String ESTIMATE = "Estimate";
    private String dishesEName;
    private double dishesENumber = 0;
    /**
     * 记录菜品是否是称重菜品
     */
    private boolean weightEnable = false;
    /**
     * 估清信息
     */
    private Double mEstimate = Double.MAX_VALUE;
    /**
     * 已经选择的菜品做法集合
     */
    private List<DishesPracticeE> choiceDishesPracticeEs = new ArrayList<>();
    private CommonAdapter<DishesPracticeE> dishesPracticeECommonAdapter;

    public static AddPracticeDishesDialogFragment newInstance(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE, double mEstimate) {
        Bundle args = new Bundle();
        args.putDouble(ESTIMATE, mEstimate);
        args.putParcelable(SALESORDERBATCHDISHESE, salesOrderBatchDishesE);
        args.putParcelableArrayList(DISHESPRACTICE, (ArrayList<? extends Parcelable>) dishesPracticeEs);
        AddPracticeDishesDialogFragment fragment = new AddPracticeDishesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        dishesPracticeEs = args.getParcelableArrayList(DISHESPRACTICE);
        salesOrderBatchDishesE = args.getParcelable(SALESORDERBATCHDISHESE);
        mEstimate = args.getDouble(ESTIMATE);
        dishesENumber = salesOrderBatchDishesE.getOrderCount();
        dishesEName = salesOrderBatchDishesE.getDishesName();
        choiceDishesPracticeEs = salesOrderBatchDishesE.getArrayOfDishesPracticeE();
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_practice_choice;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {
        dishesName.setText(dishesEName);
        weightEnable = salesOrderBatchDishesE.isWeighEnable();
        changeShowLayout();
        initClickEvent();
        initRecycler();

    }

    private void changeShowLayout() {
        if (weightEnable) {
            rlCatty.setVisibility(View.VISIBLE);
            llDishesNumber.setVisibility(View.GONE);
            dishesENumber = 0;
            choicePracticeOk.setEnabled(false);
        } else {
            rlCatty.setVisibility(View.GONE);
            llDishesNumber.setVisibility(View.VISIBLE);
            rlOrderDishes.setVisibility(View.GONE);
            if (ArithUtil.sub(mEstimate, dishesENumber) > 1) {
                choicePracticeOk.setEnabled(true);
                addDishesImage.setImageResource(R.drawable.add_blue_max);
                dishesENumber = 1;
            } else if (ArithUtil.sub(mEstimate, dishesENumber) > 0) {
                addDishesImage.setImageResource(R.drawable.add_gray);
                dishesENumber = ArithUtil.sub(mEstimate, dishesENumber);
                choicePracticeOk.setEnabled(true);
            } else {
                dishesENumber = 0;
                choicePracticeOk.setEnabled(false);
            }
        }
        setDishesNumber(dishesENumber);
    }

    @SuppressLint("CheckResult")
    private void initClickEvent() {

        RxView.clicks(rlCatty).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            callBack.changePracticeCount(dishesENumber, mEstimate);
        });

        RxView.clicks(tvOrderDishes).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            addDishes();
        });

        tvCatty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    choicePracticeOk.setEnabled(false);
                } else {
                    String temp = s.toString();
                    try {
                        if (Double.valueOf(temp) == 0) {
                            choicePracticeOk.setEnabled(false);
                        } else {
                            choicePracticeOk.setEnabled(true);
                        }
                    } catch (Exception e) {
                        choicePracticeOk.setEnabled(false);
                    }
                }
            }
        });
        dishesNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null) {
                    choicePracticeOk.setEnabled(false);
                } else {
                    String temp = s.toString();
                    if (Double.valueOf(temp) == 0) {
                        addDishesImage.setImageResource(R.drawable.add_blue_max);
                        choicePracticeOk.setEnabled(false);
                    } else {
                        if (Double.valueOf(temp) >= mEstimate) {
                            addDishesImage.setImageResource(R.drawable.add_gray);
                        } else {
                            addDishesImage.setImageResource(R.drawable.add_blue_max);
                        }
                        choicePracticeOk.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecycler() {
        dishesPracticeECommonAdapter = new CommonAdapter<DishesPracticeE>(getContext(), R.layout.dialog_practice_choice_item, dishesPracticeEs) {
            @Override
            protected void convert(ViewHolder holder, DishesPracticeE dishesPracticeE, int position) {
                holder.setText(R.id.practice_choice_item, dishesPracticeE.getName() + "+" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesPracticeE.getFees())));
                boolean isContain = false;
                if (choiceDishesPracticeEs != null) {
                    for (int i = 0; i < choiceDishesPracticeEs.size(); i++) {
                        if (choiceDishesPracticeEs.get(i).getDishesPracticeGUID().equalsIgnoreCase(dishesPracticeE.getDishesPracticeGUID())) {
                            isContain = true;
                        }
                    }
                }
                if (isContain) {
                    holder.setTextColorRes(R.id.practice_choice_item, R.color.common_text_color_2495ee);
                    holder.setBackgroundRes(R.id.practice_choice_item, R.drawable.shape_edit_choose_blue_bg);
                } else {
                    holder.setTextColorRes(R.id.practice_choice_item, R.color.confirm_text_color);
                    holder.setBackgroundRes(R.id.practice_choice_item, R.drawable.shape_edit_choose_gray_bg);
                }
            }
        };

        dishesPracticeECommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (choiceDishesPracticeEs == null) {
                    choiceDishesPracticeEs = new ArrayList<DishesPracticeE>();
                }
                boolean isRemove = false;
                for (int i = 0; i < choiceDishesPracticeEs.size(); i++) {
                    if (dishesPracticeEs.get(position).getDishesPracticeGUID().equalsIgnoreCase(choiceDishesPracticeEs.get(i).getDishesPracticeGUID())) {
                        isRemove = true;
                    }
                }
                if (isRemove) {
                    for (Iterator<DishesPracticeE> iterator = choiceDishesPracticeEs.iterator(); iterator.hasNext(); ) {
                        if (iterator.next().getDishesPracticeGUID().equalsIgnoreCase(dishesPracticeEs.get(position).getDishesPracticeGUID())) {
                            iterator.remove();
                        }
                    }
                } else {
                    choiceDishesPracticeEs.add(dishesPracticeEs.get(position));
                }
                dishesPracticeECommonAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        practiceRecycler.setLayoutManager(linearLayoutManager);
//        practiceRecycler.addItemDecoration(new LinearSpacingItemDecoration(ConvertUtils.dp2px(10.7f), false));
        practiceRecycler.setAdapter(dishesPracticeECommonAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(Object o) {

    }


    @OnClick({R.id.dishes_dialog_image, R.id.reduce_dishes_image, R.id.add_dishes_image, R.id.choice_practice_ok
            , R.id.dishes_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dishes_dialog_image:
                dismiss();
                break;
            case R.id.reduce_dishes_image:
                if (dishesENumber <= 0) {
                    return;
                }
                if (dishesENumber > 0 && dishesENumber < 1) {
                    dishesENumber = 0;
                } else {
                    dishesENumber = ArithUtil.sub(dishesENumber, 1);
                }
                setDishesNumber(dishesENumber);
                break;
            case R.id.add_dishes_image:
                addDishes();
                break;
            case R.id.choice_practice_ok:
                salesOrderBatchDishesE.setOrderCount(dishesENumber);
                salesOrderBatchDishesE.setArrayOfDishesPracticeE(choiceDishesPracticeEs);
                callBack.callBackOK(salesOrderBatchDishesE);
                dismiss();
                break;
            case R.id.dishes_number:
                if (mEstimate != null && (mEstimate <= 0 || ArithUtil.sub(mEstimate, dishesENumber) <= 0)) {
                    BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate));
                    return;
                }
                callBack.changePracticeCount(dishesENumber, mEstimate);
                break;
            default:
                break;
        }
    }

    private void addDishes() {
        boolean isLittle = false;
        if (mEstimate != null && (mEstimate <= 0 || ArithUtil.sub(mEstimate, dishesENumber) <= 0)) {
            BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate));
            return;
        } else if (ArithUtil.sub(mEstimate, dishesENumber) < 1) {
            isLittle = true;
            salesOrderBatchDishesE.setOrderCount(ArithUtil.sub(mEstimate, dishesENumber));
            if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
                for (SalesOrderBatchDishesE sub : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                    sub.setOrderCount(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), sub.getPackageDishesUnitCount()));
                }
            }
        }
        if (isLittle) {
            dishesENumber = ArithUtil.add(dishesENumber, ArithUtil.sub(mEstimate, dishesENumber));
            BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate_small, salesOrderBatchDishesE.getDishesUnit() == null ? "" : salesOrderBatchDishesE.getDishesUnit()));
        } else {
            dishesENumber = ArithUtil.add(dishesENumber, 1);
        }
        setDishesNumber(dishesENumber);
    }

    private void setDishesNumber(double dishesENumber) {
        SpanUtils spanUtils = new SpanUtils();

        if (weightEnable) {
            rlCatty.setVisibility(View.VISIBLE);
            llDishesNumber.setVisibility(View.GONE);
            rlOrderDishes.setVisibility(View.GONE);
            if (dishesENumber == 0) {
                spanUtils.append("输入重量")
                        .setForegroundColor(ContextCompat.getColor(getContext(), R.color.common_text_color_2495ee));
                choicePracticeOk.setEnabled(false);
            } else {
                spanUtils.append(ArithUtil.stripTrailingZeros(dishesENumber))
                        .setForegroundColor(ContextCompat.getColor(getContext(), R.color.common_text_color_000000))
                        .setUnderline();
                choicePracticeOk.setEnabled(true);
            }
            tvCatty.setText(spanUtils.create());
        } else {
            rlCatty.setVisibility(View.GONE);
            if (dishesENumber > 0) {
                llDishesNumber.setVisibility(View.VISIBLE);
                rlOrderDishes.setVisibility(View.GONE);
                spanUtils.append(ArithUtil.stripTrailingZeros(dishesENumber)).setUnderline();
                dishesNumber.setText(spanUtils.create());
                choicePracticeOk.setEnabled(true);
            } else {
                llDishesNumber.setVisibility(View.GONE);
                rlOrderDishes.setVisibility(View.VISIBLE);
                choicePracticeOk.setEnabled(false);
            }
        }
    }

    public interface CallBackPracticeOK {
        void callBackOK(SalesOrderBatchDishesE salesOrderBatchDishesE);

        void changePracticeCount(double currentCount, double maxCount);
    }

    private CallBackPracticeOK callBack;

    public void setCallBack(CallBackPracticeOK callBack) {
        this.callBack = callBack;
    }

    /**
     * 修改菜品数量
     */
    public void setNetPracticeCount(double count) {
        dishesENumber = count;
        setDishesNumber(dishesENumber);
    }
}
