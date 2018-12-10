package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderDaySummaryE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE;

import java.util.List;

/**
 * Created by LT on 2018-04-02.
 */

public interface TakeOutContract {
    interface View extends IView {
        void onRequestUnOrderAndPlatformStatisticSucceed(List<UnOrderE> arrayOfUnOrderE);

        void onRequestUnOrderAndPlatformStatisticFailed();

        void onRequestUnOrderAndPlatformStatisticDisposed();

        void onSubmitConfirmOrderSucceed();

        void onSubmitConfirmOrderFailed();

        void onSubmitConfirmOrderThenEnterDetailsSucceed();

        void onSubmitConfirmOrderThenEnterDetailsFailed();

        void onSubmitRejectOrderSucceed();

        void onSubmitRejectOrderFailed();

        void onSubmitConfirmChargeBackSucceed();

        void onSubmitConfirmChargeBackFailed();

        void onSubmitRefuseChargeBackSucceed();

        void onSubmitRefuseChargeBackFailed();

        /**
         * 下单成功
         */
        void onConfirmOrderSuccess();

        /**
         * 下单失败 超过估清
         */
        void addDishesEstimateFailed();

        /**
         * 下单失败 没有这个菜品
         */
        void onConfirmOrderFailedInnerNoDish();

        /**
         * 下单失败
         */
        void onConfirmOrderFailed();

        /**
         * 确认退菜
         */
        void onConfirmReturnDishes();
    }

    interface Presenter extends IPresenter {
        /**
         * 请求订单和平台统计数据
         */
        void requestUnOrderAndPlatformStatistic();

        /**
         * 取消请求订单和平台统计数据
         */
        void disposeUnOrderAndPlatformStatistic();

        /**
         * 提交接单
         *
         * @param unOrderGUID           订单标识
         * @param unOrderReceiveMsgGUID 接收消息标识
         */
        void submitConfirmOrder(String unOrderGUID, String unOrderReceiveMsgGUID, Integer isSalesOrder);

        /**
         * 提交接单并进入订单详情界面
         *
         * @param unOrderGUID           订单标识
         * @param unOrderReceiveMsgGUID 接收消息标识
         */
        void submitConfirmOrderThenEnterDetails(String unOrderGUID, String unOrderReceiveMsgGUID, Integer isSalesOrder);

        /**
         * 提交拒单
         *
         * @param unOrderGUID           订单标识
         * @param unOrderReceiveMsgGUID 接收消息标识
         */
        void submitRejectOrder(String unOrderGUID, String unOrderReceiveMsgGUID);

        /**
         * 提交确认顾客退单
         *
         * @param unOrderGUID           订单标识
         * @param unOrderReceiveMsgGUID 接收消息标识
         */
        void submitConfirmChargeBack(String unOrderGUID, String unOrderReceiveMsgGUID, boolean isCheck);

        /**
         * 提交拒绝顾客退单
         *
         * @param unOrderGUID           订单标识
         * @param unOrderReceiveMsgGUID 接收消息标识
         */
        void submitRefuseChargeBack(String unOrderGUID, String unOrderReceiveMsgGUID);

        /**
         * 下单
         *
         * @param order
         */
        void confirmOrder(UnOrderE order);
    }
}
