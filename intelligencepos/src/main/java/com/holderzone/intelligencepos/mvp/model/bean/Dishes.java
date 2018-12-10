package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29.
 */
public class Dishes implements Parcelable {

    /**
     * 自增ID
     */
    private Integer DishesID;

    /**
     * 编号
     */
    private String DishesUID;

    /**
     * 主键
     */
    private String DishesGUID;

    /**
     * 是否删除
     */
    private Integer IsDelete;
    /**会员价*/
    private Double MemberPrice;

    /**
     * 编码
     */
    private String Code;

    /**
     * 做法名称
     */
//    private String Name;

    /**
     * 简称
     */
    private String SimpleName;

    /**
     * 拼音索引
     */
    private String PYIndex;

    /**
     * 品种类型标识
     */
    private String DishesTypeGUID;

    /**
     * 品种类型
     */
    private DishesType DishesType;

    /**
     * 结算单位
     */
    private String CheckUnit;

    /**
     * 结算单价
     */
    private Double CheckPrice;

    /**
     * 是否可以作为礼品赠送  0=否  1=是
     */
    private Integer Gift;

    /**
     * 是否参加优惠活动  0=不参与  1=参与
     */
    private Integer JoinDiscount;

    /**
     * 提成方式   0=不提成  1=固定金额提成 2=按比例
     */
    private Integer CommissionsType;

    /**
     * 提成值
     */
    private Double CommissionsValue;

    /**
     * 序号
     */
    private Integer Sort;

    /**
     * 状态 1=启用 0=停用
     */
    private Integer Enabled;

    /**
     * 下单后自动上菜
     */
    private Integer AutoServing;

    /**
     * 菜品描述
     */
    private String Description;

    /**
     * 同步时间
     */
    private Integer LastStamp;

    /**
     * 菜品图片
     */
    private String DishesImage;
    /**
     * 是否为套餐菜品
     * 0：普通菜品
     * 1：套餐
     */
    private Integer IsPackageDishes;
    /**
     * 是否在平板上显示
     * 0 不显示
     * 1 显示
     */
    private Integer IsShowPad;

    /**
     * 估清数量
     */
    private Integer EstimateCount;

    /**
     * 售出数据，用于装载计算数据
     */
    private Integer SalesCount;

    /**
     * 警告值
     */
    private Integer WarningCount;

    /**
     * 此字段内为本地菜品图片的url
     */
    private String LocalDishesUrl ;

    /**
     * 套餐子项菜品
     */
    private List<PackageItem> ArrayOfPackageItem;

    /**
     * 该套餐子项的数量
     */
    private Integer DishesCount = 1;

    /**厨房打印机guid   app无用*/
    private String KitchenPrintGUID ;
    /**厨房打印机区域guid   app无用*/
    private String KitchenAreaGUID ;

    public Integer getDishesID() {
        return DishesID;
    }

    public void setDishesID(Integer dishesID) {
        DishesID = dishesID;
    }

    public String getDishesUID() {
        return DishesUID;
    }

    public void setDishesUID(String dishesUID) {
        DishesUID = dishesUID;
    }

    public String getDishesGUID() {
        return DishesGUID;
    }

    public void setDishesGUID(String dishesGUID) {
        DishesGUID = dishesGUID;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }

    public String getSimpleName() {
        return SimpleName;
    }

    public void setSimpleName(String simpleName) {
        SimpleName = simpleName;
    }

    public String getPYIndex() {
        return PYIndex;
    }

    public void setPYIndex(String PYIndex) {
        this.PYIndex = PYIndex;
    }

    public String getDishesTypeGUID() {
        return DishesTypeGUID;
    }

    public void setDishesTypeGUID(String dishesTypeGUID) {
        DishesTypeGUID = dishesTypeGUID;
    }

    public DishesType getDishesType() {
        return DishesType;
    }

    public void setDishesType(DishesType dishesType) {
        DishesType = dishesType;
    }

    public String getCheckUnit() {
        return CheckUnit;
    }

    public void setCheckUnit(String checkUnit) {
        CheckUnit = checkUnit;
    }

    public Double getCheckPrice() {
        return CheckPrice;
    }

    public void setCheckPrice(Double checkPrice) {
        CheckPrice = checkPrice;
    }

    public Integer getGift() {
        return Gift;
    }

    public void setGift(Integer gift) {
        Gift = gift;
    }

    public Integer getJoinDiscount() {
        return JoinDiscount;
    }

    public void setJoinDiscount(Integer joinDiscount) {
        JoinDiscount = joinDiscount;
    }

    public Integer getCommissionsType() {
        return CommissionsType;
    }

    public void setCommissionsType(Integer commissionsType) {
        CommissionsType = commissionsType;
    }

    public Double getCommissionsValue() {
        return CommissionsValue;
    }

    public void setCommissionsValue(Double commissionsValue) {
        CommissionsValue = commissionsValue;
    }

