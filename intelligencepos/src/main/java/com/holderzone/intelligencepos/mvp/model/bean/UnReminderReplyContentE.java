package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;

/**
 * Created by LT on 2018-04-02.
 */

public class UnReminderReplyContentE extends UnReminderReplyContent {
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
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
    }

    public UnReminderReplyContentE() {
    }

    protected UnReminderReplyContentE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
    }

    public static final Creator<UnReminderReplyContentE> CREATOR = new Creator<UnReminderReplyContentE>() {
        @Override
        public UnReminderReplyContentE createFromParcel(Parcel source) {
            return new UnReminderReplyContentE(source);
        }

        @Override
        public UnReminderReplyContentE[] newArray(int size) {
            return new UnReminderReplyContentE[size];
        }
    };
}
