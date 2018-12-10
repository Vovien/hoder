package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 结算-折扣-整单折扣contract
 * Created by zhaoping on 2017/6/1.
 */

public interface BalanceAccountsDiscountWholeOrderContract {

    interface View extends IView {

        /**
         * 整单折扣
         */
        void onDiscountRatioSuccess();
    }

    interface Presenter extends IPresenter {

        /**
         * 设置整单折扣
         * @param salesOrderGuid
         * @param discount
         */
        void setDiscountRatio(String salesOrderGuid,double discount);
    }
}
