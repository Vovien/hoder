package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;

import java.util.List;

/**
 * 换台contract
 * Created by chencao on 2017/9/4.
 */

public interface ChangeTableContract {

    interface View extends IView {

        /**
         * 刷新可换的桌台
         */
        void refreshMainGui(List<DiningTableAreaE> arrayOfDiningTableAreaE, List<DiningTableE> arrayOfDiningTableE);

        /**
         * 请求可换桌桌台失败
         */
        void onRequestTableCouldBeChangeFailed();

        /**
         * 网络错误
         */
        void onNetworkError();

        /**
         * 换桌成功
         */
        void onChangeTableSucceed();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取可换桌的所有桌子，即所有空闲桌子
         */
        void requestTableCouldBeChange();

        /**
         * 提交换桌请求，消费单变更餐桌绑定关系 SalesOrder.ChangeTable
         */
        void submitChangeTable(String salesOrderGUID, String orgDiningTableGUID, String newDiningTableGUID);
    }
}