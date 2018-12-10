package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 支付项管理contract
 * Created by tcw on 2017/6/1.
 */

public interface PaymentManageContract {

    interface View extends IView {

        /**
         * 订单信息获取成功
         */
        void onSalesOrderObtainSucceed(SalesOrderE salesOrderE);

        /**
         * 订单信息获取失败
         */
        void onSalesOrderObtainFailed(String msg);

        /**
         * 退款成功
         */
        void onRefundSucceed(SalesOrderE salesOrderE);

        /**
         * 退款失败
         */
        void onRefundFailed(String msg);

        /**
         * 显示网络错误布局
         */
        void showNetworkErrorLayout();

        /**
         * 显示轮询对话框
         */
        void showIntervalDialog();

        /**
         * 隐藏轮询对话框
         */
        void hideIntervalDialog();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求订单信息 包含付款项集合
         */
        void requestSalesOrder(String salesOrderGUID);

        /**
         * 提交退款请求
         *
         * @param saleOrderGUID
         * @param salesOrderPaymentGuid
         * @param type                  支付方式标记
         * @param traceNumber           通联支付传入 交易参考号
         */
        void submitRefund(String saleOrderGUID, String salesOrderPaymentGuid, String type, String traceNumber);
        /**
         * 取消订阅
         */
        void disposeWxPay();
    }
}
