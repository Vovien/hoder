package com.holderzone.intelligencepos.mvp.model.network;

import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.printer.PushFeedback;
import com.holderzone.intelligencepos.printer.PushPrintResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Url;

/**
 * Created by tcw on 2017/7/21.
 */

public interface NetworkManager {

    Observable<XmlData> getXmlData(XmlData xmlData);

    Observable<ResponseBody> downloadPicture(@Url String url);

    Observable<PushPrintResponse> getPrintData(String key);

    Observable<PushPrintResponse> pushRegister(String enterpriseInfoGUID, String storeGuid);

    Observable<PushPrintResponse> pushFeedback(List<PushFeedback> list);
}

