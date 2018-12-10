package com.holderzone.intelligencepos.printer;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by zhaoping on 2018/6/29.
 */
public class PushPrintBean extends RealmObject {
    @PrimaryKey
    @Required
    private String Key;
    @Required
    private Long CreatTime;
    @Required
    private Long FailTime;
    @Required
    private String MsgData;
    @Required
    private Long insertTimestamp;

    private Integer MsgType;

    private boolean hasObtain;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Long getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(Long creatTime) {
        CreatTime = creatTime;
    }

    public Long getFailTime() {
        return FailTime;
    }

    public void setFailTime(Long failTime) {
        FailTime = failTime;
    }

    public String getMsgData() {
        return MsgData;
    }

    public void setMsgData(String msgData) {
        MsgData = msgData;
    }

    public boolean isHasObtain() {
        return hasObtain;
    }

    public void setHasObtain(boolean hasObtain) {
        this.hasObtain = hasObtain;
    }

    public Long getInsertTimestamp() {
        return insertTimestamp;
    }

    public void setInsertTimestamp(Long insertTimestamp) {
        this.insertTimestamp = insertTimestamp;
    }

    public Integer getMsgType() {
        return MsgType;
    }

    public void setMsgType(Integer msgType) {
        MsgType = msgType;
    }
}
