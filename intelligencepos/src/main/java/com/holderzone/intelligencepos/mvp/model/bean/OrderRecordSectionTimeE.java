package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 预订时间
 * Created by zhaoping on 2017/5/4.
 */

public class OrderRecordSectionTimeE {
    /**
     * 时间点 格式：1800,1830
     */
    private String SectionTime;
    /**
     * 当前是否可用
     * 1=可用  0=不可用
     * 由服务端根据当前时间计算是否可用
     */
    private Integer CanUseNow;

    public String getSectionTime() {
        return SectionTime;
    }

    public void setSectionTime(String sectionTime) {
        SectionTime = sectionTime;
    }

    public OrderRecordSectionTimeE(String sectionTime) {
        SectionTime = sectionTime;
    }

    public OrderRecordSectionTimeE() {
    }
    public Integer getCanUseNow() {
        return CanUseNow;
    }

    public void setCanUseNow(Integer canUseNow) {
        CanUseNow = canUseNow;
    }
}
