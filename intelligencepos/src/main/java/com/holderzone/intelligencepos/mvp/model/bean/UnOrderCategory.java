package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderCategory {
    private String mMsgType;
    private String mUnOrderCategoryName;
    private int mUnOrderCategoryCount;

    public UnOrderCategory(String msgType, String unOrderCategoryName, int unOrderCategoryCount) {
        mMsgType = msgType;
        mUnOrderCategoryName = unOrderCategoryName;
        mUnOrderCategoryCount = unOrderCategoryCount;
    }

    public String getMsgType() {
        return mMsgType;
    }

    public void setMsgType(String msgType) {
        mMsgType = msgType;
    }

    public String getUnOrderCategoryName() {
        return mUnOrderCategoryName;
    }

    public void setUnOrderCategoryName(String unOrderCategoryName) {
        mUnOrderCategoryName = unOrderCategoryName;
    }

    public int getUnOrderCategoryCount() {
        return mUnOrderCategoryCount;
    }

    public void setUnOrderCategoryCount(int unOrderCategoryCount) {
        mUnOrderCategoryCount = unOrderCategoryCount;
    }
}
