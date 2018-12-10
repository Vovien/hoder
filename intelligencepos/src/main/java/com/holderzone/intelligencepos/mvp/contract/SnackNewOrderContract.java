package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;

import java.util.List;
import java.util.Map;

/**
 * 新点单fragment
 * Created by chencao on 2017/8/2.
 */

public interface SnackNewOrderContract {

    interface View extends IView {

        /**
         * 获取菜品列表信息
         *
         * @param dishesTypeList
         */
        void refreshDishesData(List<DishesTypeE> dishesTypeList);

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
        void refreshDishesStatus(List<DishesTypeE> list);

        /**
         * 没有网络
         */
        void showNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取菜品数据
         */
        void getDishesType();

        /**
         * 根据估清信息设置菜品停售信息
         */
        void updateDishesStopStatus(List<DishesTypeE> dishesList, Map<String, DishesEstimateRecordDishes> map);

        /**
         * 获取当前营业日估清
         */
        void getDishesEstimate(boolean isShowDialog);

    }
}
