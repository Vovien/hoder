package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 设备激活（非主动激活） 接口定义
 * 判断本地是否存在商家信息
 * 判断一体机设备是否绑定到具体商家
 * 判断一体机设备授权验证是否过期
 * 判断本地是否存在门店信息
 * Created by tcw on 2017/5/26.
 */

public interface VerificationContract {

    interface View extends IView {

        /**
         * 验证完成
         *
         * @param storeExist true进入登陆界面 false进入选择门店界面
         */
        void onVerifySuccess(Boolean storeExist);

        /**
         * 任何一环节验证失败
         *
         * @param msg
         */
        void onVerifyFailed(String msg);

        /**
         * 网路错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 校验 本地是否存在商家信息、一体机设备绑定、一体机设备授权验证、本地是否存在门店信息
         */
        void verify();
    }
}
