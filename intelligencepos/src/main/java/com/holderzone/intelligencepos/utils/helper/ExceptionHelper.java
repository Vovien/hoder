package com.holderzone.intelligencepos.utils.helper;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * 异常辅助类
 * Created by tcw on 2017/7/28.
 */

public class ExceptionHelper {

    /**
     * 检查是否是网络异常
     */
    public static boolean checkNetException(Throwable e) {
        return e instanceof HttpException
                || e instanceof SocketException
                || e instanceof SocketTimeoutException
                || e instanceof ConnectException
                || e instanceof UnknownHostException;
    }
}
