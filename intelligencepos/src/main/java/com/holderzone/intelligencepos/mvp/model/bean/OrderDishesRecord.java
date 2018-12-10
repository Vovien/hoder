package com.holderzone.intelligencepos.mvp.model.bean;

import com.holderzone.intelligencepos.utils.ArithUtil;

import java.util.List;

/**
 * 点菜记录
 * Created by Administrator on 2017/3/9.
 */

public class OrderDishesRecord {

    /**
     * 赠品标识 0=非赠送 1=赠送
     */
    private Integer gift;

    /**
     * 厨房打印状态[-1：挂起0：待打印1：已打印]
     */
    private Integer kitchenPrintStatus;
    /**
     * 菜品名称
     */
    private String dishesName;

    /**
     * 价格
     */
    private Double price;

    /**
     * 菜品主键
     */
    private String dishesGuid;
    /**
     * 做法
     */
    private List<CuisineMethodBean> cuisineMethodBeanList;

    /**
     * 下单数量 正数是加菜，负数是退菜
     */
    private Double orderCount;

    /**
     * 数量(主要用于点菜记录第一条数据显示标题)
     */
    private Integer Count;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取做法名称列表（逗号分隔）
     *
     * @return
     */
    public String getCuisineMethodString() {
        String cuisineMethodString = "";
        if (cuisineMethodBeanList != null && cuisineMethodBeanList.size() > 0) {
            for (CuisineMethodBean bean : cuisineMethodBeanList) {
                cuisineMethodString += bean.getCuisineMethodName() + ",";
            }
            cuisineMethodString = cuisineMethodString.substring(0, cuisineMethodString.length() - 1);
        }
        return cuisineMethodString;
    }

    /**
     * 获取做法总金额
     */
    public double getCuisineMethodTotalPrice() {
        double cuisineMethodTotalPrice = 0.0;
        if (cuisineMethodBeanList != null && cuisineMethodBeanList.size() > 0) {
            for (CuisineMethodBean bean : cuisineMethodBeanList) {
                ArithUtil.add(ArithUtil.mul(bean.getPrice(), orderCount), cuisineMethodTotalPrice);
            }
        }
        return cuisineMethodTotalPrice;
    }

    public Integer getGift() {
        return gift;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }

    public Integer getKitchenPrintStatus() {
        return kitchenPrintStatus;
    }

    public void setKitchenPrintStatus(Integer kitchenPrintStatus) {
        this.kitchenPrintStatus = kitchenPrintStatus;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public String getDishesGuid() {
        return dishesGuid;
    }

    public void setDishesGuid(String dishesGuid) {
        this.dishesGuid = dishesGuid;
    }

    public List<CuisineMethodBean> getCuisineMethodBeanList() {
        return cuisineMethodBeanList;
    }

    public void setCuisineMethodBeanList(List<CuisineMethodBean> cuisineMethodBeanList) {
        this.cuisineMethodBeanList = cuisineMethodBeanList;
    }

    public Double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Double orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
