package com.holderzone.intelligencepos.mvp.model.bean;

import java.io.Serializable;
import java.util.UUID;

/**
 * 菜品拆分信息
 * Created by Administrator on 2017/1/19.
 */

public class SeparateDishes implements Serializable{
    /**唯一标识*/
    private String uuid;
    /**数量*/
    private Double count;
    /**备注*/
    private String remark;
    /**
     * 赠品标识 0=非赠送 1=赠送
     */
    private int Gift;

    /**
     * 厨房打印状态[-1：挂起0：待打印1：已打印]
     */
    private Integer KitchenPrintStatus = 0;

    public SeparateDishes(Double count) {
        uuid = UUID.randomUUID().toString();
        this.count = count;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getGift() {
        return Gift;
    }

    public void setGift(int gift) {
        Gift = gift;
    }

    public Integer getKitchenPrintStatus() {
        return KitchenPrintStatus;
    }

    public void setKitchenPrintStatus(Integer kitchenPrintStatus) {
        KitchenPrintStatus = kitchenPrintStatus;
    }
}
