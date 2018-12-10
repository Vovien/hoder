package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 划菜实体
 * Created by chencao on 2018/1/9.
 */

public class SalesOrderServingDishesE implements Parcelable {
    /**
     * 企业Guid
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店Guid
     */
    private String StoreGUID;
    /**
     * 桌台Guid
     */
    private String DiningTableGUID;
    /**
     * 订单GUID
     */
    private String SalesOrderGUID;
    /**
     * 流水
     */
    private String SerialNumber;
    /**
     * 客人数量
     */
    private Integer GuestCount;
    /**
     * 菜品编码
     */
    private String DishesCode;
    /**
     * 菜品名称
     */
    private String DishesName;
    /**
     * 划菜数量
     */
    private Double ServingCount;
    /**
     * 划菜时间
     */
    private Double ServingDateTime;
    /**
     * 操作人Guid
     */
    private String OperaStaffGUID;
    /**
     * 操作人
     */
    private String OperaStaffName;
    /**
     * 划菜Guid
     */
    private String SalesOrderServingDishesGUID;

    private String SalesOrderBatchDishesGUID;
    /**
     * 返回状态
     * 0：未通过  1：通过
     */
    private Integer CheckState;

    public Integer getCheckState() {
        return CheckState;
    }

    public void setCheckState(Integer checkState) {
        CheckState = checkState;
    }

    public String getSalesOrderBatchDishesGUID() {
        return SalesOrderBatchDishesGUID;
    }

    public void setSalesOrderBatchDishesGUID(String salesOrderBatchDishesGUID) {
        SalesOrderBatchDishesGUID = salesOrderBatchDishesGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGuid) {
        SalesOrderGUID = salesOrderGuid;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public Integer getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(Integer guestCount) {
        GuestCount = guestCount;
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

    public Double getServingCount() {
        return ServingCount;
    }

    public void setServingCount(Double servingCount) {
        ServingCount = servingCount;
    }

    public Double getServingDateTime() {
        return ServingDateTime;
    }

    public void setServingDateTime(Double servingDateTime) {
        ServingDateTime = servingDateTime;
    }

    public String getOperaStaffGUID() {
        return OperaStaffGUID;
    }

    public void setOperaStaffGUID(String operaStaffGUID) {
        OperaStaffGUID = operaStaffGUID;
    }

    public String getOperaStaffName() {
        return OperaStaffName;
    }

    public void setOperaStaffName(String operaStaffName) {
        OperaStaffName = operaStaffName;
    }

    public String getSalesOrderServingDishesGUID() {
        return SalesOrderServingDishesGUID;
    }

    public void setSalesOrderServingDishesGUID(String salesOrderServingDishesGUID) {
        SalesOrderServingDishesGUID = salesOrderServingDishesGUID;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
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

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }


    public SalesOrderServingDishesE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.DiningTableGUID);
        dest.writeString(this.SalesOrderGUID);
        dest.writeString(this.SerialNumber);
        dest.writeValue(this.GuestCount);
        dest.writeString(this.DishesCode);
        dest.writeString(this.DishesName);
        dest.writeValue(this.ServingCount);
        dest.writeValue(this.ServingDateTime);
        dest.writeString(this.OperaStaffGUID);
        dest.writeString(this.OperaStaffName);
        dest.writeString(this.SalesOrderServingDishesGUID);
        dest.writeString(this.SalesOrderBatchDishesGUID);
        dest.writeValue(this.CheckState);
    }

    protected SalesOrderServingDishesE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.DiningTableGUID = in.readString();
        this.SalesOrderGUID = in.readString();
        this.SerialNumber = in.readString();
        this.GuestCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DishesCode = in.readString();
        this.DishesName = in.readString();
        this.ServingCount = (Double) in.readValue(Double.class.getClassLoader());
        this.ServingDateTime = (Double) in.readValue(Double.class.getClassLoader());
        this.OperaStaffGUID = in.readString();
        this.OperaStaffName = in.readString();
        this.SalesOrderServingDishesGUID = in.readString();
        this.SalesOrderBatchDishesGUID = in.readString();
        this.CheckState = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SalesOrderServingDishesE> CREATOR = new Creator<SalesOrderServingDishesE>() {
        @Override
        public SalesOrderServingDishesE createFromParcel(Parcel source) {
            return new SalesOrderServingDishesE(source);
        }

        @Override
        public SalesOrderServingDishesE[] newArray(int size) {
            return new SalesOrderServingDishesE[size];
        }
    };
}
