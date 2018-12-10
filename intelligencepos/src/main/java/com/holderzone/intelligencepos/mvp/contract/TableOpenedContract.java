package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 已开台未点餐contract
 * Created by tcw on 2017/6/6.
 */

public interface TableOpenedContract {

    interface View extends IView {

        /**
         * 关台 成功
         */
        void onTableClosed();
    }

    interface Presenter extends IPresenter {

        /**
         * 关台操作
         */
        void submitCloseTable(String salesOrderGUID);
    }
}