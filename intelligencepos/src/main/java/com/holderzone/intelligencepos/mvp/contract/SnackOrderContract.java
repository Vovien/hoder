package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

import java.util.List;

/**
 * 未结账快销订单contract
 * Created by LiTao on 2017-8-3.
 */

public interface SnackOrderContract {

    interface View extends IView {

        /**
         * 获取未结账单记录成功
         */
        void onGetOrderListSuccess(List<SalesOrderE> list);

        /**
         * 获取未结账单记录失败
         */
        void onGetOrderListFiled();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取未结账单记录
         */
        void getListNotCheckOut();
    }
}
