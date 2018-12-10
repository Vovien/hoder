package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

public interface VercodeUidContract  {


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
         * 重置密码成功
         */
        void onResetPasswordSuccess();
    }


    interface Presenter extends IPresenter {

        /**
         * 获取验证码
         * @param regTel
         */
        void requestVerCode(String regTel);


        /**
         * 重置密码
         */
       void submitResetPassword(String mRegTel, String verCode, String verCodeUID);

    }
}