    public Double getMemberPrice() {
        return MemberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        MemberPrice = memberPrice;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public Integer getEnabled() {
        return Enabled;
    }

    public void setEnabled(Integer enabled) {
        Enabled = enabled;
    }

    public Integer getAutoServing() {
        return AutoServing;
    }

    public void setAutoServing(Integer autoServing) {
        AutoServing = autoServing;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getLastStamp() {
        return LastStamp;
    }

    public void setLastStamp(Integer lastStamp) {
        LastStamp = lastStamp;
    }

    public String getDishesImage() {
        return DishesImage;
    }

    public void setDishesImage(String dishesImage) {
        DishesImage = dishesImage;
    }

    public Integer getIsPackageDishes() {
        return IsPackageDishes;
    }

    public void setIsPackageDishes(Integer isPackageDishes) {
        IsPackageDishes = isPackageDishes;
    }

    public Integer getIsShowPad() {
        return IsShowPad;
    }

    public void setIsShowPad(Integer isShowPad) {
        IsShowPad = isShowPad;
    }

    public Integer getEstimateCount() {
        return EstimateCount;
    }

    public void setEstimateCount(Integer estimateCount) {
        EstimateCount = estimateCount;
    }

    public Integer getSalesCount() {
        return SalesCount;
    }

    public void setSalesCount(Integer salesCount) {
        SalesCount = salesCount;
    }

    public Integer getWarningCount() {
        return WarningCount;
    }

    public void setWarningCount(Integer warningCount) {
        WarningCount = warningCount;
    }

    public String getLocalDishesUrl() {
        return LocalDishesUrl;
    }

    public void setLocalDishesUrl(String localDishesUrl) {
        LocalDishesUrl = localDishesUrl;
    }

    public List<PackageItem> getArrayOfPackageItem() {
        return ArrayOfPackageItem;
    }

    public void setArrayOfPackageItem(List<PackageItem> arrayOfPackageItem) {
        ArrayOfPackageItem = arrayOfPackageItem;
    }

    public Integer getDishesCount() {
        return DishesCount;
    }

    public void setDishesCount(Integer dishesCount) {
        DishesCount = dishesCount;
    }

    public String getKitchenPrintGUID() {
        return KitchenPrintGUID;
    }

    public void setKitchenPrintGUID(String kitchenPrintGUID) {
        KitchenPrintGUID = kitchenPrintGUID;
    }

    public String getKitchenAreaGUID() {
        return KitchenAreaGUID;
    }

    public void setKitchenAreaGUID(String kitchenAreaGUID) {
        KitchenAreaGUID = kitchenAreaGUID;
    }

    public Dishes() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.DishesID);
        dest.writeString(this.DishesUID);
        dest.writeString(this.DishesGUID);
        dest.writeValue(this.IsDelete);
        dest.writeValue(this.MemberPrice);
        dest.writeString(this.Code);
        dest.writeString(this.SimpleName);
        dest.writeString(this.PYIndex);
        dest.writeString(this.DishesTypeGUID);
        dest.writeSerializable(this.DishesType);
        dest.writeString(this.CheckUnit);
        dest.writeValue(this.CheckPrice);
        dest.writeValue(this.Gift);
        dest.writeValue(this.JoinDiscount);
        dest.writeValue(this.CommissionsType);
        dest.writeValue(this.CommissionsValue);
        dest.writeValue(this.Sort);
        dest.writeValue(this.Enabled);
        dest.writeValue(this.AutoServing);
        dest.writeString(this.Description);
        dest.writeValue(this.LastStamp);
        dest.writeString(this.DishesImage);
        dest.writeValue(this.IsPackageDishes);
        dest.writeValue(this.IsShowPad);
        dest.writeValue(this.EstimateCount);
        dest.writeValue(this.SalesCount);
        dest.writeValue(this.WarningCount);
        dest.writeString(this.LocalDishesUrl);
        dest.writeList(this.ArrayOfPackageItem);
        dest.writeValue(this.DishesCount);
        dest.writeString(this.KitchenPrintGUID);
        dest.writeString(this.KitchenAreaGUID);
    }

    protected Dishes(Parcel in) {
        this.DishesID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DishesUID = in.readString();
        this.DishesGUID = in.readString();
        this.IsDelete = (Integer) in.readValue(Integer.class.getClassLoader());
        this.MemberPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.Code = in.readString();
        this.SimpleName = in.readString();
        this.PYIndex = in.readString();
        this.DishesTypeGUID = in.readString();
        this.DishesType = (com.holderzone.intelligencepos.mvp.model.bean.DishesType) in.readSerializable();
        this.CheckUnit = in.readString();
        this.CheckPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.Gift = (Integer) in.readValue(Integer.class.getClassLoader());
        this.JoinDiscount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CommissionsType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CommissionsValue = (Double) in.readValue(Double.class.getClassLoader());
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Enabled = (Integer) in.readValue(Integer.class.getClassLoader());
        this.AutoServing = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Description = in.readString();
        this.LastStamp = (Integer) in.readValue(Integer.class.getClassLoader());
        this.DishesImage = in.readString();
        this.IsPackageDishes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsShowPad = (Integer) in.readValue(Integer.class.getClassLoader());
        this.EstimateCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SalesCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.WarningCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.LocalDishesUrl = in.readString();
        this.ArrayOfPackageItem = new ArrayList<PackageItem>();
        in.readList(this.ArrayOfPackageItem, PackageItem.class.getClassLoader());
        this.DishesCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.KitchenPrintGUID = in.readString();
        this.KitchenAreaGUID = in.readString();
    }

    public static final Creator<Dishes> CREATOR = new Creator<Dishes>() {
        @Override
        public Dishes createFromParcel(Parcel source) {
            return new Dishes(source);
        }

        @Override
        public Dishes[] newArray(int size) {
            return new Dishes[size];
        }
    };
}
