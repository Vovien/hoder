package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-4-20.
 */

public class CardRechargeLadderE implements Parcelable {

    /**
     * 数据标志
     */
    private String CardRechargeLadderGUID;
    /**
     * 充值金额
     */
    private double RechargeAmount;
    /**
     * 赠送金额
     */
    private double GiveAmount;

    public String getCardRechargeLadderGUID() {
        return CardRechargeLadderGUID;
    }

    public void setCardRechargeLadderGUID(String cardRechargeLadderGUID) {
        CardRechargeLadderGUID = cardRechargeLadderGUID;
    }

    public double getRechargeAmount() {
        return RechargeAmount;
    }

    public void setRechargeAmount(double rechargeAmount) {
        RechargeAmount = rechargeAmount;
    }

    public double getGiveAmount() {
        return GiveAmount;
    }

    public void setGiveAmount(double giveAmount) {
        GiveAmount = giveAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CardRechargeLadderGUID);
        dest.writeDouble(this.RechargeAmount);
        dest.writeDouble(this.GiveAmount);
    }

    public CardRechargeLadderE() {
    }

    protected CardRechargeLadderE(Parcel in) {
        this.CardRechargeLadderGUID = in.readString();
        this.RechargeAmount = in.readDouble();
        this.GiveAmount = in.readDouble();
    }

    public static final Creator<CardRechargeLadderE> CREATOR = new Creator<CardRechargeLadderE>() {
        @Override
        public CardRechargeLadderE createFromParcel(Parcel source) {
            return new CardRechargeLadderE(source);
        }

        @Override
        public CardRechargeLadderE[] newArray(int size) {
            return new CardRechargeLadderE[size];
        }
    };
}
