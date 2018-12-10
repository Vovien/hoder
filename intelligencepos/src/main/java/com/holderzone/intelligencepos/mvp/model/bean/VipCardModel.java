package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 何师会员卡实体
 * Created by chencao on 2018/3/8.
 */

public class VipCardModel implements Parcelable {
    /**
     * 会员卡卡号
     */
    private String cardNum;
    /**
     * 会员卡余额
     */
    private Double balance;
    /**
     * 商家名称
     */
    private String orgName;
    /**
     * 商家 uid
     */
    private String orgUid;
    /**
     * 会员卡折扣
     */
    private Float discount;
    /**
     * 会员卡名称
     */
    private String cardName;
    /**
     * 是否选中 true选中，false未选择
     */
    private Boolean selected;
    /**
     * 会员卡状态   1 有效 2 过期
     */
    private String status;
    /**
     * 会员卡积分
     */
    private Long integral;
    /**
     * 会员卡类型
     * 000 则是任何策略未使用
     * 001 则是积分策略被使用
     * 010 则是充值返现策略被使用
     * 100 则是折扣策略被使用
     * 011 则是充值返现、积分被使用
     * 101 则是折扣与积分被使用
     * 110 则是折扣与充值返现被使用
     * 111 则是折扣、充值返现、积分被使用
     */
    private String cardType;
    /**
     * 是否是账户余额
     */
    private boolean isMember;

    private String startTime;
    private String endTime;
    /**
     *  是否优惠共享
     *  true 可以优惠共享
     */
    private Boolean isShare;
    /**
     * true 永久有效
     * false 指定范围内有效
     */
    private Boolean isPerpetual;

    public Boolean getPerpetual() {
        return isPerpetual;
    }

    public void setPerpetual(Boolean perpetual) {
        isPerpetual = perpetual;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    private boolean isMemberAccount = false;

    public boolean isMemberAccount() {
        return isMemberAccount;
    }

    public void setMemberAccount(boolean memberAccount) {
        isMemberAccount = memberAccount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgUid() {
        return orgUid;
    }

    public void setOrgUid(String orgUid) {
        this.orgUid = orgUid;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


    public VipCardModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardNum);
        dest.writeValue(this.balance);
        dest.writeString(this.orgName);
        dest.writeString(this.orgUid);
        dest.writeValue(this.discount);
        dest.writeString(this.cardName);
        dest.writeValue(this.selected);
        dest.writeString(this.status);
        dest.writeValue(this.integral);
        dest.writeString(this.cardType);
        dest.writeByte(this.isMember ? (byte) 1 : (byte) 0);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeValue(this.isShare);
        dest.writeValue(this.isPerpetual);
        dest.writeByte(this.isMemberAccount ? (byte) 1 : (byte) 0);
    }

    protected VipCardModel(Parcel in) {
        this.cardNum = in.readString();
        this.balance = (Double) in.readValue(Double.class.getClassLoader());
        this.orgName = in.readString();
        this.orgUid = in.readString();
        this.discount = (Float) in.readValue(Float.class.getClassLoader());
        this.cardName = in.readString();
        this.selected = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.status = in.readString();
        this.integral = (Long) in.readValue(Long.class.getClassLoader());
        this.cardType = in.readString();
        this.isMember = in.readByte() != 0;
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.isShare = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isPerpetual = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isMemberAccount = in.readByte() != 0;
    }

    public static final Creator<VipCardModel> CREATOR = new Creator<VipCardModel>() {
        @Override
        public VipCardModel createFromParcel(Parcel source) {
            return new VipCardModel(source);
        }

        @Override
        public VipCardModel[] newArray(int size) {
            return new VipCardModel[size];
        }
    };
}
