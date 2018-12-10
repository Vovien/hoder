package com.holderzone.intelligencepos.mvp.model.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * 企业注册信息表
 * Created by Administrator on 2016-10-31.
 */
@Entity(nameInDb = "EnterpriseInfo")
public class EnterpriseInfo {
    /**
     * GUID,主键
     */
    @Property(nameInDb = "EnterpriseInfoGUID")
    private String EnterpriseInfoGUID;

    /**
     * 商户号
     */
    @Property(nameInDb = "EnterpriseInfoUID")
    private String EnterpriseInfoUID;

    /**
     * 注册电话/登录ID
     */
    @Property(nameInDb = "RegTel")
    private String RegTel;

    /**
     * 企业名称
     */
    @Property(nameInDb = "Name")
    private String Name;

    /**
     * EMALL
     */
    @Property(nameInDb = "RegEmail")
    private String RegEmail;

    /**
     * 注册设备
     */
    @Property(nameInDb = "RegDevice")
    private String RegDevice;

    /**
     * 注册设备唯一硬件ID
     */
    @Property(nameInDb = "RegDeviceID")
    private String RegDeviceID;

    /**
     * 企业地址
     */
    @Property(nameInDb = "EnterpriseAddress")
    private String EnterpriseAddress;

    /**
     * 企业法人
     */
    @Property(nameInDb = "CorporationPerson")
    private String CorporationPerson;

    /**
     * 联系人
     */
    @Property(nameInDb = "PersonName")
    private String PersonName;

    /**
     * 联系电话
     */
    @Property(nameInDb = "PersonTel")
    private String PersonTel;

    /**
     * 日结算开始时间
     */
    @Property(nameInDb = "SettlementTimeStart")
    private String SettlementTimeStart;

    /**
     * 日结算终止时间
     */
    @Property(nameInDb = "SettlementTimeEnd")
    private String SettlementTimeEnd;

    /**
     * 密码
     */
    @Property(nameInDb = "LoginPassWord")
    private String LoginPassWord;

    /**
     * 品牌名称
     */
    @Property(nameInDb = "BrandName")
    private String BrandName;

    @Keep
    @Generated(hash = 227601969)
    public EnterpriseInfo() {
    }

    @Keep
    @Generated(hash = 348813466)
    public EnterpriseInfo(String EnterpriseInfoGUID, String EnterpriseInfoUID, String RegTel, String Name, String RegEmail, String RegDevice, String RegDeviceID, String EnterpriseAddress, String CorporationPerson, String PersonName, String PersonTel, String SettlementTimeStart, String SettlementTimeEnd,
                          String LoginPassWord, String BrandName) {
        this.EnterpriseInfoGUID = EnterpriseInfoGUID;
        this.EnterpriseInfoUID = EnterpriseInfoUID;
        this.RegTel = RegTel;
        this.Name = Name;
        this.RegEmail = RegEmail;
        this.RegDevice = RegDevice;
        this.RegDeviceID = RegDeviceID;
        this.EnterpriseAddress = EnterpriseAddress;
        this.CorporationPerson = CorporationPerson;
        this.PersonName = PersonName;
        this.PersonTel = PersonTel;
        this.SettlementTimeStart = SettlementTimeStart;
        this.SettlementTimeEnd = SettlementTimeEnd;
        this.LoginPassWord = LoginPassWord;
        this.BrandName = BrandName;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getEnterpriseInfoUID() {
        return EnterpriseInfoUID;
    }

    public void setEnterpriseInfoUID(String enterpriseInfoUID) {
        EnterpriseInfoUID = enterpriseInfoUID;
    }

    public String getRegTel() {
        return RegTel;
    }

    public void setRegTel(String regTel) {
        RegTel = regTel;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRegEmail() {
        return RegEmail;
    }

    public void setRegEmail(String regEmail) {
        RegEmail = regEmail;
    }

    public String getRegDevice() {
        return RegDevice;
    }

    public void setRegDevice(String regDevice) {
        RegDevice = regDevice;
    }

    public String getRegDeviceID() {
        return RegDeviceID;
    }

    public void setRegDeviceID(String regDeviceID) {
        RegDeviceID = regDeviceID;
    }

    public String getEnterpriseAddress() {
        return EnterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        EnterpriseAddress = enterpriseAddress;
    }

    public String getCorporationPerson() {
        return CorporationPerson;
    }

    public void setCorporationPerson(String corporationPerson) {
        CorporationPerson = corporationPerson;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getPersonTel() {
        return PersonTel;
    }

    public void setPersonTel(String personTel) {
        PersonTel = personTel;
    }

    public String getSettlementTimeStart() {
        return SettlementTimeStart;
    }

    public void setSettlementTimeStart(String settlementTimeStart) {
        SettlementTimeStart = settlementTimeStart;
    }

    public String getSettlementTimeEnd() {
        return SettlementTimeEnd;
    }

    public void setSettlementTimeEnd(String settlementTimeEnd) {
        SettlementTimeEnd = settlementTimeEnd;
    }

    public String getLoginPassWord() {
        return LoginPassWord;
    }

    public void setLoginPassWord(String loginPassWord) {
        LoginPassWord = loginPassWord;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }
}
