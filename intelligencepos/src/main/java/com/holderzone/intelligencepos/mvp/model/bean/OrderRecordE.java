package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoping on 2017/5/9.
 */

public class OrderRecordE implements Parcelable {
    /**
     * 营业日 格式：yyyy-MM-dd
     */
    private String BusinessDay;
    private String StoreGUID;
    private String EnterpriseInfoGUID;
    /**
     * 所属预订时段标识
     */
    private String OrderRecordSectionGUID;
    /***
     * 订单过滤条件
     集合中的元素为或条件
     非必传值，未传值则获取当前营业日所有状态的预订单，     有值：
     0=预订  1=完成
     -1=超时  -10=作废
     100=全部
     */
    private List<Integer> OrderStatList;
    /** 订单总数*/
    private Integer  OrderCount_Total;
    /**已抵达*/
    private Integer  OrderCount_Arrive;
    /**未抵达*/
    private Integer  OrderCount_NotArrive;
    /**作废数量*/
    private Integer  OrderCount_Invalid;
    /**超时过期*/
    private Integer  OrderCount_TimeOut;


    /***/
    private String OrderRecordGUID;
    /**
     * 订单流水号
     */
    private String SerialNumber;
    /***/
    private String DiningTableGUID;
    private List<OrderRecordRemarkE> ArrayOfOrderRecordRemarkE  ;
    private ArrayList<OrderRecordRemarkItemE> ArrayOfOrderRecordRemarkItemE  ;
    /**
     * 备注
     */
    private String Remark;
    /**是否发送手机短信0=不发送 1=发送*/
    private Integer SendSMS;
    /**预订起始时段，格式：yyyy-MM-dd HH:mm:ss*/
    private String OrderTime1;
    /**预订结束时段(保留时间)*/
    private String OrderTime2;
    /**
     * 客人电话
     */
    private String GuestTel;
    /**
     * 客人称呼
     */
    private String GuestName;
    /**
     * 完成时间（）
     */
    private String FinishTime;
    /**
     * 用户性别
     * 0=女
     * 1=男
     */
    private Integer Sex;
    /**
     * 客人数量
     */
    private Integer GuestCount;
    private String OperatorName;
    private String CreateTime;
    /**
     * 短信发送条数
     */
    private Integer SMSTimes;
    private String CreateUsersGUID;
    /**
     * 状态
     * 0=预订中    1=完成  -1=超时 -2作废
     */
    private Integer OrderStat;

    /**
     * 当前预订的桌台区域名
     */
    private String TableAreaName;
    /**
     * 当前预订的桌台名
     */
    private String TableName;

    public List<Integer> getOrderStatList() {
        return OrderStatList;
    }

    public void setOrderStatList(List<Integer> orderStatList) {
        OrderStatList = orderStatList;
    }
    public String getOrderRecordGUID() {
        return OrderRecordGUID;
    }

