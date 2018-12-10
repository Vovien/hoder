package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrder implements Parcelable {
    /**
     * 自增ID
     */
    private Integer UnOrderID;

    /**
     * 编号
     */
    private String UnOrderUID;

    /**
     * 标识Guid 主键
     */
    private String UnOrderGUID;

    /**
     * 商家Id
     */
    private String EnterpriseId;

    /**
     * 门店Id
     */
    private String StoreId;

    /**
     * 订单Id
     */
    private String OrderID;

    /**
     * 流水号
     */
    private String OrderSn;

    /**
     * 日流水
     */
    private String DaySn;

    /**
     * 下单时间戳
     */
    private Long CreateOrderTimeStamp;

    /**
     * 是否预订单
     * 0=否 1=是
     */
    private Integer IsReserve;

    /**
     * 预订单生效/激活时间  非预订单，同下单时间
     */
    private Long ActivationTimeStamp;

    /**
     * 订单类型
     * 0=外卖订单  1=微信订单  2=其他订单
     */
    private Integer OrderType;

    /**
     * 订单子类
     * OrderType=0： 0=美团      1=饿了么  2=百度  3=京东
     * OrderType=1： 0=扫码订单  1=微信预订单
     */
    private Integer OrderSubType;

    /**
     * 订单最后更新时间戳
     */
    private Long UpdateTimeStamp;

    /**
     * 订单状态
     * 0	待处理,
     * -1	已取消,
     * 10	已接单,
     * 20	配送中,
     * 100	已完成,
     * -200 已退单
     */
    private Integer OrderStatus;

    /**
     * 首单  0=否 1=是 -1=未知
     */
    private Integer FirstOrder;

    /**
     * 用餐人数
     */
    private Integer CustomerNumber;

    /**
     * 顾客姓名
     */
    private String CustomerName;

    /**
     * 配送地址
     */
    private String CustomerAddress;

    /**
     * 顾客联系电话 多个用“,”分隔
     */
    private String CustomerPhone;

    /**
     * 配送地址纬度
     */
    private String Latitude;

    /**
     * 配送地址经度
     */
    private String Longitude;

    /**
     * 配送员姓名
     */
    private String ShipperName;

    /**
     * 配送员电话
     */
    private String ShipperPhone;

    /**
     * 第三方配送  0=否  1=是  -1=未知
     */
    private Integer IsThirdShipper;

    /**
     * 预计送达时间戳 0 =为立即配送  0> =具体的配送时间
     */
    private Long DeliveryTimeStamp;

    /**
     * 是否开发票 0=无发票  1=有发票
     */
    private Integer HasInvoiced;

    /**
     * 发票类型
     * 0=个人 1=企业
     */
    private Integer InvoiceType;

    /**
     * 发票抬头
     */
    private String InvoiceTitle;

    /**
     * 总价
     * 包括：菜品(原价) + 餐盒 + 配送
     */
    private Double Total;

    /**
     * 配送费
     */
    private Double ShippingFeeTotal;

    /**
     * 菜品消费合计(不含餐盒费)
     */
    private Double DishesTotal;

    /**
     * 餐盒费
     */
    private Double PackageFeeTotal;

    /**
     * 服务费率(平台抽成比例)
     * 0.15=15%
     */
    private Double ServiceFeeRate;

    /**
     * 服务费(平台抽成费用)
     */
    private Double ServiceFee;

    /**
     * 折扣合计
     * EnterpriseDiscount + PlatformDiscount + OtherDiscount
     */
    private Double DiscountTotal;

    /**
     * 商家承担的折扣金额
     */
    private Double EnterpriseDiscount;

    /**
     * 外卖平台承担的折扣金额
     */
    private Double PlatformDiscount;

    /**
     * 其他折扣金额
     */
    private Double OtherDiscount;

    /**
     * 顾客实际支付金额
     */
    private Double CustomerActualPay;

    /**
     * 门店收款金额
     */
    private Double ShopTotal;

    /**
     * 是否在线支付  1=在线支付  0=线下付款
     */
    private Integer IsOnlinePay;

    /**
     * 备注
     */
    private String Remark;

    /**
     * Api版本
     */
    private String ApiVer;

    /**
     * 企业Guid
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店Guid
     */
    private String StoreGUID;

    /**
     * 餐桌Guid
     */
    private String DiningTableGUID;

    public Integer getUnOrderID() {
        return UnOrderID;
    }

    public void setUnOrderID(Integer unOrderID) {
        UnOrderID = unOrderID;
    }

    public String getUnOrderUID() {
        return UnOrderUID;
    }

    public void setUnOrderUID(String unOrderUID) {
        UnOrderUID = unOrderUID;
    }

    public String getUnOrderGUID() {
        return UnOrderGUID;
    }

    public void setUnOrderGUID(String unOrderGUID) {
        UnOrderGUID = unOrderGUID;
    }

    public String getEnterpriseId() {
        return EnterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        EnterpriseId = enterpriseId;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderSn() {
        return OrderSn;
    }

    public void setOrderSn(String orderSn) {
        OrderSn = orderSn;
    }

    public String getDaySn() {
        return DaySn;
    }

    public void setDaySn(String daySn) {
        DaySn = daySn;
    }

    public Long getCreateOrderTimeStamp() {
        return CreateOrderTimeStamp;
    }

    public void setCreateOrderTimeStamp(Long createOrderTimeStamp) {
        CreateOrderTimeStamp = createOrderTimeStamp;
    }

    public Integer getIsReserve() {
        return IsReserve;
    }

    public void setIsReserve(Integer isReserve) {
        IsReserve = isReserve;
    }

    public Long getActivationTimeStamp() {
        return ActivationTimeStamp;
    }

    public void setActivationTimeStamp(Long activationTimeStamp) {
        ActivationTimeStamp = activationTimeStamp;
    }

    public Integer getOrderType() {
        return OrderType;
    }

    public void setOrderType(Integer orderType) {
        OrderType = orderType;
    }

    public Integer getOrderSubType() {
        return OrderSubType;
    }

    public void setOrderSubType(Integer orderSubType) {
        OrderSubType = orderSubType;
    }

    public Long getUpdateTimeStamp() {
        return UpdateTimeStamp;
    }

    public void setUpdateTimeStamp(Long updateTimeStamp) {
        UpdateTimeStamp = updateTimeStamp;
    }

    public Integer getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        OrderStatus = orderStatus;
    }

    public Integer getFirstOrder() {
        return FirstOrder;
    }

    public void setFirstOrder(Integer firstOrder) {
        FirstOrder = firstOrder;
    }

    public Integer getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getShipperPhone() {
        return ShipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        ShipperPhone = shipperPhone;
    }

    public Integer getIsThirdShipper() {
        return IsThirdShipper;
    }

    public void setIsThirdShipper(Integer isThirdShipper) {
        IsThirdShipper = isThirdShipper;
    }

    public Long getDeliveryTimeStamp() {
        return DeliveryTimeStamp;
    }

    public void setDeliveryTimeStamp(Long deliveryTimeStamp) {
        DeliveryTimeStamp = deliveryTimeStamp;
    }

    public Integer getHasInvoiced() {
        return HasInvoiced;
    }

    public void setHasInvoiced(Integer hasInvoiced) {
        HasInvoiced = hasInvoiced;
    }

    public Integer getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        InvoiceType = invoiceType;
    }

    public String getInvoiceTitle() {
        return InvoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        InvoiceTitle = invoiceTitle;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Double getShippingFeeTotal() {
        return ShippingFeeTotal;
    }

    public void setShippingFeeTotal(Double shippingFeeTotal) {
        ShippingFeeTotal = shippingFeeTotal;
    }

    public Double getDishesTotal() {
        return DishesTotal;
    }

    public void setDishesTotal(Double dishesTotal) {
        DishesTotal = dishesTotal;
    }

    public Double getPackageFeeTotal() {
        return PackageFeeTotal;
    }

    public void setPackageFeeTotal(Double packageFeeTotal) {
        PackageFeeTotal = packageFeeTotal;
    }

    public Double getServiceFeeRate() {
        return ServiceFeeRate;
    }

    public void setServiceFeeRate(Double serviceFeeRate) {
        ServiceFeeRate = serviceFeeRate;
    }

    public Double getServiceFee() {
        return ServiceFee;
    }

    public void setServiceFee(Double serviceFee) {
        ServiceFee = serviceFee;
    }

    public Double getDiscountTotal() {
        return DiscountTotal;
    }

    public void setDiscountTotal(Double discountTotal) {
        DiscountTotal = discountTotal;
    }

    public Double getEnterpriseDiscount() {
        return EnterpriseDiscount;
    }

    public void setEnterpriseDiscount(Double enterpriseDiscount) {
        EnterpriseDiscount = enterpriseDiscount;
    }

    public Double getPlatformDiscount() {
        return PlatformDiscount;
    }

    public void setPlatformDiscount(Double platformDiscount) {
        PlatformDiscount = platformDiscount;
    }

    public Double getOtherDiscount() {
        return OtherDiscount;
    }

    public void setOtherDiscount(Double otherDiscount) {
        OtherDiscount = otherDiscount;
    }

    public Double getCustomerActualPay() {
        return CustomerActualPay;
    }

    public void setCustomerActualPay(Double customerActualPay) {
        CustomerActualPay = customerActualPay;
    }

    public Double getShopTotal() {
        return ShopTotal;
    }

    public void setShopTotal(Double shopTotal) {
        ShopTotal = shopTotal;
    }

    public Integer getIsOnlinePay() {
        return IsOnlinePay;
    }

    public void setIsOnlinePay(Integer isOnlinePay) {
        IsOnlinePay = isOnlinePay;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getApiVer() {
        return ApiVer;
    }

    public void setApiVer(String apiVer) {
        ApiVer = apiVer;
    }

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

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.UnOrderID);
        dest.writeString(this.UnOrderUID);
        dest.writeString(this.UnOrderGUID);
        dest.writeString(this.EnterpriseId);
        dest.writeString(this.StoreId);
        dest.writeString(this.OrderID);
        dest.writeString(this.OrderSn);
        dest.writeString(this.DaySn);
        dest.writeValue(this.CreateOrderTimeStamp);
        dest.writeValue(this.IsReserve);
        dest.writeValue(this.ActivationTimeStamp);
        dest.writeValue(this.OrderType);
        dest.writeValue(this.OrderSubType);
        dest.writeValue(this.UpdateTimeStamp);
        dest.writeValue(this.OrderStatus);
        dest.writeValue(this.FirstOrder);
        dest.writeValue(this.CustomerNumber);
        dest.writeString(this.CustomerName);
        dest.writeString(this.CustomerAddress);
        dest.writeString(this.CustomerPhone);
        dest.writeString(this.Latitude);
        dest.writeString(this.Longitude);
        dest.writeString(this.ShipperName);
        dest.writeString(this.ShipperPhone);
        dest.writeValue(this.IsThirdShipper);
        dest.writeValue(this.DeliveryTimeStamp);
        dest.writeValue(this.HasInvoiced);
        dest.writeValue(this.InvoiceType);
        dest.writeString(this.InvoiceTitle);
        dest.writeValue(this.Total);
        dest.writeValue(this.ShippingFeeTotal);
        dest.writeValue(this.DishesTotal);
        dest.writeValue(this.PackageFeeTotal);
        dest.writeValue(this.ServiceFeeRate);
        dest.writeValue(this.ServiceFee);
        dest.writeValue(this.DiscountTotal);
        dest.writeValue(this.EnterpriseDiscount);
        dest.writeValue(this.PlatformDiscount);
        dest.writeValue(this.OtherDiscount);
        dest.writeValue(this.CustomerActualPay);
        dest.writeValue(this.ShopTotal);
        dest.writeValue(this.IsOnlinePay);
        dest.writeString(this.Remark);
        dest.writeString(this.ApiVer);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.DiningTableGUID);
    }

    public UnOrder() {
    }

    protected UnOrder(Parcel in) {
        this.UnOrderID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UnOrderUID = in.readString();
        this.UnOrderGUID = in.readString();
        this.EnterpriseId = in.readString();
        this.StoreId = in.readString();
        this.OrderID = in.readString();
        this.OrderSn = in.readString();
        this.DaySn = in.readString();
        this.CreateOrderTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.IsReserve = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ActivationTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.OrderType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrderSubType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UpdateTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.OrderStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.FirstOrder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CustomerNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CustomerName = in.readString();
        this.CustomerAddress = in.readString();
        this.CustomerPhone = in.readString();
        this.Latitude = in.readString();
        this.Longitude = in.readString();
        this.ShipperName = in.readString();
        this.ShipperPhone = in.readString();
        this.IsThirdShipper = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DeliveryTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.HasInvoiced = (Integer) in.readValue(Integer.class.getClassLoader());
        this.InvoiceType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.InvoiceTitle = in.readString();
        this.Total = (Double) in.readValue(Double.class.getClassLoader());
        this.ShippingFeeTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.PackageFeeTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.ServiceFeeRate = (Double) in.readValue(Double.class.getClassLoader());
        this.ServiceFee = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.EnterpriseDiscount = (Double) in.readValue(Double.class.getClassLoader());
        this.PlatformDiscount = (Double) in.readValue(Double.class.getClassLoader());
        this.OtherDiscount = (Double) in.readValue(Double.class.getClassLoader());
        this.CustomerActualPay = (Double) in.readValue(Double.class.getClassLoader());
        this.ShopTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.IsOnlinePay = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Remark = in.readString();
        this.ApiVer = in.readString();
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.DiningTableGUID = in.readString();
    }

    public static final Parcelable.Creator<UnOrder> CREATOR = new Parcelable.Creator<UnOrder>() {
        @Override
        public UnOrder createFromParcel(Parcel source) {
            return new UnOrder(source);
        }

        @Override
        public UnOrder[] newArray(int size) {
            return new UnOrder[size];
        }
    };
}
