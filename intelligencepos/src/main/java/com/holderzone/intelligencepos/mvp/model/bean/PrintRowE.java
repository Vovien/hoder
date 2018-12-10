package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by NEW on 2017/3/27.
 */

public class PrintRowE  {
    /**
     * 字体大小
     */
    private String FontSize;
    /**
     * X坐标字体倍数
     */
    private int XM;
    /**
     * Y坐标字体倍数
     */
    private int YM;
    /**
     * 对齐方式
     * L=居左  C=居中  R=居右
     */
    private String Align;
    /**
     * 加粗
     */
    private String Blod;
    /**
     * 字体
     */
    private String FontFamily;
    /**
     * 类型
     * Text=普通文本   IMG=图片   QRCode=二维码
     */
    private String ContentType;
    /**
     * 打印内容
     * 由ContentType决定  ContentType= IMG时，此字段为Base64编码的数据
     */
    private String Content;

    public String getFontSize() {
        return FontSize;
    }

    public void setFontSize(String fontSize) {
        FontSize = fontSize;
    }

    public int getXM() {
        return XM;
    }

    public void setXM(int XM) {
        this.XM = XM;
    }

    public int getYM() {
        return YM;
    }

    public void setYM(int YM) {
        this.YM = YM;
    }

    public String getAlign() {
        return Align;
    }

    public void setAlign(String align) {
        Align = align;
    }

    public String getBlod() {
        return Blod;
    }

    public void setBlod(String blod) {
        Blod = blod;
    }

    public String getFontFamily() {
        return FontFamily;
    }

    public void setFontFamily(String fontFamily) {
        FontFamily = fontFamily;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "PrintRowE{" +
                "FontSize='" + FontSize + '\'' +
                ", XM=" + XM +
                ", YM=" + YM +
                ", Align='" + Align + '\'' +
                ", Blod='" + Blod + '\'' +
                ", FontFamily='" + FontFamily + '\'' +
                ", ContentType='" + ContentType + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }
}
