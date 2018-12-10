package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 何师会员信息实体
 * Created by chencao on 2018/3/8.
 */

public class MemberModel implements Parcelable {
    private String EnterpriseInfoGUID;
    /**
     * 会员Uid
     */
    private String memberUid;
    /**
     * 会员名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 账户余额
     */
    private String balance;
    /**
     * 商家 uid
     */
    private String orgUid;
    /**
     * 商家名称
     */
    private String orgName;
    /**
     * 会员等级名称
     */
    private String level;
    /**
     * 会员积分
     */
    private Long integral;
    /**
     * 所属品牌的 uid
     */
    private String brandUid;
    /**
     * 是否可以享受账户优惠
     */
    private Boolean shareEquity;
    /**
     * 会员所享受的折扣
     */
    private Float discount;
    /**
     * 支付密码状态  true开启   false未开启
     */
    private Boolean payPwdStatus;
    /**
     * 商家编码
     */
    private String orgCode;
    /**
     * 是否选中    true选中，false未选择
     */
    private Boolean selected;
    /**
     * 会员卡列表信息
     */
    private List<VipCardModel> vipCardModels;
    /**
     * 优惠券列表
     */
    private List<CashCouponModel> cashCouponModels;
    /**
     * 红包列表
     */
    private List<RedPackageModel> redPackageModels;

    public List<RedPackageModel> getRedPackageModels() {
        return redPackageModels;
    }

    public void setRedPackageModels(List<RedPackageModel> redPackageModels) {
        this.redPackageModels = redPackageModels;
    }

    public List<CashCouponModel> getCashCouponModels() {
        return cashCouponModels;
    }

    public void setCashCouponModels(List<CashCouponModel> cashCouponModels) {
        this.cashCouponModels = cashCouponModels;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(String memberUid) {
        this.memberUid = memberUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOrgUid() {
        return orgUid;
    }

    public void setOrgUid(String orgUid) {
        this.orgUid = orgUid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public String getBrandUid() {
        return brandUid;
    }

    public void setBrandUid(String brandUid) {
        this.brandUid = brandUid;
    }

    public Boolean getShareEquity() {
        return shareEquity;
    }

    public void setShareEquity(Boolean shareEquity) {
        this.shareEquity = shareEquity;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Boolean getPayPwdStatus() {
        return payPwdStatus;
    }

    public void setPayPwdStatus(Boolean payPwdStatus) {
        this.payPwdStatus = payPwdStatus;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public List<VipCardModel> getVipCardModels() {
        return vipCardModels;
    }

    public void setVipCardModels(List<VipCardModel> vipCardModels) {
        this.vipCardModels = vipCardModels;
    }

    public MemberModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.memberUid);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.balance);
        dest.writeString(this.orgUid);
        dest.writeString(this.orgName);
        dest.writeString(this.level);
        dest.writeValue(this.integral);
        dest.writeString(this.brandUid);
        dest.writeValue(this.shareEquity);
        dest.writeValue(this.discount);
        dest.writeValue(this.payPwdStatus);
        dest.writeString(this.orgCode);
        dest.writeValue(this.selected);
        dest.writeTypedList(this.vipCardModels);
        dest.writeTypedList(this.cashCouponModels);
        dest.writeTypedList(this.redPackageModels);
    }

    protected MemberModel(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.memberUid = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.balance = in.readString();
        this.orgUid = in.readString();
        this.orgName = in.readString();
        this.level = in.readString();
        this.integral = (Long) in.readValue(Long.class.getClassLoader());
        this.brandUid = in.readString();
        this.shareEquity = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.discount = (Float) in.readValue(Float.class.getClassLoader());
        this.payPwdStatus = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.orgCode = in.readString();
        this.selected = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.vipCardModels = in.createTypedArrayList(VipCardModel.CREATOR);
        this.cashCouponModels = in.createTypedArrayList(CashCouponModel.CREATOR);
        this.redPackageModels = in.createTypedArrayList(RedPackageModel.CREATOR);
    }

    public static final Creator<MemberModel> CREATOR = new Creator<MemberModel>() {
        @Override
        public MemberModel createFromParcel(Parcel source) {
            return new MemberModel(source);
        }

        @Override
        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };
}
