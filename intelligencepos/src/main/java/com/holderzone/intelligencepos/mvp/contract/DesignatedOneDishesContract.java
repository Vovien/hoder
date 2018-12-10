package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderServingDishesE;

import java.util.List;

/**
 * Created by chencao on 2018/1/10.
 */

public interface DesignatedOneDishesContract {
    interface View extends IView {
        /**
         * 更新成功
         */
        void updataSalesOrderBatchSuccess();

        /**
         * 更新失败
         */
        void updataSalesOrderBatchFail();
    }
    interface Presenter extends IPresenter {
        /**
         * 更新 当前桌台划菜情况
         */
        void updataSalesOrderBatchList(List<SalesOrderServingDishesE> salesOrderServingDishesEList);

    }
}
