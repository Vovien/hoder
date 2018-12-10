package com.holderzone.intelligencepos.mvp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.DialogFactory;
import com.holderzone.intelligencepos.mvp.contract.VerificationContract;
import com.holderzone.intelligencepos.mvp.presenter.VerificationPresenter;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;

/**
 * 校验App本地是否存在企业实体
 * 校验一体机是否绑定到某个企业
 * 校验一体机是否授权、授权是否过期
 * 校验App本地是否存在门店实体
 * Created by tcw on 2017/5/26.
 */

public class VerificationActivity extends BaseActivity<VerificationContract.Presenter> implements VerificationContract.View {

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_verification;
    }

    @Override
    protected VerificationContract.Presenter initPresenter() {
        return new VerificationPresenter(this);
    }

    @Override
    protected void initView(Bundle extras) {
        StatusBarUtil.setStatusBarColorM(this,R.color.layout_bg_common);
        DeviceHelper.getInstance().restoreLoginTime();
    }

    @Override
    protected void initData(Bundle extras) {
        // 校验 企业、绑定、授权、门店等信息
        mPresenter.verify();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onVerifySuccess(Boolean storeExist) {
        if (storeExist) {// 进入LoginActivity之登陆页面
            launchActivity(UserLoginActivity.newIntent(VerificationActivity.this));
        } else {// 进入RegisterActivity
            launchActivity(RegisterActivity.newIntent(VerificationActivity.this, true));
        }
        finishActivity();
    }

    @Override
    public void onVerifyFailed(String msg) {
        // 进入RegisterActivity 以 提示校验失败
        launchActivity(RegisterActivity.newIntent(VerificationActivity.this));
        finishActivity();
    }

    @Override
    public void onNetworkError() {
        DialogFactory.showForceLogoutDialog(BaseApplication.getContext(), "网络异常，请退出软件。");
    }
}
