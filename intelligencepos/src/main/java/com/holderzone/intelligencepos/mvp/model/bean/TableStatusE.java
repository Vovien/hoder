package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/24.
 */

public class TableStatusE implements Parcelable{

    /**
     * 状态标识的ID
     * -1=全部
     * 0=空闲
     * 50=占用中
     * 51=有挂单
     * 52=已并单
     * 53=已算账
     * 60=已预订
     */
    private Integer ID;

    /**
     * 餐桌状态的中文描述
     */
    private String Name;

    /**
     * 餐桌数
     */
    private Integer TableCount;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getTableCount() {
        return TableCount;
    }

    public void setTableCount(Integer tableCount) {
        TableCount = tableCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ID);
        dest.writeString(this.Name);
        dest.writeValue(this.TableCount);
    }

    protected TableStatusE(Parcel in) {
        this.ID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Name = in.readString();
        this.TableCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<TableStatusE> CREATOR = new Creator<TableStatusE>() {
        @Override
        public TableStatusE createFromParcel(Parcel source) {
            return new TableStatusE(source);
        }

        @Override
        public TableStatusE[] newArray(int size) {
            return new TableStatusE[size];
        }
    };
}
