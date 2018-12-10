package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SubSalesOrder;

import java.util.List;

/**
 * 拆单contract
 * Created by tcw on 2017/6/6.
 */
public interface SplitTableContract {
    interface View extends IView {

        /**
         * 请求桌台成功
         *
         * @param arrayOfDiningTableE
         */
        void onRequestTableSucceed(List<DiningTableE> arrayOfDiningTableE);

        /**
         * 请求桌台失败
         */
        void onRequestTableFailed();

        /**
         * 拆单失败
         */
        void onSplitSucceed();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求所有桌台
         */
        void requestTable();

        /**
         * 提交拆单
         *
         * @param arrayOfSubSalesOrder
         */
        void submitSplitTable(List<SubSalesOrder> arrayOfSubSalesOrder);

    }
}