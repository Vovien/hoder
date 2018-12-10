package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 餐桌表
 * Created by Administrator on 2016-8-22.
 */
public class DiningTable implements Parcelable {

    /**
     * 无参构造
     */
    public DiningTable() {
    }

    /**
     * 数据标识
     */

    private String DiningTableGUID;

    /**
     * 门店标识
     */

    private String StoreGUID;

    /**
     * 区域标识
     */

    private String DiningTableAreaGUID;

    /**
     * 区域实体
     */
    private DiningTableArea DiningTableArea;

    /**
     * 餐桌代码
     */
    private String Code;
    /**
     * 餐桌代码
     */

    private String Name;

    /**
     * 餐桌状态 空闲 = 0 占用 = 1
     */

    private Integer TableStatus;

    /**
     * 启动，停用  0=停用  1=启动
     */

    private Integer Enabled;

    /**
     * 序号
     */
    private Integer Sort;

    /**
     * 座位数
     */
    private Integer SeatsCount;

    /**
     * 订单GUID
     */
    private String SalesOrderGUID;

    private String OrgSalesOrderGUID;

    /**
     * 该餐桌并单后的所属主单
     */
    private String ParentDiningTableGUID;

    /***
     * 桌状态 0：未并单  1.主单桌   2:被并单桌
     */
    private Integer SeatStatus = 0;

    private SalesOrder SalesOrder;

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getDiningTableAreaGUID() {
        return DiningTableAreaGUID;
    }

    public void setDiningTableAreaGUID(String diningTableAreaGUID) {
        DiningTableAreaGUID = diningTableAreaGUID;
    }

    public DiningTableArea getDiningTableArea() {
        return DiningTableArea;
    }

    public void setDiningTableArea(DiningTableArea diningTableArea) {
        DiningTableArea = diningTableArea;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getTableStatus() {
        return TableStatus;
    }

    public void setTableStatus(Integer tableStatus) {
        TableStatus = tableStatus;
    }

    public Integer getEnabled() {
        return Enabled;
    }

    public void setEnabled(Integer enabled) {
        Enabled = enabled;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public Integer getSeatsCount() {
        return SeatsCount;
    }

    public void setSeatsCount(Integer seatsCount) {
        SeatsCount = seatsCount;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public String getOrgSalesOrderGUID() {
        return OrgSalesOrderGUID;
    }

    public void setOrgSalesOrderGUID(String orgSalesOrderGUID) {
        OrgSalesOrderGUID = orgSalesOrderGUID;
    }

    public String getParentDiningTableGUID() {
        return ParentDiningTableGUID;
    }

    public void setParentDiningTableGUID(String parentDiningTableGUID) {
        ParentDiningTableGUID = parentDiningTableGUID;
    }

    public Integer getSeatStatus() {
        return SeatStatus;
    }

    public void setSeatStatus(Integer seatStatus) {
        SeatStatus = seatStatus;
    }

    public SalesOrder getSalesOrder() {
        return SalesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        SalesOrder = salesOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DiningTableGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.DiningTableAreaGUID);
        dest.writeParcelable(this.DiningTableArea, flags);
        dest.writeString(this.Code);
        dest.writeString(this.Name);
        dest.writeValue(this.TableStatus);
        dest.writeValue(this.Enabled);
        dest.writeValue(this.Sort);
        dest.writeValue(this.SeatsCount);
        dest.writeString(this.SalesOrderGUID);
        dest.writeString(this.OrgSalesOrderGUID);
        dest.writeString(this.ParentDiningTableGUID);
        dest.writeValue(this.SeatStatus);
        dest.writeParcelable(this.SalesOrder, flags);
    }

    protected DiningTable(Parcel in) {
        this.DiningTableGUID = in.readString();
        this.StoreGUID = in.readString();
        this.DiningTableAreaGUID = in.readString();
        this.DiningTableArea = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DiningTableArea.class.getClassLoader());
        this.Code = in.readString();
        this.Name = in.readString();
        this.TableStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Enabled = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SeatsCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrderGUID = in.readString();
        this.OrgSalesOrderGUID = in.readString();
        this.ParentDiningTableGUID = in.readString();
        this.SeatStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrder = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.SalesOrder.class.getClassLoader());
    }

    public static final Creator<DiningTable> CREATOR = new Creator<DiningTable>() {
        @Override
        public DiningTable createFromParcel(Parcel source) {
            return new DiningTable(source);
        }

        @Override
        public DiningTable[] newArray(int size) {
            return new DiningTable[size];
        }
    };
}
