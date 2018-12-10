package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by zhaoping on 2018/9/11.
 */
public class SalesOrderBatchN {

    private String SalesOrderBatchGUID;
    private Double Total;
    private Integer OperationType;
    private String BatchTime;
    private String ReturnReasonStr;
    private List<SalesOrderBatchDishesN> ArrayOfSalesOrderBatchDishesN;

    public String getSalesOrderBatchGUID() {
        return SalesOrderBatchGUID;
    }

    public void setSalesOrderBatchGUID(String salesOrderBatchGUID) {
        SalesOrderBatchGUID = salesOrderBatchGUID;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
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

    public String getReturnReasonStr() {
        return ReturnReasonStr;
    }

    public void setReturnReasonStr(String returnReasonStr) {
        ReturnReasonStr = returnReasonStr;
    }

    public List<SalesOrderBatchDishesN> getArrayOfSalesOrderBatchDishesN() {
        return ArrayOfSalesOrderBatchDishesN;
    }

    public void setArrayOfSalesOrderBatchDishesN(List<SalesOrderBatchDishesN> arrayOfSalesOrderBatchDishesN) {
        ArrayOfSalesOrderBatchDishesN = arrayOfSalesOrderBatchDishesN;
    }
}
