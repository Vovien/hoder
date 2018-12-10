package com.holderzone.intelligencepos.mvp.model.network.api;

import com.holderzone.intelligencepos.printer.PushFeedback;
import com.holderzone.intelligencepos.printer.PushPrintResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tcw on 2017/5/23.
 */

public interface AcquirePrintDataService {
    @GET("print/getList")
    Observable<PushPrintResponse> getPrint(@Query("key") String key);

    @GET("device/register")
    Observable<PushPrintResponse> pushRegister(@Query("deviceKey") String aliDeviceId, @Query("deviceCode") String deviceCode,
                                               @Query("enterpriseInfoGUID") String enterpriseInfoGUID, @Query("storeGUID") String storeGUID, @Query("appKey") long appKey,
                                               @Query("appCode") int appCode);

    @Headers({
            "Content-Type:application/json; charset=utf-8"
    })
    @POST("print/feedback")
    Observable<PushPrintResponse> pushFeedback(@Body List<PushFeedback> list);
}
