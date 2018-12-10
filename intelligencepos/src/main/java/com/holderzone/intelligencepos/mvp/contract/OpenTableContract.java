package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 开台contract
 * Created by chencao on 2017/9/4.
 */

public interface OpenTableContract {

    interface View extends IView {

        /**
         * 仅开台成功
         */
        void onSalesOrderCreated(String salesOrderGUID);

        /**
         * 开台成功并进入点菜界面
         */
        void onSalesOrderCreatedThenOrderDishes(String salesOrderGUID);
    }

    interface Presenter extends IPresenter {

        /**
         * 仅开台
         */
        void submitOpenTable(String diningTableGUID, int guestCount, String orderRecordGUID);

        /**
         * 开台并点餐
         */
        void submitOpenTableThenOrderDishes(String diningTableGUID, int guestCount, String orderRecordGUID);
    }
}