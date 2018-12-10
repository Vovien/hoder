package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/16.
 */

public class DishesRemarkE extends DishesRemark implements Parcelable{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 判断是添加还是确认备注
     */
    private Integer Type;

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
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

    public DishesRemarkE() {
    }

    protected DishesRemarkE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
    }

    public static final Creator<DishesRemarkE> CREATOR = new Creator<DishesRemarkE>() {
        @Override
        public DishesRemarkE createFromParcel(Parcel source) {
            return new DishesRemarkE(source);
        }

        @Override
        public DishesRemarkE[] newArray(int size) {
            return new DishesRemarkE[size];
        }
    };
}
