package com.holderzone.intelligencepos.mvp.model.bean;

import com.holderzone.intelligencepos.mvp.model.bean.db.Users;

import java.util.List;

/**
 * 销售单菜品变动批记录次表
 * Created by Administrator on 2016/10/31.
 */
public class SalesOrderBatch extends OrderOperatorBaseBean {
    /**数据标识*/
    private String SalesOrderBatchGUID ;

    /**主表标识*/
    private String SalesOrderGUID ;

    private SalesOrder SalesOrder ;

    /**桌子标识*/

    private String DiningTableGUID ;

    private DiningTable DiningTable ;

    /**批次流水号，由yyMMdd+6位流水组成*/

    private String BatchNumber ;

    /**批次操作人员标识*/

    private String StaffGUID ;

    /**操作类型  1=加菜  -1=退菜 0=获取全部数据  */

    private int OperationType ;

    /**制作出堂状态*/

    private int MakeComplete ;

    /**批次序号*/

    private int BatchIndex ;


    /**激活状态 0=未激活，不进入内堂  1=激活，进入内堂*/

    private int Active ;

    /**批次下的菜品*/
    private List<SalesOrderBatchDishes> ArrayOfSalesOrderBatchDishes ;

    /**批次下单人*/
    private Users StaffUsers;

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

    public String getBatchNumber() {
        return BatchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        BatchNumber = batchNumber;
    }

    public String getStaffGUID() {
        return StaffGUID;
    }

    public void setStaffGUID(String staffGUID) {
        StaffGUID = staffGUID;
    }

    public int getOperationType() {
        return OperationType;
    }

    public void setOperationType(int operationType) {
        OperationType = operationType;
    }

    public int getMakeComplete() {
        return MakeComplete;
    }

    public void setMakeComplete(int makeComplete) {
        MakeComplete = makeComplete;
    }

    public int getBatchIndex() {
        return BatchIndex;
    }

    public void setBatchIndex(int batchIndex) {
        BatchIndex = batchIndex;
    }

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }

    public List<SalesOrderBatchDishes> getArrayOfSalesOrderBatchDishes() {
        return ArrayOfSalesOrderBatchDishes;
    }

    public void setArrayOfSalesOrderBatchDishes(List<SalesOrderBatchDishes> arrayOfSalesOrderBatchDishes) {
        ArrayOfSalesOrderBatchDishes = arrayOfSalesOrderBatchDishes;
    }

    public Users getStaffUsers() {
        return StaffUsers;
    }

    public void setStaffUsers(Users staffUsers) {
        StaffUsers = staffUsers;
    }
}
