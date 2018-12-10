package com.holderzone.intelligencepos.mvp.model.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tcw on 2017/5/23.
 */

@Entity(nameInDb = "ImageBean")
public class ImageBean {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "remoteUrl")
    private String remoteUrl;

    @Property(nameInDb = "localUrl")
    private String localUrl;
    /**
     * 类型 1 = 副屏图片 2 打印logo图片
     */
    @Property(nameInDb = "type")
    private int type;

    @Keep
    @Generated(hash = 1726261472)
    public ImageBean(Long id, String remoteUrl, String localUrl, int type) {
        this.id = id;
        this.remoteUrl = remoteUrl;
        this.localUrl = localUrl;
        this.type = type;
    }

    @Keep
    @Generated(hash = 645668394)
    public ImageBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static enum Type {
        Logo("Logo", 1), PrintLogo("PrintLogo", 2);
        private String dir;
        private int type;

        private Type(String dir, int type) {
            this.dir = dir;
            this.type = type;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
