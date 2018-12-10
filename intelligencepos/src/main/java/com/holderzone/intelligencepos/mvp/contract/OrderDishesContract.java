package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 堂食点餐contract
 * Created by chencao on 2017/9/4.
 */

public interface OrderDishesContract {

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

        void onGetSystemConfigSuccess(ParametersConfig config);

        /**
         * 会员退出成功
         */
        void onMemberExitSuccess();
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
         * 获取是否开启会员价
         */
        void getHasOpenMemberPrice();

        /**
         * 会员退出
         */
        void memberLoginOut(String salesOrderGuid);
    }
}
