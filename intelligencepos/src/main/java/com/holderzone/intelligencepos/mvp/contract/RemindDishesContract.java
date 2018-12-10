package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;

import java.util.List;

/**
 * 催菜contract
 * Created by tcw on 2017/6/6.
 */

public interface RemindDishesContract {

    interface View extends IView {

        /**
         * 请求叫起数据成功
         *
         * @param arrayOfSalesOrderBatchDishesE
         */
        void onRequestCalledUpDishesSucceed(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE);

        /**
         * 请求叫起数据失败
         */
        void onRequestCalledUpDishesFailed();

        /**
         * 催菜成功
         */
        void onRemindSucceed();

        /**
         * 催菜失败
         */
        void onRemindFailed();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求叫起数据
         *
         * @param salesOrderGUID
         * @param checkStatus    0==叫起数据 1==叫起数据
         */
        void requestCalledUpDishes(String salesOrderGUID, Integer checkStatus);

        /**
         * 提交叫起数据
         *
         * @param salesOrderBatchDishesEList
         */
        void submitRemindDishes(List<SalesOrderBatchDishesE> salesOrderBatchDishesEList);//提交叫起数据
    }
}