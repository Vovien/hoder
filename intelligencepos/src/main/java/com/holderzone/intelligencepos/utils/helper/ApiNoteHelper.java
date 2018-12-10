package com.holderzone.intelligencepos.utils.helper;

import android.text.TextUtils;

import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.AccountRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.ApiNote;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * ApiNote辅助类
 * Created by tcw on 2017/7/28.
 */

public class ApiNoteHelper {

    /**
     * 业务成功
     */
    public static boolean checkBusiness(XmlData xmlData) {
        ApiNote apiNote = xmlData.getApiNote();
        if (apiNote == null) {
            return false;
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return false;
        }
        if (1 != noteCode) {
            return false;
        }
        return true;
    }

    /**
     * 业务成功 打印机未打印
     */
    public static boolean checkPrinterWhenBusinessSuccess(XmlData xmlData) {
        ApiNote apiNote = xmlData.getApiNote();
        if (apiNote == null) {
            return false;
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return false;
        }
        if (1 != noteCode) {
            return false;
        }
        Integer resultCode = apiNote.getResultCode();
        if (resultCode == null) {
            return false;
        }
        if (10 != resultCode) {
            return false;
        }
        return true;
    }

    /**
     * 设备授权过期
     */
    public static boolean checkAuthorizationWhenBusinessFailed(XmlData xmlData) {
        ApiNote apiNote = xmlData.getApiNote();
        if (apiNote == null) {
            return false;
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return false;
        }
        if (-101 != noteCode) {
            return false;
        }
        return true;
    }

    /**
     * @param xmlData
     * @return -2 授权失败 -1 营业日失败 0 忽略 1 营业日成功
     */
    public static Observable<Integer> checkAccountRecordByRESTful(XmlData xmlData) {
        return Observable.defer(() -> {
            ApiNote apiNote = xmlData.getApiNote();
            if (null == apiNote) {
                return Observable.just(0);
            }
            Integer noteCode = apiNote.getNoteCode();
            if (null == noteCode) {
                return Observable.just(0);
            }
            if (1 != noteCode && 0 != noteCode) {
                return Observable.just(-101 == noteCode ? -2 : -1);
            }
            Integer resultCode = apiNote.getResultCode();
            if (null == resultCode) {
                return Observable.just(0);
            }
            if (1 != resultCode) {
                return Observable.just(-1);
            }
            AccountRecordE accountRecordE = xmlData.getAccountRecordE();
            if (null == accountRecordE) {
                return Observable.just(-1);
            }
            return RepositoryImpl.getInstance().getAccountRecord()
                    .map(accountRecord -> accountRecord.getAccountRecordGUID().equalsIgnoreCase(accountRecordE.getAccountRecordGUID()) ? 1 : -1);
        });
    }

    /**
     * 微信、支付宝支付时：检查 开始轮询、抛出错误
     */
    public static boolean checkNeedIntervalWhenBusinessFailed(XmlData xmlData) {
        ApiNote apiNote = xmlData.getApiNote();
        if (apiNote == null) {
            return false;
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return false;
        }
        if (-4 != noteCode) {
            return false;
        }
        Integer resultCode = apiNote.getResultCode();
        if (resultCode == null) {
            return false;
        }
        if (0 != resultCode) {
            return false;
        }
        return true;
    }

    /**
     * 微信、支付宝支付时：检查 继续轮询、抛出错误
     */
    public static boolean checkIntervalStatusWhenBusinessFailed(XmlData xmlData) {
        ApiNote apiNote = xmlData.getApiNote();
        if (apiNote == null) {
            return false;
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return false;
        }
        if (-4 != noteCode) {
            return false;
        }
        Integer resultCode = apiNote.getResultCode();
        if (resultCode == null) {
            return false;
        }
        if (0 != resultCode) {
            return false;
        }
        return true;
    }

    /**
     * 获取成功提示信息
     */
    public static String obtainSuccessMsg(XmlData xmlData) {
        if (checkBusiness(xmlData)) {
            String msg = xmlData.getApiNote().getResultMsg();
            if (TextUtils.isEmpty(msg)) {
                msg = xmlData.getApiNote().getNoteMsg();
            }
            return msg;
        }
        return null;
    }

    /**
     * 获取错误提示信息
     */
    public static String obtainErrorMsg(XmlData xmlData) {
        if (checkBusiness(xmlData)) {
            return null;
        }
        ApiNote apiNote = xmlData.getApiNote();
        if (apiNote == null) {
            return "ApiNote为空";
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return "NoteCode为空";
        }
        String message = null;
        switch (noteCode) {
            case 0://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -1://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -2://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -3://一定返回NoteCode&&NoteMsg，可能返回ResultCode&&ResultMsg
                message = apiNote.getResultMsg();
                if (TextUtils.isEmpty(message)) {
                    message = apiNote.getNoteMsg();
                }
                break;
            case -4://一定返回NoteCode&&NoteMsg，一定返回ResultCode&&ResultMsg
                message = apiNote.getResultMsg();
                break;
            case -100://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -101://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            default:
                message = apiNote.getNoteMsg();
                break;
        }
        return message;
    }

    /**
     * 获取打印提示信息
     */
    public static String obtainPrinterMsg(XmlData xmlData) {
        if (checkPrinterWhenBusinessSuccess(xmlData)) {
            return xmlData.getApiNote().getResultMsg();
        }
        return null;
    }

    /**
     * 获取授权提示信息
     */
    public static String obtainAuthorizationMsg(XmlData xmlData) {
        if (checkBusiness(xmlData)) {
            return null;
        }
        if (checkAuthorizationWhenBusinessFailed(xmlData)) {
            return xmlData.getApiNote().getNoteMsg();
        }
        return null;
    }

    /**
     * 封装 检测打印是否成功，若打印失败，toast提示
     *
     * @param mIView
     * @return
     */
    public static ObservableTransformer<XmlData, XmlData> applyCheckWithPrinter(IView mIView) {
        return upstream -> upstream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(xmlData -> {
                    if (ApiNoteHelper.checkPrinterWhenBusinessSuccess(xmlData)) {
                        mIView.showMessage(ApiNoteHelper.obtainPrinterMsg(xmlData));
                    }
                });
    }

}
