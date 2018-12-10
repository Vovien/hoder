package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 聚合支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentJhPayContract {

    interface View extends IView {

        /**
         * 聚合支付成功
         * @param salesOrderE
         */
        void onJhPaySucceed(SalesOrderE salesOrderE);

        /**
         * 聚合支付失败
         */
        void onJhPayFailed(String msg);

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
         * 聚合付款
         * @param saleOrderGUID
         * @param paymentItemCode
         * @param payableAmount
         * @param transactionNumber
         */
        void submitJhPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String transactionNumber);

        /**
         * 取消订阅
         */
        void disposeAliPay();
    }
}
