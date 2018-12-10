package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 微信支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentWxPayContract {

    interface View extends IView {

        /**
         * 微信支付成功
         * @param salesOrderE
         */
        void onWxPaySucceed(SalesOrderE salesOrderE);

        /**
         * 微信支付失败
         */
        void onWxPayFailed(String msg);

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
         * 微信付款
         * @param saleOrderGUID
         * @param paymentItemCode
         * @param payableAmount
         * @param transactionNumber
         */
        void submitWxPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String transactionNumber);

        /**
         * 取消订阅
         */
        void disposeWxPay();
    }
}
