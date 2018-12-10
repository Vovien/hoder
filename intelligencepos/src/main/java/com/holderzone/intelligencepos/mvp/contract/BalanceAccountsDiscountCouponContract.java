package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.CouponsE;

import java.util.List;

/**
 * 结算-折扣-优惠券contract
 * Created by zhaoping on 2017/4/12.
 */

public interface BalanceAccountsDiscountCouponContract {

    interface View extends IView {

        /**
         * 验券成功
         */
        void onValidateCouponSuccess(CouponsE list);

        /**
         * 获取订单附加费接口返回
         */
        void onGetCouponListByMemberSuccess(List<CouponsE> list);

        /**
         * 用券成功
         */
        void onUseCouponsSuccess();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 验券
         */
        void validateCoupon(String couponsNumber, String salesOrderGuid);

        /**
         * 获取会员账户拥有的电子券
         */
        void getListByMember(String memberInfoGUID, String salesOrderGuid);

        /**
         * 用券
         */
        void useCoupons(List<CouponsE> list);
    }
}
