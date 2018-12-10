package com.holderzone.intelligencepos.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.blankj.utilcode.util.Utils;
import com.holderzone.intelligencepos.dialog.impl.ForceConfirmDialog;
import com.holderzone.intelligencepos.msg.mqtt.MqttService;
import com.holderzone.intelligencepos.printer.PushRealmMigration;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.BaiduTTS;
import com.holderzone.intelligencepos.utils.print.local.LocalPrinterDelegate;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by tcw on 2017/3/17.
 */

public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";
    /**
     * application静态实例
     */
    private static BaseApplication mBaseApplication;
    private boolean hasLogin = false;

    /**
     * application级别的静态context
     */
    private static Context mApplicationContext;

    /**
     * leakCanary观察器
     */
    private RefWatcher mRefWatcher;

    /**
     * 吐司
     */
    private static Toast mToast;

    /**
     * 订阅管理者
     */
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
        mApplicationContext = this.getApplicationContext();
        Utils.init(this);
        if (AppManager.isApkInDebug(this)) {
            installLeakCanary();//leakCanary内存泄露检查
            initCrashHandler();// CrashHandler初始化
        } else {
            Bugly.init(getApplicationContext(), Config.BUGLY_APP_ID, false);
        }
        initRealm();
        initCloudChannel(this);
        BaiduTTS.getInstance().init(this);// 初始化百度语音TTS
        LocalPrinterDelegate.getInstance();
    }

    /**
     * 安装leakCanary检测内存泄露
     */
    protected void installLeakCanary() {
//        this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Constants.PUSH_REALM_NAME)
                .schemaVersion(Constants.PUSH_REALM_VERSION)
                // 开始迁移
                .migration(new PushRealmMigration())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {
        // 创建notificaiton channel
        this.createNotificationChannel();
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * 初始化崩溃处理器
     */
    protected void initCrashHandler() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
        // 百度语音TTS释放资源
//        BaiduTTS.getInstance().release();
    }

    /**
     * 返回application实例
     *
     * @return
     */
    public static BaseApplication getApplication() {
        return mBaseApplication;
    }

    /**
     * 返回application级别的context
     *
     * @return
     */
    public static Context getContext() {
        return mApplicationContext;
    }

    /**
     * 获得leakCanary观察器
     *
     * @return
     */
    public static RefWatcher getRefWatcher() {
        return mBaseApplication.mRefWatcher;
    }

    /**
     * @param message
     */
    public static void showMessage(String message) {
        new Handler(Looper.getMainLooper()).post((() -> {
            if (mToast == null) {
                mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }));
    }


    /**
     * 开启检服务
     */
    public void startMqttService() {
        Intent intent = new Intent(this, MqttService.class);
        startService(intent);
        hasLogin = true;
    }

    /**
     * 关闭服务
     */
    public void stopMqttService() {
        Intent intent = new Intent(this, MqttService.class);
        stopService(intent);
        hasLogin = false;
    }

    public boolean isHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public static void showForceConfirmDialog(String msg) {
        ForceConfirmDialog forceConfirmDialog = new ForceConfirmDialog(msg);
        forceConfirmDialog.setOnForceConfirmClickListener(() -> {

        });
        forceConfirmDialog.show();
    }
}
