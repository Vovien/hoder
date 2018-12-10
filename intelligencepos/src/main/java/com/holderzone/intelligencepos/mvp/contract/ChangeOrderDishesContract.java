package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesPracticeE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LT on 2018-04-04.
 */

public interface ChangeOrderDishesContract {
    interface View extends IView {
        /**
         * 获取当前营业日估清
         *
         * @param map
         */
        void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map);

        /**
         * 获取菜品列表信息
         *
         * @param dishesTypeList
         */
        void getDishesData(List<DishesTypeE> dishesTypeList, HashMap<String, List<DishesPracticeE>> dishesPracticeMap);

        /**
         * 刷新菜品状态(已售罄)
         *
         * @param list
         */
        void refreshDishesStatus(List<DishesTypeE> list);

        /**
         * 换菜成功
         */
        void onChangeDishesSuccess();

        /**
         * 换菜失败
         */
        void onChangeDishesFiled();

    }

    interface Presenter extends IPresenter {
        /**
         * 获取当前营业日估清
         */
        void getDishesEstimate();

        /**
         * 获取菜品信息
         */
        void getDishesType();

        /**
         * 根据估清信息设置菜品停售信息
         */
        void updateDishesStopStatus(List<DishesTypeE> dishesList, Map<String, DishesEstimateRecordDishes> map);

        /**
         * 换菜
         *
         * @param orderGuid
         * @param originalDishesGuid
         * @param confirmDishesGuid
         * @param orderType          1为微信
         * @param practiceList
         */
        void changeDishes(String orderGuid, String originalDishesGuid, String confirmDishesGuid, Integer orderType, List<UnOrderDishesPracticeE> practiceList);
    }
}
