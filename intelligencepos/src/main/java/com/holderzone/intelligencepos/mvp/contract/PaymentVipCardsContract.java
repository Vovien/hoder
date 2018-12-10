package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

public interface PaymentVipCardsContract {



    interface View extends IView {


        /**
         * 获取验证码失败
         * @param msg   失败内容
         */
       void  onGetVerCodeFailed(String msg);


        /**
         * 获取验证码成功
         * @param verCode   验证码
         */
       void onGetVerCodeSuccess(String verCode);


        /**
         * 支付成功
         */
        void onPaySuccess(SalesOrderE salesOrderE);

        /**
         * 支付失败
         */
        void onPayFailedOutOfWrongPsd();

    }




    interface Presenter extends IPresenter {

        /**
         * 获取验证码
         * @param regTel
         */
        void requestVerCode(String regTel);
        /**
         * 会员卡 支付
         */
        void submitVipCardPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String cardsChipNo, String memberPassWord);


    }
}