    public void setOrderRecordGUID(String orderRecordGUID) {
        OrderRecordGUID = orderRecordGUID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public List<OrderRecordRemarkE> getArrayOfOrderRecordRemarkE() {
        return ArrayOfOrderRecordRemarkE;
    }

    public void setArrayOfOrderRecordRemarkE(List<OrderRecordRemarkE> arrayOfOrderRecordRemarkE) {
        ArrayOfOrderRecordRemarkE = arrayOfOrderRecordRemarkE;
    }

    public ArrayList<OrderRecordRemarkItemE> getArrayOfOrderRecordRemarkItemE() {
        return ArrayOfOrderRecordRemarkItemE;
    }

    public Integer getOrderCount_Total() {
        return OrderCount_Total;
    }

    public void setOrderCount_Total(Integer orderCount_Total) {
        OrderCount_Total = orderCount_Total;
    }

    public Integer getOrderCount_Arrive() {
        return OrderCount_Arrive;
    }

    public void setOrderCount_Arrive(Integer orderCount_Arrive) {
        OrderCount_Arrive = orderCount_Arrive;
    }

    public Integer getOrderCount_NotArrive() {
        return OrderCount_NotArrive;
    }

    public void setOrderCount_NotArrive(Integer orderCount_NotArrive) {
        OrderCount_NotArrive = orderCount_NotArrive;
    }

    public Integer getOrderCount_Invalid() {
        return OrderCount_Invalid;
    }

    public void setOrderCount_Invalid(Integer orderCount_Invalid) {
        OrderCount_Invalid = orderCount_Invalid;
    }

    public Integer getOrderCount_TimeOut() {
        return OrderCount_TimeOut;
    }

    public void setOrderCount_TimeOut(Integer orderCount_TimeOut) {
        OrderCount_TimeOut = orderCount_TimeOut;
    }

    public void setArrayOfOrderRecordRemarkItemE(ArrayList<OrderRecordRemarkItemE> arrayOfOrderRecordRemarkItemE) {
        ArrayOfOrderRecordRemarkItemE = arrayOfOrderRecordRemarkItemE;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getGuestTel() {
        return GuestTel;
    }

    public void setGuestTel(String guestTel) {
        GuestTel = guestTel;
    }

    public String getCreateUsersGUID() {
        return CreateUsersGUID;
    }

    public void setCreateUsersGUID(String createUsersGUID) {
        CreateUsersGUID = createUsersGUID;
    }

    public String getGuestName() {
        return GuestName;
    }

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }



    public Integer getSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(Integer sendSMS) {
        SendSMS = sendSMS;
    }

    public String getOrderTime1() {
        return OrderTime1;
    }

    public void setOrderTime1(String orderTime1) {
        OrderTime1 = orderTime1;
    }

    public String getOrderTime2() {
        return OrderTime2;
    }

    public void setOrderTime2(String orderTime2) {
        OrderTime2 = orderTime2;
    }

    public void setGuestName(String guestName) {
        GuestName = guestName;
    }

    public String getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(String finishTime) {
        FinishTime = finishTime;
    }

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public Integer getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(Integer guestCount) {
        GuestCount = guestCount;
    }

    public Integer getSMSTimes() {
        return SMSTimes;
    }

    public void setSMSTimes(Integer SMSTimes) {
        this.SMSTimes = SMSTimes;
    }

    public Integer getOrderStat() {
        return OrderStat;
    }

    public void setOrderStat(Integer orderStat) {
        OrderStat = orderStat;
    }

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public String getOrderRecordSectionGUID() {
        return OrderRecordSectionGUID;
    }

    public void setOrderRecordSectionGUID(String orderRecordSectionGUID) {
        OrderRecordSectionGUID = orderRecordSectionGUID;
    }

    public String getTableAreaName() {
        return TableAreaName;
    }

    public void setTableAreaName(String tableAreaName) {
        TableAreaName = tableAreaName;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public OrderRecordE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.BusinessDay);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.OrderRecordSectionGUID);
        dest.writeList(this.OrderStatList);
        dest.writeValue(this.OrderCount_Total);
        dest.writeValue(this.OrderCount_Arrive);
        dest.writeValue(this.OrderCount_NotArrive);
        dest.writeValue(this.OrderCount_Invalid);
        dest.writeValue(this.OrderCount_TimeOut);
        dest.writeString(this.OrderRecordGUID);
        dest.writeString(this.SerialNumber);
        dest.writeString(this.DiningTableGUID);
        dest.writeList(this.ArrayOfOrderRecordRemarkE);
        dest.writeTypedList(this.ArrayOfOrderRecordRemarkItemE);
        dest.writeString(this.Remark);
        dest.writeValue(this.SendSMS);
        dest.writeString(this.OrderTime1);
        dest.writeString(this.OrderTime2);
        dest.writeString(this.GuestTel);
        dest.writeString(this.GuestName);
        dest.writeString(this.FinishTime);
        dest.writeValue(this.Sex);
        dest.writeValue(this.GuestCount);
        dest.writeString(this.OperatorName);
        dest.writeString(this.CreateTime);
        dest.writeValue(this.SMSTimes);
        dest.writeString(this.CreateUsersGUID);
        dest.writeValue(this.OrderStat);
        dest.writeString(this.TableAreaName);
        dest.writeString(this.TableName);
    }

    protected OrderRecordE(Parcel in) {
        this.BusinessDay = in.readString();
        this.StoreGUID = in.readString();
        this.EnterpriseInfoGUID = in.readString();
        this.OrderRecordSectionGUID = in.readString();
        this.OrderStatList = new ArrayList<Integer>();
        in.readList(this.OrderStatList, Integer.class.getClassLoader());
        this.OrderCount_Total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderCount_Arrive = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderCount_NotArrive = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderCount_Invalid = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderCount_TimeOut = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderRecordGUID = in.readString();
        this.SerialNumber = in.readString();
        this.DiningTableGUID = in.readString();
        this.ArrayOfOrderRecordRemarkE = new ArrayList<OrderRecordRemarkE>();
        in.readList(this.ArrayOfOrderRecordRemarkE, OrderRecordRemarkE.class.getClassLoader());
        this.ArrayOfOrderRecordRemarkItemE = in.createTypedArrayList(OrderRecordRemarkItemE.CREATOR);
        this.Remark = in.readString();
        this.SendSMS = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderTime1 = in.readString();
        this.OrderTime2 = in.readString();
        this.GuestTel = in.readString();
        this.GuestName = in.readString();
        this.FinishTime = in.readString();
        this.Sex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.GuestCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OperatorName = in.readString();
        this.CreateTime = in.readString();
        this.SMSTimes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CreateUsersGUID = in.readString();
        this.OrderStat = (Integer) in.readValue(Integer.class.getClassLoader());
        this.TableAreaName = in.readString();
        this.TableName = in.readString();
    }

    public static final Creator<OrderRecordE> CREATOR = new Creator<OrderRecordE>() {
        @Override
        public OrderRecordE createFromParcel(Parcel source) {
            return new OrderRecordE(source);
        }

        @Override
        public OrderRecordE[] newArray(int size) {
            return new OrderRecordE[size];
        }
    };
}
