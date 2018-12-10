package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 餐桌实体扩展
 * Created by tcw on 2017/3/10.
 */

public class DiningTableE extends DiningTable implements Parcelable{

    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 区域实体
     */
    private DiningTableAreaE DiningTableAreaE;

    /**
     * 餐桌状态的中文描述
     */
    private String TableStatusCN;

    /**
     * 座位使用数量 即客人入住的数量
     */
    private Integer UseCount;

    /**
     * 当前餐桌的消费金额 上菜金额合计，含赠送
     */
    private double Total;

    /**
     * 使用小时 例：1.5h，0.5h
     */
    private String UseHour;

    /**
     * 是否有挂单 0=无  1=有
     */
    private Integer IsHang;

    /**
     * 是否被预订 0=无  1=有
     */
    private Integer IsOrder;

    /**
     * 是否并单 0=无  1=有
     */
    private Integer IsMerge;

    /**
     * 是否算账 0=无  1=有
     */
    private Integer IsCalc;

    /**
     * 点餐次数
     */
    private Integer OrderBatchCount;

    /**
     * 账单实体
     */
    private SalesOrderE SalesOrderE;

    /**
     * 预订记录
     */
    private OrderRecordE OrderRecordE;

    private String AreaName;
    /**
     * 流水
     */
    private String SerialNumber;
    /**
     * 客人数量
     */
    private Integer GuestCount;
    /**
     * 待上数量 （下单数-已上数量）
     */
    private Double WaitServingCount;
    /**
     * 菜品批次
     */
    private List<SalesOrderBatchDishesE> ArrayOfSalesOrderBatchDishesE;

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

    public Double getWaitServingCount() {
        return WaitServingCount;
    }

    public void setWaitServingCount(Double waitServingCount) {
        WaitServingCount = waitServingCount;
    }

    public List<SalesOrderBatchDishesE> getArrayOfSalesOrderBatchDishesE() {
        return ArrayOfSalesOrderBatchDishesE;
    }

    public void setArrayOfSalesOrderBatchDishesE(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
        ArrayOfSalesOrderBatchDishesE = arrayOfSalesOrderBatchDishesE;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public DiningTableAreaE getDiningTableAreaE() {
        return DiningTableAreaE;
    }

    public void setDiningTableAreaE(DiningTableAreaE diningTableAreaE) {
        DiningTableAreaE = diningTableAreaE;
    }

    public String getTableStatusCN() {
        return TableStatusCN;
    }

    public void setTableStatusCN(String tableStatusCN) {
        TableStatusCN = tableStatusCN;
    }

    public Integer getUseCount() {
        return UseCount;
    }

    public void setUseCount(Integer useCount) {
        UseCount = useCount;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getUseHour() {
        return UseHour;
    }

    public void setUseHour(String useHour) {
        UseHour = useHour;
    }

    public Integer getIsHang() {
        return IsHang;
    }

    public void setIsHang(Integer isHang) {
        IsHang = isHang;
    }

    public Integer getIsOrder() {
        return IsOrder;
    }

    public void setIsOrder(Integer isOrder) {
        IsOrder = isOrder;
    }

    public Integer getIsMerge() {
        return IsMerge;
    }

    public void setIsMerge(Integer isMerge) {
        IsMerge = isMerge;
    }

    public OrderRecordE getOrderRecordE() {
        return OrderRecordE;
    }

    public void setOrderRecordE(OrderRecordE orderRecordE) {
        OrderRecordE = orderRecordE;
    }

    public Integer getIsCalc() {
        return IsCalc;
    }

    public void setIsCalc(Integer isCalc) {
        IsCalc = isCalc;
    }

    public Integer getOrderBatchCount() {
        return OrderBatchCount;
    }

    public void setOrderBatchCount(Integer orderBatchCount) {
        OrderBatchCount = orderBatchCount;
    }

    public SalesOrderE getSalesOrderE() {
        return SalesOrderE;
    }

    public void setSalesOrderE(SalesOrderE salesOrderE) {
        SalesOrderE = salesOrderE;
    }

    public DiningTableE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeParcelable(this.DiningTableAreaE, flags);
        dest.writeString(this.TableStatusCN);
        dest.writeValue(this.UseCount);
        dest.writeDouble(this.Total);
        dest.writeString(this.UseHour);
        dest.writeValue(this.IsHang);
        dest.writeValue(this.IsOrder);
        dest.writeValue(this.IsMerge);
        dest.writeValue(this.IsCalc);
        dest.writeValue(this.OrderBatchCount);
        dest.writeParcelable(this.SalesOrderE, flags);
        dest.writeParcelable(this.OrderRecordE, flags);
        dest.writeString(this.AreaName);
        dest.writeString(this.SerialNumber);
        dest.writeValue(this.GuestCount);
        dest.writeValue(this.WaitServingCount);
        dest.writeTypedList(this.ArrayOfSalesOrderBatchDishesE);
    }

    protected DiningTableE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
        this.DiningTableAreaE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE.class.getClassLoader());
        this.TableStatusCN = in.readString();
        this.UseCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Total = in.readDouble();
        this.UseHour = in.readString();
        this.IsHang = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsOrder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsMerge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsCalc = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderBatchCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrderE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE.class.getClassLoader());
        this.OrderRecordE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.OrderRecordE.class.getClassLoader());
        this.AreaName = in.readString();
        this.SerialNumber = in.readString();
        this.GuestCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.WaitServingCount = (Double) in.readValue(Double.class.getClassLoader());
        this.ArrayOfSalesOrderBatchDishesE = in.createTypedArrayList(SalesOrderBatchDishesE.CREATOR);
    }

    public static final Creator<DiningTableE> CREATOR = new Creator<DiningTableE>() {
        @Override
        public DiningTableE createFromParcel(Parcel source) {
            return new DiningTableE(source);
        }

        @Override
        public DiningTableE[] newArray(int size) {
            return new DiningTableE[size];
        }
    };
}
