package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by xiaojw on 2016/9/9.
 */
public class LiveSeachItem{
    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private int totalRows;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<LiveRowsBean> getRows() {
        return rows;
    }

    public void setRows(List<LiveRowsBean> rows) {
        this.rows = rows;
    }

    private List<LiveRowsBean> rows;

}
