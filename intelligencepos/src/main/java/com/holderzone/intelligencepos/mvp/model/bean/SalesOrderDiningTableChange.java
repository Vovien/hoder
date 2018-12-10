package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by Administrator on 2016/11/3.
 */
public class SalesOrderDiningTableChange {

    /**数据标识*/
    private String SalesOrderDiningTableChangeGUID ;

    /**消费单标识*/
    private String SalesOrderGUID ;

    /**原来的餐桌标识*/
    private String OrgDiningTableGUID ;

    /**更换后的餐桌标识*/
    private String NewDiningTableGUID ;

    /**更换时间*/
    private String ChangeTime ;

    /**操作员工标识*/
    private String StaffGUID ;

    public String getSalesOrderDiningTableChangeGUID() {
        return SalesOrderDiningTableChangeGUID;
    }

    public void setSalesOrderDiningTableChangeGUID(String salesOrderDiningTableChangeGUID) {
        SalesOrderDiningTableChangeGUID = salesOrderDiningTableChangeGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public String getOrgDiningTableGUID() {
        return OrgDiningTableGUID;
    }

    public void setOrgDiningTableGUID(String orgDiningTableGUID) {
        OrgDiningTableGUID = orgDiningTableGUID;
    }

    public String getNewDiningTableGUID() {
        return NewDiningTableGUID;
    }

    public void setNewDiningTableGUID(String newDiningTableGUID) {
        NewDiningTableGUID = newDiningTableGUID;
    }

    public String getChangeTime() {
        return ChangeTime;
    }

    public void setChangeTime(String changeTime) {
        ChangeTime = changeTime;
    }

    public String getStaffGUID() {
        return StaffGUID;
    }

    public void setStaffGUID(String staffGUID) {
        StaffGUID = staffGUID;
    }
}
