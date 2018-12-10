package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/29.
 */

public class SalesOrderDishesE extends SalesOrderDishes implements Parcelable {

    private DishesE DishesE;

    public Double getPracticeSubTotal() {
        return PracticeSubTotal;
    }


    public void setPracticeSubTotal(Double practiceSubTotal) {
        PracticeSubTotal = practiceSubTotal;
    }

    /**
     * 做法金额单价
     */
    private Double PracticeSubTotal;
    /**
     * 做法
     */
    private String PracticeNames;
    /**
     * 自菜品
     */
    private String SubDishes;

    /**
     * 菜品简称
     */
    private String SimpleName;

    private String Name;

    /**
     * 是否显示会员价
     * 0=不显示  1=显示
     */
    private Integer IsUseMemberPrice;

    /**
     * 不含做法的菜品总价
     */
    private Double DishesTotal;

    public Double getDishesTotal() {
        return DishesTotal;
    }

    public void setDishesTotal(Double dishesTotal) {
        DishesTotal = dishesTotal;
    }

    public Integer getIsUseMemberPrice() {
        return IsUseMemberPrice;
    }

    public void setIsUseMemberPrice(Integer isUseMemberPrice) {
        IsUseMemberPrice = isUseMemberPrice;
    }

    public String getSimpleName() {
        return SimpleName;
    }

    public void setSimpleName(String simpleName) {
        SimpleName = simpleName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPracticeNames() {
        return PracticeNames;
    }

    public void setPracticeNames(String practiceNames) {
        PracticeNames = practiceNames;
    }

    public String getSubDishes() {
        return SubDishes;
    }

    public void setSubDishes(String subDishes) {
        SubDishes = subDishes;
    }

    public SalesOrderDishesE() {
    }

    public DishesE getDishesE() {
        return DishesE;
    }

    public void setDishesE(DishesE dishesE) {
        DishesE = dishesE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.DishesE, flags);
        dest.writeValue(this.PracticeSubTotal);
        dest.writeString(this.PracticeNames);
        dest.writeString(this.SubDishes);
        dest.writeString(this.SimpleName);
        dest.writeString(this.Name);
        dest.writeValue(this.IsUseMemberPrice);
        dest.writeValue(this.DishesTotal);
    }

    protected SalesOrderDishesE(Parcel in) {
        super(in);
        this.DishesE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DishesE.class.getClassLoader());
        this.PracticeSubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.PracticeNames = in.readString();
        this.SubDishes = in.readString();
        this.SimpleName = in.readString();
        this.Name = in.readString();
        this.IsUseMemberPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DishesTotal = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<SalesOrderDishesE> CREATOR = new Creator<SalesOrderDishesE>() {
        @Override
        public SalesOrderDishesE createFromParcel(Parcel source) {
            return new SalesOrderDishesE(source);
        }

        @Override
        public SalesOrderDishesE[] newArray(int size) {
            return new SalesOrderDishesE[size];
        }
    };
}
