package com.holderzone.intelligencepos.base;

/**
 * Created by tengchengwei on 2017/3/12.
 */

public class Config {

    /**
     * 百度云语音APP_ID
     */
    public static final String BAIDU_TTS_APP_ID = "11284917";

    /**
     * 百度云语音应用API_KEY
     */
    public static final String BAIDU_TTS__API_KEY = "dmDBdkqC7vBC8YonAtctxLDr";

    /**
     * 百度云语音应用SECRET_KEY
     */
    public static final String BAIDU_TTS__SECRET_KEY = "2e0af932526b0123881fa5f298658ad8";

    /**
     * 腾讯bugly
     */
    public static final String BUGLY_APP_ID = "b0018c2531";

    /**
     * 外网正式 数字摘要key
     */
    public static final String UNSIGN_KEY = "m81LldsihJW24oKzfbX6nSfUWz764wVWTFunoEbakKZ4cA5I60kTHhGD";

    /**
     * 外网正式 Url
     */
    public static final String INTELLIGENCE_URL = "https://ytjapi.holderzone.com/AIO/Frontend.ashx/";

    /**
     * 推送打印接口地址
     */
    public static final String PUSH_PRINT_URL = "http://msg.holderzone.com/api/";

    /**
     * 推送key
     */
    public static final int ALI_PUSH_KEY = 24966619;
    /**
     * 设置当前用户私有的 MQTT 的接入点。例如此处示意使用 XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台创建 MQTT 实例，每个实例都会分配一个接入点域名。
     */
    public static final String ALI_MQTT_BROKER = "tcp://post-cn-4590rci0301.mqtt.aliyuncs.com:1883";

    /**
     * 设置阿里云的 AccessKey，用于鉴权
     */
    public static final String ALI_MQTT_ACESS_KEY = "";

    /**
     * 设置阿里云的 SecretKey，用于鉴权
     */
    public static final String ALI_MQTT_SECRET_KEY = "";

    /**
     * 发消息使用的一级 Topic，需要先在 MQ 控制台里创建
     */
    public static final String ALI_MQTT_TOPIC = "DeviceMessage_common";

    /**
     * MQTT 的 ClientID，一般由两部分组成，GroupID@@@DeviceID
     * 其中 GroupID 在 MQ 控制台里创建
     * DeviceID 由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的 ClientID 连接
     */
    public static final String ALI_MQTT_GROUP_ID = "GID_Holder_Device_common";
}
