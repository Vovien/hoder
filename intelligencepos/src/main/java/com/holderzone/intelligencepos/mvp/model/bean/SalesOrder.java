package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.mvp.model.bean.db.Users;
import com.holderzone.intelligencepos.utils.ArithUtil;

import java.util.ArrayList;
import java.util.List;

/*
* 表说明：销售记录表
* 备注：一个销售单作为一个记录，可支持1单多桌
*/
public class SalesOrder implements Parcelable {

    public SalesOrder() {
        DiscountRatio = 1.0;
        DiscountTypeID = -1;
        ActuallyInsertStatus = 0;
        CancelTime = "1900-01-01";
    }

    /**
     * 数据标识
     */

    private String SalesOrderGUID;

    /**
     * 会员手机
     */
    private String RegTel;

    /**
     * 流水号   由系统生成一单一号
     */

    private String SerialNumber;
    private boolean isMemberPrice;

    /**
     * 原始纸张单号  手工录入
     */

    private String OrgNumber;

    /**
     * 门店标识
     */

    private String StoreGUID;

    /**
     * 客人数量
     */

    private Integer GuestCount;

    /**
     * 餐桌数据标识
     */

    private String DiningTableGUID;

    /**
     * 下单时间
     */

    private String CreateTime;

    /**
     * 下单操作员标识
     */

    private String CreateStaffGUID;

    private Users CreateStaff;

    /**
     * 结算操作人员标识
     */
    private String CheckOutStaffGUID;

    private Users CheckOutStaff;
    /**
     * 结账时间
     */
    private String ChecOutkTime;

    /**
     * 作废操作人
     */
    private String CancelStaffGUID;

    private Users CancelStaff;

    /**
     * 作废时间
     */
    private String CancelTime;

    /**
     * 订单状态（状态 -1=该单作废  0=消费中 9=已买单，未离桌  10=买单离桌，桌清空
     * ）
     */
    private Integer OrderStat;

    /**
     * 结算金额(应收)
     */
    private Double CheckTotal;

    /**
     * 消费合计   (消费合计 = 实际付合计 + 折扣合计 - 退款合计)
     */
    private Double ConsumeTotal;

    /**
     * 用户提交金额的状态 0=未提交  1=提交
     */
    private Integer ActuallyInsertStatus;

    ///实际付合计(已收)
    private Double ActuallyPayTotal;

    /**
     * 用户指定的实收款
     */
    private Double ActuallyInsert;

    ///实际退款合计
    private Double RefundTotal;

    /**
     * 折扣合计
     */

    private Double DiscountTotal;

    /**
     * 单据锁定  0=未锁定  1=锁定
     */
    /// <summary>
    /// 应收合计，消费总额 - 折扣
    /// </summary>
    /// <returns></returns>
    private double Get应收合计() {
        return ArithUtil.sub(ConsumeTotal, DiscountTotal);
    }

    private Integer Lock;

    /**
     * 锁定备注
     */

    private String LockRemark;

    /**
     * 交易模式   0=堂食模式  1=快销
     */
    private Integer TradeMode;

    /**
     * 整单折扣比例   1=100%无折扣  0.95=95折
     */
    private Double DiscountRatio;

    /**
     * 整单折扣备注
     */
    private String DiscountRatioRemark;

    /**
     * 整单折扣业务类别  -1=无折扣,详见DiscountType枚举定义
     */
    private Integer DiscountTypeID;

    /**
     * 销售类别
     */

    private String SalesOrderTypeCode;

    /**
     * --付款状态  0=未付  1=付款完成
     */

    private Integer PaymentStat;

    /**
     * 接待服务员
     */

    private String ReceptionStaffGUID;

    private Users ReceptionStaff;

    /**
     * 买单服务员
     */

    private String CheckStaffGUID;

    private Users CheckStaff;

    /**
     * 收款人
     */
    private String PayeeStaffGUID;

    private Users PayeeStaff;

    /**
     * 数据上传状态   -1=上传失败,0=未上传,1=上传成功
     */

    private Integer SalesOrderUploadSTAT;

    /**
     * 营业日
     */

    private String BusinessDay;

    private String MergeSalesOrderGUID;

    /**
     * 订单下的菜品列表
     */
    private List<SalesOrderDishes> ArrayofSalesOrderDishes;

    /**
     * 出堂批次
     */
    private List<SalesOrderBatch> ArrayofSalesOrderBatch;

    /**
     * 出堂批次菜品明细
     */
    private List<SalesOrderBatchDishes> ArrayofSalesOrderBatchDishes;

