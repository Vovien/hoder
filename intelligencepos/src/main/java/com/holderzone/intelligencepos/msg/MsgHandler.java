package com.holderzone.intelligencepos.msg;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.DialogFactory;
import com.holderzone.intelligencepos.printer.PushPrintBean;
import com.holderzone.intelligencepos.printer.PushPrintService;
import com.holderzone.intelligencepos.utils.BaiduTTS;
import com.holderzone.intelligencepos.utils.RealmUtil;

import io.realm.Realm;


/**
 * Created by zhaoping on 2018/8/23.
 */
public class MsgHandler {
    public static void handlerMsg(String msg) {
        Gson gson = new Gson();

        PushPrintBean pushPrintBean = null;
        try {
            pushPrintBean = gson.fromJson(msg, PushPrintBean.class);
        } catch (JsonSyntaxException e) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        try {
            PushPrintBean dbBean = realm.where(PushPrintBean.class).equalTo("Key", pushPrintBean.getKey()).findFirst();
            if (dbBean != null) {
                return;
            }
            realm.beginTransaction();
            pushPrintBean.setInsertTimestamp(System.currentTimeMillis());
            realm.insertOrUpdate(pushPrintBean);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
            Realm.compactRealm(RealmUtil.getPushConfig());
        }
        if (pushPrintBean.getMsgType() == 0) {
            Intent intent = new Intent(BaseApplication.getApplication().getApplicationContext(), PushPrintService.class);
            BaseApplication.getApplication().getApplicationContext().startService(intent);
        } else if (pushPrintBean.getMsgType() == 1) {
            if ((pushPrintBean.getFailTime() != null && pushPrintBean.getFailTime() < System.currentTimeMillis()) || BaseApplication.getApplication().isHasLogin() == false) {
                return;
            }
            if (EmptyUtils.isNotEmpty(pushPrintBean.getMsgData())) {
                Notice notice = gson.fromJson(pushPrintBean.getMsgData(), Notice.class);
                switch (notice.getActionType()) {
                    case 1://营业日
                        new Handler(Looper.getMainLooper()).post(() -> DialogFactory.showForceLogoutDialogWithServiceShutDownResultDataClear(BaseApplication.getApplication(), "营业日异常，请退出程序。"));
                        break;
                    case 2://结账通知
//                        BaiduTTS.getInstance().speak(notice.getVoiceText());
//                        AppNotifyEventHandler.getInstance().newPayMessageNotify("结账通知", notice.getNoticeText());
                        break;
                    case 3://付款通知
//                        if (LocalCacheUtils.getInstance().getIsBillNotifications(BaseApplication.getApplication())) {
//                            BaiduTTS.getInstance().speak(notice.getVoiceText());
//                            AppNotifyEventHandler.getInstance().newPayMessageNotify("微信点餐", notice.getNoticeText());
//                        }
                        break;
                    case 4://账单发生变化
                        BaseApplication.getApplication().sendBroadcast(new Intent(NoticeBroadcastAction.ORDER_CHANGED));
                        break;
                    case 5://桌台状态发生改变
                        BaseApplication.getApplication().sendBroadcast(new Intent(NoticeBroadcastAction.TABLE_CHANGED));
                        break;
                    case 6://外卖状态发生改变
                        //发送通知，不可跳转
                        BaiduTTS.getInstance().speak(notice.getNoticeText());
                        if (notice.isClickJump()) {
                            AppNotifyEventHandler.getInstance().newTakeOutMessageNotify(notice.getNoticeTitle(), notice.getNoticeText());
                        } else {
                            AppNotifyEventHandler.getInstance().newTakeOutMessageNotifyNoNavi(notice.getNoticeTitle(), notice.getNoticeText());
                        }
                        break;
                    case 7:// 排队发生变化
                        BaseApplication.getApplication().sendBroadcast(new Intent(NoticeBroadcastAction.QUEUE_CHANGED));
                        break;
                    case 8://外卖页面发生变化
                        BaseApplication.getApplication().sendBroadcast(new Intent(NoticeBroadcastAction.TAKEOUT_CHANGED));
                        break;
                }
            }
        }
    }
}
