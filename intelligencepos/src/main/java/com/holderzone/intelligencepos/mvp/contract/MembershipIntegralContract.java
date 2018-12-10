package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

/**
 * Created by chencao on 2018/2/2.
 */

public interface MembershipIntegralContract {
    interface View extends IView {
        /**
         * 返回的可用积分等情况
         *
         * @param salesOrderE
         */
        void responseCanUsePoints(SalesOrderE salesOrderE);

        /**
         * 是否请求成功
         */
        void resopnseUsePoints();

        /**
         * 网络错误
         */
        void showNetworkErrorLayout();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取可以积分和抵扣金额
         *
         * @param salesOrderGUID 账单GUID
         * @param memberInfoGUID 会员GUID
         */
        void requestCanUsePoints(String salesOrderGUID, String memberInfoGUID);

        /**
         * 请求使用积分
         *
         * @param usePoints    该单所用积分
         * @param pointsAmount 该单所用积分所抵扣的金额
         */
        void requestUsePoints(String salesOrderGUID, int usePoints, double pointsAmount);

        /**
         * 取消使用积分
         *
         * @param salesOrderGUID
         */
        void requestCancelUse(String salesOrderGUID);
    }
}
