package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by tcw on 2017/3/16.
 */

public class DishesEstimateRecordE {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**操作员帐号标识*/
    private String CreateUsersGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;
    /**
     * 菜品估清标识
     */
    private String DishesEstimateRecordGUID;

    /**
     * 营业日
     */
    private String BusinessDay;
    /**
     * 起始营业日
     */
    private String StartBusinessDay;
    /**
     * 结束营业日
     */
    private String EndBusinessDay;
    /**
     * 是否返回菜品估清记录明细
     */
    private Integer ReturnDetaile;

    public List<DishesEstimateRecordDishes> getArrayOfDishesEstimateRecordDishes() {
        return ArrayOfDishesEstimateRecordDishes;
    }

    public void setArrayOfDishesEstimateRecordDishes(List<DishesEstimateRecordDishes> arrayOfDishesEstimateRecordDishes) {
        ArrayOfDishesEstimateRecordDishes = arrayOfDishesEstimateRecordDishes;
    }

    private List<DishesEstimateRecordDishes> ArrayOfDishesEstimateRecordDishes;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getDishesEstimateRecordGUID() {
        return DishesEstimateRecordGUID;
    }

    public void setDishesEstimateRecordGUID(String dishesEstimateRecordGUID) {
        DishesEstimateRecordGUID = dishesEstimateRecordGUID;
    }

    public Integer getReturnDetaile() {
        return ReturnDetaile;
    }

    public void setReturnDetaile(Integer returnDetaile) {
        ReturnDetaile = returnDetaile;
    }

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getCreateUsersGUID() {
        return CreateUsersGUID;
    }

    public void setCreateUsersGUID(String createUsersGUID) {
        CreateUsersGUID = createUsersGUID;
    }
}
