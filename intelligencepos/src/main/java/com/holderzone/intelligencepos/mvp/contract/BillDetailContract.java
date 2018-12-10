package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

import java.util.List;

/**
 * 账单详情contract
 * Created by chencao on 2017/6/5.
 */

public interface BillDetailContract {

    interface View extends IView {

        /**
         * 错误信息
         * @param message
         */
        void getError(String message);

        /**
         * 获取账单具体信息
         * @param salesOrderEList
         */
        void getResponsSalesOrderEInfomation(List<SalesOrderE> salesOrderEList);

        /**
         * 打印成功时调用
         * @param message
         */
        void getResponsByReturnCount(String message);

        /**
         * 获取打印营业日账单
         * @param message
         */
        void getResopnsPrint(String message);

        /**
         * 网络链接失败
         */
        void showNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 按账单标识获取账单信息
         * @param salesOrderE
         */
        void setRequestBillInfomation(SalesOrderE salesOrderE);

        /**
         * 账单结账后还原到为结账状态重新结账  反结账
         * @param salesOrderE
         */
        void setRequestCounterCheck(SalesOrderE salesOrderE);

        /**
         * 打印算账单或结账单
         * @param salesOrderE
         */
        void setRequestPrint(SalesOrderE salesOrderE);
    }
}
