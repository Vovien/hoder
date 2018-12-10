package com.holderzone.intelligencepos.base;

/**
 * Created by tengchengwei on 2017/3/12.
 */

public class Config {

    /**
     * 百度云语音APP_ID
     */
    public static final String BAIDU_TTS_APP_ID = "11737213";

    /**
     * 腾讯bugly
     */
    public static final String BUGLY_APP_ID = "3df5f0502f";

    /**
     * 外网测试 数字摘要key
     */
    public static final String UNSIGN_KEY = "hesdjaslf54asj1fkl10jaslfjslsdlj";

    /**
     * 外网测试 Url
     */
    public static final String INTELLIGENCE_URL = "https://slbytjapi.holderzone.com/AIO/Frontend.ashx/";

    /**
     * 推送打印接口地址
     */
    public static final String PUSH_PRINT_URL = "http://msg-api-uat.holderzone.cn/api/";

    /**
     * 推送key
     */
    public static final int ALI_PUSH_KEY = 25045768;
    /**
     * 设置当前用户私有的 MQTT 的接入点。例如此处示意使用 XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台创建 MQTT 实例，每个实例都会分配一个接入点域名。
     */
    public static final String ALI_MQTT_BROKER = "tcp://post-cn-v0h0qxv7z05.mqtt.aliyuncs.com:1883";

    /**
     * 设置阿里云的 AccessKey，用于鉴权
     */
    public static final String ALI_MQTT_ACESS_KEY = "LTAIDC9IEvpIvK3u";

    /**
     * 设置阿里云的 SecretKey，用于鉴权
     */
    public static final String ALI_MQTT_SECRET_KEY = "IKgxIEvMYgupV22unKuc4SLQnsxP1k";

    /**
     * 发消息使用的一级 Topic，需要先在 MQ 控制台里创建
     */
    public static final String ALI_MQTT_TOPIC = "DeviceMessage";

    /**
     * MQTT 的 ClientID，一般由两部分组成，GroupID@@@DeviceID
     * 其中 GroupID 在 MQ 控制台里创建
     * DeviceID 由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的 ClientID 连接
     */
    public static final String ALI_MQTT_GROUP_ID = "GID_Holder_Device";
}
