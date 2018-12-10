package com.holderzone.intelligencepos.mvp.model.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Administrator on 2016-10-31.
 */
@Entity(nameInDb = "Store")
public class Store implements Parcelable {
    /**
     * 门店标识
     */
    @Property(nameInDb = "StoreGUID")
    private String StoreGUID;

    /**
     * 门店名称
     */
    @Property(nameInDb = "Name")
    private String Name;

    ///
    @Property(nameInDb = "OpenPlatStoreID")
    private String OpenPlatStoreID;

    /**
     * 门店营业时间
     */
    @Property(nameInDb = "BusinessHoursStart")
    private String BusinessHoursStart;

    /**
     * 门店打烊时间
     */
    @Property(nameInDb = "BusinessHoursEnd")
    private String BusinessHoursEnd;

    /**
     * 是否是当前门店
     */
    @Property(nameInDb = "IsCurrentStore")
    private Integer IsCurrentStore;

    @Property(nameInDb = "Code")
    private String Code;

    @Keep
    @Generated(hash = 517251631)
    public Store(String StoreGUID, String Name, String OpenPlatStoreID,
            String BusinessHoursStart, String BusinessHoursEnd,
            Integer IsCurrentStore, String Code) {
        this.StoreGUID = StoreGUID;
        this.Name = Name;
        this.OpenPlatStoreID = OpenPlatStoreID;
        this.BusinessHoursStart = BusinessHoursStart;
        this.BusinessHoursEnd = BusinessHoursEnd;
        this.IsCurrentStore = IsCurrentStore;
        this.Code = Code;
    }

    @Keep
    @Generated(hash = 770513066)
    public Store() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOpenPlatStoreID() {
        return OpenPlatStoreID;
    }

    public void setOpenPlatStoreID(String openPlatStoreID) {
        OpenPlatStoreID = openPlatStoreID;
    }

    public String getBusinessHoursStart() {
        return BusinessHoursStart;
    }

    public void setBusinessHoursStart(String businessHoursStart) {
        BusinessHoursStart = businessHoursStart;
    }

    public String getBusinessHoursEnd() {
        return BusinessHoursEnd;
    }

    public void setBusinessHoursEnd(String businessHoursEnd) {
        BusinessHoursEnd = businessHoursEnd;
    }

    public Integer getIsCurrentStore() {
        return IsCurrentStore;
    }

    public void setIsCurrentStore(Integer isCurrentStore) {
        IsCurrentStore = isCurrentStore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.StoreGUID);
        dest.writeString(this.Name);
        dest.writeString(this.OpenPlatStoreID);
        dest.writeString(this.BusinessHoursStart);
        dest.writeString(this.BusinessHoursEnd);
        dest.writeValue(this.IsCurrentStore);
        dest.writeString(this.Code);
    }

    protected Store(Parcel in) {
        this.StoreGUID = in.readString();
        this.Name = in.readString();
        this.OpenPlatStoreID = in.readString();
        this.BusinessHoursStart = in.readString();
        this.BusinessHoursEnd = in.readString();
        this.IsCurrentStore = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Code = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
