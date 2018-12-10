package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/15.
 */

public class PackageItemE extends PackageItem implements Parcelable {
    private DishesE DishesE;

    public DishesE getDishesE() {
        return DishesE;
    }

    public void setDishesE(DishesE dishesE) {
        DishesE = dishesE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.DishesE, flags);
    }

    public PackageItemE() {
    }

    protected PackageItemE(Parcel in) {
        this.DishesE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DishesE.class.getClassLoader());
    }

    public static final Parcelable.Creator<PackageItemE> CREATOR = new Parcelable.Creator<PackageItemE>() {
        @Override
        public PackageItemE createFromParcel(Parcel source) {
            return new PackageItemE(source);
        }

        @Override
        public PackageItemE[] newArray(int size) {
            return new PackageItemE[size];
        }
    };
}
