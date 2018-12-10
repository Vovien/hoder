package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/31.
 */
public class SalesOrderBatchDishes implements Parcelable {
    /**
     * 数据标识
     */
    private String SalesOrderBatchDishesGUID;

    /**
     * 所属批次标识
     */
    private String SalesOrderBatchGUID;
    private Double MemberTotal;

    /**
     * 销售单标识
     */
    private String SalesOrderGUID;

    //餐桌标识
    private String DiningTableGUID;

    /**
     * 菜品标识
     */
    private String DishesGUID;

    /**
     * 所属的菜品实体
     */
    private Dishes Dishes;
    /**
     * 会员价
     */
    private Double MemberPrice;

    /**
     * 价单
     */
    private Double Price;

    /**
     * 下单数量 正数是加菜，负数是退菜
     */
    private Double OrderCount;

    /**
     * 制作数量
     */
    private Double MakeCount;

    /**
     * 上菜数量
     */
    private Double ServingCount;

    /**
     * 退菜数量
     */
    private Double BackCount;

    /**
     * 赠品标识 0=非赠送 1=赠送
     */
    private Integer Gift;

    /**
     * 开始制作时间（系统时间达到此时间后才能显示入内堂）
     */
    private String StartMakeTime;

    /**
     * 菜品备注，可存入菜品制作要求等
     */
    private String Remark;

    /**
     * 是否是套餐菜品  0=普通菜品  1=为套餐项目  2=套餐子项目
     */
    private Integer IsPackageDishes;

    /**
     * 当前菜品为套餐项目
     */
    private String PackageDishesKeyGUID;

    /**
     * 套餐项目时，套餐构成的单位数量
     */
    private Double PackageDishesUnitCount;

    /**
     * 销售单类别
     */
    private String SalesOrderTypeCode;

    private Integer Sort;

    /**
     * 扩展数据，用于临时性业务
     */
    private String ExData;

    /**
     * 扩展值，用于临时业务
     */
    private Double ExValue;

    private String getTableInfo() {
        return tableInfo;
    }

