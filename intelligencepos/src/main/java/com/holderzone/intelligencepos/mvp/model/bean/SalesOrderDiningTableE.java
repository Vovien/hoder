package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 桌台菜品
 */
public class SalesOrderDiningTableE implements Parcelable {

    /**
     * 数据标识
     */

    private String SalesOrderGUID;

    /**
     * 菜品明细
     */
    private List<SalesOrderDishesE> ArrayOfSalesOrderDishesE;

    /**
     * 区域名称
     */
    private String AreaName;

    /**
     * 桌台名称
     */
    private String DtName;

    /**
     * 并单状态 0无并单 1主单 2子单
     */
    private Integer UpperState;

    /**
     * 单个桌台的附加费合计
     */
    private Double DtAdditionalFeesTotal;

    public Double getDtAdditionalFeesTotal() {
        return DtAdditionalFeesTotal;
    }

    public void setDtAdditionalFeesTotal(Double dtAdditionalFeesTotal) {
        DtAdditionalFeesTotal = dtAdditionalFeesTotal;
    }

    private List<SalesOrderAdditionalFeesE> ArrayOfSalesOrderAdditionalFeesE;

    public List<SalesOrderAdditionalFeesE> getArrayOfSalesOrderAdditionalFeesE() {
        return ArrayOfSalesOrderAdditionalFeesE;
    }

    public void setArrayOfSalesOrderAdditionalFeesE(List<SalesOrderAdditionalFeesE> arrayOfSalesOrderAdditionalFeesE) {
        ArrayOfSalesOrderAdditionalFeesE = arrayOfSalesOrderAdditionalFeesE;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public Integer getUpperState() {
        return UpperState;
    }

    public void setUpperState(Integer upperState) {
        UpperState = upperState;
    }

    public List<SalesOrderDishesE> getArrayOfSalesOrderDishesE() {
        return ArrayOfSalesOrderDishesE;
    }

    public void setArrayOfSalesOrderDishesE(List<SalesOrderDishesE> arrayOfSalesOrderDishesE) {
        ArrayOfSalesOrderDishesE = arrayOfSalesOrderDishesE;
    }


    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getDtName() {
        return DtName;
    }

    public void setDtName(String dtName) {
        DtName = dtName;
    }


    public SalesOrderDiningTableE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SalesOrderGUID);
        dest.writeTypedList(this.ArrayOfSalesOrderDishesE);
        dest.writeString(this.AreaName);
        dest.writeString(this.DtName);
        dest.writeValue(this.UpperState);
        dest.writeValue(this.DtAdditionalFeesTotal);
        dest.writeTypedList(this.ArrayOfSalesOrderAdditionalFeesE);
    }

    protected SalesOrderDiningTableE(Parcel in) {
        this.SalesOrderGUID = in.readString();
        this.ArrayOfSalesOrderDishesE = in.createTypedArrayList(SalesOrderDishesE.CREATOR);
        this.AreaName = in.readString();
        this.DtName = in.readString();
        this.UpperState = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DtAdditionalFeesTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.ArrayOfSalesOrderAdditionalFeesE = in.createTypedArrayList(SalesOrderAdditionalFeesE.CREATOR);
    }

    public static final Creator<SalesOrderDiningTableE> CREATOR = new Creator<SalesOrderDiningTableE>() {
        @Override
        public SalesOrderDiningTableE createFromParcel(Parcel source) {
            return new SalesOrderDiningTableE(source);
        }

        @Override
        public SalesOrderDiningTableE[] newArray(int size) {
            return new SalesOrderDiningTableE[size];
        }
    };
}
