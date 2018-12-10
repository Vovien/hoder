package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/3/16.
 */

public class ShiftConfigE {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;
    /**
     * 班次标识
     */
    private String ShiftConfigGUID;

    /**
     * 班次名称
     */
    private String Name;

    /**
     * 班次序号
     */
    private Integer Sort;

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

    public String getShiftConfigGUID() {
        return ShiftConfigGUID;
    }

    public void setShiftConfigGUID(String shiftConfigGUID) {
        ShiftConfigGUID = shiftConfigGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }
}
