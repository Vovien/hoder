package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 银行卡支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentBankCardContract {

    interface View extends IView {

        /**
         * 银行卡支付成功
         *
         * @param salesOrderE
         */
        void onBankCardPaySucceed(SalesOrderE salesOrderE);

        /**
         * 银行卡支付失败
         */
        void onBankCardPayFailed();

        /**
         * 获取是否是何师版本
         * @param isHes
         */
        void getIsHes(boolean isHes);
    }

    interface Presenter extends IPresenter {
        /**
         * 请求获取是否是何师版本
         */
        void requestIsHes();

        /**
         * 提交银行卡支付
         *
         * @param saleOrderGUID
         * @param paymentItemCode
         * @param payableAmount
         * @param actuallyAmount
         * @param transactionNumber
         */
        void submitBankCardPay(String saleOrderGUID, String paymentItemCode, double payableAmount, double actuallyAmount, String transactionNumber);
    }
}
