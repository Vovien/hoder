package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NEW on 2017/4/6.
 */

public class CardsE implements Parcelable {


    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;
    private String SalesOrderGUID;
    /**
     * 卡类型名称
     */
    private String CardTypeName;
    /**
     * 卡号
     */
    private String CardsNumber;
    /**
     * 权益折扣
     */
    private Double RightsCiscount;
    /**
     * 支付折扣
     */
    private Double RightsDiscount;
    /**
     * 生效时间
     */
    private String EffectTime;
    /**
     * 是否过期
     * 0:过期
     * 1:未过期
     */
    public Integer IsAvailable;
    /**
     * 失效时间
     */
    private String InvalidTime;
    /**
     * 会员标识
     */
    private String MemberInfoGUID;
    /**
     * 卡标识
     */
    private String CardsChipNo;
    /**
     * 余额
     */
    private Double Balance;
    /**
     * 会员电话
     */
    private String RegTel;
    /**
     * 是否启用
     */
    private Integer IsEnable;
    /**
     * 是否删除
     */
    private Integer IsDelete;
    private String CardCreateRecordGUID;

    /**
     * 会员实体
     */
    private MemberInfoE MemberInfoE;

    /**
     * 卡类型实体
     */
    private CardTypeE CardTypeE;

    /**
     * 是否返回充值记录
     */
    private Integer ReturnInRecord;
    /**
     * 是否为记名卡（是否需要绑定会员账户）
     * 0：不记名卡
     * 1：记名卡
     */
    private Integer NeedBind;
    /**
     * 售价
     */
    private Double Price;
    /**
     * 面值
     */
    private Double FaceValue;
    /**
     * 是否返回消费记录
     */
    private Integer ReturnOutRecord;
    /**
     * 起始时段
     */
    private String QueryBeginDate;
    /**
     * 结束时段
     */
    private String QueryEndDate;
    /**
     * 卡类型标识
     */
    private String CardTypeGUID;
    /**
     * 会员卡收支记录视图实体
     */
    private List<ViewCardBusinessOrderE> ArrayOfViewCardBusinessOrderE;

    /**
     * 充值阶梯
     */
    private List<CardRechargeLadderE> ArrayOfCardRechargeLadderE;

    /**
     * 是否返回会员实体   0=不返回  1=返回
     */
    private Integer ReturnMemberInfo;
    /**
     * 是否返回所属卡类型实体   0=不返回  1=返回
     */
    private Integer ReturnCardType;
    /**
     * 是否与会员绑定   0=未绑定  1=绑定
     */
    private Integer BindState;

    /**
     * 需要使用的业务类型
     * 0=充值  1=刷卡支付  2 = 办理会员卡
     */
    private Integer UseBusinessType;

    /**
     * 可使用状态具体描述
     * 0=不可使用 1=可使用
     */
    private Integer CanUseStatus;

    /**
     * 可使用状态具体描述
     */
    private String CanUseStatusMsg;

    /**
     *  一次付款应收金额
     */
    private Double PayableAmount;

    /**
     * 一次付款的折扣金额
     */
    private Double DiscountAmount;

    /**
     * 一次付款的实收金额
     */
    private Double ActualyAmount;


    /**
     * 一次付款金额是否足够
     *    =0不足
     *    =1足够
     */
    private Integer IsEnough;

