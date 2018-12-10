package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;

/**
 * Created by tcw on 2017/5/26.
 */

public class AdditionalFeesE extends AdditionalFees implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
    }

    public AdditionalFeesE() {
    }

    protected AdditionalFeesE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
    }

    public static final Parcelable.Creator<AdditionalFeesE> CREATOR = new Parcelable.Creator<AdditionalFeesE>() {
        @Override
        public AdditionalFeesE createFromParcel(Parcel source) {
            return new AdditionalFeesE(source);
        }

        @Override
        public AdditionalFeesE[] newArray(int size) {
            return new AdditionalFeesE[size];
        }
    };
}
