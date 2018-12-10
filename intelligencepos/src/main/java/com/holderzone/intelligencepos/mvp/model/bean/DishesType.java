package com.holderzone.intelligencepos.mvp.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaoping on 2016/4/19.
 * 菜品类型
 */
public class DishesType implements Serializable {

    /**
     * 默认构造
     */
    public DishesType() {
    }

    /**
     * 自增ID
     */
    private Integer DishesTypeID;
    /**
     * 编号
     */
    private String DishesTypeUID;
    /**
     * 数据标识
     */
    private String DishesTypeGUID;

    /**
     * 是否删除
     */
    private Integer IsDelete = 0;

    /**
     * 名称
     */
    private String Name;
    /**
     * 同步时间
     */
    private Integer LastStamp;
    /**
     * 状态 1=启用 0=停用
     */
    private Integer Enabled = 1;
    /**
     * 排序号
     */
    private Integer Sort = 0;
    /**
     * 数据同步状态 0=新增 1=修改 2删除 100=已同步
     */
    //public Integer DataSync = 0;

    /**
     * 最后同步ID
     */
    private Long LastUID;
    /**
     * 默认类别 0正常类别  1 默认
     */
    private Integer IsDefault = 0;

    private List<Dishes> ArrayOfDishes;

    @Override
    public boolean equals(Object o) {
        DishesType temp = (DishesType) o;
        if (temp.IsDefault.intValue() == IsDefault && Enabled.intValue() == temp.Enabled && Name.equals(temp.Name)) {
            return true;
        }
        return false;
    }

    public Integer getDishesTypeID() {
        return DishesTypeID;
    }

    public void setDishesTypeID(Integer dishesTypeID) {
        DishesTypeID = dishesTypeID;
    }

    public String getDishesTypeUID() {
        return DishesTypeUID;
    }

    public void setDishesTypeUID(String dishesTypeUID) {
        DishesTypeUID = dishesTypeUID;
    }

    public String getDishesTypeGUID() {
        return DishesTypeGUID;
    }

    public void setDishesTypeGUID(String dishesTypeGUID) {
        DishesTypeGUID = dishesTypeGUID;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getLastStamp() {
        return LastStamp;
    }

    public void setLastStamp(Integer lastStamp) {
        LastStamp = lastStamp;
    }

    public Integer getEnabled() {
        return Enabled;
    }

    public void setEnabled(Integer enabled) {
        Enabled = enabled;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public Long getLastUID() {
        return LastUID;
    }

    public void setLastUID(Long lastUID) {
        LastUID = lastUID;
    }

    public Integer getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(Integer isDefault) {
        IsDefault = isDefault;
    }

    public List<Dishes> getArrayOfDishes() {
        return ArrayOfDishes;
    }

    public void setArrayOfDishes(List<Dishes> arrayOfDishes) {
        ArrayOfDishes = arrayOfDishes;
    }
}
