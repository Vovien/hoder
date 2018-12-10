package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/4/24.
 */

public class QueueUpReportE {

    /**
     * 排队类型标识
     */
    String QueueUpTypeGUID;

    /**
     * 队列 排队类型
     */
    String QueueUpTypeName;

    /**
     * 取号桌数 即取号的单数
     */
    Integer RecordCount;

    /**
     * 取号桌数的人数 即取号的人数
     */
    Integer RecordCustomerCount;

    /**
     * 就餐桌数 确认就餐的单数
     */
    Integer ConfirmCount;

    /**
     * 就餐人数 确认就餐的人数
     */
    Integer ConfirmCustomerCount;

    /**
     * 就餐率 取值0~1，例：0.8=80%， 0.75=75%
     */
    Double ConfirmRatio;

    /**
     * 流失桌数 流失的记录数
     */
    Integer LossRecordCount;

    /**
     * 流失率 取值0~1，例：0.8=80%， 0.75=75%
     */

    Double LossRatio;

    /**
     * 平均等候时间
     */
    Integer AvgWaitingTime;

    /**
     * 流失人数
     */
    Integer LossCustomerCount;

    /**
     * 设备id
     */
    String DeviceID;

    public String getQueueUpTypeGUID() {
        return QueueUpTypeGUID;
    }

    public void setQueueUpTypeGUID(String queueUpTypeGUID) {
        QueueUpTypeGUID = queueUpTypeGUID;
    }

    public String getQueueUpTypeName() {
        return QueueUpTypeName;
    }

    public void setQueueUpTypeName(String queueUpTypeName) {
        QueueUpTypeName = queueUpTypeName;
    }

    public Integer getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(Integer recordCount) {
        RecordCount = recordCount;
    }

    public Integer getRecordCustomerCount() {
        return RecordCustomerCount;
    }

    public void setRecordCustomerCount(Integer recordCustomerCount) {
        RecordCustomerCount = recordCustomerCount;
    }

    public Integer getConfirmCount() {
        return ConfirmCount;
    }

    public void setConfirmCount(Integer confirmCount) {
        ConfirmCount = confirmCount;
    }

    public Integer getConfirmCustomerCount() {
        return ConfirmCustomerCount;
    }

    public void setConfirmCustomerCount(Integer confirmCustomerCount) {
        ConfirmCustomerCount = confirmCustomerCount;
    }

    public Double getConfirmRatio() {
        return ConfirmRatio;
    }

    public void setConfirmRatio(Double confirmRatio) {
        ConfirmRatio = confirmRatio;
    }

    public Integer getLossRecordCount() {
        return LossRecordCount;
    }

    public void setLossRecordCount(Integer lossRecordCount) {
        LossRecordCount = lossRecordCount;
    }

    public Double getLossRatio() {
        return LossRatio;
    }

    public void setLossRatio(Double lossRatio) {
        LossRatio = lossRatio;
    }

    public Integer getAvgWaitingTime() {
        return AvgWaitingTime;
    }

    public void setAvgWaitingTime(Integer avgWaitingTime) {
        AvgWaitingTime = avgWaitingTime;
    }

    public Integer getLossCustomerCount() {
        return LossCustomerCount;
    }

    public void setLossCustomerCount(Integer lossCustomerCount) {
        LossCustomerCount = lossCustomerCount;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
