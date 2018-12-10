package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chencao on 2017/7/11.
 */

public class AddOrderDishesGroup implements Parcelable {
    /**
     * 该菜品点单数量
     */
    private Double count;
    /**
     * 是否赠送该菜品
     */
    private boolean isGive;
    /**
     * 该菜品点单的价格（不是单价 是该批次中该菜品价格）
     */
    private Double amount;
    /**
     * 该批次中的菜品
     */
    private DishesE dishesE;

    /**菜品记录*/
    private List<SalesOrderBatchDishesE> orderDishesRecordList ;

    /**结算菜品汇总数据*/
    private List<SalesOrderDishesE> salesOrderDishesEList ;

    /**
     * 对单数量
     */
    private Double ReviewCheckCount;
    /**
     * 折扣执行价格
     */
    private Double DiscountPrice;

    public List<SalesOrderBatchDishesE> getOrderDishesRecordList() {
        return orderDishesRecordList;
    }

    public void setOrderDishesRecordList(List<SalesOrderBatchDishesE> orderDishesRecordList) {
        this.orderDishesRecordList = orderDishesRecordList;
    }

    public List<SalesOrderDishesE> getSalesOrderDishesEList() {
        return salesOrderDishesEList;
    }

    public void setSalesOrderDishesEList(List<SalesOrderDishesE> salesOrderDishesEList) {
        this.salesOrderDishesEList = salesOrderDishesEList;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public boolean isGive() {
        return isGive;
    }

    public void setGive(boolean give) {
        isGive = give;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public DishesE getDishesE() {
        return dishesE;
    }

    public void setDishesE(DishesE dishesE) {
        this.dishesE = dishesE;
    }


    public AddOrderDishesGroup() {
}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.count);
        dest.writeByte(this.isGive ? (byte) 1 : (byte) 0);
        dest.writeValue(this.amount);
        dest.writeParcelable(this.dishesE,flags);
        dest.writeList(this.orderDishesRecordList);
        dest.writeTypedList(this.salesOrderDishesEList);
        dest.writeValue(this.ReviewCheckCount);
        dest.writeValue(this.DiscountPrice);
    }

    protected AddOrderDishesGroup(Parcel in) {
        this.count = (Double) in.readValue(Double.class.getClassLoader());
        this.isGive = in.readByte() != 0;
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.dishesE = in.readParcelable(DishesE.class.getClassLoader());
        this.orderDishesRecordList = new ArrayList<SalesOrderBatchDishesE>();
        in.readList(this.orderDishesRecordList, SalesOrderBatchDishesE.class.getClassLoader());
        this.salesOrderDishesEList = in.createTypedArrayList(SalesOrderDishesE.CREATOR);
        this.ReviewCheckCount = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountPrice = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<AddOrderDishesGroup> CREATOR = new Creator<AddOrderDishesGroup>() {
        @Override
        public AddOrderDishesGroup createFromParcel(Parcel source) {
            return new AddOrderDishesGroup(source);
        }

        @Override
        public AddOrderDishesGroup[] newArray(int size) {
            return new AddOrderDishesGroup[size];
        }
    };
}
