package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 已并单contract
 * Created by tcw on 2017/6/6.
 */

public interface TableMergedContract {

    interface View extends IView {

        /**
         * 请求打印预结单成功
         */
        void onRequestPrintPrepaymentSucceed(String msg);
    }

    interface Presenter extends IPresenter {

        /**
         * 请求打印预结单
         *
         * @param salesOrderGUID
         */
        void requestPrintPrepayment(String salesOrderGUID);
    }
}