package com.holderzone.intelligencepos.mvp.model.prefs;

import com.holderzone.intelligencepos.mvp.model.ReleaseResourceManager;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by tcw on 2017/7/21.
 */

public interface PrefsManager extends ReleaseResourceManager {

    Observable<List<DishesTypeE>> getAllDishesTypeE();

    Observable<List<DishesTypeE>> saveAllDishesTypeE(List<DishesTypeE> allDishesTypeE);

    Observable<List<DishesE>> getAllDishesE();

    Observable<List<DishesE>> saveAllDishesE(List<DishesE> allDishesE);

    Observable<Integer> getBtnStyle();

    Observable<Integer> saveBtnStyle(Integer btnStyle);

    Observable<Boolean> saveIsStartFlapper(boolean isStart);

    Observable<Boolean> getIsStartFlapper();

    Observable<Boolean> saveIsDesignatedDishes(boolean isDesignatedDishes);

    Observable<Boolean> getIsDesignatedDishes();

    Observable<ParametersConfig> saveSystemConfig(ParametersConfig parametersConfig);
    Observable<ParametersConfig> getSystemConfig();


}
