package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.mvp.model.bean.db.Users;

import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class SalesOrderBatchE extends SalesOrderBatch implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 是否加载会员 0=否 1=是，不传默认0
     */
    private Integer ReturnMemberInfo;

    /**
     * 餐桌实体
     */
    private DiningTableE DiningTableE;

    /**
     * 批次下的菜品
     */
    private List<SalesOrderBatchDishesE> ArrayOfSalesOrderBatchDishesE;

    /**
     * 批次下单人
     */
    private Users StaffUsersE;

    private List<DishesReturnReasonE> ArrayOfDishesReturnReasonE;

    /**
     * 设备标识
     */
    private String DeviceID;

    /**
     * 叫起/挂起状态 0=挂起  1=叫起
     */
    private Integer CheckStatus;
    /**
     * 餐桌名称
     */
    private String tableName;
    /**
     * 该批次的所有钱
     */
    private Double totleMoney;
    /**
     * 会员总价格
     */
    private Double MemberTotal;

    /**
     * 下单人姓名
     */
    private String StaffName;
    /**
     * 总价格
     */
    private Double Total;

    /**
     * 是否加载特殊要求 0=否 1=是，不传默认0
     */
    private Integer ReturnBatchDishesRemark;

    /**
     * 是否加载做法 0=否 1=是，不传默认0
     */
    private Integer ReturnBatchDishesPractice;
    /**
     * 门店Guid
     */
    private String StoreGUID;
    /**
     * 操作员Guid
     */
    private String OperaStaffGUID;
    /**
     * 划菜集合
     */
    private List<SalesOrderServingDishesE> ArrayOfSalesOrderServingDishesE;

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getOperaStaffGUID() {
        return OperaStaffGUID;
    }

    public void setOperaStaffGUID(String operaStaffGUID) {
        OperaStaffGUID = operaStaffGUID;
    }

    public List<SalesOrderServingDishesE> getArrayOfSalesOrderServingDishesE() {
        return ArrayOfSalesOrderServingDishesE;
    }

    public void setArrayOfSalesOrderServingDishesE(List<SalesOrderServingDishesE> arrayOfSalesOrderServingDishesE) {
        ArrayOfSalesOrderServingDishesE = arrayOfSalesOrderServingDishesE;
    }

    public String getTableName() {
        return tableName;
    }

    public Double getMemberTotal() {
        return MemberTotal;
    }

    public void setMemberTotal(Double memberTotal) {
        MemberTotal = memberTotal;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Double getTotleMoney() {
        return totleMoney;
    }

    public void setTotleMoney(Double totleMoney) {
        this.totleMoney = totleMoney;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public DiningTableE getDiningTableE() {
        return DiningTableE;
    }

    public void setDiningTableE(DiningTableE diningTableE) {
        DiningTableE = diningTableE;
    }

    public List<SalesOrderBatchDishesE> getArrayOfSalesOrderBatchDishesE() {
        return ArrayOfSalesOrderBatchDishesE;
    }

    public void setArrayOfSalesOrderBatchDishesE(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
        ArrayOfSalesOrderBatchDishesE = arrayOfSalesOrderBatchDishesE;
    }

    public Users getStaffUsersE() {
        return StaffUsersE;
    }

    public void setStaffUsersE(Users staffUsersE) {
        StaffUsersE = staffUsersE;
    }

    public List<DishesReturnReasonE> getArrayOfDishesReturnReasonE() {
        return ArrayOfDishesReturnReasonE;
    }

    public void setArrayOfDishesReturnReasonE(List<DishesReturnReasonE> arrayOfDishesReturnReasonE) {
        ArrayOfDishesReturnReasonE = arrayOfDishesReturnReasonE;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public Integer getCheckStatus() {
        return CheckStatus;
    }

    public Integer getReturnMemberInfo() {
        return ReturnMemberInfo;
    }

    public void setReturnMemberInfo(Integer returnMemberInfo) {
        ReturnMemberInfo = returnMemberInfo;
    }

    public void setCheckStatus(Integer checkStatus) {
        CheckStatus = checkStatus;
    }

    public Integer getReturnBatchDishesRemark() {
        return ReturnBatchDishesRemark;
    }

    public void setReturnBatchDishesRemark(Integer returnBatchDishesRemark) {
        ReturnBatchDishesRemark = returnBatchDishesRemark;
    }

    public Integer getReturnBatchDishesPractice() {
        return ReturnBatchDishesPractice;
    }

    public void setReturnBatchDishesPractice(Integer returnBatchDishesPractice) {
        ReturnBatchDishesPractice = returnBatchDishesPractice;
    }

    public SalesOrderBatchE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeValue(this.ReturnMemberInfo);
        dest.writeParcelable(this.DiningTableE, flags);
        dest.writeTypedList(this.ArrayOfSalesOrderBatchDishesE);
        dest.writeParcelable(this.StaffUsersE, flags);
        dest.writeTypedList(this.ArrayOfDishesReturnReasonE);
        dest.writeString(this.DeviceID);
        dest.writeValue(this.CheckStatus);
        dest.writeString(this.tableName);
        dest.writeValue(this.totleMoney);
        dest.writeValue(this.MemberTotal);
        dest.writeString(this.StaffName);
        dest.writeValue(this.Total);
        dest.writeValue(this.ReturnBatchDishesRemark);
        dest.writeValue(this.ReturnBatchDishesPractice);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.OperaStaffGUID);
        dest.writeTypedList(this.ArrayOfSalesOrderServingDishesE);
    }

    protected SalesOrderBatchE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.ReturnMemberInfo = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DiningTableE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DiningTableE.class.getClassLoader());
        this.ArrayOfSalesOrderBatchDishesE = in.createTypedArrayList(SalesOrderBatchDishesE.CREATOR);
        this.StaffUsersE = in.readParcelable(Users.class.getClassLoader());
        this.ArrayOfDishesReturnReasonE = in.createTypedArrayList(DishesReturnReasonE.CREATOR);
        this.DeviceID = in.readString();
        this.CheckStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tableName = in.readString();
        this.totleMoney = (Double) in.readValue(Double.class.getClassLoader());
        this.MemberTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.StaffName = in.readString();
        this.Total = (Double) in.readValue(Double.class.getClassLoader());
        this.ReturnBatchDishesRemark = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnBatchDishesPractice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.StoreGUID = in.readString();
        this.OperaStaffGUID = in.readString();
        this.ArrayOfSalesOrderServingDishesE = in.createTypedArrayList(SalesOrderServingDishesE.CREATOR);
    }

    public static final Creator<SalesOrderBatchE> CREATOR = new Creator<SalesOrderBatchE>() {
        @Override
        public SalesOrderBatchE createFromParcel(Parcel source) {
            return new SalesOrderBatchE(source);
        }

        @Override
        public SalesOrderBatchE[] newArray(int size) {
            return new SalesOrderBatchE[size];
        }
    };
}
