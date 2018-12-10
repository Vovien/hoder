package com.holderzone.intelligencepos.mvp.balancehes.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.VipCardModel;

import java.util.List;

/**
 * 何师会员卡页面
 */
public interface HesMemberCardContract {
    interface View extends IView {
        /**
         * 获取会员卡列表成功
         *
         * @param vipCardModels
         */
        void getMemberCardListSuccess(boolean memberSelected,List<VipCardModel> vipCardModels);

        /**
         * 获取会员卡列表失败
         */
        void getMemberCardListFail();

        /**
         * 核销券失败
         */
        void responseUseMemberCardFail();
        /**
         * 使用会员卡成功 但未使用全部
         */
        void responseUseMemberCardNotAll(String number);

        /**
         * 请求使用 会员/卡 折扣成功
         */
        void responseUseMemberCardSuccess();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取会员卡列表
         *
         * @param memberInfoGUID
         */
        void getMemberCardList(String salesOrderGUID, String memberInfoGUID);

        /**
         * 核销券
         */
        void requestUseMemberCard(List<String> codes, String salesOrderGUID, String memberInfoGUID);
    }
}
