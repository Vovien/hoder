package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class OrderDishesGroup implements Parcelable {
    private String dishesGuid;
    private Double orderCount;
    private Double tempOrderCount;
    /**
     * 库存数量
     */
    private Double stockCount;

    private List<SalesOrderBatchDishesE> dishesList;


    /**
     * 标题
     */
    private String title;
    /**
     * 标题颜色
     */
    private Integer titleColorRes;
    /**
     * 标题图片
     */
    private Integer titleImageResource;

    /**
     * 状态(0:未生效 1：未出单:2：已出单 null 不显示)主要用于点菜记录第一条数据显示标题
     */
    private Integer status;

    private List<SalesOrderDishesE> SalesOrderDishesEList;

    public void setDishesList(ArrayList<SalesOrderBatchDishesE> dishesList) {
        this.dishesList = dishesList;
    }


    public void addDishes(SalesOrderBatchDishesE dishes) {
        if (dishesList == null) {
            dishesList = new ArrayList();
            dishesGuid = dishes.getDishesGUID();
        }
        if (!dishesList.contains(dishes)) {
            dishesList.add(dishes);
        }
        refreshOrderCount();
    }

    public void refreshData() {
        refreshOrderCount();
        refreshMaxOrderValue();
    }

    private void refreshOrderCount() {
        orderCount = 0d;
        if (CollectionUtils.isNotEmpty(dishesList)) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : dishesList) {
                orderCount = ArithUtil.add(orderCount, salesOrderBatchDishesE.getOrderCount());
            }
        }
    }

    public void refreshTempMaxValue() {
        Double tempStockCount = this.stockCount;
        if (tempStockCount != null) {
            for (SalesOrderBatchDishesE dishes : getDishesList()) {
                if (tempStockCount > getTempOrderCount()) {
                    dishes.setTempMaxValue(ArithUtil.add(dishes.getTempOrderCount(), getTempResidueCount()));
                } else {
                    if (tempStockCount >= dishes.getTempOrderCount()) {
                        dishes.setTempMaxValue(dishes.getTempOrderCount());
                        tempStockCount = ArithUtil.sub(tempStockCount, dishes.getTempOrderCount());
                    } else if (tempStockCount > 0) {
                        dishes.setTempMaxValue(tempStockCount);
                        tempStockCount = 0d;
                    } else {
                        dishes.setTempMaxValue(0d);
                    }
                }
            }
        }
    }

    public void refreshMaxOrderValue() {
        if (CollectionUtils.isNotEmpty(dishesList)) {
            Double tempStockCount = stockCount;
            for (SalesOrderBatchDishesE dishes : dishesList) {
                if (stockCount == null) {
                    dishes.setMaxValue(null);
                } else {
                    if (tempStockCount >= orderCount) {
                        dishes.setMaxValue(ArithUtil.add(dishes.getOrderCount(), getResidueCount()));
                    } else {
                        if (tempStockCount >= dishes.getOrderCount()) {
                            dishes.setMaxValue(dishes.getOrderCount());
                            tempStockCount = ArithUtil.sub(tempStockCount, dishes.getOrderCount());
                        } else if (tempStockCount > 0) {
                            dishes.setMaxValue(tempStockCount);
                            tempStockCount = 0d;
                        } else {
                            dishes.setMaxValue(0d);
                        }
                    }
                }
            }
        }
    }

    public void refreshTempOrderCount() {
        tempOrderCount = 0d;
        if (dishesList != null) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : dishesList) {
                tempOrderCount = ArithUtil.add(tempOrderCount, salesOrderBatchDishesE.getTempOrderCount());
            }
        }
    }

    public OrderDishesGroup() {
    }

    public Double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Double orderCount) {
        this.orderCount = orderCount;
    }

    public String getDishesGuid() {
        return dishesGuid;
    }

    public List<SalesOrderBatchDishesE> getDishesList() {
        return dishesList;
    }

    public List<SalesOrderBatchDishesE> getOrderDishesRecordList() {
        return dishesList;
    }

    public void setOrderDishesRecordList(List<SalesOrderBatchDishesE> orderDishesRecordList) {
        this.dishesList = orderDishesRecordList;
    }

    public Double getStockCount() {
        return stockCount;
    }

    public void setStockCount(Double stockCount) {
//        if ((this.stockCount == null && stockCount != null) || (this.stockCount != null && stockCount == null)
//                || (this.stockCount != null && stockCount != null && (!this.stockCount.equals(stockCount)))) {
        this.stockCount = stockCount;
        refreshData();
//        }
    }

    /**
     * 获取剩余数量
     *
     * @return
     */
    public Double getResidueCount() {
        if (stockCount == null) {
            return null;
        } else {
            return ArithUtil.sub(stockCount, orderCount);
        }
    }

    /**
     * 获取临时剩余数量
     *
     * @return
     */
    public Double getTempResidueCount() {
        if (stockCount == null || tempOrderCount == null) {
            return null;
        } else {
            return ArithUtil.sub(stockCount, tempOrderCount);
        }
    }

    public void setDishesGuid(String dishesGuid) {
        this.dishesGuid = dishesGuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleColorRes() {
        return titleColorRes;
    }

    public void setTitleColorRes(Integer titleColorRes) {
        this.titleColorRes = titleColorRes;
    }

    public Integer getTitleImageResource() {
        return titleImageResource;
    }

    public void setTitleImageResource(Integer titleImageResource) {
        this.titleImageResource = titleImageResource;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SalesOrderDishesE> getSalesOrderDishesEList() {
        return SalesOrderDishesEList;
    }

    public void setSalesOrderDishesEList(List<SalesOrderDishesE> salesOrderDishesEList) {
        SalesOrderDishesEList = salesOrderDishesEList;
    }

    public Double getTempOrderCount() {
        return tempOrderCount;
    }

    public void setTempOrderCount(Double tempOrderCount) {
        this.tempOrderCount = tempOrderCount;
    }

    public void removeDishesList(List<SalesOrderBatchDishesE> list) {
        if (dishesList != null) {
            dishesList.removeAll(list);
//            refreshData();
        }
    }

    public void removeDishes(SalesOrderBatchDishesE dishes) {
        if (dishesList != null) {
            dishesList.remove(dishes);
//            refreshData();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dishesGuid);
        dest.writeValue(this.orderCount);
        dest.writeValue(this.tempOrderCount);
        dest.writeValue(this.stockCount);
        dest.writeTypedList(this.dishesList);
        dest.writeString(this.title);
        dest.writeValue(this.titleColorRes);
        dest.writeValue(this.titleImageResource);
        dest.writeValue(this.status);
        dest.writeTypedList(this.SalesOrderDishesEList);
    }

    protected OrderDishesGroup(Parcel in) {
        this.dishesGuid = in.readString();
        this.orderCount = (Double) in.readValue(Double.class.getClassLoader());
        this.tempOrderCount = (Double) in.readValue(Double.class.getClassLoader());
        this.stockCount = (Double) in.readValue(Double.class.getClassLoader());
        this.dishesList = in.createTypedArrayList(SalesOrderBatchDishesE.CREATOR);
        this.title = in.readString();
        this.titleColorRes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.titleImageResource = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrderDishesEList = in.createTypedArrayList(SalesOrderDishesE.CREATOR);
    }

    public static final Creator<OrderDishesGroup> CREATOR = new Creator<OrderDishesGroup>() {
        @Override
        public OrderDishesGroup createFromParcel(Parcel source) {
            return new OrderDishesGroup(source);
        }

        @Override
        public OrderDishesGroup[] newArray(int size) {
            return new OrderDishesGroup[size];
        }
    };
}
