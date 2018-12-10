package com.holderzone.intelligencepos.mvp.model.bean;

import android.util.Pair;

import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.utils.Security;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.memoizrlabs.retrooptional.Optional;

import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by tcw on 2017/3/10.
 */

public class XmlData {
    public Boolean consolePrintLog;

    /**
     *
     */
    private ApiBase ApiBase;

    /**
     *
     */
    private ApiNote ApiNote;

    /**
     *
     */
    private PageInfo PageInfo;

    /**
     *
     */
    private AppVersion AppVersion;

    /**
     *
     */
    private String NonceStr;

    /**
     * 设备实体
     */
    private DeviceE DeviceE;

    /**
     * 预订
     */
    private OrderRecordE OrderRecordE;

    /**
     * 预订列表
     */
    private List<OrderRecordE> ArrayOfOrderRecordE;
    /**
     * 打印logo
     */
    private List<PrinterConfigE> ArrayOfPrinterConfigE;
    /**
     * 打印logo集合
     */
    private PrinterConfigE PrinterConfigE;

    /**
     * 商家实体
     */
    private EnterpriseInfoE EnterpriseInfoE;

    /**
     * 门店实体
     */
    private StoreE StoreE;

    /**
     * 门店实体集合
     */
    private List<StoreE> ArrayOfStoreE;

    /**
     *
     */
    private List<AdditionalFeesE> ArrayOfAdditionalFeesE;

    /**
     * 用户实体
     */
    private UsersE UsersE;

    /**
     * 预订时段
     */
    private OrderRecordSectionE OrderRecordSectionE;

    /**
     * 预订时段集合
     */
    private List<OrderRecordSectionE> ArrayOfOrderRecordSectionE;

    /**
     * 用户实体集合
     */
    private List<UsersE> ArrayOfUsersE;

    /**
     * 券集合
     */
    private List<CouponsE> ArrayOfCouponsE;

    /**
     * 券实体
     */
    private CouponsE CouponsE;

    /**
     * 区域实体
     */
    private DiningTableAreaE DiningTableAreaE;

    /**
     * 区域实体集合
     */
    private List<DiningTableAreaE> ArrayOfDiningTableAreaE;

    /**
     * 餐桌实体
     */
    private DiningTableE DiningTableE;

    /**
     * 餐桌实体集合
     */
    private List<DiningTableE> ArrayOfDiningTableE;

    /**
     * 菜品类别实体
     */
    private DishesTypeE DishesTypeE;

    /**
     * 菜品类别实体集合
     */
    private List<DishesTypeE> ArrayOfDishesTypeE;

    /**
     * 菜品实体
     */
    private DishesE DishesE;

    /**
     * 菜品实体集合
     */
    private List<DishesE> ArrayOfDishesE;

    /**
     * 菜品特殊要求
     */
    private DishesRemarkE DishesRemarkE;

    /**
     * 菜品特殊要求集合
     */
    private List<DishesRemarkE> ArrayOfDishesRemarkE;

    /**
     * 菜品做法实体
     */
    private DishesPracticeE DishesPracticeE;

    /**
     * 菜品做法实体集合
     */
    private List<DishesPracticeE> ArrayOfDishesPracticeE;

    /**
     * 退菜原因实体
     */
    private DishesReturnReasonE DishesReturnReasonE;

    /**
     * 退菜原因实体集合
     */
    private List<DishesReturnReasonE> ArrayOfDishesReturnReasonE;

    /**
     * 收款项目实体
     */
    private PaymentItemE PaymentItemE;

    /**
     * 收款项目实体集合
     */
    private List<PaymentItemE> ArrayOfPaymentItemE;

    /**
     * 附加费实体
     */
    private SalesOrderAdditionalFeesE SalesOrderAdditionalFeesE;

    /**
     * 附加费实体集合
     */
    private List<SalesOrderAdditionalFeesE> ArrayOfSalesOrderAdditionalFeesE;

    /**
     * 账单实体
     */
    private SalesOrderE SalesOrderE;

    /**
     *
     */
    private List<SalesOrderE> ArrayOfSalesOrderE;

    /**
     * 点餐记录实体
     */
    private SalesOrderBatchE SalesOrderBatchE;

    /**
     * 点餐记录集合实体集合
     */
    private List<SalesOrderBatchE> ArrayOfSalesOrderBatchE;
    private List<SalesOrderBatchN> ArrayOfSalesOrderBatchN;

    /**
     * 收款记录实体
     */
    private SalesOrderPaymentE SalesOrderPaymentE;

    /**
     * 收款记录实体列表
     */
    List<SalesOrderPaymentE> ArrayOfSalesOrderPaymentE;

    /**
     * 打印机类型实体
     */
    private PrinterTypeE PrinterTypeE;

    /**
     * 打印机类型实体集合
     */
    private List<PrinterTypeE> ArrayOfPrinterTypeE;

    /**
     * 打印机实体
     */
    private PrinterE PrinterE;

    /**
     * 打印机实体集合
     */
    private List<PrinterE> ArrayOfPrinterE;

    /**
     *
     */
    private Dishes Dishes;

    /**
     *
     */
    private List<Dishes> ArrayOfDishes;

    /**
     * 卡实体
     */
    private CardsE CardsE;

    /**
     * 会员账户拥有的卡实体集合
     */
    private List<CardsE> ArrayOfCardsE;

