package com.holderzone.intelligencepos.mvp.model.file;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by tcw on 2017/5/23.
 */

public class FileImage {

    public Observable<String> writeToDisk(String remoteUrl, ResponseBody body, ImageBean.Type type) {
        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            BaseApplication.showMessage("存储不可用");
            return Observable.just("");
        }
        try {
            InputStream is = body.byteStream();
            String path = getAlbumStorageDir(type.getDir()).getPath();
            File fileDr = new File(path);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            String name = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
            File file = new File(path, name);
            if (file.exists()) {
                file.delete();
                file = new File(path, name);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
            return Observable.just(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Observable.just("");
        }
    }

    public Observable<Boolean> correct(List<String> remoteUrls, ImageBean.Type type) {
        List<String> fileNames = new ArrayList<>();
        for (String remoteUrl : remoteUrls) {
            fileNames.add(remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1));
        }
        List<File> files = FileUtils.listFilesInDir(getAlbumStorageDir(type.getDir()), false);
        for (File file : files) {
            String fileName = file.getName();
            if (!fileNames.contains(fileName)) {
                FileUtils.deleteFile(file);
            }
        }
        return Observable.just(true);
    }

    private File getAlbumStorageDir() {
        return getAlbumStorageDir("holder");
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

    /**
     * Checks if external storage is available for read and write
     *
     * @return
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     *
     * @return
     */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
