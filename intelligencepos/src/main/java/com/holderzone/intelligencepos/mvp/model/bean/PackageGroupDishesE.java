package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 自选套餐分组明细
 */
public class PackageGroupDishesE implements Parcelable {
    /**
     * 自增ID
     */
    private Integer PackageGroupDishesID;

    /**
     * 主键
     */
    private String PackageGroupDishesGUID;

    /**
     * 品种类型标识
     */
    private String PackageGroupGUID;

    /**
     * 套餐下属菜品标识
     */
    private String DishesGUID;

    /**
     * 单次数量
     */
    private Double SingleCount;

    /**
     * 浮动价
     */
    private Double RiseAmount;
    /**
     * 菜品名称
     */
    private String Name;
    /**
     * 当前的点菜数量
     */
    private int PackageDishesOrderCount;
    /**
     * 结算单位
     */
    private String CheckUnit;
    /**
     * 简称
     */
    private String SimpleName;

    public String getSimpleName() {
        return SimpleName;
    }

    public void setSimpleName(String simpleName) {
        SimpleName = simpleName;
    }

    public String getCheckUnit() {
        return CheckUnit;
    }

    public void setCheckUnit(String checkUnit) {
        CheckUnit = checkUnit;
    }

    public int getPackageDishesOrderCount() {
        return PackageDishesOrderCount;
    }

    public void setPackageDishesOrderCount(int packageDishesOrderCount) {
        PackageDishesOrderCount = packageDishesOrderCount;
    }

    public Integer getPackageGroupDishesID() {
        return PackageGroupDishesID;
    }

    public void setPackageGroupDishesID(Integer packageGroupDishesID) {
        PackageGroupDishesID = packageGroupDishesID;
    }

    public String getPackageGroupDishesGUID() {
        return PackageGroupDishesGUID;
    }

    public void setPackageGroupDishesGUID(String packageGroupDishesGUID) {
        PackageGroupDishesGUID = packageGroupDishesGUID;
    }

    public String getPackageGroupGUID() {
        return PackageGroupGUID;
    }

    public void setPackageGroupGUID(String packageGroupGUID) {
        PackageGroupGUID = packageGroupGUID;
    }

    public String getDishesGUID() {
        return DishesGUID;
    }

    public void setDishesGUID(String dishesGUID) {
        DishesGUID = dishesGUID;
    }

    public Double getSingleCount() {
        return SingleCount;
    }

    public void setSingleCount(Double singleCount) {
        SingleCount = singleCount;
    }

    public Double getRiseAmount() {
        return RiseAmount;
    }

    public void setRiseAmount(Double riseAmount) {
        RiseAmount = riseAmount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.PackageGroupDishesID);
        dest.writeString(this.PackageGroupDishesGUID);
        dest.writeString(this.PackageGroupGUID);
        dest.writeString(this.DishesGUID);
        dest.writeValue(this.SingleCount);
        dest.writeValue(this.RiseAmount);
        dest.writeString(this.Name);
        dest.writeInt(this.PackageDishesOrderCount);
        dest.writeString(this.CheckUnit);
        dest.writeString(this.SimpleName);
    }

    public PackageGroupDishesE() {
    }

    protected PackageGroupDishesE(Parcel in) {
        this.PackageGroupDishesID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PackageGroupDishesGUID = in.readString();
        this.PackageGroupGUID = in.readString();
        this.DishesGUID = in.readString();
        this.SingleCount = (Double) in.readValue(Double.class.getClassLoader());
        this.RiseAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.Name = in.readString();
        this.PackageDishesOrderCount = in.readInt();
        this.CheckUnit = in.readString();
        this.SimpleName = in.readString();
    }

    public static final Parcelable.Creator<PackageGroupDishesE> CREATOR = new Parcelable.Creator<PackageGroupDishesE>() {
        @Override
        public PackageGroupDishesE createFromParcel(Parcel source) {
            return new PackageGroupDishesE(source);
        }

        @Override
        public PackageGroupDishesE[] newArray(int size) {
            return new PackageGroupDishesE[size];
        }
    };
}
