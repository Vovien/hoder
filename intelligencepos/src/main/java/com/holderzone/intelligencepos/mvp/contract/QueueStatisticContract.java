package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpReportE;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;

import java.util.List;

/**
 * 排队统计 Contract
 */
public interface QueueStatisticContract {
    interface View extends IView {

        /**
         * 请求本地营业日成功
         */
        void onRequestLocalAccountRecordSucceed(AccountRecord accountRecord);

        /**
         * 获取统计数据成功
         *
         * @param arrayOfQueueUpReportE
         */
        void onGetStatisticalReportSuccess(List<QueueUpReportE> arrayOfQueueUpReportE);

        /**
         * 网络错误
         */
        void onNetworkError();

        /**
         * 获取统计数据失败
         */
        void onGetStatisticalReportFailed();

    }

    interface Presenter extends IPresenter {

        /**
         * 请求本地营业日
         */
        void requestLocalAccountRecord();

        /**
         * 获取当前营业日排队统计报表
         */
        void getStatisticalReport(String businessDay);

    }
}
