package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw10 on 2017/3/10.
 */

public class SalesOrderBatchDishesE extends SalesOrderBatchDishes implements Parcelable {

    /**
     * 修改有做法菜品数量对话框时用到
     */
    private Double tempOrderCount;
    /**
     * 是否是新加菜
     */
    private Boolean isNew;

    public String getDishesUnit() {
        return dishesUnit;
    }

    public void setDishesUnit(String dishesUnit) {
        this.dishesUnit = dishesUnit;
    }

    private String dishesUnit;
    private String dishesName;
    /**
     * 对单数量
     */
    private Double ReviewCheckCount;
    /**
     * 折扣执行价格
     */
    private Double DiscountPrice;
    private DishesE DishesE;
    /**
     * 可点数量
     */
    private Double maxValue;
    private Double tempMaxValue;

    /**
     * 结算数量(上菜完成数量，结算金额依此数量)
     */
    private Double CheckCount;
    /**
     * 本菜品特殊要求，见定义
     */
    private List<DishesRemarkE> ArrayOfDishesRemarkE;

    /**
     * 本菜品做法要求，见定义
     */
    private List<DishesPracticeE> ArrayOfDishesPracticeE;

    /**
     * 此菜品为套餐项目所含的套餐子项，结构同本实体定义
     */
    private List<SubDishesE> ArrayOfSubDishesE;
    /**
     * 此菜品若为套餐项目，则此字段为套餐子项集合
     */
    private List<SalesOrderBatchDishesE> ArrayOfSalesOrderBatchDishesE;

    /**
     * 操作类型 1加菜 -1退菜
     */
    private Integer OperationType;

    /**
     * 所属批次时间  下单时间
     */
    private String BatchTime;

    /**
     * 原上菜记录的标识
     */
    private String ReturnSalesOrderBatchDishesGUID;
    /**
     * 总金额
     */
    private Double Total;
    /**
     * 做法附加费用
     */
    private Double PracticeSubTotal;
    /**
     * 总费用（加上了做法附加费用）
     */
    private Double SubTotal;

    private Integer mNumber;

    /**
     * 是否损耗（是否扣减库存）
     */
    private Integer IsMinusStock = 1;
    /**
     * 菜品编码
     */
    private String Code;
    /**
     * 名称
     */
    private String Name;
    /**
     * 拼音
     */
    private String PYIndex;
    /**
     * 简称
     */
    private String SimpleName;
    /**
     * 桌台名称
     */
    private String DTName;
    /**
     * 桌台编码
     */
    private String DTCode;
    /**
     * 菜品做法（拼接字符串）
     */
    private String DishesPracticeStr;
    /**
     * 套餐类型 0：固定套餐 1：自选套餐
     */
    private int PackageType;
    private int PackageDishesOrderCount;
    /**
     * 结算单位
     */
    private String CheckUnit;
    /**
     * 单次数量
     */
    private Double SingleCount;
    /**
     * 做法
     */
    private String PracticeNames;
    /**
     * 自菜品
     */
    private String SubDishes;
    /**
     * 浮动价
     */
    private Double RiseAmount;
    /**
     * 当前菜品是否选中
     */
    private boolean isChecked = false;

    private boolean isCheckedInAll = false;

    private boolean isWeighEnable;

    public boolean isWeighEnable() {
        return isWeighEnable;
    }

    public void setWeighEnable(boolean weighEnable) {
        isWeighEnable = weighEnable;
    }

    public boolean isCheckedInAll() {
        return isCheckedInAll;
    }

