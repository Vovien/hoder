package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/3/10.
 */

public class ApiBase {
    /**
     * 登录人guid
     */
    private String UserGUID;
    /**
     * 默认传值ApiNormal
     */
    private String Model;
    /**
     * Api指令
     */
    private String Method;
    /**
     * Api版本号
     */
    private int Version;
    /**
     * 终端类型
     * 0 = PC版服务端
     * 1 = PC平板
     * 11= 小店通
     * 20= 一体机安卓客户端
     * 21= V1
     * 22= 云平板
     * 23=M1
     * 24=P1
     * 25=KDS
     */
    private int TerminalType = 21;
    /**
     * 终端设备硬件唯一标识
     */
    private String TerminalID;
    /**
     * 请求服务端返回的数据格式
     * 0 = xml
     * 1 = json
     */
    private String BackType;
    /**
     * 测试状态  true = 测试，false = 非测试
     */
    private String IsTest;

    /**
     * 商户号
     */
    private String MerID;

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public int getVersion() {
        return Version;
    }

    public String getUserGUID() {
        return UserGUID;
    }

    public void setUserGUID(String userGUID) {
        UserGUID = userGUID;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public int getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(int terminalType) {
        TerminalType = terminalType;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public String getBackType() {
        return BackType;
    }

    public void setBackType(String backType) {
        BackType = backType;
    }

    public String getIsTest() {
        return IsTest;
    }

    public void setIsTest(String isTest) {
        IsTest = isTest;
    }

    public String getMerID() {
        return MerID;
    }

    public void setMerID(String merID) {
        MerID = merID;
    }
}
