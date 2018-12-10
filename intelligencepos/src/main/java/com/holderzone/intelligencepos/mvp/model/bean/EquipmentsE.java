package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/4/14.
 */

public class EquipmentsE implements Parcelable{
    /**
     * 一体机设备ID
     */
    private String EquipmentsUID;
    /**
     * 商家标志
     * */
    private String EnterpriseInfoGUID;
    /**
     * 授权期限
     * */
    private String ExpiryDate;

    public EquipmentsE() {
    }

    public EquipmentsE(String equipmentsUID) {
        EquipmentsUID = equipmentsUID;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getEquipmentsUID() {
        return EquipmentsUID;
    }

    public void setEquipmentsUID(String equipmentsUID) {
        EquipmentsUID = equipmentsUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EquipmentsUID);
    }

    protected EquipmentsE(Parcel in) {
        this.EquipmentsUID = in.readString();
    }

    public static final Creator<EquipmentsE> CREATOR = new Creator<EquipmentsE>() {
        @Override
        public EquipmentsE createFromParcel(Parcel source) {
            return new EquipmentsE(source);
        }

        @Override
        public EquipmentsE[] newArray(int size) {
            return new EquipmentsE[size];
        }
    };
}
