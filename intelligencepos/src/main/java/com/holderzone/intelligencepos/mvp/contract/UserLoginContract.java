package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.StoreE;
import com.holderzone.intelligencepos.mvp.model.bean.UsersE;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.bean.db.Users;

import java.util.List;

/**
 * 用户登录contract
 * Created by tcw on 2017/6/6.
 */

public interface UserLoginContract {

    interface View extends IView {

        /**
         * 门店数据获取成功
         */
        void onRequestLocalRemoteStoreSucceed(Store localStore, List<StoreE> storeEList);

        /**
         * 门店数据获取失败
         */
        void onRequestLocalRemoteStoreFailed();

        /**
         * 网络错误
         */
        void onNetworkError();

        /**
         * 用户登录成功
         */
        void loginSuccess(UsersE usersE);

        /**
         * 用户登录失败
         */
        void loginFail();

        /**
         * 用户登录失败，账户或密码错误
         */
        void loginFailOutOfAccountOrPasswordIncorrect();

        /**
         * 保存已登陆用户信息到本地成功
         */
        void onSaveLoginUsersSucceed();
    }

    interface Presenter extends IPresenter {

        /**
         * 发起数据请求
         */
        void requestLocalAndRemoteStore();

        /**
         * 发起登录请求
         */
        void requestLogin(String Number, String Password);

        /**
         * 保存已登陆用户信息
         */
        void saveLoginUsers(Users users);
    }
}
