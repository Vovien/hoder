package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderDishes implements Parcelable {
    /**
     * 自增ID
     */
    private Integer UnOrderDishesID;

    /**
     * 编号
     */
    private String UnOrderDishesUID;

    /**
     * 标识Guid 主键
     */
    private String UnOrderDishesGUID;

    /**
     * 外卖订单Guid 外键
     */
    private String UnOrderGUID;

    /**
     * 菜品编码
     */
    private String DishesCode;

    /**
     * 菜品名称
     */
    private String DishesName;

    /**
     * 菜品SKU
     */
    private String DishesSku;

    /**
     * 菜品单位
     */
    private String DishesUnit;

    /**
     * 菜品数量
     */
    private Double DishesQuantity;

    /**
     * 菜品单价
     */
    private Double DishesPrice;

    /**
     * 菜品金额
     */
    private Double DishesSubTotal;

    /**
     * 餐盒数量
     */
    private Double BoxQuantity;

    /**
     * 餐盒单价
     */
    private Double BoxPrice;

    /**
     * 餐盒金额
     */
    private Double BoxSubTotal;

    /**
     * 折扣比例 1=无折扣 0.85=8.5折扣
     */
    private Double DishesDiscountRatio;

    /**
     * 折扣掉的单价
     */
    private Double DishesDiscountAmount;

    /**
     * 折扣掉的金额
     */
    private Double DishesDiscountTotal;

    /**
     * 规格 多个使用","分隔开
     */
    private String DishesSpecs;

    /**
     * 特殊属性  多个使用","分隔开
     */
    private String DishesProperty;

    /**
     * 分装 0=1号口袋  1=2号口袋  2=...
     */
    private Integer CartId;

    /**
     * 赠菜 0=普通消费菜品  1=赠送菜品
     */
    private Integer SettlementType;

    /**
     * 对应系统菜品的Guid
     */
    private String DefaultDishesGUID;

    /**
     * 确认后菜品Guid
     */
    private String ConfirmDishesGUID;

    public Integer getUnOrderDishesID() {
        return UnOrderDishesID;
    }

    public void setUnOrderDishesID(Integer unOrderDishesID) {
        UnOrderDishesID = unOrderDishesID;
    }

    public String getUnOrderDishesUID() {
        return UnOrderDishesUID;
    }

    public void setUnOrderDishesUID(String unOrderDishesUID) {
        UnOrderDishesUID = unOrderDishesUID;
    }

    public String getUnOrderDishesGUID() {
        return UnOrderDishesGUID;
    }

    public void setUnOrderDishesGUID(String unOrderDishesGUID) {
        UnOrderDishesGUID = unOrderDishesGUID;
    }

    public String getUnOrderGUID() {
        return UnOrderGUID;
    }

    public void setUnOrderGUID(String unOrderGUID) {
        UnOrderGUID = unOrderGUID;
    }

    public String getDishesCode() {
        return DishesCode;
    }

    public void setDishesCode(String dishesCode) {
        DishesCode = dishesCode;
    }

    public String getDishesName() {
        return DishesName;
    }

    public void setDishesName(String dishesName) {
        DishesName = dishesName;
    }

    public String getDishesSku() {
        return DishesSku;
    }

    public void setDishesSku(String dishesSku) {
        DishesSku = dishesSku;
    }

    public String getDishesUnit() {
        return DishesUnit;
    }

    public void setDishesUnit(String dishesUnit) {
        DishesUnit = dishesUnit;
    }

    public Double getDishesQuantity() {
        return DishesQuantity;
    }

    public void setDishesQuantity(Double dishesQuantity) {
        DishesQuantity = dishesQuantity;
    }

    public Double getDishesPrice() {
        return DishesPrice;
    }

    public void setDishesPrice(Double dishesPrice) {
        DishesPrice = dishesPrice;
    }

    public Double getDishesSubTotal() {
        return DishesSubTotal;
    }

    public void setDishesSubTotal(Double dishesSubTotal) {
        DishesSubTotal = dishesSubTotal;
    }

    public Double getBoxQuantity() {
        return BoxQuantity;
    }

    public void setBoxQuantity(Double boxQuantity) {
        BoxQuantity = boxQuantity;
    }

    public Double getBoxPrice() {
        return BoxPrice;
    }

    public void setBoxPrice(Double boxPrice) {
        BoxPrice = boxPrice;
    }

    public Double getBoxSubTotal() {
        return BoxSubTotal;
    }

    public void setBoxSubTotal(Double boxSubTotal) {
        BoxSubTotal = boxSubTotal;
    }

    public Double getDishesDiscountRatio() {
        return DishesDiscountRatio;
    }

    public void setDishesDiscountRatio(Double dishesDiscountRatio) {
        DishesDiscountRatio = dishesDiscountRatio;
    }

    public Double getDishesDiscountAmount() {
        return DishesDiscountAmount;
    }

    public void setDishesDiscountAmount(Double dishesDiscountAmount) {
        DishesDiscountAmount = dishesDiscountAmount;
    }

    public Double getDishesDiscountTotal() {
        return DishesDiscountTotal;
    }

    public void setDishesDiscountTotal(Double dishesDiscountTotal) {
        DishesDiscountTotal = dishesDiscountTotal;
    }

    public String getDishesSpecs() {
        return DishesSpecs;
    }

    public void setDishesSpecs(String dishesSpecs) {
        DishesSpecs = dishesSpecs;
    }

    public String getDishesProperty() {
        return DishesProperty;
    }

    public void setDishesProperty(String dishesProperty) {
        DishesProperty = dishesProperty;
    }

    public Integer getCartId() {
        return CartId;
    }

    public void setCartId(Integer cartId) {
        CartId = cartId;
    }

    public Integer getSettlementType() {
        return SettlementType;
    }

    public void setSettlementType(Integer settlementType) {
        SettlementType = settlementType;
    }

    public String getDefaultDishesGUID() {
        return DefaultDishesGUID;
    }

    public void setDefaultDishesGUID(String defaultDishesGUID) {
        DefaultDishesGUID = defaultDishesGUID;
    }

    public String getConfirmDishesGUID() {
        return ConfirmDishesGUID;
    }

    public void setConfirmDishesGUID(String confirmDishesGUID) {
        ConfirmDishesGUID = confirmDishesGUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.UnOrderDishesID);
        dest.writeString(this.UnOrderDishesUID);
        dest.writeString(this.UnOrderDishesGUID);
        dest.writeString(this.UnOrderGUID);
        dest.writeString(this.DishesCode);
        dest.writeString(this.DishesName);
        dest.writeString(this.DishesSku);
        dest.writeString(this.DishesUnit);
        dest.writeValue(this.DishesQuantity);
        dest.writeValue(this.DishesPrice);
        dest.writeValue(this.DishesSubTotal);
        dest.writeValue(this.BoxQuantity);
        dest.writeValue(this.BoxPrice);
        dest.writeValue(this.BoxSubTotal);
        dest.writeValue(this.DishesDiscountRatio);
        dest.writeValue(this.DishesDiscountAmount);
        dest.writeValue(this.DishesDiscountTotal);
        dest.writeString(this.DishesSpecs);
        dest.writeString(this.DishesProperty);
        dest.writeValue(this.CartId);
        dest.writeValue(this.SettlementType);
        dest.writeString(this.DefaultDishesGUID);
        dest.writeString(this.ConfirmDishesGUID);
    }

    public UnOrderDishes() {
    }

    protected UnOrderDishes(Parcel in) {
        this.UnOrderDishesID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UnOrderDishesUID = in.readString();
        this.UnOrderDishesGUID = in.readString();
        this.UnOrderGUID = in.readString();
        this.DishesCode = in.readString();
        this.DishesName = in.readString();
        this.DishesSku = in.readString();
        this.DishesUnit = in.readString();
        this.DishesQuantity = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesSubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.BoxQuantity = (Double) in.readValue(Double.class.getClassLoader());
        this.BoxPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.BoxSubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesDiscountRatio = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesDiscountAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesDiscountTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesSpecs = in.readString();
        this.DishesProperty = in.readString();
        this.CartId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SettlementType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DefaultDishesGUID = in.readString();
        this.ConfirmDishesGUID = in.readString();
    }

    public static final Parcelable.Creator<UnOrderDishes> CREATOR = new Parcelable.Creator<UnOrderDishes>() {
        @Override
        public UnOrderDishes createFromParcel(Parcel source) {
            return new UnOrderDishes(source);
        }

        @Override
        public UnOrderDishes[] newArray(int size) {
            return new UnOrderDishes[size];
        }
    };
}
