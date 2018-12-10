package com.holderzone.intelligencepos.mvp.model.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 打印信息数据库记录实体
 * Created by 赵平 on 2017/4/20.
 */

@Entity(nameInDb = "PrintBean")
public class PrintBean {

    @Id(autoincrement = true)
    private Long id;
    /**
     * 打印key
     */
    @Unique
    @Property(nameInDb = "key")
    private String key;
    /**
     * 打印次数
     */
    @Property(nameInDb = "printTimes")
    private int printTimes;
    /**
     * 完成打印次数
     */
    @Property(nameInDb = "printFinishTimes")
    private int printFinishTimes;
    /**
     * 是否上传
     */
    @Property(nameInDb = "isUpload")
    private boolean isUpload;
    /**
     * 是否打印完成
     */
    @Property(nameInDb = "isPrintFinish")
    private boolean isPrintFinish;

    @Keep
    @Generated(hash = 881973693)
    public PrintBean(Long id, String key, int printTimes, int printFinishTimes,
                     boolean isUpload, boolean isPrintFinish) {
        this.id = id;
        this.key = key;
        this.printTimes = printTimes;
        this.printFinishTimes = printFinishTimes;
        this.isUpload = isUpload;
        this.isPrintFinish = isPrintFinish;
    }

    @Keep
    @Generated(hash = 1309597907)
    public PrintBean() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPrintTimes() {
        return this.printTimes;
    }

    public void setPrintTimes(int printTimes) {
        this.printTimes = printTimes;
    }

    public int getPrintFinishTimes() {
        return this.printFinishTimes;
    }

    public void setPrintFinishTimes(int printFinishTimes) {
        this.printFinishTimes = printFinishTimes;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsPrintFinish() {
        return this.isPrintFinish;
    }

    public void setIsPrintFinish(boolean isPrintFinish) {
        this.isPrintFinish = isPrintFinish;
    }
}
