package com.holderzone.intelligencepos.mvp.contract;


import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SingleDishesDiscount;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface SingleDishesDiscountContract {

    interface View extends IView {

        void onGetOrderInfoSuccess(List<SingleDishesDiscount> list, SalesOrderE salesOrderE, int orderCount);

        void onReviewDishesSuccess();

        void showNetworkError();

        /**
         * 是否启动会员价
         *
         * @param hesVersion
         */
        void onGetIsMemberPrice(Boolean hesVersion);
    }

    interface Presenter extends IPresenter {
        /**
         * 获取订单信息
         */
        void getOrderInfo(String salesOrderGuid);

        /**
         * 确认结算数量
         */
        void setPrice(List<SalesOrderBatchDishesE> list);

        /**
         * 是否启用会员价会员价
         */
        void getIsMemberPrice();
    }

}
