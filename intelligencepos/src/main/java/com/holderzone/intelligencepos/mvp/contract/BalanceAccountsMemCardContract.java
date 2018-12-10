package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;

import java.util.List;

/**
 * 结算-折扣-会员卡contract
 * Created by Administrator on 2017/3/31.
 */

public interface BalanceAccountsMemCardContract {

    interface View extends IView {

        /**
         * 获取会员卡列表
         * @param list
         */
        void onGetCardListSuccess(List<CardsE> list);

        /**
         * 卡和账单绑定(会员卡权益折扣)
         */
        void onVipCardDiscountSuccess();

        /**
         * 网络异常
         */
        void showNetworkErrorLayout();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取会员卡列表
         */
        void getCardListByMember(String memberInfoGUID);

        /**
         * 卡和账单绑定(会员卡权益折扣)
         */
        void vipCardDiscount(String salesOrderGUID, String cardsChipNo);
    }
}
