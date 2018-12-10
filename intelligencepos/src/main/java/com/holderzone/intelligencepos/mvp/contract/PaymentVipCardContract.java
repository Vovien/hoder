package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;

import java.util.List;

/**
 * 会员卡支付contract
 * Created by tcw on 2017/5/31.
 */

public interface PaymentVipCardContract {

    interface View extends IView {

        /**
         * 登陆并请求该账户所拥有的会员卡成功
         */
        void onLoginAndGetCardsSuccess(List<CardsE> arrayOfCardsE);

        /**
         * 登陆并请求该账户所拥有的会员卡失败
         */
        void onLoginAndGetCardsFailed();

        /**
         * 网络错误
         */
        void onNetworkError();


        /**
         * 请求使用卡支付折扣详情成功
         */
        void readDiscountDetailsFailed();

        /**
         * 请求使用卡支付折扣详情失败
         */
        void readDiscountDetailsSuccess(CardsE cardsE);
    }

    interface Presenter extends IPresenter {

        /**
         * 登陆并请求该账户所拥有的会员卡
         *
         * @param regTel
         */
        void requestLoginAndGetCardsByMember(String salesOrderGUID, String regTel, String memberInfoGUID);

        /**
         * 请求会员卡折扣详情
         * @param careE
         */
        void requestCardDiscountDetails(CardsE careE);
    }
}