    /**
     *
     */
    private List<CardTypeE> ArrayOfCardTypeE;

    /**
     *
     */
    private CardSellOrderE CardSellOrderE;

    /**
     * 折扣汇总集合
     */
    private List<SalesOrderDiscountE> ArrayOfSalesOrderDiscountE;

    /**
     * 优惠券汇总集合
     */
    private List<SalesOrderCouponsUseE> ArrayOfSalesOrderCouponsUseE;

    /**
     *
     */
    private List<ViewCardBusinessOrderE> ArrayOfViewCardBusinessOrderE;

    /**
     * 估清记录实体
     */
    private DishesEstimateRecordE DishesEstimateRecordE;

    /**
     * 估清记录实体集合
     */
    private List<DishesEstimateRecordE> ArrayOfDishesEstimateRecordE;

    /**
     *
     */
    private List<SalesOrderDishesE> ArrayOfSalesOrderDishesE;

    /**
     * 估清菜品集合
     */
    private List<DishesEstimateRecordDishes> ArrayOfDishesEstimateRecordDishes;

    /**
     * 扎帐记录实体
     */
    private AccountRecordE AccountRecordE;

    /**
     * 扎帐记录实体集合
     */
    private List<AccountRecordE> ArrayOfAccountRecordE;

    /**
     * 班次配置实体
     */
    private ShiftConfigE ShiftConfigE;

    /**
     * 班次配置实体集合
     */
    private List<ShiftConfigE> ArrayOfShiftConfigE;

    /**
     * 班次记录实体
     */
    private ShiftRecordE ShiftRecordE;

    /**
     * 班次记录实体集合
     */
    private List<ShiftRecordE> ArrayOfShiftRecordE;

    /**
     * 预订备注
     */
    private OrderRecordRemarkItemE OrderRecordRemarkItemE;

    /**
     * 预订备注列表
     */
    private List<OrderRecordRemarkItemE> ArrayOfOrderRecordRemarkItemE;

    /**
     * 堂食正常当前的刷新信息
     */
    private DinnerE DinnerE;

    /**
     * 会员实体
     */
    private MemberInfoE MemberInfoE;

    /**
     * 附加费实体
     */
    private AdditionalFeesE AdditionalFeesE;

    /**
     * 一体机设备实体
     */
    private EquipmentsE EquipmentsE;

    /**
     * 会员卡业务记录实体
     */
    private CardBusinessOrderE CardBusinessOrderE;

    /**
     * 排号类型
     */
    private QueueUpTypeE QueueUpTypeE;

    /**
     * 排号类型列表
     */
    private List<QueueUpTypeE> ArrayOfQueueUpTypeE;

    /**
     * 排号记录
     */
    private QueueUpRecordE QueueUpRecordE;

    /**
     * 排号记录列表
     */
    private List<QueueUpRecordE> ArrayOfQueueUpRecordE;

    /**
     * 卡类型实体
     */
    private CardTypeE CardTypeE;

    /**
     * 排队统计报表
     */
    private List<QueueUpReportE> ArrayOfQueueUpReportE;

    /**
     * 营业日汇总
     */
    private List<BusinessDaySummaryE> ArrayOfBusinessDaySummaryE;

    /**
     *
     */
    private BusinessDaySummaryE BusinessDaySummaryE;

    /**
     * 美团券实体
     */
    private MeituanCoupon MeituanCoupon;

    /**
     * 美团券实体列表
     */
    private List<MeituanCoupon> ArrayOfMeituanCoupon;

    /**
     * 状态信息请求实体
     */
    private SalesMsgReadRecordE SalesMsgReadRecordE;
    /**
     * 参数配置实体
     */
    private SysParametersE SysParametersE;

    /**
     * 状态信息实体列表
     */
    private List<StateMsg> ArrayOfStateMsg;
    /**
     * 划菜实体
     */
    private SalesOrderServingDishesE SalesOrderServingDishesE;
    /**
     * 划菜列表
     */
    private List<SalesOrderServingDishesE> ArrayOfSalesOrderServingDishesE;
    /**
     * 批次列表
     */
    private List<SalesOrderBatchDishesE> ArrayOfSalesOrderBatchDishesE;
    /**
     * 开关集合
     */
    private ParametersConfig ParametersConfig;
    /**
     * 外卖订单实体
     */
    private UnOrderE UnOrderE;
    /**
     * 外卖订单列表
     */
    private List<UnOrderE> ArrayOfUnOrderE;
    /**
     * 外卖单催菜内容实体列表
     */
    private List<UnReminderReplyContentE> ArrayOfUnReminderReplyContentE;
    /**
     * 外卖单催菜内容实体
     */
    private UnReminderReplyContentE UnReminderReplyContentE;
    /**
     * 外卖菜品实体
     */
    private UnOrderDishesE UnOrderDishesE;
    /**
     * 未处理消息列表
     */
    private List<UnMessageE> ArrayOfUnMessageE;
    private OrderCountE OrderCountE;

    private MemberModel MemberModel;

    public com.holderzone.intelligencepos.mvp.model.bean.MemberModel getMemberModel() {
        return MemberModel;
    }

