package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaoping on 2016/4/19.
 * 菜品特殊要求
 */
public class DishesRemark implements Parcelable{

    public DishesRemark() {
    }

    /**
     * 数据标识
     */
    private String DishesRemarkGUID;
    /**
     * 名称
     */
    private String Name;
    /**
     * 状态 1=启用 0=停用
     */
    private Integer IsEnabled = 1;

    /**
     * 排序
     */
    private Integer Sort = 0;
    /**
     * 选重状态
     */
    private Integer Selected = 0;

    public String getDishesRemarkGUID() {
        return DishesRemarkGUID;
    }

    public void setDishesRemarkGUID(String dishesRemarkGUID) {
        DishesRemarkGUID = dishesRemarkGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        IsEnabled = isEnabled;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public Integer getSelected() {
        return Selected;
    }

    public void setSelected(Integer selected) {
        Selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DishesRemarkGUID);
        dest.writeString(this.Name);
        dest.writeValue(this.IsEnabled);
        dest.writeValue(this.Sort);
        dest.writeValue(this.Selected);
    }

    protected DishesRemark(Parcel in) {
        this.DishesRemarkGUID = in.readString();
        this.Name = in.readString();
        this.IsEnabled = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Selected = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<DishesRemark> CREATOR = new Creator<DishesRemark>() {
        @Override
        public DishesRemark createFromParcel(Parcel source) {
            return new DishesRemark(source);
        }

        @Override
        public DishesRemark[] newArray(int size) {
            return new DishesRemark[size];
        }
    };
}
