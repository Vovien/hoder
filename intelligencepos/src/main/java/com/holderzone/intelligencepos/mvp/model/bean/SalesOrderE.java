package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class SalesOrderE extends SalesOrder implements Parcelable {

    private DiningTableE DiningTableE;
    private List<SalesOrderDiscountE> ArrayOfSalesOrderDiscountE;
    private List<SalesOrderAdditionalFeesE> ArrayOfSalesOrderAdditionalFeesE;

    private UsersE CreateStaffE;

    private MemberInfoE MemberInfoE;

    private UsersE CheckOutStaffE;

    private UsersE CancelStaffE;

    private UsersE ReceptionStaffE;

    private UsersE CheckStaffE;

    private UsersE PayeeStaffE;
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 终端设备ID
     */
    private String DeviceID;
    /**
     * 是否返回实体集合 0=不返回 1=返回
     */
    private Integer ReturnEntityArray;
    /**
     * 是否加载点单批次数据 0=否 1=是
     */
    private Integer ViewSalesOrderBatch;
    /**
     * 是否加载菜品汇总数据 0=否 1=是
     */
    private Integer ViewSalesOrderDishes;
    /**
     * 下单批次记录实体
     * TradeMode=1则必填
     */
    private SalesOrderBatchE salesOrderBatchE;

    /**
     * 账单点单批次的集合
     */
    private List<SalesOrderBatchE> ArrayOfSalesOrderBatchE;

    /**
     * 消费菜品汇总集合
     */
    private List<SalesOrderDishesE> ArrayOfSalesOrderDishesE;

    /**
     * 付款记录集合
     */
    private List<SalesOrderPaymentE> ArrayOfSalesOrderPaymentE;

    /**
     * 是否加载点单批次数据 0=否 1=是，不传默认0
     */
    private Integer ReturnSalesOrderBatch;

    /**
     * 是否加载点单批次数据 0=否 1=点菜 -1=退菜 10=全部
     */
    private Integer ReturnBatch;
    /**
     * 附加费用合计
     */
    private Double AdditionalFeesTotal;

    /**
     * 单个桌台的附加费合计
     */
    private Double DtAdditionalFeesTotal;
    /**
     * 是否加载点单批次菜品明细 0=否 1=是，不传默认0
     */
    private Integer ReturnBatchDishes;
    /**
     * 是否加载点单批次菜品的特殊要求备注 0=否 1=是，不传默认0
     */
    private Integer ReturnBatchDishesRemark;
    /**
     * 是否加载点单批次菜品的特殊要求做法 0=否 1=是，不传默认0
     */

    /**
     * 是否加载折扣项目数据 0=否 1=是，不传默认0
     */
    private Integer ReturnSalesOrderDiscount;
    /**
     * 作废订单的操作员姓名
     */
    private String CancelStaffName;

    /**
     * 该单可用积分
     */
    private Integer UsePoints;
    /**
     * 可用积分抵扣金额
     */
    private Double PointsAmount;
    /**
     * 会员剩余积分（抵扣之前剩余积分）
     */
    private Integer LeaguerPoint;

    private String MemberInfoGUID;
    /**
     * 是否启用积分
     */
    private Integer IsUsePoint;

    /**
     * 积分是否足够
     * 1足够
     * 0不足够
     */
    private Integer IsEnough;

    /**
     * 1：单品折扣项和对单
     * 2：营业数据 账单明细显示
     */
    private Integer ShowScene;

    /**
     * 核销的权码列表
     */
    private List<String> codes;

    /**
     * 根据类型确定返回列表
     * 1卡;2券;3红包
     */
    private Integer PreferentialType;
    /**
     * 是否是自动核算
     * 海谱优惠自动计算 1 自动计算; 0 手动计算
     */
    private Integer AutoCalc;

    /**
     * 是否返回 选中状态
     */
    private Boolean PreferentialSelect;

    private List<SalesOrderDiningTableE> ArrayOfSalesOrderDiningTableE;

    public Double getDtAdditionalFeesTotal() {
        return DtAdditionalFeesTotal;
    }

    public void setDtAdditionalFeesTotal(Double dtAdditionalFeesTotal) {
        DtAdditionalFeesTotal = dtAdditionalFeesTotal;
    }


    public List<SalesOrderDiningTableE> getArrayOfSalesOrderDiningTableE() {
        return ArrayOfSalesOrderDiningTableE;
    }

    public void setArrayOfSalesOrderDiningTableE(List<SalesOrderDiningTableE> arrayOfSalesOrderDiningTableE) {
        ArrayOfSalesOrderDiningTableE = arrayOfSalesOrderDiningTableE;
    }

    public Boolean getPreferentialSelect() {
        return PreferentialSelect;
    }

    public void setPreferentialSelect(Boolean preferentialSelect) {
        PreferentialSelect = preferentialSelect;
    }

    public Integer getPreferentialType() {
        return PreferentialType;
    }

    public void setPreferentialType(Integer preferentialType) {
        PreferentialType = preferentialType;
    }

    public Integer getAutoCalc() {
        return AutoCalc;
    }

    public void setAutoCalc(Integer autoCalc) {
        AutoCalc = autoCalc;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Integer getShowScene() {
        return ShowScene;
    }

    public void setShowScene(Integer showScene) {
        ShowScene = showScene;
    }

    public Integer getIsEnough() {
        return IsEnough;
    }

    public void setIsEnough(Integer isEnough) {
        IsEnough = isEnough;
    }

    public Integer getIsUsePoint() {
        return IsUsePoint;
    }

    public void setIsUsePoint(Integer isUsePoint) {
        IsUsePoint = isUsePoint;
    }

    public Integer getUsePoints() {
        return UsePoints;
    }

    public void setUsePoints(Integer usePoints) {
        UsePoints = usePoints;
    }

    public Double getPointsAmount() {
        return PointsAmount;
    }

    public void setPointsAmount(Double pointsAmount) {
        PointsAmount = pointsAmount;
    }

    public Integer getLeaguerPoint() {
        return LeaguerPoint;
    }

    public void setLeaguerPoint(Integer leaguerPoint) {
        LeaguerPoint = leaguerPoint;
    }

    public String getMemberInfoGUID() {
        return MemberInfoGUID;
    }

    public void setMemberInfoGUID(String memberInfoGUID) {
        MemberInfoGUID = memberInfoGUID;
    }

    public Integer getReturnSalesOrderDiscount() {
        return ReturnSalesOrderDiscount;
    }

    public void setReturnSalesOrderDiscount(Integer returnSalesOrderDiscount) {
        ReturnSalesOrderDiscount = returnSalesOrderDiscount;
    }

    public Integer getReturnSalesOrderAdditionalFees() {
        return ReturnSalesOrderAdditionalFees;
    }

    public void setReturnSalesOrderAdditionalFees(Integer returnSalesOrderAdditionalFees) {
        ReturnSalesOrderAdditionalFees = returnSalesOrderAdditionalFees;
    }

    /**
     * 是否加载附加费用数据 0=否 1=是，不传默认0
     */
    private Integer ReturnSalesOrderAdditionalFees;

    private Integer ReturnBatchDishesPractice;
    /**
     * 是否加载点单批次菜品的特殊要求退菜原因 0=否 1=是，不传默认0
     */
    private Integer ReturnBatchDishesReturnReason;
    /**
     * 是否加载菜品汇总数据 0=否 1=是，不传默认0
     */
    private Integer ReturnSalesOrderDishes;
    /**
     * 是否加载付款记录数据 0=否 1=是，不传默认0
     */
    private Integer ReturnSalesOrderPayment;

    /**
     * 并单状态 0无并单 1主单 2子单
     */
    private Integer UpperState;

    /**
     * 有并单情况下才有意义
     */
    private String UpperGUID;

    public Integer getReturnEntityArray() {
        return ReturnEntityArray;
    }

    public void setReturnEntityArray(Integer returnEntityArray) {
        ReturnEntityArray = returnEntityArray;
    }

    /**
     * 使用时间
     */
    private String UseHour;

    /**
     * 菜品挂起数量
     */
    private Integer DishesHangCount;

    /**
     * 批次操作次数
     */
    private Integer OrderBatchCount;

    /**
     * 操作员标识
     */
    private String StaffGUID;

    /**
     * 子单
     */
    private List<SubSalesOrder> ArrayOfSubSalesOrder;

    /**
     * 卡芯片编码
     */
    private String CardsChipNo;

    /**
     * 原桌台标识
     */
    private String OrgDiningTableGUID;

    /**
     * 新桌台标识
     */
    private String NewDiningTableGUID;

    /**
     * 订单创建人
     */
    private String UserName;
    /**
     * 未结账数量
     */
    private Integer ArrayCount;
    /**
     * 待收金额 CheckTotal - ActuallyPayTotal
     */
    private Double UnpaidTotal;
    /**
     * 支付折扣合计
     */
    private Double PaymentDiscountTotal;

    /**
     * 菜品消费合计 ConsumeTotal + PracticeTotal
     */
    private Double DishesConsumeTotal;
    /**
     * 结账员工姓名
     */
    private String CheckOutStaffName;
    /**
     * 餐桌名称
     */
    private String DiningTableName;
    /**
     * 餐桌状态
     */
    private String OrderStatCN;
    /**
     * 支付合计
     */
    private Double PayTotal;

    private Integer CheckPrintType;
    /**
     * 预订guid
     */
    private String OrderRecordGUID;
    /**
     * 收到现金
     */
    private Double ReciveMoneyTotal;

    public Double getReciveMoneyTotal() {
        return ReciveMoneyTotal;
    }

    public Integer getCheckPrintType() {
        return CheckPrintType;
    }

    public void setCheckPrintType(Integer checkPrintType) {
        CheckPrintType = checkPrintType;
    }

    public Double getPayTotal() {
        return PayTotal;
    }

    public void setPayTotal(Double payTotal) {
        PayTotal = payTotal;
    }

    public String getOrderStatCN() {
        return OrderStatCN;
    }

    public Double getPaymentDiscountTotal() {
        return PaymentDiscountTotal;
    }

    public void setPaymentDiscountTotal(Double paymentDiscountTotal) {
        PaymentDiscountTotal = paymentDiscountTotal;
    }

    public void setOrderStatCN(String orderStatCN) {
        OrderStatCN = orderStatCN;
    }

    public String getCheckOutStaffName() {
        return CheckOutStaffName;
    }

    public void setCheckOutStaffName(String checkOutStaffName) {
        CheckOutStaffName = checkOutStaffName;
    }

    public String getDiningTableName() {
        return DiningTableName;
    }

    public void setDiningTableName(String diningTableName) {
        DiningTableName = diningTableName;
    }

    public Integer getArrayCount() {
        return ArrayCount;
    }

    public Double getUnpaidTotal() {
        return UnpaidTotal;
    }

    public void setUnpaidTotal(Double unpaidTotal) {
        UnpaidTotal = unpaidTotal;
    }

    public Double getDishesConsumeTotal() {
        return DishesConsumeTotal;
    }

    public void setDishesConsumeTotal(Double dishesConsumeTotal) {
        DishesConsumeTotal = dishesConsumeTotal;
    }

    public void setArrayCount(Integer arrayCount) {
        ArrayCount = arrayCount;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public Integer getViewSalesOrderBatch() {
        return ViewSalesOrderBatch;
    }

    public void setViewSalesOrderBatch(Integer viewSalesOrderBatch) {
        ViewSalesOrderBatch = viewSalesOrderBatch;
    }

    public Integer getViewSalesOrderDishes() {
        return ViewSalesOrderDishes;
    }

    public void setViewSalesOrderDishes(Integer viewSalesOrderDishes) {
        ViewSalesOrderDishes = viewSalesOrderDishes;
    }

    public SalesOrderBatchE getSalesOrderBatchE() {
        return salesOrderBatchE;
    }

    public void setSalesOrderBatchE(SalesOrderBatchE salesOrderBatchE) {
        this.salesOrderBatchE = salesOrderBatchE;
    }

    public DiningTableE getDiningTableE() {
        return DiningTableE;
    }

    public void setDiningTableE(DiningTableE diningTableE) {
        DiningTableE = diningTableE;
    }

    public UsersE getCreateStaffE() {
        return CreateStaffE;
    }

    public void setCreateStaffE(UsersE createStaffE) {
        CreateStaffE = createStaffE;
    }

    public UsersE getCheckOutStaffE() {
        return CheckOutStaffE;
    }

    public void setCheckOutStaffE(UsersE checkOutStaffE) {
        CheckOutStaffE = checkOutStaffE;
    }

    public UsersE getCancelStaffE() {
        return CancelStaffE;
    }

    public void setCancelStaffE(UsersE cancelStaffE) {
        CancelStaffE = cancelStaffE;
    }

    public UsersE getReceptionStaffE() {
        return ReceptionStaffE;
    }

    public void setReceptionStaffE(UsersE receptionStaffE) {
        ReceptionStaffE = receptionStaffE;
    }

    public UsersE getCheckStaffE() {
        return CheckStaffE;
    }

    public void setCheckStaffE(UsersE checkStaffE) {
        CheckStaffE = checkStaffE;
    }

    public UsersE getPayeeStaffE() {
        return PayeeStaffE;
    }

    public void setPayeeStaffE(UsersE payeeStaffE) {
        PayeeStaffE = payeeStaffE;
    }
//    public List<SalesOrderDishesE> getArrayofSalesOrderDishesE() {
//        return ArrayofSalesOrderDishesE;
//    }
//
//    public void setArrayofSalesOrderDishesE(List<SalesOrderDishesE> arrayofSalesOrderDishesE) {
//        ArrayofSalesOrderDishesE = arrayofSalesOrderDishesE;
//    }

    public List<SalesOrderBatchE> getArrayOfSalesOrderBatchE() {
        return ArrayOfSalesOrderBatchE;
    }

    public void setArrayOfSalesOrderBatchE(List<SalesOrderBatchE> arrayOfSalesOrderBatchE) {
        ArrayOfSalesOrderBatchE = arrayOfSalesOrderBatchE;
    }

    public List<SalesOrderDiscountE> getArrayOfSalesOrderDiscountE() {
        return ArrayOfSalesOrderDiscountE;
    }

    public void setArrayOfSalesOrderDiscountE(List<SalesOrderDiscountE> arrayOfSalesOrderDiscountE) {
        ArrayOfSalesOrderDiscountE = arrayOfSalesOrderDiscountE;
    }

    public Integer getReturnSalesOrderBatch() {
        return ReturnSalesOrderBatch;
    }

    public void setReturnSalesOrderBatch(Integer returnSalesOrderBatch) {
        ReturnSalesOrderBatch = returnSalesOrderBatch;
    }

    public Integer getReturnBatchDishes() {
        return ReturnBatchDishes;
    }

    public void setReturnBatchDishes(Integer returnBatchDishes) {
        ReturnBatchDishes = returnBatchDishes;
    }

    public Integer getReturnBatchDishesRemark() {
        return ReturnBatchDishesRemark;
    }

    public void setReturnBatchDishesRemark(Integer returnBatchDishesRemark) {
        ReturnBatchDishesRemark = returnBatchDishesRemark;
    }

    public Integer getReturnBatchDishesPractice() {
        return ReturnBatchDishesPractice;
    }

    public void setReturnBatchDishesPractice(Integer returnBatchDishesPractice) {
        ReturnBatchDishesPractice = returnBatchDishesPractice;
    }

    public Integer getReturnBatchDishesReturnReason() {
        return ReturnBatchDishesReturnReason;
    }

    public void setReturnBatchDishesReturnReason(Integer returnBatchDishesReturnReason) {
        ReturnBatchDishesReturnReason = returnBatchDishesReturnReason;
    }

    public Integer getReturnSalesOrderDishes() {
        return ReturnSalesOrderDishes;
    }

    public void setReturnSalesOrderDishes(Integer returnSalesOrderDishes) {
        ReturnSalesOrderDishes = returnSalesOrderDishes;
    }

    public Integer getReturnSalesOrderPayment() {
        return ReturnSalesOrderPayment;
    }

    public void setReturnSalesOrderPayment(Integer returnSalesOrderPayment) {
        ReturnSalesOrderPayment = returnSalesOrderPayment;
    }

    public Integer getUpperState() {
        return UpperState;
    }

    public void setUpperState(Integer upperState) {
        UpperState = upperState;
    }

    public List<SalesOrderAdditionalFeesE> getArrayOfSalesOrderAdditionalFeesE() {
        return ArrayOfSalesOrderAdditionalFeesE;
    }

    public void setArrayOfSalesOrderAdditionalFeesE(List<SalesOrderAdditionalFeesE> arrayOfSalesOrderAdditionalFeesE) {
        ArrayOfSalesOrderAdditionalFeesE = arrayOfSalesOrderAdditionalFeesE;
    }

    public String getUpperGUID() {
        return UpperGUID;
    }

    public void setUpperGUID(String upperGUID) {
        UpperGUID = upperGUID;
    }

    public String getUseHour() {
        return UseHour;
    }

    public void setUseHour(String useHour) {
        UseHour = useHour;
    }

    public Integer getDishesHangCount() {
        return DishesHangCount;
    }

    public void setDishesHangCount(Integer dishesHangCount) {
        DishesHangCount = dishesHangCount;
    }

    public Integer getOrderBatchCount() {
        return OrderBatchCount;
    }

    public void setOrderBatchCount(Integer orderBatchCount) {
        OrderBatchCount = orderBatchCount;
    }

    public List<SalesOrderDishesE> getArrayOfSalesOrderDishesE() {
        return ArrayOfSalesOrderDishesE;
    }

    public void setArrayOfSalesOrderDishesE(List<SalesOrderDishesE> arrayOfSalesOrderDishesE) {
        ArrayOfSalesOrderDishesE = arrayOfSalesOrderDishesE;
    }

    public List<SalesOrderPaymentE> getArrayOfSalesOrderPaymentE() {
        return ArrayOfSalesOrderPaymentE;
    }

    public void setArrayOfSalesOrderPaymentE(List<SalesOrderPaymentE> arrayOfSalesOrderPaymentE) {
        ArrayOfSalesOrderPaymentE = arrayOfSalesOrderPaymentE;
    }

    public Integer getReturnBatch() {
        return ReturnBatch;
    }

    public void setReturnBatch(Integer returnBatch) {
        ReturnBatch = returnBatch;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getStaffGUID() {
        return StaffGUID;
    }

    public void setStaffGUID(String staffGUID) {
        StaffGUID = staffGUID;
    }

    public List<SubSalesOrder> getArrayOfSubSalesOrder() {
        return ArrayOfSubSalesOrder;
    }

    public void setArrayOfSubSalesOrder(List<SubSalesOrder> arrayOfSubSalesOrder) {
        ArrayOfSubSalesOrder = arrayOfSubSalesOrder;
    }

    public String getOrgDiningTableGUID() {
        return OrgDiningTableGUID;
    }

    public void setOrgDiningTableGUID(String orgDiningTableGUID) {
        OrgDiningTableGUID = orgDiningTableGUID;
    }

    public Double getAdditionalFeesTotal() {
        return AdditionalFeesTotal;
    }

    public void setAdditionalFeesTotal(Double additionalFeesTotal) {
        AdditionalFeesTotal = additionalFeesTotal;
    }

    public String getNewDiningTableGUID() {
        return NewDiningTableGUID;
    }

    public void setNewDiningTableGUID(String newDiningTableGUID) {
        NewDiningTableGUID = newDiningTableGUID;
    }

    public MemberInfoE getMemberInfoE() {
        return MemberInfoE;
    }

    public void setMemberInfoE(MemberInfoE memberInfoE) {
        MemberInfoE = memberInfoE;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCardsChipNo() {
        return CardsChipNo;
    }

    public void setCardsChipNo(String cardsChipNo) {
        this.CardsChipNo = cardsChipNo;
    }

    public String getOrderRecordGUID() {
        return OrderRecordGUID;
    }

    public void setOrderRecordGUID(String orderRecordGUID) {
        OrderRecordGUID = orderRecordGUID;
    }

    public String getCancelStaffName() {
        return CancelStaffName;
    }

    public void setCancelStaffName(String cancelStaffName) {
        CancelStaffName = cancelStaffName;
    }


    public SalesOrderE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.DiningTableE, flags);
        dest.writeTypedList(this.ArrayOfSalesOrderDiscountE);
        dest.writeTypedList(this.ArrayOfSalesOrderAdditionalFeesE);
        dest.writeParcelable(this.CreateStaffE, flags);
        dest.writeParcelable(this.MemberInfoE, flags);
        dest.writeParcelable(this.CheckOutStaffE, flags);
        dest.writeParcelable(this.CancelStaffE, flags);
        dest.writeParcelable(this.ReceptionStaffE, flags);
        dest.writeParcelable(this.CheckStaffE, flags);
        dest.writeParcelable(this.PayeeStaffE, flags);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.DeviceID);
        dest.writeValue(this.ReturnEntityArray);
        dest.writeValue(this.ViewSalesOrderBatch);
        dest.writeValue(this.ViewSalesOrderDishes);
        dest.writeParcelable(this.salesOrderBatchE, flags);
        dest.writeTypedList(this.ArrayOfSalesOrderBatchE);
        dest.writeTypedList(this.ArrayOfSalesOrderDishesE);
        dest.writeTypedList(this.ArrayOfSalesOrderPaymentE);
        dest.writeValue(this.ReturnSalesOrderBatch);
        dest.writeValue(this.ReturnBatch);
        dest.writeValue(this.AdditionalFeesTotal);
        dest.writeValue(this.DtAdditionalFeesTotal);
        dest.writeValue(this.ReturnBatchDishes);
        dest.writeValue(this.ReturnBatchDishesRemark);
        dest.writeValue(this.ReturnSalesOrderDiscount);
        dest.writeString(this.CancelStaffName);
        dest.writeValue(this.UsePoints);
        dest.writeValue(this.PointsAmount);
        dest.writeValue(this.LeaguerPoint);
        dest.writeString(this.MemberInfoGUID);
        dest.writeValue(this.IsUsePoint);
        dest.writeValue(this.IsEnough);
        dest.writeValue(this.ShowScene);
        dest.writeStringList(this.codes);
        dest.writeValue(this.PreferentialType);
        dest.writeValue(this.AutoCalc);
        dest.writeValue(this.PreferentialSelect);
        dest.writeTypedList(this.ArrayOfSalesOrderDiningTableE);
        dest.writeValue(this.ReturnSalesOrderAdditionalFees);
        dest.writeValue(this.ReturnBatchDishesPractice);
        dest.writeValue(this.ReturnBatchDishesReturnReason);
        dest.writeValue(this.ReturnSalesOrderDishes);
        dest.writeValue(this.ReturnSalesOrderPayment);
        dest.writeValue(this.UpperState);
        dest.writeString(this.UpperGUID);
        dest.writeString(this.UseHour);
        dest.writeValue(this.DishesHangCount);
        dest.writeValue(this.OrderBatchCount);
        dest.writeString(this.StaffGUID);
        dest.writeList(this.ArrayOfSubSalesOrder);
        dest.writeString(this.CardsChipNo);
        dest.writeString(this.OrgDiningTableGUID);
        dest.writeString(this.NewDiningTableGUID);
        dest.writeString(this.UserName);
        dest.writeValue(this.ArrayCount);
        dest.writeValue(this.UnpaidTotal);
        dest.writeValue(this.PaymentDiscountTotal);
        dest.writeValue(this.DishesConsumeTotal);
        dest.writeString(this.CheckOutStaffName);
        dest.writeString(this.DiningTableName);
        dest.writeString(this.OrderStatCN);
        dest.writeValue(this.PayTotal);
        dest.writeValue(this.CheckPrintType);
        dest.writeString(this.OrderRecordGUID);
        dest.writeValue(this.ReciveMoneyTotal);
    }

    protected SalesOrderE(Parcel in) {
        super(in);
        this.DiningTableE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DiningTableE.class.getClassLoader());
        this.ArrayOfSalesOrderDiscountE = in.createTypedArrayList(SalesOrderDiscountE.CREATOR);
        this.ArrayOfSalesOrderAdditionalFeesE = in.createTypedArrayList(SalesOrderAdditionalFeesE.CREATOR);
        this.CreateStaffE = in.readParcelable(UsersE.class.getClassLoader());
        this.MemberInfoE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE.class.getClassLoader());
        this.CheckOutStaffE = in.readParcelable(UsersE.class.getClassLoader());
        this.CancelStaffE = in.readParcelable(UsersE.class.getClassLoader());
        this.ReceptionStaffE = in.readParcelable(UsersE.class.getClassLoader());
        this.CheckStaffE = in.readParcelable(UsersE.class.getClassLoader());
        this.PayeeStaffE = in.readParcelable(UsersE.class.getClassLoader());
        this.EnterpriseInfoGUID = in.readString();
        this.DeviceID = in.readString();
        this.ReturnEntityArray = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ViewSalesOrderBatch = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ViewSalesOrderDishes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.salesOrderBatchE = in.readParcelable(SalesOrderBatchE.class.getClassLoader());
        this.ArrayOfSalesOrderBatchE = in.createTypedArrayList(SalesOrderBatchE.CREATOR);
        this.ArrayOfSalesOrderDishesE = in.createTypedArrayList(SalesOrderDishesE.CREATOR);
        this.ArrayOfSalesOrderPaymentE = in.createTypedArrayList(SalesOrderPaymentE.CREATOR);
        this.ReturnSalesOrderBatch = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnBatch = (Integer) in.readValue(Integer.class.getClassLoader());
        this.AdditionalFeesTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.DtAdditionalFeesTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.ReturnBatchDishes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnBatchDishesRemark = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnSalesOrderDiscount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CancelStaffName = in.readString();
        this.UsePoints = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PointsAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.LeaguerPoint = (Integer) in.readValue(Integer.class.getClassLoader());
        this.MemberInfoGUID = in.readString();
        this.IsUsePoint = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsEnough = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ShowScene = (Integer) in.readValue(Integer.class.getClassLoader());
        this.codes = in.createStringArrayList();
        this.PreferentialType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.AutoCalc = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PreferentialSelect = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.ArrayOfSalesOrderDiningTableE = in.createTypedArrayList(SalesOrderDiningTableE.CREATOR);
        this.ReturnSalesOrderAdditionalFees = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnBatchDishesPractice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnBatchDishesReturnReason = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnSalesOrderDishes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnSalesOrderPayment = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UpperState = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UpperGUID = in.readString();
        this.UseHour = in.readString();
        this.DishesHangCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderBatchCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.StaffGUID = in.readString();
        this.ArrayOfSubSalesOrder = new ArrayList<SubSalesOrder>();
        in.readList(this.ArrayOfSubSalesOrder, SubSalesOrder.class.getClassLoader());
        this.CardsChipNo = in.readString();
        this.OrgDiningTableGUID = in.readString();
        this.NewDiningTableGUID = in.readString();
        this.UserName = in.readString();
        this.ArrayCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UnpaidTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.PaymentDiscountTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesConsumeTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.CheckOutStaffName = in.readString();
        this.DiningTableName = in.readString();
        this.OrderStatCN = in.readString();
        this.PayTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.CheckPrintType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderRecordGUID = in.readString();
        this.ReciveMoneyTotal = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<SalesOrderE> CREATOR = new Creator<SalesOrderE>() {
        @Override
        public SalesOrderE createFromParcel(Parcel source) {
            return new SalesOrderE(source);
        }

        @Override
        public SalesOrderE[] newArray(int size) {
            return new SalesOrderE[size];
        }
    };
}
