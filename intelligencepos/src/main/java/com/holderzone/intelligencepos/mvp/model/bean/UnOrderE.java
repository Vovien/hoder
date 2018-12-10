package com.holderzone.intelligencepos.mvp.model.bean;

import com.google.zxing.LuminanceSource;

import java.util.List;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderE extends UnOrder {
    /**
     * 消息类型
     * 外卖==>智慧门店
     * 1010：新订单消息
     * 1020：催单标识
     * 1030：退单标识
     * 1110：状态变更
     * 智慧门店==>外卖
     * 2010：拒单
     * 2011：确认接单
     * 2020：催单回复
     * 2030：不同意退单
     * 2031：同意退单
     */
    private String MsgType;

    /**
     * 订单原始数据
     */
    private String SourceData;

    /**
     * 设备ID
     */
    private String DeviceID;

    /**
     * 队列添加时间戳
     */
    private Long AddTimeStamp;
    /**
     * 是否返回菜品明细信息  0=否（默认），1=是
     */
    private Integer ReturnDishes;
    /**
     * 是否返回折扣信息 0=否（默认），1=是
     */
    private Integer ReturnDiscount;
    /**
     * 菜品明细
     */
    private List<UnOrderDishesE> ArrayOfUnOrderDishesE;
    /**
     * 订单类型 中文描述
     */
    private String OrderSubTypeCn;
    /**
     * 订单状态 中文描述
     */
    private String OrderStatusCn;

    /**
     * 订单序号
     */
    private Integer OrderNumber;
    /**
     * 下单状态   0 未下单    1 已下单
     */
    private Integer IsSalesOrder;

    private DiningTableE DiningTableE;

    /**
     * 餐桌名字
     */
    private String DiningTableName;
    /**
     * 折扣明细
     */
    private List<UnOrderDiscountE> ArrayOfUnOrderDiscountE;
    private String UnOrderReceiveMsgGUID;
    private List<UnReminderReplyContentE> ArrayOfUnReminderReplyContentE;
    /**
     * 创建日期
     */
    private String CreateOrderTime;

    /**
     * 商品数量
     */
    private Integer DishesCount;

    /**
     * 操作人GUID
     */
    private String UserGUID;

    /**
     * 获取到的最后一条消息的时间戳
     */
    private Long MsgTimeStamp;

    /**
     * 消息时间
     */
    private String MsgTime;

    /**
     * 接单时间
     */
    private String TimeCn;
    /**
     * 是否检查能不能退菜
     */
    private boolean CheckMake;

    public boolean isCheckMake() {
        return CheckMake;
    }

    public void setCheckMake(boolean checkMake) {
        CheckMake = checkMake;
    }

    public List<UnOrderDiscountE> getArrayOfUnOrderDiscountE() {
        return ArrayOfUnOrderDiscountE;
    }

    public void setArrayOfUnOrderDiscountE(List<UnOrderDiscountE> arrayOfUnOrderDiscountE) {
        ArrayOfUnOrderDiscountE = arrayOfUnOrderDiscountE;
    }

    public String getUnOrderReceiveMsgGUID() {
        return UnOrderReceiveMsgGUID;
    }

    public void setUnOrderReceiveMsgGUID(String unOrderReceiveMsgGUID) {
        UnOrderReceiveMsgGUID = unOrderReceiveMsgGUID;
    }

    public List<UnReminderReplyContentE> getArrayOfUnReminderReplyContentE() {
        return ArrayOfUnReminderReplyContentE;
    }

    public void setArrayOfUnReminderReplyContentE(List<UnReminderReplyContentE> arrayOfUnReminderReplyContentE) {
        ArrayOfUnReminderReplyContentE = arrayOfUnReminderReplyContentE;
    }

    public String getCreateOrderTime() {
        return CreateOrderTime;
    }

    public void setCreateOrderTime(String createOrderTime) {
        CreateOrderTime = createOrderTime;
    }

    public Integer getDishesCount() {
        return DishesCount;
    }

    public void setDishesCount(Integer dishesCount) {
        DishesCount = dishesCount;
    }

    public String getUserGUID() {
        return UserGUID;
    }

    public void setUserGUID(String userGUID) {
        UserGUID = userGUID;
    }

    public Long getMsgTimeStamp() {
        return MsgTimeStamp;
    }

    public void setMsgTimeStamp(Long msgTimeStamp) {
        MsgTimeStamp = msgTimeStamp;
    }

    public String getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(String msgTime) {
        MsgTime = msgTime;
    }

    public String getTimeCn() {
        return TimeCn;
    }

    public void setTimeCn(String timeCn) {
        TimeCn = timeCn;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getSourceData() {
        return SourceData;
    }

    public void setSourceData(String sourceData) {
        SourceData = sourceData;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public Long getAddTimeStamp() {
        return AddTimeStamp;
    }

    public void setAddTimeStamp(Long addTimeStamp) {
        AddTimeStamp = addTimeStamp;
    }

    public Integer getReturnDishes() {
        return ReturnDishes;
    }

    public void setReturnDishes(Integer returnDishes) {
        ReturnDishes = returnDishes;
    }

    public Integer getReturnDiscount() {
        return ReturnDiscount;
    }

    public void setReturnDiscount(Integer returnDiscount) {
        ReturnDiscount = returnDiscount;
    }

    public List<UnOrderDishesE> getArrayOfUnOrderDishesE() {
        return ArrayOfUnOrderDishesE;
    }

    public void setArrayOfUnOrderDishesE(List<UnOrderDishesE> arrayOfUnOrderDishesE) {
        ArrayOfUnOrderDishesE = arrayOfUnOrderDishesE;
    }

    public String getOrderSubTypeCn() {
        return OrderSubTypeCn;
    }

    public void setOrderSubTypeCn(String orderSubTypeCn) {
        OrderSubTypeCn = orderSubTypeCn;
    }

    public String getOrderStatusCn() {
        return OrderStatusCn;
    }

    public void setOrderStatusCn(String orderStatusCn) {
        OrderStatusCn = orderStatusCn;
    }

    public Integer getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        OrderNumber = orderNumber;
    }

    public Integer getIsSalesOrder() {
        return IsSalesOrder;
    }

    public void setIsSalesOrder(Integer isSalesOrder) {
        IsSalesOrder = isSalesOrder;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.DiningTableE getDiningTableE() {
        return DiningTableE;
    }

    public void setDiningTableE(com.holderzone.intelligencepos.mvp.model.bean.DiningTableE diningTableE) {
        DiningTableE = diningTableE;
    }

    public String getDiningTableName() {
        return DiningTableName;
    }

    public void setDiningTableName(String diningTableName) {
        DiningTableName = diningTableName;
    }
}
