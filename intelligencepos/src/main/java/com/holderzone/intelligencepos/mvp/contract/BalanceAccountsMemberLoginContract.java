package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;

/**
 * 结算-折扣-会员登陆contract
 * Created by zhaoping on 2017/6/1.
 */

public interface BalanceAccountsMemberLoginContract {

    interface View extends IView {

        /**
         * 会员登录
         */
        void onMemberLoginSuccess(MemberInfoE memberInfoE);

        /**
         * 会员登录失败  已登陆其他账单
         */
        void onMemberLoginFailByLoginOther();

        /***
         * 会员登录失败  会员未注册
         */
        void onMemberLoginFailByNoRegister();

        /**
         * 会员查询成功
         */
        void onQueryMemberSuccess(MemberInfoE memberInfoE);
    }

    interface Presenter extends IPresenter {

        /**
         * 会员登录
         */
        void memberLogin(String salesOrderGuid, String tel);

        /**
         * 查询会员
         */
        void requestMemberInfo(String tel);
    }
}
