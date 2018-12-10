package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 支付结账contract
 * Created by tcw on 2017/6/1.
 */

public interface PaymentCheckoutContract {

    interface View extends IView {

        /**
         * 结账成功
         */
        void onCheckOutSucceed(SalesOrderE salesOrderE);

        /**
         * 结账失败
         */
        void onCheckOutFailed();

        /**
         * 结账失败，应付金额与实收金额不一致
         */
        void onCheckOutFailedBecauseOfMoneyNotMatch();

        /**
         * 结账失败，账单已结账
         */
        void onCheckOutFailedBecauseOfCheckoutAlready();

        /**
         * 结账失败 会员积分变动
         */
        void onCheckOutFailedByIntegralChange();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 提交结账请求
         *
         * @param salesOrderGUID
         */
        void submitCheckOut(String salesOrderGUID);
    }
}
