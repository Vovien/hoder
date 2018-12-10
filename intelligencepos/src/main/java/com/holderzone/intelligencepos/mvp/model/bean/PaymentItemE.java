package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;

/**
 * Created by tcw on 2017/3/10.
 */

public class PaymentItemE extends PaymentItem implements Parcelable{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
    }

    public PaymentItemE() {
    }

    protected PaymentItemE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
    }

    public static final Creator<PaymentItemE> CREATOR = new Creator<PaymentItemE>() {
        @Override
        public PaymentItemE createFromParcel(Parcel source) {
            return new PaymentItemE(source);
        }

        @Override
        public PaymentItemE[] newArray(int size) {
            return new PaymentItemE[size];
        }
    };
}
