package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by terry on 17-11-23.
 */

public class SalesMsgReadRecordE {

    /**
     * 企业guid
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店guid
     */
    private String StoreGUID;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }
}
