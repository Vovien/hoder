package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 结算-商品合计contract
 * Created by zhaoping on 2017/6/3.
 */

public interface BalanceAccountsAllDishesContract {

    interface View extends IView {

        /**
         * 打印预结单
         */
        void onCheckPrintSuccess();

        /**
         * 获取是否开启会员价
         * @param hasOpen
         */
        void onGetHasOpenMemberPrice(Boolean hasOpen);
    }

    interface Presenter extends IPresenter {

        /**
         * 打印预结单
         */
        void checkPrint(String salesOrderGuid);

        /**
         * 获取是否开发会员价
         */
        public void getHasOpenMemberPrice();
    }
}
