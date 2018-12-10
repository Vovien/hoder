package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;

/**
 * Created by LT on 2018-04-04.
 */

public interface TakeawayBillDetailContract {
    interface View extends IView {
        /**
         * 下单成功
         */
        void onConfirmOrderSuccess();

        /**
         * 仅刷新已付项数据
         */
        void onGetOrderSuccess(UnOrderE order);

        void showNetworkError();

        /**
         * 没有菜品
         */
        void onConfirmOrderFailedInnerNoDish();

        /**
         * 超过估清
         */
        void addDishesEstimateFailed();

        /**
         * 下单失败
         */
        void onConfirmOrderFailed();

        /**
         * 接单成功
         */
        void onSubmitConfirmOrderSucceed();

        /**
         * 接单失败
         */
        void onSubmitConfirmOrderFailed();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取外卖订单详情
         */
        void getOrderDetail(String orderGuid);

        /**
         * 下单
         *
         * @param order
         */
        void confirmOrder(UnOrderE order);

        /**
         * 接单
         */
        void submitConfirmOrder(String unOrderGUID, String unOrderReceiveMsgGUID, Integer isSalesOrder);
    }
}
