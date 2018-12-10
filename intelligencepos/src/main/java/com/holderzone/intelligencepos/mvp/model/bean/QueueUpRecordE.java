package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/4/24.
 */

public class QueueUpRecordE {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 数据标识
     */
    private String QueueUpRecordGUID;

    /**
     * 营业日
     */
    private String BusinessDay;

    /**
     * 排号类别标识
     */
    private String QueueUpTypeGUID;

    /**
     * 排号编码
     */
    private String Code;

    /**
     * 记录状态 0=排号中  1=排号完成  -1=排号完成
     */
    private Integer QueueUpStatus;

    /**
     * 客人数
     */
    private Integer CustomerCount;

    /**
     * 客人电话
     */
    private String CustomerTel;

    /**
     * 叫号次数
     */
    private Integer CallTimes;

    /**
     * 创建操作员
     */
    private String CreateUsersGUID;

    /**
     * 创建时间 格式：yyyy-MM-dd HH:mm:ss
     */
    private String CreateTime;

    /**
     * 完成操作员
     */
    private String FinishUsersGUID;

    /**
     * 完成时间 格式：yyyy-MM-dd HH:mm:ss
     */
    private String FinishTime;

    /**
     * 删除状态 0=未删除  1=已删除
     */
    private Integer IsDelete;

    /**
     * 是否排在当前类别的第一位
     */
    private Integer Index;

    /**
     * 设备唯一id
     */
    private String DeviceID;

    /**
     * 等待时间
     */
    private Long WaitingTime;

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

    public String getQueueUpRecordGUID() {
        return QueueUpRecordGUID;
    }

    public void setQueueUpRecordGUID(String queueUpRecordGUID) {
        QueueUpRecordGUID = queueUpRecordGUID;
    }

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public String getQueueUpTypeGUID() {
        return QueueUpTypeGUID;
    }

    public void setQueueUpTypeGUID(String queueUpTypeGUID) {
        QueueUpTypeGUID = queueUpTypeGUID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Integer getQueueUpStatus() {
        return QueueUpStatus;
    }

    public void setQueueUpStatus(Integer queueUpStatus) {
        QueueUpStatus = queueUpStatus;
    }

    public Integer getCustomerCount() {
        return CustomerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        CustomerCount = customerCount;
    }

    public String getCustomerTel() {
        return CustomerTel;
    }

    public void setCustomerTel(String customerTel) {
        CustomerTel = customerTel;
    }

    public Integer getCallTimes() {
        return CallTimes;
    }

    public void setCallTimes(Integer callTimes) {
        CallTimes = callTimes;
    }

    public String getCreateUsersGUID() {
        return CreateUsersGUID;
    }

    public void setCreateUsersGUID(String createUsersGUID) {
        CreateUsersGUID = createUsersGUID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getFinishUsersGUID() {
        return FinishUsersGUID;
    }

    public void setFinishUsersGUID(String finishUsersGUID) {
        FinishUsersGUID = finishUsersGUID;
    }

    public String getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(String finishTime) {
        FinishTime = finishTime;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    public Integer getIndex() {
        return Index;
    }

    public void setIndex(Integer index) {
        Index = index;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public Long getWaitingTime() {
        return WaitingTime;
    }

    public void setWaitingTime(Long waitingTime) {
        WaitingTime = waitingTime;
    }
}
