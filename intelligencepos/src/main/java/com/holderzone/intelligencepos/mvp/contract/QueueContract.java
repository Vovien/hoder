package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpTypeE;

import java.util.List;

/**
 * 排队 Contract
 */
public interface QueueContract {
    interface View extends IView {
        /**
         * 获取刷新类型、记录数据成功
         *
         * @param queueUpTypeEList
         * @param queueUpRecordEList
         */
        void onGetQueueUpInfoSucceed(List<QueueUpTypeE> queueUpTypeEList, List<QueueUpRecordE> queueUpRecordEList);

        /**
         * 获取刷新类型、记录数据失败
         */
        void onGetQueueUpInfoFailed();

        /**
         * 请求刷新类型、记录数据已取消
         */
        void onQueueUpInfoDisposed();

        /**
         * 叫号成功
         */
        void onCallSuccess();

        /**
         * 叫号失败
         */
        void onCallFailed();

        /**
         * 更改人数成功
         */
        void onUpdateCustomerCountSuccess();

        /**
         * 更改人数失败
         */
        void onUpdateCustomerCountFailed();

        /**
         * 确认就餐成功
         */
        void onConfirmSuccess();

        /**
         * 确认就餐失败
         */
        void onConfirmFailed();

        /**
         * 过号成功
         */
        void onSkipSuccess();

        /**
         * 过号失败
         */
        void onSkipFailed();

        /**
         * 撤销成功
         */
        void onRecoverSuccess();

        /**
         * 撤销失败
         */
        void onRecoverFailed();

        /**
         * 删除成功
         */
        void onDeleteSuccess();

        /**
         * 删除失败
         */
        void onDeleteFailed();

        /**
         * 网络错误
         */
        void onNetWorkError();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取排号类型列表
         */
        void getQueueUpInfo();

        /**
         * 请求排队信息已取消
         */
        void disposeQueueUpInfo();

        /**
         * 叫号
         */
        void submitCall(String queueUpRecordGUID);

        /**
         * 更改人数
         */
        void submitUpdateCustomerCount(String queueUpRecordGUID, int customerCount);

        /**
         * 确认就餐
         */
        void submitConfirm(String queueUpRecordGUID);

        /**
         * 过号
         */
        void submitSkip(String queueUpRecordGUID);

        /**
         * 撤销
         */
        void submitRecover(String queueUpRecordGUID);

        /**
         * 删除
         */
        void submitDelete(String queueUpRecordGUID);
    }
}
