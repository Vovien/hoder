package com.holderzone.intelligencepos.mvp.model.file;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 文件模块
 * Created by tcw on 2017/7/21.
 */

public class FileManagerImpl implements FileManager {
    private volatile static FileManagerImpl sInstance;

    private static final String BANNER_DIR_NAME = "banner";

    public static FileManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (FileManagerImpl.class) {
                if (sInstance == null) {
                    sInstance = new FileManagerImpl();
                }
            }
        }
        return sInstance;
    }

    private FileManagerImpl() {
    }

    @Override
    public Observable<Boolean> checkLocalFileInDiskExist(String localPath) {
        return Observable.just(FileUtils.isFileExists(localPath));
    }

    @Override
    public Observable<String> writeFileToDisk(String remoteUrl, ResponseBody responseBody, ImageBean.Type type) {
        return FileClient.getInstance().getFileImage().writeToDisk(remoteUrl, responseBody, type);
    }

    @Override
    public Observable<String> saveBannerImageToAlbum(String path) {
        return Observable.create(e -> {
            File srcFile = new File(path);
            String srcFileName = srcFile.getName();
            File albumDir = getAlbumStorageDir(BANNER_DIR_NAME);
            File destFile = new File(albumDir, srcFileName);
            if (FileUtils.copyFile(srcFile, destFile, () -> true)) {
                String copyedImagePath = destFile.getAbsolutePath();
                e.onNext(copyedImagePath);
                e.onComplete();
            } else {
                e.onError(new Exception("未能复制轮播图片至相簿"));
            }
        });
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                LogUtils.d("Directory not created");
            }
        }
        return file;
    }
}
