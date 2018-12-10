package com.holderzone.intelligencepos.mvp.model.bean.db;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 支付方式
 * Created by Administrator on 2017/3/13.
 */

@Entity(nameInDb = "PaymentItem")
public class PaymentItem implements Parcelable{
    /**
     * 编码
     */
    @Property(nameInDb = "PaymentItemCode")
    private String PaymentItemCode;
    /**
     * 支付名称
     */
    @Property(nameInDb = "PaymentItemName")
    private String PaymentItemName;
    /**
     * 排序
     */
    @Property(nameInDb = "Sort")
    private int Sort;
    /**
     * 二维码
     */
    @Property(nameInDb = "QrCode")
    private String QrCode;

    public String getPaymentItemCode() {
        return PaymentItemCode;
    }

    public void setPaymentItemCode(String paymentItemCode) {
        PaymentItemCode = paymentItemCode;
    }

    public String getPaymentItemName() {
        return PaymentItemName;
    }

    public void setPaymentItemName(String paymentItemName) {
        PaymentItemName = paymentItemName;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getQrCode() {
        return QrCode;
    }

    public void setQrCode(String qrCode) {
        QrCode = qrCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PaymentItemCode);
        dest.writeString(this.PaymentItemName);
        dest.writeInt(this.Sort);
        dest.writeString(this.QrCode);
    }

    protected PaymentItem(Parcel in) {
        this.PaymentItemCode = in.readString();
        this.PaymentItemName = in.readString();
        this.Sort = in.readInt();
        this.QrCode = in.readString();
    }

    @Generated(hash = 385433338)
    public PaymentItem(String PaymentItemCode, String PaymentItemName, int Sort,
            String QrCode) {
        this.PaymentItemCode = PaymentItemCode;
        this.PaymentItemName = PaymentItemName;
        this.Sort = Sort;
        this.QrCode = QrCode;
    }

    @Generated(hash = 45838440)
    public PaymentItem() {
    }

    public static final Creator<PaymentItem> CREATOR = new Creator<PaymentItem>() {
        @Override
        public PaymentItem createFromParcel(Parcel source) {
            return new PaymentItem(source);
        }

        @Override
        public PaymentItem[] newArray(int size) {
            return new PaymentItem[size];
        }
    };
}
