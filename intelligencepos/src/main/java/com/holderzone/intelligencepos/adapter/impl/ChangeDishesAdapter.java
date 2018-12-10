package com.holderzone.intelligencepos.adapter.impl;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.concurrent.TimeUnit;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by LT on 2018-04-04.
 */

public class ChangeDishesAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    private List<DishesTypeE> dishesTypeEList;
    private List<DishesE> dishesEList;
    private Map<String, List<DishesPracticeE>> dishesPracticeMap;
    //是否是外卖
    private boolean isTakeway;

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

    public ChangeDishesAdapter(Context context, boolean isTakeway) {
        this.context = context;
        this.isTakeway = isTakeway;
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
        initDishesNum();
        notifyDataSetChanged();
    }

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_change_order_dishes, null);
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
        vh.dishesName.setText(dishesEList.get(position).getSimpleName());
        vh.dishesPrice.setText(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesEList.get(position).getCheckPrice())));
        String unit = dishesEList.get(position).getCheckUnit();
        if (unit == null) {
            vh.dishesUnit.setText("");
        } else {
            unit = "/" + unit;
            vh.dishesUnit.setText(unit);
        }

//        if (dishesEList.get(position).getArrayOfDishesPracticeE() != null) {
//            vh.operationLayout.setVisibility(View.GONE);
//            vh.dishesOperation.setVisibility(View.VISIBLE);
//            vh.dishesOperation.setEnabled(true);
//            vh.dishesOperation.setText("选做法");
//            vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_choose_bg);
//        } else {
//            vh.operationLayout.setVisibility(View.GONE);
//            vh.dishesOperation.setVisibility(View.GONE);
//        }
        if (isTakeway) {//外卖
            vh.dishesOperation.setVisibility(View.VISIBLE);
            vh.dishesOperation.setText("选择");
            vh.content.setEnabled(true);
        } else {//微信
            vh.content.setEnabled(true);
            vh.dishesOperation.setVisibility(View.VISIBLE);
            if (dishesEList.get(position).getArrayOfDishesPracticeE() != null) {//有做法
                vh.dishesOperation.setText("选做法");
                vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_choose_bg);
            } else {
                vh.dishesOperation.setText("选择");
                vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_choose_bg);
            }
            if (dishesEList.get(position).isStopSale()) {//停售
                setIsStopSale(vh);
            }
        }

//        if (dishesEList.get(position).isStopSale()) {
//            setIsStopSale(vh);
//        } else {
//            if (mEstimateMap != null) {
//                estimate = mEstimateMap.get(dishesEList.get(position).getDishesGUID().toLowerCase());
//                if (estimate != null) {
//                    if (estimate.getResidueCount() <= 0) {
//                        setIsStopSale(vh);
//                    } else if (ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]) <= 0) {
//                        if (estimate.getResidueCount() < dishesNum[position]) {
//                            vh.dishesCount.setTextColor(context.getResources().getColor(R.color.btn_bg_light_red_normal));
//                        } else {
//                            vh.dishesCount.setTextColor(context.getResources().getColor(R.color.btn_text_black_000000));
//                        }
//                        vh.addDishes.setImageResource(R.drawable.add_gray);
//                    } else {
//                        vh.dishesCount.setTextColor(context.getResources().getColor(R.color.btn_text_black_000000));
//                        vh.addDishes.setImageResource(R.drawable.add_blue_max);
//                    }
//                } else {
//                    vh.dishesCount.setTextColor(context.getResources().getColor(R.color.btn_text_black_000000));
//                    vh.addDishes.setImageResource(R.drawable.add_blue_max);
//                }
//            } else if (stopSalse.get(dishesEList.get(position).getDishesGUID()) == null ? false : stopSalse.get(dishesEList.get(position).getDishesGUID())) {
//                setIsStopSale(vh);
//            } else {
//                vh.dishesCount.setTextColor(context.getResources().getColor(R.color.btn_text_black_000000));
//                vh.addDishes.setImageResource(R.drawable.add_blue_max);
//            }
//        }

