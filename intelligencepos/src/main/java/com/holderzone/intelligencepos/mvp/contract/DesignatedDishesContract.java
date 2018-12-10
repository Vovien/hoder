package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;

import java.util.List;

/**
 * 、
 * 划菜
 * Created by chencao on 2018/1/8.
 */

public interface DesignatedDishesContract {
    interface View extends IView {
        /**
         * 获取 当前桌台划菜情况
         *
         * @param diningTableEList
         */
        void getSalesOrderBatchList(List<DiningTableE> diningTableEList);

        /**
         * 获取失败
         */
        void getSalesOrderBatchListFail();


        /**
         * 整桌更新成功
         */
        void upDataAllSuccess();

        /**
         * 整桌更新失败
         */
        void upDataAllFail();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取 当前桌台划菜情况
         * @param diningTableGUID
         */
        void getSalesOrderBatchList(String diningTableGUID);

        /**
         * 整张桌台划菜
         * @param diningTableGUID
         * @param salesOrderGUID
         */
        void upDataAll(String diningTableGUID, String salesOrderGUID);
    }
}
