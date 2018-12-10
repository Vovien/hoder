package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;

import java.util.List;

/**
 * 支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentContract {

    interface View extends IView {

        /**
         * 支付方式获取成功
         * @param arrayOfPaymentItem
         */
        void onPaymentItemObtainSucceed(List<PaymentItem> arrayOfPaymentItem);

        /**
         * 账单数据获取失败
         */
        void onSalesOrderObtainSucceed(SalesOrderE salesOrderE);

        /**
         * 账单数据获取失败
         */
        void onSalesOrderObtainFailed();

        void responseIsHesMember(boolean isHesMember);
    }


    interface Presenter extends IPresenter {

        /**
         * 请求支付方式
         */
        void requestPaymentItem();

        /**
         * 获取订单详情(含支付数据) 付款时出错-4,0，需立即刷新已付项，调用该接口
         */
        void requestSalesOrder(String salesOrderGUID);

        /**
         * 请求是否是何师会员
         */
        void requestIsHesMember();
    }
}
