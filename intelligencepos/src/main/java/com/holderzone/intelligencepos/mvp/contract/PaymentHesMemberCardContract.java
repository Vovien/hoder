package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.VipCardModel;

import java.util.List;

public interface PaymentHesMemberCardContract {
    interface View extends IView {
        /**
         * 获取会员卡列表 成功
         *
         * @param vipCardModels
         */
        void responseMemberCardListSuccess(List<VipCardModel> vipCardModels);

        /**
         * 获取会员卡列表 失败
         */
        void responseMemberCardListFail();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取会员卡列表
         *
         * @param salesOrderGUID
         * @param memberInfoGUID
         */
        void requestMemberCardList(String salesOrderGUID, String memberInfoGUID);
    }
}
