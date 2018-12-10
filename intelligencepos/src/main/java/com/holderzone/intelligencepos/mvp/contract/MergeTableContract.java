package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SubSalesOrder;

import java.util.List;

/**
 * 并单contract
 * Created by chencao on 2017/9/4.
 */

public interface MergeTableContract {

    interface View extends IView {

        /**
         * 刷新可并单的桌台
         */
        void refreshMainGui(List<DiningTableAreaE> arrayOfDiningTableAreaE, List<DiningTableE> arrayOfDiningTableE);

        /**
         * 请求可并单桌台失败
         */
        void onRequestTableCouldBeMergeFailed();

        /**
         * 网络错误
         */
        void onNetworkError();

        /**
         * 换桌成功
         */
        void onMergeTableSucceed();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取可并单的所有桌子，即除了并单和空闲的桌子
         */
        void requestTableCouldBeMerge(String salesOrderGUID);

        /**
         * 提交确认并单请求
         */
        void submitMergeTable(String salesOrderGUID, List<SubSalesOrder> arrayOfSubSalesOrder);
    }
}