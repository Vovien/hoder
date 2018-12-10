package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 打印图片
 */
public class PrinterConfigE {
    private String EnterpriseInfoGUID;
    private String ImgLogo;
    private String StoreGUID;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getImgLogo() {
        return ImgLogo;
    }

    public void setImgLogo(String imgLogo) {
        ImgLogo = imgLogo;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }
}
