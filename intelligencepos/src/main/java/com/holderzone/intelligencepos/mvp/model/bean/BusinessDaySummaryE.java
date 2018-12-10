package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by LiTao on 2017-5-9.
 */

public class BusinessDaySummaryE {
    /**
     * 商家标志GUID
     * */
    private String EnterpriseInfoGUID;
    /**
     * 门店标志GUID
     * */
    private String StoreGUID;
    /**
     * 标识 1=菜品销售汇总 2=赠菜汇总 3=退菜汇总 4=收款汇总 5=折扣汇总 6=优惠券汇总
     */
    private Integer ItemID;
    /**
     * 项目名称
     */
    private String Caption;
    /**
     * 笔数值
     */
    private String Data01;
    /**
     * 笔数值名称
     */
    private String Data01Caption;
    /**
     * 汇总值
     */
    private String Data02;
    /**
     * 汇总值名称
     */
    private String Data02Caption;
    /**
     * 排序号
     */
    private Integer Sort;
    /**
     * 营业日
     * */
    private String BusinessDay;
    private String Data03;
    private String Data03Caption;
    private String Data04;
    private String Data04Caption;
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

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public Integer getItemID() {
        return ItemID;
    }

    public void setItemID(Integer itemID) {
        ItemID = itemID;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getData01() {
        return Data01;
    }

    public void setData01(String data01) {
        Data01 = data01;
    }

    public String getData01Caption() {
        return Data01Caption;
    }

    public void setData01Caption(String data01Caption) {
        Data01Caption = data01Caption;
    }

    public String getData02() {
        return Data02;
    }

    public void setData02(String data02) {
        Data02 = data02;
    }

    public String getData02Caption() {
        return Data02Caption;
    }

    public void setData02Caption(String data02Caption) {
        Data02Caption = data02Caption;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public String getData03() {
        return Data03;
    }

    public void setData03(String data03) {
        Data03 = data03;
    }

    public String getData03Caption() {
        return Data03Caption;
    }

    public void setData03Caption(String data03Caption) {
        Data03Caption = data03Caption;
    }

    public String getData04() {
        return Data04;
    }

    public void setData04(String data04) {
        Data04 = data04;
    }

    public String getData04Caption() {
        return Data04Caption;
    }

    public void setData04Caption(String data04Caption) {
        Data04Caption = data04Caption;
    }
}
