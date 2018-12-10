package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by Administrator on 2018/3/24/024.
 */

public class ParametersConfig {
    /**
     * 是否何师会员系统
     */
    private boolean isHesMember;

    /**
     * 是否启用交接班
     */

    private boolean isHandRecord;
    /**
     * 基础数据的判断  判断是否是何师版本（新加字段）
     */
    private boolean isHesVersion;

    /**
     * 是否平板点餐
     */

    private boolean isPadCheck;
    /**
     * 是否启用划菜
     */

    private boolean isServingDishes;

    /**
     * 是否允许自动接单
     */
    private boolean isEnableAutoOrder;

    /**
     * 是否允许键盘录入模式
     */
    private boolean isEnableKeyboardInputMode;
    /**
     * 是否启用会员价
     */
    private boolean isMemberPrice;

    /**
     * 菜品录入模式
     * 0 == 默认模式
     * 1 == 键盘录入模式
     * 2 == 扫描枪模式
     */
    private Integer dishesInputMode;

    /**
     * 菜品编码长度
     */
    private Integer dishesCodeLength;
    /**
     * 快餐是否输入人数
     */
    private boolean isFastSalesGuestCount;

    public boolean isHesVersion() {
        return isHesVersion;
    }

    public void setHesVersion(boolean hesVersion) {
        isHesVersion = hesVersion;
    }

    public boolean isHandRecord() {
        return isHandRecord;
    }

    public void setHandRecord(boolean handRecord) {
        isHandRecord = handRecord;
    }

    public boolean isPadCheck() {
        return isPadCheck;
    }

    public void setPadCheck(boolean padCheck) {
        isPadCheck = padCheck;
    }

    public boolean isServingDishes() {
        return isServingDishes;
    }

    public void setServingDishes(boolean servingDishes) {
        isServingDishes = servingDishes;
    }

    public boolean isEnableAutoOrder() {
        return isEnableAutoOrder;
    }

    public void setEnableAutoOrder(boolean enableAutoOrder) {
        isEnableAutoOrder = enableAutoOrder;
    }

    public boolean isEnableKeyboardInputMode() {
        return isEnableKeyboardInputMode;
    }

    public void setEnableKeyboardInputMode(boolean enableKeyboardInputMode) {
        isEnableKeyboardInputMode = enableKeyboardInputMode;
    }

    public Integer getDishesInputMode() {
        return dishesInputMode;
    }

    public void setDishesInputMode(Integer dishesInputMode) {
        this.dishesInputMode = dishesInputMode;
    }

    public Integer getDishesCodeLength() {
        return dishesCodeLength;
    }

    public void setDishesCodeLength(Integer dishesCodeLength) {
        this.dishesCodeLength = dishesCodeLength;
    }

    public boolean isFastSalesGuestCount() {
        return isFastSalesGuestCount;
    }

    public void setFastSalesGuestCount(boolean fastSalesGuestCount) {
        isFastSalesGuestCount = fastSalesGuestCount;
    }

    public boolean isMemberPrice() {
        return isMemberPrice;
    }

    public boolean isHesMember() {
        return isHesMember;
    }

    public void setHesMember(boolean hesMember) {
        isHesMember = hesMember;
    }

    public void setMemberPrice(boolean memberPrice) {
        isMemberPrice = memberPrice;
    }
}
