package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by tcw on 2017/3/16.
 */

public class SalesOrderMergeE {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 账单标识
     */
    private String SalesOrderGUID;

    /**   * 并入的子单标识
     */
    private List<SubSalesOrder> ArrayOfSubSalesOrder;
    /**
     * 操作人帐号标识
     */
    private String StaffGUID;
    /**
     * 终端设备ID
     */
    private String DeviceID;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public List<SubSalesOrder> getArrayOfSubSalesOrder() {
        return ArrayOfSubSalesOrder;
    }

    public void setArrayOfSubSalesOrder(List<SubSalesOrder> arrayOfSubSalesOrder) {
        ArrayOfSubSalesOrder = arrayOfSubSalesOrder;
    }

    public String getStaffGUID() {
        return StaffGUID;
    }

    public void setStaffGUID(String staffGUID) {
        StaffGUID = staffGUID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
