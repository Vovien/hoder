package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

import java.util.List;

/**
 * 结算contract
 * Created by zhaoping on 2017/5/27.
 */

public interface BalanceAccountsContract {

    interface View extends IView {

        /**
         * 获取订单信息成功
         */
        void onGetOrderDisheSuccess(boolean isAutoCalc, List<OrderDishesGroup> list, List<SalesOrderE> salesOrderEList);

        /**
         * 会员退出登录
         */
        void onMemberLoginOutSuccess();

        /**
         * 网络异常
         */
        void showNetworkErrorLayout();

        /**
         * 作废账单成功
         */
        void invalidSuccess();

        /**
         * 作废账单成功
         */
        void invalidFiled(int code);

        /**
         * 预结单打印成功
         */
        void onCheckPrintSuccess();

        /**
         * 获取是否为何师系统
         *
         * @param hesVersion
         */
        void onGetHesMember(Boolean hesVersion);

    }

    interface Presenter extends IPresenter {

        /**
         * 获取订单信息
         */
        void getOrderInfo(String salesOrderGuid);

        /**
         * 会员退出登录
         */
        void memberLoginOut(String salesOrderGuid);

        /**
         * 请求作废账单
         */
        void requestInvalid(String salesOrderGuid);

        /**
         * 打印预结单
         */
        void checkPrint(String salesOrderGuid);

        /**
         * 获取是否为何师系统
         */
        void getHesMember();
    }
}
