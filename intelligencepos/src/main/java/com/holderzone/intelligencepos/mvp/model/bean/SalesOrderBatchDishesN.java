package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by zhaoping on 2018/9/11.
 */
public class SalesOrderBatchDishesN {
    /***/
    private String SimpleName;
    /**菜品名称*/
    private String Name;
    /**0=未赠送  1=赠送*/
    private Integer Gift;
    /**菜品状态	 0 = 挂起 1 = 叫起 2=制作*/
    private Integer DishesStatus;
    /**套餐子项拼接*/
    private String SubDishesStr;
    /**划菜数量*/
    /**结算数量*/
    private Double CheckCount;
    /**退菜数量*/
    private Double BackCount;
    /**点单数量*/
    private Double OrderCount;
    /**制作数量*/
    private Double MakeCount;
    /**划菜数量*/
    private Double ServingCount;
    /**做法单价*/
    private Double PracticePrice;
    /**菜品单价（门店执行价或会员价）*/
    private Double Price;
    /**做法拼接*/
    private String PracticeStr;
    /**是否显示会员价	是否显示会员价 （此处仅判断会员登录 且执行价和会员价不相等时 才显示会员,会员价的开关由客户端判断）*/
    private Integer IsMemberPrice;

    public Integer getIsMemberPrice() {
        return IsMemberPrice;
    }

    public void setIsMemberPrice(Integer isMemberPrice) {
        IsMemberPrice = isMemberPrice;
    }

    public String getSimpleName() {
        return SimpleName;
    }

    public void setSimpleName(String simpleName) {
        SimpleName = simpleName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getGift() {
        return Gift;
    }

    public void setGift(Integer gift) {
        Gift = gift;
    }

    public Integer getDishesStatus() {
        return DishesStatus;
    }

    public void setDishesStatus(Integer dishesStatus) {
        DishesStatus = dishesStatus;
    }

    public String getSubDishesStr() {
        return SubDishesStr;
    }

    public void setSubDishesStr(String subDishesStr) {
        SubDishesStr = subDishesStr;
    }

    public Double getCheckCount() {
        return CheckCount;
    }

    public void setCheckCount(Double checkCount) {
        CheckCount = checkCount;
    }

    public Double getBackCount() {
        return BackCount;
    }

    public void setBackCount(Double backCount) {
        BackCount = backCount;
    }

    public Double getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(Double orderCount) {
        OrderCount = orderCount;
    }

    public Double getMakeCount() {
        return MakeCount;
    }

    public void setMakeCount(Double makeCount) {
        MakeCount = makeCount;
    }

    public Double getServingCount() {
        return ServingCount;
    }

    public void setServingCount(Double servingCount) {
        ServingCount = servingCount;
    }

    public Double getPracticePrice() {
        return PracticePrice;
    }

    public void setPracticePrice(Double practicePrice) {
        PracticePrice = practicePrice;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getPracticeStr() {
        return PracticeStr;
    }

    public void setPracticeStr(String practiceStr) {
        PracticeStr = practiceStr;
    }
}
