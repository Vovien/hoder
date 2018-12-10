package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * Created by terry on 17-11-6.
 */

public interface PaymentMtcCancelContract {

    interface View extends IView {

        /**
         * 美团退券成功
         */
        void MeituanRefundSuccess();

        /**
         * 账单数据获取失败
         */
        void onSalesOrderObtainSucceed(SalesOrderE salesOrderE);

        /**
         * 账单数据获取失败
         */
        void onSalesOrderObtainFailed();
    }

    interface Presenter extends IPresenter {

        /**
         * 美团退券
         */
        void MeituanRefund(String salesOrderGUID, String salesOrderPaymentGUID, String transactionNumber);

        /**
         * 获取订单详情(含支付数据) 付款时出错-4,0，需立即刷新已付项，调用该接口
         */
        void requestSalesOrder(String salesOrderGUID);
    }
}
