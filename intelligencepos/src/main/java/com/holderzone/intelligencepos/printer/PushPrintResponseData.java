package com.holderzone.intelligencepos.printer;

import java.util.List;

/**
 * Created by zhaoping on 2018/6/27.
 */
public class PushPrintResponseData {
    private List<String> list;
    private List<String> not;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getNot() {
        return not;
    }

    public void setNot(List<String> not) {
        this.not = not;
    }
}
