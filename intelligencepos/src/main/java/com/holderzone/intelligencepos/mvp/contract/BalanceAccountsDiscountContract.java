package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

import java.util.List;

/**
 * 结算-折扣contract
 * Created by zhaoping on 2017/7/20.
 */

public interface BalanceAccountsDiscountContract {

    interface View extends IView {

        /**
         * 获取订单信息成功
         */
        void onGetOrderDisheSuccess(List<OrderDishesGroup> list, SalesOrderE mainOrder);

        /**
         * 网络异常
         */
        void showNetworkErrorLayout();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取订单信息
         */
        void getOrderInfo(String salesOrderGuid);
    }
}
