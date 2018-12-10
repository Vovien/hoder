package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.MeituanCoupon;

import java.util.List;

/**
 * Created by terry on 17-11-6.
 */

public interface PaymentMtcRefundContract {

    interface View extends IView {

        /**
         * 查询美团用券成功
         */
        void QueryMeituanCouponSuccess(List<MeituanCoupon> arrayOfMeituanCoupon);

        /**
         * 美团退券成功
         */
        void MeituanRefundSuccess();
    }

    interface Presenter extends IPresenter {

        /**
         * 查询美团券
         */
        void QueryMeituanCoupon(String salesOrderPaymentGUID);

        /**
         * 美团退券
         */
        void MeituanRefund(String salesOrderGUID, String salesOrderPaymentGUID, String transactionNumber);
    }
}
