package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by terry on 17-11-23.
 */

public class StateMsg {

    /**
     * 消息等级
     * confirm = 2
     * hint = 1
     * log = 0
     */
    private Integer Level;

    /**
     *
     */
    private String Msg;

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
