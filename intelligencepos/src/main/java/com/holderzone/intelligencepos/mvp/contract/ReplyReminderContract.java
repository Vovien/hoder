package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE;

import java.util.List;

/**
 * Created by LT on 2018-04-03.
 */

public interface ReplyReminderContract {
    interface View extends IView {
        void onRequestReminderReplyContentSucceed(List<UnReminderReplyContentE> arrayOfUnReminderReplyContentE);

        void onRequestReminderReplyContentFailed();

        void onSubmitReplyReminderSucceed();

        void onSubmitReplyReminderFailed();

        void onAddNewReminderReplyContentSucceed(UnReminderReplyContentE data);

        void onAddNewReminderReplyContentFailed();
    }

    interface Presenter extends IPresenter {
        /**
         * 提交回复顾客催单
         *
         * @param unOrderGUID                    订单标识
         * @param unOrderReceiveMsgGUID          接收消息标识
         * @param arrayOfUnReminderReplyContentE
         */
        void submitReplyReminder(String unOrderGUID, String unOrderReceiveMsgGUID, List<UnReminderReplyContentE> arrayOfUnReminderReplyContentE);

        /**
         * 新增催单回复常用内容
         *
         * @param unReminderReplyContentE 内容
         */
        void addNewReminderReplyContent(UnReminderReplyContentE unReminderReplyContentE);

        /**
         * 请求获取催单回复常用列表
         */
        void requestReminderReplyContent();
    }
}
