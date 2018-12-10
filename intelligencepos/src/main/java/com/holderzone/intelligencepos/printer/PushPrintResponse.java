package com.holderzone.intelligencepos.printer;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;

import java.util.List;

/**
 * Created by zhaoping on 2018/6/27.
 */
public class PushPrintResponse {
    private Integer success;
    private String msg;
    private PushPrintResponseData data;
    private List<PrinterE> printList;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PushPrintResponseData getData() {
        return data;
    }

    public void setData(PushPrintResponseData data) {
        this.data = data;
    }

    public List<PrinterE> getPrintList() {
        return printList;
    }

    public void setPrintList(List<PrinterE> printList) {
        this.printList = printList;
    }
}
