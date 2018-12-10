package com.holderzone.intelligencepos.mvp.model.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 附加费实体
 * Created by tcw on 2017/4/12.
 */
@Entity(nameInDb = "AdditionalFees")
public class AdditionalFees {

    /**
     * 数据标识
     */
    @Property(nameInDb = "AdditionalFeesGUID")
    private String AdditionalFeesGUID;

    /**
     * 附加费名称
     */
    @Property(nameInDb = "Name")
    private String Name;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 附加费金额
     */
    @Property(nameInDb = "Amount")
    private Double Amount;

    /**
     * 序号
     */
    @Property(nameInDb = "Sort")
    private Integer Sort;

    public AdditionalFees() {
    }

    @Keep
    @Generated(hash = 61078477)
    public AdditionalFees(String AdditionalFeesGUID, String Name, Integer count,
            Double Amount, Integer Sort) {
        this.AdditionalFeesGUID = AdditionalFeesGUID;
        this.Name = Name;
        this.count = count;
        this.Amount = Amount;
        this.Sort = Sort;
    }

    public String getAdditionalFeesGUID() {
        return AdditionalFeesGUID;
    }

    public void setAdditionalFeesGUID(String additionalFeesGUID) {
        AdditionalFeesGUID = additionalFeesGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Integer getSort() {
        return Sort;
    }

    public void setSort(Integer sort) {
        Sort = sort;
    }
}
