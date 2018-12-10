package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-03-08.
 */

public class OrderCountE implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;
    /**
     * 堂食订单数量
     */
    private int TSOrderCount;
    /**
     * 快销
     */
    private int SnackOrderCount;
    /**
     * 外卖
     */
    private int WmOrderCount;
    /**
     * 排队数量
     */
    private int QueueUpCount;

    public int getQueueUpCount() {
        return QueueUpCount;
    }

    public void setQueueUpCount(int queueUpCount) {
        QueueUpCount = queueUpCount;
    }

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

    public int getTSOrderCount() {
        return TSOrderCount;
    }

    public void setTSOrderCount(int TSOrderCount) {
        this.TSOrderCount = TSOrderCount;
    }

    public int getSnackOrderCount() {
        return SnackOrderCount;
    }

    public void setSnackOrderCount(int snackOrderCount) {
        SnackOrderCount = snackOrderCount;
    }

    public int getWmOrderCount() {
        return WmOrderCount;
    }

    public void setWmOrderCount(int wmOrderCount) {
        WmOrderCount = wmOrderCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeInt(this.TSOrderCount);
        dest.writeInt(this.SnackOrderCount);
        dest.writeInt(this.WmOrderCount);
    }

    public OrderCountE() {
    }

    protected OrderCountE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.TSOrderCount = in.readInt();
        this.SnackOrderCount = in.readInt();
        this.WmOrderCount = in.readInt();
    }

    public static final Creator<OrderCountE> CREATOR = new Creator<OrderCountE>() {
        @Override
        public OrderCountE createFromParcel(Parcel source) {
            return new OrderCountE(source);
        }

        @Override
        public OrderCountE[] newArray(int size) {
            return new OrderCountE[size];
        }
    };
}