//        RxView.clicks(vh.addDishes).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
//            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
//            boolean isLittle = false;
//            /**
//             * 菜品估清信息
//             */
//            estimate = mEstimateMap.get(dishesEList.get(position).getDishesGUID().toLowerCase());
//            if (estimate != null) {
//                if (ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]) <= 0) {
//                    BaseApplication.showMessage(context.getString(R.string.add_dishes_failed_by_estimate));
//                    return;
//                } else if (ArithUtil.sub(estimate.getResidueCount(), dishesNum[position]) < 1) {
//                    isLittle = true;
//                    salesOrderBatchDishesE.setOrderCount(estimate.getResidueCount() - dishesNum[position]);
//                    if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
//                        for (SalesOrderBatchDishesE sub : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
//                            sub.setOrderCount(ArithUtil.add(salesOrderBatchDishesE.getOrderCount(), sub.getPackageDishesUnitCount()));
//                        }
//                    }
//                }
//            }
//            /**
//             * 判断是否有做法
//             */
//            if (dishesEList.get(position).getArrayOfDishesPracticeE() != null) {
//                List<DishesPracticeE> dishesPracticeEs = dishesPracticeMap.get(dishesEList.get(position).getDishesGUID());
//                salesOrderBatchDishesE.setOrderCount(0d);
//                callBackClick.addDishesEPractice(dishesPracticeEs, salesOrderBatchDishesE, dishesOrderCount);
//                return;
//            }
//
//            if (isLittle) {
//                String salesUnit = salesOrderBatchDishesE.getDishesUnit();
//                BaseApplication.showMessage(context.getString(R.string.add_dishes_failed_by_estimate_small, salesUnit == null ? "" : salesUnit));
//                dishesNum[position] = estimate.getResidueCount();
//            } else {
//                dishesNum[position]++;
//            }
//            callBackClick.addDishesE(salesOrderBatchDishesE);
//        });
//        RxView.clicks(vh.reduceDishes).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
//            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
//            if (dishesEList.get(position).getArrayOfDishesPracticeE() != null) {
//                callBackClick.reduceDishesEDialog(salesOrderBatchDishesE.getDishesGUID());
//                return;
//            }
//            if (dishesNum[position] > 0 && dishesNum[position] < 1) {
//                dishesNum[position] = 0;
//            } else {
//                dishesNum[position]--;
//            }
//            isSelected(dishesNum[position], vh);
//            callBackClick.reduceDishesE(salesOrderBatchDishesE);
//        });
        /**
         * 添加做法
         */
        RxView.clicks(vh.content).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            SalesOrderBatchDishesE salesOrderBatchDishesE = getSalesOrderBatchDishesE(position);
            if (isTakeway) {//外卖
                callBackClick.addDishesE(salesOrderBatchDishesE);
            } else {//微信
                if (dishesEList.get(position).getArrayOfDishesPracticeE() != null) {//有做法
                    List<DishesPracticeE> dishesPracticeEs = dishesPracticeMap.get(dishesEList.get(position).getDishesGUID());
                    salesOrderBatchDishesE.setOrderCount(0d);
                    callBackClick.addDishesEPractice(dishesPracticeEs, salesOrderBatchDishesE, dishesOrderCount);
                } else {
                    callBackClick.addDishesE(salesOrderBatchDishesE);
                }
            }
        });
        return convertView;
    }

    private void setIsStopSale(ViewHolder vh) {
        vh.dishesOperation.setVisibility(View.VISIBLE);
        vh.dishesOperation.setText("已售罄");
        vh.content.setEnabled(false);
        vh.dishesOperation.setBackgroundResource(R.drawable.shape_edit_no_choose_bg);
    }

    private SalesOrderBatchDishesE getSalesOrderBatchDishesE(int position) {
        DishesE dishesE = dishesEList.get(position);
        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
        salesOrderBatchDishesE.setDishesE(dishesE);

        salesOrderBatchDishesE.setCheckStatus(1);
        salesOrderBatchDishesE.setKitchenPrintStatus(1);
//        salesOrderBatchDishesE.setNew(true);
        salesOrderBatchDishesE.setDishesUnit(dishesE.getCheckUnit());
        salesOrderBatchDishesE.setGift(0);
        salesOrderBatchDishesE.setIsPackageDishes(dishesE.getIsPackageDishes());
        salesOrderBatchDishesE.setOrderCount(1.0);
        if (dishesE.getIsPackageDishes() == 1) {//套餐
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
        salesOrderBatchDishesE.setDishesGUID(dishesE.getDishesGUID());
        salesOrderBatchDishesE.setDishesName(dishesE.getSimpleName());
        return salesOrderBatchDishesE;
    }

//    /**
//     * 判断是否某菜品有添加到
//     *
//     * @param i  条目下标
//     * @param vh ViewHolder
//     */
//    private void isSelected(double i, ViewHolder vh) {
//        if (i == 0) {
//            vh.dishesCount.setVisibility(View.GONE);
//            vh.reduceDishes.setVisibility(View.GONE);
//        } else {
//            vh.dishesCount.setVisibility(View.VISIBLE);
//            if (i == (int) i) {
//                vh.dishesCount.setText((int) i + "");
//            } else {
//                vh.dishesCount.setText(context.getString(R.string.two_decimal, i));
//            }
//
//            vh.reduceDishes.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder hVH;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_change_order_dishes_title, null);
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

    private CallBackClick callBackClick;

    public interface CallBackClick {
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
    }

    public void setCallBackClick(CallBackClick callBackClick) {
        this.callBackClick = callBackClick;
    }

    private class HeaderViewHolder {
        public final TextView dishesTypeName;
        public final LinearLayout titleLyout;
        public final View root;

        public HeaderViewHolder(View root) {
            dishesTypeName = (TextView) root.findViewById(R.id.tv_order_dishes_title_name);
            titleLyout = (LinearLayout) root.findViewById(R.id.order_dishes_title_item_layout);
            this.root = root;
        }
    }

    public class ViewHolder {
        public final TextView dishesName;
        public final TextView dishesPrice;
        public final TextView dishesUnit;
        public final TextView dishesOperation;
        public final LinearLayout content;
        public final View topDivider;
        public final View root;

        public ViewHolder(View root) {
            dishesName = (TextView) root.findViewById(R.id.tv_order_dishes_name);
            dishesPrice = (TextView) root.findViewById(R.id.tv_order_dishes_price);
            dishesUnit = (TextView) root.findViewById(R.id.tv_order_dishes_price_unit);
            dishesOperation = (TextView) root.findViewById(R.id.tv_order_dishes_operation);
            topDivider = root.findViewById(R.id.layout_top_divider);
            content = root.findViewById(R.id.item_content);
            this.root = root;
        }
    }
}
