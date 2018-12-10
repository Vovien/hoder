package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class DishesTypeE extends DishesType{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;

    public List<DishesE> ArrayOfDishesE;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public List<DishesE> getArrayOfDishesE() {
        return ArrayOfDishesE;
    }

    public void setArrayOfDishesE(List<DishesE> arrayOfDishesE) {
        ArrayOfDishesE = arrayOfDishesE;
    }
}
