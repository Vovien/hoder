package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class DishesE extends Dishes {

    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 品种类型
     */
    private DishesTypeE DishesTypeE;

    /**
     * 该菜品的打印机标识
     */
    private String PrinterGUID;

    /**
     * 该套餐子项的菜品标识
     */
    private String SubDishesGUID;

    /**
     * 该套餐子项的菜品实体
     */
    private DishesE DishesE;

    /**
     * 是否停售
     */
    private boolean isStopSale;

    /**
     * 做法标识
     */
    private String DishesPracticeGUID;

    /**
     * 做法费用单价
     */
    private Double Fees;

    /**
     * 该DisehsE是套餐时，子项菜品集合的
     */
    private List<PackageItemE> ArrayOfPackageItemE;
    /**
     * 菜品类别名称
     */
    private String DishesTypeName;
    /**
     * 是否已估清：已无菜品
     * 0:否
     * 1:是
     */
    private Integer IsEstimate;
    /**
     * 套餐类型 0：固定套餐 1：自选套餐
     */
    private int PackageType;
    /**
     * 自选套餐分组
     */
    private List<PackageGroup> ArrayOfPackageGroup;
    /**
     * 备注
     */
    private String Remark;
    /**
     * 做法
     */
    private String PracticeNames;
    /**
     * 自菜品
     */
    private String SubDishes;
    /**
     * 否启用称重
     * 0=未启用
     * 1=启用
     */
    private Integer WeighEnable;

    public Integer getWeighEnable() {
        return WeighEnable;
    }

    public void setWeighEnable(Integer weighEnable) {
        WeighEnable = weighEnable;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public int getPackageType() {
        return PackageType;
    }

    public void setPackageType(int packageType) {
        PackageType = packageType;
    }

    public List<PackageGroup> getArrayOfPackageGroup() {
        return ArrayOfPackageGroup;
    }

    public void setArrayOfPackageGroup(List<PackageGroup> arrayOfPackageGroup) {
        ArrayOfPackageGroup = arrayOfPackageGroup;
    }

    public Integer getIsEstimate() {
        return IsEstimate;
    }

    public void setIsEstimate(Integer isEstimate) {
        IsEstimate = isEstimate;
    }

    public String getDishesTypeName() {
        return DishesTypeName;
    }

    public void setDishesTypeName(String dishesTypeName) {
        DishesTypeName = dishesTypeName;
    }

    public List<DishesPracticeBindE> getArrayOfDishesPracticeBindE() {
        return ArrayOfDishesPracticeBindE;
    }

    public List<DishesPracticeE> getArrayOfDishesPracticeE() {
        List<DishesPracticeE> list = null;
        if (ArrayOfDishesPracticeBindE != null && !ArrayOfDishesPracticeBindE.isEmpty()) {
            list = new ArrayList<>();
            for (DishesPracticeBindE dishesPracticeBindE : ArrayOfDishesPracticeBindE) {
                list.add(dishesPracticeBindE.getDishesPracticeE());
            }
        }
        return list;
    }

    public void setArrayOfDishesPracticeBindE(List<DishesPracticeBindE> arrayOfDishesPracticeBindE) {
        ArrayOfDishesPracticeBindE = arrayOfDishesPracticeBindE;
    }

    private List<DishesPracticeBindE> ArrayOfDishesPracticeBindE;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public boolean isStopSale() {
        return isStopSale;
    }

    public void setStopSale(boolean stopSale) {
        isStopSale = stopSale;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public DishesTypeE getDishesTypeE() {
        return DishesTypeE;
    }

    public void setDishesTypeE(DishesTypeE dishesTypeE) {
        DishesTypeE = dishesTypeE;
    }

    public String getPrinterGUID() {
        return PrinterGUID;
    }

    public void setPrinterGUID(String printerGUID) {
        PrinterGUID = printerGUID;
    }

    public String getSubDishesGUID() {
        return SubDishesGUID;
    }

    public void setSubDishesGUID(String subDishesGUID) {
        SubDishesGUID = subDishesGUID;
    }

    public DishesE getDishesE() {
        return DishesE;
    }

    public void setDishesE(DishesE dishesE) {
        DishesE = dishesE;
    }

    public String getDishesPracticeGUID() {
        return DishesPracticeGUID;
    }

    public void setDishesPracticeGUID(String dishesPracticeGUID) {
        DishesPracticeGUID = dishesPracticeGUID;
    }

    public Double getFees() {
        return Fees;
    }

    public void setFees(Double fees) {
        Fees = fees;
    }

    public List<PackageItemE> getArrayOfPackageItemE() {
        return ArrayOfPackageItemE;
    }

    public void setArrayOfPackageItemE(List<PackageItemE> arrayOfPackageItemE) {
        ArrayOfPackageItemE = arrayOfPackageItemE;
    }

    public DishesE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeSerializable(this.DishesTypeE);
        dest.writeString(this.PrinterGUID);
        dest.writeString(this.SubDishesGUID);
        dest.writeParcelable(this.DishesE, flags);
        dest.writeByte(this.isStopSale ? (byte) 1 : (byte) 0);
        dest.writeString(this.DishesPracticeGUID);
        dest.writeValue(this.Fees);
        dest.writeTypedList(this.ArrayOfPackageItemE);
        dest.writeString(this.DishesTypeName);
        dest.writeValue(this.IsEstimate);
        dest.writeInt(this.PackageType);
        dest.writeTypedList(this.ArrayOfPackageGroup);
        dest.writeString(this.Remark);
        dest.writeString(this.PracticeNames);
        dest.writeString(this.SubDishes);
        dest.writeValue(this.WeighEnable);
        dest.writeTypedList(this.ArrayOfDishesPracticeBindE);
    }

    protected DishesE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.DishesTypeE = (com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE) in.readSerializable();
        this.PrinterGUID = in.readString();
        this.SubDishesGUID = in.readString();
        this.DishesE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DishesE.class.getClassLoader());
        this.isStopSale = in.readByte() != 0;
        this.DishesPracticeGUID = in.readString();
        this.Fees = (Double) in.readValue(Double.class.getClassLoader());
        this.ArrayOfPackageItemE = in.createTypedArrayList(PackageItemE.CREATOR);
        this.DishesTypeName = in.readString();
        this.IsEstimate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PackageType = in.readInt();
        this.ArrayOfPackageGroup = in.createTypedArrayList(PackageGroup.CREATOR);
        this.Remark = in.readString();
        this.PracticeNames = in.readString();
        this.SubDishes = in.readString();
        this.WeighEnable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ArrayOfDishesPracticeBindE = in.createTypedArrayList(DishesPracticeBindE.CREATOR);
    }

    public static final Creator<DishesE> CREATOR = new Creator<DishesE>() {
        @Override
        public DishesE createFromParcel(Parcel source) {
            return new DishesE(source);
        }

        @Override
        public DishesE[] newArray(int size) {
            return new DishesE[size];
        }
    };
}
