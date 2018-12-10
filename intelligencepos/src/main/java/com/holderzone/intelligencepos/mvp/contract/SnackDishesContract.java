package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;

/**
 * 快销点菜页面
 * Created by chencao on 2017/8/2.
 */

public interface SnackDishesContract {

    interface View extends IView {

        /**
         * 获取未结 账单数量
         *
         * @param count
         */
        void onGetListNotCheckOutSuccess(int count);

        void onGetSystemConfigSuccess(ParametersConfig config);
    }

    interface Presenter extends IPresenter {

        /**
         * 获取待结账信息
         */
        void getListNotCheckOut();

        /**
         * 获取是否开启会员价
         */
        void getSystemConfig();
    }
}
