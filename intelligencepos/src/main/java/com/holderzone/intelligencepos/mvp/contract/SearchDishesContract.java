package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;

import java.util.List;
import java.util.Map;

/**
 * 查询菜品
 */
public interface SearchDishesContract {
    interface View extends IView {
        /**
         * 获取菜品信息
         *
         * @param dishesData
         */
        void setDishesData(List<DishesE> dishesData);

        /**
         * 菜品做法列表
         *
         * @param dishesPracticeList
         */
        void setDishesPracticeData(Map<String, List<DishesPracticeE>> dishesPracticeList);

        /**
         * 获取当前营业日估清
         *
         * @param map
         */
        void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map);

        /**
         * 刷新菜品停售信息
         *
         * @param list
         */
        void refreshDishesStatus(List<DishesE> list);

        /**
         * 没有网络
         */
        void showNetworkError();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取菜品做法信息
         */
        void getDishesAndPracticeList();

        /**
         * 根据估清信息设置菜品停售信息
         */
        void updateDishesStopStatus(List<DishesE> dishesEList, Map<String, DishesEstimateRecordDishes> map);

        /**
         * 获取当前营业日估清
         */
        void getDishesEstimate();
    }
}
