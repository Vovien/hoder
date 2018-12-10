package com.holderzone.intelligencepos.utils.print;

/**
 * Created by tcw on 2017/10/16.
 */

public class PrintException extends Exception {
    public static final int MESSAGE_TYPE_LOCAL_PRINT_NOT_FIND = 2;
    public static final int MESSAGE_TYPE_LOCAL_PRINT_NOT_CONNECT = 3;
    public static final int MESSAGE_TYPE_NET_PRINT_FAILED = 4;
    public static final int MESSAGE_TYPE_LABEL_PRINT_FAILED = 5;

    private int mType;

    public PrintException(String message, int type) {
        super(message);
        this.mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }
}
