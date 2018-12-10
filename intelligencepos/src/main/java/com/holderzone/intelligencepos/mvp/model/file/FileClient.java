package com.holderzone.intelligencepos.mvp.model.file;

/**
 * Created by tcw on 2017/5/23.
 */

public class FileClient {
    private volatile static FileClient sInstance;//使用volatile变量以保证是在主内存上取值
    private FileImage mFileImage;

    public static FileClient getInstance() {
        if (sInstance == null) {
            synchronized (FileClient.class) {
                if (sInstance == null) {
                    sInstance = new FileClient();
                }
            }
        }
        return sInstance;
    }

    private FileClient() {
        mFileImage = new FileImage();
    }

    public FileImage getFileImage() {
        return mFileImage;
    }
}
