package com.holderzone.intelligencepos.msg;

/**
 * 推送通知类
 * Created by zhaoping on 2018/8/23.
 */
public class Notice {
    /**
     * 通知栏标题
     */
    private String NoticeTitle;
    /**
     * 1=结束营业日
     * 2=结账通知
     * 3=付款通知
     * 4=账单发生变化
     * 5=桌台状态发生改变
     * 6=外卖状态发生改变
     */
    private Integer ActionType;
    /**
     * 是否通知栏通知
     */
    private boolean IsNotice;
    /**
     * 通知栏文字
     */
    private String NoticeText;
    /**
     * 是否播报语音
     */
    private boolean IsVoice;
    /**
     * 语音内容
     */
    private String VoiceText;
    /**
     * 是否点击跳转
     */
    private boolean IsClickJump;
    /**
     * 拓展业务内容（JsonString）
     */
    private boolean ExtendData;

    public Integer getActionType() {
        return ActionType;
    }

    public void setActionType(Integer actionType) {
        ActionType = actionType;
    }

    public boolean isNotice() {
        return IsNotice;
    }

    public void setNotice(boolean notice) {
        IsNotice = notice;
    }

    public String getNoticeText() {
        return NoticeText;
    }

    public void setNoticeText(String noticeText) {
        NoticeText = noticeText;
    }

    public boolean isVoice() {
        return IsVoice;
    }

    public void setVoice(boolean voice) {
        IsVoice = voice;
    }

    public String getVoiceText() {
        return VoiceText;
    }

    public void setVoiceText(String voiceText) {
        VoiceText = voiceText;
    }

    public boolean isClickJump() {
        return IsClickJump;
    }

    public void setClickJump(boolean clickJump) {
        IsClickJump = clickJump;
    }

    public boolean isExtendData() {
        return ExtendData;
    }

    public void setExtendData(boolean extendData) {
        ExtendData = extendData;
    }

    public String getNoticeTitle() {
        return NoticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        NoticeTitle = noticeTitle;
    }
}
