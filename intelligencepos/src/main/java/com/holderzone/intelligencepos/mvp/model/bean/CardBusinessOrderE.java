package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by NEW on 2017/4/17.
 */

public class CardBusinessOrderE {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 会员标识
     */
    private String MemberInfoGUID;
    /**
     * 卡标识  卡芯片编码
     */
    private String CardsChipNo;
    /**
     * 卡类型标识
     */
    private String CardTypeGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;
    /**
     * 支付方式     支付方式编码
     */
    private String PaymentItemCode;
    /**
     * 交易流水号   目前保存银行卡交易流水号
     */
    private String PayUID;
    /**
     * 操作员标识
     */
    private String OperationUsersGUID;
    /**
     * 售卡业务员标识
     */
    private String BusinessUsersGUID;
    /**
     * 客户端业务请求标识     由客户端生成，凭此标识可查询实体，建议使用GUID格式
     */
    private String BusTradeNo;

    /**
     * 数据标识
     */
    private String CardBusinessOrderGUID;

    /**
     * 业务类型    1=充值   -1=支付
     */
    private int PayAmountType;
    /**
     * 金额
     * 业务发生金额
     * 充值：记录充值的收款金额
     * 刷卡：记录卡扣点金额
     */
    private double Amount;
    /**
     * 记录状态    0=处理中  1=处理成功  0≤ = 异常
     */
    private int OrderState;
    /**
     * 业务终端类型
     */
    private String TerminalType;
    /**
     * 业务终端标识    硬件标识ID
     */
    private String TerminalID;
    /**
     * 业务发生时间    格式：yyyy-MM-dd HH:mm:ss
     */
    private String AddTime;
    /**
     * 支付方式类型
     * 0=现金、银行卡 等线下交易，已收款
     * 1=刷卡支付 线上交易，未实时收款
     * 2=扫码支付 线上交易，未实时收款
     */
    private int PayType;
    /**
     * 卡余额     业务发生后的余额
     */
    private double CardsBalance;
    /**
     * 支付状态     0=待支付  1=支付成功到账
     */
    private int PayState;
    /**
     * 线上支付授权码    支付宝、微信生成的授权码，即条码、二维码
     */
    private String Auth_code;
    /**
     * 线上支付反馈的交易标识    支付宝、微信反馈的数据标识
     */
    private String ThirdTradeNo;
    /**
     * 赠送金额
     */
    private double GiftAmount;
    /**
     * 卡名称
     */
    private String CardName;
    /**
     * 卡余额
     */
    private double Balance;
    /**
     * 设备ID
     */
    private String DeviceID;

    /**
     * 数据标识
     */
    private String CardSellOrderGUID;

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public double getGiftAmount() {
        return GiftAmount;
    }

    public void setGiftAmount(double giftAmount) {
        GiftAmount = giftAmount;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getMemberInfoGUID() {
        return MemberInfoGUID;
    }

    public void setMemberInfoGUID(String memberInfoGUID) {
        MemberInfoGUID = memberInfoGUID;
    }

    public String getCardsChipNo() {
        return CardsChipNo;
    }

    public void setCardsChipNo(String cardsChipNo) {
        CardsChipNo = cardsChipNo;
    }

    public String getCardTypeGUID() {
        return CardTypeGUID;
    }

    public void setCardTypeGUID(String cardTypeGUID) {
        CardTypeGUID = cardTypeGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getPaymentItemCode() {
        return PaymentItemCode;
    }

    public void setPaymentItemCode(String paymentItemCode) {
        PaymentItemCode = paymentItemCode;
    }

    public String getPayUID() {
        return PayUID;
    }

    public void setPayUID(String payUID) {
        PayUID = payUID;
    }

    public String getOperationUsersGUID() {
        return OperationUsersGUID;
    }

    public void setOperationUsersGUID(String operationUsersGUID) {
        OperationUsersGUID = operationUsersGUID;
    }

    public String getBusinessUsersGUID() {
        return BusinessUsersGUID;
    }

    public void setBusinessUsersGUID(String businessUsersGUID) {
        BusinessUsersGUID = businessUsersGUID;
    }

    public String getBusTradeNo() {
        return BusTradeNo;
    }

    public void setBusTradeNo(String busTradeNo) {
        BusTradeNo = busTradeNo;
    }

    public String getCardBusinessOrderGUID() {
        return CardBusinessOrderGUID;
    }

    public void setCardBusinessOrderGUID(String cardBusinessOrderGUID) {
        CardBusinessOrderGUID = cardBusinessOrderGUID;
    }

    public int getPayAmountType() {
        return PayAmountType;
    }

    public void setPayAmountType(int payAmountType) {
        PayAmountType = payAmountType;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getOrderState() {
        return OrderState;
    }

    public void setOrderState(int orderState) {
        OrderState = orderState;
    }

    public String getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(String terminalType) {
        TerminalType = terminalType;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public int getPayType() {
        return PayType;
    }

    public void setPayType(int payType) {
        PayType = payType;
    }

    public double getCardsBalance() {
        return CardsBalance;
    }

    public void setCardsBalance(double cardsBalance) {
        CardsBalance = cardsBalance;
    }

    public int getPayState() {
        return PayState;
    }

    public void setPayState(int payState) {
        PayState = payState;
    }

    public String getAuth_code() {
        return Auth_code;
    }

    public void setAuth_code(String auth_code) {
        Auth_code = auth_code;
    }

    public String getThirdTradeNo() {
        return ThirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        ThirdTradeNo = thirdTradeNo;
    }

    public String getCardSellOrderGUID() {
        return CardSellOrderGUID;
    }

    public void setCardSellOrderGUID(String cardSellOrderGUID) {
        CardSellOrderGUID = cardSellOrderGUID;
    }
}
