package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-04-02.
 */

public class UnReminderReplyContent implements Parcelable {
    /**
     * 自增ID
     */
    private Integer UnReminderReplyContentID;

    /**
     * UID
     */
    private String UnReminderReplyContentUID;

    /**
     * 标识 Guid 主键
     */
    private String UnReminderReplyContentGUID;

    /**
     * 回复内容
     */
    private String Content;

    /**
     * 是否启用
     * 0=未启用，1=已启用（默认）
     */
    private Integer IsEnable;

    public Integer getUnReminderReplyContentID() {
        return UnReminderReplyContentID;
    }

    public void setUnReminderReplyContentID(Integer unReminderReplyContentID) {
        UnReminderReplyContentID = unReminderReplyContentID;
    }

    public String getUnReminderReplyContentUID() {
        return UnReminderReplyContentUID;
    }

    public void setUnReminderReplyContentUID(String unReminderReplyContentUID) {
        UnReminderReplyContentUID = unReminderReplyContentUID;
    }

    public String getUnReminderReplyContentGUID() {
        return UnReminderReplyContentGUID;
    }

    public void setUnReminderReplyContentGUID(String unReminderReplyContentGUID) {
        UnReminderReplyContentGUID = unReminderReplyContentGUID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(Integer isEnable) {
        IsEnable = isEnable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.UnReminderReplyContentID);
        dest.writeString(this.UnReminderReplyContentUID);
        dest.writeString(this.UnReminderReplyContentGUID);
        dest.writeString(this.Content);
        dest.writeValue(this.IsEnable);
    }

    public UnReminderReplyContent() {
    }

    protected UnReminderReplyContent(Parcel in) {
        this.UnReminderReplyContentID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UnReminderReplyContentUID = in.readString();
        this.UnReminderReplyContentGUID = in.readString();
        this.Content = in.readString();
        this.IsEnable = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<UnReminderReplyContent> CREATOR = new Parcelable.Creator<UnReminderReplyContent>() {
        @Override
        public UnReminderReplyContent createFromParcel(Parcel source) {
            return new UnReminderReplyContent(source);
        }

        @Override
        public UnReminderReplyContent[] newArray(int size) {
            return new UnReminderReplyContent[size];
        }
    };
}
