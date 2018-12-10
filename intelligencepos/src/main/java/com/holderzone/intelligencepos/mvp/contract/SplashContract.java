package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;


/**
 * 缓存页contract
 * Created by tcw on 2017/6/6.
 */

public interface SplashContract {

    interface View extends IView {

        void refreshUI(int progress);

        void showErrorLayout();

        void launchHomeActivity();
    }

    interface Presenter extends IPresenter {

        void requestData();
    }
}
