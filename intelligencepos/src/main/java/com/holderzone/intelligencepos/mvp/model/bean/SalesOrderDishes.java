package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/1.
 */
public class SalesOrderDishes implements Parcelable {

    /**数据标识*/

    private String SalesOrderDishesGUID ;

///主表标识

    private String SalesOrderGUID ;

    private SalesOrder SalesOrder ;

    /**餐桌标识*/

    private String DiningTableGUID ;

    private DiningTable DiningTable ;

    /**菜品标识*/

    private String DishesGUID ;

    private Dishes Dishes ;

    /**单价*/

    private Double Price ;
    private Double MemberTotal ;

    /**下单总量*/

    private Double OrderCount ;

    /**出堂数量*/
    private Double MakeCount ;

    /**上菜数量*/
    private Double ServingCount ;


    /**退菜数量*/
    private Double BackCount ;

    /**结算数量(上菜完成数量，结算金额依此数量)*/
    private Double CheckCount ;


    private Double Total ;
    /**小计*/
    private Double SubTotal;

    /**赠品标识 0=非赠送 1=赠送*/

    private int Gift ;

    /**是否是套餐菜品  0=普通菜品  1=为套餐项目  2=套餐子项目*/

    private int IsPackageDishes ;

    /**套餐子项目用，保存所属的套餐项目标识*/

    private String PackageDishesKeyGUID ;



    private String OrgSalesOrderGUID ;

    /**
     * 用来保存默认的赠送状态 0=非赠送 1=赠送
     */
    private Integer defaultGift;

    public Integer getDefaultGift() {
        return defaultGift;
    }

    public void setDefaultGift(Integer defaultGift) {
        this.defaultGift = defaultGift;
    }

    public String getSalesOrderDishesGUID() {
        return SalesOrderDishesGUID;
    }

    public void setSalesOrderDishesGUID(String salesOrderDishesGUID) {
        SalesOrderDishesGUID = salesOrderDishesGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public SalesOrder getSalesOrder() {
        return SalesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        SalesOrder = salesOrder;
    }

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }

    public DiningTable getDiningTable() {
        return DiningTable;
    }

    public void setDiningTable(DiningTable diningTable) {
        DiningTable = diningTable;
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

    public Double getCheckCount() {
        return CheckCount;
    }

    public void setCheckCount(Double checkCount) {
        CheckCount = checkCount;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public int getGift() {
        return Gift;
    }

    public void setGift(int gift) {
        Gift = gift;
    }

    public Double getMemberTotal() {
        return MemberTotal;
    }

    public void setMemberTotal(Double memberTotal) {
        MemberTotal = memberTotal;
    }

    public int getIsPackageDishes() {
        return IsPackageDishes;
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

    public String getOrgSalesOrderGUID() {
        return OrgSalesOrderGUID;
    }

    public void setOrgSalesOrderGUID(String orgSalesOrderGUID) {
        OrgSalesOrderGUID = orgSalesOrderGUID;
    }

    public Double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(Double subTotal) {
        SubTotal = subTotal;
    }

    public SalesOrderDishes() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SalesOrderDishesGUID);
        dest.writeString(this.SalesOrderGUID);
        dest.writeParcelable(this.SalesOrder, flags);
        dest.writeString(this.DiningTableGUID);
        dest.writeParcelable(this.DiningTable, flags);
        dest.writeString(this.DishesGUID);
        dest.writeParcelable(this.Dishes, flags);
        dest.writeValue(this.Price);
        dest.writeValue(this.MemberTotal);
        dest.writeValue(this.OrderCount);
        dest.writeValue(this.MakeCount);
        dest.writeValue(this.ServingCount);
        dest.writeValue(this.BackCount);
        dest.writeValue(this.CheckCount);
        dest.writeValue(this.Total);
        dest.writeValue(this.SubTotal);
        dest.writeInt(this.Gift);
        dest.writeInt(this.IsPackageDishes);
        dest.writeString(this.PackageDishesKeyGUID);
        dest.writeString(this.OrgSalesOrderGUID);
        dest.writeValue(this.defaultGift);
    }

    protected SalesOrderDishes(Parcel in) {
        this.SalesOrderDishesGUID = in.readString();
        this.SalesOrderGUID = in.readString();
        this.SalesOrder = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.SalesOrder.class.getClassLoader());
        this.DiningTableGUID = in.readString();
        this.DiningTable = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DiningTable.class.getClassLoader());
        this.DishesGUID = in.readString();
        this.Dishes = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.Dishes.class.getClassLoader());
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        this.MemberTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.OrderCount = (Double) in.readValue(Double.class.getClassLoader());
        this.MakeCount = (Double) in.readValue(Double.class.getClassLoader());
        this.ServingCount = (Double) in.readValue(Double.class.getClassLoader());
        this.BackCount = (Double) in.readValue(Double.class.getClassLoader());
        this.CheckCount = (Double) in.readValue(Double.class.getClassLoader());
        this.Total = (Double) in.readValue(Double.class.getClassLoader());
        this.SubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.Gift = in.readInt();
        this.IsPackageDishes = in.readInt();
        this.PackageDishesKeyGUID = in.readString();
        this.OrgSalesOrderGUID = in.readString();
        this.defaultGift = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SalesOrderDishes> CREATOR = new Creator<SalesOrderDishes>() {
        @Override
        public SalesOrderDishes createFromParcel(Parcel source) {
            return new SalesOrderDishes(source);
        }

        @Override
        public SalesOrderDishes[] newArray(int size) {
            return new SalesOrderDishes[size];
        }
    };
}
