package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/20.
 */

public class DishesPracticeBindE implements Parcelable {
    private DishesPracticeE DishesPracticeE;
    private Double Fees;
    private String DishesGUID;

    public DishesPracticeE getDishesPracticeE() {
        return DishesPracticeE;
    }

    public void setDishesPracticeE(DishesPracticeE dishesPracticeE) {
        DishesPracticeE = dishesPracticeE;
    }

    public Double getFees() {
        return Fees;
    }

    public void setFees(Double fees) {
        Fees = fees;
    }

    public String getDishesGUID() {
        return DishesGUID;
    }

    public void setDishesGUID(String dishesGUID) {
        DishesGUID = dishesGUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.DishesPracticeE, flags);
        dest.writeValue(this.Fees);
        dest.writeString(this.DishesGUID);
    }

    public DishesPracticeBindE() {
    }

    protected DishesPracticeBindE(Parcel in) {
        this.DishesPracticeE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE.class.getClassLoader());
        this.Fees = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesGUID = in.readString();
    }

    public static final Parcelable.Creator<DishesPracticeBindE> CREATOR = new Parcelable.Creator<DishesPracticeBindE>() {
        @Override
        public DishesPracticeBindE createFromParcel(Parcel source) {
            return new DishesPracticeBindE(source);
        }

        @Override
        public DishesPracticeBindE[] newArray(int size) {
            return new DishesPracticeBindE[size];
        }
    };
}
