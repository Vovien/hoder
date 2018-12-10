package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;

import java.util.List;

/**
 * 选择备注contract
 * Created by LiTao on 2017-8-2.
 */

public interface RemarkContract {
    interface View extends IView {
        /**
         * 获取菜品备注成功
         */
        void getDishesRemarkSuccess(List<DishesRemarkE> arrayOfDishesRemarkE);

        /**
         * 获取备注失败
         */
        void getDishesRemarkFiled();
        /**
         * 新增备注成功
         */
        void onAddDishesRemarkSucceed(String msg, DishesRemarkE dishesRemarkE);

        /**
         * 新增备注失败
         */
        void onAddDishesRemarkFailed();
    }

    interface Presenter extends IPresenter {
        /**
         * 请求菜品备注
         */
        void requestDishesRemark();
        /**
         * 添加特殊要求
         */
        void addDishesRemark(DishesRemarkE dishesRemarkE);
    }
}
