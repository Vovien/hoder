package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;

import java.util.List;

/**
 * 退菜contract
 * Created by tcw on 2017/6/6.
 */

public interface RetreatDishesContract {
    interface View extends IView {

        /**
         * 请求挂起中&&已叫起菜品数据成功
         * @param arrayOfSalesOrderBatchDishesE
         */
        void onRequestDishesSucceed(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE);

        /**
         * 请求挂起中&&已叫起菜品数据失败
         */
        void onRequestDishesFailed();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 挂起中&&已叫起菜品数据
         *
         * @param salesOrderGUID
         * @param checkStatus    0==挂起数据 1==叫起数据 null==全部数据
         */
        void requestDishes(String salesOrderGUID, Integer checkStatus);
    }
}