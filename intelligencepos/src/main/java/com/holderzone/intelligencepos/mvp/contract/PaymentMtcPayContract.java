package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.MeituanCoupon;

import java.util.List;

/**
 * Created by terry on 17-11-6.
 */

public interface PaymentMtcPayContract {

    interface View extends IView {

        /**
         * 美团用券成功
         */
        void MeituanPaySuccess(List<MeituanCoupon> arrayOfMeituanCoupon);

        /**
         * 美团用券失败
         */
        void MeituanPayFailed();
    }

    interface Presenter extends IPresenter {

        /**
         * 美团用券
         */
        void MeituanPay(String salesOrderGUID, String transactionNumber, int useCount, String paymentItemCode);
    }
}
