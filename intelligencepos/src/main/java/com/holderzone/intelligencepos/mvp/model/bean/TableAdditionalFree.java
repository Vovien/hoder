package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaoping on 2017/6/30.
 */

public class TableAdditionalFree implements Parcelable {
    private String tableGuid;
    private String salesOrderGuid;
    private Double total;
    private String tableName;

    public String getTableGuid() {
        return tableGuid;
    }

    public void setTableGuid(String tableGuid) {
        this.tableGuid = tableGuid;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSalesOrderGuid() {
        return salesOrderGuid;
    }

    public void setSalesOrderGuid(String salesOrderGuid) {
        this.salesOrderGuid = salesOrderGuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tableGuid);
        dest.writeString(this.salesOrderGuid);
        dest.writeValue(this.total);
        dest.writeString(this.tableName);
    }

    public TableAdditionalFree() {
    }

    protected TableAdditionalFree(Parcel in) {
        this.tableGuid = in.readString();
        this.salesOrderGuid = in.readString();
        this.total = (Double) in.readValue(Double.class.getClassLoader());
        this.tableName = in.readString();
    }

    public static final Creator<TableAdditionalFree> CREATOR = new Creator<TableAdditionalFree>() {
        @Override
        public TableAdditionalFree createFromParcel(Parcel source) {
            return new TableAdditionalFree(source);
        }

        @Override
        public TableAdditionalFree[] newArray(int size) {
            return new TableAdditionalFree[size];
        }
    };
}
