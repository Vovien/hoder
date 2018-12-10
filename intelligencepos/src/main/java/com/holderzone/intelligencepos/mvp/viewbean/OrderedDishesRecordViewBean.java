package com.holderzone.intelligencepos.mvp.viewbean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;

/**
 * 已点菜品ViewBean
 * Created by zhaoping on 2018/9/10.
 */
public class OrderedDishesRecordViewBean implements Parcelable {
    /**
     * 子菜信息
     */
    private String subDishesString;
    private boolean showTitle;
    private String operateTime;
    private String operatePrice;
    private int titleColorResId;
    private String dishesTagName;
    private int dishesTagColorResId;
    private String dishesName;
    private String price;
    private String count;
    private String cookMethod;
    private String cookMethodPrice;
    private String returnReason;
    private boolean showBottomSpace;
    private boolean showTopSpace;
    private boolean showDivider;
    private boolean isReturnDishes;
    private int dishesStatusResId;

    public boolean isShowTopSpace() {
        return showTopSpace;
    }

    public void setShowTopSpace(boolean showTopSpace) {
        this.showTopSpace = showTopSpace;
    }

    public int getDishesStatusResId() {
        return dishesStatusResId;
    }

    public void setDishesStatusResId(int dishesStatusResId) {
        this.dishesStatusResId = dishesStatusResId;
    }

    public boolean isReturnDishes() {
        return isReturnDishes;
    }

    public void setReturnDishes(boolean returnDishes) {
        isReturnDishes = returnDishes;
    }

    public boolean isShowDivider() {
        return showDivider;
    }

    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
    }

    public String getSubDishesString() {
        return subDishesString == null ? "" : subDishesString;
    }

    public void setSubDishesString(String subDishesString) {
        this.subDishesString = subDishesString;
    }

    public String getCookMethodPrice() {
        return cookMethodPrice == null ? "" : cookMethodPrice;
    }

    public void setCookMethodPrice(String cookMethodPrice) {
        this.cookMethodPrice = cookMethodPrice;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperatePrice() {
        return operatePrice;
    }

    public void setOperatePrice(String operatePrice) {
        this.operatePrice = operatePrice;
    }

    public int getTitleColorResId() {
        return titleColorResId;
    }

    public void setTitleColorResId(int titleColorResId) {
        this.titleColorResId = titleColorResId;
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

    public void setDishesTagColorResId(@ColorRes int dishesTagColorResId) {
        this.dishesTagColorResId = dishesTagColorResId;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCookMethod() {
        return cookMethod;
    }

    public void setCookMethod(String cookMethod) {
        this.cookMethod = cookMethod;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public boolean isShowBottomSpace() {
        return showBottomSpace;
    }

    public void setShowBottomSpace(boolean showBottomSpace) {
        this.showBottomSpace = showBottomSpace;
    }

    public OrderedDishesRecordViewBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subDishesString);
        dest.writeByte(this.showTitle ? (byte) 1 : (byte) 0);
        dest.writeString(this.operateTime);
        dest.writeString(this.operatePrice);
        dest.writeInt(this.titleColorResId);
        dest.writeString(this.dishesTagName);
        dest.writeInt(this.dishesTagColorResId);
        dest.writeString(this.dishesName);
        dest.writeString(this.price);
        dest.writeString(this.count);
        dest.writeString(this.cookMethod);
        dest.writeString(this.cookMethodPrice);
        dest.writeString(this.returnReason);
        dest.writeByte(this.showBottomSpace ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showTopSpace ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showDivider ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isReturnDishes ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dishesStatusResId);
    }

    protected OrderedDishesRecordViewBean(Parcel in) {
        this.subDishesString = in.readString();
        this.showTitle = in.readByte() != 0;
        this.operateTime = in.readString();
        this.operatePrice = in.readString();
        this.titleColorResId = in.readInt();
        this.dishesTagName = in.readString();
        this.dishesTagColorResId = in.readInt();
        this.dishesName = in.readString();
        this.price = in.readString();
        this.count = in.readString();
        this.cookMethod = in.readString();
        this.cookMethodPrice = in.readString();
        this.returnReason = in.readString();
        this.showBottomSpace = in.readByte() != 0;
        this.showTopSpace = in.readByte() != 0;
        this.showDivider = in.readByte() != 0;
        this.isReturnDishes = in.readByte() != 0;
        this.dishesStatusResId = in.readInt();
    }

    public static final Creator<OrderedDishesRecordViewBean> CREATOR = new Creator<OrderedDishesRecordViewBean>() {
        @Override
        public OrderedDishesRecordViewBean createFromParcel(Parcel source) {
            return new OrderedDishesRecordViewBean(source);
        }

        @Override
        public OrderedDishesRecordViewBean[] newArray(int size) {
            return new OrderedDishesRecordViewBean[size];
        }
    };
}
