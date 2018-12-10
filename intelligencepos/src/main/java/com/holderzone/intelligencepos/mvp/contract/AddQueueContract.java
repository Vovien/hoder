package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;

/**
 * 新增排队
 */
public interface AddQueueContract {
    interface View extends IView {
        /**
         * 新排队成功
         */
        void onAddSuccess();

        /**
         * 新排队失败
         */
        void onAddFailed();
    }
    interface Presenter extends IPresenter {
        /**
         * 新排队
         */
        void submitAdd(int customerCount, String customerTel);
    }
}
