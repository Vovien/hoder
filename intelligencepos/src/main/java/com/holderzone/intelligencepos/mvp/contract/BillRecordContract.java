package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;

import java.util.List;

/**
 * 账单记录contract
 * Created by chencao on 2017/6/5.
 */

public interface BillRecordContract {

    interface View extends IView {

        /**
         * 请求本地营业日成功
         * @param accountRecord
         */
        void onRequestLocalAccountRecordSucceed(AccountRecord accountRecord);

        /**
         * 获取账单列表
         */
        void getBillRecodeList(List<SalesOrderE> arrayOfSalesOrderE,int pageRowCount);

        /**
         * 网络异常
         */
        void showNetworkError();

        /**
         * 显示错误信息
         * @param message
         */
        void getError(String message);
    }

     interface Presenter extends IPresenter {

         /**
          * 请求本地营业日
          */
         void requestLocalAccountRecord();

        /**
         * 请求账单列表
         */
        void setRequestBillRecodeList(int currentPage);
    }
}
