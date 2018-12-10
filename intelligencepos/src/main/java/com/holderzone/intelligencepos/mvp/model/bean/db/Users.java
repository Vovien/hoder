package com.holderzone.intelligencepos.mvp.model.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29.
 */
@Entity(nameInDb = "Users")
public class Users implements Parcelable {

    @Property(nameInDb = "UsersGUID")
    private String UsersGUID;

    @Property(nameInDb = "Number")
    private String Number;

    @Property(nameInDb = "PassWord")
    private String PassWord;

    @Property(nameInDb = "Name")
    private String Name;

    @Property(nameInDb = "Tel")
    private String Tel;

    @Property(nameInDb = "Address")
    private String Address;

    @Property(nameInDb = "UserImage")
    private String UserImage;

    @Property(nameInDb = "Enabled")
    private int Enabled;

    @Property(nameInDb = "Remark")
    private String Remark;

    @Property(nameInDb = "UserType")
    private int UserType;

    @Property(nameInDb = "Sort")
    private int Sort;

    @Property(nameInDb = "UserRight")
    private String UserRight;

    public String getUsersGUID() {
        return UsersGUID;
    }

    public void setUsersGUID(String usersGUID) {
        UsersGUID = usersGUID;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public int getEnabled() {
        return Enabled;
    }

    public void setEnabled(int enabled) {
        Enabled = enabled;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getUserRight() {
        return UserRight;
    }

    public void setUserRight(String userRight) {
        UserRight = userRight;
    }

    public List<String> getUserRightList() {
        if (UserRight != null) {
            List<String> userRights = new ArrayList<>();
            Collections.addAll(userRights, UserRight.split("&"));
            return userRights;
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UsersGUID);
        dest.writeString(this.Number);
        dest.writeString(this.PassWord);
        dest.writeString(this.Name);
        dest.writeString(this.Tel);
        dest.writeString(this.Address);
        dest.writeString(this.UserImage);
        dest.writeInt(this.Enabled);
        dest.writeString(this.Remark);
        dest.writeInt(this.UserType);
        dest.writeInt(this.Sort);
        dest.writeString(this.UserRight);
    }

    protected Users(Parcel in) {
        this.UsersGUID = in.readString();
        this.Number = in.readString();
        this.PassWord = in.readString();
        this.Name = in.readString();
        this.Tel = in.readString();
        this.Address = in.readString();
        this.UserImage = in.readString();
        this.Enabled = in.readInt();
        this.Remark = in.readString();
        this.UserType = in.readInt();
        this.Sort = in.readInt();
        this.UserRight = in.readString();
    }

    @Keep
    @Generated(hash = 1155075124)
    public Users(String UsersGUID, String Number, String PassWord, String Name,
                 String Tel, String Address, String UserImage, int Enabled,
                 String Remark, int UserType, int Sort, String UserRight) {
        this.UsersGUID = UsersGUID;
        this.Number = Number;
        this.PassWord = PassWord;
        this.Name = Name;
        this.Tel = Tel;
        this.Address = Address;
        this.UserImage = UserImage;
        this.Enabled = Enabled;
        this.Remark = Remark;
        this.UserType = UserType;
        this.Sort = Sort;
        this.UserRight = UserRight;
    }

    @Keep
    @Generated(hash = 2146996206)
    public Users() {
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel source) {
            return new Users(source);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
