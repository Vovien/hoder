package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/3/10.
 */

public class ApiNote {
    /**
     * api处理状态类别ID
     */
    private Integer NoteCode;
    /**
     * api处理状态类别消息
     * 1 成功 业务成功
     * 0 服务器停机 系统维护
     * -1 异常 签名错误
     * -2 异常 数据结构错误
     * -3 异常 参数错误
     * -4 异常 业务失败
     * -100 异常 系统错误
     * -101 异常 其它错误
     */
    private String NoteMsg;
    /**
     * 业务处理返回状态ID
     * ResultCode为api处理状态，1为处理成功，非1则为失败或未响应处理，具体参见详细注释。
     * ResultCode为具体的业务层面处理状态标识
     */
    private Integer ResultCode;
    /**
     * 业务处理量返回消息
     */
    private String ResultMsg;

    public Integer getNoteCode() {
        return NoteCode;
    }

    public void setNoteCode(Integer noteCode) {
        NoteCode = noteCode;
    }

    public String getNoteMsg() {
        return NoteMsg;
    }

    public void setNoteMsg(String noteMsg) {
        NoteMsg = noteMsg;
    }

    public Integer getResultCode() {
        return ResultCode;
    }

    public void setResultCode(Integer resultCode) {
        ResultCode = resultCode;
    }

    public String getResultMsg() {
        return ResultMsg;
    }

    public void setResultMsg(String resultMsg) {
        ResultMsg = resultMsg;
    }
}
