package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderAdditionalFeesE;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;

import java.util.List;

/**
 * 结算-附加费contract
 * Created by zhaoping on 2017/4/12.
 */

public interface BalanceAccountsAdditionalFeesContract {

    interface View extends IView {

        /**
         * 获取附加费项目成功
         * @param list
         */
        void onGetAdditionalFeesSuccess(List<AdditionalFees> list);

        /**
         * 获取订单附加费接口返回
         * @param list
         */
        void onGetOrderAdditionalFeesSuccess(List<SalesOrderAdditionalFeesE> list);

        /**
         * 设置附加费成功
         */
        void onSetFeesSuccess();

        /**
         *
         */
        void showNetworkErrorLayout();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取附加费列表
         */
        void getAdditionalFeesList();

        /**
         * 获取订单附加费列表
         */
        void getOrderAdditionalFeesList(String salesOrderGUID);

        /**
         * 账单设置附加费
         */
        void setFees(String salesOrderGUID, List<SalesOrderAdditionalFeesE> list);
    }
}
