package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/4/24.
 */

public class QueueUpTypeE {

    /**
     * 商家标识
     */
    String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    String StoreGUID;

    /**
     * 数据标识
     */
    String QueueUpTypeGUID;

    /**
     * 排号类型名称
     */
    String Name;

    /**
     * 客人数下限
     */
    Integer MinCustomerCount;

    /**
     * 客人数上限
     */
    Integer MaxCustomerCount;

    /**
     * 序号
     */
    String Sort;

    /**
     * 剩余空桌数
     */
    Integer SurplusTableCount;

    /**
     * 当前排队数量
     */
    Integer WaitingCount;

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

    public String getQueueUpTypeGUID() {
        return QueueUpTypeGUID;
    }

    public void setQueueUpTypeGUID(String queueUpTypeGUID) {
        QueueUpTypeGUID = queueUpTypeGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getMinCustomerCount() {
        return MinCustomerCount;
    }

    public void setMinCustomerCount(Integer minCustomerCount) {
        MinCustomerCount = minCustomerCount;
    }

    public Integer getMaxCustomerCount() {
        return MaxCustomerCount;
    }

    public void setMaxCustomerCount(Integer maxCustomerCount) {
        MaxCustomerCount = maxCustomerCount;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public Integer getSurplusTableCount() {
        return SurplusTableCount;
    }

    public void setSurplusTableCount(Integer surplusTableCount) {
        SurplusTableCount = surplusTableCount;
    }

    public Integer getWaitingCount() {
        return WaitingCount;
    }

    public void setWaitingCount(Integer waitingCount) {
        WaitingCount = waitingCount;
    }
}
