package com.holderzone.intelligencepos.mvp.model.network;

import android.util.Log;

import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.holderzone.intelligencepos.BuildConfig;
import com.holderzone.intelligencepos.base.Config;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.api.AcquirePrintDataService;
import com.holderzone.intelligencepos.mvp.model.network.api.NetworkService;
import com.holderzone.intelligencepos.mvp.model.network.converter.CustomGsonConverterFactory;
import com.holderzone.intelligencepos.mvp.model.network.interceptor.RequestInterceptor;
import com.holderzone.intelligencepos.printer.PushFeedback;
import com.holderzone.intelligencepos.printer.PushPrintResponse;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.holderzone.intelligencepos.base.Config.PUSH_PRINT_URL;

/**
 * 网络模块
 * Created by tcw on 2017/7/21.
 */

public class NetworkManagerImpl implements NetworkManager {
    private volatile static NetworkManagerImpl sInstance;
    private static final int TIME_OUT = 10;
    private NetworkService mNetworkService;
    private AcquirePrintDataService mAcquirePrintDataService;

    public static NetworkManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (NetworkManagerImpl.class) {
                if (sInstance == null) {
                    sInstance = new NetworkManagerImpl();
                }
            }
        }
        return sInstance;
    }

    private NetworkManagerImpl() {
        // OkHttpClient 基本配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS) //设置连接超时
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)    //设置读取超时
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS);   //设置写入超时
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new RequestInterceptor());//设置网络拦截器
        }
        // 添加证书信任
        applyUnsafeTransform(builder);
        // 构建OkHttpClient
        OkHttpClient okHttpClient = builder.build();
        // Retrofit 基本配置
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.INTELLIGENCE_URL)//域名
                .client(okHttpClient)//设置okhttpClient
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//设置callAdapter
                .addConverterFactory(CustomGsonConverterFactory.create())//设置gsonConverter
                .build();
        mNetworkService = retrofit.create(NetworkService.class);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder acquirePrintDataServiceBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS) //设置连接超时
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)    //设置读取超时
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS);   //设置写入超时
        // 添加证书信任
        applyUnsafeTransform(acquirePrintDataServiceBuilder);
        // 构建OkHttpClient
        OkHttpClient acquirePrintDataOkHttpClient = acquirePrintDataServiceBuilder.build();
        Retrofit acquirePrintDataRetrofit = new Retrofit.Builder()
                .baseUrl(PUSH_PRINT_URL)//域名
                .client(acquirePrintDataOkHttpClient)//设置okhttpClient
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//设置callAdapter
                .addConverterFactory(GsonConverterFactory.create())//设置gsonConverter
                .build();
        mAcquirePrintDataService = acquirePrintDataRetrofit.create(AcquirePrintDataService.class);
    }

    /**
     * 信任所有证书
     *
     * @param builder
     * @return
     */
    private OkHttpClient.Builder applyUnsafeTransform(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Observable<XmlData> getXmlData(XmlData xmlData) {
        return mNetworkService.getXmlData(xmlData);
    }

    @Override
    public Observable<ResponseBody> downloadPicture(String url) {
        return mNetworkService.downloadPicture(url);
    }

    @Override
    public Observable<PushPrintResponse> getPrintData(String key) {
        return mAcquirePrintDataService.getPrint(key);
    }

    @Override
    public Observable<PushPrintResponse> pushRegister(String enterpriseInfoGUID, String storeGuid) {
        return mAcquirePrintDataService.pushRegister(PushServiceFactory.getCloudPushService().getDeviceId(),
                DeviceHelper.getInstance().getDeviceID(), enterpriseInfoGUID, storeGuid, Config.ALI_PUSH_KEY, 1);
    }

    @Override
    public Observable<PushPrintResponse> pushFeedback(List<PushFeedback> list) {
        return mAcquirePrintDataService.pushFeedback(list);
    }
}
