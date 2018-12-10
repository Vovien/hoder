package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class PrinterE {

    private Long id;
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 网络打印机标识
     */
    private String PrinterGUID;

    /**
     * 终端设备ID
     */
    private String DeviceID;

    /**
     * 是否返回打印机控制的菜品
     * 0=不返回菜品
     * 1=需要返回
     */
    private int ReturnDishes;

    /**
     * 打印机类别标识，从PrinterTypeE中获取10=点菜打印机
     * 20=算账打印机
     * 30=收银打印机
     * 100=厨房打印机
     */
    private String PrinterTypeUID;

    /**
     * 打印机名称
     */
    private String Name;
    private String DishesName;

    /**
     * 0=网络打印机
     * 1=本地打印机
     */
    private int OnLocal;

    /**
     * IP地址
     */
    private String IP;

    /**
     * 端口
     */
    private int Port;

    /**
     * 纸张尺寸，仅传入58,80两种值
     */
    private String Paper;
    /**
     * 0=关闭
     * 1=开启
     */
    private int PrintEnable;

    /**
     * 备注说明
     */
    private String Remark;

    /**
     * 打印次数
     */
    private int PrintTimes;
    /**
     * 完成数量
     */
    private int PrintFinishTimes;

    /**
     * 打印数据标识
     */
    private String PrintKey;

    private List<String> ArrayOfPrintKey;
    /**
     * 0=整单打印    1=分菜打印
     */
    private int PrintModel;

    /**
     * 打印机控制菜品集合
     */
    private List<DishesE> ArrayOfDishesE;
    /**
     * 打印数据集合
     */
    private List<PrintRowE> ArrayOfPrintRowE;

    private Long PullPrintDataTimeStamp;

    public Long getPullPrintDataTimeStamp() {
        return PullPrintDataTimeStamp;
    }

    public void setPullPrintDataTimeStamp(Long pullPrintDataTimeStamp) {
        PullPrintDataTimeStamp = pullPrintDataTimeStamp;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public List<String> getArrayOfPrintKey() {
        return ArrayOfPrintKey;
    }

    public void setArrayOfPrintKey(List<String> arrayOfPrintKey) {
        ArrayOfPrintKey = arrayOfPrintKey;
    }

    public List<PrintRowE> getArrayOfPrintRowE() {
        return ArrayOfPrintRowE;
    }

    public void setArrayOfPrintRowE(List<PrintRowE> arrayOfPrintRowE) {
        ArrayOfPrintRowE = arrayOfPrintRowE;
    }

    public int getPrintFinishTimes() {
        return PrintFinishTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrintFinishTimes(int printFinishTimes) {
        PrintFinishTimes = printFinishTimes;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public int getPrintModel() {
        return PrintModel;
    }

    public void setPrintModel(int printModel) {
        PrintModel = printModel;
    }

    public String getDishesName() {
        return DishesName;
    }

    public void setDishesName(String dishesName) {
        DishesName = dishesName;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public int getPrintEnable() {
        return PrintEnable;
    }

    public void setPrintEnable(int printEnable) {
        PrintEnable = printEnable;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getPrinterGUID() {
        return PrinterGUID;
    }

    public void setPrinterGUID(String printerGUID) {
        PrinterGUID = printerGUID;
    }

    public String getDeviceGUID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public int getReturnDishes() {
        return ReturnDishes;
    }

    public void setReturnDishes(int returnDishes) {
        ReturnDishes = returnDishes;
    }

    public String getPrinterTypeUID() {
        return PrinterTypeUID;
    }

    public void setPrinterTypeUID(String printerTypeUID) {
        PrinterTypeUID = printerTypeUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getOnLocal() {
        return OnLocal;
    }

    public void setOnLocal(int onLocal) {
        OnLocal = onLocal;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public String getPaper() {
        return Paper;
    }

    public void setPaper(String paper) {
        Paper = paper;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getPrintTimes() {
        return PrintTimes;
    }

    public void setPrintTimes(int printTimes) {
        PrintTimes = printTimes;
    }

    public String getPrintKey() {
        return PrintKey;
    }

    public void setPrintKey(String printKey) {
        PrintKey = printKey;
    }

    public List<DishesE> getArrayOfDishesE() {
        return ArrayOfDishesE;
    }

    public void setArrayOfDishesE(List<DishesE> arrayOfDishesE) {
        ArrayOfDishesE = arrayOfDishesE;
    }
}
