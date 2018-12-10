package com.holderzone.intelligencepos.mvp.contract;


import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.StoreE;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;

import java.util.List;

/**
 * 选择门店contract
 * Created by tcw on 2017-6-6.
 */

public interface ChooseStoreContract {

    interface View extends IView {

        /**
         * 获取门店数据成功
         */
        void onStoreEListObtainSucceed(List<StoreE> storeEList);

        /**
         * 获取门店数据失败
         */
        void onStoreEListObtainFailed();

        /**
         * 网络错误
         */
        void onNetworkError();

        /**
         * 保存已选择的Store至本地成功 然后返回UserLoginActivity
         */
        void onSaveSelectedStoreSucceedThenReturn(Store store);

        /**
         * 保存已选择的Store至本地成功 然后进入UserLoginActivity
         */
        void onSaveSelectedStoreSucceedThenLaunch(Store store);
    }

    interface Presenter extends IPresenter {

        /**
         * 发起数据请求
         */
        void requestStoreEList();

        /**
         * 保存已选择的Store至本地 然后返回UserLoginActivity
         * @param store
         */
        void saveSelectedStoreThenReturn(Store store);

        /**
         * 保存已选择的Store至本地 然后进入UserLoginActivity
         * @param store
         */
        void saveSelectedStoreThenLaunch(Store store);
    }
}
