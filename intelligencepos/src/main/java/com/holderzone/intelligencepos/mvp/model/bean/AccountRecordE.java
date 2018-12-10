package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;

/**
 * Created by tcw on 2017/5/26.
 */

public class AccountRecordE extends AccountRecord implements Parcelable {
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

    public AccountRecordE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
    }

    protected AccountRecordE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
    }

    public static final Creator<AccountRecordE> CREATOR = new Creator<AccountRecordE>() {
        @Override
        public AccountRecordE createFromParcel(Parcel source) {
            return new AccountRecordE(source);
        }

        @Override
        public AccountRecordE[] newArray(int size) {
            return new AccountRecordE[size];
        }
    };
}
