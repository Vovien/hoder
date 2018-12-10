package com.holderzone.intelligencepos.msg.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Created by 赵平 on 2017/3/30.
 * <p>
 */

public class MqttService extends Service {


    @Override
    public void onCreate() {
        new Thread(() -> {
            while (!MqttManager.getInstance().createConnect()) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        MqttManager.getInstance().release();
        super.onDestroy();
    }

}
