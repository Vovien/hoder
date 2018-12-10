package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/16.
 */

public class DishesReturnReasonE implements Parcelable{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 商家UID
     */
    private String EnterpriseInfoUID;
    /**
     * 商家ID
     */
    private String EnterpriseInfoID;

    /**
     * 退菜原因标识
     */
    private String DishesReturnReasonGUID;

    /**
     * 退菜原因
     */
    private String Name;
    /**
     *
     */
    private Integer Sort;
    /**
     *
     */
    private Integer IsDelete;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getEnterpriseInfoUID() {
        return EnterpriseInfoUID;
    }

    public void setEnterpriseInfoUID(String enterpriseInfoUID) {
        EnterpriseInfoUID = enterpriseInfoUID;
    }

    public String getEnterpriseInfoID() {
        return EnterpriseInfoID;
    }

    public void setEnterpriseInfoID(String enterpriseInfoID) {
        EnterpriseInfoID = enterpriseInfoID;
    }

    public String getDishesReturnReasonGUID() {
        return DishesReturnReasonGUID;
    }

    public void setDishesReturnReasonGUID(String dishesReturnReasonGUID) {
        DishesReturnReasonGUID = dishesReturnReasonGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.EnterpriseInfoUID);
        dest.writeString(this.EnterpriseInfoID);
        dest.writeString(this.DishesReturnReasonGUID);
        dest.writeString(this.Name);
        dest.writeValue(this.Sort);
        dest.writeValue(this.IsDelete);
    }

    public DishesReturnReasonE() {
    }

    protected DishesReturnReasonE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.EnterpriseInfoUID = in.readString();
        this.EnterpriseInfoID = in.readString();
        this.DishesReturnReasonGUID = in.readString();
        this.Name = in.readString();
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsDelete = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<DishesReturnReasonE> CREATOR = new Creator<DishesReturnReasonE>() {
        @Override
        public DishesReturnReasonE createFromParcel(Parcel source) {
            return new DishesReturnReasonE(source);
        }

        @Override
        public DishesReturnReasonE[] newArray(int size) {
            return new DishesReturnReasonE[size];
        }
    };
}
