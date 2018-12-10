package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/30.
 */

public class DiningTableEWrapper implements Parcelable {

    private DiningTableE mDiningTableE;
    private String mTableAreaName;

    public DiningTableEWrapper(DiningTableE diningTableE, String tableAreaName) {
        mDiningTableE = diningTableE;
        mTableAreaName = tableAreaName;
    }

    public DiningTableE getDiningTableE() {
        return mDiningTableE;
    }

    public void setDiningTableE(DiningTableE diningTableE) {
        mDiningTableE = diningTableE;
    }

    public String getTableAreaName() {
        return mTableAreaName;
    }

    public void setTableAreaName(String tableAreaName) {
        mTableAreaName = tableAreaName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDiningTableE, flags);
        dest.writeString(this.mTableAreaName);
    }

    protected DiningTableEWrapper(Parcel in) {
        this.mDiningTableE = in.readParcelable(DiningTableE.class.getClassLoader());
        this.mTableAreaName = in.readString();
    }

    public static final Creator<DiningTableEWrapper> CREATOR = new Creator<DiningTableEWrapper>() {
        @Override
        public DiningTableEWrapper createFromParcel(Parcel source) {
            return new DiningTableEWrapper(source);
        }

        @Override
        public DiningTableEWrapper[] newArray(int size) {
            return new DiningTableEWrapper[size];
        }
    };

}
