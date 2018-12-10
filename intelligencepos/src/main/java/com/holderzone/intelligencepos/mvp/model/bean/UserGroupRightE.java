package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by chencao on 2017/9/27.
 */

public class UserGroupRightE {
    /**
     * 权限码
     */
    private String UserRightUID;
    /**
     * 权限名称
     */
    private String Name;

    public String getUserRightUID() {
        return UserRightUID;
    }

    public void setUserRightUID(String userRightUID) {
        UserRightUID = userRightUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