    public void setMemberModel(com.holderzone.intelligencepos.mvp.model.bean.MemberModel memberModel) {
        MemberModel = memberModel;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.OrderCountE getOrderCountE() {
        return OrderCountE;
    }

    public void setOrderCountE(com.holderzone.intelligencepos.mvp.model.bean.OrderCountE orderCountE) {
        OrderCountE = orderCountE;
    }

    public List<UnMessageE> getArrayOfUnMessageE() {
        return ArrayOfUnMessageE;
    }

    public void setArrayOfUnMessageE(List<UnMessageE> arrayOfUnMessageE) {
        ArrayOfUnMessageE = arrayOfUnMessageE;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesE getUnOrderDishesE() {
        return UnOrderDishesE;
    }

    public void setUnOrderDishesE(com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesE unOrderDishesE) {
        UnOrderDishesE = unOrderDishesE;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE getUnReminderReplyContentE() {
        return UnReminderReplyContentE;
    }

    public void setUnReminderReplyContentE(com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE unReminderReplyContentE) {
        UnReminderReplyContentE = unReminderReplyContentE;
    }

    public List<UnReminderReplyContentE> getArrayOfUnReminderReplyContentE() {
        return ArrayOfUnReminderReplyContentE;
    }

    public void setArrayOfUnReminderReplyContentE(List<UnReminderReplyContentE> arrayOfUnReminderReplyContentE) {
        ArrayOfUnReminderReplyContentE = arrayOfUnReminderReplyContentE;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.UnOrderE getUnOrderE() {
        return UnOrderE;
    }

    public void setUnOrderE(com.holderzone.intelligencepos.mvp.model.bean.UnOrderE unOrderE) {
        UnOrderE = unOrderE;
    }

    public List<com.holderzone.intelligencepos.mvp.model.bean.UnOrderE> getArrayOfUnOrderE() {
        return ArrayOfUnOrderE;
    }

    public void setArrayOfUnOrderE(List<com.holderzone.intelligencepos.mvp.model.bean.UnOrderE> arrayOfUnOrderE) {
        ArrayOfUnOrderE = arrayOfUnOrderE;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig getParametersConfig() {
        return ParametersConfig;
    }

    public void setParametersConfig(com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig parametersConfig) {
        ParametersConfig = parametersConfig;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.SysParametersE getSysParametersE() {
        return SysParametersE;
    }

    public void setSysParametersE(com.holderzone.intelligencepos.mvp.model.bean.SysParametersE sysParametersE) {
        SysParametersE = sysParametersE;
    }

    public List<SalesOrderBatchDishesE> getArrayOfSalesOrderBatchDishesE() {
        return ArrayOfSalesOrderBatchDishesE;
    }

    public void setArrayOfSalesOrderBatchDishesE(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
        ArrayOfSalesOrderBatchDishesE = arrayOfSalesOrderBatchDishesE;
    }

    public ApiBase getApiBase() {
        return ApiBase;
    }

    public void setApiBase(ApiBase apiBase) {
        ApiBase = apiBase;
    }

    public ApiNote getApiNote() {
        return ApiNote;
    }

    public void setApiNote(ApiNote apiNote) {
        ApiNote = apiNote;
    }

    public PageInfo getPageInfo() {
        return PageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        PageInfo = pageInfo;
    }

    public AppVersion getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(AppVersion appVersion) {
        AppVersion = appVersion;
    }

    public String getNonceStr() {
        return NonceStr;
    }

    public void setNonceStr(String nonceStr) {
        NonceStr = nonceStr;
    }

    public DeviceE getDeviceE() {
        return DeviceE;
    }

    public void setDeviceE(DeviceE deviceE) {
        DeviceE = deviceE;
    }

    public OrderRecordE getOrderRecordE() {
        return OrderRecordE;
    }

    public void setOrderRecordE(OrderRecordE orderRecordE) {
        OrderRecordE = orderRecordE;
    }

    public List<OrderRecordE> getArrayOfOrderRecordE() {
        return ArrayOfOrderRecordE;
    }

    public void setArrayOfOrderRecordE(List<OrderRecordE> arrayOfOrderRecordE) {
        ArrayOfOrderRecordE = arrayOfOrderRecordE;
    }

    public EnterpriseInfoE getEnterpriseInfoE() {
        return EnterpriseInfoE;
    }

    public void setEnterpriseInfoE(EnterpriseInfoE enterpriseInfoE) {
        EnterpriseInfoE = enterpriseInfoE;
    }

    public StoreE getStoreE() {
        return StoreE;
    }

    public void setStoreE(StoreE storeE) {
        StoreE = storeE;
    }

    public List<StoreE> getArrayOfStoreE() {
        return ArrayOfStoreE;
    }

    public void setArrayOfStoreE(List<StoreE> arrayOfStoreE) {
        ArrayOfStoreE = arrayOfStoreE;
    }

    public List<AdditionalFeesE> getArrayOfAdditionalFeesE() {
        return ArrayOfAdditionalFeesE;
    }

    public void setArrayOfAdditionalFeesE(List<AdditionalFeesE> arrayOfAdditionalFeesE) {
        ArrayOfAdditionalFeesE = arrayOfAdditionalFeesE;
    }

    public UsersE getUsersE() {
        return UsersE;
    }

    public void setUsersE(UsersE usersE) {
        UsersE = usersE;
    }

    public OrderRecordSectionE getOrderRecordSectionE() {
        return OrderRecordSectionE;
    }

    public void setOrderRecordSectionE(OrderRecordSectionE orderRecordSectionE) {
        OrderRecordSectionE = orderRecordSectionE;
    }

    public List<OrderRecordSectionE> getArrayOfOrderRecordSectionE() {
        return ArrayOfOrderRecordSectionE;
    }

    public void setArrayOfOrderRecordSectionE(List<OrderRecordSectionE> arrayOfOrderRecordSectionE) {
        ArrayOfOrderRecordSectionE = arrayOfOrderRecordSectionE;
    }

    public List<UsersE> getArrayOfUsersE() {
        return ArrayOfUsersE;
    }

    public void setArrayOfUsersE(List<UsersE> arrayOfUsersE) {
        ArrayOfUsersE = arrayOfUsersE;
    }

    public List<CouponsE> getArrayOfCouponsE() {
        return ArrayOfCouponsE;
    }

    public void setArrayOfCouponsE(List<CouponsE> arrayOfCouponsE) {
        ArrayOfCouponsE = arrayOfCouponsE;
    }

    public CouponsE getCouponsE() {
        return CouponsE;
    }

    public void setCouponsE(CouponsE couponsE) {
        CouponsE = couponsE;
    }

    public DiningTableAreaE getDiningTableAreaE() {
        return DiningTableAreaE;
    }

    public void setDiningTableAreaE(DiningTableAreaE diningTableAreaE) {
        DiningTableAreaE = diningTableAreaE;
    }

    public List<DiningTableAreaE> getArrayOfDiningTableAreaE() {
        return ArrayOfDiningTableAreaE;
    }

    public void setArrayOfDiningTableAreaE(List<DiningTableAreaE> arrayOfDiningTableAreaE) {
        ArrayOfDiningTableAreaE = arrayOfDiningTableAreaE;
    }

    public DiningTableE getDiningTableE() {
        return DiningTableE;
    }

    public void setDiningTableE(DiningTableE diningTableE) {
        DiningTableE = diningTableE;
    }

    public List<DiningTableE> getArrayOfDiningTableE() {
        return ArrayOfDiningTableE;
    }

    public void setArrayOfDiningTableE(List<DiningTableE> arrayOfDiningTableE) {
        ArrayOfDiningTableE = arrayOfDiningTableE;
    }

    public DishesTypeE getDishesTypeE() {
        return DishesTypeE;
    }

    public void setDishesTypeE(DishesTypeE dishesTypeE) {
        DishesTypeE = dishesTypeE;
    }

    public List<DishesTypeE> getArrayOfDishesTypeE() {
        return ArrayOfDishesTypeE;
    }

    public void setArrayOfDishesTypeE(List<DishesTypeE> arrayOfDishesTypeE) {
        ArrayOfDishesTypeE = arrayOfDishesTypeE;
    }

    public DishesE getDishesE() {
        return DishesE;
    }

    public void setDishesE(DishesE dishesE) {
        DishesE = dishesE;
    }

    public List<DishesE> getArrayOfDishesE() {
        return ArrayOfDishesE;
    }

    public void setArrayOfDishesE(List<DishesE> arrayOfDishesE) {
        ArrayOfDishesE = arrayOfDishesE;
    }

    public DishesRemarkE getDishesRemarkE() {
        return DishesRemarkE;
    }

    public void setDishesRemarkE(DishesRemarkE dishesRemarkE) {
        DishesRemarkE = dishesRemarkE;
    }

    public List<DishesRemarkE> getArrayOfDishesRemarkE() {
        return ArrayOfDishesRemarkE;
    }

    public void setArrayOfDishesRemarkE(List<DishesRemarkE> arrayOfDishesRemarkE) {
        ArrayOfDishesRemarkE = arrayOfDishesRemarkE;
    }

    public DishesPracticeE getDishesPracticeE() {
        return DishesPracticeE;
    }

    public void setDishesPracticeE(DishesPracticeE dishesPracticeE) {
        DishesPracticeE = dishesPracticeE;
    }

    public List<DishesPracticeE> getArrayOfDishesPracticeE() {
        return ArrayOfDishesPracticeE;
    }

    public void setArrayOfDishesPracticeE(List<DishesPracticeE> arrayOfDishesPracticeE) {
        ArrayOfDishesPracticeE = arrayOfDishesPracticeE;
    }

    public DishesReturnReasonE getDishesReturnReasonE() {
        return DishesReturnReasonE;
    }

    public void setDishesReturnReasonE(DishesReturnReasonE dishesReturnReasonE) {
        DishesReturnReasonE = dishesReturnReasonE;
    }

    public List<DishesReturnReasonE> getArrayOfDishesReturnReasonE() {
        return ArrayOfDishesReturnReasonE;
    }

    public void setArrayOfDishesReturnReasonE(List<DishesReturnReasonE> arrayOfDishesReturnReasonE) {
        ArrayOfDishesReturnReasonE = arrayOfDishesReturnReasonE;
    }

    public PaymentItemE getPaymentItemE() {
        return PaymentItemE;
    }

    public void setPaymentItemE(PaymentItemE paymentItemE) {
        PaymentItemE = paymentItemE;
    }

    public List<PaymentItemE> getArrayOfPaymentItemE() {
        return ArrayOfPaymentItemE;
    }

    public void setArrayOfPaymentItemE(List<PaymentItemE> arrayOfPaymentItemE) {
        ArrayOfPaymentItemE = arrayOfPaymentItemE;
    }

    public SalesOrderAdditionalFeesE getSalesOrderAdditionalFeesE() {
        return SalesOrderAdditionalFeesE;
    }

    public void setSalesOrderAdditionalFeesE(SalesOrderAdditionalFeesE salesOrderAdditionalFeesE) {
        SalesOrderAdditionalFeesE = salesOrderAdditionalFeesE;
    }

    public List<SalesOrderAdditionalFeesE> getArrayOfSalesOrderAdditionalFeesE() {
        return ArrayOfSalesOrderAdditionalFeesE;
    }

    public void setArrayOfSalesOrderAdditionalFeesE(List<SalesOrderAdditionalFeesE> arrayOfSalesOrderAdditionalFeesE) {
        ArrayOfSalesOrderAdditionalFeesE = arrayOfSalesOrderAdditionalFeesE;
    }

    public List<com.holderzone.intelligencepos.mvp.model.bean.PrinterConfigE> getArrayOfPrinterConfigE() {
        return ArrayOfPrinterConfigE;
    }

    public void setArrayOfPrinterConfigE(List<com.holderzone.intelligencepos.mvp.model.bean.PrinterConfigE> arrayOfPrinterConfigE) {
        ArrayOfPrinterConfigE = arrayOfPrinterConfigE;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.PrinterConfigE getPrinterConfigE() {
        return PrinterConfigE;
    }

    public void setPrinterConfigE(com.holderzone.intelligencepos.mvp.model.bean.PrinterConfigE printerConfigE) {
        PrinterConfigE = printerConfigE;
    }

    public SalesOrderE getSalesOrderE() {
        return SalesOrderE;
    }

    public void setSalesOrderE(SalesOrderE salesOrderE) {
        SalesOrderE = salesOrderE;
    }

    public List<SalesOrderE> getArrayOfSalesOrderE() {
        return ArrayOfSalesOrderE;
    }

    public void setArrayOfSalesOrderE(List<SalesOrderE> arrayOfSalesOrderE) {
        ArrayOfSalesOrderE = arrayOfSalesOrderE;
    }

    public SalesOrderBatchE getSalesOrderBatchE() {
        return SalesOrderBatchE;
    }

    public void setSalesOrderBatchE(SalesOrderBatchE salesOrderBatchE) {
        SalesOrderBatchE = salesOrderBatchE;
    }

    public List<SalesOrderBatchE> getArrayOfSalesOrderBatchE() {
        return ArrayOfSalesOrderBatchE;
    }

    public void setArrayOfSalesOrderBatchE(List<SalesOrderBatchE> arrayOfSalesOrderBatchE) {
        ArrayOfSalesOrderBatchE = arrayOfSalesOrderBatchE;
    }

    public SalesOrderPaymentE getSalesOrderPaymentE() {
        return SalesOrderPaymentE;
    }

    public void setSalesOrderPaymentE(SalesOrderPaymentE salesOrderPaymentE) {
        SalesOrderPaymentE = salesOrderPaymentE;
    }

    public List<SalesOrderPaymentE> getArrayOfSalesOrderPaymentE() {
        return ArrayOfSalesOrderPaymentE;
    }

    public void setArrayOfSalesOrderPaymentE(List<SalesOrderPaymentE> arrayOfSalesOrderPaymentE) {
        ArrayOfSalesOrderPaymentE = arrayOfSalesOrderPaymentE;
    }

    public PrinterTypeE getPrinterTypeE() {
        return PrinterTypeE;
    }

    public void setPrinterTypeE(PrinterTypeE printerTypeE) {
        PrinterTypeE = printerTypeE;
    }

    public List<PrinterTypeE> getArrayOfPrinterTypeE() {
        return ArrayOfPrinterTypeE;
    }

    public void setArrayOfPrinterTypeE(List<PrinterTypeE> arrayOfPrinterTypeE) {
        ArrayOfPrinterTypeE = arrayOfPrinterTypeE;
    }

    public PrinterE getPrinterE() {
        return PrinterE;
    }

    public void setPrinterE(PrinterE printerE) {
        PrinterE = printerE;
    }

    public List<PrinterE> getArrayOfPrinterE() {
        return ArrayOfPrinterE;
    }

    public void setArrayOfPrinterE(List<PrinterE> arrayOfPrinterE) {
        ArrayOfPrinterE = arrayOfPrinterE;
    }

    public Dishes getDishes() {
        return Dishes;
    }

    public void setDishes(Dishes dishes) {
        Dishes = dishes;
    }

    public List<Dishes> getArrayOfDishes() {
        return ArrayOfDishes;
    }

    public void setArrayOfDishes(List<Dishes> arrayOfDishes) {
        ArrayOfDishes = arrayOfDishes;
    }

    public CardsE getCardsE() {
        return CardsE;
    }

    public void setCardsE(CardsE cardsE) {
        CardsE = cardsE;
    }

    public List<CardsE> getArrayOfCardsE() {
        return ArrayOfCardsE;
    }

    public void setArrayOfCardsE(List<CardsE> arrayOfCardsE) {
        ArrayOfCardsE = arrayOfCardsE;
    }

    public List<CardTypeE> getArrayOfCardTypeE() {
        return ArrayOfCardTypeE;
    }

    public void setArrayOfCardTypeE(List<CardTypeE> arrayOfCardTypeE) {
        ArrayOfCardTypeE = arrayOfCardTypeE;
    }

    public CardSellOrderE getCardSellOrderE() {
        return CardSellOrderE;
    }

    public void setCardSellOrderE(CardSellOrderE cardSellOrderE) {
        CardSellOrderE = cardSellOrderE;
    }

    public List<SalesOrderDiscountE> getArrayOfSalesOrderDiscountE() {
        return ArrayOfSalesOrderDiscountE;
    }

    public void setArrayOfSalesOrderDiscountE(List<SalesOrderDiscountE> arrayOfSalesOrderDiscountE) {
        ArrayOfSalesOrderDiscountE = arrayOfSalesOrderDiscountE;
    }

    public List<SalesOrderCouponsUseE> getArrayOfSalesOrderCouponsUseE() {
        return ArrayOfSalesOrderCouponsUseE;
    }

    public void setArrayOfSalesOrderCouponsUseE(List<SalesOrderCouponsUseE> arrayOfSalesOrderCouponsUseE) {
        ArrayOfSalesOrderCouponsUseE = arrayOfSalesOrderCouponsUseE;
    }

    public List<ViewCardBusinessOrderE> getArrayOfViewCardBusinessOrderE() {
        return ArrayOfViewCardBusinessOrderE;
    }

    public void setArrayOfViewCardBusinessOrderE(List<ViewCardBusinessOrderE> arrayOfViewCardBusinessOrderE) {
        ArrayOfViewCardBusinessOrderE = arrayOfViewCardBusinessOrderE;
    }

    public DishesEstimateRecordE getDishesEstimateRecordE() {
        return DishesEstimateRecordE;
    }

    public void setDishesEstimateRecordE(DishesEstimateRecordE dishesEstimateRecordE) {
        DishesEstimateRecordE = dishesEstimateRecordE;
    }

    public List<DishesEstimateRecordE> getArrayOfDishesEstimateRecordE() {
        return ArrayOfDishesEstimateRecordE;
    }

    public void setArrayOfDishesEstimateRecordE(List<DishesEstimateRecordE> arrayOfDishesEstimateRecordE) {
        ArrayOfDishesEstimateRecordE = arrayOfDishesEstimateRecordE;
    }

    public List<SalesOrderDishesE> getArrayOfSalesOrderDishesE() {
        return ArrayOfSalesOrderDishesE;
    }

    public void setArrayOfSalesOrderDishesE(List<SalesOrderDishesE> arrayOfSalesOrderDishesE) {
        ArrayOfSalesOrderDishesE = arrayOfSalesOrderDishesE;
    }

    public List<DishesEstimateRecordDishes> getArrayOfDishesEstimateRecordDishes() {
        return ArrayOfDishesEstimateRecordDishes;
    }

    public void setArrayOfDishesEstimateRecordDishes(List<DishesEstimateRecordDishes> arrayOfDishesEstimateRecordDishes) {
        ArrayOfDishesEstimateRecordDishes = arrayOfDishesEstimateRecordDishes;
    }

    public AccountRecordE getAccountRecordE() {
        return AccountRecordE;
    }

    public void setAccountRecordE(AccountRecordE accountRecordE) {
        AccountRecordE = accountRecordE;
    }

    public List<AccountRecordE> getArrayOfAccountRecordE() {
        return ArrayOfAccountRecordE;
    }

    public void setArrayOfAccountRecordE(List<AccountRecordE> arrayOfAccountRecordE) {
        ArrayOfAccountRecordE = arrayOfAccountRecordE;
    }

    public ShiftConfigE getShiftConfigE() {
        return ShiftConfigE;
    }

    public void setShiftConfigE(ShiftConfigE shiftConfigE) {
        ShiftConfigE = shiftConfigE;
    }

    public List<ShiftConfigE> getArrayOfShiftConfigE() {
        return ArrayOfShiftConfigE;
    }

    public void setArrayOfShiftConfigE(List<ShiftConfigE> arrayOfShiftConfigE) {
        ArrayOfShiftConfigE = arrayOfShiftConfigE;
    }

    public ShiftRecordE getShiftRecordE() {
        return ShiftRecordE;
    }

    public void setShiftRecordE(ShiftRecordE shiftRecordE) {
        ShiftRecordE = shiftRecordE;
    }

    public List<ShiftRecordE> getArrayOfShiftRecordE() {
        return ArrayOfShiftRecordE;
    }

    public void setArrayOfShiftRecordE(List<ShiftRecordE> arrayOfShiftRecordE) {
        ArrayOfShiftRecordE = arrayOfShiftRecordE;
    }

    public OrderRecordRemarkItemE getOrderRecordRemarkItemE() {
        return OrderRecordRemarkItemE;
    }

    public void setOrderRecordRemarkItemE(OrderRecordRemarkItemE orderRecordRemarkItemE) {
        OrderRecordRemarkItemE = orderRecordRemarkItemE;
    }

    public List<OrderRecordRemarkItemE> getArrayOfOrderRecordRemarkItemE() {
        return ArrayOfOrderRecordRemarkItemE;
    }

    public void setArrayOfOrderRecordRemarkItemE(List<OrderRecordRemarkItemE> arrayOfOrderRecordRemarkItemE) {
        ArrayOfOrderRecordRemarkItemE = arrayOfOrderRecordRemarkItemE;
    }

    public DinnerE getDinnerE() {
        return DinnerE;
    }

    public void setDinnerE(DinnerE dinnerE) {
        DinnerE = dinnerE;
    }

    public MemberInfoE getMemberInfoE() {
        return MemberInfoE;
    }

    public void setMemberInfoE(MemberInfoE memberInfoE) {
        MemberInfoE = memberInfoE;
    }

    public AdditionalFeesE getAdditionalFeesE() {
        return AdditionalFeesE;
    }

    public void setAdditionalFeesE(AdditionalFeesE additionalFeesE) {
        AdditionalFeesE = additionalFeesE;
    }

    public SalesOrderServingDishesE getSalesOrderServingDishesE() {
        return SalesOrderServingDishesE;
    }

    public void setSalesOrderServingDishesE(SalesOrderServingDishesE salesOrderServingDishesE) {
        SalesOrderServingDishesE = salesOrderServingDishesE;
    }

    public List<SalesOrderServingDishesE> getArrayOfSalesOrderServingDishesE() {
        return ArrayOfSalesOrderServingDishesE;
    }

    public void setArrayOfSalesOrderServingDishesE(List<SalesOrderServingDishesE> arrayOfSalesOrderServingDishesE) {
        ArrayOfSalesOrderServingDishesE = arrayOfSalesOrderServingDishesE;
    }

    public EquipmentsE getEquipmentsE() {
        return EquipmentsE;
    }

    public void setEquipmentsE(EquipmentsE equipmentsE) {
        EquipmentsE = equipmentsE;
    }

    public CardBusinessOrderE getCardBusinessOrderE() {
        return CardBusinessOrderE;
    }

    public void setCardBusinessOrderE(CardBusinessOrderE cardBusinessOrderE) {
        CardBusinessOrderE = cardBusinessOrderE;
    }

    public QueueUpTypeE getQueueUpTypeE() {
        return QueueUpTypeE;
    }

    public void setQueueUpTypeE(QueueUpTypeE queueUpTypeE) {
        QueueUpTypeE = queueUpTypeE;
    }

    public List<QueueUpTypeE> getArrayOfQueueUpTypeE() {
        return ArrayOfQueueUpTypeE;
    }

    public void setArrayOfQueueUpTypeE(List<QueueUpTypeE> arrayOfQueueUpTypeE) {
        ArrayOfQueueUpTypeE = arrayOfQueueUpTypeE;
    }

    public QueueUpRecordE getQueueUpRecordE() {
        return QueueUpRecordE;
    }

    public void setQueueUpRecordE(QueueUpRecordE queueUpRecordE) {
        QueueUpRecordE = queueUpRecordE;
    }

    public List<SalesOrderBatchN> getArrayOfSalesOrderBatchN() {
        return ArrayOfSalesOrderBatchN;
    }

    public void setArrayOfSalesOrderBatchN(List<SalesOrderBatchN> arrayOfSalesOrderBatchN) {
        ArrayOfSalesOrderBatchN = arrayOfSalesOrderBatchN;
    }

    public List<QueueUpRecordE> getArrayOfQueueUpRecordE() {
        return ArrayOfQueueUpRecordE;
    }

    public void setArrayOfQueueUpRecordE(List<QueueUpRecordE> arrayOfQueueUpRecordE) {
        ArrayOfQueueUpRecordE = arrayOfQueueUpRecordE;
    }

    public CardTypeE getCardTypeE() {
        return CardTypeE;
    }

    public void setCardTypeE(CardTypeE cardTypeE) {
        CardTypeE = cardTypeE;
    }

    public List<QueueUpReportE> getArrayOfQueueUpReportE() {
        return ArrayOfQueueUpReportE;
    }

    public void setArrayOfQueueUpReportE(List<QueueUpReportE> arrayOfQueueUpReportE) {
        ArrayOfQueueUpReportE = arrayOfQueueUpReportE;
    }

    public List<BusinessDaySummaryE> getArrayOfBusinessDaySummaryE() {
        return ArrayOfBusinessDaySummaryE;
    }

    public void setArrayOfBusinessDaySummaryE(List<BusinessDaySummaryE> arrayOfBusinessDaySummaryE) {
        ArrayOfBusinessDaySummaryE = arrayOfBusinessDaySummaryE;
    }

    public BusinessDaySummaryE getBusinessDaySummaryE() {
        return BusinessDaySummaryE;
    }

    public void setBusinessDaySummaryE(BusinessDaySummaryE businessDaySummaryE) {
        BusinessDaySummaryE = businessDaySummaryE;
    }

    public MeituanCoupon getMeituanCoupon() {
        return MeituanCoupon;
    }

    public void setMeituanCoupon(MeituanCoupon meituanCoupon) {
        MeituanCoupon = meituanCoupon;
    }

    public List<MeituanCoupon> getArrayOfMeituanCoupon() {
        return ArrayOfMeituanCoupon;
    }

    public void setArrayOfMeituanCoupon(List<MeituanCoupon> arrayOfMeituanCoupon) {
        ArrayOfMeituanCoupon = arrayOfMeituanCoupon;
    }

    public SalesMsgReadRecordE getSalesMsgReadRecordE() {
        return SalesMsgReadRecordE;
    }

    public void setSalesMsgReadRecordE(SalesMsgReadRecordE salesMsgReadRecordE) {
        SalesMsgReadRecordE = salesMsgReadRecordE;
    }

    public List<StateMsg> getArrayOfStateMsg() {
        return ArrayOfStateMsg;
    }

    public void setArrayOfStateMsg(List<StateMsg> arrayOfStateMsg) {
        ArrayOfStateMsg = arrayOfStateMsg;
    }

    public static Builder Builder() {
        return new Builder(true);
    }

    public static Builder Builder(boolean fbWithMerID) {
        return new Builder(fbWithMerID);
    }

    public static class Builder {
        private XmlData mXmlData;
        private ApiBase mApiBase;
        private AppVersion mAppVersion;
        private PageInfo mPageInfo;
        private String mNonceStr;

        private String mRequestMethod;
        private Object mRequestBody;

        public Builder(boolean fbWithMerID) {
            mXmlData = new XmlData();
            mApiBase = new ApiBase();
            if (fbWithMerID) {
                mApiBase.setModel("FrontendB");
            } else {
                mApiBase.setModel("FrontendB_Public");
            }
            //设置终端类型
            if (DeviceHelper.getInstance().getDeviceID().startsWith("V")) {
                //V1
                mApiBase.setTerminalType(21);
            } else if (DeviceHelper.getInstance().getDeviceID().startsWith("P")) {
                //P1
                mApiBase.setTerminalType(24);
            }
            mApiBase.setTerminalID(DeviceHelper.getInstance().getDeviceID());
            mApiBase.setBackType("1");
            mAppVersion = new AppVersion();
            mAppVersion.setVersionType(1);
            mPageInfo = new PageInfo();
            mNonceStr = Security.getStr(16, 32);
        }

        public Builder setRequestMethod(String requestMethod) {
            mRequestMethod = requestMethod;
            return this;
        }

        public Builder setRequestBody(Object requestBody) {
            mRequestBody = requestBody;
            return this;
        }

        public Observable<XmlData> buildRESTful() {
            Observable<Optional<EnterpriseInfo>> first = RepositoryImpl.getInstance().getOptionalEnterpriseInfo();
            Observable<Optional<Store>> second = RepositoryImpl.getInstance().getOptionalStore();
            return Observable.zip(first, second, Pair::new)
                    .flatMap(pair -> Observable.create(emitter -> {
                        mApiBase.setMethod(mRequestMethod);
                        if ("FrontendB".equalsIgnoreCase(mApiBase.getModel())) {
                            mApiBase.setMerID(pair.first.get().getEnterpriseInfoUID());
                        }
                        mXmlData.setApiBase(mApiBase);
                        mXmlData.setAppVersion(mAppVersion);
                        mXmlData.setPageInfo(mPageInfo);
                        mXmlData.setNonceStr(mNonceStr);
                        if ("FrontendB".equalsIgnoreCase(mApiBase.getModel())) {
                            // 利用反射给每个Body设置EnterpriseInfoGUID和StoreGUID，若没有对应函数，则抛出异常
                            try {
                                Method getEnterpriseInfoGUIDMethod = mRequestBody.getClass().getMethod("getEnterpriseInfoGUID");
                                String enterpriseInfoGUID = (String) getEnterpriseInfoGUIDMethod.invoke(mRequestBody);
                                if (enterpriseInfoGUID == null) {
                                    Method setEnterpriseInfoGUIDMethod = mRequestBody.getClass().getMethod("setEnterpriseInfoGUID", String.class);
                                    setEnterpriseInfoGUIDMethod.invoke(mRequestBody, pair.first.get().getEnterpriseInfoGUID());
                                }
                            } catch (Exception e) {
                            }
                            if (pair.second.isPresent()) {
                                try {
                                    Method getStoreGUIDMethod = mRequestBody.getClass().getMethod("getStoreGUID");
                                    String storeGUID = (String) getStoreGUIDMethod.invoke(mRequestBody);
                                    if (storeGUID == null) {
                                        Method setStoreGUIDMethod = mRequestBody.getClass().getMethod("setStoreGUID", String.class);
                                        setStoreGUIDMethod.invoke(mRequestBody, pair.second.get().getStoreGUID());
                                    }
                                } catch (Exception e) {
                                }
                            }
                            try {
                                Method getDeviceIDMethod = mRequestBody.getClass().getMethod("getDeviceID");
                                String deviceID = (String) getDeviceIDMethod.invoke(mRequestBody);
                                if (deviceID == null) {
                                    Method setDeviceIDMethod = mRequestBody.getClass().getMethod("setDeviceID", String.class);
                                    setDeviceIDMethod.invoke(mRequestBody, DeviceHelper.getInstance().getDeviceID());
                                }
                            } catch (Exception e) {
                            }
                        }
                        // 利用反射给XmlData设置相应的请求实体
                        try {
                            String name = mRequestMethod.split("\\.")[0];
                            StringBuilder requestName = new StringBuilder();
                            requestName.append("set");
                            requestName.append(name.substring(0, name.length() - 1));
                            requestName.append("E");
                            Method method = mXmlData.getClass().getMethod(requestName.toString(), mRequestBody.getClass());
                            method.invoke(mXmlData, mRequestBody);
                        } catch (NoSuchMethodException e) {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        emitter.onNext(mXmlData);
                        emitter.onComplete();
                    }));
        }
    }
}
