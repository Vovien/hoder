package com.holderzone.intelligencepos.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.DialogFactory;
import com.holderzone.intelligencepos.mvp.activity.TakeOutActivity;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.AccountRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.UnMessageE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tcw on 2017/5/16.
 */

public class AccountRecordService extends Service {
    public static final String ACTION_START_SERVICE = "com.holderzone.intelligencepos.startservice";
    public static final String ACTION_CHECK_IMMEDIATELY = "com.holderzone.intelligencepos.checkimmediately";
    public static final String ACTION_RESTART_NEW_TAKE_OUT_ORDER = "com.holderzone.intelligencestore.restart_new_take_out_order";

    /**
     * 轮询营业日的handler
     */
    private Handler mHandler;

    /**
     * 订阅管理者
     */
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * 状态消息Disposable
     */
    private Disposable mStateMsgDisposable;
    /**
     * 外卖单新消息Disposable
     */
    private Disposable mNewMessageDisposable;
    /**
     * 确认已接收外卖单新消息Disposable
     */
    private Disposable mConfirmNewMessageDisposable;

    /**
     * 消息what字段
     */
    private static final int MSG_ACCOUNT_RECORD = 0x01;
    private static final int MSG_TAKE_OUT_ORDER = 0x03;

    /**
     * 轮播时间间隔
     */
    private long mIntervalTime = 5000;

    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.Builder mBuilderNoNavi;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化handler
        initHandler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_START_SERVICE.equalsIgnoreCase(action)) {
            // 建立新的订阅 营业日
            verifyAccountRecord();
            // 建立新的订阅 外卖单未处理消息
            requestNewMessage4TakeOutOrder();
        } else if (ACTION_CHECK_IMMEDIATELY.equalsIgnoreCase(action)) {
            // 移除消息队列
            mHandler.removeMessages(MSG_ACCOUNT_RECORD);
            // 解除旧的订阅
            disposeAccountRecord();
            // 建立新的订阅
            verifyAccountRecord();
        } else if (ACTION_RESTART_NEW_TAKE_OUT_ORDER.equalsIgnoreCase(action)) {
            // 移除消息队列 外卖单未处理消息
            mHandler.removeMessages(MSG_TAKE_OUT_ORDER);
            // 解除旧的订阅 外卖单未处理消息
            disposeRequestNewMessage4TakeOutOrder();
            // 建立新的订阅 外卖单未处理消息
            requestNewMessage4TakeOutOrder();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 移除消息队列 营业日
        mHandler.removeMessages(MSG_ACCOUNT_RECORD);
        // 解除旧的订阅 营业日
        disposeAccountRecord();
        // 移除消息队列 外卖单未处理消息
        mHandler.removeMessages(MSG_TAKE_OUT_ORDER);
        // 解除旧的订阅 外卖单未处理消息
        disposeRequestNewMessage4TakeOutOrder();
        // 解除旧的订阅 确认外卖单未处理消息
        disposeConfirmNewMessage4TakeOutOrder();
    }

    /**
     * 初始化handler
     */
    private void initHandler() {
        HandlerThread handlerThread = new HandlerThread("AccountRecordService");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_ACCOUNT_RECORD) {
                    // 解除旧的订阅 营业日
                    disposeAccountRecord();
                    // 建立新的订阅 营业日
                    verifyAccountRecord();
                } else if (msg.what == MSG_TAKE_OUT_ORDER) {
                    // 解除旧的订阅 外卖单未处理消息
                    disposeRequestNewMessage4TakeOutOrder();
                    // 建立新的订阅 外卖单未处理消息
                    requestNewMessage4TakeOutOrder();
                }
            }
        };
    }

    /**
     * 建立新的订阅，请求营业日数据
     */
    private void verifyAccountRecord() {
        // 建立新的订阅
        XmlData.Builder()
                .setRequestMethod(RequestMethod.AccountRecordB.GetCurrent)
                .setRequestBody(new AccountRecordE()).buildRESTful()
                .flatMap(RepositoryImpl.getInstance()::getXmlData)
                .flatMap(ApiNoteHelper::checkAccountRecordByRESTful)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer match) {
                        if (-2 == match) {// 弹出对话框
                            DialogFactory.showForceLogoutDialogWithServiceShutDownResultDataClear(AccountRecordService.this, "终端不存在或失效。");
                            disposeRequestNewMessage4TakeOutOrder();
                            mHandler.removeMessages(MSG_TAKE_OUT_ORDER);
                        } else if (-1 == match) {// 弹出对话框
                            DialogFactory.showForceLogoutDialogWithServiceShutDownResultDataClear(AccountRecordService.this, "营业日异常，请退出程序。");
                            disposeRequestNewMessage4TakeOutOrder();
                            mHandler.removeMessages(MSG_TAKE_OUT_ORDER);
                        } else {
                            mHandler.sendEmptyMessageDelayed(MSG_ACCOUNT_RECORD, mIntervalTime);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHandler.sendEmptyMessageDelayed(MSG_ACCOUNT_RECORD, mIntervalTime);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 解除旧的订阅
     */
    private void disposeAccountRecord() {
        // 清空订阅
        mCompositeDisposable.clear();
    }

    /**
     * 解除旧的订阅
     */
    private void disposeConfirmNewMessage4TakeOutOrder() {
        if (mConfirmNewMessageDisposable != null && !mConfirmNewMessageDisposable.isDisposed()) {
            mConfirmNewMessageDisposable.dispose();
        }
    }


    /**
     * 解除旧的订阅
     */
    private void disposeRequestNewMessage4TakeOutOrder() {
        if (mNewMessageDisposable != null && !mNewMessageDisposable.isDisposed()) {
            mNewMessageDisposable.dispose();
        }
    }

    /**
     * 请求外卖新消息
     */
    private void requestNewMessage4TakeOutOrder() {
        XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.NewMessage)
                .setRequestBody(new UnOrderE()).buildRESTful()
                .flatMap(RepositoryImpl.getInstance()::getXmlData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<XmlData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mNewMessageDisposable = d;
                    }

                    @Override
                    public void onNext(XmlData xmlData) {
                        if (ApiNoteHelper.checkBusiness(xmlData)) {
                            List<UnMessageE> arrayOfUnMessageE = xmlData.getArrayOfUnMessageE();
                            if (arrayOfUnMessageE.size() > 0) {
                                // 语音播报
                                int[] a = new int[4];
                                for (UnMessageE unMessageE : arrayOfUnMessageE) {
                                    String msgType = unMessageE.getMsgType();
                                    if ("1010".equalsIgnoreCase(msgType)) {
                                        a[0]++;
                                    } else if ("1020".equalsIgnoreCase(msgType)) {
                                        a[1]++;
                                    } else if ("1030".equalsIgnoreCase(msgType)) {
                                        a[2]++;
                                    } else if ("2040".equalsIgnoreCase(msgType)) {
                                        a[3]++;
                                    }
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                if (a[0] == 0 && a[1] == 0 && a[2] == 0 && a[3] > 0) {
                                    stringBuilder.append("有用户已退单了");
                                } else {
                                    boolean shouldAddProcess = false;
                                    stringBuilder.append(a[0] > 0 ? "您有新的订单，" : "");
                                    stringBuilder.append(a[1] > 0 ? "有用户催单了，" : "")
                                            .append(a[2] > 0 ? "有用户申请退单了，" : "")
                                            .append(a[3] > 0 ? "有用户已退单了，" : "");
                                    stringBuilder.append("请及时处理。");
                                }
                                //通知
                                stringBuilder.delete(0, stringBuilder.length());
                                if (a[0] == 0 && a[1] == 0 && a[2] == 0 && a[3] > 0) {
                                    stringBuilder.append("有用户已退单了");
                                    newTakeOutMessageNotifyNoNavi(stringBuilder.toString(), "1分钟内退单已由系统自动处理");
                                } else {
                                    stringBuilder.append(a[0] > 0 ? "您有新的订单" + (a[1] > 0 || a[2] > 0 || a[3] > 0 ? "，" : "") : "");
                                    stringBuilder.append(a[1] > 0 ? "有用户催单了" + (a[2] > 0 || a[3] > 0 ? "，" : "") : "")
                                            .append(a[2] > 0 ? "有用户申请退单了" + (a[3] > 0 ? "，" : "") : "")
                                            .append(a[3] > 0 ? "有用户已退单了" : "");
                                    newTakeOutMessageNotify(stringBuilder.toString(), "点击查看详细信息");
                                }
                                //移除消息队列
                                UnMessageE unMessageE = arrayOfUnMessageE.get(arrayOfUnMessageE.size() - 1);
                                confirmNewMessage4TakeOutOrder(unMessageE.getMsgTimeStamp(), unMessageE.getMsgTime());
                                return;
                            }
                        }
                        mHandler.sendEmptyMessageDelayed(MSG_TAKE_OUT_ORDER, mIntervalTime);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHandler.sendEmptyMessageDelayed(MSG_TAKE_OUT_ORDER, mIntervalTime);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 发送通知 点击不可跳转
     */
    private void newTakeOutMessageNotifyNoNavi(String title, String content) {
        if (mBuilderNoNavi == null) {
            mBuilderNoNavi = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
        } else {
            mBuilderNoNavi.setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis());
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancelAll();
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilderNoNavi.build());
    }

    /**
     * 发送通知 点击可跳转
     */
    private void newTakeOutMessageNotify(String title, String content) {
        if (mBuilder == null) {
            Intent pushIntent = new Intent(this, TakeOutActivity.class);
            PendingIntent pushPendingIntent = PendingIntent.getActivity(this, 0, pushIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pushPendingIntent)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setFullScreenIntent(pushPendingIntent, true);
            }
        } else {
            mBuilder.setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis());
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancelAll();
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    /**
     * 确认已收到外卖单新消息
     */
    private void confirmNewMessage4TakeOutOrder(long msgTimeStamp, String msgTime) {
        UnOrderE unOrderE = new UnOrderE();
        unOrderE.setMsgTimeStamp(msgTimeStamp);
        unOrderE.setMsgTime(msgTime);
        XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.MsgReceiveConfirm)
                .setRequestBody(unOrderE).buildRESTful()
                .flatMap(RepositoryImpl.getInstance()::getXmlData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<XmlData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mConfirmNewMessageDisposable = d;
                    }

                    @Override
                    public void onNext(XmlData xmlData) {
                        mHandler.sendEmptyMessageDelayed(MSG_TAKE_OUT_ORDER, mIntervalTime);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHandler.sendEmptyMessageDelayed(MSG_TAKE_OUT_ORDER, mIntervalTime);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}