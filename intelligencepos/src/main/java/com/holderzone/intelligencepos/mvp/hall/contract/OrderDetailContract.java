package com.holderzone.intelligencepos.mvp.hall.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

import java.util.Map;

/**
 * 堂食点餐菜品详情contract
 * Created by LiTao on 2017-9-5.
 */

public interface OrderDetailContract {

    interface View extends IView {

        /**
         * 获取菜品估清数据成功
         */
        void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map);

        /**
         * 下单成功
         */
        void onCreateOrderSuccess(SalesOrderE salesOrderE);

        /**
         * 下单失败
         */
        void onCreateOrderFiled(String msg);
    }

    interface Presenter extends IPresenter {

        /**
         * 请求当前营业日估清
         */
        void getDishesEstimate();
        /**
         * 下单请求
         */
        void createOrder(SalesOrderBatchE salesOrderBatchE);
    }
}
