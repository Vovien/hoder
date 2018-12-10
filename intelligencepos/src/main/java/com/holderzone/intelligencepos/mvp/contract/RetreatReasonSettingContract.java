package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;

import java.util.List;

/**
 * Created by LiTao on 2017-9-7.
 */

public interface RetreatReasonSettingContract {
    interface View extends IView {

        /**
         * 请求退菜原因成功
         *
         * @param arrayOfDishesReturnReasonE
         */
        void onRequestRetreatReasonsSucceed(List<DishesReturnReasonE> arrayOfDishesReturnReasonE);

        /**
         * 请求退菜原因失败
         */
        void onRequestRetreatReasonsFailed();

        /**
         * 新增退菜原因成功
         *
         * @param msg
         */
        void onAddNewRetreatReasonSucceed(String msg, DishesReturnReasonE dishesReturnReasonE);

        /**
         * 新增退菜原因失败
         */
        void onAddNewRetreatReasonFailed(String retreatName);

        /**
         * 删除退菜原因成功
         */
        void onDeleteRetreatReasonSucceed();

        /**
         * 删除退菜原因失败
         */
        void onDeleteRetreatReasonFailed();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求所有可选退菜原因
         */
        void requestRetreatReasons();

        /**
         * 新增退菜原因
         *
         * @param nameOfRetreatReason
         */
        void addNewRetreatReason(String nameOfRetreatReason);

        /**
         * 删除退菜原因
         *
         * @param arrayOfDishesReturnReasonE
         */
        void deleteRetreatReason(List<DishesReturnReasonE> arrayOfDishesReturnReasonE);
    }
}
