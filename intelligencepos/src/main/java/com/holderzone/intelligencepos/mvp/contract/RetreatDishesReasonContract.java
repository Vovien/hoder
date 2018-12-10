package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;

import java.util.List;


/**
 * 退菜原因contract
 * Created by tcw on 2017/6/6.
 */

public interface RetreatDishesReasonContract {
    interface View extends IView {
        /**
         * 获取退菜原因成功
         */
        void getReturnReasonSuccess(List<DishesReturnReasonE> arrayOfDishesReturnReasonE);

        /**
         * 请求退菜原因失败
         */
        void onRequestRetreatReasonsFailed();

        /**
         * 新增退菜原因成功
         *
         * @param msg
         */
        void onAddNewRetreatReasonSucceed(String msg, DishesReturnReasonE dishesReturnReasonE);

        /**
         * 新增退菜原因失败
         */
        void onAddNewRetreatReasonFailed();

        /**
         * 退菜成功
         */
        void onRetreatDishesSucceed();

        /**
         * 退菜失败
         */
        void onRetreatDishesFailed();
    }

    interface Presenter extends IPresenter {
        /**
         * 请求退菜原因
         */
        void requestReturnRemark();

        /**
         * 新增退菜原因
         *
         * @param nameOfRetreatReason
         */
        void addNewRetreatReason(String nameOfRetreatReason);

        /**
         * 提交退菜数据
         *
         * @param salesOrderGUID
         * @param diningTableGUID
         * @param arrayOfSalesOrderBatchDishesE
         * @param arrayOfDishesReturnReasonE
         */
        void submitRetreatDishes(
                String salesOrderGUID, String diningTableGUID,
                List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE,
                List<DishesReturnReasonE> arrayOfDishesReturnReasonE);
    }
}
