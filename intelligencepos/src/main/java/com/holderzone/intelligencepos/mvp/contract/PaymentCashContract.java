package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 现金支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentCashContract {

    interface View extends IView {

        /**
         * 现金支付成功
         */
        void onCashPaySucceed(SalesOrderE salesOrderE);

        /**
         * 现金支付失败
         */
        void onCashPayFailed();
    }

    interface Presenter extends IPresenter {

        /**
         * 提交现金支付
         *
         * @param saleOrderGUID
         * @param paymentItemCode
         * @param payableAmount
         * @param receivedMoney
         * @param repayMoney
         * @param actuallyAmount
         */
        void submitCashPay(String saleOrderGUID, String paymentItemCode, double payableAmount, double receivedMoney, double repayMoney, double actuallyAmount);
    }
}
