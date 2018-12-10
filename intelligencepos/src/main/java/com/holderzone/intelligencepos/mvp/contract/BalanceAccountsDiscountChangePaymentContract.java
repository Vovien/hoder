package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * 结算-折扣contract
 * Created by zhaoping on 2017/6/1.
 */

public interface BalanceAccountsDiscountChangePaymentContract {

    interface View extends IView {

        /**
         * 整单折扣和改价
         */
        void onChangeOrderPriceSuccess();
    }

    interface Presenter extends IPresenter {

        /**
         * 改价
         * @param salesOrderE
         */
        void setActually(SalesOrderE salesOrderE);
    }
}
