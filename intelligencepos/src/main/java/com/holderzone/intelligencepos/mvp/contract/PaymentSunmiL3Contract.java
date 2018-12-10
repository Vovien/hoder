package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * Created by Administrator on 2017-10-30.
 * L3支付
 */

public interface PaymentSunmiL3Contract {
    interface View extends IView {

        /**
         * L3支付成功
         */
        void onSunmiL3Succeed(SalesOrderE salesOrderE);

        /**
         * L3支付失败
         *
         * @param code 失败状态 0：账单已结账 1：超额支付 2:其他异常
         */
        void onSunmiL3Failed(int code);
    }

    interface Presenter extends IPresenter {

        /**
         * 提交L3支付
         *
         * @param saleOrderGUID
         * @param paymentItemCode
         * @param payableAmount
         * @param actuallyAmount
         */
        void sunmiL3Pay(String saleOrderGUID, String paymentItemCode, double payableAmount, double actuallyAmount, String reTraceNo, String refNumber);
    }
}
