package com.holderzone.intelligencepos.base;

import android.content.Intent;

/**
 * Created by tcw on 2017/3/17.
 */

public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 取消订阅
     */
    void onDispose();

    /**
     * 显示一般信息
     */
    void showMessage(String message);

    /**
     * 显示授权过期dialog
     */
    void showAuthorizationDialog(String msg);

    /**
     * 跳转activity
     */
    void launchActivity(Intent intent);

    /**
     * 结束activity
     */
    void finishActivity();
}