    /**
     * 是否重新结账标识 1=重新结账， 0=正常结账
     */
    private Integer IsReCheck;

    /**
     * 结算单打印次数
     */
    private Integer CalculationPrintCount;

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getOrgNumber() {
        return OrgNumber;
    }

    public void setOrgNumber(String orgNumber) {
        OrgNumber = orgNumber;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public Integer getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(Integer guestCount) {
        GuestCount = guestCount;
    }

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getCreateStaffGUID() {
        return CreateStaffGUID;
    }

    public void setCreateStaffGUID(String createStaffGUID) {
        CreateStaffGUID = createStaffGUID;
    }

    public Users getCreateStaff() {
        return CreateStaff;
    }

    public void setCreateStaff(Users createStaff) {
        CreateStaff = createStaff;
    }

    public String getCheckOutStaffGUID() {
        return CheckOutStaffGUID;
    }

    public void setCheckOutStaffGUID(String checkOutStaffGUID) {
        CheckOutStaffGUID = checkOutStaffGUID;
    }

    public Users getCheckOutStaff() {
        return CheckOutStaff;
    }

    public void setCheckOutStaff(Users checkOutStaff) {
        CheckOutStaff = checkOutStaff;
    }

    public String getChecOutkTime() {
        return ChecOutkTime;
    }

    public void setChecOutkTime(String checOutkTime) {
        ChecOutkTime = checOutkTime;
    }

    public String getCancelStaffGUID() {
        return CancelStaffGUID;
    }

    public void setCancelStaffGUID(String cancelStaffGUID) {
        CancelStaffGUID = cancelStaffGUID;
    }

    public Users getCancelStaff() {
        return CancelStaff;
    }

    public void setCancelStaff(Users cancelStaff) {
        CancelStaff = cancelStaff;
    }

    public String getRegTel() {
        return RegTel;
    }

    public void setRegTel(String regTel) {
        RegTel = regTel;
    }

    public String getCancelTime() {
        return CancelTime;
    }

    public void setCancelTime(String cancelTime) {
        CancelTime = cancelTime;
    }

    public Integer getOrderStat() {
        return OrderStat;
    }

    public void setOrderStat(Integer orderStat) {
        OrderStat = orderStat;
    }

    public Double getCheckTotal() {
        return CheckTotal;
    }

    public void setCheckTotal(Double checkTotal) {
        CheckTotal = checkTotal;
    }

    public Double getConsumeTotal() {
        return ConsumeTotal;
    }

    public void setConsumeTotal(Double consumeTotal) {
        ConsumeTotal = consumeTotal;
    }

    public Integer getActuallyInsertStatus() {
        return ActuallyInsertStatus;
    }

    public void setActuallyInsertStatus(Integer actuallyInsertStatus) {
        ActuallyInsertStatus = actuallyInsertStatus;
    }

    public Double getActuallyPayTotal() {
        return ActuallyPayTotal;
    }

    public void setActuallyPayTotal(Double actuallyPayTotal) {
        ActuallyPayTotal = actuallyPayTotal;
    }

    public Double getActuallyInsert() {
        return ActuallyInsert;
    }

    public void setActuallyInsert(Double actuallyInsert) {
        ActuallyInsert = actuallyInsert;
    }

    public Double getRefundTotal() {
        return RefundTotal;
    }

    public void setRefundTotal(Double refundTotal) {
        RefundTotal = refundTotal;
    }

    public Double getDiscountTotal() {
        return DiscountTotal;
    }

    public void setDiscountTotal(Double discountTotal) {
        DiscountTotal = discountTotal;
    }

    public Integer getLock() {
        return Lock;
    }

    public void setLock(Integer lock) {
        Lock = lock;
    }

    public String getLockRemark() {
        return LockRemark;
    }

    public void setLockRemark(String lockRemark) {
        LockRemark = lockRemark;
    }

    public Integer getTradeMode() {
        return TradeMode;
    }

    public void setTradeMode(Integer tradeMode) {
        TradeMode = tradeMode;
    }

    public Double getDiscountRatio() {
        return DiscountRatio;
    }

    public void setDiscountRatio(Double discountRatio) {
        DiscountRatio = discountRatio;
    }

    public String getDiscountRatioRemark() {
        return DiscountRatioRemark;
    }

    public void setDiscountRatioRemark(String discountRatioRemark) {
        DiscountRatioRemark = discountRatioRemark;
    }

    public Integer getDiscountTypeID() {
        return DiscountTypeID;
    }

    public void setDiscountTypeID(Integer discountTypeID) {
        DiscountTypeID = discountTypeID;
    }

    public String getSalesOrderTypeCode() {
        return SalesOrderTypeCode;
    }

    public boolean isMemberPrice() {
        return isMemberPrice;
    }

    public void setMemberPrice(boolean memberPrice) {
        isMemberPrice = memberPrice;
    }

    public void setSalesOrderTypeCode(String salesOrderTypeCode) {
        SalesOrderTypeCode = salesOrderTypeCode;
    }

    public Integer getPaymentStat() {
        return PaymentStat;
    }

    public void setPaymentStat(Integer paymentStat) {
        PaymentStat = paymentStat;
    }

    public String getReceptionStaffGUID() {
        return ReceptionStaffGUID;
    }

    public void setReceptionStaffGUID(String receptionStaffGUID) {
        ReceptionStaffGUID = receptionStaffGUID;
    }

    public Users getReceptionStaff() {
        return ReceptionStaff;
    }

    public void setReceptionStaff(Users receptionStaff) {
        ReceptionStaff = receptionStaff;
    }

    public String getCheckStaffGUID() {
        return CheckStaffGUID;
    }

    public void setCheckStaffGUID(String checkStaffGUID) {
        CheckStaffGUID = checkStaffGUID;
    }

    public Users getCheckStaff() {
        return CheckStaff;
    }

    public void setCheckStaff(Users checkStaff) {
        CheckStaff = checkStaff;
    }

    public String getPayeeStaffGUID() {
        return PayeeStaffGUID;
    }

    public void setPayeeStaffGUID(String payeeStaffGUID) {
        PayeeStaffGUID = payeeStaffGUID;
    }

    public Users getPayeeStaff() {
        return PayeeStaff;
    }

    public void setPayeeStaff(Users payeeStaff) {
        PayeeStaff = payeeStaff;
    }

    public Integer getSalesOrderUploadSTAT() {
        return SalesOrderUploadSTAT;
    }

    public void setSalesOrderUploadSTAT(Integer salesOrderUploadSTAT) {
        SalesOrderUploadSTAT = salesOrderUploadSTAT;
    }

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public String getMergeSalesOrderGUID() {
        return MergeSalesOrderGUID;
    }

    public void setMergeSalesOrderGUID(String mergeSalesOrderGUID) {
        MergeSalesOrderGUID = mergeSalesOrderGUID;
    }

    public List<SalesOrderDishes> getArrayofSalesOrderDishes() {
        return ArrayofSalesOrderDishes;
    }

    public void setArrayofSalesOrderDishes(List<SalesOrderDishes> arrayofSalesOrderDishes) {
        ArrayofSalesOrderDishes = arrayofSalesOrderDishes;
    }

    public List<SalesOrderBatch> getArrayofSalesOrderBatch() {
        return ArrayofSalesOrderBatch;
    }

    public void setArrayofSalesOrderBatch(List<SalesOrderBatch> arrayofSalesOrderBatch) {
        ArrayofSalesOrderBatch = arrayofSalesOrderBatch;
    }

    public List<SalesOrderBatchDishes> getArrayofSalesOrderBatchDishes() {
        return ArrayofSalesOrderBatchDishes;
    }

    public void setArrayofSalesOrderBatchDishes(List<SalesOrderBatchDishes> arrayofSalesOrderBatchDishes) {
        ArrayofSalesOrderBatchDishes = arrayofSalesOrderBatchDishes;
    }

    public Integer getIsReCheck() {
        return IsReCheck;
    }

    public void setIsReCheck(Integer isReCheck) {
        IsReCheck = isReCheck;
    }

    public Integer getCalculationPrintCount() {
        return CalculationPrintCount;
    }

    public void setCalculationPrintCount(Integer calculationPrintCount) {
        CalculationPrintCount = calculationPrintCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SalesOrderGUID);
        dest.writeString(this.RegTel);
        dest.writeString(this.SerialNumber);
        dest.writeByte(this.isMemberPrice ? (byte) 1 : (byte) 0);
        dest.writeString(this.OrgNumber);
        dest.writeString(this.StoreGUID);
        dest.writeValue(this.GuestCount);
        dest.writeString(this.DiningTableGUID);
        dest.writeString(this.CreateTime);
        dest.writeString(this.CreateStaffGUID);
        dest.writeParcelable(this.CreateStaff, flags);
        dest.writeString(this.CheckOutStaffGUID);
        dest.writeParcelable(this.CheckOutStaff, flags);
        dest.writeString(this.ChecOutkTime);
        dest.writeString(this.CancelStaffGUID);
        dest.writeParcelable(this.CancelStaff, flags);
        dest.writeString(this.CancelTime);
        dest.writeValue(this.OrderStat);
        dest.writeValue(this.CheckTotal);
        dest.writeValue(this.ConsumeTotal);
        dest.writeValue(this.ActuallyInsertStatus);
        dest.writeValue(this.ActuallyPayTotal);
        dest.writeValue(this.ActuallyInsert);
        dest.writeValue(this.RefundTotal);
        dest.writeValue(this.DiscountTotal);
        dest.writeValue(this.Lock);
        dest.writeString(this.LockRemark);
        dest.writeValue(this.TradeMode);
        dest.writeValue(this.DiscountRatio);
        dest.writeString(this.DiscountRatioRemark);
        dest.writeValue(this.DiscountTypeID);
        dest.writeString(this.SalesOrderTypeCode);
        dest.writeValue(this.PaymentStat);
        dest.writeString(this.ReceptionStaffGUID);
        dest.writeParcelable(this.ReceptionStaff, flags);
        dest.writeString(this.CheckStaffGUID);
        dest.writeParcelable(this.CheckStaff, flags);
        dest.writeString(this.PayeeStaffGUID);
        dest.writeParcelable(this.PayeeStaff, flags);
        dest.writeValue(this.SalesOrderUploadSTAT);
        dest.writeString(this.BusinessDay);
        dest.writeString(this.MergeSalesOrderGUID);
        dest.writeTypedList(this.ArrayofSalesOrderDishes);
        dest.writeList(this.ArrayofSalesOrderBatch);
        dest.writeTypedList(this.ArrayofSalesOrderBatchDishes);
        dest.writeValue(this.IsReCheck);
        dest.writeValue(this.CalculationPrintCount);
    }

