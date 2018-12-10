package com.holderzone.intelligencepos.mvp.model.bean;

import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;

/**
 * Created by tcw on 2017/3/10.
 */

public class EnterpriseInfoE extends EnterpriseInfo {

    /**
     * 验证激活码，商家手机收到的验证码
     */
    private String Code;

    /**
     * 终端设备ID
     */
    private String DeviceID;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}

