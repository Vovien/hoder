package com.holderzone.intelligencepos.adapter.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageItemE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * 快销菜品 适配器
 * Created by chencao on 2017/8/2.
 */

public class SnackNewOrderRightAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    private List<DishesTypeE> dishesTypeEList;
    private List<DishesE> dishesEList;
    private Map<String, List<DishesPracticeE>> dishesPracticeMap;
    private boolean useMemberPrice;
    /**
     * 各菜品列表的数量
     */
    private Map<String, Double> dishesOrderCount = new ArrayMap<>();
    /**
     * 估清信息
     */
    private Map<String, DishesEstimateRecordDishes> mEstimateMap = new HashMap<>();
    /**
     * 停售信息
     */
    private Map<String, Boolean> stopSalse = new ArrayMap<>();

    public void setDishesPracticeMap(Map<String, List<DishesPracticeE>> dishesPracticeMap) {
        this.dishesPracticeMap = dishesPracticeMap;
        notifyDataSetChanged();
    }

    /**
     * 记录菜品类型列表的位置
     */
    private ArrayMap<String, Integer> mDishesTypeEPositionHashMap = new ArrayMap<>();

    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private double[] dishesNum;

    public SnackNewOrderRightAdapter(Context context) {
        this.context = context;
    }

    public void setDishesEList(List<DishesE> dishesEList, List<DishesTypeE> dishesTypeEList, ArrayMap<String, Integer> mDishesTypeEPositionHashMap) {
        this.dishesTypeEList = dishesTypeEList;
        this.dishesEList = dishesEList;
        this.mDishesTypeEPositionHashMap = mDishesTypeEPositionHashMap;
        initDishesNum();
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    /**
     * 初始化各个菜品数量
     */
    private void initDishesNum() {
        int leng = dishesEList.size();
        dishesNum = new double[leng];
        for (int i = 0; i < leng; i++) {
            if (dishesOrderCount != null) {
                Double tempCount = dishesOrderCount.get(dishesEList.get(i).getDishesGUID());
                dishesNum[i] = tempCount == null ? 0 : tempCount;
            } else {
                dishesNum[i] = 0;
            }
        }
    }

    /**
     * 存放每个分组的第一条的ID
     *
     * @return
     */
    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<>();
        String lastFirstPoi = null;
        for (int i = 0; i < dishesEList.size(); i++) {
            if (dishesEList.get(i).getDishesTypeGUID().equals(lastFirstPoi)) {
                lastFirstPoi = dishesEList.get(i).getDishesTypeGUID();
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    /**
     * 填充每一个分组要展现的数据
     *
     * @return
     */
    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = dishesTypeEList.get(i).getName();
        }
        return letters;
    }

    public void setOrderDishesGroup(OrderDishesGroup orderDishesGroup) {
        if (orderDishesGroup == null || orderDishesGroup.getOrderDishesRecordList() == null) {
            return;
        }
        dishesOrderCount.clear();
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = orderDishesGroup.getOrderDishesRecordList();
        for (int i = 0; i < salesOrderBatchDishesEList.size(); i++) {
            SalesOrderBatchDishesE salesOrderBatchDishesE = salesOrderBatchDishesEList.get(i);
            double temp = salesOrderBatchDishesE.getOrderCount();
            String dishesGUID = salesOrderBatchDishesE.getDishesGUID().toLowerCase();
            Double tempCount = dishesOrderCount.get(dishesGUID);
            double count = tempCount != null ? ArithUtil.add(tempCount, temp) : temp;
            dishesOrderCount.put(dishesGUID, count);
        }
        initDishesNum();
        notifyDataSetChanged();
    }

    public void setStopSalse(Map<String, Boolean> stopSalse, Map<String, DishesEstimateRecordDishes> mEstimateMap) {
        this.stopSalse.clear();
        this.stopSalse.putAll(stopSalse);
        this.mEstimateMap.clear();
        this.mEstimateMap.putAll(mEstimateMap);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dishesEList != null ? dishesEList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dishesEList != null ? dishesEList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    DishesEstimateRecordDishes estimate = null;

    public void resetUseMemberPrice(boolean useMemberPrice) {
        this.useMemberPrice = useMemberPrice;
        notifyDataSetChanged();
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishesE bean = dishesEList.get(position);
        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_snack_new_order_right_list_center, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (position + 1 < dishesEList.size()) {
            vh.topDivider.setVisibility(View.VISIBLE);
        } else {
            vh.topDivider.setVisibility(View.GONE);
        }
        vh.dishesName.setText(bean.getSimpleName());
        //2018-04-02  添加会员价
        if (useMemberPrice && bean.getMemberPrice() != null && bean.getMemberPrice().compareTo(bean.getCheckPrice()) != 0) {
            vh.dishesPrice.setText(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(bean.getMemberPrice())));
            vh.originalPrice.setVisibility(View.VISIBLE);
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(bean.getCheckPrice())))
                    .setStrikethrough()
                    .append("/" + bean.getCheckUnit()).setFontSize(context.getResources().getDimensionPixelSize(R.dimen.tv_text_size_10)).setStrikethrough();
            vh.originalPrice.setText(spanUtils.create());
        } else {
            vh.dishesPrice.setText(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(bean.getCheckPrice())));
            vh.originalPrice.setVisibility(View.GONE);
        }
        String unit = bean.getCheckUnit();
        if (unit == null) {
            vh.dishesUnit.setText("");
        } else {
            unit = "/" + unit;
            vh.dishesUnit.setText(unit);
        }
        vh.dishesNumber.setText(bean.getCode());
        boolean isWightEnable = bean.getWeighEnable() != null && bean.getWeighEnable() == 1;

        if (bean.getArrayOfDishesPracticeE() != null) {
            //斤数
            vh.llCatty.setVisibility(View.GONE);
            vh.operationLayout.setVisibility(View.GONE);
            vh.practiceLayout.setVisibility(View.VISIBLE);
            vh.dishesOperation.setEnabled(true);
            vh.dishesOperation.setText("选做法");
            if (isWightEnable) {
                vh.dishesOperation.setTextColor(ContextCompat.getColor(context, R.color.common_text_color_2495ee));
                vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_choose_blue_text_bg);
            } else {
                vh.dishesOperation.setTextColor(ContextCompat.getColor(context, R.color.tv_text_white_ffffff));
                vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_choose_bg);
            }
            if (dishesNum[position] == 0) {
                vh.practiceOrderCount.setVisibility(View.GONE);
            } else {
                vh.practiceOrderCount.setVisibility(View.VISIBLE);
                vh.practiceOrderCount.setText("×" + ArithUtil.stripTrailingZeros(dishesNum[position]));
            }
        } else if (bean.getIsPackageDishes() == 1 && bean.getPackageType() == 1) {
            //斤数
            vh.llCatty.setVisibility(View.GONE);
            vh.operationLayout.setVisibility(View.GONE);
            vh.practiceLayout.setVisibility(View.VISIBLE);
            vh.dishesOperation.setEnabled(true);
            vh.dishesOperation.setText("选套餐");
            vh.dishesOperation.setTextColor(ContextCompat.getColor(context, R.color.tv_text_white_ffffff));
            vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_choose_bg);
            if (dishesNum[position] == 0) {
                vh.practiceOrderCount.setVisibility(View.GONE);
            } else {
                vh.practiceOrderCount.setVisibility(View.VISIBLE);
                vh.practiceOrderCount.setText("×" + ArithUtil.stripTrailingZeros(dishesNum[position]));
            }
        } else {
            if (isWightEnable) {
                vh.llCatty.setVisibility(View.VISIBLE);
                vh.operationLayout.setVisibility(View.GONE);
                vh.practiceLayout.setVisibility(View.GONE);
            } else {
                vh.llCatty.setVisibility(View.GONE);
                vh.operationLayout.setVisibility(View.VISIBLE);
                vh.practiceLayout.setVisibility(View.GONE);
            }
        }

        if (bean.isStopSale()) {
            setIsStopSale(vh);
        } else {
            if (isWightEnable) {
                SpanUtils spanUtils = new SpanUtils();
                if (dishesNum[position] == 0) {
                    spanUtils.append("输入重量")
                            .setFontSize(13, true)
                            .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_2495ee));
                    vh.etInputCatty.setBackgroundResource(R.drawable.shape_edit_choose_blue_text_bg);
                    vh.etInputCatty.setText(spanUtils.create());
                } else {
                    spanUtils.append(ArithUtil.stripTrailingZeros(dishesNum[position]))
                            .setFontSize(16, true)
                            .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_000000))
                            .setUnderline();
                    vh.etInputCatty.setBackgroundResource(R.color.btn_text_white_ffffff);
                    vh.etInputCatty.setText(spanUtils.create());
                }
            } else if (mEstimateMap != null) {
                estimate = mEstimateMap.get(bean.getDishesGUID().toLowerCase());
                if (estimate != null) {
                    if (estimate.getResidueCount() <= 0) {
                        setIsStopSale(vh);
                    } else if (ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]) <= 0) {
                        if (estimate.getResidueCount() < dishesNum[position]) {
                            vh.dishesCount.setTextColor(context.getResources().getColor(R.color.btn_bg_light_red_normal));
                            vh.practiceOrderCount.setTextColor(context.getResources().getColor(R.color.btn_bg_light_red_normal));
                        } else {
                            vh.dishesCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                            vh.practiceOrderCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                        }
                        vh.addDishes.setImageResource(R.drawable.add_gray);
                    } else {
                        vh.dishesCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                        vh.practiceOrderCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                        vh.addDishes.setImageResource(R.drawable.add_blue_max);
                    }
                } else {
                    vh.dishesCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                    vh.practiceOrderCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                    vh.addDishes.setImageResource(R.drawable.add_blue_max);
                }
            } else if (stopSalse.get(bean.getDishesGUID()) == null ? false : stopSalse.get(bean.getDishesGUID())) {
                setIsStopSale(vh);
            } else {
                vh.dishesCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                vh.practiceOrderCount.setTextColor(context.getResources().getColor(R.color.common_text_color_000000));
                vh.addDishes.setImageResource(R.drawable.add_blue_max);
            }
        }

        isSelected(dishesNum[position], vh);

        RxView.clicks(vh.etInputCatty).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
            //设置已经点的菜品数量
            salesOrderBatchDishesE.setOrderCount(dishesNum[position]);
            callBackClick.onCattyDishes(salesOrderBatchDishesE);
        });

        RxView.clicks(vh.addDishes).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
            boolean isLittle = false;
            //菜品估清信息
            estimate = mEstimateMap.get(bean.getDishesGUID().toLowerCase());
            if (estimate != null) {
                if (ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]) <= 0) {
                    BaseApplication.showMessage(context.getString(R.string.add_dishes_failed_by_estimate));
                    return;
                } else if (ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]) < 1) {
                    isLittle = true;
                    salesOrderBatchDishesE.setOrderCount(ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]));
                    if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
                        for (SalesOrderBatchDishesE sub : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                            sub.setOrderCount(ArithUtil.add(salesOrderBatchDishesE.getOrderCount(), sub.getPackageDishesUnitCount()));
                        }
                    }
                }
            }
            //判断是否有做法
            if (bean.getArrayOfDishesPracticeE() != null) {
                List<DishesPracticeE> dishesPracticeEs = dishesPracticeMap.get(bean.getDishesGUID());
                salesOrderBatchDishesE.setOrderCount(0d);
                callBackClick.addDishesEPractice(dishesPracticeEs, salesOrderBatchDishesE, dishesOrderCount);
                return;
            }
            if (isLittle) {
                String salesUnit = salesOrderBatchDishesE.getDishesUnit();
                BaseApplication.showMessage(context.getString(R.string.add_dishes_failed_by_estimate_small, salesUnit == null ? "" : salesUnit));
                dishesNum[position] = estimate.getResidueCount();
            } else {
                dishesNum[position] = ArithUtil.add(dishesNum[position], 1);
            }
            isSelected(dishesNum[position], vh);
            callBackClick.addDishesE(salesOrderBatchDishesE);
        });
        RxView.clicks(vh.reduceDishes).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
            if (bean.getArrayOfDishesPracticeE() != null) {
                callBackClick.reduceDishesEDialog(salesOrderBatchDishesE.getDishesGUID());
                return;
            }
            if (dishesNum[position] > 0 && dishesNum[position] < 1) {
                dishesNum[position] = 0;
            } else {
                dishesNum[position] = ArithUtil.sub(dishesNum[position], 1);
            }
            isSelected(dishesNum[position], vh);
            callBackClick.reduceDishesE(salesOrderBatchDishesE);
        });
        //添加做法或者选择套餐按钮
        RxView.clicks(vh.dishesOperation).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
            salesOrderBatchDishesE.setOrderCount(0d);
            if (bean.getIsPackageDishes() == 1 && bean.getPackageType() == 1) {
                callBackClick.onAddPackageGroup(salesOrderBatchDishesE.getDishesGUID(), dishesOrderCount);
            } else {
                List<DishesPracticeE> dishesPracticeEs = dishesPracticeMap.get(bean.getDishesGUID());
                callBackClick.addDishesEPractice(dishesPracticeEs, salesOrderBatchDishesE, dishesOrderCount);
            }
        });
        //修改数量
        RxView.clicks(vh.dishesCount).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
            //设置已经点的菜品数量
            salesOrderBatchDishesE.setOrderCount(dishesNum[position]);
            callBackClick.onChangeCount(salesOrderBatchDishesE);
        });
        return convertView;
    }

    private void setIsStopSale(ViewHolder vh) {
        vh.operationLayout.setVisibility(View.GONE);
        vh.practiceLayout.setVisibility(View.VISIBLE);
        vh.practiceOrderCount.setVisibility(View.GONE);
        vh.llCatty.setVisibility(View.GONE);
        vh.dishesOperation.setText("已售罄");
        vh.dishesOperation.setEnabled(false);
        vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_no_choose_bg);
    }

    private SalesOrderBatchDishesE getSalesOrderBatchDishesE(int position) {
        DishesE dishesE = dishesEList.get(position);
        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
        salesOrderBatchDishesE.setSalesOrderBatchGUID(UUID.randomUUID().toString());
        salesOrderBatchDishesE.setDishesE(dishesE);
        salesOrderBatchDishesE.setWeighEnable(dishesE.getWeighEnable() != null && dishesE.getWeighEnable() == 1);
        salesOrderBatchDishesE.setCheckStatus(1);
        salesOrderBatchDishesE.setKitchenPrintStatus(1);
        salesOrderBatchDishesE.setNew(true);
        salesOrderBatchDishesE.setDishesUnit(dishesE.getCheckUnit());
        salesOrderBatchDishesE.setGift(0);
        salesOrderBatchDishesE.setIsPackageDishes(dishesE.getIsPackageDishes());
        salesOrderBatchDishesE.setOrderCount(1.0);
        if (dishesE.getIsPackageDishes() == 1 && dishesE.getPackageType() == 0) {//套餐
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

    /**
     * 判断是否某菜品有添加到
     *
     * @param i  条目下标
     * @param vh ViewHolder
     */
    private void isSelected(double i, ViewHolder vh) {
        if (i == 0) {
            vh.dishesCount.setVisibility(View.GONE);
            vh.reduceDishes.setVisibility(View.GONE);
        } else {
            SpanUtils dishesCount = new SpanUtils();
            vh.dishesCount.setVisibility(View.VISIBLE);
            if (i == (int) i) {
                dishesCount.append((int) i + "");
            } else {
                dishesCount.append(context.getString(R.string.two_decimal_str, ArithUtil.stripTrailingZeros(i)));
            }
            dishesCount.setUnderline();
            vh.dishesCount.setText(dishesCount.create());
            vh.reduceDishes.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public Object[] getSections() {
//        return mSectionLetters;
//    }
//
//    @Override
//    public int getPositionForSection(int sectionIndex) {
//        if (mSectionIndices.length == 0) {
//            return 0;
//        }
//        if (sectionIndex >= mSectionIndices.length) {
//            sectionIndex = mSectionIndices.length - 1;
//        } else if (sectionIndex < 0) {
//            sectionIndex = 0;
//        }
//        return mSectionIndices[sectionIndex];
//    }
//
//    @Override
//    public int getSectionForPosition(int position) {
//        for (int i = 0; i < mSectionIndices.length; i++) {
//            if (position < mSectionIndices[i]) {
//                return i - 1;
//            }
//        }
//        return mSectionIndices.length - 1;

//    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder hVH;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_snack_new_order_right_list_title, null);
            hVH = new HeaderViewHolder(convertView);
            convertView.setTag(hVH);
        } else {
            hVH = (HeaderViewHolder) convertView.getTag();
        }
        hVH.titleLyout.setVisibility(View.VISIBLE);
        hVH.dishesTypeName.setText(dishesTypeEList.get(
                mDishesTypeEPositionHashMap.get(dishesEList.get(position).getDishesTypeGUID())).getName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mDishesTypeEPositionHashMap.get(dishesEList.get(position).getDishesTypeGUID());
    }


    private class HeaderViewHolder {
        public final TextView dishesTypeName;
        public final LinearLayout titleLyout;
        public final View root;

        public HeaderViewHolder(View root) {
            dishesTypeName = root.findViewById(R.id.snack_new_order_right_title);
            titleLyout = root.findViewById(R.id.snack_new_order_right_title_layout);
            this.root = root;
        }
    }

    public class ViewHolder {
        public final TextView dishesName;
        public final TextView dishesPrice;
        public final TextView originalPrice;
        public final TextView dishesUnit;
        public final ImageView addDishes;
        public final TextView dishesCount;
        public final ImageView reduceDishes;
        public final TextView dishesOperation;
        public final LinearLayout operationLayout;
        public final View topDivider;
        public final View root;
        private final LinearLayout practiceLayout;
        private final TextView practiceOrderCount;
        private final TextView dishesNumber;

        private final LinearLayout llCatty;
        private final TextView etInputCatty;

        public ViewHolder(View root) {
            dishesName = root.findViewById(R.id.snack_new_order_right_center_item_name);
            dishesPrice = root.findViewById(R.id.snack_new_order_right_center_item_price);
            dishesUnit = root.findViewById(R.id.snack_new_order_right_center_item_price_unit);
            originalPrice = root.findViewById(R.id.tv_original_price);
            addDishes = root.findViewById(R.id.add_image);
            dishesCount = root.findViewById(R.id.dishes_count);
            reduceDishes = root.findViewById(R.id.reduce_image);
            dishesOperation = root.findViewById(R.id.snack_new_order_right_center_item_operation_text);
            operationLayout = root.findViewById(R.id.snack_new_order_right_center_item_operation_layout);
            topDivider = root.findViewById(R.id.top_divider);
            practiceLayout = root.findViewById(R.id.practice_order_dishes_ll);
            practiceOrderCount = root.findViewById(R.id.practice_order_dishes_count);
            dishesNumber = root.findViewById(R.id.tv_dishes_number);

            llCatty = root.findViewById(R.id.ll_catty);
            etInputCatty = root.findViewById(R.id.et_input_catty);
            this.root = root;
        }
    }

    private CallBackClick callBackClick;

    public interface CallBackClick {
        /**
         * 普通减菜操作
         *
         * @param salesOrderBatchDishesE
         */
        void reduceDishesE(SalesOrderBatchDishesE salesOrderBatchDishesE);

        /**
         * 普通加菜操作
         *
         * @param salesOrderBatchDishesE
         */
        void addDishesE(SalesOrderBatchDishesE salesOrderBatchDishesE);

        /**
         * 加菜时 菜品有做法 显示加菜的对话框
         *
         * @param dishesPracticeEs
         * @param salesOrderBatchDishesE
         * @param dishesOrderCount
         */
        void addDishesEPractice(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE, Map<String, Double> dishesOrderCount);

        /**
         * 有做法的菜品减少菜品时显示的对话框
         */
        void reduceDishesEDialog(String dishesGUID);

        /**
         * 修改点菜数量
         */
        void onChangeCount(SalesOrderBatchDishesE salesOrderBatchDishesE);

        /**
         * 自选套餐
         */
        void onAddPackageGroup(String dishesGUID, Map<String, Double> dishesOrderCount);

        /**
         * 斤数菜品
         *
         * @param salesOrderBatchDishesE
         */
        void onCattyDishes(SalesOrderBatchDishesE salesOrderBatchDishesE);
    }

    public void setCallBackClick(CallBackClick callBackClick) {
        this.callBackClick = callBackClick;
    }
}
