package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

import com.holderzone.intelligencepos.mvp.model.bean.db.Users;

/**
 * 说明：
 * 截至2017-02-17，此实体在数据库中无对应表，仅用于数据交换使用，用于退菜记录分组装载，分组依据是按操作时间
 */

public class SalesOrderDishesBackBatch  extends OrderOperatorBaseBean {

    /**
     * 标识
     */
    public String SalesOrderDishesBackBatchGUID;
    /**
     * 操作员
     */
    public String StaffGUID;

    public Users StaffUsers;

    public String Remark;
    /**
     * 退菜记录
     */
    public List<SalesOrderDishesBack> ArrayOfSalesOrderDishesBack;
}
