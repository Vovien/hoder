package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 配置信息实体
 * Created by chencao on 2018/1/15.
 */

public class SysParametersE {
    /**
     * 企业GUID
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店GUID
     */
    private String StoreGUID;
    /**
     * 参数类型 1：平板下单密码参数设置 2:划菜配置
     */
    private Integer ParType;
    /**
     * 状态 0：停用 1：启用
     */
    private Integer State;

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

    public Integer getParType() {
        return ParType;
    }

    public void setParType(Integer parType) {
        ParType = parType;
    }

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
    }
}
