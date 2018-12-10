package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.MeituanCoupon;

/**
 * Created by terry on 17-11-6.
 */

public interface PaymentMtcCheckContract {

    interface View extends IView {

        /**
         * 美团验券成功
         */
        void MeituanCheckSuccess(MeituanCoupon meituanCoupon);

        /**
         * 美团验券失败
         */
        void MeituanCheckFailed();
    }

    interface Presenter extends IPresenter {

        /**
         * 美团验券
         */
        void MeituanCheck(String transactionNumber);
    }
}
