package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;

import java.util.List;

/**
 * 叫起contract
 * Created by chencao on 2017/9/4.
 */

public interface CallUpDishesContract {
    interface View extends IView {

        /**
         * 请求挂起数据成功
         * @param arrayOfSalesOrderBatchDishesE
         */
        void onRequestHangedUpDishesSucceed(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE);

        /**
         * 请求挂起数据失败
         */
        void onRequestHangedUpDishesFailed();

        /**
         * 叫起成功
         */
        void onCallUpSucceed();

        /**
         * 叫起失败
         */
        void onCallUpFailed();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求挂起、叫起数据
         * @param salesOrderGUID
         * @param checkStatus 0==挂起数据 1==叫起数据
         */
        void requestHangedUpDishes(String salesOrderGUID, Integer checkStatus);

        /**
         * 提交叫起数据
         * @param salesOrderBatchDishesEList
         */
        void submitCallUpDishes(List<SalesOrderBatchDishesE> salesOrderBatchDishesEList);//提交叫起数据
    }
}