    public void setCheckedInAll(boolean checkedInAll) {
        isCheckedInAll = checkedInAll;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Double getRiseAmount() {
        return RiseAmount;
    }

    public void setRiseAmount(Double riseAmount) {
        RiseAmount = riseAmount;
    }

    public String getPracticeNames() {
        return PracticeNames;
    }

    public void setPracticeNames(String practiceNames) {
        PracticeNames = practiceNames;
    }

    public String getSubDishes() {
        return SubDishes;
    }

    public void setSubDishes(String subDishes) {
        SubDishes = subDishes;
    }

    public Double getSingleCount() {
        return SingleCount;
    }

    public void setSingleCount(Double singleCount) {
        SingleCount = singleCount;
    }

    public String getCheckUnit() {
        return CheckUnit;
    }

    public void setCheckUnit(String checkUnit) {
        CheckUnit = checkUnit;
    }

    public int getPackageDishesOrderCount() {
        return PackageDishesOrderCount;
    }

    public void setPackageDishesOrderCount(int packageDishesOrderCount) {
        PackageDishesOrderCount = packageDishesOrderCount;
    }

    public static Creator<SalesOrderBatchDishesE> getCREATOR() {
        return CREATOR;
    }

    public int getPackageType() {
        return PackageType;
    }

    public void setPackageType(int packageType) {
        PackageType = packageType;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPYIndex() {
        return PYIndex;
    }

    public void setPYIndex(String PYIndex) {
        this.PYIndex = PYIndex;
    }

    public String getSimpleName() {
        return SimpleName;
    }

    public void setSimpleName(String simpleName) {
        SimpleName = simpleName;
    }

    public String getDTName() {
        return DTName;
    }

    public void setDTName(String DTName) {
        this.DTName = DTName;
    }

    public String getDTCode() {
        return DTCode;
    }

    public void setDTCode(String DTCode) {
        this.DTCode = DTCode;
    }

    public String getDishesPracticeStr() {
        return DishesPracticeStr;
    }

    public void setDishesPracticeStr(String dishesPracticeStr) {
        DishesPracticeStr = dishesPracticeStr;
    }

    public Double getTempOrderCount() {
        return tempOrderCount;
    }

    public void setTempOrderCount(Double tempOrderCount) {
        this.tempOrderCount = tempOrderCount;
    }

    public Integer getNumber() {
        return mNumber;
    }

    public void setNumber(Integer number) {
        mNumber = number;
    }

    public Integer getmNumber() {
        return mNumber;
    }

    public void setmNumber(Integer mNumber) {
        this.mNumber = mNumber;
    }

    public Double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(Double subTotal) {
        SubTotal = subTotal;
    }

    public Double getPracticeSubTotal() {
        return PracticeSubTotal;
    }

    public void setPracticeSubTotal(Double practiceSubTotal) {
        PracticeSubTotal = practiceSubTotal;
    }

    public Boolean getNew() {
        return isNew;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public List<SalesOrderBatchDishesE> getArrayOfSalesOrderBatchDishesE() {
        return ArrayOfSalesOrderBatchDishesE;
    }

    public void setArrayOfSalesOrderBatchDishesE(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
        ArrayOfSalesOrderBatchDishesE = arrayOfSalesOrderBatchDishesE;
    }

    public List<DishesRemarkE> getArrayOfDishesRemarkE() {
        return ArrayOfDishesRemarkE;
    }

    public void setArrayOfDishesRemarkE(List<DishesRemarkE> arrayOfDishesRemarkE) {
        ArrayOfDishesRemarkE = arrayOfDishesRemarkE;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public List<DishesPracticeE> getArrayOfDishesPracticeE() {
        return ArrayOfDishesPracticeE;
    }

    public void setArrayOfDishesPracticeE(List<DishesPracticeE> arrayOfDishesPracticeE) {
        ArrayOfDishesPracticeE = arrayOfDishesPracticeE;
    }

    public List<SubDishesE> getArrayOfSubDishesE() {
        return ArrayOfSubDishesE;
    }

    public void setArrayOfSubDishesE(List<SubDishesE> arrayOfSubDishesE) {
        ArrayOfSubDishesE = arrayOfSubDishesE;
    }

    public Double getReviewCheckCount() {
        return ReviewCheckCount;
    }

    public void setReviewCheckCount(Double reviewCheckCount) {
        ReviewCheckCount = reviewCheckCount;
    }

    public Double getCheckCount() {
        return CheckCount;
    }

    public void setCheckCount(Double checkCount) {
        CheckCount = checkCount;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public DishesE getDishesE() {
        return DishesE;
    }

    public void setDishesE(DishesE dishesE) {
        DishesE = dishesE;
    }

    public Boolean isNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Integer getOperationType() {
        return OperationType;
    }

    public void setOperationType(Integer operationType) {
        OperationType = operationType;
    }

    public String getBatchTime() {
        return BatchTime;
    }

    public void setBatchTime(String batchTime) {
        BatchTime = batchTime;
    }

    public String getReturnSalesOrderBatchDishesGUID() {
        return ReturnSalesOrderBatchDishesGUID;
    }

    public void setReturnSalesOrderBatchDishesGUID(String returnSalesOrderBatchDishesGUID) {
        ReturnSalesOrderBatchDishesGUID = returnSalesOrderBatchDishesGUID;
    }

    public Double getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        DiscountPrice = discountPrice;
    }

    public Double getTempMaxValue() {
        return tempMaxValue;
    }

    public void setTempMaxValue(Double tempMaxValue) {
        this.tempMaxValue = tempMaxValue;
    }

    public Integer getIsMinusStock() {
        return IsMinusStock;
    }

    public void setIsMinusStock(Integer isMinusStock) {
        IsMinusStock = isMinusStock;
    }

    public SalesOrderBatchDishesE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.tempOrderCount);
        dest.writeValue(this.isNew);
        dest.writeString(this.dishesUnit);
        dest.writeString(this.dishesName);
        dest.writeValue(this.ReviewCheckCount);
        dest.writeValue(this.DiscountPrice);
        dest.writeParcelable(this.DishesE, flags);
        dest.writeValue(this.maxValue);
        dest.writeValue(this.tempMaxValue);
        dest.writeValue(this.CheckCount);
        dest.writeTypedList(this.ArrayOfDishesRemarkE);
        dest.writeTypedList(this.ArrayOfDishesPracticeE);
        dest.writeList(this.ArrayOfSubDishesE);
        dest.writeTypedList(this.ArrayOfSalesOrderBatchDishesE);
        dest.writeValue(this.OperationType);
        dest.writeString(this.BatchTime);
        dest.writeString(this.ReturnSalesOrderBatchDishesGUID);
        dest.writeValue(this.Total);
        dest.writeValue(this.PracticeSubTotal);
        dest.writeValue(this.SubTotal);
        dest.writeValue(this.mNumber);
        dest.writeValue(this.IsMinusStock);
        dest.writeString(this.Code);
        dest.writeString(this.Name);
        dest.writeString(this.PYIndex);
        dest.writeString(this.SimpleName);
        dest.writeString(this.DTName);
        dest.writeString(this.DTCode);
        dest.writeString(this.DishesPracticeStr);
        dest.writeInt(this.PackageType);
        dest.writeInt(this.PackageDishesOrderCount);
        dest.writeString(this.CheckUnit);
        dest.writeValue(this.SingleCount);
        dest.writeString(this.PracticeNames);
        dest.writeString(this.SubDishes);
        dest.writeValue(this.RiseAmount);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCheckedInAll ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWeighEnable ? (byte) 1 : (byte) 0);
    }

    protected SalesOrderBatchDishesE(Parcel in) {
        super(in);
        this.tempOrderCount = (Double) in.readValue(Double.class.getClassLoader());
        this.isNew = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.dishesUnit = in.readString();
        this.dishesName = in.readString();
        this.ReviewCheckCount = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.DishesE = in.readParcelable(com.holderzone.intelligencepos.mvp.model.bean.DishesE.class.getClassLoader());
        this.maxValue = (Double) in.readValue(Double.class.getClassLoader());
        this.tempMaxValue = (Double) in.readValue(Double.class.getClassLoader());
        this.CheckCount = (Double) in.readValue(Double.class.getClassLoader());
        this.ArrayOfDishesRemarkE = in.createTypedArrayList(DishesRemarkE.CREATOR);
        this.ArrayOfDishesPracticeE = in.createTypedArrayList(DishesPracticeE.CREATOR);
        this.ArrayOfSubDishesE = new ArrayList<SubDishesE>();
        in.readList(this.ArrayOfSubDishesE, SubDishesE.class.getClassLoader());
        this.ArrayOfSalesOrderBatchDishesE = in.createTypedArrayList(SalesOrderBatchDishesE.CREATOR);
        this.OperationType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.BatchTime = in.readString();
        this.ReturnSalesOrderBatchDishesGUID = in.readString();
        this.Total = (Double) in.readValue(Double.class.getClassLoader());
        this.PracticeSubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.SubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.mNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsMinusStock = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Code = in.readString();
        this.Name = in.readString();
        this.PYIndex = in.readString();
        this.SimpleName = in.readString();
        this.DTName = in.readString();
        this.DTCode = in.readString();
        this.DishesPracticeStr = in.readString();
        this.PackageType = in.readInt();
        this.PackageDishesOrderCount = in.readInt();
        this.CheckUnit = in.readString();
        this.SingleCount = (Double) in.readValue(Double.class.getClassLoader());
        this.PracticeNames = in.readString();
        this.SubDishes = in.readString();
        this.RiseAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.isChecked = in.readByte() != 0;
        this.isCheckedInAll = in.readByte() != 0;
        this.isWeighEnable = in.readByte() != 0;
    }

    public static final Creator<SalesOrderBatchDishesE> CREATOR = new Creator<SalesOrderBatchDishesE>() {
        @Override
        public SalesOrderBatchDishesE createFromParcel(Parcel source) {
            return new SalesOrderBatchDishesE(source);
        }

        @Override
        public SalesOrderBatchDishesE[] newArray(int size) {
            return new SalesOrderBatchDishesE[size];
        }
    };
}
