package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesRecordViewBean;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesViewBean;

import java.util.List;

/**
 * 已点餐contract
 * Created by chencao on 2017/9/4.
 */

public interface DishesOrderedContract {

    interface View extends IView {

        /**
         * 请求批次数据成功
         *
         * @param recordList
         * @param activeList
         * @param isDesignated
         */
        void onRequestArrayOfSalesOrderBatchSucceed(List<OrderedDishesRecordViewBean> recordList, List<OrderedDishesViewBean> activeList, boolean isDesignated, MemberInfoE memberInfoE);

        /**
         * 请求批次数据失败
         */
        void onRequestArrayOfSalesOrderBatchFailed();

        /**
         * 网络错误
         */
        void onNetworkError();

        /**
         * 请求打印预结单成功
         */
        void onRequestPrintPrepaymentSucceed(String msg);

        /**
         * 获取检测结果
         *
         * @param result
         */
        void getChickBillResult(boolean result);

    }

    interface Presenter extends IPresenter {
        /**
         * 检测当前订单是否还有未上菜
         *
         * @param SalesOrderGUID
         */
        void checkBill(String SalesOrderGUID);

        /**
         * 按消费单的餐桌获取点单记录 SalesOrderBatchB. GetListByTable
         */
        void requestArrayOfSalesOrderBatchE(String salesOrderGUID, String diningTableGUID);

        /**
         * 请求打印预结单
         *
         * @param salesOrderGUID
         */
        void requestPrintPrepayment(String salesOrderGUID);

        /**
         * 获取是否开发会员价
         */
        public void getHasOpenMemberPrice();

    }
}