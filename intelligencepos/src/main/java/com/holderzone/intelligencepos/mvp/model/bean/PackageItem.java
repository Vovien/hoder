package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by Administrator on 2016/10/31.
 */
public class PackageItem {
    /**
     * 数据标识
     */
    private String PackageItemGUID;

    /**
     * 套餐菜品
     */
    private String DishesGUID;

    /**
     * 套餐子项菜品
     */
    private String SubDishesGUID;

    private Dishes SubDishes;

    /**
     * 数量
     */
    private Double DishesCount;

    public String getPackageItemGUID() {
        return PackageItemGUID;
    }

    public void setPackageItemGUID(String packageItemGUID) {
        PackageItemGUID = packageItemGUID;
    }

    public String getDishesGUID() {
        return DishesGUID;
    }

    public void setDishesGUID(String dishesGUID) {
        DishesGUID = dishesGUID;
    }

    public String getSubDishesGUID() {
        return SubDishesGUID;
    }

    public void setSubDishesGUID(String subDishesGUID) {
        SubDishesGUID = subDishesGUID;
    }

    public Dishes getSubDishes() {
        return SubDishes;
    }

    public void setSubDishes(Dishes subDishes) {
        SubDishes = subDishes;
    }

    public Double getDishesCount() {
        return DishesCount;
    }

    public void setDishesCount(Double dishesCount) {
        DishesCount = dishesCount;
    }
}
