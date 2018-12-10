package com.holderzone.intelligencepos.utils.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.blankj.utilcode.util.EncryptUtils;
import com.holderzone.intelligencepos.base.BaseApplication;

import java.lang.reflect.Method;
import java.util.Calendar;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * 设备辅助类
 * Created by tcw on 2017/4/19.
 */

public class DeviceHelper {

    private volatile static DeviceHelper sInstance;//使用volatile变量以保证是在主内存上取值

    public static DeviceHelper getInstance() {
        if (sInstance == null) {
            synchronized (DeviceHelper.class) {
                if (sInstance == null) {
                    sInstance = new DeviceHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 一体机设备ID
     */
    private String mDeviceID;

    /**
     * 登录时间
     */
    private long mLoginTime;

    public String getDeviceID() {
        if (mDeviceID == null) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                mDeviceID = (String) get.invoke(c, "ro.serialno");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mDeviceID == null) {
            mDeviceID = getDeviceID4Hardware();
        }
        return mDeviceID;
    }

    public String getLoginTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mLoginTime);
        int loginHour = calendar.get(Calendar.HOUR_OF_DAY);
        int loginMinute = calendar.get(Calendar.MINUTE);
        String loginHourStr = loginHour < 10 ? "0" + loginHour : "" + loginHour;
        String loginMinuteStr = loginMinute < 10 ? "0" + loginMinute : "" + loginMinute;
        return loginHourStr + ":" + loginMinuteStr;
    }

    public String getLoginDuration() {
        Calendar calendar = Calendar.getInstance();
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(mLoginTime);
        int loginDay = calendar.get(Calendar.DAY_OF_MONTH);
        int loginHour = calendar.get(Calendar.HOUR_OF_DAY);
        int loginMinute = calendar.get(Calendar.MINUTE);
        int totalMinute = ((nowDay - loginDay) * 24 + (nowHour - loginHour)) * 60 + (nowMinute - loginMinute);
        int hour = totalMinute / 60;
        int minute = totalMinute % 60;
        String loginHourStr = hour < 10 ? "0" + hour : "" + hour;
        String loginMinuteStr = minute < 10 ? "0" + minute : "" + minute;
        return loginHourStr + "小时" + loginMinuteStr + "分钟";
    }

    public void restoreLoginTime() {
        Calendar instance = Calendar.getInstance();
        mLoginTime = instance.getTimeInMillis();
    }

    /**
     * 生成设备唯一标识：IMEI、AndroidId、macAddress 三者拼接再 MD5
     *
     * @return
     */
    public String getDeviceID4Hardware() {
        Context context = BaseApplication.getContext().getApplicationContext();
        String imei = "";
        String androidId = "";
        String macAddress = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            imei = telephonyManager.getDeviceId();
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver != null) {
            androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            macAddress = wifiManager.getConnectionInfo().getMacAddress();
        }
        StringBuilder longIdBuilder = new StringBuilder();
        if (imei != null) {
            longIdBuilder.append(imei);
        }
        if (androidId != null) {
            longIdBuilder.append(androidId);
        }
        if (macAddress != null) {
            longIdBuilder.append(macAddress);
        }
        return longIdBuilder.length() == 0 ? "" : EncryptUtils.encryptMD5ToString(longIdBuilder.toString());
    }
}
