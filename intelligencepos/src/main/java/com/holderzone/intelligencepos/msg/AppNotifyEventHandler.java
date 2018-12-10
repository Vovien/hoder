package com.holderzone.intelligencepos.msg;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.activity.TakeOutActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by tcw on 2017/5/16.
 */

public class AppNotifyEventHandler {


    /**
     * 轮询营业日的handler
     */
    private Handler mHandler;
    private static AppNotifyEventHandler appNotifyEventHandler;
    private Context mContext;

    private AppNotifyEventHandler() {
        mContext = BaseApplication.getApplication();
    }

    public static AppNotifyEventHandler getInstance() {
        if (appNotifyEventHandler == null) {
            synchronized (AppNotifyEventHandler.class) {
                if (appNotifyEventHandler == null) {
                    appNotifyEventHandler = new AppNotifyEventHandler();
                }
            }
        }
        return appNotifyEventHandler;
    }

    /**
     * 确认已收到外卖单新消息
     *
     * @param msgTimeStamp
     * @param msgTime
     */
    public void confirmNewMessage4TakeOutOrder(long msgTimeStamp, String msgTime) {
//        UnOrderE unOrderE = new UnOrderE();
//        unOrderE.setMsgTimeStamp(msgTimeStamp);
//        unOrderE.setMsgTime(msgTime);
//        RetrofitClient.getInstance().getXmlData(
//                XmlData.Builder()
//                        .setRequestMethod(RequestMethod.UnOrderB.MsgReceiveConfirm)
//                        .setRequestBody(unOrderE)
//                        .build())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<XmlData>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(XmlData xmlData) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }


    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.Builder mBuilderNoNavi;
    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mPayBuilder;

    /**
     * 发送通知 点击可跳转
     *
     * @param title
     * @param content
     */
    public void newTakeOutMessageNotify(String title, String content) {
        if (mBuilder == null) {
            Intent pushIntent = new Intent(mContext, TakeOutActivity.class);
            PendingIntent pushPendingIntent = PendingIntent.getActivity(mContext, 0, pushIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder = new NotificationCompat.Builder(mContext)
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
            mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        }

        mNotificationManager.cancelAll();
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }
//
    public void newPayMessageNotify(String title, String content) {
        if (mPayBuilder == null) {
            Intent pushIntent = new Intent();
            PendingIntent pushPendingIntent = PendingIntent.getActivity(mContext, 0, pushIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mPayBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPayBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setFullScreenIntent(pushPendingIntent, true);
            }
        } else {
            mPayBuilder.setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis());
        }

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        }

        mNotificationManager.cancelAll();
        mNotificationManager.notify((int) System.currentTimeMillis(), mPayBuilder.build());
    }

    /**
     * 发送通知 点击不可跳转
     *
     * @param title
     * @param content
     */
    public void newTakeOutMessageNotifyNoNavi(String title, String content) {
//        if (mBuilderNoNavi == null) {
//            mBuilderNoNavi = new NotificationCompat.Builder(mContext)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setAutoCancel(true)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setDefaults(NotificationCompat.DEFAULT_LIGHTS);
//        } else {
//            mBuilderNoNavi.setContentTitle(title)
//                    .setContentText(content)
//                    .setWhen(System.currentTimeMillis());
//        }
//
//        if (mNotificationManager == null) {
//            mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
//        }
//
//        mNotificationManager.cancelAll();
//        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilderNoNavi.build());
    }
}