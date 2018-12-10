package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 海普新定义的 红包实体
 */
public class RedPackageModel implements Parcelable {
    /**
     * 红包名称
     */
    private String redPacketName;
    /**
     * 二维码
     */
    private String qrCode;
    private String barCode;
    /**
     * 红包金额
     */
    private Double money;
    /**
     * 红包状态 （1 待使用 2 已过期 3 已使用）
     */
    private String status;
    private String startTime;
    private String endTime;
    /**
     * 是否选中
     */
    private Boolean selected;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getRedPacketName() {
        return redPacketName;
    }

    public void setRedPacketName(String redPacketName) {
        this.redPacketName = redPacketName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public RedPackageModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.redPacketName);
        dest.writeString(this.qrCode);
        dest.writeString(this.barCode);
        dest.writeValue(this.money);
        dest.writeString(this.status);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeValue(this.selected);
    }

    protected RedPackageModel(Parcel in) {
        this.redPacketName = in.readString();
        this.qrCode = in.readString();
        this.barCode = in.readString();
        this.money = (Double) in.readValue(Double.class.getClassLoader());
        this.status = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.selected = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<RedPackageModel> CREATOR = new Creator<RedPackageModel>() {
        @Override
        public RedPackageModel createFromParcel(Parcel source) {
            return new RedPackageModel(source);
        }

        @Override
        public RedPackageModel[] newArray(int size) {
            return new RedPackageModel[size];
        }
    };
}
