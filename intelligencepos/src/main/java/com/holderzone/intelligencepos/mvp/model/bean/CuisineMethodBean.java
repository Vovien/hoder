package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 做法实体
 * Created by Administrator on 2017/3/7.
 */

public class CuisineMethodBean {
    /**
     * 做法主键
     */
    private String guid;
    /**
     * 做法名称
     */
    private String cuisineMethodName;
    /**
     * 价格
     */
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCuisineMethodName() {
        return cuisineMethodName;
    }

    public void setCuisineMethodName(String cuisineMethodName) {
        this.cuisineMethodName = cuisineMethodName;
    }
}
