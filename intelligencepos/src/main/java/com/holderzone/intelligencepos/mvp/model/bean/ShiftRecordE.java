package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/3/16.
 */

public class ShiftRecordE {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 交接班标识
     */
    private String ShiftRecordGUID;

    /**
     * 班次配置标识,非必传字段，若传入此字段则作为过滤条件
     */
    private String ShiftConfigGUID;

    /**
     * 开始时间
     */
    private String StartBusinessDay;

    /**
     * 结束时间
     */
    private String EndBusinessDay;


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

    public String getShiftRecordGUID() {
        return ShiftRecordGUID;
    }

    public void setShiftRecordGUID(String shiftRecordGUID) {
        ShiftRecordGUID = shiftRecordGUID;
    }

    public String getShiftConfigGUID() {
        return ShiftConfigGUID;
    }

    public void setShiftConfigGUID(String shiftConfigGUID) {
        ShiftConfigGUID = shiftConfigGUID;
    }

    public String getStartBusinessDay() {
        return StartBusinessDay;
    }

    public void setStartBusinessDay(String startBusinessDay) {
        StartBusinessDay = startBusinessDay;
    }

    public String getEndBusinessDay() {
        return EndBusinessDay;
    }

    public void setEndBusinessDay(String endBusinessDay) {
        EndBusinessDay = endBusinessDay;
    }
}
