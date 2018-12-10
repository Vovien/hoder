package com.holderzone.intelligencepos.mvp.model.file;

import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by tcw on 2017/7/21.
 */

public interface FileManager {

    Observable<String> saveBannerImageToAlbum(String path);

    Observable<Boolean> checkLocalFileInDiskExist(String localPath);

    Observable<String> writeFileToDisk(String remoteUrl, ResponseBody responseBody, ImageBean.Type type);
}
