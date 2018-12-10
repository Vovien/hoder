package com.holderzone.intelligencepos.base;

/**
 * Created by zhaoping on 2018/5/9.
 */
public class Constants {
    public static final long GUEST_SCREEN_ALTERNATE_TIME = 4000L;
    public static final int AK390DT_SEND_DATA_TIMEOUT = 60 * 10000;
    public static final String T1MINI = "T1mini";
    public static final int SUNMI_LOCAL_PRINT_CHECK_EXCEPTION_TIME = 1 * 5 * 1000;

    //网络连接类型相关
    public static final int NETWORK_CONNECT_STATE_SUCCESS = 0;
    public static final int NETWORK_CONNECT_STATE_FAIL = 1;
    public static final int CNETWORK_CONNECT_STATE_INSTABLE = 2;

    //
    public static final int PUSH_REALM_VERSION = 1;
    public static final String PUSH_REALM_NAME = "push.realm";
    public static final int PUSH_MESSAGE_SAVE_TIME_MILLIS = 2 * 60 * 60 * 1000;


    public static final String EXTRA_NEED_REFRESH_BACK_VIEW = "REFRESH_BACK_VIEW";
    public static final String EXTRAS_MEMBER_INFO = "EXTRAS_MEMBER_INFO";
    //
}
