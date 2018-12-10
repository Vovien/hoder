package com.holderzone.intelligencepos.mvp.balancehes.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.RedPackageModel;

import java.util.List;

public interface HesMemberRedPackageContract {
    interface View extends IView {
        /**
         * 获取红包列表成功
         */
        void responseMemberRedEnvelopesListSuccess(List<RedPackageModel> redPackageModels);

        /**
         * 获取红包列表失败
         */
        void responseMemberRedEnvelopesListFail();

        /**
         * 使用红包成功
         */
        void responseUseRedEnvelopesSuccess();

        /**
         * 使用红包成功 但未使用全部红包
         *
         * @param number
         */
        void responseUseRedEnvelopesNotAll(String number);

        /**
         * 使用红包失败
         */
        void responseUseRedEnvelopesFail();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取红包列表
         *
         * @param memberInfoGUID
         */
        void getMemberRedPackageList(String salesOrderGUID, String memberInfoGUID);

        /**
         * 核红包
         */
        void requestUseRedPackage(List<String> codes, String salesOrderGUID, String memberInfoGUID);
    }
}
