package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017-4-20.
 */

public class CardTypeE extends CardType implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 赠送规则
     */
    private List<CardRechargeLadderE> ArrayOfCardRechargeLadderE;

    public List<CardRechargeLadderE> getArrayOfCardRechargeLadderE() {
        return ArrayOfCardRechargeLadderE;
    }

    public void setArrayOfCardRechargeLadderE(List<CardRechargeLadderE> arrayOfCardRechargeLadderE) {
        ArrayOfCardRechargeLadderE = arrayOfCardRechargeLadderE;
    }

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
        dest.writeTypedList(this.ArrayOfCardRechargeLadderE);
    }

    public CardTypeE() {
    }

    protected CardTypeE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.ArrayOfCardRechargeLadderE = in.createTypedArrayList(CardRechargeLadderE.CREATOR);
    }

    public static final Creator<CardTypeE> CREATOR = new Creator<CardTypeE>() {
        @Override
        public CardTypeE createFromParcel(Parcel source) {
            return new CardTypeE(source);
        }

        @Override
        public CardTypeE[] newArray(int size) {
            return new CardTypeE[size];
        }
    };
}
