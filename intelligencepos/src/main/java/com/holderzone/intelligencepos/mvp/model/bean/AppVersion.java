package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by Administrator on 2016-11-3.
 */
public class AppVersion {
    private Integer NewCode;
    private String NewName;
    private String Content;
    private Integer MinCode;
    private Integer VersionType;
    private Integer AppVersionID;
    private String AppVersionUID;
    private String AppVersionGUID;
    private String VersionTypeName;
    private Integer DownType;
    private String DownUrl;

    public Integer getNewCode() {
        return NewCode;
    }

    public void setNewCode(Integer newCode) {
        NewCode = newCode;
    }

    public String getNewName() {
        return NewName;
    }

    public void setNewName(String newName) {
        NewName = newName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getMinCode() {
        return MinCode;
    }

    public void setMinCode(Integer minCode) {
        MinCode = minCode;
    }

    public Integer getVersionType() {
        return VersionType;
    }

    public void setVersionType(Integer versionType) {
        VersionType = versionType;
    }

    public Integer getAppVersionID() {
        return AppVersionID;
    }

    public void setAppVersionID(Integer appVersionID) {
        AppVersionID = appVersionID;
    }

    public String getAppVersionUID() {
        return AppVersionUID;
    }

    public void setAppVersionUID(String appVersionUID) {
        AppVersionUID = appVersionUID;
    }

    public String getAppVersionGUID() {
        return AppVersionGUID;
    }

    public void setAppVersionGUID(String appVersionGUID) {
        AppVersionGUID = appVersionGUID;
    }

    public String getVersionTypeName() {
        return VersionTypeName;
    }

    public void setVersionTypeName(String versionTypeName) {
        VersionTypeName = versionTypeName;
    }

    public Integer getDownType() {
        return DownType;
    }

    public void setDownType(Integer downType) {
        DownType = downType;
    }

    public String getDownUrl() {
        return DownUrl;
    }

    public void setDownUrl(String downUrl) {
        DownUrl = downUrl;
    }
}
