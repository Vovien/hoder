package com.holderzone.intelligencepos.mvp.model.network.errorhandling;

import android.net.ParseException;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * NoteCode == 1 && ResultCode == 1 成功
 * NoteCode == 1 && ResultCode == 10 成功（打印失败）
 * NoteCode == -101 设备授权过期
 * 其他 错误
 * Created by tcw on 2017/4/17.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    /**
     * view层的引用
     */
    private IView mIView;

    /**
     * 是否启用统一处理
     */
    private boolean mHandleApiUniformlyEnable = true;

    public BaseObserver(IView iView) {
        this(iView, true);
    }

    public BaseObserver(IView iView, boolean handleApiUniformlyEnable) {
        mIView = iView;
        mHandleApiUniformlyEnable = handleApiUniformlyEnable;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (mHandleApiUniformlyEnable && t instanceof XmlData) {
            XmlData xmlData = (XmlData) t;
            if (ApiNoteHelper.checkBusiness(xmlData)) {
                if (ApiNoteHelper.checkPrinterWhenBusinessSuccess(xmlData)) {
                    mIView.showMessage(ApiNoteHelper.obtainPrinterMsg(xmlData));
                }
                next(t);
            } else {
                onError(new ApiException(xmlData));
            }
        } else {
            next(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof ApiException) {
            String message = e.getMessage();
            ApiException apiException = (ApiException) e;
            if (apiException.getNoteCode() == -101) {
                mIView.showAuthorizationDialog(message);//授权过期 错误
                return;
            } else {
                mIView.showMessage(message);//一般api错误
            }
        } else if (e instanceof HttpException) {
            mIView.showMessage("网络错误");
        } else if (e instanceof SocketException) {
            mIView.showMessage("连接失败");
        } else if (e instanceof SocketTimeoutException) {
            mIView.showMessage("连接超时");
        } else if (e instanceof IOException||e instanceof ConnectException) {
            mIView.showMessage("网络错误");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            mIView.showMessage("解析失败");
        } else if (e instanceof NullPointerException) {
            mIView.showMessage("空指针异常");
        } else if (e instanceof NetworkOnMainThreadException) {
            mIView.showMessage("主线程耗时操作");
        } else {
            mIView.showMessage(!TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "未知错误");
        }
        error(e);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 正常事件处理
     *
     * @param t
     */
    protected abstract void next(T t);

    /**
     * 发生错误但不至于一定退出软件时会调用该方法；否则，不调用。
     *
     * @param e
     */
    protected void error(Throwable e) {

    }
}
