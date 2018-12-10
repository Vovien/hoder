package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 会员卡收款contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentVipCardCollectionContract {

    interface View extends IView {

        /**
         * 支付成功
         */
        void onPaySuccess(SalesOrderE salesOrderE);

        /**
         * 支付失败
         */
        void onPayFailedOutOfWrongPsd();

        /**
         * 获取验证码成功
         */
        void onGetVerCodeSuccess(String verCode);

        /**
         * 获取验证码失败
         */
        void onGetVerCodeFailed(String msg);

        /**
         * 重置密码成功
         */
        void onResetPasswordSuccess();

        /**
         * 重置密码失败
         */
        void onResetPasswordFailed();
    }

    interface Presenter extends IPresenter {

        /**
         * 何师会员支付
         *
         * @param paymentItemCode 支付编码 Hes01
         * @param payableAmount   支付金额
         * @param useMemberBlance 0:会员卡 1：账户余额
         * @param content         卡号 或者会员GUID
         * @param password        密码
         */
        void hesMemberPay(String salesOrderGUID, String paymentItemCode, double payableAmount, int useMemberBlance, String content, String password);
         /**
         * 会员卡 支付
         */
        void submitVipCardPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String cardsChipNo, String memberPassWord);

        /**
         * 获取验证码
         * @param regTel
         */
        void requestVerCode(String regTel);

        /**
         * 重置密码
         * @param regTel
         */
        void submitResetPassword(String regTel, String verCode, String verCodeUID);
    }
}
