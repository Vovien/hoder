package com.holderzone.intelligencepos.mvp.model.network.api;

import com.holderzone.intelligencepos.mvp.model.bean.XmlData;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by tcw on 2017/3/17.
 */

public interface NetworkService {
    @POST(".")
    Observable<XmlData> getXmlData(@Body XmlData jsonData);

    @GET
    Observable<ResponseBody> downloadPicture(@Url String url);
}
