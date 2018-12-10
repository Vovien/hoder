package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 设备状态报送，用于客户端和服务端状态报送通讯
 * Created by Administrator on 2016-10-31.
 */
public class DeviceInfo {
    /**
     * 企业标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 编号
     */
    private String DeviceID;

    /**
     * 标识，一般是mac地址
     */
    private String DeviceUID;
    /**
     * 设置类型
     * 0 - 未知
     * 10 = PC服务端
     * 11 = PC客户端
     * 20 = 安卓
     * 30 = 苹果
     */
    private Integer DeviceType = 20;

    /**
     * 设备当前时间
     */
    private String DeviceSystemTime;

    /**
     * 报送时间
     */
    private String ReportTime;

    /**
     * 本地服务器地址，广播通知用
     */
    private String LocalServerUrl;

    /**
     * 消息
     */
    private String msg;

    /**
     * 系统版本号
     */
    private String ver;

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

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getDeviceUID() {
        return DeviceUID;
    }

    public void setDeviceUID(String deviceUID) {
        DeviceUID = deviceUID;
    }

    public Integer getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(Integer deviceType) {
        DeviceType = deviceType;
    }

    public String getDeviceSystemTime() {
        return DeviceSystemTime;
    }

    public void setDeviceSystemTime(String deviceSystemTime) {
        DeviceSystemTime = deviceSystemTime;
    }

    public String getReportTime() {
        return ReportTime;
    }

    public void setReportTime(String reportTime) {
        ReportTime = reportTime;
    }

    public String getLocalServerUrl() {
        return LocalServerUrl;
    }

    public void setLocalServerUrl(String localServerUrl) {
        LocalServerUrl = localServerUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
