package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.HashMap;
import java.util.List;

/**
 * 菜品基本数据
 * Created by Administrator on 2017/3/21.
 */

public class DishesBaseData {
    private List<DishesTypeE> dishesTypeEList;
    private List<DishesE> dishesEList;
    private HashMap<String, List<DishesPracticeE>> dishesPracticeMap;

    public List<DishesE> getDishesEList() {
        return dishesEList;
    }

    public void setDishesEList(List<DishesE> dishesEList) {
        this.dishesEList = dishesEList;
    }

    public List<DishesTypeE> getDishesTypeEList() {
        return dishesTypeEList;
    }

    public void setDishesTypeEList(List<DishesTypeE> dishesTypeEList) {
        this.dishesTypeEList = dishesTypeEList;
    }

    public HashMap<String, List<DishesPracticeE>> getDishesPracticeMap() {
        return dishesPracticeMap;
    }

    public void setDishesPracticeMap(HashMap<String, List<DishesPracticeE>> dishesPracticeMap) {
        this.dishesPracticeMap = dishesPracticeMap;
    }
}
