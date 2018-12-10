package com.holderzone.intelligencepos.mvp.model.bean;

import com.holderzone.intelligencepos.mvp.model.bean.db.Store;

/**
 * Created by tcw on 2017/3/10.
 */

public class StoreE extends Store {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 终端设备硬件唯一标识
     */
    private String TerminalID;
    /**
     * 地址
     */
    private String Address;


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }
}
