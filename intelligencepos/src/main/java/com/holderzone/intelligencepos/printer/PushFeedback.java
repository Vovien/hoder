package com.holderzone.intelligencepos.printer;

/**
 * Created by zhaoping on 2018/7/9.
 */
public class PushFeedback {
    private String PrintKey;
    /**
     * 获取耗时
     */
    private Long ObtainTime;
    /**
     * 打印耗时
     */
    private Long PrintConsumeTime;
    /**
     * 实际打印次数
     */
    private Integer ShouldPrintCount;
    /**
     * 应打次数
     */
    private Integer ActualPrintCount;

    public String getPrintKey() {
        return PrintKey;
    }

    public void setPrintKey(String printKey) {
        PrintKey = printKey;
    }

    public Long getObtainTime() {
        return ObtainTime;
    }

    public void setObtainTime(Long obtainTime) {
        ObtainTime = obtainTime;
    }

    public Long getPrintConsumeTime() {
        return PrintConsumeTime;
    }

    public void setPrintConsumeTime(Long printConsumeTime) {
        PrintConsumeTime = printConsumeTime;
    }

    public Integer getShouldPrintCount() {
        return ShouldPrintCount;
    }

    public void setShouldPrintCount(Integer shouldPrintCount) {
        ShouldPrintCount = shouldPrintCount;
    }

    public Integer getActualPrintCount() {
        return ActualPrintCount;
    }

    public void setActualPrintCount(Integer actualPrintCount) {
        ActualPrintCount = actualPrintCount;
    }
}
