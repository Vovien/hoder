package com.holderzone.intelligencepos.msg.mqtt;

import android.util.Log;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.holderzone.intelligencepos.base.Config;
import com.holderzone.intelligencepos.msg.MsgHandler;
import com.holderzone.intelligencepos.mvp.model.Repository;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 管理mqtt的连接,发布,订阅,断开连接, 断开重连等操作
 *
 * @author LichFaker on 16/3/24.
 * @Email lichfaker@gmail.com
 */
public class MqttManager {

    /**
     * MQTT 的 ClientID，一般由两部分组成，GroupID@@@DeviceID
     * 其中 GroupID 在 MQ 控制台里创建
     * DeviceID 由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的 ClientID 连接
     */
    final String clientId = Config.ALI_MQTT_GROUP_ID + "@@@" + DeviceHelper.getInstance().getDeviceID();
    private static final String TAG = "MqttManager";
    // 单例
    private static MqttManager mInstance = null;

    // 回调
    private MqttCallback mCallback;

    // Private instance variables
    private MqttClient client;
    private MqttConnectOptions conOpt;
    private boolean clean = false;
    static Disposable disposable;
    private String storeTopic = null;

    final int[] qos = new int[]{1};

    private MqttManager() {
        mCallback = new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (client != null && client.isConnected()) {
                    Repository repository = RepositoryImpl.getInstance();
                    Observable.zip(repository.getEnterpriseInfo(), repository.getStore(), (enterpriseInfo, store) -> EncryptUtils.encryptMD5ToString((enterpriseInfo.getEnterpriseInfoGUID() + store.getStoreGUID()).toLowerCase()).toUpperCase()).subscribe(s -> {
                        storeTopic = s;
                        String[] topicFilters = new String[]{Config.ALI_MQTT_TOPIC + "/" + s + "/"};
                        try {
                            client.subscribe(topicFilters, qos);
                            getOfflineMsg();
                        } catch (MqttException e) {
                        }
                    });

                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                if (cause instanceof MqttException) {
                    LogUtils.e("连接超时。");
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Observable.zip(RepositoryImpl.getInstance().getEnterpriseInfo(), RepositoryImpl.getInstance().getStore(), (enterpriseInfo, store) -> EncryptUtils.encryptMD5ToString((enterpriseInfo.getEnterpriseInfoGUID() + store.getStoreGUID()).toLowerCase()).toUpperCase()).subscribe(s -> {
                    if (topic.startsWith(Config.ALI_MQTT_TOPIC + "/p2p/") || topic.contains(s)) {
                        Observable.create(e -> MsgHandler.handlerMsg(message.toString())).subscribe(o -> {
                        }, throwable -> throwable.printStackTrace());
                    } else {
                        unsubscribe(topic);
                    }
                }, throwable -> {
                    LogUtils.e(throwable.getStackTrace());
                });
                LogUtils.d(topic + "====" + message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                LogUtils.d("deliveryComplete====");
            }
        };
    }

    public static ExecutorService MQTT_THREAD_POOL = Executors.newFixedThreadPool(1);

    private void unsubscribe(String topic) {
        if (client != null && client.isConnected()) {
            Completable.fromAction(() -> {
                try {
                    client.unsubscribe(new String[]{topic});
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }).subscribeOn(Schedulers.from(MQTT_THREAD_POOL)).subscribe(() -> {
            }, throwable -> throwable.printStackTrace());

        }
    }

    public static MqttManager getInstance() {
        if (null == mInstance) {
            mInstance = new MqttManager();
        }
        return mInstance;
    }

    public void getOfflineMsg() {
        if (disposable != null) {
            disposable = Observable.interval(30, 60, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (client != null && client.isConnected()) {
                                MqttMessage mqttMessage = new MqttMessage("{\"maxPushNum\":30,\"pushOrder\":\"DESC\"}".toString().getBytes());
                                mqttMessage.setQos(1);
                                try {
                                    client.publish("$SYS/getOfflineMsg", mqttMessage);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
        }
    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 创建Mqtt 连接
     *
     * @return
     */
    public boolean createConnect() {
        boolean flag = false;
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
        try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            conOpt = new MqttConnectOptions();
            conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            conOpt.setAutomaticReconnect(true);
            conOpt.setCleanSession(clean);
            String sign = MacSignature.macSignature(clientId.split("@@@")[0], Config.ALI_MQTT_SECRET_KEY);
            if (sign != null) {
                conOpt.setPassword(sign.toCharArray());
            }
            conOpt.setUserName(Config.ALI_MQTT_ACESS_KEY);
            // Construct an MQTT blocking mode client
            client = new MqttClient(Config.ALI_MQTT_BROKER, clientId, dataStore);
            // Set this wrapper as the callback handler
            client.setCallback(mCallback);
            flag = doConnect();
        } catch (MqttException e) {
            LogUtils.e(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e(e.getMessage());
        } catch (InvalidKeyException e) {
            LogUtils.e(e.getMessage());
        }
        return flag;
    }

    /**
     * 建立连接
     *
     * @return
     */
    public boolean doConnect() {
        boolean flag = false;
        if (client != null) {
            try {
                client.connect(conOpt);
                LogUtils.d("Connected to " + client.getServerURI() + " with client ID " + client.getClientId());
                flag = true;
            } catch (Exception e) {
                LogUtils.e("Connected error  " + e.getMessage());
            }
        }
        return flag;
    }

    /**
     * Publish / send a message to an MQTT server
     *
     * @param topicName the name of the topic to publish to
     * @param qos       the quality of service to delivery the message at (0,1,2)
     * @param payload   the set of bytes to send to the MQTT server
     * @return boolean
     */
    public boolean publish(String topicName, int qos, byte[] payload) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            LogUtils.d("Publishing to topic \"" + topicName + "\" qos " + qos);
            // Create and configure a message
            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);
            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                client.publish(topicName, message);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     *
     * @param topicName to subscribe to (can be wild carded)
     * @param qos       the maximum quality of service to receive messages at for this subscription
     * @return boolean
     */
    public boolean subscribe(String topicName, int qos) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            LogUtils.d("Subscribing to topic \"" + topicName + "\" qos " + qos);
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }

    public boolean subscribe(String[] topicName, int[] qos) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            Log.d(TAG, "Subscribing to topic \"" + topicName + "\" qos " + qos);
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }

    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
        if (disposable != null && disposable.isDisposed() == false) {
            disposable.dispose();
            disposable = null;
        }
    }
}