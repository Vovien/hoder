package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.HomeItemBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;

import java.util.List;

/**
 * 主页contract
 * Created by tcw on 2017/5/26.
 */

public interface HomeContract {

    interface View extends IView {

        /**
         * 获取营业日成功
         *
         * @param accountRecord
         */
        void onAccountRecordGetSuccess(AccountRecord accountRecord);

        /**
         * 获取营业日失败
         */
        void onAccountRecordGetFailed();

        /**
         * 保存营业日成功
         */
        void onAccountRecordSaveSuccess(AccountRecord accountRecord);

        /**
         * 获取本地门店成功
         *
         * @param store
         */
        void onLocalStoreGetSucceed(Store store);

        /**
         * 刷新主界面
         */
        void refreshMainGui(List<HomeItemBean> itemList);

        /**
         * 请求主界面数据已取消
         */
        void onMainGUIRefreshInfoDisposed();

        /**
         * 获取是否为何师系统
         * @param hesVersion
         */
        void onGetHesVersion(Boolean hesVersion);
    }

    interface Presenter extends IPresenter {

        /**
         * 获取营业日
         */
        void getAccountRecord();

        /**
         * 保存营业日
         */
        void saveAccountRecord(AccountRecord accountRecord);

        /**
         * 请求本地门店数据
         */
        void getLocalStore();

        /**
         * 请求主页面各账单数据
         */
        void requestMainGUIRefreshInfo();

        /**
         * 取消订阅
         */
        void disposeMainGUIRefreshInfo();

        /**
         * 获取是否为何师系统
         */
        void getHesVersion();
    }
}
