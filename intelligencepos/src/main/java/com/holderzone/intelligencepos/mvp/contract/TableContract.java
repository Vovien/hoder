package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;

import java.util.List;

/**
 * 桌台contract
 * Created by tcw on 2017/6/6.
 */
public interface TableContract {

    interface View extends IView {

        /**
         * 根据网络数据刷新主界面 包括桌台区域、桌台状态、桌台
         *
         * @param arrayOfDiningTableAreaE
         * @param arrayOfDiningTableE
         * @param md5Hash16
         */
        void refreshMainGui(List<DiningTableAreaE> arrayOfDiningTableAreaE, List<DiningTableE> arrayOfDiningTableE, String md5Hash16);

        /**
         * 请求主界面数据失败
         */
        void onRequestMainGUIRefreshInfoFailed();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求主界面数据，从网络获取
         */
        void requestMainGUIRefreshInfo(String md5Hash16);

        /**
         * 取消订阅
         */
        void disposeMainGUIRefreshInfo();
    }
}