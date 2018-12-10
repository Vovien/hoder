package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;

import java.util.List;

/**
 * 备注设置contract
 * Created by LiTao on 2017-8-9.
 */

public interface RemarkSettingContract {
    interface View extends IView {
        /**
         * 获取菜品备注成功
         */
        void getDishesRemarkSuccess(List<DishesRemarkE> arrayOfDishesRemarkE);

        /**
         * 新增成功
         */
        void addRemarkSuccess();

        /**
         * 删除成功
         */
        void deleteRemarkSuccess();

        /**
         * 网络错误
         */
        void showNetworkError();

        /**
         * 新增备注失败
         */
        void addDishesRemarkFiled(String msg,String remark);
    }

    interface Presenter extends IPresenter {
        /**
         * 请求菜品备注
         */
        void requestDishesRemark();

        /**
         * 请求新增菜品备注
         */
        void requestAddDishesRemark(String Name);

        /**
         * 请求删除菜品备注
         */
        void requestDeleteDishesRemark(List<DishesRemarkE> arrayOfDishesRemarkE);
    }
}
