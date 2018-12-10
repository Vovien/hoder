package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.holderzone.intelligencepos.mvp.model.bean.db.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw on 2017/3/10.
 */

public class UsersE extends Users implements Parcelable{
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGUID;

    /**
     * 原密码，用于验证
     */
    private String OrgPassword;

    /**
     * 设置的新密码
     */
    private String NewPassword;

    /**
     * 用户权限组
     */
    private UserGroupE UserGroupE;

    /**
     * 用户权限列表
     */
    private List<UserGroupRightE> ArrayofUserGroupRightE;

    /**
     * 版本号
     */
    private Integer VersionCode;

    /**
     * 版本名
     */
    private String VersionName;

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

    public String getOrgPassword() {
        return OrgPassword;
    }

    public void setOrgPassword(String orgPassword) {
        OrgPassword = orgPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public UserGroupE getUserGroupE() {
        return UserGroupE;
    }

    public void setUserGroupE(UserGroupE userGroupE) {
        UserGroupE = userGroupE;
    }

    public List<UserGroupRightE> getArrayofUserGroupRightE() {
        return ArrayofUserGroupRightE;
    }

    public void setArrayofUserGroupRightE(List<UserGroupRightE> arrayofUserGroupRightE) {
        ArrayofUserGroupRightE = arrayofUserGroupRightE;
    }

    public Integer getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(Integer versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public UsersE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.OrgPassword);
        dest.writeString(this.NewPassword);
        dest.writeParcelable(this.UserGroupE, flags);
        dest.writeList(this.ArrayofUserGroupRightE);
        dest.writeInt(this.VersionCode);
        dest.writeString(this.VersionName);
    }

    protected UsersE(Parcel in) {
        super(in);
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.OrgPassword = in.readString();
        this.NewPassword = in.readString();
        this.UserGroupE = in.readParcelable(UserGroupE.class.getClassLoader());
        this.ArrayofUserGroupRightE = new ArrayList<>();
        in.readList(this.ArrayofUserGroupRightE, UserGroupRightE.class.getClassLoader());
        this.VersionCode = in.readInt();
        this.VersionName = in.readString();
    }

    public static final Creator<UsersE> CREATOR = new Creator<UsersE>() {
        @Override
        public UsersE createFromParcel(Parcel source) {
            return new UsersE(source);
        }

        @Override
        public UsersE[] newArray(int size) {
            return new UsersE[size];
        }
    };
}
