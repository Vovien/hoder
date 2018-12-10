package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by tcw on 2017/3/24.
 */

public class DinnerE implements Parcelable{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;
    /**
     * 终端设备标识
     */
    private String DeviceID;
    /**
     * 是否返回餐桌区域信息 0=不返回  1=返回
     */
    private int ReturnTableArea;
    /**
     * 餐桌区域集合信息
     */
    private List<DiningTableAreaE> ArrayOfDiningTableAreaE;
    /**
     * 是否返回餐桌信息 0=不返回  1=返回
     */
    private int ReturnDiningTable;
    /**
     * 餐桌区域集合信息
     */
    private List<DiningTableE> ArrayOfDiningTableE;
    /**
     * 是否返回统计状态信息 0=不返回  1=返回
     */
    private int ReturnTableStatus;
    /**
     * 状态统计信息
     */
    private List<TableStatusE> ArrayOfTableStatusE;
    /**
     * 是否返回今日统计信息 0=不返回  1=返回
     */
    private int ReturnTodayStatistics;
    /**
     * 今日统计信息
     */
    private List<TodayStatisticsE> ArrayOfTodayStatisticsE;

    /**
     * 是否返回业务消息  0=不返回  1=返回
     */
    private int ReturnSalesMsg;

    private List<SalesMsgE> ArrayOfSalesMsgE;


    /**
     * 订单标识
     */
    private String SalesOrderGUID;

    /**
     *
     */
    private String Md5Hash16;

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

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public int getReturnTableArea() {
        return ReturnTableArea;
    }

    public void setReturnTableArea(int returnTableArea) {
        ReturnTableArea = returnTableArea;
    }

    public List<DiningTableAreaE> getArrayOfDiningTableAreaE() {
        return ArrayOfDiningTableAreaE;
    }

    public void setArrayOfDiningTableAreaE(List<DiningTableAreaE> arrayOfDiningTableAreaE) {
        ArrayOfDiningTableAreaE = arrayOfDiningTableAreaE;
    }

    public int getReturnDiningTable() {
        return ReturnDiningTable;
    }

    public void setReturnDiningTable(int returnDiningTable) {
        ReturnDiningTable = returnDiningTable;
    }

    public List<DiningTableE> getArrayOfDiningTableE() {
        return ArrayOfDiningTableE;
    }

    public void setArrayOfDiningTableE(List<DiningTableE> arrayOfDiningTableE) {
        ArrayOfDiningTableE = arrayOfDiningTableE;
    }

    public int getReturnTableStatus() {
        return ReturnTableStatus;
    }

    public void setReturnTableStatus(int returnTableStatus) {
        ReturnTableStatus = returnTableStatus;
    }

    public List<TableStatusE> getArrayOfTableStatusE() {
        return ArrayOfTableStatusE;
    }

    public void setArrayOfTableStatusE(List<TableStatusE> arrayOfTableStatusE) {
        ArrayOfTableStatusE = arrayOfTableStatusE;
    }

    public int getReturnTodayStatistics() {
        return ReturnTodayStatistics;
    }

    public void setReturnTodayStatistics(int returnTodayStatistics) {
        ReturnTodayStatistics = returnTodayStatistics;
    }

    public List<TodayStatisticsE> getArrayOfTodayStatisticsE() {
        return ArrayOfTodayStatisticsE;
    }

    public void setArrayOfTodayStatisticsE(List<TodayStatisticsE> arrayOfTodayStatisticsE) {
        ArrayOfTodayStatisticsE = arrayOfTodayStatisticsE;
    }

    public int getReturnSalesMsg() {
        return ReturnSalesMsg;
    }

    public void setReturnSalesMsg(int returnSalesMsg) {
        ReturnSalesMsg = returnSalesMsg;
    }

    public List<SalesMsgE> getArrayOfSalesMsgE() {
        return ArrayOfSalesMsgE;
    }

    public void setArrayOfSalesMsgE(List<SalesMsgE> arrayOfSalesMsgE) {
        ArrayOfSalesMsgE = arrayOfSalesMsgE;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public String getMd5Hash16() {
        return Md5Hash16;
    }

    public void setMd5Hash16(String md5Hash16) {
        Md5Hash16 = md5Hash16;
    }

    public DinnerE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.DeviceID);
        dest.writeInt(this.ReturnTableArea);
        dest.writeTypedList(this.ArrayOfDiningTableAreaE);
        dest.writeInt(this.ReturnDiningTable);
        dest.writeTypedList(this.ArrayOfDiningTableE);
        dest.writeInt(this.ReturnTableStatus);
        dest.writeTypedList(this.ArrayOfTableStatusE);
        dest.writeInt(this.ReturnTodayStatistics);
        dest.writeTypedList(this.ArrayOfTodayStatisticsE);
        dest.writeInt(this.ReturnSalesMsg);
        dest.writeTypedList(this.ArrayOfSalesMsgE);
        dest.writeString(this.SalesOrderGUID);
        dest.writeString(this.Md5Hash16);
    }

    protected DinnerE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.DeviceID = in.readString();
        this.ReturnTableArea = in.readInt();
        this.ArrayOfDiningTableAreaE = in.createTypedArrayList(DiningTableAreaE.CREATOR);
        this.ReturnDiningTable = in.readInt();
        this.ArrayOfDiningTableE = in.createTypedArrayList(DiningTableE.CREATOR);
        this.ReturnTableStatus = in.readInt();
        this.ArrayOfTableStatusE = in.createTypedArrayList(TableStatusE.CREATOR);
        this.ReturnTodayStatistics = in.readInt();
        this.ArrayOfTodayStatisticsE = in.createTypedArrayList(TodayStatisticsE.CREATOR);
        this.ReturnSalesMsg = in.readInt();
        this.ArrayOfSalesMsgE = in.createTypedArrayList(SalesMsgE.CREATOR);
        this.SalesOrderGUID = in.readString();
        this.Md5Hash16 = in.readString();
    }

    public static final Creator<DinnerE> CREATOR = new Creator<DinnerE>() {
        @Override
        public DinnerE createFromParcel(Parcel source) {
            return new DinnerE(source);
        }

        @Override
        public DinnerE[] newArray(int size) {
            return new DinnerE[size];
        }
    };
}
