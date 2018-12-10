package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 支付宝支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentAliPayContract {

    interface View extends IView {

        /**
         * 支付宝支付成功
         * @param salesOrderE
         */
        void onAliPaySucceed(SalesOrderE salesOrderE);

        /**
         * 支付宝支付失败
         */
        void onAliPayFailed(String msg);

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
         * 支付宝付款
         * @param saleOrderGUID
         * @param paymentItemCode
         * @param payableAmount
         * @param transactionNumber
         */
        void submitAliPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String transactionNumber);

        /**
         * 取消订阅
         */
        void disposeAliPay();
    }
}
