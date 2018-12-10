package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by tcw10 on 2017/3/10.
 */

public class DiningTableAreaE extends DiningTableArea implements Parcelable{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 区域UID
     */
    private String DiningTableAreaUID;
    /**
     * 区域ID
     */
    private Integer DiningTableAreaID;
    /**
     * 是否删除
     */
    private Integer IsDelete;

    /**
     * 此区域拥有的餐桌数量
     */
    private int TableCount;

    /**
     * 预订个数
     */
    private int OrderCount;

    /**
     * 当前占用数量 仅占用，不包含预订
     */
    private int UseCount;
    /**
     *
     */
    private List<DiningTableE> ArrayOfDiningTableE;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getDiningTableAreaUID() {
        return DiningTableAreaUID;
    }

    public void setDiningTableAreaUID(String diningTableAreaUID) {
        DiningTableAreaUID = diningTableAreaUID;
    }

    public Integer getDiningTableAreaID() {
        return DiningTableAreaID;
    }

    public void setDiningTableAreaID(Integer diningTableAreaID) {
        DiningTableAreaID = diningTableAreaID;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    public int getTableCount() {
        return TableCount;
    }

    public void setTableCount(int tableCount) {
        TableCount = tableCount;
    }

    public int getUseCount() {
        return UseCount;
    }

    public void setUseCount(int useCount) {
        UseCount = useCount;
    }

    public List<DiningTableE> getArrayOfDiningTableE() {
        return ArrayOfDiningTableE;
    }

    public void setArrayOfDiningTableE(List<DiningTableE> arrayOfDiningTableE) {
        ArrayOfDiningTableE = arrayOfDiningTableE;
    }

    public DiningTableAreaE() {
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.DiningTableAreaUID);
        dest.writeValue(this.DiningTableAreaID);
        dest.writeValue(this.IsDelete);
        dest.writeInt(this.TableCount);
        dest.writeInt(this.OrderCount);
        dest.writeInt(this.UseCount);
        dest.writeTypedList(this.ArrayOfDiningTableE);
    }

    protected DiningTableAreaE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
        this.DiningTableAreaUID = in.readString();
        this.DiningTableAreaID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsDelete = (Integer) in.readValue(Integer.class.getClassLoader());
        this.TableCount = in.readInt();
        this.OrderCount = in.readInt();
        this.UseCount = in.readInt();
        this.ArrayOfDiningTableE = in.createTypedArrayList(DiningTableE.CREATOR);
    }

    public static final Creator<DiningTableAreaE> CREATOR = new Creator<DiningTableAreaE>() {
        @Override
        public DiningTableAreaE createFromParcel(Parcel source) {
            return new DiningTableAreaE(source);
        }

        @Override
        public DiningTableAreaE[] newArray(int size) {
            return new DiningTableAreaE[size];
        }
    };
}
