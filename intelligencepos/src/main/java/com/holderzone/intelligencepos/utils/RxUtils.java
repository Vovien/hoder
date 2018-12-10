package com.holderzone.intelligencepos.utils;

import android.text.TextUtils;


import com.holderzone.intelligencepos.mvp.model.bean.ApiNote;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by tcw on 2017/3/17.
 */

public class RxUtils {
    private static final ObservableTransformer schedulersTransformer = upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

    /**
     * 提供通用转换，只包括切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }


    /**
     * 业务成功
     *
     * @param xmlData
     * @return
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
        if (1 != noteCode.intValue()) {
            return false;
        }
        return true;
    }

    /**
     * 业务成功 打印机未打印
     *
     * @param xmlData
     * @return
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
        if (1 != noteCode.intValue()) {
            return false;
        }
        Integer resultCode = apiNote.getResultCode();
        if (resultCode == null) {
            return false;
        }
        if (10 != resultCode.intValue()) {
            return false;
        }
        return true;
    }

    /**
     * 设备授权过期
     *
     * @param xmlData
     * @return
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
        if (-101 != noteCode.intValue()) {
            return false;
        }
        return true;
    }

    /**
     * @param xmlData
     * @return
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
     * @param xmlData
     * @return
     */
    public static String obtainPrinterMsg(XmlData xmlData) {
        if (checkPrinterWhenBusinessSuccess(xmlData)) {
            return xmlData.getApiNote().getResultMsg();
        }
        return null;
    }

    /**
     * @param xmlData
     * @return
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
        switch (noteCode.intValue()) {
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
     * @param xmlData
     * @return
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
     * 检查是否是网络异常
     *
     * @param e
     * @return
     */
    public static boolean checkNetException(Throwable e) {
        return e instanceof HttpException
                || e instanceof SocketException
                || e instanceof SocketTimeoutException
                || e instanceof ConnectException
                || e instanceof UnknownHostException
                || e instanceof IOException;
    }
}