    private void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }

    private String tableInfo;
    /**
     * 厨房打印状态，在叫起状态下此状态由客户端发起控制，是否进行对本菜品打印 0=不打印  1=打印
     */
    private Integer KitchenPrintStatus;
    /**
     * 菜品状态，叫起/挂起 0=挂起  1=叫起
     */
    private Integer CheckStatus;

    /**
     * 叫起页面是否选择
     */
    private boolean selected;
    /**
     * 菜品拆分信息2017-01-19
     */
    private ArrayList<SeparateDishes> separateDishesList = new ArrayList<>();

    public String getSalesOrderBatchDishesGUID() {
        return SalesOrderBatchDishesGUID;
    }

    public void setSalesOrderBatchDishesGUID(String salesOrderBatchDishesGUID) {
        SalesOrderBatchDishesGUID = salesOrderBatchDishesGUID;
    }

    public String getSalesOrderBatchGUID() {
        return SalesOrderBatchGUID;
    }

    public void setSalesOrderBatchGUID(String salesOrderBatchGUID) {
        SalesOrderBatchGUID = salesOrderBatchGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }

    public String getDishesGUID() {
        return DishesGUID;
    }

    public void setDishesGUID(String dishesGUID) {
        DishesGUID = dishesGUID;
    }

    public Dishes getDishes() {
        return Dishes;
    }

    public void setDishes(Dishes dishes) {
        Dishes = dishes;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(Double orderCount) {
        OrderCount = orderCount;
    }

    public Double getMakeCount() {
        return MakeCount;
    }

    public void setMakeCount(Double makeCount) {
        MakeCount = makeCount;
    }

    public Double getServingCount() {
        return ServingCount;
    }

    public void setServingCount(Double servingCount) {
        ServingCount = servingCount;
    }

    public Double getBackCount() {
        return BackCount;
    }

    public void setBackCount(Double backCount) {
        BackCount = backCount;
    }

    public Integer getGift() {
        return Gift;
    }

    public void setGift(int gift) {
        Gift = gift;
    }

    public String getStartMakeTime() {
        return StartMakeTime;
    }

    public void setStartMakeTime(String startMakeTime) {
        StartMakeTime = startMakeTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Integer getIsPackageDishes() {
        return IsPackageDishes;
    }

    public Double getMemberTotal() {
        return MemberTotal;
    }

    public void setMemberTotal(Double memberTotal) {
        MemberTotal = memberTotal;
    }

    public void setIsPackageDishes(int isPackageDishes) {
        IsPackageDishes = isPackageDishes;
    }

    public String getPackageDishesKeyGUID() {
        return PackageDishesKeyGUID;
    }

    public void setPackageDishesKeyGUID(String packageDishesKeyGUID) {
        PackageDishesKeyGUID = packageDishesKeyGUID;
    }

    public Double getPackageDishesUnitCount() {
        return PackageDishesUnitCount;
    }

    public void setPackageDishesUnitCount(Double packageDishesUnitCount) {
        PackageDishesUnitCount = packageDishesUnitCount;
    }

    public Double getMemberPrice() {
        return MemberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        MemberPrice = memberPrice;
    }

    public void setGift(Integer gift) {
        Gift = gift;
    }

    public void setIsPackageDishes(Integer isPackageDishes) {
        IsPackageDishes = isPackageDishes;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }

    public String getSalesOrderTypeCode() {
        return SalesOrderTypeCode;
    }

    public void setSalesOrderTypeCode(String salesOrderTypeCode) {
        SalesOrderTypeCode = salesOrderTypeCode;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getExData() {
        return ExData;
    }

    public void setExData(String exData) {
        ExData = exData;
    }

    public Double getExValue() {
        return ExValue;
    }

    public void setExValue(Double exValue) {
        ExValue = exValue;
    }

    public Integer getKitchenPrintStatus() {
        return KitchenPrintStatus;
    }

    public void setKitchenPrintStatus(Integer kitchenPrintStatus) {
        KitchenPrintStatus = kitchenPrintStatus;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<SeparateDishes> getSeparateDishesList() {
        return separateDishesList;
    }

    public void setSeparateDishesList(ArrayList<SeparateDishes> separateDishesList) {
        this.separateDishesList = separateDishesList;
    }

    public Integer getCheckStatus() {
        return CheckStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        CheckStatus = checkStatus;
    }

    public SalesOrderBatchDishes() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SalesOrderBatchDishesGUID);
        dest.writeString(this.SalesOrderBatchGUID);
        dest.writeValue(this.MemberTotal);
        dest.writeString(this.SalesOrderGUID);
        dest.writeString(this.DiningTableGUID);
        dest.writeString(this.DishesGUID);
        dest.writeParcelable(this.Dishes, flags);
        dest.writeValue(this.MemberPrice);
        dest.writeValue(this.Price);
        dest.writeValue(this.OrderCount);
        dest.writeValue(this.MakeCount);
        dest.writeValue(this.ServingCount);
        dest.writeValue(this.BackCount);
        dest.writeValue(this.Gift);
        dest.writeString(this.StartMakeTime);
        dest.writeString(this.Remark);
        dest.writeValue(this.IsPackageDishes);
        dest.writeString(this.PackageDishesKeyGUID);
        dest.writeValue(this.PackageDishesUnitCount);
        dest.writeString(this.SalesOrderTypeCode);
        dest.writeValue(this.Sort);
        dest.writeString(this.ExData);
        dest.writeValue(this.ExValue);
        dest.writeString(this.tableInfo);
        dest.writeValue(this.KitchenPrintStatus);
        dest.writeValue(this.CheckStatus);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeList(this.separateDishesList);
    }

    protected SalesOrderBatchDishes(Parcel in) {
        this.SalesOrderBatchDishesGUID = in.readString();
        this.SalesOrderBatchGUID = in.readString();
        this.MemberTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.SalesOrderGUID = in.readString();
        this.DiningTableGUID = in.readString();
        this.DishesGUID = in.readString();
        this.Dishes = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.Dishes.class.getClassLoader());
        this.MemberPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        this.OrderCount = (Double) in.readValue(Double.class.getClassLoader());
        this.MakeCount = (Double) in.readValue(Double.class.getClassLoader());
        this.ServingCount = (Double) in.readValue(Double.class.getClassLoader());
        this.BackCount = (Double) in.readValue(Double.class.getClassLoader());
        this.Gift = (Integer) in.readValue(Integer.class.getClassLoader());
        this.StartMakeTime = in.readString();
        this.Remark = in.readString();
        this.IsPackageDishes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PackageDishesKeyGUID = in.readString();
        this.PackageDishesUnitCount = (Double) in.readValue(Double.class.getClassLoader());
        this.SalesOrderTypeCode = in.readString();
        this.Sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ExData = in.readString();
        this.ExValue = (Double) in.readValue(Double.class.getClassLoader());
        this.tableInfo = in.readString();
        this.KitchenPrintStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CheckStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.selected = in.readByte() != 0;
        this.separateDishesList = new ArrayList<SeparateDishes>();
        in.readList(this.separateDishesList, SeparateDishes.class.getClassLoader());
    }

    public static final Creator<SalesOrderBatchDishes> CREATOR = new Creator<SalesOrderBatchDishes>() {
        @Override
        public SalesOrderBatchDishes createFromParcel(Parcel source) {
            return new SalesOrderBatchDishes(source);
        }

        @Override
        public SalesOrderBatchDishes[] newArray(int size) {
            return new SalesOrderBatchDishes[size];
        }
    };
}