    protected SalesOrder(Parcel in) {
        this.SalesOrderGUID = in.readString();
        this.RegTel = in.readString();
        this.SerialNumber = in.readString();
        this.isMemberPrice = in.readByte() != 0;
        this.OrgNumber = in.readString();
        this.StoreGUID = in.readString();
        this.GuestCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DiningTableGUID = in.readString();
        this.CreateTime = in.readString();
        this.CreateStaffGUID = in.readString();
        this.CreateStaff = in.readParcelable(Users.class.getClassLoader());
        this.CheckOutStaffGUID = in.readString();
        this.CheckOutStaff = in.readParcelable(Users.class.getClassLoader());
        this.ChecOutkTime = in.readString();
        this.CancelStaffGUID = in.readString();
        this.CancelStaff = in.readParcelable(Users.class.getClassLoader());
        this.CancelTime = in.readString();
        this.OrderStat = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CheckTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.ConsumeTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.ActuallyInsertStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ActuallyPayTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.ActuallyInsert = (Double) in.readValue(Double.class.getClassLoader());
        this.RefundTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.Lock = (Integer) in.readValue(Integer.class.getClassLoader());
        this.LockRemark = in.readString();
        this.TradeMode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DiscountRatio = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountRatioRemark = in.readString();
        this.DiscountTypeID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesOrderTypeCode = in.readString();
        this.PaymentStat = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReceptionStaffGUID = in.readString();
        this.ReceptionStaff = in.readParcelable(Users.class.getClassLoader());
        this.CheckStaffGUID = in.readString();
        this.CheckStaff = in.readParcelable(Users.class.getClassLoader());
        this.PayeeStaffGUID = in.readString();
        this.PayeeStaff = in.readParcelable(Users.class.getClassLoader());
        this.SalesOrderUploadSTAT = (Integer) in.readValue(Integer.class.getClassLoader());
        this.BusinessDay = in.readString();
        this.MergeSalesOrderGUID = in.readString();
        this.ArrayofSalesOrderDishes = in.createTypedArrayList(SalesOrderDishes.CREATOR);
        this.ArrayofSalesOrderBatch = new ArrayList<SalesOrderBatch>();
        in.readList(this.ArrayofSalesOrderBatch, SalesOrderBatch.class.getClassLoader());
        this.ArrayofSalesOrderBatchDishes = in.createTypedArrayList(SalesOrderBatchDishes.CREATOR);
        this.IsReCheck = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CalculationPrintCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SalesOrder> CREATOR = new Creator<SalesOrder>() {
        @Override
        public SalesOrder createFromParcel(Parcel source) {
            return new SalesOrder(source);
        }

        @Override
        public SalesOrder[] newArray(int size) {
            return new SalesOrder[size];
        }
    };
}
