package com.holderzone.intelligencepos.mvp.balancehes.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.CashCouponModel;

import java.util.List;

public interface HesMemberCouponContract {
    interface View extends IView {
        /**
         * 核销券成功
         */
        void responseUseCouponSuccess();

        /**
         * 使用优惠券成功 但未使用全部
         *
         * @param number
         */
        void responseUseCouponNotAll(String number);

        /**
         * 核销券失败
         */
        void responseUseCouponFail();

        /**
         * 获取可使用的券列表 成功
         */
        void responseGetCouponListSuccess(List<CashCouponModel> cashCouponModels);


        /**
         * 获取可使用的券列表 失败
         */
        void responseGetCouponListFail();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取优惠券列表
         *
         * @param memberInfoGUID
         */
        void getMemberCouponList(String salesOrderGUID, String memberInfoGUID);

        /**
         * 核销券
         */
        void requestUseMemberCoupon(List<String> codes, String salesOrderGUID, String memberInfoGUID);
    }
}