    public Double getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        PayableAmount = payableAmount;
    }

    public Double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public Double getActualyAmount() {
        return ActualyAmount;
    }

    public void setActualyAmount(Double actualyAmount) {
        ActualyAmount = actualyAmount;
    }

    public Integer getIsEnough() {
        return IsEnough;
    }

    public void setIsEnough(Integer isEnough) {
        IsEnough = isEnough;
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

    public String getCardTypeName() {
        return CardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        CardTypeName = cardTypeName;
    }

    public String getCardsNumber() {
        return CardsNumber;
    }

    public void setCardsNumber(String cardsNumber) {
        CardsNumber = cardsNumber;
    }

    public Double getRightsCiscount() {
        return RightsCiscount;
    }

    public void setRightsCiscount(Double rightsCiscount) {
        RightsCiscount = rightsCiscount;
    }

    public Double getRightcDiscount() {
        return RightsDiscount;
    }

    public void setRightcDiscount(Double rightcDiscount) {
        RightsDiscount = rightcDiscount;
    }

    public String getEffectTime() {
        return EffectTime;
    }

    public void setEffectTime(String effectTime) {
        EffectTime = effectTime;
    }

    public Integer getIsAvailable() {
        return IsAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        IsAvailable = isAvailable;
    }

    public String getInvalidTime() {
        return InvalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        InvalidTime = invalidTime;
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

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        Balance = balance;
    }

    public String getRegTel() {
        return RegTel;
    }

    public void setRegTel(String regTel) {
        RegTel = regTel;
    }

    public Integer getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(Integer isEnable) {
        IsEnable = isEnable;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    public String getCardCreateRecordGUID() {
        return CardCreateRecordGUID;
    }

    public void setCardCreateRecordGUID(String cardCreateRecordGUID) {
        CardCreateRecordGUID = cardCreateRecordGUID;
    }

    public MemberInfoE getMemberInfoE() {
        return MemberInfoE;
    }

    public void setMemberInfoE(MemberInfoE memberInfoE) {
        MemberInfoE = memberInfoE;
    }

    public CardTypeE getCardTypeE() {
        return CardTypeE;
    }

    public void setCardTypeE(CardTypeE cardTypeE) {
        CardTypeE = cardTypeE;
    }

    public Integer getReturnInRecord() {
        return ReturnInRecord;
    }

    public void setReturnInRecord(Integer returnInRecord) {
        ReturnInRecord = returnInRecord;
    }

    public Integer getNeedBind() {
        return NeedBind;
    }

    public void setNeedBind(Integer needBind) {
        NeedBind = needBind;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getFaceValue() {
        return FaceValue;
    }

    public void setFaceValue(Double faceValue) {
        FaceValue = faceValue;
    }

    public Integer getReturnOutRecord() {
        return ReturnOutRecord;
    }

    public void setReturnOutRecord(Integer returnOutRecord) {
        ReturnOutRecord = returnOutRecord;
    }

    public String getQueryBeginDate() {
        return QueryBeginDate;
    }

    public void setQueryBeginDate(String queryBeginDate) {
        QueryBeginDate = queryBeginDate;
    }

    public String getQueryEndDate() {
        return QueryEndDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        QueryEndDate = queryEndDate;
    }

    public String getCardTypeGUID() {
        return CardTypeGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public void setCardTypeGUID(String cardTypeGUID) {
        CardTypeGUID = cardTypeGUID;
    }

    public List<ViewCardBusinessOrderE> getArrayOfViewCardBusinessOrderE() {
        return ArrayOfViewCardBusinessOrderE;
    }

    public void setArrayOfViewCardBusinessOrderE(List<ViewCardBusinessOrderE> arrayOfViewCardBusinessOrderE) {
        ArrayOfViewCardBusinessOrderE = arrayOfViewCardBusinessOrderE;
    }

    public List<CardRechargeLadderE> getArrayOfCardRechargeLadderE() {
        return ArrayOfCardRechargeLadderE;
    }

    public void setArrayOfCardRechargeLadderE(List<CardRechargeLadderE> arrayOfCardRechargeLadderE) {
        ArrayOfCardRechargeLadderE = arrayOfCardRechargeLadderE;
    }

    public Integer getReturnMemberInfo() {
        return ReturnMemberInfo;
    }

    public void setReturnMemberInfo(Integer returnMemberInfo) {
        ReturnMemberInfo = returnMemberInfo;
    }

    public Integer getReturnCardType() {
        return ReturnCardType;
    }

    public void setReturnCardType(Integer returnCardType) {
        ReturnCardType = returnCardType;
    }

    public Double getRightsDiscount() {
        return RightsDiscount;
    }

    public void setRightsDiscount(Double rightsDiscount) {
        RightsDiscount = rightsDiscount;
    }

    public Integer getBindState() {
        return BindState;
    }

    public void setBindState(Integer bindState) {
        BindState = bindState;
    }

    public Integer getUseBusinessType() {
        return UseBusinessType;
    }

    public void setUseBusinessType(Integer useBusinessType) {
        UseBusinessType = useBusinessType;
    }

    public Integer getCanUseStatus() {
        return CanUseStatus;
    }

    public void setCanUseStatus(Integer canUseStatus) {
        CanUseStatus = canUseStatus;
    }

    public String getCanUseStatusMsg() {
        return CanUseStatusMsg;
    }

    public void setCanUseStatusMsg(String canUseStatusMsg) {
        CanUseStatusMsg = canUseStatusMsg;
    }

    public CardsE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.SalesOrderGUID);
        dest.writeString(this.CardTypeName);
        dest.writeString(this.CardsNumber);
        dest.writeValue(this.RightsCiscount);
        dest.writeValue(this.RightsDiscount);
        dest.writeString(this.EffectTime);
        dest.writeValue(this.IsAvailable);
        dest.writeString(this.InvalidTime);
        dest.writeString(this.MemberInfoGUID);
        dest.writeString(this.CardsChipNo);
        dest.writeValue(this.Balance);
        dest.writeString(this.RegTel);
        dest.writeValue(this.IsEnable);
        dest.writeValue(this.IsDelete);
        dest.writeString(this.CardCreateRecordGUID);
        dest.writeParcelable(this.MemberInfoE, flags);
        dest.writeParcelable(this.CardTypeE, flags);
        dest.writeValue(this.ReturnInRecord);
        dest.writeValue(this.NeedBind);
        dest.writeValue(this.Price);
        dest.writeValue(this.FaceValue);
        dest.writeValue(this.ReturnOutRecord);
        dest.writeString(this.QueryBeginDate);
        dest.writeString(this.QueryEndDate);
        dest.writeString(this.CardTypeGUID);
        dest.writeList(this.ArrayOfViewCardBusinessOrderE);
        dest.writeTypedList(this.ArrayOfCardRechargeLadderE);
        dest.writeValue(this.ReturnMemberInfo);
        dest.writeValue(this.ReturnCardType);
        dest.writeValue(this.BindState);
        dest.writeValue(this.UseBusinessType);
        dest.writeValue(this.CanUseStatus);
        dest.writeString(this.CanUseStatusMsg);
        dest.writeValue(this.PayableAmount);
        dest.writeValue(this.DiscountAmount);
        dest.writeValue(this.ActualyAmount);
        dest.writeValue(this.IsEnough);
    }

    protected CardsE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.SalesOrderGUID = in.readString();
        this.CardTypeName = in.readString();
        this.CardsNumber = in.readString();
        this.RightsCiscount = (Double) in.readValue(Double.class.getClassLoader());
        this.RightsDiscount = (Double) in.readValue(Double.class.getClassLoader());
        this.EffectTime = in.readString();
        this.IsAvailable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.InvalidTime = in.readString();
        this.MemberInfoGUID = in.readString();
        this.CardsChipNo = in.readString();
        this.Balance = (Double) in.readValue(Double.class.getClassLoader());
        this.RegTel = in.readString();
        this.IsEnable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsDelete = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CardCreateRecordGUID = in.readString();
        this.MemberInfoE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE.class.getClassLoader());
        this.CardTypeE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.CardTypeE.class.getClassLoader());
        this.ReturnInRecord = (Integer) in.readValue(Integer.class.getClassLoader());
        this.NeedBind = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        this.FaceValue = (Double) in.readValue(Double.class.getClassLoader());
        this.ReturnOutRecord = (Integer) in.readValue(Integer.class.getClassLoader());
        this.QueryBeginDate = in.readString();
        this.QueryEndDate = in.readString();
        this.CardTypeGUID = in.readString();
        this.ArrayOfViewCardBusinessOrderE = new ArrayList<ViewCardBusinessOrderE>();
        in.readList(this.ArrayOfViewCardBusinessOrderE, ViewCardBusinessOrderE.class.getClassLoader());
        this.ArrayOfCardRechargeLadderE = in.createTypedArrayList(CardRechargeLadderE.CREATOR);
        this.ReturnMemberInfo = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ReturnCardType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.BindState = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UseBusinessType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CanUseStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CanUseStatusMsg = in.readString();
        this.PayableAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.ActualyAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.IsEnough = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<CardsE> CREATOR = new Creator<CardsE>() {
        @Override
        public CardsE createFromParcel(Parcel source) {
            return new CardsE(source);
        }

        @Override
        public CardsE[] newArray(int size) {
            return new CardsE[size];
        }
    };
}
