package com.holderzone.intelligencepos.mvp.model.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tcw on 2017/3/16.
 */

@Entity(nameInDb = "AccountRecord")
public class AccountRecord implements Parcelable {
    /**
     * 门店标识
     */
    @Property(nameInDb = "StoreGUID")
    private String StoreGUID;
    /**
     * 扎帐记录标识
     */
    @Property(nameInDb = "AccountRecordGUID")
    private String AccountRecordGUID;
    /**
     * 开始日期
     */
    @Transient
    private String BeginDate;
    /**
     * 结束日期
     */
    @Transient
    private String EndDate;

    /**
     * 营业日
     */
    @Property(nameInDb = "BusinessDay")
    private String BusinessDay;

    /**
     * 操作员标识
     */
    @Transient
    private String CreateStaffGUID;

    /**
     * 扎帐操作员标识
     */
    @Transient
    private String CheckStaffGUID;

    /**
     * 扎帐状态
     * 0=未扎帐   1=已扎帐
     */
    @Property(nameInDb = "Stat")
    private Integer Stat;
    /**
     * 终端设备ID
     */
    @Transient
    private String DeviceID;
    /**
     * 打印人标识
     * 客户端传入当前登录人员即可
     */
    @Transient
    private String PrintUsersGUID;

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getAccountRecordGUID() {
        return AccountRecordGUID;
    }

    public void setAccountRecordGUID(String accountRecordGUID) {
        AccountRecordGUID = accountRecordGUID;
    }

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public String getCreateStaffGUID() {
        return CreateStaffGUID;
    }

    public void setCreateStaffGUID(String createStaffGUID) {
        CreateStaffGUID = createStaffGUID;
    }

    public String getCheckStaffGUID() {
        return CheckStaffGUID;
    }

    public void setCheckStaffGUID(String checkStaffGUID) {
        CheckStaffGUID = checkStaffGUID;
    }

    public Integer getStat() {
        return Stat;
    }

    public void setStat(Integer stat) {
        Stat = stat;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getPrintUsersGUID() {
        return PrintUsersGUID;
    }

    public void setPrintUsersGUID(String printUsersGUID) {
        PrintUsersGUID = printUsersGUID;
    }

    public AccountRecord() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.StoreGUID);
        dest.writeString(this.AccountRecordGUID);
        dest.writeString(this.BeginDate);
        dest.writeString(this.EndDate);
        dest.writeString(this.BusinessDay);
        dest.writeString(this.CreateStaffGUID);
        dest.writeString(this.CheckStaffGUID);
        dest.writeValue(this.Stat);
        dest.writeString(this.DeviceID);
        dest.writeString(this.PrintUsersGUID);
    }

    protected AccountRecord(Parcel in) {
        this.StoreGUID = in.readString();
        this.AccountRecordGUID = in.readString();
        this.BeginDate = in.readString();
        this.EndDate = in.readString();
        this.BusinessDay = in.readString();
        this.CreateStaffGUID = in.readString();
        this.CheckStaffGUID = in.readString();
        this.Stat = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DeviceID = in.readString();
        this.PrintUsersGUID = in.readString();
    }

    @Generated(hash = 2108033486)
    public AccountRecord(String StoreGUID, String AccountRecordGUID, String BusinessDay,
            Integer Stat) {
        this.StoreGUID = StoreGUID;
        this.AccountRecordGUID = AccountRecordGUID;
        this.BusinessDay = BusinessDay;
        this.Stat = Stat;
    }

    public static final Creator<AccountRecord> CREATOR = new Creator<AccountRecord>() {
        @Override
        public AccountRecord createFromParcel(Parcel source) {
            return new AccountRecord(source);
        }

        @Override
        public AccountRecord[] newArray(int size) {
            return new AccountRecord[size];
        }
    };
}
