package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 自选套餐，套餐明细分组
 */
public class PackageGroup implements Parcelable {
    /**
     * 自增ID
     */
    private Integer PackageGroupID;

    /**
     * 主键
     */
    private String PackageGroupGUID;

    /**
     * 套餐标识
     */
    private String DishesGUID;

    /**
     * 名称
     */
    private String Name;

    /**
     * 可选数量
     */
    private Integer OptionalCount;

    /**
     * 分组明细集合
     */
    private List<PackageGroupDishesE> ArrayOfPackageGroupDishesE;
    /**
     * 当前已选的子项数量
     */
    private double SelectedCount;

    public double getSelectedCount() {
        return SelectedCount;
    }

    public void setSelectedCount(double selectedCount) {
        SelectedCount = selectedCount;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getOptionalCount() {
        return OptionalCount;
    }

    public void setOptionalCount(Integer optionalCount) {
        OptionalCount = optionalCount;
    }

    public List<PackageGroupDishesE> getArrayOfPackageGroupDishesE() {
        return ArrayOfPackageGroupDishesE;
    }

    public void setArrayOfPackageGroupDishesE(List<PackageGroupDishesE> arrayOfPackageGroupDishesE) {
        ArrayOfPackageGroupDishesE = arrayOfPackageGroupDishesE;
    }

    public Integer getPackageGroupID() {
        return PackageGroupID;
    }

    public void setPackageGroupID(Integer packageGroupID) {
        PackageGroupID = packageGroupID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.PackageGroupID);
        dest.writeString(this.PackageGroupGUID);
        dest.writeString(this.DishesGUID);
        dest.writeString(this.Name);
        dest.writeValue(this.OptionalCount);
        dest.writeTypedList(this.ArrayOfPackageGroupDishesE);
        dest.writeDouble(this.SelectedCount);
    }

    public PackageGroup() {
    }

    protected PackageGroup(Parcel in) {
        this.PackageGroupID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PackageGroupGUID = in.readString();
        this.DishesGUID = in.readString();
        this.Name = in.readString();
        this.OptionalCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ArrayOfPackageGroupDishesE = in.createTypedArrayList(PackageGroupDishesE.CREATOR);
        this.SelectedCount = in.readDouble();
    }

    public static final Parcelable.Creator<PackageGroup> CREATOR = new Parcelable.Creator<PackageGroup>() {
        @Override
        public PackageGroup createFromParcel(Parcel source) {
            return new PackageGroup(source);
        }

        @Override
        public PackageGroup[] newArray(int size) {
            return new PackageGroup[size];
        }
    };
}
