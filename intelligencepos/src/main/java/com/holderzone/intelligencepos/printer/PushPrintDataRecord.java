package com.holderzone.intelligencepos.printer;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * @author zhaoping
 * @date 2018/6/29
 */
public class PushPrintDataRecord extends RealmObject {

    @PrimaryKey
    @Required
    private String key;

    @Required
    private Integer printTimes;

    private Integer printFinishTimes;

    private boolean isTimeout;

    private boolean isPrintFinish;
    /**
     * 获取打印数据时间
     */
    private Long obtainDataMillisecond;

    private long printUseTime;

    private String printDataJson;

    /**
     * 推送到达至获取打印数据耗时
     */
    private Long pullDataElapsedTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(Integer printTimes) {
        this.printTimes = printTimes;
    }

    public Integer getPrintFinishTimes() {
        return printFinishTimes;
    }

    public void setPrintFinishTimes(Integer printFinishTimes) {
        this.printFinishTimes = printFinishTimes;
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean timeout) {
        isTimeout = timeout;
    }

    public boolean isPrintFinish() {
        return isPrintFinish;
    }

    public void setPrintFinish(boolean printFinish) {
        isPrintFinish = printFinish;
    }

    public Long getObtainDataMillisecond() {
        return obtainDataMillisecond;
    }

    public void setObtainDataMillisecond(Long obtainDataMillisecond) {
        this.obtainDataMillisecond = obtainDataMillisecond;
    }

    public long getPrintUseTime() {
        return printUseTime;
    }

    public void setPrintUseTime(long printUseTime) {
        this.printUseTime = printUseTime;
    }

    public String getPrintDataJson() {
        return printDataJson;
    }

    public void setPrintDataJson(String printDataJson) {
        this.printDataJson = printDataJson;
    }

    public Long getPullDataElapsedTime() {
        return pullDataElapsedTime;
    }

    public void setPullDataElapsedTime(Long pullDataElapsedTime) {
        this.pullDataElapsedTime = pullDataElapsedTime;
    }
}
