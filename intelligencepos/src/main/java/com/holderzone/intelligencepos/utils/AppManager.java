package com.holderzone.intelligencepos.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.activity.UserLoginActivity;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;

import java.util.Stack;

/**
 * @author zhaokaiqiang
 * @ClassName: com.qust.myutils.AppManager
 * @Description: Activity管理类：用于管理Activity和退出程序
 * @date 2014-11-20 下午4:53:33
 */
public class AppManager {

    // Activity栈
    private static Stack<Activity> activityStack;
    // 单例模式
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    public void finishUntil(Class cls) {
        while (activityStack.size() > 0) {
            Activity activity = activityStack.lastElement();
            if (cls != null && activity.getClass().equals(cls)) {
                return;
            }
            finishActivity(activity);
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 清除临时数据，然后退出应用程序
     */
    public void AppExitWithDataClear(Context context) {
        RepositoryImpl.getInstance().releaseForAppExit()
                .subscribe(aBoolean -> AppExit(context));
    }

    /**
     * 关闭服务、清除临时数据，然后退出应用程序
     */
    public void AppExitWithServiceShutDownAndDataClear(Context context) {
        // 停止营业日服务
        BaseApplication.getApplication().stopMqttService();
        // 清除数据
        AppExitWithDataClear(context);
    }

    /**
     * 清除临时数据，然后进入登陆界面
     */
    public void AppReLoginWithDataClear(Context context) {
        RepositoryImpl.getInstance().releaseForReLogin()
                .subscribe(aBoolean -> {
                    Intent intent = UserLoginActivity.newIntent(context);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
    }

    /**
     * 关闭服务、清除临时数据，然后进入登陆界面
     */
    public void AppReLoginWithServiceShutDownAndDataClear(Context context) {
        // 停止营业日服务
        BaseApplication.getApplication().stopMqttService();
        // 清除数据
        AppReLoginWithDataClear(context);
    }
}