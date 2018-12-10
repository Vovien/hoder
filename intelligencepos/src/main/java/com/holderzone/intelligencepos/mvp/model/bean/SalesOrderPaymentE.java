package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/16.
 */

public class SalesOrderPaymentE extends SalesOrderPayment implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 收款记录标识
     */
    private String SalesOrderPaymentGUID;

    /**
     * 收款金额,非必填，现金类交易使用
     */
    private Double ReceivedMoney;

    /**
     * 找零金额,非必填，现金类交易使用
     */
    private Double RepayMoney;

    /**
     * 交易流水号
     */
    private String TransactionNumber;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 支付名称
     */
    private String PaymentItemName;

    /**
     * 会员卡ID
     */
    private String CardsChipNo;

    /**
     * 实际支付金额
     */
    private Integer ActuallyAmountSummary;

    /**
     * 折扣金额
     */
    private Integer DiscountAmountSummary;

    /**
     * 自增ID
     */
    private Long SalesOrderPaymentID;

    /**
     * 编号
     */
    private String SalesOrderPaymentUID;

    private Double DiscountAmount;

    /**
     * 付款记录创建时间
     */
    private String CreateTime;

    /**
     * 付款记录创建时间时间戳
     */
    private Long CreateTimeStamp;

    /**
     * 排序
     */
    private Integer Sort;

    /**
     * 最后同步ID
     */
    private Integer LastUID;

    /**
     * 同步时间
     */
    private Integer LastStamp;

    /**
     * 退款金额合计
     */
    private Double RefundAmount;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 会员密码
     */
    private String MemberPassWord;

    /**
     * 退款操作人
     */
    private String RefundStaffGUID;

    /**
     * 收款人姓名
     */
    private String CreateStaffName;

    /**
     * 收款笔数
     */
    private Double Quantity;

    /**
     * 用券数量
     */
    private Integer UseCount;
    /**
     * 退款GUID
     */
    private String SalesOrderRefundGUID;

    /**
     * 会员余额/卡消费
     * 0：会员卡
     * 1：账户余额
     */
    private int useMemberBlance;

    /**
     * 卡号或者会员GUID
     */
    private String content;

    /**
     * 何师会员密码
     */
    private String payPassword;

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUseMemberBlance() {
        return useMemberBlance;
    }

    public void setUseMemberBlance(int useMemberBlance) {
        this.useMemberBlance = useMemberBlance;
    }

    public String getSalesOrderRefundGUID() {
        return SalesOrderRefundGUID;
    }

    public void setSalesOrderRefundGUID(String salesOrderRefundGUID) {
        SalesOrderRefundGUID = salesOrderRefundGUID;
    }

    public String getCreateStaffName() {
        return CreateStaffName;
    }

    public void setCreateStaffName(String createStaffName) {
        CreateStaffName = createStaffName;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getSalesOrderPaymentGUID() {
        return SalesOrderPaymentGUID;
    }

    public void setSalesOrderPaymentGUID(String salesOrderPaymentGUID) {
        SalesOrderPaymentGUID = salesOrderPaymentGUID;
    }

    public Double getReceivedMoney() {
        return ReceivedMoney;
    }

    public void setReceivedMoney(Double receivedMoney) {
        ReceivedMoney = receivedMoney;
    }

    public Double getRepayMoney() {
        return RepayMoney;
    }

    public void setRepayMoney(Double repayMoney) {
        RepayMoney = repayMoney;
    }

    public String getTransactionNumber() {
        return TransactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        TransactionNumber = transactionNumber;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getPaymentItemName() {
        return PaymentItemName;
    }

    public void setPaymentItemName(String paymentItemName) {
        PaymentItemName = paymentItemName;
    }

    public String getCardsChipNo() {
        return CardsChipNo;
    }

    public void setCardsChipNo(String cardsChipNo) {
        CardsChipNo = cardsChipNo;
    }

    public Integer getActuallyAmountSummary() {
        return ActuallyAmountSummary;
    }

    public void setActuallyAmountSummary(Integer actuallyAmountSummary) {
        ActuallyAmountSummary = actuallyAmountSummary;
    }

    public Integer getDiscountAmountSummary() {
        return DiscountAmountSummary;
    }

    public void setDiscountAmountSummary(Integer discountAmountSummary) {
        DiscountAmountSummary = discountAmountSummary;
    }

    public Long getSalesOrderPaymentID() {
        return SalesOrderPaymentID;
    }

    public void setSalesOrderPaymentID(Long salesOrderPaymentID) {
        SalesOrderPaymentID = salesOrderPaymentID;
    }

    public String getSalesOrderPaymentUID() {
        return SalesOrderPaymentUID;
    }

    public void setSalesOrderPaymentUID(String salesOrderPaymentUID) {
        SalesOrderPaymentUID = salesOrderPaymentUID;
    }

    public Double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public Long getCreateTimeStamp() {
        return CreateTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        CreateTimeStamp = createTimeStamp;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public Integer getLastUID() {
        return LastUID;
    }

    public void setLastUID(Integer lastUID) {
        LastUID = lastUID;
    }

    public Integer getLastStamp() {
        return LastStamp;
    }

    public void setLastStamp(Integer lastStamp) {
        LastStamp = lastStamp;
    }

    public Double getRefundAmount() {
        return RefundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        RefundAmount = refundAmount;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getMemberPassWord() {
        return MemberPassWord;
    }

    public void setMemberPassWord(String memberPassWord) {
        MemberPassWord = memberPassWord;
    }

    public String getRefundStaffGUID() {
        return RefundStaffGUID;
    }

    public void setRefundStaffGUID(String refundStaffGUID) {
        RefundStaffGUID = refundStaffGUID;
    }

    public Integer getUseCount() {
        return UseCount;
    }

    public void setUseCount(Integer useCount) {
        UseCount = useCount;
    }

    public SalesOrderPaymentE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.SalesOrderPaymentGUID);
        dest.writeValue(this.ReceivedMoney);
        dest.writeValue(this.RepayMoney);
        dest.writeString(this.TransactionNumber);
        dest.writeString(this.Remark);
        dest.writeString(this.PaymentItemName);
        dest.writeString(this.CardsChipNo);
        dest.writeValue(this.ActuallyAmountSummary);
        dest.writeValue(this.DiscountAmountSummary);
        dest.writeValue(this.SalesOrderPaymentID);
        dest.writeString(this.SalesOrderPaymentUID);
        dest.writeValue(this.DiscountAmount);
        dest.writeString(this.CreateTime);
        dest.writeValue(this.CreateTimeStamp);
        dest.writeValue(this.Sort);
        dest.writeValue(this.LastUID);
        dest.writeValue(this.LastStamp);
        dest.writeValue(this.RefundAmount);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.MemberPassWord);
        dest.writeString(this.RefundStaffGUID);
        dest.writeString(this.CreateStaffName);
        dest.writeValue(this.Quantity);
        dest.writeValue(this.UseCount);
        dest.writeString(this.SalesOrderRefundGUID);
        dest.writeInt(this.useMemberBlance);
        dest.writeString(this.content);
        dest.writeString(this.payPassword);
    }

    protected SalesOrderPaymentE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.SalesOrderPaymentGUID = in.readString();
        this.ReceivedMoney = (Double) in.readValue(Double.class.getClassLoader());
        this.RepayMoney = (Double) in.readValue(Double.class.getClassLoader());
        this.TransactionNumber = in.readString();
        this.Remark = in.readString();
        this.PaymentItemName = in.readString();
        this.CardsChipNo = in.readString();
        this.ActuallyAmountSummary = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DiscountAmountSummary = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrderPaymentID = (Long) in.readValue(Long.class.getClassLoader());
        this.SalesOrderPaymentUID = in.readString();
        this.DiscountAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.CreateTime = in.readString();
        this.CreateTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.LastUID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.LastStamp = (Integer) in.readValue(Integer.class.getClassLoader());
        this.RefundAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.StoreGUID = in.readString();
        this.MemberPassWord = in.readString();
        this.RefundStaffGUID = in.readString();
        this.CreateStaffName = in.readString();
        this.Quantity = (Double) in.readValue(Double.class.getClassLoader());
        this.UseCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrderRefundGUID = in.readString();
        this.useMemberBlance = in.readInt();
        this.content = in.readString();
        this.payPassword = in.readString();
    }

    public static final Creator<SalesOrderPaymentE> CREATOR = new Creator<SalesOrderPaymentE>() {
        @Override
        public SalesOrderPaymentE createFromParcel(Parcel source) {
            return new SalesOrderPaymentE(source);
        }

        @Override
        public SalesOrderPaymentE[] newArray(int size) {
            return new SalesOrderPaymentE[size];
        }
    };
}

