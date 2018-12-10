package com.holderzone.intelligencepos.mvp.model.network.interceptor;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.holderzone.intelligencepos.BuildConfig;
import com.holderzone.intelligencepos.utils.ZipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by tcw on 2017/3/11.
 */

public class RequestInterceptor implements Interceptor {

    public RequestInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Buffer requestbuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestbuffer);
        } else {
            LogUtils.w("Request", "request.body() == null");
        }
        String requestString = request.body() != null ? parseParams(request.body(), requestbuffer) : "null";
        final boolean displayLog = BuildConfig.DEBUG && requestString.contains("\"consolePrintLog\":true");
        Response originalResponse = chain.proceed(request);
        if (displayLog) {
            //打印url信息
            LogUtils.w("Request", String.format("Sending Request %s on %n Params --->  %s%n Connection ---> %s%n Headers ---> %s", request.url()
                    , requestString
                    , chain.connection()
                    , request.headers()));
            long t1 = System.nanoTime();
            long t2 = System.nanoTime();
            LogUtils.w("Response", "Received response  in %.1fms%n%s", (t2 - t1) / 1e6d, originalResponse.headers());
        }
        //打印响应时间
        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        //获取content的压缩类型
        String encoding = originalResponse
                .headers()
                .get("Content-Encoding");
        Buffer clone = buffer.clone();
        String bodyString;
//        //解析response content 默认没压缩
//        Charset charset = Charset.forName("UTF-8");
//        MediaType contentType = responseBody.contentType();
//        if (contentType != null) {
//            charset = contentType.charset(charset);
//        }
//        bodyString = clone.readString(charset);
        //解析response content
        if (encoding != null && "gzip".equalsIgnoreCase(encoding)) {//content使用gzip压缩
            bodyString = ZipUtils.decompressForGzip(clone.readByteArray());//解压
        } else if (encoding != null && "zlib".equalsIgnoreCase(encoding)) {//content使用zlib压缩
            bodyString = ZipUtils.decompressToStringForZlib(clone.readByteArray());//解压
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }
        //格式化body
        String message;
        try {
            if (bodyString.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(bodyString);
                message = jsonObject.toString(4);
            } else if (bodyString.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(bodyString);
                message = jsonArray.toString(4);
            } else {
                message = bodyString;
            }
        } catch (JSONException e) {
            message = bodyString;
        }
        if (displayLog) {
            LogUtils.w("Result", message);
        }
        return originalResponse;
    }

    @NonNull
    public static String parseParams(RequestBody body, Buffer requestbuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return requestbuffer.readUtf8();
        }
        return "null";
    }
}
