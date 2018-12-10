package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by NEW on 2017/4/5.
 */

public class MemberInfoE implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 会员标识
     */
    private String MemberInfoGUID;
    /**
     * 会员注册的手机号
     */
    private String RegTel;
    /**
     * 验证码   用户收到的注册验证码
     */
    private String Code;
    /**
     * 会员昵称
     */
    private String NickName;
    /**
     * 出生日期    格式：yyyy-MM-dd
     */
    private String Brithday;
    /**
     * 性别
     * 0 保密
     * 1 男
     * 2 女
     */
    private Integer Sex;
    /**
     * 原密码
     */
    private String OrgTradingPassWord;
    /**
     * 新密码
     */
    private String NewTradingPassWord;
    /**
     * 如果没有注册  是否注册
     */
    private Integer NeedReg;
    /**
     * 注册发送验证码时返回的校验码
     */
    private String VerCodeUID;
    /**
     * 是否绑定微信
     * 0 没有注册
     * 1 注册了
     */
    private Integer IsWxMember;

    /**
     * 操作员GUID
     */
    private String StaffGUID;
    /**
     * 会员卡列表
     */
    private List<VipCardModel> vipCardModels;
    /**
     * 是否设置了支付密码
     */
    private boolean IsSetPassWord;

    /**
     * 查询类型
     * 1：会员卡
     * 2：手机号
     * 3：二维码或条形码
     * 4：会员UId
     */
    private String type;

    /**
     * 是否返回有效的会员卡
     */
    private boolean allInfo;

    /**
     * 查询内容
     * type = 1 时为会员卡号
     * = 2  手机号
     * =  3  会员码
     * = 4  会员Uid
     */
    private String content;

    /**
     * 门店GUID
     */
    private String StoreGUID;

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAllInfo() {
        return allInfo;
    }

    public void setAllInfo(boolean allInfo) {
        this.allInfo = allInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSetPassWord() {
        return IsSetPassWord;
    }

    public void setSetPassWord(boolean setPassWord) {
        IsSetPassWord = setPassWord;
    }
    /**
     * 会员等级名称
     */
    private String level;
    /**
     * 会员积分
     */
    private Long integral;
    /**
     * 会员权益折扣
     */
    private Float discount;

    public Integer getNeedReg() {
        return NeedReg;
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

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public List<VipCardModel> getVipCardModels() {
        return vipCardModels;
    }

    public void setVipCardModels(List<VipCardModel> vipCardModels) {
        this.vipCardModels = vipCardModels;
    }

    public Integer getIsWxMember() {
        return IsWxMember;
    }

    public void setIsWxMember(Integer isWxMember) {
        IsWxMember = isWxMember;
    }

    public Integer isNeedReg() {
        return NeedReg;
    }

    public void setNeedReg(Integer needReg) {
        NeedReg = needReg;
    }

    public String getVerCodeUID() {
        return VerCodeUID;
    }

    public void setVerCodeUID(String verCodeUID) {
        VerCodeUID = verCodeUID;
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

    public String getRegTel() {
        return RegTel;
    }

    public void setRegTel(String regTel) {
        RegTel = regTel;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getBrithday() {
        return Brithday;
    }

    public void setBrithday(String brithday) {
        Brithday = brithday;
    }

    public Integer getSex() {
        return Sex;
    }

    public String getGender() {
        if (Sex == null || Sex == 0) {
            return "保密";
        } else if (Sex == 1) {
            return "男";
        } else if (Sex == 2) {
            return "女";
        } else {
            return "保密";
        }
    }
    public void setSex(Integer sex) {
        Sex = sex;
    }

    public String getOrgTradingPassWord() {
        return OrgTradingPassWord;
    }

    public void setOrgTradingPassWord(String orgTradingPassWord) {
        OrgTradingPassWord = orgTradingPassWord;
    }

    public String getNewTradingPassWord() {
        return NewTradingPassWord;
    }

    public void setNewTradingPassWord(String newTradingPassWord) {
        NewTradingPassWord = newTradingPassWord;
    }

    public String getStaffGUID() {
        return StaffGUID;
    }

    public void setStaffGUID(String staffGUID) {
        StaffGUID = staffGUID;
    }

    public MemberInfoE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.MemberInfoGUID);
        dest.writeString(this.RegTel);
        dest.writeString(this.Code);
        dest.writeString(this.NickName);
        dest.writeString(this.Brithday);
        dest.writeValue(this.Sex);
        dest.writeString(this.OrgTradingPassWord);
        dest.writeString(this.NewTradingPassWord);
        dest.writeValue(this.NeedReg);
        dest.writeString(this.VerCodeUID);
        dest.writeValue(this.IsWxMember);
        dest.writeString(this.StaffGUID);
        dest.writeTypedList(this.vipCardModels);
        dest.writeByte(this.IsSetPassWord ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeByte(this.allInfo ? (byte) 1 : (byte) 0);
        dest.writeString(this.content);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.level);
        dest.writeValue(this.integral);
        dest.writeValue(this.discount);
    }

    protected MemberInfoE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.MemberInfoGUID = in.readString();
        this.RegTel = in.readString();
        this.Code = in.readString();
        this.NickName = in.readString();
        this.Brithday = in.readString();
        this.Sex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.OrgTradingPassWord = in.readString();
        this.NewTradingPassWord = in.readString();
        this.NeedReg = (Integer) in.readValue(Integer.class.getClassLoader());
        this.VerCodeUID = in.readString();
        this.IsWxMember = (Integer) in.readValue(Integer.class.getClassLoader());
        this.StaffGUID = in.readString();
        this.vipCardModels = in.createTypedArrayList(VipCardModel.CREATOR);
        this.IsSetPassWord = in.readByte() != 0;
        this.type = in.readString();
        this.allInfo = in.readByte() != 0;
        this.content = in.readString();
        this.StoreGUID = in.readString();
        this.level = in.readString();
        this.integral = (Long) in.readValue(Long.class.getClassLoader());
        this.discount = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<MemberInfoE> CREATOR = new Creator<MemberInfoE>() {
        @Override
        public MemberInfoE createFromParcel(Parcel source) {
            return new MemberInfoE(source);
        }

        @Override
        public MemberInfoE[] newArray(int size) {
            return new MemberInfoE[size];
        }
    };
}
