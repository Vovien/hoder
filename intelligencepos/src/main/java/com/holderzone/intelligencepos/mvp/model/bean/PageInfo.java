package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/3/10.
 */

public class PageInfo {
    /**
     * 每页记录数
     */
    private Integer PageRowCount;
    /**
     * 当前页索引号
     */
    private Integer CurrentPage;
    /**
     * 总记录行数
     */
    private Integer TotalRowCount;
    /**
     * 总页数
     */
    private Integer TotalPage;

    public Integer getPageRowCount() {
        return PageRowCount;
    }

    public void setPageRowCount(Integer pageRowCount) {
        PageRowCount = pageRowCount;
    }

    public Integer getCurrentPage() {
        return CurrentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        CurrentPage = currentPage;
    }

    public Integer getTotalRowCount() {
        return TotalRowCount;
    }

    public void setTotalRowCount(Integer totalRowCount) {
        TotalRowCount = totalRowCount;
    }

    public Integer getTotalPage() {
        return TotalPage;
    }

    public void setTotalPage(Integer totalPage) {
        TotalPage = totalPage;
    }
}
