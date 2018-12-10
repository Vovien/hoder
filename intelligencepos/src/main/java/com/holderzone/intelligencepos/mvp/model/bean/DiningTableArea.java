package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 门店区域表
 * Created by Administrator on 2016-8-22.
 */
public class DiningTableArea implements Parcelable {

    /**
     * 门店区域
     */
    public DiningTableArea() {
    }

    /**
     * 数据标识
     */
    private String DiningTableAreaGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 区域名称
     */
    private String Name;

    /**
     * 状态   1=启用  0=停用
     */
    private int Enabled;

    /**
     * 序号
     */
    private int Sort;

    /**
     *
     */
    private ArrayList<DiningTable> diningTableList = new ArrayList<>();

    public String getDiningTableAreaGUID() {
        return DiningTableAreaGUID;
    }

    public void setDiningTableAreaGUID(String diningTableAreaGUID) {
        DiningTableAreaGUID = diningTableAreaGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getEnabled() {
        return Enabled;
    }

    public void setEnabled(int enabled) {
        Enabled = enabled;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public ArrayList<DiningTable> getDiningTableList() {
        return diningTableList;
    }

    public void setDiningTableList(ArrayList<DiningTable> diningTableList) {
        this.diningTableList = diningTableList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DiningTableAreaGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.Name);
        dest.writeInt(this.Enabled);
        dest.writeInt(this.Sort);
        dest.writeTypedList(this.diningTableList);
    }

    protected DiningTableArea(Parcel in) {
        this.DiningTableAreaGUID = in.readString();
        this.StoreGUID = in.readString();
        this.Name = in.readString();
        this.Enabled = in.readInt();
        this.Sort = in.readInt();
        this.diningTableList = in.createTypedArrayList(DiningTable.CREATOR);
    }

    public static final Creator<DiningTableArea> CREATOR = new Creator<DiningTableArea>() {
        @Override
        public DiningTableArea createFromParcel(Parcel source) {
            return new DiningTableArea(source);
        }

        @Override
        public DiningTableArea[] newArray(int size) {
            return new DiningTableArea[size];
        }
    };
}
