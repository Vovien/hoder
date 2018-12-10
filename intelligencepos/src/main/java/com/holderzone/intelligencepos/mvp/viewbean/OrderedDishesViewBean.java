package com.holderzone.intelligencepos.mvp.viewbean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * 已点菜品ViewBean
 * Created by zhaoping on 2018/9/10.
 */
public class OrderedDishesViewBean implements Parcelable {
    /**
     * 子菜信息
     */
    private String subDishesString;
    private String dishesName;
    private Integer groupTitleImgResId;
    private String groupTitleName;
    private int groupTitleColorResId;
    private String dishesTagName;
    private int dishesTagColorResId;
    private Double price;
    private Double count;
    private Double servingCount;
    private String cookMethod;
    private Double cookMethodPrice;
    private boolean showTopSpace;
    private boolean goneDivider;

    public boolean isShowTopSpace() {
        return showTopSpace;
    }

    public void setShowTopSpace(boolean showTopSpace) {
        this.showTopSpace = showTopSpace;
    }

    public Double getServingCount() {
        return servingCount;
    }

    public void setServingCount(Double servingCount) {
        this.servingCount = servingCount;
    }

    public String getSubDishesString() {
        return subDishesString;
    }

    public void setSubDishesString(String subDishesString) {
        this.subDishesString = subDishesString;
    }

    public int getGroupTitleColorResId() {
        return groupTitleColorResId;
    }

    public void setGroupTitleColorResId(int groupTitleColorResId) {
        this.groupTitleColorResId = groupTitleColorResId;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public Integer getGroupTitleImgResId() {
        return groupTitleImgResId;
    }

    public void setGroupTitleImgResId(@DrawableRes Integer groupTitleImgResId) {
        this.groupTitleImgResId = groupTitleImgResId;
    }

    public boolean isGoneDivider() {
        return goneDivider;
    }

    public void setGoneDivider(boolean goneDivider) {
        this.goneDivider = goneDivider;
    }

    public String getDishesTagName() {
        return dishesTagName;
    }

    public void setDishesTagName(String dishesTagName) {
        this.dishesTagName = dishesTagName;
    }

    public int getDishesTagColorResId() {
        return dishesTagColorResId;
    }

    public void setDishesTagColorResId(int dishesTagColorResId) {
        this.dishesTagColorResId = dishesTagColorResId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getCookMethod() {
        return cookMethod;
    }

    public void setCookMethod(String cookMethod) {
        this.cookMethod = cookMethod;
    }

    public Double getCookMethodPrice() {
        return cookMethodPrice;
    }

    public void setCookMethodPrice(Double cookMethodPrice) {
        this.cookMethodPrice = cookMethodPrice;
    }

    public String getGroupTitleName() {
        return groupTitleName;
    }

    public void setGroupTitleName(String groupTitleName) {
        this.groupTitleName = groupTitleName;
    }

    public OrderedDishesViewBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subDishesString);
        dest.writeString(this.dishesName);
        dest.writeValue(this.groupTitleImgResId);
        dest.writeString(this.groupTitleName);
        dest.writeInt(this.groupTitleColorResId);
        dest.writeString(this.dishesTagName);
        dest.writeInt(this.dishesTagColorResId);
        dest.writeValue(this.price);
        dest.writeValue(this.count);
        dest.writeValue(this.servingCount);
        dest.writeString(this.cookMethod);
        dest.writeValue(this.cookMethodPrice);
        dest.writeByte(this.showTopSpace ? (byte) 1 : (byte) 0);
        dest.writeByte(this.goneDivider ? (byte) 1 : (byte) 0);
    }

    protected OrderedDishesViewBean(Parcel in) {
        this.subDishesString = in.readString();
        this.dishesName = in.readString();
        this.groupTitleImgResId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.groupTitleName = in.readString();
        this.groupTitleColorResId = in.readInt();
        this.dishesTagName = in.readString();
        this.dishesTagColorResId = in.readInt();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.count = (Double) in.readValue(Double.class.getClassLoader());
        this.servingCount = (Double) in.readValue(Double.class.getClassLoader());
        this.cookMethod = in.readString();
        this.cookMethodPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.showTopSpace = in.readByte() != 0;
        this.goneDivider = in.readByte() != 0;
    }

    public static final Creator<OrderedDishesViewBean> CREATOR = new Creator<OrderedDishesViewBean>() {
        @Override
        public OrderedDishesViewBean createFromParcel(Parcel source) {
            return new OrderedDishesViewBean(source);
        }

        @Override
        public OrderedDishesViewBean[] newArray(int size) {
            return new OrderedDishesViewBean[size];
        }
    };
}
