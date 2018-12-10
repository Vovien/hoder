package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/16.
 */

public class DishesPracticeE implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 做法标识
     */
    private String DishesPracticeGUID;

    private String Name;

    private String Remark;

    private Integer Sort;

    private Integer IsDelete;
    /**价格*/
    private Double Fees;

    private Integer IsEnabled;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getDishesPracticeGUID() {
        return DishesPracticeGUID;
    }

    public void setDishesPracticeGUID(String dishesPracticeGUID) {
        DishesPracticeGUID = dishesPracticeGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public Integer getIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        IsEnabled = isEnabled;
    }

    public Double getFees() {
        return Fees;
    }

    public void setFees(Double fees) {
        Fees = fees;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.DishesPracticeGUID);
        dest.writeString(this.Name);
        dest.writeString(this.Remark);
        dest.writeValue(this.Sort);
        dest.writeValue(this.IsDelete);
        dest.writeValue(this.Fees);
        dest.writeValue(this.IsEnabled);
    }

    public DishesPracticeE() {
    }

    protected DishesPracticeE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.DishesPracticeGUID = in.readString();
        this.Name = in.readString();
        this.Remark = in.readString();
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsDelete = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Fees = (Double) in.readValue(Double.class.getClassLoader());
        this.IsEnabled = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<DishesPracticeE> CREATOR = new Creator<DishesPracticeE>() {
        @Override
        public DishesPracticeE createFromParcel(Parcel source) {
            return new DishesPracticeE(source);
        }

        @Override
        public DishesPracticeE[] newArray(int size) {
            return new DishesPracticeE[size];
        }
    };
}
